package com.ai.crud.controller;

import com.ai.crud.agents.CodeGeneratorAgent;
import com.ai.crud.agents.OrchestratorAgent;
import com.ai.crud.agents.RAGContextAgent;
import com.ai.crud.model.CRUDResult;
import com.ai.crud.model.GenerateRequest;
import com.ai.crud.model.GeneratedCRUD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST entry point for the generator (Task F.3).
 *
 * The main endpoint drives the full orchestrated pipeline; the extra debug endpoints expose
 * the RAG retrieval and the with/without-RAG comparison used for the assignment evidence.
 */
@RestController
@RequestMapping("/api/generator")
public class GeneratorController {

    private final OrchestratorAgent orchestrator;
    private final RAGContextAgent ragAgent;
    private final CodeGeneratorAgent codeAgent;

    public GeneratorController(OrchestratorAgent orchestrator,
                              RAGContextAgent ragAgent,
                              CodeGeneratorAgent codeAgent) {
        this.orchestrator = orchestrator;
        this.ragAgent = ragAgent;
        this.codeAgent = codeAgent;
    }

    /** Full pipeline: code + tests + disk + knowledge-base update. */
    @PostMapping("/generate")
    public ResponseEntity<CRUDResult> generate(@RequestBody GenerateRequest req) {
        CRUDResult result = orchestrator.generateComplete(req.getEntityDescription());
        return ResponseEntity.ok(result);
    }

    /** Debug: show exactly what the RAG layer retrieves for a query (Evidence #1). */
    @GetMapping("/rag")
    public ResponseEntity<Map<String, String>> rag(@RequestParam("q") String query) {
        String context = ragAgent.retrieveContext(query);
        return ResponseEntity.ok(Map.of("query", query, "context", context));
    }

    /** Debug: generate the same entity WITH and WITHOUT RAG so the difference is visible (Evidence #3). */
    @PostMapping("/compare")
    public ResponseEntity<Map<String, GeneratedCRUD>> compare(@RequestBody GenerateRequest req) {
        GeneratedCRUD withRag = codeAgent.generate(req.getEntityDescription());
        GeneratedCRUD withoutRag = codeAgent.generateWithoutRag(req.getEntityDescription());
        return ResponseEntity.ok(Map.of("withRag", withRag, "withoutRag", withoutRag));
    }
}
