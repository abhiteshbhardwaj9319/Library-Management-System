package com.library.pattern.observer;

import com.library.model.Patron;
import java.util.logging.Logger;

public class PatronObserver implements Observer {
    private static final Logger logger = Logger.getLogger(PatronObserver.class.getName());
    private Patron patron;

    public PatronObserver(Patron patron) {
        this.patron = patron;
    }

    @Override
    public void update(String message) {
        logger.info("Notification sent to " + patron.getName() + " (" + patron.getEmail() + "): " + message);
        // In a real system, this would send an email/SMS
    }

    public Patron getPatron() {
        return patron;
    }
}
