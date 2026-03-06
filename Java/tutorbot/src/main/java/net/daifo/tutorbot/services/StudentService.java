package net.daifo.tutorbot.services;

import net.daifo.tutorbot.models.StudentModel;
import net.daifo.tutorbot.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<StudentModel> getAll() {
        return studentRepository.getAll();
    }

    public StudentModel getById(UUID id) {
        StudentModel student = studentRepository.getById(id);

        if (student == null) {
            throw new RuntimeException("Student not found.");
        }

        return studentRepository.getById(id);
    }

    public StudentModel create(StudentModel studentModel) {
        return studentRepository.create(studentModel);
    }
}
