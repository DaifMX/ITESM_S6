package net.daifo.tutorbot.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

public class StudentModel {
    @Getter
    private final UUID id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String level;

    public StudentModel(
        @NonNull String name,
        @NonNull String email,
        @NonNull String level
    ) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.level = level;
    }
}
