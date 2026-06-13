package com.ai.crud.model;

/**
 * Request body for POST /api/generator/generate.
 *
 * Example: { "entityDescription": "A Product with name, price, category, and stock quantity" }
 */
public class GenerateRequest {

    private String entityDescription;

    public GenerateRequest() {
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }
}
