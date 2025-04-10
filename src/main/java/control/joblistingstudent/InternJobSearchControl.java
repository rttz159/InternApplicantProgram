package control.joblistingstudent;

import adt.ArrayList;
import adt.HashMap;
import adt.ListInterface;
import adt.MapInterface;
import boundary.joblistingstudent.InternJobSearchBoundary;
import dao.MainControlClass;
import entity.Company;
import entity.InternPost;
import entity.Student;
import java.time.LocalDate;
import static utils.FuzzyMatch.fuzzyMatch;
import utils.QualificationChecker;
import utils.SimilarityCalculator;

/**
 *
 * @author ziyang
 */
public class InternJobSearchControl {

    private ListInterface<InternPost> originalPost = new ArrayList<>();
    private ListInterface<InternPost> filteredPost = new ArrayList<>();
    private Student currentStudent;
    private MapInterface<InternPost, Double> similarityScores = new HashMap<>();
    private InternJobSearchBoundary boundary;
  
    public InternJobSearchControl(InternJobSearchBoundary boundary) {
        this.boundary = boundary;
        currentStudent = (Student) MainControlClass.getCurrentUser();
        enrichOriginalPostList();
        for (InternPost post : filteredPost) {
            double score = SimilarityCalculator.calculateSimilarity(currentStudent, post);
            similarityScores.put(post, score);
        }
    }

    public ListInterface<InternPost> getOriginalPost() {
        return originalPost;
    }

    public ListInterface<InternPost> getFilteredPost() {
        return filteredPost;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public MapInterface<InternPost, Double> getSimilarityScores() {
        return similarityScores;
    }

    public String generateReportContent() {
        StringBuilder report = new StringBuilder();
        report.append("==== Job Matching Report ====\n\n");
        report.append(String.format("Generated on: %s\n", LocalDate.now()));
        report.append(String.format("Student: %s\n\n", currentStudent.getName()));

        String searchQuery = boundary.getSearchTextField().getText().trim();
        report.append(String.format("Search Keyword: %s\n", searchQuery.isEmpty() ? "None" : searchQuery));

        String qualificationFilter = (String) boundary.getQualificationComboBox().getSelectionModel().getSelectedItem();
        report.append(String.format("Qualification Filter: %s\n", qualificationFilter));

        String sortingCriteria = boundary.getSimilarityScoreBtn().isSelected() ? "Similarity Score (Descending)" : "Location (Ascending)";
        report.append(String.format("Sorting By: %s\n\n", sortingCriteria));

        report.append("-------------------------------------------------------------------------------------------------------------------------------------------\n");
        report.append(String.format("%-30s | %-25s | %-15s | %-40s | %-10s\n",
                "Job Title", "Company", "State", "Full Address", "Score"));
        report.append("-------------------------------------------------------------------------------------------------------------------------------------------\n");

        for (InternPost post : filteredPost) {
            double score = similarityScores.get(post);
            Company tempCompany = null;
            for (var x : MainControlClass.getCompanies()) {
                if (x.getInternPosts().contains(post)) {
                    tempCompany = x;
                    break;
                }
            }
            report.append(String.format("%-30s | %-25s | %-15s | %-40s | %-10.2f\n",
                    post.getTitle(),
                    tempCompany.getCompanyName(),
                    post.getLocation().getState(),
                    post.getLocation().getFullAddress(),
                    score));
        }

        report.append("-------------------------------------------------------------------------------------------------------------------------------------------\n");
        report.append(String.format("\nTotal Jobs: %d\n", filteredPost.getNumberOfEntries()));

        return report.toString();
    }

    public void filterInternPostsBySearch() {
        String query = boundary.getSearchTextField().getText().trim().toLowerCase();

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

    public void filterInternPostsByQualification(int idx) {
        filterInternPostsBySearch();
        if (idx == 0) {
            if (boundary.getLocationBtn().isSelected()) {
                rankInternPostsByLocation();
            } else if (boundary.getSimilarityScoreBtn().isSelected()) {
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

        if (boundary.getLocationBtn().isSelected()) {
            rankInternPostsByLocation();
        } else if (boundary.getSimilarityScoreBtn().isSelected()) {
            rankInternPostsBySimilarity();
        }
    }

    public boolean checkQualification(InternPost internpost) {
        Student stud = (Student) MainControlClass.getCurrentUser();
        boolean qualification = QualificationChecker.checkQualification(stud.getStudentQualifications(), internpost.getInternPostQualifications());
        boolean experience = QualificationChecker.checkExperience(stud.getStudentExperiences(), internpost.getInterPostExperiences());
        boolean skill = QualificationChecker.checkSkill(stud.getStudentSkills(), internpost.getInternPostSkills());

        return qualification && experience && skill;
    }

    public void enrichOriginalPostList() {
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

    public void addFilteredListToObservableList() {
        boundary.getInternJobListView().getItems().clear();
        for (var x : filteredPost) {
            boundary.getInternJobListView().getItems().add(x);
        }
        boundary.getCountLabel().setText(String.format("[%d Jobs Found]", filteredPost.getNumberOfEntries()));
    }

    public void rankInternPostsBySimilarity() {
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((post1, post2)
                -> Double.compare(similarityScores.get(post2), similarityScores.get(post1))
        );

        addFilteredListToObservableList();
        boundary.getInternJobListView().scrollTo(0);
    }

    public void rankInternPostsByLocation() {
        if (currentStudent == null) {
            return;
        }

        filteredPost.sort((InternPost post1, InternPost post2) -> {
            double score1 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), post1.getLocation());
            double score2 = SimilarityCalculator.calculateLocationDistance(currentStudent.getLocation(), post2.getLocation());
            return Double.compare(score1, score2);
        });
        addFilteredListToObservableList();
        boundary.getInternJobListView().scrollTo(0);
    }
}
