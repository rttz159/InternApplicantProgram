package boundary;

import control.MainControlClass;
import entity.Experience;
import entity.InternPost;
import entity.Qualification;
import entity.Skill;
import entity.Student;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import utils.InternshipSimulation;
import utils.QualificationChecker;

/**
 *
 * @author rttz159
 */
public class InternJobPostDetailsController {

    @FXML
    private Button applyBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private ListView<Experience> requiredExperienceListView;

    @FXML
    private ListView<Qualification> requiredQualificationListView;

    @FXML
    private ListView<Skill> requiredSkillListView;

    @FXML
    private TextField salaryRangeTextField;

    @FXML
    private TextField titleTextField;

    private InternPost internpost;

    public void setInternPost(InternPost internpost) {
        this.internpost = internpost;
        setUp();
    }

    private void setUp() {
        this.titleTextField.setText(this.internpost.getTitle().toUpperCase());
        this.descTextField.setText(this.internpost.getDesc());
        this.locationTextField.setText("State: " + this.internpost.getLocation().getState() + " , Full Address: " + this.internpost.getLocation().getFullAddress());
        this.salaryRangeTextField.setText(String.format("RM %.2f - RM %.2f", this.internpost.getMinMaxSalary().getX(), this.internpost.getMinMaxSalary().getY()));
        requiredExperienceListView.setCellFactory(param -> new javafx.scene.control.ListCell<Experience>() {
            @Override
            protected void updateItem(Experience item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Industry Type: %s, Duration: %d", item.getIndustryType().toString(), item.getDuration()));
            }
        });
        requiredQualificationListView.setCellFactory(param -> new javafx.scene.control.ListCell<Qualification>() {
            @Override
            protected void updateItem(Qualification item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Qualification Type: %s, Level: %d", item.getQualificationType().toString(), item.getLevel()));
            }
        });
        requiredSkillListView.setCellFactory(param -> new javafx.scene.control.ListCell<Skill>() {
            @Override
            protected void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("Skill Type: %s, Proficiency Level: %d", item.getSkilltype().toString(), item.getProficiencyLevel()));
            }
        });
        for (var x : internpost.getInterPostExperiences()) {
            requiredExperienceListView.getItems().add(x);
        }
        for (var x : internpost.getInternPostQualifications()) {
            requiredQualificationListView.getItems().add(x);
        }
        for (var x : internpost.getInternPostSkills()) {
            requiredSkillListView.getItems().add(x);
        }
        
        if(!checkQualification()){
            applyBtn.setDisable(true);
            applyBtn.setText("Not Qualified");
        }

    }

    private boolean checkQualification() {
        Student stud = InternshipSimulation.getMockStudent();//(Student) MainControlClass.getCurrentUser();
        return QualificationChecker.checkQualification(stud.getStudentQualifications(), internpost.getInternPostQualifications())
                && QualificationChecker.checkExperience(stud.getStudentExperiences(), internpost.getInterPostExperiences())
                && QualificationChecker.checkSkill(stud.getStudentSkills(), internpost.getInternPostSkills());
    }

}
