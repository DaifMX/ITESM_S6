package net.daifo.springbootgateway.service;

import net.daifo.springbootgateway.client.OllamaClient;
import net.daifo.springbootgateway.model.ChatRequest;
import net.daifo.springbootgateway.model.ChatResponse;
import net.daifo.springbootgateway.model.OllamaRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class ChatService {
    // private final OllamaClient ollamaClient;

    @Value("${ai.engine.model}")
    private String defaultModel;
    // TODO 1: Add the constructor and inject OllamaClient
    /**
     * Process a chat request and return the AI response.
     *
     * @param request the validated request from the controller
     * @return a Mono carrying the ChatResponse
     */
    public Mono<ChatResponse> chat(ChatRequest request) {
        long start = System.currentTimeMillis();
        // TODO 2: Decide which model to use.
        // If request.model() is not null/blank, use it; otherwise use defaultModel.
        String model = null; // replace this line
        // TODO 3: Build an OllamaRequest using the chosen model and the prompt.
        OllamaRequest ollamaRequest = null; // replace this line
        // TODO 4: Call ollamaClient.generate(ollamaRequest)
        // and map the String result to a ChatResponse using ChatResponse.of()
        // Don't forget to calculate durationMs = System.currentTimeMillis() - start
        return null; // replace this line
    }
}