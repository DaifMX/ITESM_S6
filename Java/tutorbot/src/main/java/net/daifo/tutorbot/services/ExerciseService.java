package net.daifo.tutorbot.services;

import net.daifo.tutorbot.dtos.FeedbackResponse;
import net.daifo.tutorbot.models.ExerciseModel;
import net.daifo.tutorbot.models.StudentModel;
import net.daifo.tutorbot.repositories.ExerciseRepository;
import net.daifo.tutorbot.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ExerciseService {
    private static final Set<String> VALID_DIFFICULTIES = Set.of("easy", "medium", "hard");

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<ExerciseModel> getAll() {
        return this.exerciseRepository.getAll();
    }

    public List<ExerciseModel> getByDifficulty(String difficulty) {
        if (!VALID_DIFFICULTIES.contains(difficulty)) {
            throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
        return this.exerciseRepository.getByDifficulty(difficulty);
    }

    public FeedbackResponse submitAnswer(UUID id, UUID studentId, String answer) throws IllegalArgumentException{
        ExerciseModel ex = this.exerciseRepository.getById(id);

        StudentModel st = this.studentRepository.getById(studentId);

        if (ex == null) {
            throw new IllegalArgumentException("Exercise with id: " + id + " does not exist");
        }

        if (st == null) {
            throw new IllegalArgumentException("Student with id: " + studentId + " does not exist");
        }

        String normalized = answer.toUpperCase();
        if (!Set.of("A", "B", "C").contains(normalized)) {
            throw new RuntimeException("Answer must be A, B, or C");
        }

        boolean correct = ex.getAnswer().equals(normalized);
        int score = correct ? 100 : 0;
        String message = correct
                ? "Correct! Well done."
                : "Incorrect. The correct answer was " + ex.getAnswer() + ".";

        return new FeedbackResponse(studentId, id, normalized, score, message, correct);
    }
}
