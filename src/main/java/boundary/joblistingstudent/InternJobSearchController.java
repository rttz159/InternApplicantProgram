package boundary.joblistingstudent;

import adt.MapInterface;
import atlantafx.base.theme.Styles;
import boundary.NullSelectionModel;
import com.rttz.assignment.App;
import control.joblistingstudent.InternJobSearchControl;
import entity.InternPost;
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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;
import utils.ReportGenerator;

/**
 *
 * @Raymond
 */
public class InternJobSearchController implements Initializable {

    @FXML private ListView<InternPost> internJobListView;
    @FXML private ToggleButton locationBtn;
    @FXML private Button searchBtn;
    @FXML private TextField searchTextField;
    @FXML private ToggleButton similarityScoreBtn;
    @FXML private Button resetBtn;
    @FXML private Button generateReportBtn;
    @FXML private Label countLabel;
    @FXML private ComboBox qualificationComboBox;
    
    private ToggleGroup toggleGroup;
    private InternJobSearchControl control;

    public ListView<InternPost> getInternJobListView() {
        return internJobListView;
    }

    public ToggleButton getLocationBtn() {
        return locationBtn;
    }

    public Button getSearchBtn() {
        return searchBtn;
    }

    public TextField getSearchTextField() {
        return searchTextField;
    }

    public ToggleButton getSimilarityScoreBtn() {
        return similarityScoreBtn;
    }

    public Button getResetBtn() {
        return resetBtn;
    }

    public Button getGenerateReportBtn() {
        return generateReportBtn;
    }

    public Label getCountLabel() {
        return countLabel;
    }

    public ComboBox getQualificationComboBox() {
        return qualificationComboBox;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new InternJobSearchControl(this);

        countLabel.setText(String.format("[%d Jobs Found]", control.getOriginalPost().getNumberOfEntries()));

        qualificationComboBox.getItems().addAll("All", "Qualified", "Not Qualified");
        qualificationComboBox.getSelectionModel().select("All");

        qualificationComboBox.setOnAction(eh -> {
            int selectedQualification = qualificationComboBox.getSelectionModel().getSelectedIndex();
            control.filterInternPostsByQualification(selectedQualification);
        });

        internJobListView.setCellFactory(new CustomListCellFactory(control.getSimilarityScores()));
        internJobListView.setSelectionModel(new NullSelectionModel());
        internJobListView.setFixedCellSize(100);
        internJobListView.setPlaceholder(new Label("No job listing available"));
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);

        resetBtn.setOnAction(eh -> {
            control.getFilteredPost().clear();
            for (InternPost post : control.getOriginalPost()) {
                control.getFilteredPost().append(post);
            }
            control.addFilteredListToObservableList();
            similarityScoreBtn.setSelected(true);
            searchTextField.setText("");
            qualificationComboBox.getSelectionModel().select("All");
            internJobListView.scrollTo(0);
            countLabel.setText(String.format("[%d Jobs Found]", control.getOriginalPost().getNumberOfEntries()));
        });

        searchBtn.setOnAction(eh -> control.filterInternPostsBySearch());

        generateReportBtn.setOnAction(eh -> {
            ReportGenerator.generateReport(control.generateReportContent());
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
                control.rankInternPostsByLocation();
            } else if (newValue == similarityScoreBtn) {
                control.rankInternPostsBySimilarity();
            }
        });
        control.rankInternPostsBySimilarity();
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
