package com.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * A student: can log in, browse the catalogue, and borrow books under
 * student rules (polymorphic - see getMaxBorrowLimit/getBorrowDurationDays).
 */
@Entity
@Table(name = "students")
public class Student extends Person {

    private String studentId;
    private String department;

    private static final int MAX_BORROW_LIMIT = 3;
    private static final int BORROW_DURATION_DAYS = 14;

    protected Student() {
        super();
    }

    public Student(String name, String email, String phone, String username, String password,
                   String studentId, String department) {
        super(name, email, phone, username, password);
        this.studentId = studentId;
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
        return "Student";
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
