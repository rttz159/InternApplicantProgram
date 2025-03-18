package dao;

import adt.ArrayList;
import adt.HashSet;
import adt.ListInterface;
import adt.SetInterface;
import entity.Application;
import entity.Experience;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;

/**
 *
 * @author USER
 */
public class test {

    public static void main(String[] args) {
        DatabaseSetup.setUpDatabase();
        StudentDAO.deleteStudentById(getQualifiedStudent().getUserId());
        StudentDAO.insertStudent(getQualifiedStudent()); //bobo1231231
    }

    public static Student getQualifiedStudent() {
        Location location = new Location("KUALA_LUMPUR", "Company HQ, KUALA_LUMPUR");

        SetInterface<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.MASTER_DEGREE, "Hospitality Management", 5, "University of Kuala Lumpur", 2022));
        qualifications.add(new Qualification("Q2", Qualification.QualificationType.DOCTORATE, "Hospitality Management", 5, "University of Kuala Lumpur", 2022));

        SetInterface<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Hotel Manager", Experience.IndustryType.RETAIL, 5));
        experiences.add(new Experience("E2", "Energy Manager", Experience.IndustryType.ENERGY, 6));
        experiences.add(new Experience("E3", "Energy Manager", Experience.IndustryType.MANUFACTURING, 6));
        experiences.add(new Experience("E3", "Energy Manager", Experience.IndustryType.HOSPITALITY, 6));

        SetInterface<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.NETWORKING, "Team Management", 4));
        skills.add(new Skill("S2", Skill.SkillType.PROJECT_MANAGEMENT, "Team Management", 4));
        skills.add(new Skill("S3", Skill.SkillType.DATABASE_MANAGEMENT, "Team Management", 5));

        ListInterface<Application> applications = new ArrayList<>();

        return new Student(
                "7897897897",
                "bobo1231231",
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
}
