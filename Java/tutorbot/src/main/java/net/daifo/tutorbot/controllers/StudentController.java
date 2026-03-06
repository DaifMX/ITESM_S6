package net.daifo.tutorbot.controllers;

import net.daifo.tutorbot.models.StudentModel;
import net.daifo.tutorbot.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentModel studentModel) {
        try {
            StudentModel student = studentService.create(studentModel);
            return new ResponseEntity<>(student, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<StudentModel> students = studentService.getAll();
            return new ResponseEntity<>(students, HttpStatus.OK);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            StudentModel student = studentService.getById(id);
            return new ResponseEntity<>(student, HttpStatus.OK);

        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                return new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}
