package net.daifo.models;

public class ScholarshipStudentModel extends StudentModel {
    public ScholarshipStudentModel(
            String firstName,
            String lastName,
            int age
    ) {
        super(firstName, lastName, age);
    }

    @Override
    public String getType() { return "Scholarship"; }
}
