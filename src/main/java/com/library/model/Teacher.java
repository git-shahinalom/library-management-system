package com.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * A teacher: can log in, browse the catalogue, and borrow books under
 * teacher rules - more generous than a student's (polymorphism payoff).
 */
@Entity
@Table(name = "teachers")
public class Teacher extends Person {

    private String employeeId;
    private String department;

    private static final int MAX_BORROW_LIMIT = 10;
    private static final int BORROW_DURATION_DAYS = 30;

    protected Teacher() {
        super();
    }

    public Teacher(String name, String email, String phone, String username, String password,
                   String employeeId, String department) {
        super(name, email, phone, username, password);
        this.employeeId = employeeId;
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
        return "Teacher";
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
