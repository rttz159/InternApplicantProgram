package boundary.studentapplication;

import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.Location;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import utils.ReportGenerator;
import utils.SimilarityCalculator;

/**
 *
 * @author
 */
public class StudentApplicationController implements Initializable {

    @FXML
    private ListView<Application> applicationListview;

    @FXML
    private ToggleButton dateToggleButton;

    @FXML
    private ToggleButton locationBtn;

    @FXML
    private Button generateReportBtn;

    @FXML
    private ComboBox statusComboBox;

    @FXML
    private Label countLabel;

    private ToggleGroup toggleGroup;

    private ListInterface<Application> originalApplications;

    private ListInterface<Application> filteredApplications = new ArrayList<>();

    private Student currentStudent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentStudent = (Student) MainControlClass.getCurrentUser();
        setOriginalApplicationList();
        resetFilteredApplications();
        rankApplicationByDate();

        countLabel.setText(String.format("[%d Applications]", originalApplications.getNumberOfEntries()));

        applicationListview.setSelectionModel(new NullSelectionModel());
        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        Styles.toggleStyleClass(applicationListview, Styles.STRIPED);
        StudentApplicationShareState.getInstance().setListView(applicationListview);

        statusComboBox.getItems().addAll("ALL", "SUCCESS", "PENDING", "REJECTED", "CANCELLED");
        statusComboBox.getSelectionModel().select("ALL");
        statusComboBox.setOnAction(eh -> filterApplicationsByStatus());

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(dateToggleButton);
        toggleGroup.getToggles().add(locationBtn);
        dateToggleButton.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
                return;
            }

            if (newValue == locationBtn) {
                rankApplicationByLocation();
            } else if (newValue == dateToggleButton) {
                rankApplicationByDate();
            }
            applicationListview.scrollTo(0);
        }
        );

        generateReportBtn.setOnAction(eh -> ReportGenerator.generateReport(generateReportContent()));
    }

    private String generateReportContent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        StringBuilder report = new StringBuilder();

        report.append(
                "==== Student Applications Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Student: %s\n\n", currentStudent.getName()));

        String selectedStatus = (String) statusComboBox.getSelectionModel().getSelectedItem();

        report.append(String.format("Filtered by Status: %s\n", selectedStatus));

        String sortingCriteria = (toggleGroup.getSelectedToggle() == locationBtn)
                ? "Location Proximity"
                : "Application Date";

        report.append(String.format("Sorted by: %s\n\n", sortingCriteria));

        report.append(
                "------------------------------------------------------\n");

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

            report.append(String.format(
                    "Company: %s\nJob: %s\nStatus: %s\nInterview Date: %s\nInterview Time: %s\nLocation (State): %s\nLocation (Full Address): %s\nLocation Similarity Score: %.2f\n",
                    companyName, jobTitle, status, formatter.format(appDate), timeFormatter.format(appTime), location.getState(),location.getFullAddress(), similarityScore
            ));
            report.append("------------------------------------------------------\n");
        }

        report.append(String.format("\nTotal Applications: %d\n", filteredApplications.getNumberOfEntries()));
        return report.toString();
    }

    private void setOriginalApplicationList() {
        this.originalApplications = ((Student) MainControlClass.getCurrentUser()).getStudentApplications();
    }

    private void addFilteredListToObservableList() {
        applicationListview.getItems().clear();
        for (var x : filteredApplications) {
            applicationListview.getItems().add(x);
        }
    }

    private void resetFilteredApplications() {
        filteredApplications.clear();
        for (var x : originalApplications) {
            filteredApplications.append(x);
        }
        addFilteredListToObservableList();
    }

    private void rankApplicationByLocation() {
        filteredApplications.sort((Application app1, Application app2) -> {
            double score1 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), MainControlClass.getInternPostMap().get(app1.getInternPostId()).getLocation());
            double score2 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), MainControlClass.getInternPostMap().get(app2.getInternPostId()).getLocation());
            return Double.compare(score1, score2);
        });
        addFilteredListToObservableList();
    }

    private void rankApplicationByDate() {
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

    private void filterApplicationsByStatus() {
        String selectedStatus = (String) statusComboBox.getSelectionModel().getSelectedItem();

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

        if (toggleGroup.getSelectedToggle() == locationBtn) {
            rankApplicationByLocation();
            applicationListview.scrollTo(0);
        } else if (toggleGroup.getSelectedToggle() == dateToggleButton) {
            rankApplicationByDate();
            applicationListview.scrollTo(0);

        }
    }

}

class CustomListCellFactory implements Callback<ListView<Application>, ListCell<Application>> {

    @Override
    public ListCell<Application> call(ListView<Application> listView) {
        return new ListCell<Application>() {
            private FXMLLoader fxmlLoader;
            private Node node;
            private ApplicationCardController controller;

            @Override
            protected void updateItem(Application item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if (fxmlLoader == null) {
                        try {
                            fxmlLoader = new FXMLLoader(App.class.getResource("studentapplication/ApplicationCard.fxml"));
                            node = fxmlLoader.load();
                            controller = fxmlLoader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (controller != null) {
                        controller.setApplication(item);
                    }
                    this.setGraphic(node);
                }
            }
        };
    }
}
