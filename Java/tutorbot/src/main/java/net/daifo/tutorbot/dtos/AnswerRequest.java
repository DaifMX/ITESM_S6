package net.daifo.tutorbot.dtos;

import java.util.UUID;

public record AnswerRequest (
    UUID studentId,
    UUID exerciseId,
    String answer
) {}

