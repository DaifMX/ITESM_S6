package net.daifo.services;

/**
 * Service class for performing operations on student grades.
 */
public class StatsService {
    /**
     * Calculates the average of all grades in the array.
     *
     * @param grades array of grades
     * @return the group average
     */
    public double calculateAverage(double[] grades) {
        int size = grades.length;
        int i = size - 1;
        double total = this.sumGrades(grades, i);

        return total / size;
    }

    /**
     * Determines whether a grade is a pass or fail.
     *
     * @param grade the grade to evaluate (must be between 0 and 10)
     * @return "Passed" if grade >= 6.0, "Failed" otherwise
     * @throws IllegalArgumentException if the grade is not between 0 and 10
     */
    public String getStatus(double grade) throws IllegalArgumentException {
        if (grade < 0 || grade > 10.0) {
            throw new IllegalArgumentException("Grade must be between 0 and 10.0");
        }

        if (grade >= 6) {
            return "Passed";
        } else {
            return "Failed";
        }
    }

    /**
     * Finds the highest grade in the array.
     *
     * @param grades array of grades
     * @return the maximum grade
     */
    public double highestGrade(double[] grades) {
        double max = Double.NEGATIVE_INFINITY;
        for (double grade : grades) {
            max = Math.max(grade, max);
        }

        return max;
    }

    /**
     * Finds the lowest grade in the array.
     *
     * @param grades array of grades
     * @return the minimum grade
     */
    public double lowestGrade(double[] grades) {
        double min = Double.POSITIVE_INFINITY;
        for (double grade : grades) {
            min = Math.min(grade, min);
        }

        return min;
    }

    /**
     * Counts how many grades are passing (>= 6.0).
     *
     * @param grades array of grades
     * @return the number of passing grades
     */
    public int countPassed(double[] grades) {
        int count = 0;
        for (double grade : grades) {
            if (grade >= 6) {
                count++;
            }
        }

        return count;
    }

    /**
     * Recursively sums the grades in the array from index 0 to i.
     *
     * @param grades array of grades
     * @param i the current index to sum up to (starts at grades.length - 1)
     * @return the sum of grades from index 0 to i
     */
    private double sumGrades(double[] grades, int i) {
        if (i < 0) { return 0.0; }

        return grades[i] + sumGrades(grades, i - 1);
    }
}