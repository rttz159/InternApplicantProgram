package boundary.companyapplication;

import control.companyapplication.CompanyApplicationShareState;
import atlantafx.base.theme.Styles;
import boundary.PredefinedDialog;
import static boundary.PredefinedDialog.showErrorDialog;
import com.rttz.assignment.App;
import control.companyapplication.CompanyApplicationManagementControl;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import javafx.util.StringConverter;
import utils.ReportGenerator;
import utils.builders.InternPostBuilder;

/**
 *
 * @author Raymond
 */
public class CompanyApplicationManagementBoundary implements Initializable {

    @FXML private Button clearBtn;
    @FXML private ToggleButton dateToggleBtn;
    @FXML private Button selectAllBtn;
    @FXML private Button resetBtn;
    @FXML private Button generateReportBtn;
    @FXML private ToggleButton similarityScoreToggleBtn;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private ComboBox<InternPost> jobPostComboBox;
    @FXML private ListView<Application> applicationListview;
    @FXML private Label countLabel;
    private ContextMenu contextMenu = new ContextMenu();
    private ToggleGroup toggleGroup;
    private InternPost allOption;
    
    private CompanyApplicationManagementControl control;

    public Button getClearBtn() {
        return clearBtn;
    }

    public ToggleButton getDateToggleBtn() {
        return dateToggleBtn;
    }

    public Button getSelectAllBtn() {
        return selectAllBtn;
    }

    public Button getResetBtn() {
        return resetBtn;
    }

    public Button getGenerateReportBtn() {
        return generateReportBtn;
    }

    public ToggleButton getSimilarityScoreToggleBtn() {
        return similarityScoreToggleBtn;
    }

    public ComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }

    public ComboBox<InternPost> getJobPostComboBox() {
        return jobPostComboBox;
    }

    public ListView<Application> getApplicationListview() {
        return applicationListview;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public InternPost getAllOption() {
        return allOption;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new CompanyApplicationManagementControl(this);
        Company currentCompany = control.getCurrentCompany();

        countLabel.setText(String.format("[%d Applications]", control.getOriginalApplications().getNumberOfEntries()));

        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        applicationListview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Styles.toggleStyleClass(applicationListview, Styles.BORDERED);
        applicationListview.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Application>) change -> {
            control.updateCountLabel();
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
        statusComboBox.setOnAction(eh -> control.filterApplicationsByStatus());

        //Add a pseudo Intern Post
        allOption = new InternPostBuilder()
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
        jobPostComboBox.setOnAction(eh -> control.jobPostAction());

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
                control.sortApplicationBySimilarityScore();
            } else if (newValue == dateToggleBtn) {
                control.sortApplicationByDate();
            }

            control.addFilteredListToObservableList();
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

        cancelSelected.setOnAction(eh -> control.cancelSelectedAction());

        contextMenu.getItems().addAll(selectAll, clearSelection, rejectSelected, cancelSelected);

            selectAllBtn.setOnAction(eh -> {
            applicationListview.getSelectionModel().selectAll();
        });

        clearBtn.setOnAction(eh -> 
            clearSelections()
        );

        resetBtn.setOnAction(eh -> {
            control.resetFilteredApplications();
            statusComboBox.getSelectionModel().select("ALL");
            similarityScoreToggleBtn.setSelected(true);
            jobPostComboBox.getSelectionModel().select(allOption);
            applicationListview.scrollTo(0);
            clearSelections();
        });

        generateReportBtn.setOnAction(eh -> ReportGenerator.generateReport(control.generateReportContent()));
    }
    
    public void clearSelections() {
        applicationListview.getSelectionModel().clearSelection();
        refreshListView();
    }

    public void refreshListView() {
        var tempList = applicationListview.getItems();
        applicationListview.setItems(null);
        applicationListview.setItems(tempList);

        applicationListview.refresh();
    }
}

class CustomListCellFactory implements Callback<ListView<Application>, ListCell<Application>> {

    @Override
    public ListCell<Application> call(ListView<Application> listView) {
        return new ListCell<Application>() {
            private FXMLLoader fxmlLoader;
            private Node node;
            private ApplicationCardBoundary controller;

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
