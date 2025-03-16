package boundary.studentprofile;

import atlantafx.base.theme.Styles;
import entity.Skill;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static utils.Validation.validateText;
import static utils.Validation.validateDigit;
import utils.builders.SkillBuilder;

/**
 *
 * @author rttz159
 */
public class SkillDetailController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField proficiencyLevelTextField;

    @FXML
    private ComboBox<Skill.SkillType> skillTypeComboBox;

    private Skill skill = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        skillTypeComboBox.getItems().addAll(Skill.SkillType.values());
        skillTypeComboBox.getSelectionModel().selectFirst();

        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });

        addBtn.setOnAction(eh -> {
            boolean valid = true;
            
            boolean nameValid = validateText(nameTextField.getText());
            nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;

            boolean proficiencyLevelValid = validateDigit(proficiencyLevelTextField.getText());
            proficiencyLevelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !proficiencyLevelValid);
            proficiencyLevelTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, proficiencyLevelValid);
            valid &= proficiencyLevelValid;

            if (valid) {
                skill = new SkillBuilder()
                        .name(nameTextField.getText())
                        .proficiencyLevel(Integer.parseInt(proficiencyLevelTextField.getText()))
                        .skillType(skillTypeComboBox.getSelectionModel().getSelectedItem())
                        .build();

                ((Stage) addBtn.getScene().getWindow()).close();
            }
        });
    }

    public Skill getSkill() {
        return this.skill;
    }

}
