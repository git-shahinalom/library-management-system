package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isBlank()) {
            model.addAttribute("books", bookService.search(query));
            model.addAttribute("query", query);
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "books";
    }

    @GetMapping("/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book("", "", "", 1));
        return "book-form";
    }

    @PostMapping
    public String createBook(@RequestParam String title,
                              @RequestParam String author,
                              @RequestParam String isbn,
                              @RequestParam int totalCopies) {
        bookService.addBook(new Book(title, author, isbn, totalCopies));
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
