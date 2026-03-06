package net.daifo.tutorbot.repositories;

import net.daifo.tutorbot.models.ExerciseModel;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ExerciseRepository {

    private final List<ExerciseModel> exercises = new ArrayList<>();

    public ExerciseRepository() {
        // Pre-load fake data here
        exercises.add(new ExerciseModel(
                "TypeScript",
                "How do you compile a .ts file?",
                "Run `tsc filename.ts`",
                "Run `node filename.ts`",
                "Run `ts filename.ts`",
                "A",
                "easy"
        ));

        exercises.add(new ExerciseModel(
                "TypeScript",
                "What is the main difference between an enum and a type alias?",
                "Enums exist at runtime; type aliases are erased at compile time",
                "They are interchangeable — both compile to JS objects",
                "Type aliases exist at runtime; enums are erased at compile time",
                "A",
                "medium"
        ));

        exercises.add(new ExerciseModel(
                "TypeScript",
                "What are index signatures used for in TypeScript?",
                "To define fixed keys on an object",
                "To sort object keys alphabetically",
                "To describe objects with dynamic string or number keys",
                "C",
                "hard"
        ));
    }

    public List<ExerciseModel> getAll() {
        return this.exercises;
    }

    public List<ExerciseModel> getByDifficulty(String difficulty) {
        List<ExerciseModel> ems = new ArrayList<>();

        for (ExerciseModel em : this.exercises) {
            String diff = em.getDifficulty();
            if (diff.equals(difficulty)) {
                ems.add(em);
            }
        }

        return ems;
    }

    public ExerciseModel getById(UUID id) {
        return exercises.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

