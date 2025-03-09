package entity;

import adt.ArrayList;
import adt.HashSet;
import adt.OrderPair;
import adt.SetInterface;
import adt.ListInterface;

/**
 *
 * @author rttz159
 */
public class InternPost {

    private String interPostId;
    private String title;
    private String desc;
    private Location location;
    private OrderPair<Double, Double> minMaxSalary;
    private SetInterface<Qualification> internPostQualifications;
    private SetInterface<Experience> interPostExperiences;
    private SetInterface<Skill> internPostSkills;
    private ListInterface<Application> internPostApplications;

    public InternPost(String interPostId, String title, String desc, Location location, OrderPair<Double, Double> minMaxSalary) {
        this.interPostId = interPostId;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.minMaxSalary = minMaxSalary;
        this.internPostQualifications = new HashSet<>();
        this.interPostExperiences = new HashSet<>();
        this.internPostSkills = new HashSet<>();
        this.internPostApplications = new ArrayList<>();
    }

    public InternPost(String interPostId, String title, String desc, Location location, OrderPair<Double, Double> minMaxSalary, SetInterface<Qualification> internPostQualifications, SetInterface<Experience> interPostExperiences, SetInterface<Skill> internPostSkills, ListInterface<Application> internPostApplications) {
        this.interPostId = interPostId;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.minMaxSalary = minMaxSalary;
        this.internPostQualifications = internPostQualifications;
        this.interPostExperiences = interPostExperiences;
        this.internPostSkills = internPostSkills;
        this.internPostApplications = internPostApplications;
    }

    public String getInterPostId() {
        return interPostId;
    }

    public void setInterPostId(String interPostId) {
        this.interPostId = interPostId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OrderPair<Double, Double> getMinMaxSalary() {
        return minMaxSalary;
    }

    public void setMinMaxSalary(OrderPair<Double, Double> minMaxSalary) {
        this.minMaxSalary = minMaxSalary;
    }

    public SetInterface<Qualification> getInternPostQualifications() {
        return internPostQualifications;
    }

    public void setInternPostQualifications(HashSet<Qualification> internPostQualifications) {
        this.internPostQualifications = internPostQualifications;
    }

    public SetInterface<Experience> getInterPostExperiences() {
        return interPostExperiences;
    }

    public void setInterPostExperiences(HashSet<Experience> interPostExperiences) {
        this.interPostExperiences = interPostExperiences;
    }

    public SetInterface<Skill> getInternPostSkills() {
        return internPostSkills;
    }

    public void setInternPostSkills(HashSet<Skill> internPostSkills) {
        this.internPostSkills = internPostSkills;
    }

    public ListInterface<Application> getInternPostApplications() {
        return internPostApplications;
    }

    public void setInternPostApplications(ListInterface<Application> internPostApplications) {
        this.internPostApplications = internPostApplications;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InternPost internPost = (InternPost) obj;
        return interPostId.equals(internPost.interPostId);
    }

    @Override
    public int hashCode() {
        return interPostId.hashCode();
    }

}
