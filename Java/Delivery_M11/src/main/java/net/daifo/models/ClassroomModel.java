package net.daifo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ClassroomModel {
    private String id;

    private ArrayList<StudentModel> students;

    private HashMap<String, Double> grades;

    public ClassroomModel() {
        this.id = UUID.randomUUID().toString();
        this.students = new ArrayList<>();
        this.grades = new HashMap<>();
        System.out.println("Classroom: " + this.id + " was created.");
    }

    // ID
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    // Students
    public ArrayList<StudentModel> getStudents() {
        return students;
    }
    public void setStudents(ArrayList<StudentModel> students) {
        this.students = students;
    }

    // Grades
    public HashMap<String, Double> getGrades() {
        return grades;
    }
    public double[] getGradesArray() {
        double[] gradesArray = new double[students.size()];
        for (int i = 0; i < gradesArray.length; i++) {
            gradesArray[i] = grades.get(students.get(i).getId());
        }

        return gradesArray;
    }
    public void setGrades(HashMap<String, Double> grades) {
        this.grades = grades;
    }
}