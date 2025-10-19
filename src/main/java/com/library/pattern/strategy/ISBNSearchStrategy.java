
package com.library.pattern.strategy;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;

public class ISBNSearchStrategy implements SearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String query) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(query)) {
                results.add(book);
            }
        }
        return results;
    }
}
