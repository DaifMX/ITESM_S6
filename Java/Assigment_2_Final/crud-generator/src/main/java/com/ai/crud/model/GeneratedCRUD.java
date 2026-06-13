package com.ai.crud.model;

/**
 * The four Spring Boot source files produced by the {@code CodeGeneratorAgent} for a
 * single entity, plus the entity name and the raw model response (kept for debugging /
 * evidence).
 */
public class GeneratedCRUD {

    private String entityName;
    private String entityCode;
    private String repositoryCode;
    private String serviceCode;
    private String controllerCode;
    private String rawResponse;

    public GeneratedCRUD() {
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getControllerCode() {
        return controllerCode;
    }

    public void setControllerCode(String controllerCode) {
        this.controllerCode = controllerCode;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }
}
