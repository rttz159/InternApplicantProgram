package utils.builders;

import adt.HashSet;
import adt.SetInterface;
import adt.ListInterface;
import adt.ArrayList;
import entity.Application;
import entity.Experience;
import entity.Qualification;
import entity.Skill;
import entity.Student;
/**
 *
 * @author Raymond
 */
public class StudentBuilder extends UserBuilder<StudentBuilder> {
    private String name;
    private int age;
    private SetInterface<Qualification> studentQualifications = new HashSet<>();
    private SetInterface<Experience> studentExperiences = new HashSet<>();
    private SetInterface<Skill> studentSkills = new HashSet<>();
    private ListInterface<Application> studentApplications = new ArrayList<>();

    public StudentBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StudentBuilder age(int age) {
        this.age = age;
        return this;
    }

    public StudentBuilder qualifications(SetInterface<Qualification> qualifications) {
        this.studentQualifications = qualifications;
        return this;
    }

    public StudentBuilder experiences(SetInterface<Experience> experiences) {
        this.studentExperiences = experiences;
        return this;
    }

    public StudentBuilder skills(SetInterface<Skill> skills) {
        this.studentSkills = skills;
        return this;
    }

    public StudentBuilder applications(ListInterface<Application> applications) {
        this.studentApplications = applications;
        return this;
    }

    public Student build() {
        return new Student(userId, username, password, contactNo, email, location, name, age, studentQualifications, studentExperiences, studentSkills, studentApplications);
    }

    @Override
    protected StudentBuilder self() {
        return this;
    }
}