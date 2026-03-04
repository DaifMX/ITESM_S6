package net.daifo.models;

public class PersonModel {
    private String firstName;

    private String lastName;

    private int age;

    public PersonModel(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // FirstName
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // LastName
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Age
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}