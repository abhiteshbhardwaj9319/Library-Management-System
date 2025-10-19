import com.library.exception.*;
import com.library.model.*;
import com.library.pattern.singleton.LibraryManager;
import com.library.pattern.strategy.*;

import com.library.service.*;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        logger.info("=== Library Management System Demo ===\n");
        // TODO: Add error handling for missing services
        // System.out.println("DEBUG: Starting demo...");
        // Initialize Library Manager (Singleton Pattern)

        LibraryManager manager = LibraryManager.getInstance();

        BookService bookService = manager.getBookService();
        PatronService patronService = manager.getPatronService();
        LendingService lendingService = manager.getLendingService();
        InventoryService inventoryService = manager.getInventoryService();

        // Setup repositories for lending service
        lendingService.setBookRepository(bookService.getRepository());
        lendingService.setPatronRepository(patronService.getRepository());
        inventoryService.setBookRepository(bookService.getRepository());
        try {
            // Create Library Branches
            System.out.println("\n--- Creating Library Branches ---");
            LibraryBranch mainBranch = inventoryService.createBranch("Main Branch", "Downtown");
            LibraryBranch westBranch = inventoryService.createBranch("West Branch",
             "West Side");
            System.out.println("Created: " + mainBranch.getName());
            System.out.println("Created: "  +  westBranch.getName());

            // Add Books
            System.out.println("\n--- Adding Books to Inventory ---");
            Book book1 = bookService.addBook("The Great Gatsby", "F. Scott Fitzgerald",  "978-0743273565", Year.of(1925), mainBranch.getBranchId());
            Book book2 = bookService.addBook("To Kill a Mockingbird", "Harper Lee","978-0061120084", Year.of(1960), mainBranch.getBranchId());
            Book book3 = bookService.addBook("1984", "George Orwell", "978-0451524935", Year.of(1949), westBranch.getBranchId());
            Book book4 = bookService.addBook("Pride and Prejudice", "Jane Austen", 
                "978-0141439518", Year.of(1813), mainBranch.getBranchId());
            Book book5 = bookService.addBook("The Catcher in the Rye", "J.D. Salinger", "978-0316769174", Year.of(1951), westBranch.getBranchId());
        
            System.out.println("Added 5 books to the library");
            // System.out.println("DEBUG: Book IDs: " + book1.getId() + ", " + book2.getId());
            // Register Patrons
            System.out.println("\n--- Registering Patrons ---");
            Patron patron1 = patronService.registerPatron("Alice Johnson","alice@email.com", "555-0101");
            Patron patron2 = patronService.registerPatron("Bob Smith", "bob@email.com", "555-0102");
            Patron patron3 = patronService.registerPatron("Carol White","carol@email.com", "555-0103");
            
            System.out.println("Registered: " + patron1.getName());
            System.out.println("Registered: "+patron2.getName());
            System.out.println("Registered: " + patron3.getName());

            // Search Books
            System.out.println("\n--- Searching for Books ---");
            SearchStrategy titleSearch = new TitleSearchStrategy();
            List<Book> searchResults = bookService.searchBooks(titleSearch, "the");
            System.out.println("Books with 'the' in title: " + searchResults.size());
            for(Book book : searchResults) {
                System.out.println("  - " + book.getTitle() + " by " + book.getAuthor());
            }
            SearchStrategy authorSearch = new AuthorSearchStrategy();
            searchResults = bookService.searchBooks(authorSearch, "Orwell");
            System.out.println("\nBooks by Orwell: " + searchResults.size());
            for (Book book : searchResults) {
                System.out.println("  - " + book.getTitle());
            }

            // Checkout Books
            System.out.println("\n--- Checking Out Books ---");
            Transaction txn1 = lendingService.checkoutBook(patron1.getId(),book1.getIsbn());
            System.out.println("Alice borrowed: " + book1.getTitle());
            Transaction txn2 = lendingService.checkoutBook(patron2.getId(), book2.getIsbn());
            System.out.println("Bob borrowed: "+book2.getTitle());

            // Inventory Tracking
            System.out.println("\n--- Tracking Inventory ---");
            Map<String, Integer> inventory = inventoryService.trackInventory();
            System.out.println("Total Books: " + inventory.get("Total Books"));
            System.out.println("Available: " + inventory.get("Available"));
            System.out.println("Borrowed: " + inventory.get("Borrowed"));

                 // Reservation System
            System.out.println("\n--- Reservation System ---");
            try{
                // Try to reserve an available book (should fail)
                lendingService.reserveBook(patron3.getId(), book3.getIsbn(), 1);
            }catch(InvalidOperationException e){
                System.out.println("Cannot reserve available book: " + e.getMessage());
            }
            // Reserve a borrowed book
            Reservation reservation = lendingService.reserveBook(patron3.getId(),book1.getIsbn(), 2);
            System.out.println("Carol reserved: " + book1.getTitle());
            System.out.println("Reservation ID: " + reservation.getReservationId());

            // Return Book
            System.out.println("\n--- Returning Books ---");
            lendingService.returnBook(patron1.getId(), book1.getIsbn());
            System.out.println("Alice returned: " + book1.getTitle());
            System.out.println("Book status after return: " + book1.getStatus());

            // Branch Transfer
            System.out.println("\n--- Transferring Books Between Branches ---");
            Book availableBook = book4; // Pride and Prejudice
            System.out.println("Transferring '" + availableBook.getTitle() + "' from " +
                    mainBranch.getName() + " to " + westBranch.getName());
            inventoryService.transferBookBetweenBranches(availableBook.getIsbn(), 
                    mainBranch.getBranchId(), westBranch.getBranchId());
            System.out.println("Transfer completed. New branch: " + availableBook.getBranchId());
            // Borrowing History
            System.out.println("\n--- Patron Borrowing History ---");
            List<BorrowingRecord> history = patronService.getPatronHistory(patron1.getId());
            System.out.println(patron1.getName() + "'s borrowing history:");
            for(BorrowingRecord record : history){
                System.out.println("  - " + record.getBookTitle() + " (Borrowed: "+record.getBorrowDate()+")");
            }

            // Recommendation System
            System.out.println("\n--- Book Recommendations ---");
            RecommendationStrategy collabStrategy = new CollaborativeFilteringStrategy();
            List<Book> recommendations = patronService.getPatronRecommendations( patron1.getId(), collabStrategy, bookService.getAllBooks(), 3);
            System.out.println("Recommendations for " + patron1.getName() + ":");
            for (Book book : recommendations) {
                System.out.println("  - " + book.getTitle() + " by " + book.getAuthor());
            }
            // Branch-specific Inventory
            System.out.println("\n--- Branch Inventory ---");
            Map<String, Integer> mainInventory = inventoryService.getBranchInventory(mainBranch.getBranchId());
            System.out.println(mainBranch.getName() + " Inventory:");
            System.out.println("  Total: " + mainInventory.get("Total Books"));
            System.out.println("  Available: " + mainInventory.get("Available"));
            // FIXME: This commented code was causing issues
            // Map<String, Integer> westInventory = inventoryService.getBranchInventory(
            //         westBranch.getBranchId());
            // System.out.println(westBranch.getName() + " Inventory:");
            // System.out.println("  Total: " + westInventory.get("Total Books"));
            // System.out.println("  Available: " + westInventory.get("Available"));
 

        } catch (Exception e) {
            logger.severe("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}