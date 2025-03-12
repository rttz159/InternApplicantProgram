package boundary;

import adt.ArrayList;
import adt.HashSet;
import adt.ListInterface;
import adt.OrderPair;
import adt.SetInterface;
import atlantafx.base.theme.Styles;
import com.rttz.assignment.App;
import control.MainControlClass;
import entity.Application;
import utils.ExperienceBuilder;
import utils.InternPostBuilder;
import utils.QualificationBuilder;
import utils.SkillBuilder;
import entity.Experience;
import entity.InternPost;
import entity.Location;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
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

    private ObservableList<InternPost> post;

    private Student currentStudent;

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        internJobListView.setCellFactory(new CustomListCellFactory());
        internJobListView.setSelectionModel(new NullSelectionModel());
        internJobListView.setFixedCellSize(100);
        Styles.toggleStyleClass(internJobListView, Styles.STRIPED);

        post = FXCollections.observableArrayList();
        resetBtn.setOnAction(eh -> resetFilterList());

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

        //Testing Purpose
        buildSampleData();
        for (var x : originalPost) {
            this.filteredPost.append(x.deepCopy());
        }
        //Testing Purpose End

        addFilteredListToObservableList();
    }

    private void buildSampleData() {

        SetInterface<Qualification> softwareQualifications = new HashSet<>();
        softwareQualifications.add(new QualificationBuilder()
                .qualificationType(Qualification.QualificationType.DIPLOMA)
                .desc("Bachelor's degree in Computer Science or related field")
                .level(1)
                .institution("Any accredited university")
                .yearOfComplete(2025)
                .build());

        SetInterface<Skill> softwareSkills = new HashSet<>();
        softwareSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.PROGRAMMING)
                .name("Java")
                .proficiencyLevel(4)
                .build());
        softwareSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.PROGRAMMING)
                .name("Python")
                .proficiencyLevel(3)
                .build());

        SetInterface<Experience> softwareExperiences = new HashSet<>();
        softwareExperiences.add(new ExperienceBuilder()
                .desc("Developed a web application using Spring Boot")
                .industryType(Experience.IndustryType.TECHNOLOGY)
                .duration(6)
                .build());

        InternPost softwareIntern = new InternPostBuilder()
                .title("Software Engineering Intern")
                .desc("Work on real-world projects, collaborate with developers, and learn modern software development practices.")
                .location(new Location("San Francisco", "CA"))
                .minMaxSalary(new OrderPair<>(3000.0, 5000.0))
                .qualifications(softwareQualifications)
                .skills(softwareSkills)
                .experiences(softwareExperiences)
                .build();
        SetInterface<Qualification> marketingQualifications = new HashSet<>();
        marketingQualifications.add(new QualificationBuilder()
                .qualificationType(Qualification.QualificationType.DIPLOMA)
                .desc("Bachelor's degree in Marketing or Business Administration")
                .level(1)
                .institution("Any accredited university")
                .yearOfComplete(2024)
                .build());

        SetInterface<Skill> marketingSkills = new HashSet<>();
        marketingSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.MARKETING)
                .name("Social Media Management")
                .proficiencyLevel(4)
                .build());
        marketingSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.MARKETING)
                .name("SEO Optimization")
                .proficiencyLevel(3)
                .build());

        SetInterface<Experience> marketingExperiences = new HashSet<>();
        marketingExperiences.add(new ExperienceBuilder()
                .desc("Managed a company's social media presence and increased engagement by 30%")
                .industryType(Experience.IndustryType.MANUFACTURING)
                .duration(12)
                .build());

        InternPost marketingIntern = new InternPostBuilder()
                .title("Marketing Intern")
                .desc("Assist in content creation, social media management, and market research.")
                .location(new Location("Selangor", "NY"))
                .minMaxSalary(new OrderPair<>(2500.0, 4000.0))
                .qualifications(marketingQualifications)
                .skills(marketingSkills)
                .experiences(marketingExperiences)
                .build();
        SetInterface<Qualification> dataScienceQualifications = new HashSet<>();
        dataScienceQualifications.add(new QualificationBuilder()
                .qualificationType(Qualification.QualificationType.DOCTORATE)
                .desc("Master's degree in Data Science or related field")
                .level(2)
                .institution("Top universities")
                .yearOfComplete(2025)
                .build());

        SetInterface<Skill> dataScienceSkills = new HashSet<>();
        dataScienceSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.DATA_ANALYSIS)
                .name("Machine Learning")
                .proficiencyLevel(5)
                .build());
        dataScienceSkills.add(new SkillBuilder()
                .skillType(Skill.SkillType.DATA_ANALYSIS)
                .name("Python (Pandas, NumPy, Scikit-learn)")
                .proficiencyLevel(4)
                .build());

        SetInterface<Experience> dataScienceExperiences = new HashSet<>();
        dataScienceExperiences.add(new ExperienceBuilder()
                .desc("Built predictive models for sales forecasting using regression analysis")
                .industryType(Experience.IndustryType.TECHNOLOGY)
                .duration(8)
                .build());

        InternPost dataScienceIntern = new InternPostBuilder()
                .title("Data Science Intern")
                .desc("Analyze data, create machine learning models, and generate insights for business growth.")
                .location(new Location("Remote", "HJEHURHAJEH"))
                .minMaxSalary(new OrderPair<>(3500.0, 6000.0))
                .qualifications(dataScienceQualifications)
                .skills(dataScienceSkills)
                .experiences(dataScienceExperiences)
                .build();

        originalPost.append(softwareIntern);
        originalPost.append(marketingIntern);
        originalPost.append(dataScienceIntern);
        originalPost.append(softwareIntern);
        originalPost.append(marketingIntern);
        originalPost.append(dataScienceIntern);
    }

    public static Student getMockStudent() {
        Location location = new Location("Selangor", "123, Jalan University, Petaling Jaya");

        SetInterface<Qualification> qualifications = new HashSet<>();
        qualifications.add(new Qualification("Q1", Qualification.QualificationType.BACHELOR_DEGREE, "Computer Science", 1, "University of Malaya", 2024));
        qualifications.add(new Qualification("Q2", Qualification.QualificationType.CERTIFICATION, "AWS Certified Developer", 1, "AWS", 2023));

        SetInterface<Experience> experiences = new HashSet<>();
        experiences.add(new Experience("E1", "Software Development Intern at ABC Corp", Experience.IndustryType.TECHNOLOGY, 6));
        experiences.add(new Experience("E2", "Freelance Web Developer", Experience.IndustryType.TECHNOLOGY, 12));

        SetInterface<Skill> skills = new HashSet<>();
        skills.add(new Skill("S1", Skill.SkillType.PROGRAMMING, "Java", 5));
        skills.add(new Skill("S2", Skill.SkillType.PROGRAMMING, "Python", 4));
        skills.add(new Skill("S3", Skill.SkillType.CLOUD_COMPUTING, "AWS", 3));

        ListInterface<Application> applications = new ArrayList<>();

        return new Student(
                "S12345",
                "john_doe",
                "password123",
                "012-3456789",
                "john.doe@example.com",
                location,
                "John Doe",
                22,
                qualifications,
                experiences,
                skills,
                applications
        );
    }

    private void filterInternPostsBySearch() {
        String query = searchTextField.getText().trim().toLowerCase();

        if (query.isEmpty()) {
            resetFilterList();
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
        int threshold = 4;

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
        this.filteredPost.clear();
        for (var x : originalPost) {
            this.filteredPost.append(x.deepCopy());
        }
        internJobListView.getItems().clear();
        for (var x : filteredPost) {
            internJobListView.getItems().add(x);
        }
    }

    private void addFilteredListToObservableList() {
        internJobListView.getItems().clear();
        for (var x : filteredPost) {
            internJobListView.getItems().add(x);
        }
    }

    private void rankInternPostsBySimilarity() {
        currentStudent = getMockStudent();//(Student) MainControlClass.getCurrentUser();
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
        currentStudent = getMockStudent();//(Student) MainControlClass.getCurrentUser();
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((InternPost post1, InternPost post2) -> {
            double score1 = SimilarityCalculator.calculateLocationScore(currentStudent.getLocation(), post1.getLocation());
            double score2 = SimilarityCalculator.calculateLocationScore(currentStudent.getLocation(), post2.getLocation());
            return Double.compare(score2, score1);
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
