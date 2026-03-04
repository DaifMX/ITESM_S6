package net.daifo.models;

import java.util.UUID;

public class StudentModel extends PersonModel {
    private String id;

    public StudentModel(String firstName, String lastName, int age) {
        super(firstName, lastName, age);
        this.id = UUID.randomUUID().toString();
        System.out.println("Student: " + this.getId() + " was created. Type: " + this.getType());
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return "NULL";
    }
}
