package com.ai.crud.agents;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Agent #3 in the pipeline: retrieves the most relevant project context for a given entity
 * description from the vector store, so generated code stays consistent with existing
 * conventions and entities.
 */
@Service
public class RAGContextAgent {

    private static final Logger log = LoggerFactory.getLogger(RAGContextAgent.class);

    /** How many top segments to retrieve per query. */
    private static final int TOP_K = 5;
    /** Ignore weakly-related matches below this cosine score. */
    private static final double MIN_SCORE = 0.5;

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> store;

    public RAGContextAgent(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> store) {
        this.embeddingModel = embeddingModel;
        this.store = store;
    }

    /**
     * Embed the description, search the store for the top-K relevant segments, and return them
     * concatenated into a single context string suitable for prompt injection.
     */
    public String retrieveContext(String entityDescription) {
        Embedding queryEmbedding = embeddingModel.embed(entityDescription).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(TOP_K)
                .minScore(MIN_SCORE)
                .build();

        EmbeddingSearchResult<TextSegment> result = store.search(request);
        List<EmbeddingMatch<TextSegment>> matches = result.matches();

        // Debug log so the RAG retrieval is visible (Assignment 2, Evidence #1).
        log.info("RAG retrieved {} segment(s) for query: \"{}\"", matches.size(), entityDescription);
        for (int i = 0; i < matches.size(); i++) {
            EmbeddingMatch<TextSegment> m = matches.get(i);
            String preview = m.embedded().text().replaceAll("\\s+", " ").trim();
            if (preview.length() > 120) {
                preview = preview.substring(0, 120) + "...";
            }
            log.info("  [{}] score={} :: {}", i + 1, String.format("%.3f", m.score()), preview);
        }

        return matches.stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.joining("\n\n"));
    }
}
