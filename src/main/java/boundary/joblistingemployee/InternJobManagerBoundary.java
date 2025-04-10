package boundary.joblistingemployee;

import control.joblistingemployee.JobListingEmployeeShareState;
import atlantafx.base.theme.Styles;
import control.joblistingstudent.ApplicationSharedState;
import com.rttz.assignment.App;
import control.joblistingemployee.InternJobManagerControl;
import dao.CompanyDAO;
import dao.MainControlClass;
import entity.Company;
import entity.InternPost;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author 
 */
public class InternJobManagerBoundary implements Initializable {

    @FXML private Label countLabel;
    @FXML private ListView<InternPost> internJobListView;
    @FXML private Button resetBtn;
    @FXML private Button searchBtn;
    @FXML private TextField searchTextField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button addBtn;
    @FXML private Button toggleStatusBtn;
    
    private InternJobManagerControl control;

    public Label getCountLabel() {
        return countLabel;
    }

    public ListView<InternPost> getInternJobListView() {
        return internJobListView;
    }

    public Button getResetBtn() {
        return resetBtn;
    }

    public Button getSearchBtn() {
        return searchBtn;
    }

    public TextField getSearchTextField() {
        return searchTextField;
    }

    public ComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public Button getToggleStatusBtn() {
        return toggleStatusBtn;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new InternJobManagerControl(this);
        statusComboBox.getItems().addAll("All", "Active", "Archived");
        statusComboBox.getSelectionModel().select("All");
        statusComboBox.setOnAction(eh -> {
            int selectedStatus = statusComboBox.getSelectionModel().getSelectedIndex();
            control.filterInternPostsByStatus(selectedStatus);
        });

        countLabel.setText(String.format("[%d Jobs Found]", control.getOriginalPost().getNumberOfEntries()));

        internJobListView.setCellFactory(new CustomListCellFactory());
        internJobListView.setFixedCellSize(100);
        internJobListView.setPlaceholder(new Label("No job listing available"));
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);
        JobListingEmployeeShareState.getInstance().setListView(internJobListView);

        resetBtn.setOnAction(eh -> control.reset());

        searchBtn.setOnAction(eh -> control.filterInternPostsBySearch());

        toggleStatusBtn.setOnAction(eh -> control.toggleSelectedInternPost());

        addBtn.setOnAction(eh -> control.addInternPost());

        control.filterInternPostsByStatus(0);
    }
}

class CustomListCellFactory implements Callback<ListView<InternPost>, ListCell<InternPost>> {

    @Override
    public ListCell<InternPost> call(ListView<InternPost> listView) {
        return new ListCell<InternPost>() {
            private FXMLLoader fxmlLoader;
            private Node node;
            private InternJobCardBoundary controller;

            @Override
            protected void updateItem(InternPost item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    if (fxmlLoader == null) {
                        try {
                            fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternJobCard.fxml"));
                            node = fxmlLoader.load();
                            controller = fxmlLoader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (controller != null) {
                        controller.setInternPost(item);
                    }
                    this.setGraphic(node);
                }
            }
        };
    }
}
