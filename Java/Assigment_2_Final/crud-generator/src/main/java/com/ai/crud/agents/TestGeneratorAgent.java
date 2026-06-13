package com.ai.crud.agents;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Agent #2: given a Java source class, generates a comprehensive JUnit 5 + Mockito test class.
 */
@Service
public class TestGeneratorAgent {

    private static final Logger log = LoggerFactory.getLogger(TestGeneratorAgent.class);

    private final ChatLanguageModel model;

    public TestGeneratorAgent(ChatLanguageModel model) {
        this.model = model;
    }

    public String generateTests(String javaSourceCode, String className) {
        if (javaSourceCode == null || javaSourceCode.isBlank()) {
            log.warn("No source code provided to TestGeneratorAgent for {}; skipping", className);
            return "";
        }

        String prompt = """
                You are an expert in Java testing. Generate comprehensive JUnit 5 tests
                for the following class named %s. Requirements:
                - Use @ExtendWith(MockitoExtension.class)
                - Mock all dependencies with @Mock and inject with @InjectMocks
                - Cover: happy path, null inputs, edge cases, exception scenarios
                - Use descriptive test method names (given_when_then pattern)
                - Minimum 5 test methods for Service classes
                Return ONLY the Java test class, no markdown fences and no commentary.

                Class to test:
                %s
                """.formatted(className, javaSourceCode);

        log.info("Generating tests for {} ({} chars of source)", className, javaSourceCode.length());
        String tests = model.generate(prompt);
        return stripMarkdownFences(tests);
    }

    private String stripMarkdownFences(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("(?m)^\\s*```[a-zA-Z]*\\s*$", "").trim();
    }
}
