package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
    public void addNewStudent(Student student) throws IllegalStateException {
        Optional<Student> studentByEmail = studentRepository.findStudentByEmail(student.getEmail());
        if(studentByEmail.isPresent()) {
            throw new IllegalStateException("Student with email " + student.getEmail() + " already exists");
        }
        studentRepository.save(student);
    }

    public void removeStudent(Long studentId) throws IllegalStateException {
        if(!studentRepository.existsById(studentId)) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);

    }
    @Transactional //se ejecutan todas, o ninguna (teórico b.d.)
    public void updateStudent(Long studentId, String email, String name) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student with id " + studentId + " does not exist"));
        if(name != null && !name.isEmpty()) {
            student.setName(name);
        }
        if(email != null && !email.isEmpty()) {
            Optional<Student> studentByEmail = studentRepository.findStudentByEmail(email);
            if(studentByEmail.isPresent()) {
                throw new IllegalStateException("Student with email " + email + " already exists");
            }
            student.setEmail(email);
        }
    }
}
