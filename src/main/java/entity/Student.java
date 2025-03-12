package entity;

import adt.ArrayList;
import adt.HashSet;
import adt.SetInterface;
import adt.ListInterface;

/**
 *
 * @author rttz159
 */
public class Student extends User {

    private String name;
    private int age;
    private SetInterface<Qualification> studentQualifications;
    private SetInterface<Experience> studentExperiences;
    private SetInterface<Skill> studentSkills;
    private ListInterface<Application> studentApplications;

    public Student(String userId, String username, String password, String contactno, String email, Location location, String name, int age) {
        super(userId, username, password, contactno, email, location);
        this.name = name;
        this.age = age;
        this.studentQualifications = new HashSet<>();
        this.studentExperiences = new HashSet<>();
        this.studentSkills = new HashSet<>();
        this.studentApplications = new ArrayList<>();
    }

    public Student(String userId, String username, String password, String contactno, String email, Location location, String name, int age, SetInterface<Qualification> studentQualifications, SetInterface<Experience> studentExperiences, SetInterface<Skill> studentSkills, ListInterface<Application> studentApplications) {
        super(userId, username, password, contactno, email, location);
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

    public SetInterface<Qualification> getStudentQualifications() {
        return studentQualifications;
    }

    public void setStudentQualifications(HashSet<Qualification> studentQualifications) {
        this.studentQualifications = studentQualifications;
    }

    public SetInterface<Experience> getStudentExperiences() {
        return studentExperiences;
    }

    public void setStudentExperiences(HashSet<Experience> studentExperiences) {
        this.studentExperiences = studentExperiences;
    }

    public SetInterface<Skill> getStudentSkills() {
        return studentSkills;
    }

    public void setStudentSkills(HashSet<Skill> studentSkills) {
        this.studentSkills = studentSkills;
    }

    public ListInterface<Application> getStudentApplications() {
        return studentApplications;
    }

    public void setStudentApplications(ArrayList<Application> studentApplications) {
        this.studentApplications = studentApplications;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;
        return userId.equals(student.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    public Student deepCopy() {
        SetInterface<Qualification> copiedQualifications = new HashSet<>();
        for (Qualification q : this.studentQualifications) {
            copiedQualifications.add(q.deepCopy());
        }

        SetInterface<Experience> copiedExperiences = new HashSet<>();
        for (Experience e : this.studentExperiences) {
            copiedExperiences.add(e.deepCopy());
        }

        SetInterface<Skill> copiedSkills = new HashSet<>();
        for (Skill s : this.studentSkills) {
            copiedSkills.add(s.deepCopy());
        }

        ListInterface<Application> copiedApplications = new ArrayList<>();
        for (Application a : this.studentApplications) {
            copiedApplications.append(a.deepCopy());
        }

        return new Student(
                this.getUserId(),
                this.getUsername(),
                this.getPassword(),
                this.getContactno(),
                this.getEmail(),
                this.getLocation() != null ? this.getLocation().deepCopy() : null,
                this.name,
                this.age,
                copiedQualifications,
                copiedExperiences,
                copiedSkills,
                copiedApplications
        );
    }

}
