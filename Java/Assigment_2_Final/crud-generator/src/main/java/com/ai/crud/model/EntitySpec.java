package com.ai.crud.model;

/**
 * A lightweight, parsed description of the entity the user wants to generate.
 *
 * The pipeline works primarily from the free-text {@code description}; {@code name}
 * is a best-effort guess extracted from that text and is mainly used for logging and
 * for naming the output directory before the model has produced any code.
 */
public class EntitySpec {

    private String name;
    private String description;

    public EntitySpec() {
    }

    public EntitySpec(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EntitySpec{name='" + name + "', description='" + description + "'}";
    }
}
