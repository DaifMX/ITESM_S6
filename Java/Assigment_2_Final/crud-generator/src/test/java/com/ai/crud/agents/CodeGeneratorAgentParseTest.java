package com.ai.crud.agents;

import com.ai.crud.model.GeneratedCRUD;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Offline unit tests for the response parser — no Ollama required.
 * Verifies the {@code === X.java ===} sections are split correctly and the entity name is derived.
 */
class CodeGeneratorAgentParseTest {

    private final CodeGeneratorAgent agent = new CodeGeneratorAgent(null, null);

    @Test
    void parsesAllFourSectionsAndEntityName() {
        String response = """
                === Entity.java ===
                package com.ai.crud.entity;
                public class Product {
                    private Long id;
                }
                === Repository.java ===
                package com.ai.crud.repository;
                public interface ProductRepository {}
                === Service.java ===
                package com.ai.crud.service;
                public class ProductService {}
                === Controller.java ===
                package com.ai.crud.controller;
                public class ProductController {}
                """;

        GeneratedCRUD crud = agent.parseResponse(response, "A Product with name and price");

        assertEquals("Product", crud.getEntityName());
        assertTrue(crud.getEntityCode().contains("class Product"));
        assertTrue(crud.getRepositoryCode().contains("interface ProductRepository"));
        assertTrue(crud.getServiceCode().contains("class ProductService"));
        assertTrue(crud.getControllerCode().contains("class ProductController"));
    }

    @Test
    void stripsMarkdownFencesAndFallsBackToDescriptionForName() {
        String response = """
                ```java
                === Entity.java ===
                // no type declaration here on purpose
                @Entity
                ```
                === Service.java ===
                public class WidgetService {}
                """;

        GeneratedCRUD crud = agent.parseResponse(response, "An Widget with color");

        // No "class X" in the entity section, so the name is derived from the description.
        assertEquals("Widget", crud.getEntityName());
        assertTrue(crud.getServiceCode().contains("class WidgetService"));
        assertTrue(crud.getRawResponse().contains("```") , "raw response is preserved verbatim");
    }
}
