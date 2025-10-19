package com.library.pattern.factory;

import com.library.model.Book;
import java.time.Year;

public class LibraryItemFactory {
    
    public static Book createBook(String id, String title, String author, String isbn, Year publicationYear, String branchId) {
        return new Book(id, title, author, isbn, publicationYear, branchId);
    }

    public static Book createBook(String title, String author, String isbn, Year publicationYear, String branchId) {
        String id = generateBookId();
        return new Book(id, title, author, isbn, publicationYear, branchId);
    }

    private static String generateBookId() {
        return "BOOK-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}