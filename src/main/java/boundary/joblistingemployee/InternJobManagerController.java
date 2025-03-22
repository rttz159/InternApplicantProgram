package boundary.joblistingemployee;

import adt.ArrayList;
import adt.ListInterface;
import atlantafx.base.theme.Styles;
import static boundary.PredefinedDialog.showErrorDialog;
import boundary.joblistingstudent.ApplicationSharedState;
import com.rttz.assignment.App;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
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
import static utils.FuzzyMatch.fuzzyMatch;

/**
 *
 * @author 
 */
public class InternJobManagerController implements Initializable {

    @FXML
    private Label countLabel;

    @FXML
    private ListView<InternPost> internJobListView;

    @FXML
    private Button resetBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button addBtn;

    @FXML
    private Button toggleStatusBtn;

    private ListInterface<InternPost> originalPost = new ArrayList<>();

    private ListInterface<InternPost> filteredPost = new ArrayList<>();

    private Company currentCompany;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentCompany = (Company) MainControlClass.getCurrentUser();
        originalPost = currentCompany.getInternPosts();

        statusComboBox.getItems().addAll("All", "Active", "Archived");
        statusComboBox.getSelectionModel().select("All");
        statusComboBox.setOnAction(eh -> {
            int selectedStatus = statusComboBox.getSelectionModel().getSelectedIndex();
            filterInternPostsByStatus(selectedStatus);
        });

        countLabel.setText(String.format("[%d Jobs Found]", originalPost.getNumberOfEntries()));

        internJobListView.setCellFactory(new CustomListCellFactory());
        internJobListView.setFixedCellSize(100);
        internJobListView.setPlaceholder(new Label("No job listing available"));
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);
        JobListingEmployeeShareState.getInstance().setListView(internJobListView);

        resetBtn.setOnAction(eh -> reset());

        searchBtn.setOnAction(eh -> filterInternPostsBySearch());

        toggleStatusBtn.setOnAction(eh -> toggleSelectedInternPost());

        addBtn.setOnAction(eh -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsController controller = fxmlLoader.getController();
                controller.setInternPost(null);
                ApplicationSharedState.getInstance().setApplied(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Intern Post");
                alert.setHeaderText("");
                alert.getDialogPane().setContent(node);
                alert.getButtonTypes().clear();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> stage.close());
                alert.showAndWait();
                if (controller.getInternPost() != null) {
                    this.currentCompany.getInternPosts().append(controller.getInternPost());
                    internJobListView.getItems().add(controller.getInternPost());
                    MainControlClass.getInternPost().append(controller.getInternPost());
                    MainControlClass.getInternPostMap().put(controller.getInternPost().getInterPostId(), controller.getInternPost());
                    CompanyDAO.updateCompanyById((Company) MainControlClass.getCurrentUser());
                    reset();
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        filterInternPostsByStatus(0);
    }

    private void reset() {
        filteredPost.clear();
        for (InternPost post : originalPost) {
            filteredPost.append(post);
        }
        addFilteredListToObservableList();
        searchTextField.setText("");
        statusComboBox.getSelectionModel().select("All");
        internJobListView.scrollTo(0);
        countLabel.setText(String.format("[%d Jobs Found]", originalPost.getNumberOfEntries()));
    }

    private void filterInternPostsByStatus(int idx) {
        filterInternPostsBySearch();
        if (idx == 0) {
            return;
        }

        ArrayList<InternPost> tempPost = new ArrayList<>();
        if (idx == 1) {
            for (var x : filteredPost) {
                if (x.getStatus()) {
                    tempPost.append(x);
                }
            }
        } else {
            for (var x : filteredPost) {
                if (!x.getStatus()) {
                    tempPost.append(x);
                }
            }
        }

        filteredPost.clear();
        for (var x : tempPost) {
            filteredPost.append(x);
        }

        addFilteredListToObservableList();
    }

    private void filterInternPostsBySearch() {
        String query = searchTextField.getText().trim().toLowerCase();

        if (query.isEmpty() || query.isBlank() || query.equals("") || query.trim().isEmpty() || query.trim().isBlank()) {
            filteredPost.clear();
            for (InternPost post : originalPost) {
                filteredPost.append(post);
            }
            addFilteredListToObservableList();
            return;
        }

        filteredPost.clear();
        for (InternPost post : originalPost) {
            if (fuzzyMatch(query, post.getTitle().toLowerCase()) || fuzzyMatch(query, post.getDesc().toLowerCase())) {
                filteredPost.append(post);
            }
        }

        addFilteredListToObservableList();
    }

    private void addFilteredListToObservableList() {
        internJobListView.getItems().clear();
        for (var x : filteredPost) {
            internJobListView.getItems().add(x);
        }
        countLabel.setText(String.format("[%d Jobs Found]", filteredPost.getNumberOfEntries()));
    }

    private void toggleSelectedInternPost() {
        InternPost tempInternPost = internJobListView.getSelectionModel().getSelectedItem();

        if (tempInternPost == null) {
            showErrorDialog("Please select an intern post before proceed");
            return;
        }
        boolean tempStatus = tempInternPost.getStatus();
        tempInternPost.setStatus(!tempStatus);
        for (var x : tempInternPost.getInternPostApplications()) {
            if (x.getStatus().equals(Application.Status.PENDING)) {
                x.setStatus(Application.Status.CANCELLED);
            }
        }

        if (tempStatus) {
            for (var x : MainControlClass.getStudents()) {
                boolean modified = false;
                for (var y : x.getStudentApplications()) {
                    if (y.getInternPostId().equals(tempInternPost.getInterPostId()) && y.getStatus().equals(Application.Status.PENDING)) {
                        y.setStatus(Application.Status.CANCELLED);
                        currentCompany.getInterviewManager().interviewCancelled(y.getInterview().getDate(), y.getInterview().getStart_time());
                        modified = true;
                    }
                }
                if (modified) {
                    StudentDAO.updateStudentById(x);
                }
            }
        }

        CompanyDAO.updateCompanyById(currentCompany);
        reset();
        internJobListView.scrollTo(tempInternPost);
    }

}

class CustomListCellFactory implements Callback<ListView<InternPost>, ListCell<InternPost>> {

    @Override
    public ListCell<InternPost> call(ListView<InternPost> listView) {
        return new ListCell<InternPost>() {
            private FXMLLoader fxmlLoader;
            private Node node;
            private InternJobCardController controller;

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
