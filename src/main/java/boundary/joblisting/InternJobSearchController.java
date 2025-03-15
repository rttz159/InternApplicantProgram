package boundary.joblisting;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import control.MainControlClass;
import entity.InternPost;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
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

    private MapInterface<InternPost, Double> similarityScores = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentStudent = (Student) MainControlClass.getCurrentUser();
        enrichOriginalPostList();

        for (InternPost post : filteredPost) {
            double score = SimilarityCalculator.calculateSimilarity(currentStudent, post);
            similarityScores.put(post, score);
        }

        internJobListView.setCellFactory(new CustomListCellFactory(similarityScores));
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
            internJobListView.scrollTo(0);
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
            internJobListView.scrollTo(0);
        });

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
        this.originalPost = MainControlClass.getInternPost();
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
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((post1, post2)
                -> Double.compare(similarityScores.get(post2), similarityScores.get(post1))
        );

        addFilteredListToObservableList();
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
                            fxmlLoader = new FXMLLoader(App.class.getResource("InternJobCard.fxml"));
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