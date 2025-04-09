package control.joblistingemployee;

import adt.ArrayList;
import adt.ListInterface;
import static boundary.PredefinedDialog.showErrorDialog;
import boundary.joblistingemployee.InternJobManagerBoundary;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import static utils.FuzzyMatch.fuzzyMatch;

/**
 *
 * @author Raymond
 */
public class InternJobManagerControl {

    private ListInterface<InternPost> originalPost = new ArrayList<>();
    private ListInterface<InternPost> filteredPost = new ArrayList<>();
    private Company currentCompany;

    private InternJobManagerBoundary boundary;

    public ListInterface<InternPost> getOriginalPost() {
        return originalPost;
    }

    public ListInterface<InternPost> getFilteredPost() {
        return filteredPost;
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public InternJobManagerControl(InternJobManagerBoundary boundary) {
        this.boundary = boundary;
        currentCompany = (Company) MainControlClass.getCurrentUser();
        originalPost = currentCompany.getInternPosts();
    }

    public void reset() {
        filteredPost.clear();
        for (InternPost post : originalPost) {
            filteredPost.append(post);
        }
        addFilteredListToObservableList();
        boundary.getSearchTextField().setText("");
        boundary.getStatusComboBox().getSelectionModel().select("All");
        boundary.getInternJobListView().scrollTo(0);
        boundary.getCountLabel().setText(String.format("[%d Jobs Found]", originalPost.getNumberOfEntries()));
    }

    public void filterInternPostsByStatus(int idx) {
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

    public void addFilteredListToObservableList() {
        boundary.getInternJobListView().getItems().clear();
        for (var x : filteredPost) {
            boundary.getInternJobListView().getItems().add(x);
        }
        boundary.getCountLabel().setText(String.format("[%d Jobs Found]", filteredPost.getNumberOfEntries()));
    }

    public void toggleSelectedInternPost() {
        InternPost tempInternPost = boundary.getInternJobListView().getSelectionModel().getSelectedItem();

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
        boundary.getInternJobListView().scrollTo(tempInternPost);
    }

}
