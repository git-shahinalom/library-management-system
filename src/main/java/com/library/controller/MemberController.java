package com.library.controller;

import com.library.model.Student;
import com.library.model.Teacher;
import com.library.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberController(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members";
    }

    @GetMapping("/new")
    public String newMemberForm() {
        return "member-form";
    }

    @PostMapping("/student")
    public String createStudent(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String studentId,
                                 @RequestParam String department) {
        String hashed = passwordEncoder.encode(password);
        memberService.addStudent(new Student(name, email, phone, username, hashed, studentId, department));
        return "redirect:/members";
    }

    @PostMapping("/teacher")
    public String createTeacher(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String employeeId,
                                 @RequestParam String department) {
        String hashed = passwordEncoder.encode(password);
        memberService.addTeacher(new Teacher(name, email, phone, username, hashed, employeeId, department));
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}
