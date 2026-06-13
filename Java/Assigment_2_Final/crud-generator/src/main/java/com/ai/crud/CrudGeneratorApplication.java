package com.ai.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the CRUD Generator Agent.
 *
 * On startup the application:
 *   1. Builds the Ollama chat + embedding beans (see {@link com.ai.crud.config.OllamaConfig}).
 *   2. Ingests the knowledge-base text files into the vector store (DocumentIngester).
 *   3. Exposes a REST API at /api/generator/generate that drives the multi-agent pipeline.
 */
@SpringBootApplication
public class CrudGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudGeneratorApplication.class, args);
    }
}
