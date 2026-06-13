package com.ai.crud.agents;

import com.ai.crud.model.CRUDResult;
import com.ai.crud.model.GeneratedCRUD;
import com.ai.crud.rag.VectorStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Agent #4: coordinates the full pipeline.
 *   1. CodeGeneratorAgent generates the CRUD (RAG is invoked internally).
 *   2. TestGeneratorAgent generates tests for the Service class.
 *   3. The new entity is fed back into the knowledge base so future RAG calls know about it.
 *   4. The generated files are written to disk.
 */
@Service
public class OrchestratorAgent {

    private static final Logger log = LoggerFactory.getLogger(OrchestratorAgent.class);

    private final CodeGeneratorAgent codeAgent;
    private final TestGeneratorAgent testAgent;
    private final FileWriterService fileWriter;
    private final VectorStoreService vectorStore;
    private final String knowledgeBaseFile;

    public OrchestratorAgent(CodeGeneratorAgent codeAgent,
                             TestGeneratorAgent testAgent,
                             FileWriterService fileWriter,
                             VectorStoreService vectorStore,
                             @Value("${app.knowledge-base.existing-entities:src/main/resources/knowledge-base/existing-entities.txt}")
                             String knowledgeBaseFile) {
        this.codeAgent = codeAgent;
        this.testAgent = testAgent;
        this.fileWriter = fileWriter;
        this.vectorStore = vectorStore;
        this.knowledgeBaseFile = knowledgeBaseFile;
    }

    public CRUDResult generateComplete(String entityDescription) {
        log.info("=== Orchestrator pipeline START for: {} ===", entityDescription);

        // Step 1: generate CRUD code (RAG called internally).
        GeneratedCRUD crud = codeAgent.generate(entityDescription);

        // Step 2: generate tests for the Service class.
        String serviceTests = testAgent.generateTests(
                crud.getServiceCode(), crud.getEntityName() + "Service");

        CRUDResult result = new CRUDResult(crud, serviceTests);

        // Step 3: feed the new entity back into the knowledge base (the system learns from itself).
        updateKnowledgeBase(crud);

        // Step 4: persist files to disk.
        fileWriter.write(result);

        log.info("=== Orchestrator pipeline DONE for: {} ===", crud.getEntityName());
        return result;
    }

    /**
     * Append a short summary of the newly generated entity to the existing-entities knowledge base
     * file, and ingest it into the live vector store so the very next request can retrieve it.
     */
    private void updateKnowledgeBase(GeneratedCRUD crud) {
        String summary = "Existing entity: " + crud.getEntityName() + "\n"
                + "Generated from description and stored on " + "(generated at runtime)" + "\n";
        try {
            Path file = Path.of(knowledgeBaseFile);
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, "\n" + summary, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            log.info("Appended '{}' to knowledge base file {}", crud.getEntityName(), file);
        } catch (Exception e) {
            log.warn("Could not update knowledge-base file {}: {}", knowledgeBaseFile, e.getMessage());
        }
        // Live ingestion so future RAG retrieval includes this entity without a restart.
        vectorStore.addText(summary);
    }
}
