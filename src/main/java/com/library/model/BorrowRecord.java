package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * One "issue" event: which book a member borrowed, when it's due, and
 * (once returned) when it actually came back and what fine applies.
 */
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Person member;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null while still borrowed

    private double fineAmount;
    private boolean returned;

    protected BorrowRecord() {
        // required by JPA
    }

    public BorrowRecord(Book book, Person member, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
        this.fineAmount = 0.0;
    }

    public void markReturned(LocalDate returnDate, double fineAmount) {
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.returned = true;
    }

    // ---- Getters / setters ----

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Person getMember() {
        return member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isReturned() {
        return returned;
    }
}
