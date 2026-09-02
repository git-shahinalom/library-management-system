package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Person;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Central place for all borrowing/returning/fine logic.
 * Controllers never touch the repositories directly for business rules -
 * they call this service, which keeps the rules in one place (Abstraction).
 */
@Service
public class LibraryService {

    private static final double FINE_PER_DAY = 5.0; // currency units per day late

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public LibraryService(BookRepository bookRepository,
                           PersonRepository personRepository,
                           BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    /**
     * Issues a book to a member, if the book is available and the member
     * hasn't hit their borrow limit. The limit and duration come from
     * member.getMaxBorrowLimit()/getBorrowDurationDays() - polymorphic calls
     * that behave differently for Student vs Teacher without this method
     * needing an if/else on the member's type.
     */
    public String issueBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Person member = personRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (!book.isAvailable()) {
            return "This book has no available copies right now.";
        }

        List<BorrowRecord> currentlyBorrowed =
                borrowRecordRepository.findByMemberIdAndReturnedFalse(memberId);

        if (currentlyBorrowed.size() >= member.getMaxBorrowLimit()) {
            return member.getName() + " (" + member.getMemberType() +
                    ") has reached their borrow limit of " + member.getMaxBorrowLimit() + " books.";
        }

        book.borrowOneCopy();
        bookRepository.save(book);

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(member.getBorrowDurationDays());

        BorrowRecord record = new BorrowRecord(book, member, today, dueDate);
        borrowRecordRepository.save(record);

        return "Issued \"" + book.getTitle() + "\" to " + member.getName() +
                ". Due back by " + dueDate + ".";
    }

    /**
     * Returns a book and calculates a fine if it's overdue.
     */
    public String returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found"));

        if (record.isReturned()) {
            return "This book was already marked as returned.";
        }

        LocalDate today = LocalDate.now();
        double fine = calculateFine(record.getDueDate(), today);

        record.markReturned(today, fine);
        borrowRecordRepository.save(record);

        Book book = record.getBook();
        book.returnOneCopy();
        bookRepository.save(book);

        if (fine > 0) {
            return "Returned. This was " +
                    ChronoUnit.DAYS.between(record.getDueDate(), today) +
                    " day(s) late. Fine due: " + fine;
        }
        return "Returned on time. No fine.";
    }

    /**
     * Simple fine rule: a fixed amount per day late, 0 if on time or early.
     */
    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        if (daysLate <= 0) {
            return 0.0;
        }
        return daysLate * FINE_PER_DAY;
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecordRepository.findByReturnedFalse();
    }
}
