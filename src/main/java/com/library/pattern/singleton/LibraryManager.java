package com.library.pattern.singleton;

import com.library.service.*;
import java.util.logging.Logger;

public class LibraryManager {
    private static final Logger logger = Logger.getLogger(LibraryManager.class.getName());
    private static volatile LibraryManager instance;
    private BookService bookService;
    private PatronService patronService;
    private LendingService lendingService;
    private InventoryService inventoryService;
    private LibraryManager() {
        logger.info("Initializing Library Management System...");
        this.bookService = new BookService();
        this.patronService = new PatronService();
        this.lendingService = new LendingService();
        this.inventoryService = new InventoryService();
    }
    public static LibraryManager getInstance() {
        if (instance == null) {
            synchronized (LibraryManager.class) {
                if (instance == null) {
                    instance = new LibraryManager();
                }
            }
        }
        return instance;
    }

    public BookService getBookService() { return bookService; }
    public PatronService getPatronService() { return patronService; }
    public LendingService getLendingService() { return lendingService; }
    public InventoryService getInventoryService() { return inventoryService; }
}
