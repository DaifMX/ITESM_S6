package com.ai.crud.model;

/**
 * The complete output of the orchestrated pipeline: the generated CRUD code plus the
 * generated Service test class. This is what the REST API returns.
 */
public class CRUDResult {

    private GeneratedCRUD crud;
    private String serviceTests;

    public CRUDResult() {
    }

    public CRUDResult(GeneratedCRUD crud, String serviceTests) {
        this.crud = crud;
        this.serviceTests = serviceTests;
    }

    public GeneratedCRUD getCrud() {
        return crud;
    }

    public void setCrud(GeneratedCRUD crud) {
        this.crud = crud;
    }

    public String getServiceTests() {
        return serviceTests;
    }

    public void setServiceTests(String serviceTests) {
        this.serviceTests = serviceTests;
    }
}
