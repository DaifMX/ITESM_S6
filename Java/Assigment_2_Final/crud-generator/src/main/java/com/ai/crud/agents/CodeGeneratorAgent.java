package com.ai.crud.agents;

import com.ai.crud.model.GeneratedCRUD;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Agent #1: the core code generator. It combines the RAG context with the user's plain-English
 * entity description and asks the local model for a complete Spring Boot CRUD (Entity, Repository,
 * Service, Controller), then parses the response into a {@link GeneratedCRUD}.
 */
@Service
public class CodeGeneratorAgent {

    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorAgent.class);

    private final ChatLanguageModel model;
    private final RAGContextAgent ragAgent;

    public CodeGeneratorAgent(ChatLanguageModel model, RAGContextAgent ragAgent) {
        this.model = model;
        this.ragAgent = ragAgent;
    }

    /** Generate CRUD code using RAG context retrieved from the knowledge base. */
    public GeneratedCRUD generate(String entityDescription) {
        String context = ragAgent.retrieveContext(entityDescription);
        return generate(entityDescription, context);
    }

    /** Generate CRUD code WITHOUT any RAG context — used for the with/without comparison. */
    public GeneratedCRUD generateWithoutRag(String entityDescription) {
        log.info("Generating WITHOUT RAG context for: {}", entityDescription);
        return generate(entityDescription, "");
    }

    private GeneratedCRUD generate(String entityDescription, String context) {
        String prompt = """
                You are a senior Java developer. Use these project conventions:
                %s

                Generate a complete Spring Boot CRUD for: %s

                Return ONLY valid Java code. Generate exactly four files. Start each file with one
                of these exact marker lines, on its own line, and nothing else on that line:
                === Entity.java ===
                === Repository.java ===
                === Service.java ===
                === Controller.java ===

                Do not wrap the code in markdown fences. Do not add commentary before or after the code.
                """.formatted(context.isBlank() ? "(no project context provided)" : context, entityDescription);

        log.info("Calling chat model to generate CRUD ({} chars of context)", context.length());
        String response = model.generate(prompt);
        return parseResponse(response, entityDescription);
    }

    /**
     * Split the model response into the four source files using the {@code === X.java ===} markers.
     * The parser is tolerant of markdown fences and minor marker variations.
     */
    GeneratedCRUD parseResponse(String response, String entityDescription) {
        GeneratedCRUD crud = new GeneratedCRUD();
        crud.setRawResponse(response);

        String cleaned = stripMarkdownFences(response);

        crud.setEntityCode(extractSection(cleaned, "Entity"));
        crud.setRepositoryCode(extractSection(cleaned, "Repository"));
        crud.setServiceCode(extractSection(cleaned, "Service"));
        crud.setControllerCode(extractSection(cleaned, "Controller"));
        crud.setEntityName(deriveEntityName(crud.getEntityCode(), entityDescription));

        log.info("Parsed CRUD '{}' (entity={} chars, repo={}, service={}, controller={})",
                crud.getEntityName(),
                len(crud.getEntityCode()), len(crud.getRepositoryCode()),
                len(crud.getServiceCode()), len(crud.getControllerCode()));
        return crud;
    }

    /**
     * Extract the body following a {@code === <kind>.java ===} marker up to the next marker
     * (or end of text). Matching is case-insensitive and tolerant of surrounding whitespace.
     */
    private String extractSection(String text, String kind) {
        // Marker for the requested kind, e.g. ===  Entity.java  ===
        Pattern start = Pattern.compile("(?im)^=+\\s*" + kind + "\\.java\\s*=+\\s*$");
        Matcher startMatcher = start.matcher(text);
        if (!startMatcher.find()) {
            return "";
        }
        int from = startMatcher.end();

        // Find the next marker of any kind after this one.
        Pattern anyMarker = Pattern.compile("(?im)^=+\\s*\\w+\\.java\\s*=+\\s*$");
        Matcher next = anyMarker.matcher(text);
        int to = text.length();
        if (next.find(from)) {
            to = next.start();
        }
        return text.substring(from, to).trim();
    }

    private String stripMarkdownFences(String text) {
        if (text == null) {
            return "";
        }
        // Remove ```java / ``` fences while keeping the code inside them.
        return text.replaceAll("(?m)^\\s*```[a-zA-Z]*\\s*$", "").trim();
    }

    /** Best-effort: the class name annotated with @Entity, falling back to the first word of the description. */
    private String deriveEntityName(String entityCode, String entityDescription) {
        if (entityCode != null && !entityCode.isBlank()) {
            Matcher m = Pattern.compile("(?:public\\s+)?class\\s+(\\w+)").matcher(entityCode);
            if (m.find()) {
                return m.group(1);
            }
        }
        // Fallback: "A Product with ..." -> "Product"
        Matcher m = Pattern.compile("(?i)\\b(?:a|an)\\s+([A-Za-z]+)").matcher(entityDescription);
        if (m.find()) {
            String word = m.group(1);
            return Character.toUpperCase(word.charAt(0)) + word.substring(1);
        }
        return "GeneratedEntity";
    }

    private int len(String s) {
        return s == null ? 0 : s.length();
    }
}
