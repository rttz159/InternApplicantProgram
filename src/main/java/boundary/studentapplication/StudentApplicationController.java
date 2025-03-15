package boundary.studentapplication;

import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import control.MainControlClass;
import entity.Application;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

public class StudentApplicationController implements Initializable {

    @FXML
    private ListView<Application> applicationListview;

    @FXML
    private ToggleButton dateToggleButton;

    @FXML
    private ToggleButton locationBtn;

    private ToggleGroup toggleGroup;

    private ListInterface<Application> originalApplications;

    private ListInterface<Application> filteredApplications = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        originalApplications = ((Student) MainControlClass.getCurrentUser()).getStudentApplications();

        applicationListview.setSelectionModel(new NullSelectionModel());
        applicationListview.setPlaceholder(new Label("No applications available"));
        applicationListview.setCellFactory(new CustomListCellFactory());
        applicationListview.setFixedCellSize(100);
        Styles.toggleStyleClass(applicationListview, Styles.STRIPED);

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(locationBtn);
        toggleGroup.getToggles().add(dateToggleButton);

        setOriginalApplicationList();
        resetFilteredApplications();
    }

    public void setOriginalApplicationList() {
        this.originalApplications = ((Student) MainControlClass.getCurrentUser()).getStudentApplications();
    }

    public void resetFilteredApplications() {
        filteredApplications.clear();
        for (var x : originalApplications) {
            filteredApplications.append(x.deepCopy());
        }
        applicationListview.getItems().clear();
        for (var x : filteredApplications) {
            applicationListview.getItems().add(x);
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
                            fxmlLoader = new FXMLLoader(App.class.getResource("ApplicationCard.fxml"));
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
