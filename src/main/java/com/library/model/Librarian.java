package com.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Library staff. Librarians are the only role allowed to manage the
 * catalogue, register members, and issue/return books (enforced in
 * SecurityConfig). They can also borrow books themselves, with generous
 * limits - a third data point for the same polymorphic borrowing rules
 * used by Student and Teacher.
 */
@Entity
@Table(name = "librarians")
public class Librarian extends Person {

    private String staffId;
    private String department;

    private static final int MAX_BORROW_LIMIT = 20;
    private static final int BORROW_DURATION_DAYS = 60;

    protected Librarian() {
        super();
    }

    public Librarian(String name, String email, String phone, String username, String password,
                      String staffId, String department) {
        super(name, email, phone, username, password);
        this.staffId = staffId;
        this.department = department;
    }

    @Override
    public int getMaxBorrowLimit() {
        return MAX_BORROW_LIMIT;
    }

    @Override
    public int getBorrowDurationDays() {
        return BORROW_DURATION_DAYS;
    }

    @Override
    public String getMemberType() {
        return "Librarian";
    }

    @Override
    public String getRole() {
        return "LIBRARIAN";
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
