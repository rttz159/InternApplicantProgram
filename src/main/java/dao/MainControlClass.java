package dao;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Student;
import entity.User;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 *
 * @author Raymond
 */
public class MainControlClass {

    private static MainControlHelperClass instance;

    public static MainControlHelperClass getInstance() {
        if (instance == null) {
            synchronized (MainControlClass.class) {
                if (instance == null) {
                    instance = new MainControlHelperClass();
                }
            }
        }
        return instance;
    }

    public static User getCurrentUser() {
        return getInstance().getCurrentUser();
    }

    public static void setCurrentUser(User user) {
        getInstance().setCurrentUser(user);
    }

    public static boolean logIn(String username, String password, boolean isStudent) {
        return getInstance().login(username, password, isStudent);
    }

    public static boolean signUp(User user) {
        return getInstance().signup(user);
    }

    public static ListInterface<Student> getStudents() {
        return getInstance().getStudents();
    }

    public static ListInterface<Company> getCompanies() {
        return getInstance().getCompanies();
    }

    public static ListInterface<InternPost> getInternPost() {
        return getInstance().getInternPost();
    }

    public static MapInterface<String, Student> getStudentsMap() {
        return getInstance().getStudentsMap();
    }

    public static MapInterface<String, Student> getStudentsIdMap() {
        return getInstance().getStudentsIdMap();
    }

    public static MapInterface<String, Company> getCompanyMap() {
        return getInstance().getCompanyMap();
    }

    public static MapInterface<String, InternPost> getInternPostMap() {
        return getInstance().getInternPostMap();
    }

    public static MapInterface<String, Application> getStudentApplicationMap() {
        return getInstance().getStudentApplicationMap();
    }
}

class MainControlHelperClass {

    private ListInterface<Student> students;
    private ListInterface<Company> companies;
    private ListInterface<InternPost> internpost;
    private MapInterface<String, Student> studentMap = new HashMap<>();
    private MapInterface<String, Student> studentIdMap = new HashMap<>();
    private MapInterface<String, Company> companyMap = new HashMap<>();
    private MapInterface<String, InternPost> internpostMap = new HashMap<>();
    private MapInterface<String, Application> studentApplicationMap = new HashMap<>();
    private User currentUser;

    public MainControlHelperClass() {
        DatabaseSetup.setUpDatabase();
        try {
            this.students = StudentDAO.getStudents();
            for (var x : students) {
                studentMap.put(x.getUsername(), x);
                studentIdMap.put(x.getUserId(), x);
                for (var y : x.getStudentApplications()) {
                    studentApplicationMap.put(y.getApplicationId(), y);
                }
            }
            this.companies = CompanyDAO.getCompanies();
            for (var x : companies) {
                companyMap.put(x.getUsername(), x);
            }
            internpost = new ArrayList<>();
            for (var x : this.companies) {
                if (x.getInternPosts() != null) {
                    for (var y : x.getInternPosts()) {
                        internpost.append(y);
                    }
                }
            }
            for (var x : internpost) {
                internpostMap.put(x.getInterPostId(), x);
            }

        } catch (SQLException ex) {
            System.out.println("Error happened when loading data from database.");
        }
    }

    public ListInterface<Student> getStudents() {
        return this.students;
    }

    public ListInterface<Company> getCompanies() {
        return this.companies;
    }

    public ListInterface<InternPost> getInternPost() {
        return this.internpost;
    }

    public MapInterface<String, Student> getStudentsMap() {
        return this.studentMap;
    }

    public MapInterface<String, Student> getStudentsIdMap() {
        return this.studentIdMap;
    }

    public MapInterface<String, Company> getCompanyMap() {
        return this.companyMap;
    }

    public MapInterface<String, InternPost> getInternPostMap() {
        return this.internpostMap;
    }

    public MapInterface<String, Application> getStudentApplicationMap() {
        return this.studentApplicationMap;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean login(String username, String password, boolean isStudent) {
        try {
            if (isStudent) {
                Student student = studentMap.get(username);
                if (student != null && student.getPassword().equals(password)) {
                    this.currentUser = student;
                    return true;
                }
            } else {
                Company company = companyMap.get(username);
                if (company != null && company.getPassword().equals(password)) {
                    this.currentUser = company;
                    return true;
                }
            }
            return false;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public boolean signup(User user) {
        if (user instanceof Student) {
            Student student = (Student) user;
            if (studentMap.containsKey(student.getUsername())) {
                return false;
            }
            this.students.append(student);
            studentMap.put(student.getUsername(), student);
            studentIdMap.put(student.getUserId(), student);
            StudentDAO.insertStudent(student);
            return true;
        }

        if (user instanceof Company) {
            Company company = (Company) user;
            if (companyMap.containsKey(company.getUsername())) {
                return false;
            }
            this.companies.append(company);
            companyMap.put(company.getUsername(), company);
            CompanyDAO.insertCompany(company);
            return true;
        }

        return false;
    }
}
