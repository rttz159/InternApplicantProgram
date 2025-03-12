package utils;
import adt.ListInterface;
import control.InterviewManager;
import entity.Company;
import entity.Experience;
import entity.InternPost;
/**
 *
 * @author rttz159
 */
public class CompanyBuilder extends UserBuilder<CompanyBuilder> {
    private String companyName;
    private Experience.IndustryType industryType;
    private ListInterface<InternPost> internPosts;
    private InterviewManager interviewManager = new InterviewManager();

    public CompanyBuilder companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public CompanyBuilder industryType(Experience.IndustryType industryType) {
        this.industryType = industryType;
        return this;
    }

    public CompanyBuilder internPosts(ListInterface<InternPost> internPosts) {
        this.internPosts = internPosts;
        return this;
    }

    public CompanyBuilder interviewManager(InterviewManager interviewManager) {
        this.interviewManager = interviewManager;
        return this;
    }

    public Company build() {
        return new Company(userId, username, password, contactNo, email, location, companyName, industryType, internPosts, interviewManager);
    }

    @Override
    protected CompanyBuilder self() {
        return this;
    }
}
