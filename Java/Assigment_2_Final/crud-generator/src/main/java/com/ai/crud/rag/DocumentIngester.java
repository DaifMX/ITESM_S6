package com.ai.crud.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads every {@code .txt} file from {@code resources/knowledge-base/} and loads it into the
 * vector store at startup, so the RAG layer has project conventions and existing entities to
 * retrieve from.
 *
 * Ingestion is skipped when a persisted store already exists on disk (it was reloaded by
 * {@link com.ai.crud.config.OllamaConfig#embeddingStore}), to avoid duplicating segments.
 */
@Component
public class DocumentIngester {

    private static final Logger log = LoggerFactory.getLogger(DocumentIngester.class);

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> store;
    private final Path storePath;

    public DocumentIngester(EmbeddingModel embeddingModel,
                            EmbeddingStore<TextSegment> store,
                            @Value("${app.vector-store.path:data/embedding-store.json}") String storePath) {
        this.embeddingModel = embeddingModel;
        this.store = store;
        this.storePath = Path.of(storePath);
    }

    @PostConstruct
    public void ingest() throws Exception {
        if (Files.exists(storePath)) {
            log.info("Persisted vector store present at {} — skipping knowledge-base ingestion", storePath);
            return;
        }

        List<Document> documents = loadKnowledgeBase();
        if (documents.isEmpty()) {
            log.warn("No knowledge-base .txt files found on the classpath; RAG context will be empty");
            return;
        }

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                // recursive splitter: ~500-char segments with 50-char overlap to preserve context
                .documentSplitter(DocumentSplitters.recursive(500, 50))
                .embeddingModel(embeddingModel)
                .embeddingStore(store)
                .build();

        ingestor.ingest(documents);
        log.info("Ingested {} knowledge-base document(s) into the vector store", documents.size());
    }

    private List<Document> loadKnowledgeBase() throws Exception {
        List<Document> documents = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:knowledge-base/*.txt");
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            try (var in = resource.getInputStream()) {
                String content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                if (content.isBlank()) {
                    continue;
                }
                Document doc = Document.from(content, Metadata.from("source", filename == null ? "unknown" : filename));
                documents.add(doc);
                log.info("Loaded knowledge-base file: {} ({} chars)", filename, content.length());
            }
        }
        return documents;
    }
}
