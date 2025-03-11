package entity;

import adt.ArrayList;
import adt.OrderPair;
import adt.ListInterface;
import control.InterviewManager;
import dao.CompanyDAO;
import java.time.LocalDate;
import java.time.LocalTime;

public class CompanyTest {
    public static void main(String[] args) {
        // Create a location for the company
        Location location = new Location("Selangor", "123, Business Street, Kuala Lumpur");

        // Create an instance of InterviewManager
        InterviewManager interviewManager = new InterviewManager();

        // Create a list to store Intern Posts
        ListInterface<InternPost> internPosts = new ArrayList<>();

        // Creating Intern Posts
        InternPost internPost1 = new InternPost(
                "IP001", "Software Engineer Intern", "Develop and test software applications",
                location, new OrderPair<>(2000.0, 3000.0)
        );

        InternPost internPost2 = new InternPost(
                "IP002", "Data Analyst Intern", "Analyze business data and provide insights",
                location, new OrderPair<>(1800.0, 2500.0)
        );

        InternPost internPost3 = new InternPost(
                "IP003", "Cybersecurity Intern", "Assist in securing company infrastructure",
                location, new OrderPair<>(2200.0, 2800.0)
        );

        // Adding Intern Posts to the list
        internPosts.append(internPost1);
        internPosts.append(internPost2);
        internPosts.append(internPost3);

        // Create a company object
        Company company = new Company(
                "C001", "TechCorp", "securePass123", "012-3456789",
                "contact@techcorp.com", location, "TechCorp Solutions",
                Experience.IndustryType.TECHNOLOGY, internPosts, interviewManager
        );

        // Schedule Interviews
        LocalDate interviewDate = LocalDate.of(2025, 3, 17);
        LocalTime interviewTime1 = LocalTime.of(10, 0);
        LocalTime interviewTime2 = LocalTime.of(14, 30);

        interviewManager.interviewBooking(interviewDate, interviewTime1);
        interviewManager.interviewBooking(interviewDate, interviewTime2);

        // Print company details
        System.out.println("Company Created:");
        System.out.println("ID: " + company.getUserId());
        System.out.println("Username: " + company.getUsername());
        System.out.println("Company Name: " + company.getCompanyName());
        System.out.println("Industry Type: " + company.getIndustryType());
        System.out.println("Contact No: " + company.getContactno());
        System.out.println("Email: " + company.getEmail());
        System.out.println("Location: " + company.getLocation().getFullAddress());

        // Print Intern Posts
        System.out.println("\nIntern Posts:");
        for (int i = 0; i < internPosts.getNumberOfEntries(); i++) {
            InternPost post = internPosts.getEntry(i);
            System.out.println(post.getTitle() + " - Salary: RM" + post.getMinMaxSalary().getX() + " to RM" + post.getMinMaxSalary().getY());
        }

        // Print Interview Slots
        System.out.println("\nScheduled Interviews on " + interviewDate + ":");
        interviewManager.getParticularDaySchedule(interviewDate).showBookedSlots().forEach(slot -> 
            System.out.println("Interview at " + slot.start + " - " + slot.end)
        );
        
        CompanyDAO.insertCompany(company);
    }
}
