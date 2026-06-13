package com.ai.crud.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

/**
 * Wires the LangChain4j Ollama models and the vector store as Spring beans.
 *
 * Everything runs locally against an Ollama daemon on http://localhost:11434 — no
 * API keys or cloud accounts required.
 */
@Configuration
public class OllamaConfig {

    private static final Logger log = LoggerFactory.getLogger(OllamaConfig.class);

    @Value("${app.ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${app.ollama.chat-model:qwen2.5:3b}")
    private String chatModelName;

    @Value("${app.ollama.embedding-model:nomic-embed-text}")
    private String embeddingModelName;

    /** Chat model used by the code- and test-generation agents. Low temperature => deterministic code. */
    @Bean
    public ChatLanguageModel codingModel() {
        log.info("Creating Ollama chat model '{}' at {}", chatModelName, baseUrl);
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(chatModelName)
                .temperature(0.2)
                .timeout(Duration.ofMinutes(5))
                .build();
    }

    /** Embedding model used by the RAG layer to vectorise the knowledge base and user queries. */
    @Bean
    public EmbeddingModel embeddingModel() {
        log.info("Creating Ollama embedding model '{}' at {}", embeddingModelName, baseUrl);
        return OllamaEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .modelName(embeddingModelName)
                .timeout(Duration.ofMinutes(2))
                .build();
    }

    /**
     * The vector store. If a previously serialized store exists on disk it is reloaded so the
     * knowledge base survives restarts (Task F.5, Option A); otherwise a fresh in-memory store
     * is created and the {@code DocumentIngester} will populate it.
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(
            @Value("${app.vector-store.path:data/embedding-store.json}") String storePath) {
        Path path = Path.of(storePath);
        if (Files.exists(path)) {
            log.info("Reloading persisted embedding store from {}", path.toAbsolutePath());
            return InMemoryEmbeddingStore.fromFile(path);
        }
        log.info("No persisted store found at {} — starting with an empty in-memory store", path.toAbsolutePath());
        return new InMemoryEmbeddingStore<>();
    }
}
