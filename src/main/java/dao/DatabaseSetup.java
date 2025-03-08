package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author rttz159
 */
public class DatabaseSetup {
    
    public static void setUpDatabase(){
        Connection connection;
        
        try {
            connection = DatabaseConnectionPool.getDataSource().getConnection();
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return;
        }

        createTables(connection);
    }

    private static void createTables(Connection connection) {
        String[] createTableStatements = {
            "CREATE TABLE IF NOT EXISTS student(\n"
            + "    userId TEXT,\n"
            + "    username TEXT,\n"
            + "    password TEXT,\n"
            + "    contactno TEXT,\n"
            + "    email TEXT,\n"
            + "    city TEXT,\n"
            + "    fullAddress TEXT,\n"
            + "    name TEXT,\n"
            + "    age INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS company(\n"
            + "    userId TEXT,\n"
            + "    username TEXT,\n"
            + "    password TEXT,\n"
            + "    contactno TEXT,\n"
            + "    email TEXT,\n"
            + "    city TEXT,\n"
            + "    fullAddress TEXT,\n"
            + "    companyName TEXT,\n"
            + "    industryType TEXT\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS internpost(\n"
            + "    companyId TEXT,\n"
            + "    interPostId TEXT,\n"
            + "    title TEXT,\n"
            + "    desc TEXT,\n"
            + "    city TEXT,\n"
            + "    fullAddress TEXT,\n"
            + "    minSalary REAL,\n"
            + "    maxSalary REAL\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS application(\n"
            + "    applicationId TEXT,\n"
            + "    internPostId TEXT,\n"
            + "    applicantId TEXT,\n"
            + "    status TEXT\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS interview_student(\n"
            + "    interviewId TEXT,\n"
            + "    applicationId TEXT,\n"
            + "    date TEXT,\n"
            + "    start_time TEXT\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS interview_interval(\n"
            + "    companyId TEXT,\n"
            + "    date TEXT,\n"
            + "    start_time TEXT\n"
            + ");",
            "CREATE TABLE IF NOT EXISTS student_qualification(\n"
            + "    qualificationId TEXT,\n"
            + "    userId TEXT,\n"
            + "    qualificationType TEXT,\n"
            + "    desc TEXT,\n"
            + "    level INTEGER,\n"
            + "    institution TEXT,\n"
            + "    yearOfComplete INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS internpost_qualification(\n"
            + "    qualificationId TEXT,\n"
            + "    interPostId TEXT,\n"
            + "    qualificationType TEXT,\n"
            + "    desc TEXT,\n"
            + "    level INTEGER,\n"
            + "    institution TEXT,\n"
            + "    yearOfComplete INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS student_skill(\n"
            + "    skillId TEXT,\n"
            + "    userId TEXT,\n"
            + "    skilltype TEXT,\n"
            + "    name TEXT,\n"
            + "    proficiencyLevel INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS internpost_skill(\n"
            + "    skillId TEXT,\n"
            + "    interPostId TEXT,\n"
            + "    skilltype TEXT,\n"
            + "    name TEXT,\n"
            + "    proficiencyLevel INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS student_experience(\n"
            + "    experienceId TEXT,\n"
            + "    userId TEXT,\n"
            + "    desc TEXT,\n"
            + "    industryType TEXT,\n"
            + "    duration INTEGER\n"
            + ");\n",
            "CREATE TABLE IF NOT EXISTS internpost_experience(\n"
            + "    experienceId TEXT,\n"
            + "    interPostId TEXT,\n"
            + "    desc TEXT,\n"
            + "    industryType TEXT,\n"
            + "    duration INTEGER\n"
            + ");\n"
        };

        try (Statement statement = connection.createStatement()) {
            for (String sql : createTableStatements) {
                statement.execute(sql);
            }
            System.out.println("All tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }
}

/*
CREATE TABLE IF NOT EXISTS student(
    userId TEXT,
    username TEXT,
    password TEXT,
    contactno TEXT,
    email TEXT,
    city TEXT,
    fullAddress TEXT,
    name TEXT,
    age INTEGER
);

CREATE TABLE IF NOT EXISTS company(
    userId TEXT,
    username TEXT,
    password TEXT,
    contactno TEXT,
    email TEXT,
    city TEXT,
    fullAddress TEXT,
    companyName TEXT,
    industryType TEXT
);

CREATE TABLE IF NOT EXISTS internpost(
    companyId TEXT,
    interPostId TEXT,
    title TEXT,
    desc TEXT,
    city TEXT,
    fullAddress TEXT,
    minSalary REAL,
    maxSalary REAL
);

CREATE TABLE IF NOT EXISTS application(
    applicationId TEXT,
    internPostId TEXT,
    applicantId TEXT,
    status TEXT
);

CREATE TABLE IF NOT EXISTS interview_student(
    interviewId TEXT,
    applicationId TEXT,
    date TEXT,
    start_time TEXT
);

CREATE TABLE IF NOT EXISTS interview_interval(
    companyId TEXT,
    date TEXT,
    start_time TEXT
);

CREATE TABLE IF NOT EXISTS student_qualification(
    qualificationId TEXT,
    userId TEXT,
    qualificationType TEXT,
    desc TEXT,
    level INTEGER,
    institution TEXT,
    yearOfComplete INTEGER
);

CREATE TABLE IF NOT EXISTS internpost_qualification(
    qualificationId TEXT,
    interPostId TEXT,
    qualificationType TEXT,
    desc TEXT,
    level INTEGER,
    institution TEXT,
    yearOfComplete INTEGER
);

CREATE TABLE IF NOT EXISTS student_skill(
    skillId TEXT,
    userId TEXT,
    skilltype TEXT,
    name TEXT,
    proficiencyLevel INTEGER
);

CREATE TABLE IF NOT EXISTS internpost_skill(
    skillId TEXT,
    interPostId TEXT,
    skilltype TEXT,
    name TEXT,
    proficiencyLevel INTEGER
);

CREATE TABLE IF NOT EXISTS student_experience(
    experienceId TEXT,
    userId TEXT,
    desc TEXT,
    industryType TEXT,
    duration INTEGER
);

CREATE TABLE IF NOT EXISTS internpost_experience(
    experienceId TEXT,
    interPostId TEXT,
    desc TEXT,
    industryType TEXT,
    duration INTEGER
);

 */
