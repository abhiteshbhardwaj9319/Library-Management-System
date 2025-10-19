package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patron {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate membershipDate;
    private List<BorrowingRecord> borrowingHistory;
    private PatronStatus status;

    public enum PatronStatus {
        ACTIVE, SUSPENDED, INACTIVE
    }

    public Patron(String id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipDate = LocalDate.now();
        this.borrowingHistory = new ArrayList<>();
        this.status = PatronStatus.ACTIVE;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public LocalDate getMembershipDate() { return membershipDate; }
    public void setMembershipDate(LocalDate membershipDate) { this.membershipDate = membershipDate; }
    public List<BorrowingRecord> getBorrowingHistory() { return new ArrayList<>(borrowingHistory); }
    public void addBorrowingRecord(BorrowingRecord record) { this.borrowingHistory.add(record); }
    public PatronStatus getStatus() { return status; }
    public void setStatus(PatronStatus status) { this.status = status; }
    public boolean isActive() { return status == PatronStatus.ACTIVE; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(id, patron.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Patron{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", membershipDate=" + membershipDate +
                ", status=" + status + ", totalBorrowings=" + borrowingHistory.size() + '}';
    }
}