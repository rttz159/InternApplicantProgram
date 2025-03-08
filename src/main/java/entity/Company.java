package entity;

import control.InterviewManager;
import entity.Experience.IndustryType;
import adt.ListInterface;

/**
 *
 * @author rttz159
 */
public class Company extends User{

    private String companyName;
    private IndustryType industryType;
    private InterviewManager interviewManager;
    private ListInterface<InternPost> internPosts;

    public Company(String userId, String username, String password, String contactno, String email, Location location, String companyName, IndustryType industryType, ListInterface<InternPost> internPosts) {
        super(userId,username,password,contactno,email,location);
        this.companyName = companyName;
        this.industryType = industryType;
        this.internPosts = internPosts;
        this.interviewManager = new InterviewManager();
    }

    public Company(String userId, String username, String password, String contactno, String email, Location location, String companyName, IndustryType industryType, ListInterface<InternPost> internPosts, InterviewManager interviewManager) {
        super(userId,username,password,contactno,email,location);
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

    public ListInterface<InternPost> getInternPosts() {
        return internPosts;
    }

    public void setInternPosts(ListInterface<InternPost> internPosts) {
        this.internPosts = internPosts;
    }

    public InterviewManager getInterviewManager() {
        return this.interviewManager;
    }
}
