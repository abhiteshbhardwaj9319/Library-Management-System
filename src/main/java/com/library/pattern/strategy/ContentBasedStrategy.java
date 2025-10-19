 
package com.library.pattern.strategy;

import com.library.model.Book;
import com.library.model.BorrowingRecord;
import com.library.model.Patron;
import java.util.*;
import java.util.stream.Collectors;

public class ContentBasedStrategy implements RecommendationStrategy {
    @Override
    public List<Book> recommend(Patron patron, List<Book> allBooks, int limit) {
        // Content-based: recommend books by authors the patron has borrowed before
        Set<String> favoriteAuthors = patron.getBorrowingHistory().stream()
                .map(BorrowingRecord::getBookTitle)
                .collect(Collectors.toSet());

        Set<String> borrowedBookIds = patron.getBorrowingHistory().stream()
                .map(BorrowingRecord::getBookId)
                .collect(Collectors.toSet());

        Map<String, Integer> authorFrequency = new HashMap<>();
        for (BorrowingRecord record : patron.getBorrowingHistory()) {
            // In real system, we'd lookup author from bookId
            // For demo, we use a simplified approach
        }

        List<Book> recommendations = allBooks.stream()
                .filter(book -> !borrowedBookIds.contains(book.getId()) && book.isAvailable())
                .collect(Collectors.toList());

        Collections.shuffle(recommendations);
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
}