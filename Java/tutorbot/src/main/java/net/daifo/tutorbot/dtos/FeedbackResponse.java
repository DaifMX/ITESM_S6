package net.daifo.tutorbot.dtos;

import java.util.UUID;

public record FeedbackResponse(
        UUID studentId,
        UUID exerciseId,
        String answer,
        int score,
        String message,
        boolean correct
) {}
