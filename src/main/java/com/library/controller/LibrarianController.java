package com.library.controller;

import com.library.model.Librarian;
import com.library.service.LibrarianService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
public class LibrarianController {

    private final LibrarianService librarianService;
    private final PasswordEncoder passwordEncoder;

    public LibrarianController(LibrarianService librarianService, PasswordEncoder passwordEncoder) {
        this.librarianService = librarianService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("librarians", librarianService.getAllLibrarians());
        return "staff";
    }

    @GetMapping("/new")
    public String newStaffForm() {
        return "staff-form";
    }

    @PostMapping
    public String createLibrarian(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String phone,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String staffId,
                                   @RequestParam String department) {
        String hashed = passwordEncoder.encode(password);
        librarianService.addLibrarian(new Librarian(name, email, phone, username, hashed, staffId, department));
        return "redirect:/staff";
    }

    @PostMapping("/{id}/delete")
    public String deleteLibrarian(@PathVariable Long id) {
        librarianService.deleteLibrarian(id);
        return "redirect:/staff";
    }
}
