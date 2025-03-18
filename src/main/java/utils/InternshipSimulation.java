package utils;

import utils.builders.SkillBuilder;
import utils.builders.ExperienceBuilder;
import utils.builders.InterviewBuilder;
import utils.builders.CompanyBuilder;
import utils.builders.StudentBuilder;
import utils.builders.ApplicationBuilder;
import utils.builders.QualificationBuilder;
import utils.builders.InternPostBuilder;
import adt.HashSet;
import adt.OrderPair;
import adt.ArrayList;
import adt.ListInterface;
import adt.SetInterface;
import dao.CompanyDAO;
import dao.DatabaseSetup;
import dao.StudentDAO;
import java.util.Random;
import entity.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author raint
 */
public class InternshipSimulation {

    public static Student getMockStudent() {
        Location location = new Location("SELANGOR", "123, Jalan University, Petaling Jaya");

        SetInterface<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.BACHELOR_DEGREE, "Computer Science", 1, "University of Malaya", 2024));
        qualifications.add(new Qualification("Q2", Qualification.QualificationType.CERTIFICATION, "AWS Certified Developer", 1, "AWS", 2023));

        SetInterface<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Software Development Intern at ABC Corp", Experience.IndustryType.TECHNOLOGY, 6));
        experiences.add(new Experience("E2", "Freelance Web Developer", Experience.IndustryType.TECHNOLOGY, 12));

        SetInterface<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.PROGRAMMING, "Java", 5));
        skills.add(new Skill("S2", Skill.SkillType.PROGRAMMING, "Python", 4));
        skills.add(new Skill("S3", Skill.SkillType.CLOUD_COMPUTING, "AWS", 3));

        ListInterface<Application> applications = new ArrayList<>();

        return new Student(
                "S12345",
                "john_doe",
                "password123",
                "012-3456789",
                "john.doe@example.com",
                location,
                "John Doe",
                22,
                qualifications,
                experiences,
                skills,
                applications
        );
    }

    public static Student getQualifiedHospitalityStudent() {
        Location location = new Location("KUALA_LUMPUR", "Company HQ, KUALA_LUMPUR");

        SetInterface<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.DOCTORATE, "Hospitality Management", 4, "University of Kuala Lumpur", 2022));

        SetInterface<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Hotel Manager", Experience.IndustryType.HOSPITALITY, 5));

        SetInterface<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.LEADERSHIP, "Team Management", 2));

        ListInterface<Application> applications = new ArrayList<>();

        return new Student(
                "S67890",
                "jane_doe",
                "securePass456",
                "013-9876543",
                "jane.doe@example.com",
                location,
                "Jane Doe",
                30,
                qualifications,
                experiences,
                skills,
                applications
        );
    }

    public static void main(String[] args) {
        Random rand = new Random();

        // === Skill and Qualification Types ===
        Skill.SkillType[] skillTypes = Skill.SkillType.values();
        Qualification.QualificationType[] qualificationTypes = Qualification.QualificationType.values();
        Experience.IndustryType[] industryTypes = Experience.IndustryType.values();
        Location.MalaysianRegion[] locations = Location.MalaysianRegion.values();

        // === Create Students with Realistic Qualifications & Skills ===
        ListInterface<Student> students = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SetInterface<Qualification> qualifications = new HashSet<>();
            SetInterface<Skill> skills = new HashSet<>();
            SetInterface<Experience> experiences = new HashSet<>();

            Qualification.QualificationType degreeType = qualificationTypes[rand.nextInt(3) + 2];  // Bachelor, Master, or Doctorate
            Skill.SkillType primarySkill = skillTypes[rand.nextInt(skillTypes.length)];
            Experience.IndustryType industry = industryTypes[i % industryTypes.length];
            Location.MalaysianRegion region = locations[i % locations.length];

            qualifications.add(new QualificationBuilder()
                    .qualificationType(degreeType)
                    .desc("Degree in " + industry.name().replace("_", " "))
                    .level(rand.nextInt(4) + 1)
                    .institution("University of Malaysia " + region.name())
                    .yearOfComplete(2018 + rand.nextInt(5))
                    .build());

            skills.add(new SkillBuilder()
                    .skillType(primarySkill)
                    .name(primarySkill.name().replace("_", " ") + " Expertise")
                    .proficiencyLevel(rand.nextInt(5) + 1)
                    .build());

            experiences.add(new ExperienceBuilder()
                    .desc("Internship in " + industry.name().replace("_", " "))
                    .industryType(industry)
                    .duration(rand.nextInt(12) + 1)
                    .build());

            students.append(new StudentBuilder()
                    .username("student" + i)
                    .password("password" + i)
                    .contactNo("123-456-789" + i)
                    .email("student" + i + "@mail.com")
                    .name("Student " + i)
                    .age(20 + i)
                    .qualifications(qualifications)
                    .skills(skills)
                    .experiences(experiences)
                    .applications(new ArrayList<>())
                    .location(new Location(region.toString(), "123 Street, " + region.name()))
                    .build());
        }

        // === Create Companies in Relevant Locations ===
        ListInterface<Company> companies = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Experience.IndustryType industry = industryTypes[i % industryTypes.length];
            Location.MalaysianRegion region = locations[i % locations.length];

            companies.append(new CompanyBuilder()
                    .username("company" + i)
                    .password("password" + i)
                    .contactNo("987-654-321" + i)
                    .email("company" + i + "@business.com")
                    .companyName("Company " + i)
                    .industryType(industry)
                    .location(new Location(region.toString(), "Business District, " + region.name()))
                    .internPosts(new ArrayList<>())
                    .build());
        }

        // === Create Intern Posts with Realistic Requirements ===
        ListInterface<InternPost> internPosts = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            SetInterface<Qualification> requiredQualifications = new HashSet<>();
            SetInterface<Skill> requiredSkills = new HashSet<>();
            SetInterface<Experience> requiredExperience = new HashSet<>();

            Qualification.QualificationType requiredDegree = qualificationTypes[rand.nextInt(3) + 2];
            Skill.SkillType requiredSkill = skillTypes[rand.nextInt(skillTypes.length)];
            Experience.IndustryType industry = industryTypes[i % industryTypes.length];
            Location.MalaysianRegion region = locations[i % locations.length];

            requiredQualifications.add(new QualificationBuilder()
                    .qualificationType(requiredDegree)
                    .desc("Required Degree in " + industry.name().replace("_", " "))
                    .level(rand.nextInt(4) + 1)
                    .institution("Top University")
                    .yearOfComplete(2022)
                    .build());

            requiredSkills.add(new SkillBuilder()
                    .skillType(requiredSkill)
                    .name("Expertise in " + requiredSkill.name().replace("_", " "))
                    .proficiencyLevel(rand.nextInt(3) + 2)
                    .build());

            requiredExperience.add(new ExperienceBuilder()
                    .desc("Experience in " + industry.name().replace("_", " "))
                    .industryType(industry)
                    .duration(rand.nextInt(6) + 1)
                    .build());
            
            Random rd = new Random();
            InternPost post = new InternPostBuilder()
                    .title(industry.name().replace("_", " ") + " Internship")
                    .desc("Exciting opportunity in " + industry.name().replace("_", " "))
                    .location(new Location(region.toString(), "Company HQ, " + region.name()))
                    .minMaxSalary(new OrderPair<>(2000.0 + rand.nextInt(500), 3500.0 + rand.nextInt(1000)))
                    .qualifications(requiredQualifications)
                    .skills(requiredSkills)
                    .experiences(requiredExperience)
                    .applications(new ArrayList<>())
                    .status(rd.nextBoolean())
                    .build();

            internPosts.append(post);
            companies.getEntry(i % 5).getInternPosts().append(post);
        }

        // === Create Applications if Students Qualify ===
        ListInterface<Application> applications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = students.getEntry(i % 5);
            InternPost post = internPosts.getEntry(rand.nextInt(internPosts.getNumberOfEntries()));

            boolean qualified = false;
            for (Qualification q : student.getStudentQualifications()) {
                for (Qualification rq : post.getInternPostQualifications()) {
                    if (q.getQualificationType().equals(rq.getQualificationType()) && q.getLevel() >= rq.getLevel()) {
                        qualified = true;
                        break;
                    }
                }
            }

            for (Skill s : student.getStudentSkills()) {
                for (Skill rs : post.getInternPostSkills()) {
                    if (s.getSkilltype().equals(rs.getSkilltype()) && s.getProficiencyLevel() >= rs.getProficiencyLevel()) {
                        qualified = true;
                        break;
                    }
                }
            }

            if (qualified) {
                Interview interview = new InterviewBuilder()
                        .date(LocalDate.now().plusDays(rand.nextInt(14)))
                        .startTime(LocalTime.of(9 + rand.nextInt(3), 0))
                        .build();

                Application application = new ApplicationBuilder()
                        .applicantId(student.getUserId())
                        .internPostId(post.getInterPostId())
                        .status(Application.Status.PENDING)
                        .interview(interview)
                        .build();

                applications.append(application);
                student.getStudentApplications().append(application);
                post.getInternPostApplications().append(application);
            }
        }

        // Output Summary
        DatabaseSetup.setUpDatabase();
        for (var x : students) {
            StudentDAO.insertStudent(x);
        }
        for (var x : companies) {
            CompanyDAO.insertCompany(x);
        }
        System.out.println("Students: " + students.getNumberOfEntries());
        System.out.println("Companies: " + companies.getNumberOfEntries());
        System.out.println("Internship Posts: " + internPosts.getNumberOfEntries());
        System.out.println("Applications: " + applications.getNumberOfEntries());
    }
}
