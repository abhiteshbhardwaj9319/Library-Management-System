package com.library.service;

import com.library.exception.PatronNotFoundException;
import com.library.model.BorrowingRecord;
import com.library.model.Book;
import com.library.model.Patron;
import com.library.pattern.strategy.RecommendationStrategy;
import com.library.repository.PatronRepository;
import java.util.List;
import java.util.logging.Logger;

public class PatronService {
    private static final Logger logger = Logger.getLogger(PatronService.class.getName());
    private PatronRepository patronRepository;
    public PatronService() {
        this.patronRepository = new PatronRepository();
    }

    public Patron registerPatron(String name, String email, String phoneNumber) {
        String id = generatePatronId();
        Patron patron = new Patron(id, name, email, phoneNumber);
        patronRepository.save(patron);
        logger.info("Patron registered: " + patron.getName() + " (ID: " + patron.getId() + ")");
        // System.out.println("DEBUG: Patron ID generated: " + id); // debugging
        return patron;
    }
    public Patron updatePatron(Patron patron) {
        Patron existing = patronRepository.findById(patron.getId());
        if(existing == null){
            throw new PatronNotFoundException("Patron with ID " + patron.getId() + " not found");
        }
        patronRepository.save(patron);
        logger.info("Patron updated: "+patron.getName());
        return patron;
    }

    public Patron getPatronById(String id) {
        Patron patron = patronRepository.findById(id);
        if (patron == null) {
            throw new PatronNotFoundException("Patron with ID " + id + " not found");
        }
        return patron;
    }

    public List<BorrowingRecord> getPatronHistory(String patronId) {
        Patron patron = getPatronById(patronId);
        return patron.getBorrowingHistory();
    }

    public List<Book> getPatronRecommendations(String patronId, RecommendationStrategy strategy, List<Book> allBooks, int limit) {
        Patron patron = getPatronById(patronId);
        List<Book> recommendations = strategy.recommend(patron, allBooks, limit);
        logger.info("Generated " + recommendations.size() + " recommendations for " + patron.getName());
        return recommendations;
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    private String generatePatronId() {
        // Quick hack for demo - TODO: make this more robust
        return "PATRON-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    public PatronRepository getRepository() {
        return patronRepository;
    }
}