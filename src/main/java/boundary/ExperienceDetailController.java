package boundary;

import atlantafx.base.theme.Styles;
import entity.Experience;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import static utils.Validation.validateDigit;
import static utils.Validation.validateText;
import utils.builders.ExperienceBuilder;

/**
 *
 * @author rttz159
 */
public class ExperienceDetailController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField durationTextField;

    @FXML
    private ComboBox<Experience.IndustryType> industryTypeComboBox;

    private Experience experience = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        industryTypeComboBox.getItems().addAll(Experience.IndustryType.values());
        industryTypeComboBox.getSelectionModel().selectFirst();
        industryTypeComboBox.setEditable(false);
        industryTypeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Experience.IndustryType experienceType) {
                return experienceType == null ? "" : experienceType.name();
            }

            @Override
            public Experience.IndustryType fromString(String string) {
                try {
                    return Experience.IndustryType.valueOf(string);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        });

        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });

        addBtn.setOnAction(eh -> {
            boolean valid = true;
            
            boolean descValid = validateText(descTextField.getText());
            descTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !descValid);
            descTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, descValid);
            valid &= descValid;

            boolean durationValid = validateDigit(durationTextField.getText());
            durationTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !durationValid);
            durationTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, durationValid);
            valid &= durationValid;

            if (valid) {
                experience = new ExperienceBuilder()
                        .desc(descTextField.getText())
                        .industryType(industryTypeComboBox.getSelectionModel().getSelectedItem())
                        .duration(Integer.parseInt(durationTextField.getText()))
                        .build();
                
                ((Stage) addBtn.getScene().getWindow()).close();
            }
        });
    }

    public Experience getExperience() {
        return this.experience;
    }

}
