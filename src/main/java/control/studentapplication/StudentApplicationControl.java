package control.studentapplication;

import adt.ArrayList;
import adt.ListInterface;
import boundary.studentapplication.StudentApplicationBoundary;
import entity.Application;
import entity.Student;
import entity.Location;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import utils.SimilarityCalculator;
import dao.MainControlClass;

/**
 *
 * @author Raymond
 */
public class StudentApplicationControl {
    
    private ListInterface<Application> originalApplications;
    private ListInterface<Application> filteredApplications = new ArrayList<>();
    private Student currentStudent;
    
    private StudentApplicationBoundary boundary;

    public StudentApplicationControl(StudentApplicationBoundary boundary) {
        this.boundary = boundary;
        this.currentStudent = (Student) MainControlClass.getCurrentUser();
        this.originalApplications = currentStudent.getStudentApplications();
        this.filteredApplications = new ArrayList<>();
        resetFilteredApplications();
    }

    public Student getCurrentStudent() {
        return this.currentStudent;
    }

    public ListInterface<Application> getOriginalApplications() {
        return originalApplications;
    }

    public ListInterface<Application> getFilteredApplications() {
        return filteredApplications;
    }

    
    public String generateReportContent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        StringBuilder report = new StringBuilder();

        report.append("==== Student Applications Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Student: %s\n\n", currentStudent.getName()));

        String selectedStatus = (String) boundary.getStatusComboBox().getSelectionModel().getSelectedItem();
        report.append(String.format("Filtered by Status: %s\n", selectedStatus));

        String sortingCriteria = (boundary.getToggleGroup().getSelectedToggle() == boundary.getLocationBtn())
                ? "Location Proximity"
                : "Application Date";
        report.append(String.format("Sorted by: %s\n\n", sortingCriteria));

        report.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        report.append(String.format("%-25s | %-30s | %-12s | %-16s | %-10s | %-15s | %-40s | %-10s\n",
                "Company", "Job", "Status", "Interview Date", "Time", "State", "Full Address", "Similarity Score"));
        report.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (Application app : filteredApplications) {
            String companyName = "";
            for (var x : MainControlClass.getCompanies()) {
                if (x.getInternPosts().contains(MainControlClass.getInternPostMap().get(app.getInternPostId()))) {
                    companyName = x.getCompanyName();
                    break;
                }
            }

            String jobTitle = MainControlClass.getInternPostMap().get(app.getInternPostId()).getTitle();
            String status = app.getStatus().toString();
            LocalDate appDate = app.getInterview().getDate();
            LocalTime appTime = app.getInterview().getStart_time();
            Location location = MainControlClass.getInternPostMap().get(app.getInternPostId()).getLocation();
            double similarityScore = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), location);

            report.append(String.format("%-25s | %-30s | %-12s | %-16s | %-10s | %-15s | %-40s | %-10.2f\n",
                    companyName, jobTitle, status, formatter.format(appDate), timeFormatter.format(appTime),
                    location.getState(), location.getFullAddress(), similarityScore));
        }

        report.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        report.append(String.format("\nTotal Applications: %d\n", filteredApplications.getNumberOfEntries()));

        return report.toString();
    }

    public void setOriginalApplicationList() {
        this.originalApplications = ((Student) MainControlClass.getCurrentUser()).getStudentApplications();
    }

    public void addFilteredListToObservableList() {
        boundary.getApplicationListview().getItems().clear();
        for (var x : filteredApplications) {
            boundary.getApplicationListview().getItems().add(x);
        }
    }

    public void resetFilteredApplications() {
        filteredApplications.clear();
        for (var x : originalApplications) {
            filteredApplications.append(x);
        }
        addFilteredListToObservableList();
    }

    public void rankApplicationByLocation() {
        filteredApplications.sort((Application app1, Application app2) -> {
            double score1 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), MainControlClass.getInternPostMap().get(app1.getInternPostId()).getLocation());
            double score2 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), MainControlClass.getInternPostMap().get(app2.getInternPostId()).getLocation());
            return Double.compare(score1, score2);
        });
        addFilteredListToObservableList();
    }

    public void rankApplicationByDate() {
        filteredApplications.sort((Application app1, Application app2) -> {
            LocalDate date1 = (app1.getInterview() != null) ? app1.getInterview().getDate() : LocalDate.MAX;
            LocalDate date2 = (app2.getInterview() != null) ? app2.getInterview().getDate() : LocalDate.MAX;

            if (!date1.equals(date2)) {
                return date1.compareTo(date2);
            }

            LocalTime time1 = (app1.getInterview() != null) ? app1.getInterview().getStart_time() : LocalTime.MAX;
            LocalTime time2 = (app2.getInterview() != null) ? app2.getInterview().getStart_time() : LocalTime.MAX;

            return time1.compareTo(time2);
        });
        addFilteredListToObservableList();
    }

    public void filterApplicationsByStatus() {
        String selectedStatus = (String) boundary.getStatusComboBox().getSelectionModel().getSelectedItem();

        filteredApplications.clear();
        if (selectedStatus.equals("ALL")) {
            for (Application app : originalApplications) {
                filteredApplications.append(app);

            }
        } else {
            for (Application app : originalApplications) {
                if (app.getStatus().toString().equalsIgnoreCase(selectedStatus)) {
                    filteredApplications.append(app);
                }
            }
        }

        if (boundary.getToggleGroup().getSelectedToggle() == boundary.getLocationBtn()) {
            rankApplicationByLocation();
            boundary.getApplicationListview().scrollTo(0);
        } else if (boundary.getToggleGroup().getSelectedToggle() == boundary.getDateToggleButton()) {
            rankApplicationByDate();
            boundary.getApplicationListview().scrollTo(0);

        }
    }
}
