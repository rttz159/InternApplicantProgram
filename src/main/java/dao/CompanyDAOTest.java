package dao;

import entity.*;
import control.InterviewManager;
import adt.ArrayList;
import adt.OrderPair;
import adt.HashSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import adt.SetInterface;
import adt.ListInterface;

public class CompanyDAOTest {

    public static void main(String[] args) {
        try {
            System.out.println("=== TESTING CompanyDAO METHODS ===");

            // Creating a new company
            Company newCompany = createTestCompany();
            System.out.println("\nInserting new company...");
            CompanyDAO.insertCompany(newCompany);

            // Retrieve the inserted company
            System.out.println("\nRetrieving inserted company...");
            Company retrievedCompany = CompanyDAO.getCompanyById(newCompany.getUserId());
            System.out.println("Retrieved Company: " + retrievedCompany);

            // Testing getCompanies()
            System.out.println("\nRetrieving all companies...");
            ListInterface<Company> companies = CompanyDAO.getCompanies();
            if (companies.isEmpty()) {
                System.out.println("No companies found.");
            } else {
                System.out.println("Companies retrieved successfully:");
                for (Company company : companies) {
                    System.out.println(company);
                }
            }

            // Update the company
            System.out.println("\nUpdating company details...");
            newCompany.setCompanyName("Updated Tech Corp");
            newCompany.setContactno("9998887777");
            boolean updateSuccess = CompanyDAO.updateCompanyById(newCompany);
            System.out.println("Update Successful: " + updateSuccess);

            // Retrieve and verify updated company
            System.out.println("\nRetrieving updated company...");
            retrievedCompany = CompanyDAO.getCompanyById(newCompany.getUserId());
            System.out.println("Updated Company: " + retrievedCompany);

            // Delete the company
            System.out.println("\nDeleting company...");
            boolean deleteSuccess = CompanyDAO.deleteCompanyById(newCompany.getUserId());
            System.out.println("Delete Successful: " + deleteSuccess);

            // Verify deletion
            System.out.println("\nVerifying deletion...");
            Company deletedCompany = CompanyDAO.getCompanyById(newCompany.getUserId());
            System.out.println("Company Found After Deletion: " + (deletedCompany != null));

        } catch (SQLException e) {
            System.out.println("Error during testing: " + e.getMessage());
        }
    }

    private static Company createTestCompany() {
        // Create a test company with dummy data
        String companyId = "C001";
        String username = "tech_admin";
        String password = "securepass";
        String contactNo = "1234567890";
        String email = "tech@example.com";
        Location location = new Location("New York", "123 Tech Street");
        String companyName = "Tech Corp";
        Experience.IndustryType industryType = Experience.IndustryType.TECHNOLOGY;

        // Creating intern posts
        ListInterface<InternPost> internPosts = new ArrayList<>();
        internPosts.append(createTestInternPost("IP001"));
        internPosts.append(createTestInternPost("IP002"));

        // Creating interview manager
        InterviewManager interviewManager = new InterviewManager();
        interviewManager.interviewBooking(LocalDate.now(), LocalTime.of(10, 0));

        return new Company(companyId, username, password, contactNo, email, location, companyName, industryType, internPosts, interviewManager);
    }

    private static InternPost createTestInternPost(String postId) {
        String title = "Software Intern";
        String desc = "Exciting internship opportunity in software development.";
        Location location = new Location("New York", "123 Tech Street");
        OrderPair<Double, Double> salaryRange = new OrderPair<>(3000.0, 5000.0);

        SetInterface<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q001", Qualification.QualificationType.BACHELOR_DEGREE, "CS Degree", 3, "Tech University", 2024));

        SetInterface<Skill> skills = new HashSet<>();
        skills.add(new Skill("S001", Skill.SkillType.PROGRAMMING, "Java", 5));

        SetInterface<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E001", "Software Development", Experience.IndustryType.TECHNOLOGY, 12));

        ListInterface<Application> applications = new ArrayList<>();

        return new InternPost(postId, title, desc, location, salaryRange, qualifications, experiences, skills, applications);
    }
}
