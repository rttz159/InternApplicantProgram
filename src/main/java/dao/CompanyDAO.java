package dao;

import adt.ArrayList;
import control.InterviewManager;
import entity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import adt.ListInterface;

/**
 *
 * @author Raymond
 */
public class CompanyDAO {

    public static boolean updateCompanyById(Company company) {
        String sql = "UPDATE company SET username=?, password=?, contactno=?, email=?, city=?, fullAddress=?, companyName=?, industryType=? WHERE userId=?";

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, company.getUsername());
                pstmt.setString(2, company.getPassword());
                pstmt.setString(3, company.getContactno());
                pstmt.setString(4, company.getEmail());
                pstmt.setString(5, company.getLocation().getState());
                pstmt.setString(6, company.getLocation().getFullAddress());
                pstmt.setString(7, company.getCompanyName());
                pstmt.setString(8, company.getIndustryType().toString());
                pstmt.setString(9, company.getUserId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
            }

            deleteCompanyAssociations(company.getUserId(), conn);
            insertCompanyAssociations(company, conn);

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating company: " + e.getMessage());
        }
        return false;
    }

    public static void insertCompany(Company company) {
        String companySql = "INSERT INTO company (userId,username,password,contactno,email,city,fullAddress,companyName,industryType) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(companySql)) {
                pstmt.setString(1, company.getUserId());
                pstmt.setString(2, company.getUsername());
                pstmt.setString(3, company.getPassword());
                pstmt.setString(4, company.getContactno());
                pstmt.setString(5, company.getEmail());
                pstmt.setString(6, company.getLocation().getState());
                pstmt.setString(7, company.getLocation().getFullAddress());
                pstmt.setString(8, company.getCompanyName());
                pstmt.setString(9, company.getIndustryType().toString());
                pstmt.executeUpdate();
            }

            insertCompanyAssociations(company, conn);

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error inserting Company: " + e.getMessage());
        }
    }

    private static void insertCompanyAssociations(Company company, Connection conn) throws SQLException {
        String interviewManagerSql = "INSERT INTO interview_interval (companyId, date,start_time) VALUES (?,?,?)";
        String internPostSql = "INSERT INTO internpost (companyId,interPostId,title,desc,city,fullAddress,minSalary,maxSalary,status) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(interviewManagerSql)) {
            for (var x : company.getInterviewManager().getBookingRecords()) {
                for (var y : x.getValue().showBookedSlots()) {
                    pstmt.setString(1, company.getUserId());
                    pstmt.setString(2, x.getKey().toString());
                    pstmt.setString(3, y.start.toString());
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(internPostSql)) {
            for (var x : company.getInternPosts()) {
                pstmt.setString(1, company.getUserId());
                pstmt.setString(2, x.getInterPostId());
                pstmt.setString(3, x.getTitle());
                pstmt.setString(4, x.getDesc());
                pstmt.setString(5, x.getLocation().getState());
                pstmt.setString(6, x.getLocation().getFullAddress());
                pstmt.setDouble(7, x.getMinMaxSalary().getX());
                pstmt.setDouble(8, x.getMinMaxSalary().getY());
                pstmt.setBoolean(9, x.getStatus());

                insertCompanyInternPostsAssociation(x, conn);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

    }

    private static void insertCompanyInternPostsAssociation(InternPost internpost, Connection conn) throws SQLException {
        String qualificationSql = "INSERT INTO internpost_qualification (qualificationId, interPostId, qualificationType, desc, level, institution, yearOfComplete) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String skillSql = "INSERT INTO internpost_skill (skillId, interPostId, skilltype, name, proficiencyLevel) VALUES (?, ?, ?, ?, ?)";
        String experienceSql = "INSERT INTO internpost_experience (experienceId, interPostId, desc, industryType, duration) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(qualificationSql)) {
            for (Qualification qualification : internpost.getInternPostQualifications()) {
                pstmt.setString(1, qualification.getQualificationId());
                pstmt.setString(2, internpost.getInterPostId());
                pstmt.setString(3, qualification.getQualificationType().toString());
                pstmt.setString(4, qualification.getDesc());
                pstmt.setInt(5, qualification.getLevel());
                pstmt.setString(6, qualification.getInstitution());
                pstmt.setInt(7, qualification.getYearOfComplete());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(skillSql)) {
            for (Skill skill : internpost.getInternPostSkills()) {
                pstmt.setString(1, skill.getSkillId());
                pstmt.setString(2, internpost.getInterPostId());
                pstmt.setString(3, skill.getSkilltype().toString());
                pstmt.setString(4, skill.getName());
                pstmt.setInt(5, skill.getProficiencyLevel());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(experienceSql)) {
            for (Experience experience : internpost.getInterPostExperiences()) {
                pstmt.setString(1, experience.getExperienceId());
                pstmt.setString(2, internpost.getInterPostId());
                pstmt.setString(3, experience.getDesc());
                pstmt.setString(4, experience.getIndustryType().toString());
                pstmt.setInt(5, experience.getDuration());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public static boolean deleteCompanyById(String companyId) {
        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            deleteCompanyAssociations(companyId, conn);

            String deleteCompanySql = "DELETE FROM company WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteCompanySql)) {
                pstmt.setString(1, companyId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting company: " + e.getMessage());
        }
        return false;
    }

    private static void deleteCompanyAssociations(String companyId, Connection conn) throws SQLException {
        String getInterPostSql = "SELECT * FROM internpost WHERE companyId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(getInterPostSql)) {
            pstmt.setString(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String internPostId = rs.getString("interPostId");
                    if (internPostId != null) {
                        InternPostDAOHelper.deleteInternPostAssociations(internPostId, conn);
                    }
                }
            }

        }

        String[] deleteQueries = {
            "DELETE FROM interview_interval WHERE companyId = ?",
            "DELETE FROM internpost WHERE companyId = ?"
        };

        for (String query : deleteQueries) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, companyId);
                pstmt.executeUpdate();
            }
        }
    }

    public static Company getCompanyById(String companyId) throws SQLException {
        Company tempCompany = null;
        String sql = "SELECT * FROM company WHERE userId = ?";

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, companyId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        String contactno = rs.getString("contactno");
                        String email = rs.getString("email");
                        String city = rs.getString("city");
                        String fullAddress = rs.getString("fullAddress");
                        String companyName = rs.getString("companyName");
                        String industryType = rs.getString("industryType");

                        Location location = new Location(city, fullAddress);
                        ListInterface<InternPost> internPosts = InternPostDAOHelper.getInternPostByCompanyId(companyId, conn);
                        InterviewManager tempInterviewManager = getInterviewManagerById(companyId, conn);

                        tempCompany = new Company(companyId, username, password, contactno, email, location, companyName, Experience.IndustryType.valueOf(industryType),
                                internPosts, tempInterviewManager);
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Error getting company: " + e.getMessage());
        }

        return tempCompany;

    }

    public static ListInterface<Company> getCompanies() throws SQLException {
        ListInterface<Company> tempCompanies = new ArrayList<>();
        String sql = "SELECT * FROM company";

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
                ResultSet rs = pstmt.executeQuery();

                if (!rs.isBeforeFirst()) {
                    System.out.println("⚠ No records found in company table.");
                } else {
                    System.out.println("✅ Companies found, processing results...");
                }
                while (rs.next()) {
                    String companyId = rs.getString("userId");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String contactno = rs.getString("contactno");
                    String email = rs.getString("email");
                    String city = rs.getString("city");
                    String fullAddress = rs.getString("fullAddress");
                    String companyName = rs.getString("companyName");
                    String industryType = rs.getString("industryType");

                    Location location = new Location(city, fullAddress);
                    ListInterface<InternPost> internPosts = InternPostDAOHelper.getInternPostByCompanyId(companyId, conn);
                    InterviewManager tempInterviewManager = getInterviewManagerById(companyId, conn);

                    tempCompanies.append(new Company(companyId, username, password, contactno, email, location, companyName, Experience.IndustryType.valueOf(industryType),
                            internPosts, tempInterviewManager));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting company: " + e.getMessage());
        }

        return tempCompanies;

    }

    private static InterviewManager getInterviewManagerById(String companyId, Connection conn) throws SQLException {
        InterviewManager tempInterviewManager = new InterviewManager();
        String sql = "SELECT * FROM interview_interval WHERE companyId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, companyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    LocalTime start_time = LocalTime.parse(rs.getString("start_time"));

                    tempInterviewManager.interviewBooking(date, start_time);
                }
            }

        }
        return tempInterviewManager;
    }

}
