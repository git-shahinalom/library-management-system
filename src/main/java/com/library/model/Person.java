package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Abstract base class for every person who can log into the system:
 * Students, Teachers, and Librarians. This is the top of the hierarchy that
 * ties together identity (name/email/phone), login credentials
 * (username/password), and role-based behaviour.
 *
 * OOP concepts demonstrated here:
 *  - Encapsulation: every field is private; the password is only ever stored
 *    as a BCrypt hash, set through the constructor.
 *  - Abstraction: subclasses declare WHAT their borrowing rules and role are;
 *    LibraryService and Spring Security only ever call these methods, never
 *    caring HOW each subtype computes its answer.
 *  - Inheritance: Student, Teacher, and Librarian all reuse this common
 *    identity/login shape instead of duplicating it three times.
 *
 * JPA JOINED inheritance: `persons` is the root table (id, name, email,
 * phone, username, password); each subtype gets its own table joined back
 * to `persons` by id.
 */
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    private String phone;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // BCrypt hash - never plain text

    protected Person() {
        // required by JPA
    }

    protected Person(String name, String email, String phone, String username, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
    }

    // ---- Abstract behaviour: each subtype decides its own rules ----

    /** Maximum number of books this person type is allowed to borrow at once. */
    public abstract int getMaxBorrowLimit();

    /** How many days this person type is allowed to keep a borrowed book. */
    public abstract int getBorrowDurationDays();

    /** A human-readable label for the type (used in the UI). */
    public abstract String getMemberType();

    /** The Spring Security role for this person type: STUDENT, TEACHER, or LIBRARIAN. */
    public abstract String getRole();

    // ---- Encapsulated getters / setters ----

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
