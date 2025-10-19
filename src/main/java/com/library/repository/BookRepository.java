package com.library.repository;

import com.library.model.Book;
import java.util.*;

public class BookRepository {
    private Map<String, Book> books;

    public BookRepository() {
        this.books = new HashMap<>();
    }

    public void save(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book findByIsbn(String isbn) {
        return books.get(isbn);
    }

    public Book findById(String id) {
        for (Book book : books.values()) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public void delete(String isbn) {
        books.remove(isbn);
    }

    public List<Book> findByBranch(String branchId) {
        List<Book> branchBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getBranchId().equals(branchId)) {
                branchBooks.add(book);
            }
        }
        return branchBooks;
    }

    public List<Book> findAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }
}