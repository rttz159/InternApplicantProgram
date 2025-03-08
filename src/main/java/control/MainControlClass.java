package control;

import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import dao.CompanyDAO;
import dao.DatabaseSetup;
import dao.StudentDAO;
import entity.Company;
import entity.Student;
import entity.User;
import java.sql.SQLException;

/**
 *
 * @author rttz159
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

    public static MapInterface<String, Student> getStudentsMap() {
        return getInstance().getStudentsMap();
    }

    public static MapInterface<String, Company> getCompanyMap() {
        return getInstance().getCompanyMap();
    }
}

class MainControlHelperClass {

    private ListInterface<Student> students;
    private ListInterface<Company> companies;
    private MapInterface<String, Student> studentMap = new HashMap<>();
    private MapInterface<String, Company> companyMap = new HashMap<>();
    private User currentUser;

    public MainControlHelperClass() {
        DatabaseSetup.setUpDatabase();
        try {
            this.students = StudentDAO.getStudents();
            for (var x : students) {
                studentMap.put(x.getUsername(), x);
            }
            this.companies = CompanyDAO.getCompanies();
            for (var x : companies) {
                companyMap.put(x.getUsername(), x);
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

    public MapInterface<String, Student> getStudentsMap() {
        return this.studentMap;
    }

    public MapInterface<String, Company> getCompanyMap() {
        return this.companyMap;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public boolean login(String username, String password, boolean isStudent) {
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
    }

    public boolean signup(User user) {
        if (user instanceof Student) {
            Student student = (Student) user;
            if (studentMap.containsKey(student.getUsername())) {
                return false;
            }
            this.students.append(student);
            studentMap.put(student.getUsername(), student); 
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
