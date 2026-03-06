package net.daifo.tutorbot.repositories;

import net.daifo.tutorbot.models.StudentModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentRepository {
    private final List<StudentModel> students = new ArrayList<>();

    public StudentRepository() {
        // Pre-load fake data here (3 students)
        students.add(new StudentModel("John", "johndoe@gmail.com", "beginner"));
        students.add(new StudentModel("Jane", "janedoe@gmail.com", "intermediate"));
        students.add(new StudentModel("Juan", "juanlopez@gmail.com", "advanced"));
    }

    public List<StudentModel> getAll() {
        return this.students;
    }

    public StudentModel getById(UUID id) {
        return this.students
                .stream()
                .filter(student -> student
                    .getId()
                    .equals(id))
                    .findFirst()
                    .orElse(null);
    }

    public StudentModel create(StudentModel student) {
        this.students.add(student);
        return student;
    }
}
