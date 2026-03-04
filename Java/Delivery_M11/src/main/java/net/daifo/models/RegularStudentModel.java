package net.daifo.models;

public class RegularStudentModel extends StudentModel {
    public RegularStudentModel(
            String firstName,
            String lastName,
            int age
    ) {
        super(firstName, lastName, age);
    }

    @Override
    public String getType() { return "Regular"; }
}
