package boundary.companyapplication;

import boundary.studentapplication.*;
import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.Company;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

/**
 *
 * @author rttz159
 */
public class CompanyApplicationManagementController implements Initializable {

    @FXML
    private ListView<Application> applicationListview;

    @FXML
    private ToggleButton dateToggleButton;

    @FXML
    private ToggleButton locationBtn;

    @FXML
    private ComboBox statusComboBox;

    @FXML
    private Label countLabel;

    private ToggleGroup toggleGroup;

    private ListInterface<Application> originalApplications;

    private ListInterface<Application> filteredApplications = new ArrayList<>();

    private Company currentCompany;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentCompany = (Company) MainControlClass.getCurrentUser();
        setOriginalApplicationList();
        resetFilteredApplications();

        countLabel.setText(String.format("[%d Applications]", originalApplications.getNumberOfEntries()));

        applicationListview.setSelectionModel(new NullSelectionModel());
        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        Styles.toggleStyleClass(applicationListview, Styles.STRIPED);

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
                //
            } else if (newValue == dateToggleButton) {
                //
            }
            applicationListview.scrollTo(0);
        }
        );
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
                    }
                    this.setGraphic(node);
                }
            }
        };
    }
}
