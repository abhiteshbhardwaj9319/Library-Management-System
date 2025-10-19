package com.library.repository;

import com.library.model.Transaction;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository {
    private List<Transaction> transactions;

    public TransactionRepository() {
        this.transactions = new ArrayList<>();
    }

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public Transaction findById(String transactionId) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId().equals(transactionId)) {
                return transaction;
            }
        }
        return null;
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> findByPatronId(String patronId) {
        return transactions.stream()
                .filter(t -> t.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    public List<Transaction> findActiveTransactions() {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public Transaction findActiveTransactionByBookId(String bookId) {
        for (Transaction transaction : transactions) {
            if (transaction.getBookId().equals(bookId) && 
                transaction.getStatus() == Transaction.TransactionStatus.ACTIVE) {
                return transaction;
            }
        }
        return null;
    }
}