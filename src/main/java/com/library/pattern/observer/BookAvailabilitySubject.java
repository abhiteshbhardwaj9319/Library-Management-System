package com.library.pattern.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BookAvailabilitySubject implements Subject {
    private static final Logger logger = Logger.getLogger(BookAvailabilitySubject.class.getName());
    private List<Observer> observers;
    private String bookId;

    public BookAvailabilitySubject(String bookId) {
        this.bookId = bookId;
        this.observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
        logger.info("Observer attached for book: " + bookId);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
        logger.info("Observer detached for book: " + bookId);
    }

    @Override
    public void notifyObservers(String message) {
        logger.info("Notifying " + observers.size() + " observers for book: " + bookId);
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void bookBecameAvailable(String bookTitle) {
        String message = "Good news! The book '" + bookTitle + "' is now available for checkout.";
        notifyObservers(message);
    }
}
