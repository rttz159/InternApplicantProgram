package dao;

import adt.ArrayList;
import adt.HashSet;
import dao.StudentDAO;
import entity.*;

public class StudentDriver {
    public static void main(String[] args) {
        // Creating location
        Location location = new Location("New York", "123 Street, NY");

        // Creating associations
        HashSet<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.BACHELOR_DEGREE, "Computer Science", 4, "NY University", 2023));

        HashSet<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.TECHNICAL, "Java", 5));

        HashSet<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Software Developer Internship", Experience.IndustryType.TECHNOLOGY, 6));

        ArrayList<Application> applications = new ArrayList<>();
        applications.append(new Application("A1", "P1", "S123", Application.Status.PENDING, null));

        // Creating Student
        Student student = new Student("S123", "testuser", "password123", "1234567890", "test@mail.com", location, "Test User", 22,
                qualifications, experiences, skills, applications);

        // Insert student into DB
        StudentDAO.insertStudent(student);

        // Retrieve and print student data
        Student retrievedStudent = StudentDAO.getStudentById("S123");
        if (retrievedStudent != null) {
            System.out.println("\nRetrieved Student Data:");
            System.out.println(retrievedStudent);
        } else {
            System.out.println("Student not found.");
        }

        // Update associations
        retrievedStudent.getStudentSkills().add(new Skill("S2", Skill.SkillType.TECHNICAL, "Python", 4));
        retrievedStudent.getStudentQualifications().add(new Qualification("Q2", Qualification.QualificationType.MASTER_DEGREE, "Software Engineering", 2, "NY University", 2025));

        boolean updateSuccess = StudentDAO.updateStudentById(retrievedStudent);
        System.out.println(updateSuccess ? "Student updated successfully!" : "Failed to update student.");

        // Retrieve and print updated data
        Student updatedStudent = StudentDAO.getStudentById("S123");
        if (updatedStudent != null) {
            System.out.println("\nUpdated Student Data:");
            System.out.println(updatedStudent);
        }

        // Delete student and check if removed
        boolean deleteSuccess = StudentDAO.deleteStudentById("S123");
        System.out.println(deleteSuccess ? "Student deleted successfully!" : "Failed to delete student.");

        Student deletedStudent = StudentDAO.getStudentById("S123");
        System.out.println(deletedStudent == null ? "Student not found after deletion." : "Student still exists!");

        System.out.println("\nDriver program execution completed.");
    }
}