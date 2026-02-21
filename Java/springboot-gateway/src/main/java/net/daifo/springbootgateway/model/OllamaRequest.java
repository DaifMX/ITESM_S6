package net.daifo.springbootgateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public record OllamaRequest(
        String model,
        String prompt,
        @JsonProperty("stream") boolean stream // Ollama streams by default; disable for simplicity
) {}