 
package com.library.pattern.strategy;

import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import java.util.*;
import java.util.stream.Collectors;

public class CollaborativeFilteringStrategy implements RecommendationStrategy {
    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, int limit) {
        // Simple collaborative filtering: recommend popular books the patron hasn't borrowed
        Set<String> borrowedBookIds = patron.getBorrowingHistory().stream()
                .map(BorrowingRecord::getBookId)
                .collect(Collectors.toSet());

        // Simulate popularity by shuffling and returning books not yet borrowed
        List<Book> candidates = allBooks.stream()
                .filter(book -> !borrowedBookIds.contains(book.getId()) && book.isAvailable())
                .collect(Collectors.toList());

        Collections.shuffle(candidates);
        return candidates.stream().limit(limit).collect(Collectors.toList());
    }
}
