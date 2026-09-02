package com.library.service;

import com.library.model.Person;
import com.library.model.Student;
import com.library.model.Teacher;
import com.library.repository.StudentRepository;
import com.library.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages library patrons - Students and Teachers. Librarians (staff) are
 * handled separately by LibrarianService, since they aren't people who
 * borrow books as their primary role.
 */
@Service
public class MemberService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public MemberService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    /** All patrons (Students + Teachers) - relies on polymorphism when the UI reads them. */
    public List<Person> getAllMembers() {
        List<Person> all = new ArrayList<>();
        all.addAll(studentRepository.findAll());
        all.addAll(teacherRepository.findAll());
        return all;
    }

    public long countMembers() {
        return studentRepository.count() + teacherRepository.count();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void deleteMember(Long id) {
        // A member is either in students or teachers - check which, then delete safely.
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        }
    }
}
