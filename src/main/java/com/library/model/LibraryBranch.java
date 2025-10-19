package com.library.model;

import java.util.*;

public class LibraryBranch {
    private String branchId;
    private String name;
    private String location;
    private Map<String, Book> inventory;

    public LibraryBranch(String branchId, String name, String location) {
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.inventory = new HashMap<>();
    }

    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public void addBook(Book book) {
        inventory.put(book.getIsbn(), book);
        book.setBranchId(this.branchId);
    }

    public Book removeBook(String isbn) {
        return inventory.remove(isbn);
    }
    public Book getBook(String isbn) {
        return inventory.get(isbn);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(inventory.values());
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : inventory.values()) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }

    @Override
    public String toString() {
        return "LibraryBranch{" + "branchId='" + branchId + '\'' + ", name='" + name + '\'' +", location='" + location + '\'' + ", totalBooks=" + inventory.size() + '}';
    }
}
