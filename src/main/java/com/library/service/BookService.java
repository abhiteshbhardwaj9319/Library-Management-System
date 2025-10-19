package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.model.Book;
import com.library.pattern.factory.LibraryItemFactory;
import com.library.pattern.strategy.SearchStrategy;
import com.library.repository.BookRepository;
import java.time.Year;
import java.util.List;
import java.util.logging.Logger;

public class BookService {
    private static final Logger logger = Logger.getLogger(BookService.class.getName());
    private BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepository();
    }

    public Book addBook(String title, String author, String isbn, Year publicationYear, String branchId) {
        // TODO: Add validation for duplicate ISBN
        Book book = LibraryItemFactory.createBook(title, author, isbn, publicationYear, branchId);
        bookRepository.save(book);
        logger.info("Book added: " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
        return book;
    }

    public void removeBook(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if(book == null){
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        }
        bookRepository.delete(isbn);
        logger.info("Book removed: "+book.getTitle());
    }

    public Book updateBook(Book book) {
        Book existing = bookRepository.findByIsbn(book.getIsbn());
        if (existing == null) {
            throw new BookNotFoundException("Book with ISBN " + book.getIsbn() + " not found");
        }
        bookRepository.save(book);
        logger.info("Book updated: " + book.getTitle());
        return book;
    }

    public List<Book> searchBooks(SearchStrategy strategy, String query) {
        List<Book> allBooks = bookRepository.findAll();
        List<Book> results = strategy.search(allBooks, query);
        logger.info("Search performed: Found " + results.size() + " books");
        // System.out.println("DEBUG: Search query: " + query); // debugging
        return results;
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }

    public Book getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
        }
        return book;
    }

    public Book getBookById(String id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByBranch(String branchId) {
        return bookRepository.findByBranch(branchId);
    }

    public BookRepository getRepository() {
        return bookRepository;
    }
}