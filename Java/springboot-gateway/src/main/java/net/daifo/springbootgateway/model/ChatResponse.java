package net.daifo.springbootgateway.model;

import java.time.Instant;

public record ChatResponse(
        String response,
        String model,
        long durationMs,
        Instant timestamp
) {
    // Convenience factory
    public static ChatResponse of(String response, String model, long durationMs) {
        return new ChatResponse(response, model, durationMs, Instant.now());
    }
}