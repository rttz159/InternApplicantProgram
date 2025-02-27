package entity;

import adt.List;
import control.InterviewManager;
import entity.Experience.IndustryType;

/**
 *
 * @author rttz159
 */
public class Company {

    private String companyName;
    private IndustryType industryType;
    private InterviewManager interviewManager;
    private List<InternPost> internPosts;

    public Company(String companyName, IndustryType industryType, List<InternPost> internPosts) {
        this.companyName = companyName;
        this.industryType = industryType;
        this.internPosts = internPosts;
        this.interviewManager = new InterviewManager();
    }

    public Company(String companyName, IndustryType industryType, List<InternPost> internPosts, InterviewManager interviewManager) {
        this.companyName = companyName;
        this.industryType = industryType;
        this.internPosts = internPosts;
        this.interviewManager = interviewManager;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public List<InternPost> getInternPosts() {
        return internPosts;
    }

    public void setInternPosts(List<InternPost> internPosts) {
        this.internPosts = internPosts;
    }

    public InterviewManager getInterviewManager() {
        return this.interviewManager;
    }
}
