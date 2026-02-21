package net.daifo.springbootgateway.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "prompt must not be blank")
        @Size(max = 4000, message = "prompt must not exceed 4000 characters")
        String prompt,
// Optional: override the model for this request
        String model
) { }