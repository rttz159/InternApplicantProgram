package boundary;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import static boundary.PredefinedDialog.showErrorDialog;
import static boundary.PredefinedDialog.showSuccessDialog;
import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import dao.MainControlClass;
import utils.builders.CompanyBuilder;
import utils.builders.StudentBuilder;
import entity.Experience;
import entity.Location;
import entity.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import utils.Validation;

/**
 *
 * @author rttz159
 */
public class SignupPageController implements Initializable {

    @FXML
    private Button logInSignUpBtn;

    @FXML
    private TextArea signupCompanyAddressTextArea;

    @FXML
    private ComboBox<Location.MalaysianRegion> signupCompanyCityComboBox;

    @FXML
    private TextField signupCompanyCompNameTextField;

    @FXML
    private TextField signupCompanyContactNoTextField;

    @FXML
    private TextField signupCompanyEmailTextField;

    @FXML
    private ComboBox<Experience.IndustryType> signupCompanyIndustryTypeComboBox;

    @FXML
    private TextField signupCompanyPasswordTextField;

    @FXML
    private TextField signupCompanyUsernameTextField;

    @FXML
    private TextArea signupStudentAddressTextArea;

    @FXML
    private TextField signupStudentAgeTextField;

    @FXML
    private Button signupStudentBtn;

    @FXML
    private ComboBox<Location.MalaysianRegion> signupStudentCityComboBox;

    @FXML
    private TextField signupStudentContactNoTextField;

    @FXML
    private TextField signupStudentEmailTextField;

    @FXML
    private TextField signupStudentNameTextField;

    @FXML
    private TextField signupStudentPasswordTextField;

    @FXML
    private TextField signupStudentUsernameTextField;

    @FXML
    private Button singupCompanyBtn;

    @FXML
    private TabPane signupTabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logInSignUpBtn.setOnAction(eh -> {
            try {
                Scene scene = new Scene(loadFXML("login"), 800, 600);
                App.getPrimaryStage().setScene(scene);
                App.getPrimaryStage().setTitle("Log In Page");
            } catch (IOException ex) {
                System.out.println("There are exceptions when loading the Log In Page.");
            }
        });

        signupStudentCityComboBox.getItems().addAll(Location.MalaysianRegion.values());
        signupStudentCityComboBox.getSelectionModel().selectFirst();
        signupCompanyCityComboBox.getItems().addAll(Location.MalaysianRegion.values());
        signupCompanyCityComboBox.getSelectionModel().selectFirst();
        signupCompanyIndustryTypeComboBox.getItems().addAll(Experience.IndustryType.values());
        signupCompanyIndustryTypeComboBox.getSelectionModel().selectFirst();

        singupCompanyBtn.setOnAction(ev -> {
            validateFieldsAndSignUp(false);
        });

        signupStudentBtn.setOnAction(ev -> {
            validateFieldsAndSignUp(true);
        });

        signupTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldTab, newTab) -> {
                    if (newTab != oldTab) {
                        clearFields();
                    }
                    if (newTab != null) {
                        Node content = newTab.getContent();
                        if (content != null) {
                            var animation = Animations.fadeIn(content, Duration.seconds(.5));
                            animation.playFromStart();
                        }
                    }
                }
        );

    }

    private void clearFields() {
        signupStudentUsernameTextField.setText("");
        signupStudentPasswordTextField.setText("");
        signupStudentNameTextField.setText("");
        signupStudentEmailTextField.setText("");
        signupStudentContactNoTextField.setText("");
        signupStudentCityComboBox.getSelectionModel().selectFirst();
        signupStudentAgeTextField.setText("");
        signupStudentAddressTextArea.setText("");
        signupCompanyUsernameTextField.setText("");
        signupCompanyPasswordTextField.setText("");
        signupCompanyIndustryTypeComboBox.getSelectionModel().selectFirst();
        signupCompanyCityComboBox.getSelectionModel().selectFirst();
        signupCompanyEmailTextField.setText("");
        signupCompanyContactNoTextField.setText("");
        signupCompanyCompNameTextField.setText("");
        signupCompanyAddressTextArea.setText("");

        signupStudentNameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentNameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentEmailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentEmailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentContactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentContactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentPasswordTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentPasswordTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupStudentAgeTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupStudentAgeTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyPasswordTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyPasswordTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyContactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyContactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyEmailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyEmailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        signupCompanyCompNameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        signupCompanyCompNameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
    }

    private void validateFieldsAndSignUp(boolean isStudent) {
        boolean valid = true;
        if (isStudent) {
            boolean usernameValid = validateUsername(signupStudentUsernameTextField.getText());
            signupStudentUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !usernameValid);
            signupStudentUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, usernameValid);
            valid &= usernameValid;

            boolean passwordValid = validatePassword(signupStudentPasswordTextField.getText());
            signupStudentPasswordTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !passwordValid);
            signupStudentPasswordTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, passwordValid);
            valid &= passwordValid;

            boolean contactValid = validateContactNo(signupStudentContactNoTextField.getText());
            signupStudentContactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !contactValid);
            signupStudentContactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, contactValid);
            valid &= contactValid;

            boolean emailValid = validateEmail(signupStudentEmailTextField.getText());
            signupStudentEmailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
            signupStudentEmailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
            valid &= emailValid;

            boolean addressValid = validateAddress(signupStudentAddressTextArea.getText());
            signupStudentAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            signupStudentAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            boolean nameValid = validateName(signupStudentNameTextField.getText());
            signupStudentNameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            signupStudentNameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;

            boolean ageValid = validateAge(signupStudentAgeTextField.getText());
            signupStudentAgeTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !ageValid);
            signupStudentAgeTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, ageValid);
            valid &= ageValid;
        } else {
            boolean usernameValid = validateUsername(signupCompanyUsernameTextField.getText());
            signupCompanyUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !usernameValid);
            signupCompanyUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, usernameValid);
            valid &= usernameValid;

            boolean passwordValid = validatePassword(signupCompanyPasswordTextField.getText());
            signupCompanyPasswordTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !passwordValid);
            signupCompanyPasswordTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, passwordValid);
            valid &= passwordValid;

            boolean contactValid = validateContactNo(signupCompanyContactNoTextField.getText());
            signupCompanyContactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !contactValid);
            signupCompanyContactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, contactValid);
            valid &= contactValid;

            boolean emailValid = validateEmail(signupCompanyEmailTextField.getText());
            signupCompanyEmailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
            signupCompanyEmailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
            valid &= emailValid;

            boolean addressValid = validateAddress(signupCompanyAddressTextArea.getText());
            signupCompanyAddressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            signupCompanyAddressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            boolean nameValid = validateName(signupCompanyCompNameTextField.getText());
            signupCompanyCompNameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            signupCompanyCompNameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;
        }

        if (valid) {
            User tempUser = null;
            if (isStudent) {
                tempUser = new StudentBuilder()
                        .username(signupStudentUsernameTextField.getText())
                        .password(signupStudentPasswordTextField.getText())
                        .contactNo(signupStudentContactNoTextField.getText())
                        .email(signupStudentEmailTextField.getText())
                        .location(new Location(signupStudentCityComboBox.getSelectionModel().getSelectedItem().toString(), signupStudentAddressTextArea.getText()))
                        .name(signupStudentNameTextField.getText())
                        .age(Integer.parseInt(signupStudentAgeTextField.getText()))
                        .build();
            } else {
                tempUser = new CompanyBuilder()
                        .username(signupCompanyUsernameTextField.getText())
                        .password(signupCompanyPasswordTextField.getText())
                        .contactNo(signupCompanyContactNoTextField.getText())
                        .email(signupCompanyEmailTextField.getText())
                        .location(new Location(signupCompanyCityComboBox.getSelectionModel().getSelectedItem().toString(), signupStudentAddressTextArea.getText()))
                        .companyName(signupCompanyCompNameTextField.getText())
                        .industryType(signupCompanyIndustryTypeComboBox.getSelectionModel().getSelectedItem())
                        .build();
            }
            if (MainControlClass.signUp(tempUser)) {
                showSuccessDialog("Sign Up Successfully");
                try {
                    Scene scene = new Scene(loadFXML("login"), 800, 600);
                    App.getPrimaryStage().setScene(scene);
                    App.getPrimaryStage().setTitle("Log In Page");
                } catch (IOException ex) {
                    System.out.println("There are exceptions when loading the Log In Page.");
                }
                return;
            } else {
                showErrorDialog("User existed.");
                return;
            }
        }
        showErrorDialog("Invalid Credentials.");
    }

    private boolean validateUsername(String name) {
        return (name != null && !name.isBlank() && !name.isEmpty());
    }

    private boolean validatePassword(String password) {
        return (password != null && !password.isBlank() && !password.isEmpty() && password.length() >= 6);
    }

    private boolean validateContactNo(String contactNo) {
        return Validation.isValidPhoneNumber(contactNo);
    }

    private boolean validateEmail(String email) {
        return Validation.isValidEmail(email);
    }

    private boolean validateAddress(String address) {
        return (address != null && !address.isBlank() && !address.isEmpty());
    }

    private boolean validateName(String name) {
        return (name != null && !name.isBlank() && !name.isEmpty());
    }

    private boolean validateAge(String age) {
        if (age == null || age.isBlank() || age.isEmpty()) {
            return false;
        }
        boolean valid = true;
        for (char x : age.toCharArray()) {
            if (!Character.isDigit(x)) {
                valid = false;
                break;
            }
        }
        return valid;
    }
}
