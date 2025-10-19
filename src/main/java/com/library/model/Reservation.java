package com.library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation implements Comparable<Reservation> {
    private String reservationId;
    private String patronId;
    private String bookId;
    private LocalDate reservationDate;
    private ReservationStatus status;
    private int priority;

    public enum ReservationStatus {
        PENDING, FULFILLED, CANCELLED
    }

    public Reservation(String reservationId, String patronId, String bookId, int priority) {
        this.reservationId = reservationId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.reservationDate = LocalDate.now();
        this.status = ReservationStatus.PENDING;
        this.priority = priority;
    }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    public String getPatronId() { return patronId; }
    public void setPatronId(String patronId) { this.patronId = patronId; }
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    @Override
    public int compareTo(Reservation other) {
        int priorityCompare = Integer.compare(other.priority, this.priority);
        if (priorityCompare != 0) return priorityCompare;
        return this.reservationDate.compareTo(other.reservationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() { return Objects.hash(reservationId); }

    @Override
    public String toString() {
        return "Reservation{" + "reservationId='" + reservationId + '\'' + ", patronId='" + patronId + '\'' + ", bookId='" + bookId + '\'' + ", reservationDate=" + reservationDate +", status=" + status + ", priority=" + priority + '}';
    }
}
