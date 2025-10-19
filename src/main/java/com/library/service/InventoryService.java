package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.BranchNotFoundException;
import com.library.exception.InvalidOperationException;
import com.library.model.Book;
import com.library.model.LibraryBranch;
import com.library.repository.BookRepository;
import com.library.repository.BranchRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class InventoryService {
    private static final Logger logger = Logger.getLogger(InventoryService.class.getName());
    private BranchRepository branchRepository;
    private BookRepository bookRepository;

    public InventoryService() {
        this.branchRepository = new BranchRepository();
        this.bookRepository = new BookRepository();
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public LibraryBranch createBranch(String name, String location) {
        String branchId = generateBranchId();
        LibraryBranch branch = new LibraryBranch(branchId, name, location);
        branchRepository.save(branch);
        logger.info("Branch created: " + branch.getName() + " at " + branch.getLocation());
        return branch;
    }

    public LibraryBranch getBranchById(String branchId) {
        LibraryBranch branch = branchRepository.findById(branchId);
        if (branch == null) {
            throw new BranchNotFoundException("Branch not found: " + branchId);
        }
        return branch;
    }

    public List<LibraryBranch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Map<String, Integer> trackInventory() {
        Map<String, Integer> inventory = new HashMap<>();
        List<Book> allBooks = bookRepository.findAll();

        inventory.put("Total Books", allBooks.size());
        inventory.put("Available", (int) allBooks.stream().filter(Book::isAvailable).count());
        inventory.put("Borrowed", (int) allBooks.stream()
                .filter(b -> b.getStatus() == Book.BookStatus.BORROWED).count());
        inventory.put("Reserved", (int) allBooks.stream()
                .filter(b -> b.getStatus() == Book.BookStatus.RESERVED).count());

        logger.info("Inventory tracked: " + inventory);
        return inventory;
    }

    public Book.BookStatus getBookStatus(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }
        return book.getStatus();
    }

    public void transferBookBetweenBranches(String isbn, String fromBranchId, String toBranchId) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book not found: " + isbn);
        }

        LibraryBranch fromBranch = branchRepository.findById(fromBranchId);
        if (fromBranch == null) {
            throw new BranchNotFoundException("Source branch not found: " + fromBranchId);
        }

        LibraryBranch toBranch = branchRepository.findById(toBranchId);
        if (toBranch == null) {
            throw new BranchNotFoundException("Destination branch not found: " + toBranchId);
        }

        if (!book.getBranchId().equals(fromBranchId)) {
            throw new InvalidOperationException("Book is not in the source branch");
        }

        if (!book.isAvailable()) {
            throw new InvalidOperationException("Cannot transfer book that is not available");
        }

        // Update book's branch
        book.setBranchId(toBranchId);
        book.setStatus(Book.BookStatus.IN_TRANSIT);
        bookRepository.save(book);

        logger.info("Book transferred: " + book.getTitle() + " from " + 
                    fromBranch.getName() + " to " + toBranch.getName());

        // Simulate transit completion
        book.setStatus(Book.BookStatus.AVAILABLE);
        bookRepository.save(book);
    }

    public Map<String, Integer> getBranchInventory(String branchId) {
        LibraryBranch branch = getBranchById(branchId);
        List<Book> branchBooks = bookRepository.findByBranch(branchId);

        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Total Books", branchBooks.size());
        inventory.put("Available", (int) branchBooks.stream().filter(Book::isAvailable).count());
        inventory.put("Borrowed", (int) branchBooks.stream()
                .filter(b -> b.getStatus() == Book.BookStatus.BORROWED).count());

        return inventory;
    }

    private String generateBranchId() {
        return "BRANCH-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 100);
    }
}