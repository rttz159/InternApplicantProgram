package boundary;

import adt.ArrayList;
import adt.HashSet;
import adt.ListInterface;
import adt.SetInterface;
import atlantafx.base.theme.Styles;
import com.rttz.assignment.App;
import control.MainControlClass;
import entity.Application;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import utils.InternshipSimulation;
import utils.SimilarityCalculator;

/**
 *
 * @author rttz159
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

    private ListInterface<InternPost> originalPost = new ArrayList<>();

    private ListInterface<InternPost> filteredPost = new ArrayList<>();

    private Student currentStudent;

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        internJobListView.setCellFactory(new CustomListCellFactory());
        internJobListView.setSelectionModel(new NullSelectionModel());
        internJobListView.setFixedCellSize(100);
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);

        resetBtn.setOnAction(eh -> {
            filteredPost.clear();
            for (InternPost post : originalPost) {
                filteredPost.append(post.deepCopy());
            }
            addFilteredListToObservableList();
            toggleGroup.selectToggle(null);
            searchTextField.setText("");
        });

        searchBtn.setOnAction(eh -> filterInternPostsBySearch());

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().add(locationBtn);
        toggleGroup.getToggles().add(similarityScoreBtn);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == locationBtn) {
                rankInternPostsByLocation();
            } else if (newValue == similarityScoreBtn) {
                rankInternPostsBySimilarity();
            } else {
                resetFilterList();
            }
        });

        enrichOriginalPostList();
        addFilteredListToObservableList();
    }

    private void filterInternPostsBySearch() {
        String query = searchTextField.getText().trim().toLowerCase();

        if (query.isEmpty() || query.isBlank() || query.equals("") || query.trim().isEmpty() || query.trim().isBlank()) {
            filteredPost.clear();
            for (InternPost post : originalPost) {
                filteredPost.append(post.deepCopy());
            }
            addFilteredListToObservableList();
            return;
        }

        filteredPost.clear();
        for (InternPost post : originalPost) {
            if (fuzzyMatch(query, post.getTitle().toLowerCase()) || fuzzyMatch(query, post.getDesc().toLowerCase())) {
                filteredPost.append(post.deepCopy());
            }
        }

        addFilteredListToObservableList();
    }

    private boolean fuzzyMatch(String query, String text) {
        int threshold = 8;

        if (text.contains(query)) {
            return true;
        }

        return levenshteinDistance(query, text) <= threshold;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private void enrichOriginalPostList() {
        ListInterface<InternPost> tempInternPost = new ArrayList<>();
        for (var x : MainControlClass.getCompanies()) {
            for (var y : x.getInternPosts()) {
                tempInternPost.append(y);
            }
        }
        this.originalPost = tempInternPost;
        this.filteredPost = new ArrayList<>();
        for (var x : originalPost) {
            this.filteredPost.append(x.deepCopy());
        }
    }

    private void resetFilterList() {
        filterInternPostsBySearch();
        toggleGroup.selectToggle(null);
    }

    private void addFilteredListToObservableList() {
        internJobListView.getItems().clear();
        for (var x : filteredPost) {
            internJobListView.getItems().add(x);
        }
    }

    private void rankInternPostsBySimilarity() {
        currentStudent = InternshipSimulation.getMockStudent();//(Student) MainControlClass.getCurrentUser();
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((InternPost post1, InternPost post2) -> {
            double score1 = SimilarityCalculator.calculateSimilarity(currentStudent, post1);
            double score2 = SimilarityCalculator.calculateSimilarity(currentStudent, post2);
            return Double.compare(score2, score1);
        });
        addFilteredListToObservableList();
    }

    private void rankInternPostsByLocation() {
        currentStudent = InternshipSimulation.getMockStudent();//(Student) MainControlClass.getCurrentUser();
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((InternPost post1, InternPost post2) -> {
            double score1 = SimilarityCalculator.calculateLocationScore(currentStudent.getLocation(), post1.getLocation());
            double score2 = SimilarityCalculator.calculateLocationScore(currentStudent.getLocation(), post2.getLocation());
            return Double.compare(score1, score2);
        });
        addFilteredListToObservableList();
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
                            fxmlLoader = new FXMLLoader(App.class.getResource("InternJobCard.fxml"));
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
                    this.setMaxWidth(Double.MAX_VALUE);
                    this.setMinHeight(USE_COMPUTED_SIZE);
                    this.setWrapText(true);
                }
            }
        };
    }

}

class NullSelectionModel<T> extends MultipleSelectionModel<T> {

    @Override
    public void select(int index) {
    }

    @Override
    public void select(T item) {
    }

    @Override
    public void clearSelection(int index) {
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void selectPrevious() {
    }

    @Override
    public void selectNext() {
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return FXCollections.observableArrayList();
    }

    @Override
    public void selectIndices(int i, int... ints) {
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void selectFirst() {
    }

    @Override
    public void selectLast() {
    }

    @Override
    public void clearAndSelect(int i) {
    }
}
