package control.companyapplication;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import boundary.PredefinedDialog;
import static boundary.PredefinedDialog.showErrorDialog;
import boundary.companyapplication.CompanyApplicationManagementBoundary;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import utils.SimilarityCalculator;

/**
 *
 * @author Raymond
 */
public class CompanyApplicationManagementControl {

    private ListInterface<Application> originalApplications;
    private ListInterface<Application> filteredApplications = new ArrayList<>();
    private Company currentCompany;
    private MapInterface<Application, Double> similarityScores = new HashMap<>();

    private CompanyApplicationManagementBoundary boundary;

    public ListInterface<Application> getOriginalApplications() {
        return originalApplications;
    }

    public ListInterface<Application> getFilteredApplications() {
        return filteredApplications;
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public MapInterface<Application, Double> getSimilarityScores() {
        return similarityScores;
    }

    public CompanyApplicationManagementControl(CompanyApplicationManagementBoundary boundary) {
        this.boundary = boundary;
        currentCompany = (Company) MainControlClass.getCurrentUser();
        setOriginalApplicationList();

        for (Application app : originalApplications) {
            double score = SimilarityCalculator.calculateSimilarity(MainControlClass.getStudentsIdMap().get(app.getApplicantId()), MainControlClass.getInternPostMap().get(app.getInternPostId()));
            similarityScores.put(app, score);
        }

        resetFilteredApplications();
    }

    public String generateReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("==== Company Applications Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Company: %s\n\n", currentCompany.getCompanyName()));

        String selectedJobPost = boundary.getJobPostComboBox().getSelectionModel().getSelectedItem().getTitle();
        String selectedStatus = boundary.getStatusComboBox().getSelectionModel().getSelectedItem();

        report.append(String.format("Filtered by Job Post: %s\n", selectedJobPost));
        report.append(String.format("Filtered by Status: %s\n", selectedStatus));

        String sortingCriteria = (boundary.getToggleGroup().getSelectedToggle() == boundary.getSimilarityScoreToggleBtn())
                ? "Similarity Score (Descending)"
                : "Application Date (Ascending)";
        report.append(String.format("Sorted by: %s\n\n", sortingCriteria));

        report.append("---------------------------------------------------------------------------------------------------------------\n");

        report.append(String.format("%-30s | %-30s | %-15s | %-10s\n",
                "Applicant", "Job", "Status", "Similarity Score"));
        report.append("---------------------------------------------------------------------------------------------------------------\n");

        for (Application app : filteredApplications) {
            String applicantName = MainControlClass.getStudentsIdMap().get(app.getApplicantId()).getName();
            String jobTitle = MainControlClass.getInternPostMap().get(app.getInternPostId()).getTitle();
            String status = app.getStatus().toString();
            String similarityScore = String.format("%.2f", similarityScores.get(app));

            report.append(String.format("%-30s | %-30s | %-15s | %-10s\n",
                    applicantName, jobTitle, status, similarityScore));
        }

        report.append("---------------------------------------------------------------------------------------------------------------\n");
        report.append(String.format("\nTotal Applications: %d\n", filteredApplications.getNumberOfEntries()));

        return report.toString();
    }

    public void setOriginalApplicationList() {
        originalApplications = getCompanyApplication();
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

        if (boundary.getToggleGroup().getSelectedToggle() == boundary.getSimilarityScoreToggleBtn()) {
            sortApplicationBySimilarityScore();
        } else if (boundary.getToggleGroup().getSelectedToggle() == boundary.getDateToggleBtn()) {
            sortApplicationByDate();
        }

        addFilteredListToObservableList();
        updateCountLabel();
        boundary.getApplicationListview().scrollTo(0);
    }

    public void sortApplicationBySimilarityScore() {
        filteredApplications.sort((app1, app2)
                -> Double.compare(similarityScores.get(app2), similarityScores.get(app1))
        );
    }

    public void sortApplicationByDate() {
        filteredApplications.sort((Application app1, Application app2) -> {
            Application tempApp1 = MainControlClass.getStudentApplicationMap().get(app1.getApplicationId());
            Application tempApp2 = MainControlClass.getStudentApplicationMap().get(app2.getApplicationId());
            LocalDate date1 = (tempApp1.getInterview() != null) ? tempApp1.getInterview().getDate() : LocalDate.MAX;
            LocalDate date2 = (tempApp2.getInterview() != null) ? tempApp2.getInterview().getDate() : LocalDate.MAX;

            if (!date1.equals(date2)) {
                return date1.compareTo(date2);
            }

            LocalTime time1 = (tempApp1.getInterview() != null) ? tempApp1.getInterview().getStart_time() : LocalTime.MAX;
            LocalTime time2 = (tempApp2.getInterview() != null) ? tempApp2.getInterview().getStart_time() : LocalTime.MAX;

            return time1.compareTo(time2);
        });
    }

    private ListInterface<Application> getCompanyApplication() {
        ListInterface<Application> tempApplication = new ArrayList<>();
        for (var x : currentCompany.getInternPosts()) {
            for (var y : x.getInternPostApplications()) {
                tempApplication.append(y);
            }
        }
        return tempApplication;
    }

    public void updateCountLabel() {
        boundary.getCountLabel().setText(String.format("[%d Applications] [%d selected]", filteredApplications.getNumberOfEntries(), boundary.getApplicationListview().getSelectionModel().getSelectedIndices().size()));
    }

    public void jobPostAction() {
        InternPost selectedInternPost = boundary.getJobPostComboBox().getSelectionModel().getSelectedItem();
        setOriginalApplicationList();
        if (selectedInternPost != boundary.getAllOption()) {
            ListInterface<Application> tempOriginalApplications = new ArrayList<>();
            for (var x : getOriginalApplications()) {
                if (x.getInternPostId().equals(selectedInternPost.getInterPostId())) {
                    tempOriginalApplications.append(x);
                }
            }
            originalApplications = tempOriginalApplications;
        }

        filterApplicationsByStatus();
    }
    
    public void cancelSelectedAction(){
            if (boundary.getApplicationListview().getSelectionModel().getSelectedItems().isEmpty()) {
                showErrorDialog("Please select an application before proceed");
                return;
            }

            Optional<ButtonType> result = PredefinedDialog.showConfirmationDialog("The action is irreversible");
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                for (var x : boundary.getApplicationListview().getSelectionModel().getSelectedItems()) {
                    Application.Status prevStatus = x.getStatus();
                    if (!prevStatus.equals(Application.Status.CANCELLED)) {
                        x.setStatus(Application.Status.CANCELLED);
                        Student tempStud = MainControlClass.getStudentsIdMap().get(x.getApplicantId());
                        MainControlClass.getStudentApplicationMap().get(x.getApplicationId()).setStatus(Application.Status.CANCELLED);
                        StudentDAO.updateStudentById(tempStud);
                        Application studApplication = MainControlClass.getStudentApplicationMap().get(x.getApplicationId());
                        if (prevStatus.equals(Application.Status.PENDING)) {
                            currentCompany.getInterviewManager().interviewCancelled(studApplication.getInterview().getDate(), studApplication.getInterview().getStart_time());
                        }
                    }
                }

                CompanyDAO.updateCompanyById(currentCompany);
                boundary.refreshListView();
            }
        }
}
