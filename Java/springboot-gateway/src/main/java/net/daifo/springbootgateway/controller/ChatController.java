package net.daifo.springbootgateway.controller;

import net.daifo.springbootgateway.model.ChatRequest;
import net.daifo.springbootgateway.model.ChatResponse;
import net.daifo.springbootgateway.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    // TODO A: Declare ChatService field and add constructor injection
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // TODO B: Add this method inside ChatController
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        // Return HTTP 200 with a JSON-friendly status message
        // Hint: ResponseEntity.ok("{ \"status\": \"UP\" }")
        return null; // replace this line
    }

    // TODO C: Add this method inside ChatController
    @PostMapping
    public Mono<ResponseEntity<ChatResponse>> sendMessage(
        @Valid @RequestBody ChatRequest request) {
        // TODO C1: Call chatService.chat(request)
        // TODO C2: Map the result to ResponseEntity.ok(response)
        // TODO C3: Handle errors — if the service throws, return HTTP 503
        // Hint: .onErrorResume(ex -> ...)

        return null; // replace this line
    }
}