package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    private Connection connection;

    public DatabaseSetup() {
        try {
            connection = DatabaseConnectionPool.getDataSource().getConnection();
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            return;
        }

        createTables();
    }

    private void createTables() {
        String[] createTableStatements = {
            "CREATE TABLE IF NOT EXISTS student(\n" + "    userId TEXT,\n" + "    username TEXT,\n" + "    password TEXT,\n" + "    contactno TEXT,\n" + "    email TEXT,\n" + "    city TEXT,\n" + "    fullAddress TEXT,\n" + "    name TEXT,\n" + "    age INTEGER,\n" + "    PRIMARY KEY (userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS company(\n" + "    userId TEXT,\n" + "    username TEXT,\n" + "    password TEXT,\n" + "    contactno TEXT,\n" + "    email TEXT,\n" + "    city TEXT,\n" + "    fullAddress TEXT,\n" + "    companyName TEXT,\n" + "    industryType INTEGER,\n" + "    PRIMARY KEY (userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS internpost(\n" + "    interPostId TEXT,\n" + "    title TEXT,\n" + "    desc TEXT,\n" + "    city TEXT,\n" + "    fullAddress TEXT,\n" + "    minSalary REAL,\n" + "    maxSalary REAL,\n" + "    PRIMARY KEY (interPostId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS application(\n" + "    applicationId TEXT,\n" + "    internPostId TEXT,\n" + "    applicantId TEXT,\n" + "    status TEXT,\n" + "    PRIMARY KEY (applicationId),\n" + "    FOREIGN KEY (internPostId) REFERENCES internpost (interPostId),\n" + "    FOREIGN KEY (applicantId) REFERENCES student (userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS interview(\n" + "    interviewId TEXT,\n" + "    applicationId TEXT,\n" + "    date TEXT,\n" + "    start_time TEXT,\n" + "    PRIMARY KEY (interviewId),\n" + "    FOREIGN KEY (applicationId) REFERENCES application(applicationId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS student_qualification(\n" + "    qualificationId TEXT,\n" + "    userId TEXT,\n" + "    qualificationType TEXT,\n" + "    desc TEXT,\n" + "    level INTEGER,\n" + "    institution TEXT,\n" + "    yearOfComplete INTEGER,\n" + "    PRIMARY KEY (qualificationId),\n" + "    FOREIGN KEY (userId) REFERENCES student(userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS internpost_qualification(\n" + "    qualificationId TEXT,\n" + "    interPostId TEXT,\n" + "    qualificationType TEXT,\n" + "    desc TEXT,\n" + "    level INTEGER,\n" + "    institution TEXT,\n" + "    yearOfComplete INTEGER,\n" + "    PRIMARY KEY (qualificationId),\n" + "    FOREIGN KEY (interPostId) REFERENCES internpost(interPostId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS student_skill(\n" + "    skillId TEXT,\n" + "    userId TEXT,\n" + "    skilltype TEXT,\n" + "    name TEXT,\n" + "    proficiencyLevel INTEGER,\n" + "    PRIMARY KEY (skillId),\n" + "    FOREIGN KEY (userId) REFERENCES student(userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS internpost_skill(\n" + "    skillId TEXT,\n" + "    interPostId TEXT,\n" + "    skilltype TEXT,\n" + "    name TEXT,\n" + "    proficiencyLevel INTEGER,\n" + "    PRIMARY KEY (skillId),\n" + "    FOREIGN KEY (interPostId) REFERENCES internpost(interPostId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS student_experience(\n" + "    experienceId TEXT,\n" + "    userId TEXT,\n" + "    desc TEXT,\n" + "    industryType TEXT,\n" + "    duration INTEGER,\n" + "    PRIMARY KEY (experienceId),\n" + "    FOREIGN KEY (userId) REFERENCES student(userId)\n" + ");\n", "CREATE TABLE IF NOT EXISTS internpost_experience(\n" + "    experienceId TEXT,\n" + "    interPostId TEXT,\n" + "    desc TEXT,\n" + "    industryType TEXT,\n" + "    duration INTEGER,\n" + "    PRIMARY KEY (experienceId),\n" + "    FOREIGN KEY (interPostId) REFERENCES internpost(interPostId)\n" + ");\n"};

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