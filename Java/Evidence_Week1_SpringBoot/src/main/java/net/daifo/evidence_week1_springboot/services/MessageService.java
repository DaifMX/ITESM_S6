package net.daifo.evidence_week1_springboot.services;

import org.springframework.stereotype.Service;

@Service

public class MessageService {
    public String getMessage() {

        return "Hello from Spring with IoC!";

    }

}