package dao;

import entity.*;
import adt.HashSet;
import adt.Set;
import adt.ArrayList;

public class StudentDAOTest {

    public static void main(String[] args) {
        // Create a test student with qualifications, skills, and experiences
        DatabaseSetup instance =  new DatabaseSetup();
        Location location = new Location("Test City", "123 Test Address");

        Set<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.BACHELOR_DEGREE, "Computer Science", 3, "Test University", 2020));

        Set<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.PROGRAMMING, "Java", 5));

        Set<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Software Developer", Experience.IndustryType.TECHNOLOGY, 2));

        Student testStudent = new Student(
                "TEST123", "testuser", "password", "1234567890", "test@example.com",
                location, "Test User", 25, qualifications, experiences, skills, new ArrayList<>()
        );

        // Test INSERT operation
        System.out.println("Inserting student...");
        StudentDAO.insertStudent(testStudent);
        System.out.println("Student inserted successfully!");

        // Test GET operation
        System.out.println("\nFetching student by ID...");
        Student retrievedStudent = StudentDAO.getStudentById("TEST123");
        if (retrievedStudent != null) {
            System.out.println("Student found:");
            System.out.println("User ID: " + retrievedStudent.getUserId());
            System.out.println("Name: " + retrievedStudent.getName());
            System.out.println("Email: " + retrievedStudent.getEmail());
            System.out.println("Age: " + retrievedStudent.getAge());
            System.out.println("Qualifications: " + retrievedStudent.getStudentQualifications().size());
            System.out.println("Skills: " + retrievedStudent.getStudentSkills().size());
            System.out.println("Experiences: " + retrievedStudent.getStudentExperiences().size());
        } else {
            System.out.println("Student not found!");
        }

        // Test UPDATE operation
        System.out.println("\nUpdating student...");
        testStudent.setAge(26);
        testStudent.setEmail("updated@example.com");
        boolean isUpdated = StudentDAO.updateStudentById(testStudent);
        if (isUpdated) {
            System.out.println("Student updated successfully!");
            Student updatedStudent = StudentDAO.getStudentById("TEST123");
            System.out.println("Updated Age: " + updatedStudent.getAge());
            System.out.println("Updated Email: " + updatedStudent.getEmail());
        } else {
            System.out.println("Failed to update student!");
        }

        // Test DELETE operation
        System.out.println("\nDeleting student...");
        boolean isDeleted = StudentDAO.deleteStudentById("TEST123");
        if (isDeleted) {
            System.out.println("Student deleted successfully!");
        } else {
            System.out.println("Failed to delete student!");
        }

        // Verify deletion
        System.out.println("\nFetching student after deletion...");
        Student deletedStudent = StudentDAO.getStudentById("TEST123");
        if (deletedStudent == null) {
            System.out.println("Student not found (deletion verified).");
        } else {
            System.out.println("Student still exists!");
        }
    }
}