package com.library.service;

import com.library.exception.*;
import com.library.model.*;
import com.library.pattern.observer.BookAvailabilitySubject;
import com.library.pattern.observer.PatronObserver;
import com.library.repository.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LendingService {
    private static final Logger logger = Logger.getLogger(LendingService.class.getName());
    private static final int LENDING_PERIOD_DAYS = 14;
    private static final double FINE_PER_DAY = 0.50;

    private TransactionRepository transactionRepository;
    private ReservationRepository reservationRepository;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;
    private Map<String, BookAvailabilitySubject> notificationSubjects;

    public LendingService() {
        this.transactionRepository = new TransactionRepository();
        this.reservationRepository = new ReservationRepository();
        this.bookRepository = new BookRepository();
        this.patronRepository = new PatronRepository();
        this.notificationSubjects = new HashMap<>();
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setPatronRepository(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public Transaction checkoutBook(String patronId, String isbn) {
        Patron patron = patronRepository.findById(patronId);
        if(patron == null){
            throw new PatronNotFoundException("Patron not found: " + patronId);
        }

        if(!patron.isActive()){
            throw new InvalidOperationException("Patron account is not active");
        }

        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }

        if (!book.isAvailable()) {
            throw new BookAlreadyBorrowedException("Book is not available: " + book.getTitle());
        }

        // Create transaction
        String transactionId = generateTransactionId();
        LocalDate checkoutDate = LocalDate.now();
        LocalDate dueDate = checkoutDate.plusDays(LENDING_PERIOD_DAYS);
        
        Transaction transaction = new Transaction(transactionId, patronId, book.getId(), 
                                                   checkoutDate, dueDate);
        transactionRepository.save(transaction);

        // Update book status
        book.setStatus(Book.BookStatus.BORROWED);
        bookRepository.save(book);

        // Add to patron's borrowing history
        BorrowingRecord record = new BorrowingRecord(book.getId(), book.getTitle(), 
                                                      checkoutDate, null);
        patron.addBorrowingRecord(record);
        patronRepository.save(patron);

        logger.info("Book checked out: " + book.getTitle() + " by " + patron.getName());
        return transaction;
    }

    public void returnBook(String patronId, String isbn) {
        Patron patron = patronRepository.findById(patronId);
        if (patron == null) {
            throw new PatronNotFoundException("Patron not found: " + patronId);
        }

        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }

        Transaction transaction = transactionRepository.findActiveTransactionByBookId(book.getId());
        if (transaction == null) {
            throw new InvalidOperationException("No active transaction found for this book");
        }

        if (!transaction.getPatronId().equals(patronId)) {
            throw new InvalidOperationException("This book was not borrowed by this patron");
        }

        // Update transaction
        transaction.setReturnDate(LocalDate.now());
        transaction.setStatus(Transaction.TransactionStatus.RETURNED);

        // Check if overdue
        if (transaction.isOverdue()) {
            double fine = calculateFine(transaction);
            logger.warning("Book returned late. Fine: $" + fine);
            // System.out.println("DEBUG: Fine calculated: " + fine); // debugging
        }

        // Check for reservations
        Reservation nextReservation = reservationRepository.getNextReservation(book.getId());
        if (nextReservation != null) {
            // Book is reserved, notify patron
            book.setStatus(Book.BookStatus.RESERVED);
            nextReservation.setStatus(Reservation.ReservationStatus.FULFILLED);
            reservationRepository.save(nextReservation);
            
            notifyPatronBookAvailable(nextReservation.getPatronId(), book);
            logger.info("Book reserved for next patron in queue");
        } else {
            // Book becomes available
            book.setStatus(Book.BookStatus.AVAILABLE);
        }

        bookRepository.save(book);
        logger.info("Book returned: " + book.getTitle() + " by " + patron.getName());
    }

    public Reservation reserveBook(String patronId, String isbn, int priority) {
        Patron patron = patronRepository.findById(patronId);
        if (patron == null) {
            throw new PatronNotFoundException("Patron not found: " + patronId);
        }

        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }

        if (book.isAvailable()) {
            throw new InvalidOperationException("Book is available. Please checkout directly.");
        }

        String reservationId = generateReservationId();
        Reservation reservation = new Reservation(reservationId, patronId, book.getId(), priority);
        reservationRepository.save(reservation);

        // Attach observer for notification
        attachObserver(book.getId(), patron);

        logger.info("Book reserved: " + book.getTitle() + " for " + patron.getName());
        return reservation;
    }

    public double calculateFine(String patronId) {
        List<Transaction> transactions = transactionRepository.findByPatronId(patronId);
        double totalFine = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.isOverdue()) {
                totalFine += calculateFine(transaction);
            }
        }

        return totalFine;
    }

    private double calculateFine(Transaction transaction) {
        long overdueDays = LocalDate.now().toEpochDay() - transaction.getDueDate().toEpochDay();
        return overdueDays * FINE_PER_DAY;
    }

    private void attachObserver(String bookId, Patron patron) {
        BookAvailabilitySubject subject = notificationSubjects.computeIfAbsent(
            bookId, k -> new BookAvailabilitySubject(bookId)
        );
        subject.attach(new PatronObserver(patron));
    }

    private void notifyPatronBookAvailable(String patronId, Book book) {
        BookAvailabilitySubject subject = notificationSubjects.get(book.getId());
        if (subject != null) {
            subject.bookBecameAvailable(book.getTitle());
        }
    }

    public List<Reservation> getPatronReservations(String patronId) {
        return reservationRepository.findByPatronId(patronId);
    }

    public List<Transaction> getActiveTransactions() {
        return transactionRepository.findActiveTransactions();
    }

    private String generateTransactionId() {
        // TODO: Use proper UUID instead of this hack
        return "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    private String generateReservationId() {
        // FIXME: This might cause collisions
        return "RES-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}