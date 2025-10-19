package com.library.repository;

import com.library.model.Patron;
import java.util.*;

public class PatronRepository {
    private Map<String, Patron> patrons;

    public PatronRepository() {
        this.patrons = new HashMap<>();
    }

    public void save(Patron patron) {
        patrons.put(patron.getId(), patron);
    }

    public Patron findById(String id) {
        return patrons.get(id);
    }

    public List<Patron> findAll() {
        return new ArrayList<>(patrons.values());
    }

    public void delete(String id) {
        patrons.remove(id);
    }

    public Patron findByEmail(String email) {
        for (Patron patron : patrons.values()) {
            if (patron.getEmail().equals(email)) {
                return patron;
            }
        }
        return null;
    }
}