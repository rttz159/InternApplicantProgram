package entity;

import adt.ArrayList;
import adt.HashSet;
import adt.OrderPair;

/**
 *
 * @author rttz159
 */
public class InternPost {
    private String interPostId;
    private String title;
    private String desc;
    private Location location;
    private OrderPair<Double,Double> minMaxSalary;
    private HashSet<Qualification> internPostQualifications;
    private HashSet<Experience> interPostExperiences;
    private HashSet<Skill> internPostSkills;
    private ArrayList<Application> internPostApplications;

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

    public InternPost(String interPostId, String title, String desc, Location location, OrderPair<Double, Double> minMaxSalary, HashSet<Qualification> internPostQualifications, HashSet<Experience> interPostExperiences, HashSet<Skill> internPostSkills, ArrayList<Application> internPostApplications) {
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

    public HashSet<Qualification> getInternPostQualifications() {
        return internPostQualifications;
    }

    public void setInternPostQualifications(HashSet<Qualification> internPostQualifications) {
        this.internPostQualifications = internPostQualifications;
    }

    public HashSet<Experience> getInterPostExperiences() {
        return interPostExperiences;
    }

    public void setInterPostExperiences(HashSet<Experience> interPostExperiences) {
        this.interPostExperiences = interPostExperiences;
    }

    public HashSet<Skill> getInternPostSkills() {
        return internPostSkills;
    }

    public void setInternPostSkills(HashSet<Skill> internPostSkills) {
        this.internPostSkills = internPostSkills;
    }

    public ArrayList<Application> getInternPostApplications() {
        return internPostApplications;
    }

    public void setInternPostApplications(ArrayList<Application> internPostApplications) {
        this.internPostApplications = internPostApplications;
    }
    
    
}
