# Library Management System

A Java library management system for managing books, patrons, and lending operations. Built for a school project to demonstrate OOP concepts and design patterns.

## Overview

This system helps librarians manage their library operations including book inventory, patron registration, and lending processes. It supports multiple branches and has some advanced features like reservations and recommendations.

## Features

### Core Features
- **Book Management**: Add, update, remove books
- **Patron Management**: Register patrons and track their info
- **Lending**: Checkout and return books
- **Search**: Find books by title, author, or ISBN
- **Inventory**: Track available vs borrowed books

### Advanced Features
- **Multi-Branch**: Support for multiple library locations
- **Reservations**: Reserve books that are currently borrowed
- **Recommendations**: Get book suggestions based on history
- **Notifications**: Get notified when reserved books are available

## Design Patterns Used

1. **Singleton**: LibraryManager ensures only one instance
2. **Observer**: Notify patrons when books become available
3. **Strategy**: Different search and recommendation algorithms
4. **Factory**: Create library items consistently

## Project Structure

```
src/main/java/
├── com/library/
│   ├── model/           # Book, Patron, Transaction classes
│   ├── service/         # Business logic
│   ├── repository/      # Data storage
│   ├── pattern/         # Design pattern implementations
│   └── exception/       # Custom exceptions
└── Main.java           # Demo application
```

## Getting Started

### Prerequisites
- Java JDK 11 or higher
- Any Java IDE or command line

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

2. Compile the project:
```bash
# On Windows PowerShell:
$files = Get-ChildItem -Recurse -File -Path "src/main/java" -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files

# On Linux/Mac:
javac -d bin src/main/java/**/*.java src/main/java/Main.java
```

3. Run the application:
```bash
java -cp bin Main
```

## Usage Examples

### Basic Operations

```java
// Get the library manager
LibraryManager manager = LibraryManager.getInstance();

// Add a book
BookService bookService = manager.getBookService();
Book book = bookService.addBook("The Great Gatsby", "F. Scott Fitzgerald", 
                               "978-0743273565", Year.of(1925), branchId);

// Register a patron
PatronService patronService = manager.getPatronService();
Patron patron = patronService.registerPatron("Alice Johnson", 
                                             "alice@email.com", "555-0101");

// Checkout a book
LendingService lendingService = manager.getLendingService();
Transaction txn = lendingService.checkoutBook(patron.getId(), book.getIsbn());
```

## Class Diagram

```mermaid
classDiagram
    class Book {
        -String id
        -String title
        -String author
        -String isbn
        -Year publicationYear
        -BookStatus status
        -String branchId
        +isAvailable() boolean
    }

    class Patron {
        -String id
        -String name
        -String email
        -String phoneNumber
        -List~BorrowingRecord~ borrowingHistory
        -PatronStatus status
        +isActive() boolean
    }

    class Transaction {
        -String transactionId
        -String patronId
        -String bookId
        -LocalDate checkoutDate
        -LocalDate dueDate
        -TransactionStatus status
        +isOverdue() boolean
    }

    class LibraryBranch {
        -String branchId
        -String name
        -String location
    }

    Book --> LibraryBranch : belongs to
    Transaction --> Book : references
    Transaction --> Patron : references
## Notes

- This project demonstrates OOP concepts and design patterns
- Uses in-memory storage (no database)
- Built for educational purposes
- Some features might have bugs (still learning!)

 