package entity;

import adt.ArrayList;
import adt.HashSet;

/**
 *
 * @author rttz159
 */
public class Student {
    private String name;
    private int age;
    private HashSet<Qualification> studentQualifications;
    private HashSet<Experience> studentExperiences;
    private HashSet<Skill> studentSkills;
    private ArrayList<Application> studentApplications;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        this.studentQualifications = new HashSet<>();
        this.studentExperiences = new HashSet<>();
        this.studentSkills = new HashSet<>();
        this.studentApplications = new ArrayList<>();
    }

    public Student(String name, int age, HashSet<Qualification> studentQualifications, HashSet<Experience> studentExperiences, HashSet<Skill> studentSkills, ArrayList<Application> studentApplications) {
        this.name = name;
        this.age = age;
        this.studentQualifications = studentQualifications;
        this.studentExperiences = studentExperiences;
        this.studentSkills = studentSkills;
        this.studentApplications = studentApplications;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public HashSet<Qualification> getStudentQualifications() {
        return studentQualifications;
    }

    public void setStudentQualifications(HashSet<Qualification> studentQualifications) {
        this.studentQualifications = studentQualifications;
    }

    public HashSet<Experience> getStudentExperiences() {
        return studentExperiences;
    }

    public void setStudentExperiences(HashSet<Experience> studentExperiences) {
        this.studentExperiences = studentExperiences;
    }

    public HashSet<Skill> getStudentSkills() {
        return studentSkills;
    }

    public void setStudentSkills(HashSet<Skill> studentSkills) {
        this.studentSkills = studentSkills;
    }

    public ArrayList<Application> getStudentApplications() {
        return studentApplications;
    }

    public void setStudentApplications(ArrayList<Application> studentApplications) {
        this.studentApplications = studentApplications;
    }
    
}
