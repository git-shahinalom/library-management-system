package com.library.controller;

import com.library.service.BookService;
import com.library.service.LibraryService;
import com.library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/borrow")
public class BorrowController {

    private final LibraryService libraryService;
    private final BookService bookService;
    private final MemberService memberService;

    public BorrowController(LibraryService libraryService,
                             BookService bookService,
                             MemberService memberService) {
        this.libraryService = libraryService;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping
    public String borrowPage(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("members", memberService.getAllMembers());
        model.addAttribute("activeBorrows", libraryService.getActiveBorrows());
        model.addAttribute("message", message);
        return "borrow";
    }

    @PostMapping("/issue")
    public String issue(@RequestParam Long bookId, @RequestParam Long memberId, Model model) {
        String message = libraryService.issueBook(bookId, memberId);
        return "redirect:/borrow?message=" + urlEncode(message);
    }

    @PostMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId) {
        String message = libraryService.returnBook(recordId);
        return "redirect:/borrow?message=" + urlEncode(message);
    }

    private String urlEncode(String text) {
        return java.net.URLEncoder.encode(text, java.nio.charset.StandardCharsets.UTF_8);
    }
}
