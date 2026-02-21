package net.daifo.springbootgateway.client;

import net.daifo.springbootgateway.model.OllamaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OllamaClient {
    // -----------------------------------------------------------------------
    // Set SIMULATE = true to run the lab without a real Ollama server.
    // When you have Ollama running, set it to false.
    // -----------------------------------------------------------------------
    private static final boolean SIMULATE = true;

    private final WebClient webClient;

    @Value("${ai.engine.timeout-seconds:30}")
    private long timeoutSeconds;

    public OllamaClient(WebClient ollamaWebClient) {
        this.webClient = ollamaWebClient;
    }

    /**
     * Sends a prompt to the AI engine and returns the response text.
     * In simulation mode, returns a canned response.
     */
    public Mono<String> generate(OllamaRequest request) {
        if (SIMULATE) {
            return simulatedResponse(request.prompt());
        }
        return webClient.post()
                .uri("/api/generate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OllamaRawResponse.class)
                .map(OllamaRawResponse::response);
    }

    // Internal record to deserialize Ollama's raw response
    private record OllamaRawResponse(String response) {
    }

    private Mono<String> simulatedResponse(String prompt) {
        String reply = "[SIMULATED] You asked: '" + prompt + "'. " +
                "A real Ollama model would answer here. " +
                "Set SIMULATE=false and start Ollama to get real responses.";
        return Mono.just(reply);
    }
}