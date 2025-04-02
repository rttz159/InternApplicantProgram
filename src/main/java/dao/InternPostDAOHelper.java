package dao;

import adt.ArrayList;
import adt.HashSet;
import adt.OrderPair;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import adt.SetInterface;
import adt.ListInterface;

/**
 *
 * @author Raymond
 */
class InternPostDAOHelper {

    public static ListInterface<InternPost> getInternPostByCompanyId(String companyId, Connection conn) throws SQLException {
        ListInterface<InternPost> internPost = new ArrayList<>();
        String sql = "SELECT * FROM internpost WHERE companyId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, companyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String interPostId = rs.getString("interPostId");
                    String title = rs.getString("title");
                    String desc = rs.getString("desc");
                    String city = rs.getString("city");
                    String fullAddress = rs.getString("fullAddress");
                    double minSalary = rs.getDouble("minSalary");
                    double maxSalary = rs.getDouble("maxSalary");
                    SetInterface<Qualification> tempQualification = InternPostDAOHelper.getInternPostQualifications(interPostId, conn);
                    SetInterface<Skill> tempSkill = InternPostDAOHelper.getInternPostSkills(interPostId, conn);
                    SetInterface<Experience> tempExperience = InternPostDAOHelper.getInternPostExperiences(interPostId, conn);
                    ListInterface<Application> tempApplication = InternPostDAOHelper.getInternPostApplications(interPostId, conn);
                    boolean tempStatus = rs.getBoolean("status");
                    
                    internPost.append(new InternPost(interPostId, title, desc, new Location(city, fullAddress), new OrderPair<>(minSalary, maxSalary), tempQualification, tempExperience, tempSkill, tempApplication,tempStatus));
                }
            }
        }

        return internPost;
    }

    public static void deleteInternPostAssociations(String internpostId, Connection conn) throws SQLException {
        String[] deleteQueries = {
            "DELETE FROM internpost_qualification WHERE interPostId = ?",
            "DELETE FROM internpost_skill WHERE interPostId = ?",
            "DELETE FROM internpost_experience WHERE interPostId = ?"
        };

        for (String query : deleteQueries) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, internpostId);
                pstmt.executeUpdate();
            }
        }
    }

    public static SetInterface<Qualification> getInternPostQualifications(String internpostId, Connection conn) throws SQLException {
        SetInterface<Qualification> qualifications = new HashSet<>();
        String sql = "SELECT * FROM internpost_qualification WHERE interPostId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, internpostId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String qualificationId = rs.getString("qualificationId");
                    String qualificationType = rs.getString("qualificationType");
                    String desc = rs.getString("desc");
                    String institution = rs.getString("institution");
                    int yearOfComplete = rs.getInt("yearOfComplete");

                    qualifications.add(new Qualification(qualificationId, Qualification.QualificationType.valueOf(qualificationType), desc, institution, yearOfComplete));
                }
            }
        }
        return qualifications;
    }

    public static SetInterface<Skill> getInternPostSkills(String internpostId, Connection conn) throws SQLException {
        SetInterface<Skill> skills = new HashSet<>();
        String sql = "SELECT * FROM internpost_skill WHERE interPostId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, internpostId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String skillId = rs.getString("skillId");
                    String skillType = rs.getString("skilltype");
                    String name = rs.getString("name");
                    int proficiencyLevel = rs.getInt("proficiencyLevel");

                    skills.add(new Skill(skillId, Skill.SkillType.valueOf(skillType), name, proficiencyLevel));
                }
            }
        }
        return skills;
    }

    public static SetInterface<Experience> getInternPostExperiences(String internpostId, Connection conn) throws SQLException {
        SetInterface<Experience> experiences = new HashSet<>();
        String sql = "SELECT * FROM internpost_experience WHERE interPostId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, internpostId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String experienceId = rs.getString("experienceId");
                    String desc = rs.getString("desc");
                    String industryType = rs.getString("industryType");
                    int duration = rs.getInt("duration");

                    experiences.add(new Experience(experienceId, desc, Experience.IndustryType.valueOf(industryType), duration));
                }
            }
        }
        return experiences;
    }

    public static ListInterface<Application> getInternPostApplications(String internpostId, Connection conn) throws SQLException {
        ListInterface<Application> tempApplication = new ArrayList<>();
        String sql = "SELECT * FROM application WHERE internPostId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, internpostId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String applicationId = rs.getString("applicationId");
                    String internPostId = rs.getString("internPostId");
                    String applicantId = rs.getString("applicantId");
                    String status = rs.getString("status");

                    tempApplication.append(new Application(applicationId, internPostId, applicantId, Application.Status.valueOf(status), null));
                }
            }
        }
        return tempApplication;
    }
}
