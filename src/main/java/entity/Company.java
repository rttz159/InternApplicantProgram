package entity;

import adt.ArrayList;
import control.InterviewManager;
import entity.Experience.IndustryType;
import adt.ListInterface;

/**
 *
 * @author Raymond
 */
public class Company extends User {

    private String companyName;
    private IndustryType industryType;
    private InterviewManager interviewManager;
    private ListInterface<InternPost> internPosts;

    public Company(String userId, String username, String password, String contactno, String email, Location location, String companyName, IndustryType industryType, ListInterface<InternPost> internPosts) {
        super(userId, username, password, contactno, email, location);
        this.companyName = companyName;
        this.industryType = industryType;
        this.internPosts = internPosts;
        this.interviewManager = new InterviewManager();
    }

    public Company(String userId, String username, String password, String contactno, String email, Location location, String companyName, IndustryType industryType, ListInterface<InternPost> internPosts, InterviewManager interviewManager) {
        super(userId, username, password, contactno, email, location);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Company company = (Company) obj;
        return userId.equals(company.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    public Company deepCopy() {
        ListInterface<InternPost> copiedInternPosts = new ArrayList<>();
        for (InternPost post : this.internPosts) {
            copiedInternPosts.append(post.deepCopy());
        }

        return new Company(
                this.getUserId(),
                this.getUsername(),
                this.getPassword(),
                this.getContactno(),
                this.getEmail(),
                this.getLocation() != null ? this.getLocation().deepCopy() : null,
                this.companyName,
                this.industryType,
                copiedInternPosts
        );
    }

}
