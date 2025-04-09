package boundary.studentapplication;

import control.studentapplication.StudentApplicationShareState;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import control.studentapplication.StudentApplicationControl;
import entity.Application;
import java.io.IOException;
import java.net.URL;
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

/**
 *
 * @Raymond
 */
public class StudentApplicationBoundary implements Initializable {

    @FXML private ListView<Application> applicationListview;
    @FXML private ToggleButton dateToggleButton;
    @FXML private ToggleButton locationBtn;
    @FXML private Button generateReportBtn;
    @FXML private ComboBox statusComboBox;
    @FXML private Label countLabel;

    private ToggleGroup toggleGroup;
    private StudentApplicationControl control;

    public ListView<Application> getApplicationListview() {
        return applicationListview;
    }

    public ToggleButton getDateToggleButton() {
        return dateToggleButton;
    }

    public ToggleButton getLocationBtn() {
        return locationBtn;
    }

    public Button getGenerateReportBtn() {
        return generateReportBtn;
    }

    public ComboBox getStatusComboBox() {
        return statusComboBox;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new StudentApplicationControl(this);
        control.setOriginalApplicationList();
        control.resetFilteredApplications();
        control.rankApplicationByDate();

        countLabel.setText(String.format("[%d Applications]", control.getOriginalApplications().getNumberOfEntries()));

        applicationListview.setSelectionModel(new NullSelectionModel());
        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        Styles.toggleStyleClass(applicationListview, Styles.STRIPED);
        StudentApplicationShareState.getInstance().setListView(applicationListview);

        statusComboBox.getItems().addAll("ALL", "SUCCESS", "PENDING", "REJECTED", "CANCELLED");
        statusComboBox.getSelectionModel().select("ALL");
        statusComboBox.setOnAction(eh -> control.filterApplicationsByStatus());

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
                control.rankApplicationByLocation();
            } else if (newValue == dateToggleButton) {
                control.rankApplicationByDate();
            }
            applicationListview.scrollTo(0);
        }
        );

        generateReportBtn.setOnAction(eh -> ReportGenerator.generateReport(control.generateReportContent()));
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