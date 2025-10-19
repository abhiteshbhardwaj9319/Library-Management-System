package com.library.model;

import java.time.LocalDate;

public class BorrowingRecord {
    private String bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowingRecord(String bookId, String bookTitle, LocalDate borrowDate, LocalDate returnDate) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return "BorrowingRecord{" + "bookId='" + bookId + '\'' + ", bookTitle='" + bookTitle + '\'' +", borrowDate=" + borrowDate + ", returnDate=" + returnDate + '}';
    }
}