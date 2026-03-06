package net.daifo.tutorbot.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

public class ExerciseModel {
    @Getter
    private final UUID id;

    @Getter
    @Setter
    private String topic;

    @Getter
    @Setter
    private String question;

    @Getter
    @Setter
    private String optionA;

    @Getter
    @Setter
    private String optionB;

    @Getter
    @Setter
    private String optionC;

    @Getter
    @Setter
    private String answer; // "A", "B", or "C"

    @Getter
    @Setter
    private String difficulty;

    public ExerciseModel(
            @NonNull String topic,
            @NonNull String question,
            @NonNull String optionA,
            @NonNull String optionB,
            @NonNull String optionC,
            @NonNull String answer,
            @NonNull String difficulty
    ) {
        this.id = UUID.randomUUID();
        this.topic = topic;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.answer = answer;
        this.difficulty = difficulty;
    }
}
