package boundary.joblistingstudent;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import static utils.FuzzyMatch.fuzzyMatch;
import utils.QualificationChecker;
import utils.ReportGenerator;
import utils.SimilarityCalculator;

/**
 *
 * @author
 */
public class InternJobSearchController implements Initializable {

    @FXML
    private ListView<InternPost> internJobListView;

    @FXML
    private ToggleButton locationBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTextField;

    @FXML
    private ToggleButton similarityScoreBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button generateReportBtn;
    ;

    @FXML
    private Label countLabel;

    @FXML
    private ComboBox qualificationComboBox;

    private ListInterface<InternPost> originalPost = new ArrayList<>();

    private ListInterface<InternPost> filteredPost = new ArrayList<>();

    private Student currentStudent;

    private ToggleGroup toggleGroup;

    private MapInterface<InternPost, Double> similarityScores = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentStudent = (Student) MainControlClass.getCurrentUser();
        enrichOriginalPostList();

        countLabel.setText(String.format("[%d Jobs Found]", originalPost.getNumberOfEntries()));

        for (InternPost post : filteredPost) {
            double score = SimilarityCalculator.calculateSimilarity(currentStudent, post);
            similarityScores.put(post, score);
        }

        qualificationComboBox.getItems().addAll("All", "Qualified", "Not Qualified");
        qualificationComboBox.getSelectionModel().select("All");

        qualificationComboBox.setOnAction(eh -> {
            int selectedQualification = qualificationComboBox.getSelectionModel().getSelectedIndex();
            filterInternPostsByQualification(selectedQualification);
        });

        internJobListView.setCellFactory(new CustomListCellFactory(similarityScores));
        internJobListView.setSelectionModel(new NullSelectionModel());
        internJobListView.setFixedCellSize(100);
        internJobListView.setPlaceholder(new Label("No job listing available"));
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);

        resetBtn.setOnAction(eh -> {
            filteredPost.clear();
            for (InternPost post : originalPost) {
                filteredPost.append(post);
            }
            addFilteredListToObservableList();
            similarityScoreBtn.setSelected(true);
            searchTextField.setText("");
            qualificationComboBox.getSelectionModel().select("All");
            internJobListView.scrollTo(0);
            countLabel.setText(String.format("[%d Jobs Found]", originalPost.getNumberOfEntries()));
        });

        searchBtn.setOnAction(eh -> filterInternPostsBySearch());

        generateReportBtn.setOnAction(eh -> {
            ReportGenerator.generateReport(generateReportContent());
        });

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(locationBtn);
        toggleGroup.getToggles().add(similarityScoreBtn);
        similarityScoreBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
                return;
            }

            if (newValue == locationBtn) {
                rankInternPostsByLocation();
            } else if (newValue == similarityScoreBtn) {
                rankInternPostsBySimilarity();
            }
        });
        rankInternPostsBySimilarity();
    }

    private String generateReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("==== Job Matching Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Student: %s\n\n", currentStudent.getName()));

        String searchQuery = searchTextField.getText().trim();
        if (!searchQuery.isEmpty()) {
            report.append(String.format("Search Keyword: %s\n", searchQuery));
        } else {
            report.append("Search Keyword: None\n");
        }

        String qualificationFilter = (String) qualificationComboBox.getSelectionModel().getSelectedItem();
        report.append(String.format("Qualification Filter: %s\n", qualificationFilter));

        String sortingCriteria = similarityScoreBtn.isSelected() ? "Similarity Score (Descending)" : "Location (Ascending)";
        report.append(String.format("Sorting By: %s\n\n", sortingCriteria));
        
        report.append(
                "------------------------------------------------------\n");

        for (InternPost post : filteredPost) {
            double score = similarityScores.get(post);
            Company tempCompany = null;
            for (var x : MainControlClass.getCompanies()) {
                if (x.getInternPosts().contains(post)) {
                    tempCompany = x;
                    break;
                }
            }
            report.append(String.format("Job Title: %s\nCompany: %s\nState: %s\nFull Address: %s\nScore: %.2f\n",
                    post.getTitle(), tempCompany.getCompanyName(), post.getLocation().getState(), post.getLocation().getFullAddress(), score));
            report.append("------------------------------------------------------\n");
        }

        report.append(String.format("\nTotal Jobs: %d\n", filteredPost.getNumberOfEntries()));

        return report.toString();
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

    private void filterInternPostsByQualification(int idx) {
        filterInternPostsBySearch();
        if (idx == 0) {
            if (locationBtn.isSelected()) {
                rankInternPostsByLocation();
            } else if (similarityScoreBtn.isSelected()) {
                rankInternPostsBySimilarity();
            }
            return;
        }

        ArrayList<InternPost> tempPost = new ArrayList<>();
        if (idx == 1) {
            for (var x : filteredPost) {
                if (checkQualification(x)) {
                    tempPost.append(x);
                }
            }
        } else {
            for (var x : filteredPost) {
                if (!checkQualification(x)) {
                    tempPost.append(x);
                }
            }
        }

        filteredPost.clear();
        for (var x : tempPost) {
            filteredPost.append(x);
        }

        if (locationBtn.isSelected()) {
            rankInternPostsByLocation();
        } else if (similarityScoreBtn.isSelected()) {
            rankInternPostsBySimilarity();
        }
    }

    private boolean checkQualification(InternPost internpost) {
        Student stud = (Student) MainControlClass.getCurrentUser();
        boolean qualification = QualificationChecker.checkQualification(stud.getStudentQualifications(), internpost.getInternPostQualifications());
        boolean experience = QualificationChecker.checkExperience(stud.getStudentExperiences(), internpost.getInterPostExperiences());
        boolean skill = QualificationChecker.checkSkill(stud.getStudentSkills(), internpost.getInternPostSkills());

        return qualification && experience && skill;
    }

    private void enrichOriginalPostList() {
        this.originalPost = new ArrayList<>();
        for (var x : MainControlClass.getInternPost()) {
            if (x.getStatus()) {
                originalPost.append(x);
            }
        }
        this.filteredPost = new ArrayList<>();
        for (var x : originalPost) {
            this.filteredPost.append(x);
        }
    }

    private void addFilteredListToObservableList() {
        internJobListView.getItems().clear();
        for (var x : filteredPost) {
            internJobListView.getItems().add(x);
        }
        countLabel.setText(String.format("[%d Jobs Found]", filteredPost.getNumberOfEntries()));
    }

    private void rankInternPostsBySimilarity() {
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((post1, post2)
                -> Double.compare(similarityScores.get(post2), similarityScores.get(post1))
        );

        addFilteredListToObservableList();
        internJobListView.scrollTo(0);
    }

    private void rankInternPostsByLocation() {
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((InternPost post1, InternPost post2) -> {
            double score1 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), post1.getLocation());
            double score2 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), post2.getLocation());
            return Double.compare(score1, score2);
        });
        addFilteredListToObservableList();
        internJobListView.scrollTo(0);
    }

}

class CustomListCellFactory implements Callback<ListView<InternPost>, ListCell<InternPost>> {

    private MapInterface<InternPost, Double> similarityScores;

    public CustomListCellFactory(MapInterface<InternPost, Double> similarityScores) {
        this.similarityScores = similarityScores;
    }

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
                            fxmlLoader = new FXMLLoader(App.class.getResource("JobListingStudent/InternJobCard.fxml"));
                            node = fxmlLoader.load();
                            controller = fxmlLoader.getController();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (controller != null) {
                        double score;
                        try {
                            score = similarityScores.get(item);
                        } catch (Exception e) {
                            score = 0;
                        }
                        controller.setInternPost(item, score);
                    }
                    this.setGraphic(node);
                }
            }
        };
    }
}
