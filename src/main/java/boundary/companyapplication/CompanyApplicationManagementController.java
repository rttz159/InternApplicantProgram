package boundary.companyapplication;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import atlantafx.base.theme.Styles;
import boundary.PredefinedDialog;
import static boundary.PredefinedDialog.showErrorDialog;
import com.rttz.assignment.App;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import utils.ReportGenerator;
import utils.SimilarityCalculator;
import utils.builders.InternPostBuilder;

/**
 *
 * @author Raymond
 */
public class CompanyApplicationManagementController implements Initializable {

    @FXML
    private Button clearBtn;

    @FXML
    private ToggleButton dateToggleBtn;

    @FXML
    private Button selectAllBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button generateReportBtn;

    @FXML
    private ToggleButton similarityScoreToggleBtn;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ComboBox<InternPost> jobPostComboBox;

    @FXML
    private ListView<Application> applicationListview;

    @FXML
    private Label countLabel;

    private ContextMenu contextMenu = new ContextMenu();

    private ToggleGroup toggleGroup;

    private ListInterface<Application> originalApplications;

    private ListInterface<Application> filteredApplications = new ArrayList<>();

    private Company currentCompany;

    private MapInterface<Application, Double> similarityScores = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentCompany = (Company) MainControlClass.getCurrentUser();
        setOriginalApplicationList();

        for (Application app : originalApplications) {
            double score = SimilarityCalculator.calculateSimilarity(MainControlClass.getStudentsIdMap().get(app.getApplicantId()), MainControlClass.getInternPostMap().get(app.getInternPostId()));
            similarityScores.put(app, score);
        }

        resetFilteredApplications();

        countLabel.setText(String.format("[%d Applications]", originalApplications.getNumberOfEntries()));

        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        applicationListview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Styles.toggleStyleClass(applicationListview, Styles.BORDERED);
        applicationListview.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Application>) change -> {
            updateCountLabel();
        });
        applicationListview.setContextMenu(contextMenu);
        applicationListview.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
                contextMenu.show(applicationListview, event.getScreenX(), event.getScreenY());
            }
        });
        CompanyApplicationShareState.getInstance().setListView(applicationListview);

        statusComboBox.getItems().addAll("ALL", "SUCCESS", "PENDING", "REJECTED", "CANCELLED");
        statusComboBox.getSelectionModel().select("ALL");
        statusComboBox.setOnAction(eh -> filterApplicationsByStatus());

        //Add a pseudo Intern Post
        InternPost allOption = new InternPostBuilder()
                .title("ALL")
                .build();
        jobPostComboBox.setCellFactory(lv -> new ListCell<InternPost>() {
            @Override
            protected void updateItem(InternPost item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getTitle());
            }
        });
        jobPostComboBox.setConverter(new StringConverter<InternPost>() {
            @Override
            public String toString(InternPost item) {
                return (item != null) ? item.getTitle() : "";
            }

            @Override
            public InternPost fromString(String string) {
                return null;
            }
        });
        jobPostComboBox.getItems().add(allOption);
        for (var x : currentCompany.getInternPosts()) {
            jobPostComboBox.getItems().add(x);
        }
        jobPostComboBox.getSelectionModel().select(allOption);
        jobPostComboBox.setOnAction(eh -> {
            InternPost selectedInternPost = jobPostComboBox.getSelectionModel().getSelectedItem();
            setOriginalApplicationList();
            if (selectedInternPost != allOption) {
                ListInterface<Application> tempOriginalApplications = new ArrayList<>();
                for (var x : originalApplications) {
                    if (x.getInternPostId().equals(selectedInternPost.getInterPostId())) {
                        tempOriginalApplications.append(x);
                    }
                }
                originalApplications = tempOriginalApplications;
            }

            filterApplicationsByStatus();
        });

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(dateToggleBtn);
        toggleGroup.getToggles().add(similarityScoreToggleBtn);
        similarityScoreToggleBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
                return;
            }

            if (newValue == similarityScoreToggleBtn) {
                sortApplicationBySimilarityScore();
            } else if (newValue == dateToggleBtn) {
                sortApplicationByDate();
            }

            addFilteredListToObservableList();
        });

        //Context Menu
        MenuItem selectAll = new MenuItem("Select All");
        MenuItem clearSelection = new MenuItem("Clear Selection");
        MenuItem rejectSelected = new MenuItem("Reject Selected");
        MenuItem cancelSelected = new MenuItem("Cancel Selected");

        clearSelection.setOnAction(eh -> {
            clearSelections();
        });

        selectAll.setOnAction(eh -> {
            applicationListview.getSelectionModel().selectAll();
        });

        rejectSelected.setOnAction(eh -> {
            if (applicationListview.getSelectionModel().getSelectedItems().isEmpty()) {
                showErrorDialog("Please select an application before proceed");
                return;
            }

            Optional<ButtonType> result = PredefinedDialog.showConfirmationDialog("The action is irreversible");
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                for (var x : applicationListview.getSelectionModel().getSelectedItems()) {
                    x.setStatus(Application.Status.REJECTED);
                    Student tempStud = MainControlClass.getStudentsIdMap().get(x.getApplicantId());
                    MainControlClass.getStudentApplicationMap().get(x.getApplicationId()).setStatus(Application.Status.REJECTED);
                    StudentDAO.updateStudentById(tempStud);
                }

                CompanyDAO.updateCompanyById(currentCompany);
                refreshListView();
            }
        });

        cancelSelected.setOnAction(eh -> {
            if (applicationListview.getSelectionModel().getSelectedItems().isEmpty()) {
                showErrorDialog("Please select an application before proceed");
                return;
            }

            Optional<ButtonType> result = PredefinedDialog.showConfirmationDialog("The action is irreversible");
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                for (var x : applicationListview.getSelectionModel().getSelectedItems()) {
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
                refreshListView();
            }
        });

        contextMenu.getItems().addAll(selectAll, clearSelection, rejectSelected, cancelSelected);

        selectAllBtn.setOnAction(eh -> {
            applicationListview.getSelectionModel().selectAll();
        });

        clearBtn.setOnAction(eh -> {
            clearSelections();
        });

        resetBtn.setOnAction(eh -> {
            resetFilteredApplications();
            statusComboBox.getSelectionModel().select("ALL");
            similarityScoreToggleBtn.setSelected(true);
            jobPostComboBox.getSelectionModel().select(allOption);
            applicationListview.scrollTo(0);
            clearSelections();
        });

        generateReportBtn.setOnAction(eh -> ReportGenerator.generateReport(generateReportContent()));
    }

    private String generateReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("==== Company Applications Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Company: %s\n\n", currentCompany.getCompanyName()));

        String selectedJobPost = jobPostComboBox.getSelectionModel().getSelectedItem().getTitle();
        String selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();

        report.append(String.format("Filtered by Job Post: %s\n", selectedJobPost));
        report.append(String.format("Filtered by Status: %s\n", selectedStatus));

        String sortingCriteria = (toggleGroup.getSelectedToggle() == similarityScoreToggleBtn)
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

    private void clearSelections() {
        applicationListview.getSelectionModel().clearSelection();
        refreshListView();
    }

    private void refreshListView() {
        var tempList = applicationListview.getItems();
        applicationListview.setItems(null);
        applicationListview.setItems(tempList);

        applicationListview.refresh();
    }

    private void setOriginalApplicationList() {
        originalApplications = getCompanyApplication();
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

        if (toggleGroup.getSelectedToggle() == similarityScoreToggleBtn) {
            sortApplicationBySimilarityScore();
        } else if (toggleGroup.getSelectedToggle() == dateToggleBtn) {
            sortApplicationByDate();
        }

        addFilteredListToObservableList();
        updateCountLabel();
        applicationListview.scrollTo(0);
    }

    private void sortApplicationBySimilarityScore() {
        filteredApplications.sort((app1, app2)
                -> Double.compare(similarityScores.get(app2), similarityScores.get(app1))
        );
    }

    private void sortApplicationByDate() {
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

    private void updateCountLabel() {
        countLabel.setText(String.format("[%d Applications] [%d selected]", filteredApplications.getNumberOfEntries(), applicationListview.getSelectionModel().getSelectedIndices().size()));
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
                            fxmlLoader = new FXMLLoader(App.class.getResource("companyapplication/ApplicationCard.fxml"));
                            node = fxmlLoader.load();
                            controller = fxmlLoader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (controller != null) {
                        controller.setApplication(item);
                        if (getListView().getSelectionModel().getSelectedItems().contains(item)) {
                            controller.setIsSelected();
                        } else {
                            controller.setNotSelected();
                        }
                    }
                    this.setGraphic(node);
                }
            }
        };
    }
}
