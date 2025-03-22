package boundary;

import atlantafx.base.theme.Styles;
import entity.Qualification;
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
import utils.builders.QualificationBuilder;

/**
 *
 * @author rttz159
 */
public class QualificationDetailController implements Initializable {

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField descTextField;

    @FXML
    private TextField institutionTextField;

    @FXML
    private TextField levelTextField;

    @FXML
    private ComboBox<Qualification.QualificationType> qualificationTypeComboBox;

    @FXML
    private TextField yearOfCompleteTextField;

    private Qualification qualification = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        qualificationTypeComboBox.getItems().addAll(Qualification.QualificationType.values());
        qualificationTypeComboBox.getSelectionModel().selectFirst();
        qualificationTypeComboBox.setEditable(false);
        qualificationTypeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Qualification.QualificationType qualificationType) {
                return qualificationType == null ? "" : qualificationType.name();
            }

            @Override
            public Qualification.QualificationType fromString(String string) {
                try {
                    return Qualification.QualificationType.valueOf(string);
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

            boolean institutionValid = validateText(institutionTextField.getText());
            institutionTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !institutionValid);
            institutionTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, institutionValid);
            valid &= institutionValid;

            boolean levelValid = validateDigit(levelTextField.getText());
            levelTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !levelValid);
            levelTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, levelValid);
            valid &= levelValid;

            boolean yearOfCompleteValid = validateDigit(yearOfCompleteTextField.getText());
            yearOfCompleteTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !yearOfCompleteValid);
            yearOfCompleteTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, yearOfCompleteValid);
            valid &= yearOfCompleteValid;

            if (valid) {
                qualification = new QualificationBuilder()
                        .institution(institutionTextField.getText())
                        .desc(descTextField.getText())
                        .level(Integer.parseInt(levelTextField.getText()))
                        .yearOfComplete(Integer.parseInt(yearOfCompleteTextField.getText()))
                        .qualificationType(qualificationTypeComboBox.getSelectionModel().getSelectedItem())
                        .build();

                ((Stage) addBtn.getScene().getWindow()).close();
            }
        });
    }

    public Qualification getQualification() {
        return this.qualification;
    }
}
