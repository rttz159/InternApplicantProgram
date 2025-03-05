package dao;

import adt.ArrayList;
import adt.HashSet;
import adt.List;
import adt.Set;
import entity.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class StudentDAO {

    public static void insertStudent(Student student) {
        String studentSql = "INSERT INTO student (userId, username, password, contactno, email, city, fullAddress, name, age) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false); 

            try (PreparedStatement pstmt = conn.prepareStatement(studentSql)) {
                pstmt.setString(1, student.getUserId());
                pstmt.setString(2, student.getUsername());
                pstmt.setString(3, student.getPassword());
                pstmt.setString(4, student.getContactno());
                pstmt.setString(5, student.getEmail());
                pstmt.setString(6, student.getLocation().getCity());
                pstmt.setString(7, student.getLocation().getFullAddress());
                pstmt.setString(8, student.getName());
                pstmt.setInt(9, student.getAge());
                pstmt.executeUpdate();
            }

            insertStudentAssociations(student, conn);

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error inserting student: " + e.getMessage());
        }
    }

    public static Student getStudentById(String userId) {
        String sql = "SELECT * FROM student WHERE userId = ?";
        Student student = null;

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String contactno = rs.getString("contactno");
                    String email = rs.getString("email");
                    String city = rs.getString("city");
                    String fullAddress = rs.getString("fullAddress");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");

                    Location location = new Location(city, fullAddress);

                    Set<Qualification> studentQualifications = getStudentQualifications(userId, conn);
                    Set<Skill> studentSkills = getStudentSkills(userId, conn);
                    Set<Experience> studentExperiences = getStudentExperiences(userId, conn);
                    List<Application> studentApplications = getStudentApplications(userId, conn);

                    student = new Student(userId, username, password, contactno, email, location, name, age,
                            studentQualifications, studentExperiences, studentSkills, studentApplications);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
        return student;
    }

    public static boolean updateStudentById(Student student) {
        String sql = "UPDATE student SET username = ?, password = ?, contactno = ?, email = ?, city = ?, fullAddress = ?, name = ?, age = ? WHERE userId = ?";

        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, student.getUsername());
                pstmt.setString(2, student.getPassword());
                pstmt.setString(3, student.getContactno());
                pstmt.setString(4, student.getEmail());
                pstmt.setString(5, student.getLocation().getCity());
                pstmt.setString(6, student.getLocation().getFullAddress());
                pstmt.setString(7, student.getName());
                pstmt.setInt(8, student.getAge());
                pstmt.setString(9, student.getUserId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    return false;
                }
            }

            deleteStudentAssociations(student.getUserId(), conn);
            insertStudentAssociations(student, conn);

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteStudentById(String userId) {
        try (Connection conn = DatabaseConnectionPool.getDataSource().getConnection()) {
            conn.setAutoCommit(false);

            deleteStudentAssociations(userId, conn);

            String deleteStudentSql = "DELETE FROM student WHERE userId = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteStudentSql)) {
                pstmt.setString(1, userId);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); 
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
        return false;
    }

    private static void deleteStudentAssociations(String userId, Connection conn) throws SQLException {
        String[] deleteQueries = {
            "DELETE FROM student_qualification WHERE userId = ?",
            "DELETE FROM student_skill WHERE userId = ?",
            "DELETE FROM student_experience WHERE userId = ?",
            "DELETE FROM interview WHERE applicationId IN (SELECT applicationId FROM application WHERE applicantId = ?)",
            "DELETE FROM application WHERE applicantId = ?"
        };

        for (String query : deleteQueries) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                pstmt.executeUpdate();
            }
        }
    }

    private static void insertStudentAssociations(Student student, Connection conn) throws SQLException {
        String qualificationSql = "INSERT INTO student_qualification (qualificationId, userId, qualificationType, desc, level, institution, yearOfComplete) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String skillSql = "INSERT INTO student_skill (skillId, userId, skilltype, name, proficiencyLevel) VALUES (?, ?, ?, ?, ?)";
        String experienceSql = "INSERT INTO student_experience (experienceId, userId, desc, industryType, duration) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(qualificationSql)) {
            for (Qualification qualification : student.getStudentQualifications()) {
                pstmt.setString(1, qualification.getQualificationId());
                pstmt.setString(2, student.getUserId());
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
            for (Skill skill : student.getStudentSkills()) {
                pstmt.setString(1, skill.getSkillId());
                pstmt.setString(2, student.getUserId());
                pstmt.setString(3, skill.getSkilltype().toString());
                pstmt.setString(4, skill.getName());
                pstmt.setInt(5, skill.getProficiencyLevel());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        try (PreparedStatement pstmt = conn.prepareStatement(experienceSql)) {
            for (Experience experience : student.getStudentExperiences()) {
                pstmt.setString(1, experience.getExperienceId());
                pstmt.setString(2, student.getUserId());
                pstmt.setString(3, experience.getDesc());
                pstmt.setString(4, experience.getIndustryType().toString());
                pstmt.setInt(5, experience.getDuration());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private static Set<Qualification> getStudentQualifications(String userId, Connection conn) throws SQLException {
        Set<Qualification> qualifications = new HashSet<>();
        String sql = "SELECT * FROM student_qualification WHERE userId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String qualificationId = rs.getString("qualificationId");
                    String qualificationType = rs.getString("qualificationType");
                    String desc = rs.getString("desc");
                    int level = rs.getInt("level");
                    String institution = rs.getString("institution");
                    int yearOfComplete = rs.getInt("yearOfComplete");

                    qualifications.add(new Qualification(qualificationId, Qualification.QualificationType.valueOf(qualificationType), desc, level, institution, yearOfComplete));
                }
            }
        }
        return qualifications;
    }

    private static Set<Skill> getStudentSkills(String userId, Connection conn) throws SQLException {
        Set<Skill> skills = new HashSet<>();
        String sql = "SELECT * FROM student_skill WHERE userId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

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

    private static Set<Experience> getStudentExperiences(String userId, Connection conn) throws SQLException {
        Set<Experience> experiences = new HashSet<>();
        String sql = "SELECT * FROM student_experience WHERE userId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

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

    private static List<Application> getStudentApplications(String userId, Connection conn) throws SQLException {
        List<Application> applications = new ArrayList<>();
        String sql = "SELECT * FROM application WHERE applicantId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String applicationId = rs.getString("applicationId");
                    String internPostId = rs.getString("internPostId");
                    String applicantId = rs.getString("applicantId");
                    String status = rs.getString("status");

                    Interview interview = getInterviewByApplicationId(applicationId, conn);
                    applications.append(new Application(applicationId, internPostId, applicantId, Application.Status.valueOf(status), interview));
                }
            }
        }
        return applications;
    }

    private static Interview getInterviewByApplicationId(String applicationId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM interview WHERE applicationId = ?";
        Interview interview = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, applicationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String interviewId = rs.getString("interviewId");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    LocalTime startTime = LocalTime.parse(rs.getString("start_time"));

                    interview = new Interview(interviewId, date, startTime);
                }
            }
        }
        return interview;
    }
}