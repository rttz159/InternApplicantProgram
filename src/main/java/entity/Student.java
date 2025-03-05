package entity;

import adt.ArrayList;
import adt.List;
import adt.HashSet;
import adt.Set;

/**
 *
 * @author rttz159
 */
public class Student extends User{

    private String name;
    private int age;
    private Set<Qualification> studentQualifications;
    private Set<Experience> studentExperiences;
    private Set<Skill> studentSkills;
    private List<Application> studentApplications;

    public Student(String userId, String username, String password, String contactno, String email, Location location, String name, int age) {
        super(userId,username,password,contactno,email,location);
        this.name = name;
        this.age = age;
        this.studentQualifications = new HashSet<>();
        this.studentExperiences = new HashSet<>();
        this.studentSkills = new HashSet<>();
        this.studentApplications = new ArrayList<>();
    }

    public Student(String userId, String username, String password, String contactno, String email, Location location, String name, int age, Set<Qualification> studentQualifications, Set<Experience> studentExperiences, Set<Skill> studentSkills, List<Application> studentApplications) {
        super(userId,username,password,contactno,email,location);
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

    public Set<Qualification> getStudentQualifications() {
        return studentQualifications;
    }

    public void setStudentQualifications(HashSet<Qualification> studentQualifications) {
        this.studentQualifications = studentQualifications;
    }

    public Set<Experience> getStudentExperiences() {
        return studentExperiences;
    }

    public void setStudentExperiences(HashSet<Experience> studentExperiences) {
        this.studentExperiences = studentExperiences;
    }

    public Set<Skill> getStudentSkills() {
        return studentSkills;
    }

    public void setStudentSkills(HashSet<Skill> studentSkills) {
        this.studentSkills = studentSkills;
    }

    public List<Application> getStudentApplications() {
        return studentApplications;
    }

    public void setStudentApplications(ArrayList<Application> studentApplications) {
        this.studentApplications = studentApplications;
    }

}
