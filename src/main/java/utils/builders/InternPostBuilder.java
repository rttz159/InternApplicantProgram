package utils.builders;

import adt.ArrayList;
import adt.HashSet;
import adt.ListInterface;
import adt.OrderPair;
import adt.SetInterface;
import com.fasterxml.uuid.Generators;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;

/**
 *
 * @author rttz159
 */
public class InternPostBuilder {
    private String internPostId;
    private String title;
    private String desc;
    private Location location;
    private OrderPair<Double, Double> minMaxSalary;
    private SetInterface<Qualification> internPostQualifications = new HashSet<>();
    private SetInterface<Experience> internPostExperiences = new HashSet<>();
    private SetInterface<Skill> internPostSkills = new HashSet<>();
    private ListInterface<Application> internPostApplications = new ArrayList<>();

    public InternPostBuilder() {
        this.internPostId = generateUUIDv1();
    }

    private String generateUUIDv1() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public InternPostBuilder title(String title) {
        this.title = title;
        return this;
    }

    public InternPostBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    public InternPostBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public InternPostBuilder minMaxSalary(OrderPair<Double, Double> minMaxSalary) {
        this.minMaxSalary = minMaxSalary;
        return this;
    }

    public InternPostBuilder qualifications(SetInterface<Qualification> qualifications) {
        this.internPostQualifications = qualifications;
        return this;
    }

    public InternPostBuilder experiences(SetInterface<Experience> experiences) {
        this.internPostExperiences = experiences;
        return this;
    }

    public InternPostBuilder skills(SetInterface<Skill> skills) {
        this.internPostSkills = skills;
        return this;
    }

    public InternPostBuilder applications(ListInterface<Application> applications) {
        this.internPostApplications = applications;
        return this;
    }

    public InternPost build() {
        return new InternPost(internPostId, title, desc, location, minMaxSalary, internPostQualifications, internPostExperiences, internPostSkills, internPostApplications);
    }
}
