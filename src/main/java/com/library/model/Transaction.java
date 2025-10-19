package com.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {
    private String transactionId;
    private String patronId;
    private String bookId;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private TransactionStatus status;

    public enum TransactionStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    public Transaction(String transactionId, String patronId, String bookId, LocalDate checkoutDate, LocalDate dueDate) {
        this.transactionId = transactionId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.status = TransactionStatus.ACTIVE;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getPatronId() { return patronId; }
    public void setPatronId(String patronId) { this.patronId = patronId; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(LocalDate checkoutDate) { this.checkoutDate = checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() { return Objects.hash(transactionId); }

    @Override
    public String toString() {
        return "Transaction{" + "transactionId='" + transactionId + '\'' + ", patronId='" + patronId + '\'' +", bookId='" + bookId + '\'' + ", checkoutDate=" + checkoutDate + ", dueDate=" + dueDate +  ", returnDate=" + returnDate + ", status=" + status + '}';
    }
}