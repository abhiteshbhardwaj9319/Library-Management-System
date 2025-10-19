package com.library.model;

import java.time.Year;
import java.util.Objects;

public class Book {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private Year publicationYear;
    private BookStatus status;
    private String branchId;

    public enum BookStatus {
        AVAILABLE, BORROWED, RESERVED, IN_TRANSIT
    }

    public Book(String id, String title, String author, String isbn, Year publicationYear, String branchId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.status = BookStatus.AVAILABLE;
        this.branchId = branchId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Year getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Year publicationYear) { this.publicationYear = publicationYear; }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public String getBranchId() { return branchId; }
    public void setBranchId(String branchId) { this.branchId = branchId; }
    public boolean isAvailable() { return status == BookStatus.AVAILABLE; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() { return Objects.hash(isbn); }

    @Override
    public String toString() {
        return "Book{" + "id='" + id + '\'' + ", title='" + title + '\'' + ", author='" + author + '\'' + ", isbn='" + isbn + '\'' + ", publicationYear=" + publicationYear + ", status=" + status + ", branchId='" + branchId + '\'' + '}';
    }
}