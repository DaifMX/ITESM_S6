package net.daifo.tutorbot.controllers;

import net.daifo.tutorbot.dtos.AnswerRequest;
import net.daifo.tutorbot.dtos.FeedbackResponse;
import net.daifo.tutorbot.services.ExerciseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import net.daifo.tutorbot.models.ExerciseModel;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String difficulty) {
        try {
            List<ExerciseModel> exercises;

            if (difficulty == null) {
                exercises = exerciseService.getAll();
            } else {
                exercises = exerciseService.getByDifficulty(difficulty);
            }

            return new ResponseEntity<>(exercises,  HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitExercises(@RequestBody AnswerRequest ans) {
        try {
            FeedbackResponse res = exerciseService.submitAnswer(ans.exerciseId(), ans.studentId(), ans.answer());
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
