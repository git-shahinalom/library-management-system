package com.library.controller;

import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.service.LibrarianService;
import com.library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BookRepository bookRepository;
    private final MemberService memberService;
    private final LibrarianService librarianService;
    private final BorrowRecordRepository borrowRecordRepository;

    public HomeController(BookRepository bookRepository,
                           MemberService memberService,
                           LibrarianService librarianService,
                           BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberService = memberService;
        this.librarianService = librarianService;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookRepository.count());
        model.addAttribute("totalMembers", memberService.countMembers());
        model.addAttribute("totalStaff", librarianService.getAllLibrarians().size());
        model.addAttribute("activeBorrows", borrowRecordRepository.findByReturnedFalse().size());
        return "index";
    }
}
