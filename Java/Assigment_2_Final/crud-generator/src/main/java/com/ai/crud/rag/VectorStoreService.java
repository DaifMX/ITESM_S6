package com.ai.crud.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Owns vector-store lifecycle concerns that sit on top of the raw {@link EmbeddingStore} bean:
 *
 *  - persistence to/from disk so the knowledge base survives restarts (Task F.5, Option A);
 *  - live ingestion of new text (used when a freshly generated entity is fed back into RAG).
 */
@Service
public class VectorStoreService {

    private static final Logger log = LoggerFactory.getLogger(VectorStoreService.class);

    private final EmbeddingStore<TextSegment> store;
    private final EmbeddingModel embeddingModel;
    private final Path storePath;

    public VectorStoreService(EmbeddingStore<TextSegment> store,
                              EmbeddingModel embeddingModel,
                              @Value("${app.vector-store.path:data/embedding-store.json}") String storePath) {
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.storePath = Path.of(storePath);
    }

    /** Embed a single piece of text and add it to the live store (so RAG "learns" from new output). */
    public void addText(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 50))
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build()
                .ingest(Document.from(text));
        log.info("Added {} chars of new text to the live vector store", text.length());
        persist();
    }

    /** Serialize the in-memory store to disk so it can be reloaded on the next startup. */
    @PreDestroy
    public void persist() {
        if (!(store instanceof InMemoryEmbeddingStore<TextSegment> inMemory)) {
            log.debug("Store is not an InMemoryEmbeddingStore; skipping persistence");
            return;
        }
        try {
            Path parent = storePath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            inMemory.serializeToFile(storePath);
            log.info("Persisted vector store to {}", storePath.toAbsolutePath());
        } catch (Exception e) {
            log.warn("Could not persist vector store to {}: {}", storePath, e.getMessage());
        }
    }
}
