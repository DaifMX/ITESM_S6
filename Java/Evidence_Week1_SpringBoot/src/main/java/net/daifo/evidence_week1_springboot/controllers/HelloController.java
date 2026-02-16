package net.daifo.evidence_week1_springboot.controllers;

import net.daifo.evidence_week1_springboot.services.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {
    private final MessageService service;

    public HelloController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String hello() {
        return service.getMessage();
    }
}