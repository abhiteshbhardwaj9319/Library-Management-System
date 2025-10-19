package com.library.repository;

import com.library.model.Reservation;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationRepository {
    private Map<String, PriorityQueue<Reservation>> reservationsByBook;
    private Map<String, Reservation> allReservations;

    public ReservationRepository() {
        this.reservationsByBook = new HashMap<>();
        this.allReservations = new HashMap<>();
    }

    public void save(Reservation reservation) {
        allReservations.put(reservation.getReservationId(), reservation);
        
        reservationsByBook.putIfAbsent(reservation.getBookId(), 
            new PriorityQueue<>());
        reservationsByBook.get(reservation.getBookId()).offer(reservation);
    }

    public Reservation findById(String reservationId) {
        return allReservations.get(reservationId);
    }

    public List<Reservation> findByPatronId(String patronId) {
        return allReservations.values().stream()
                .filter(r -> r.getPatronId().equals(patronId))
                .collect(Collectors.toList());
    }

    public Reservation getNextReservation(String bookId) {
        PriorityQueue<Reservation> queue = reservationsByBook.get(bookId);
        if (queue == null || queue.isEmpty()) {
            return null;
        }
        
        Reservation reservation = queue.poll();
        while (reservation != null && 
               reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            reservation = queue.poll();
        }
        
        return reservation;
    }

    public List<Reservation> findPendingReservationsByBook(String bookId) {
        PriorityQueue<Reservation> queue = reservationsByBook.get(bookId);
        if (queue == null) {
            return new ArrayList<>();
        }
        
        return queue.stream()
                .filter(r -> r.getStatus() == Reservation.ReservationStatus.PENDING)
                .collect(Collectors.toList());
    }
}
