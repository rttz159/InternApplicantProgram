package control;

import atlantafx.base.theme.Styles;
import static boundary.PredefinedDialog.showErrorDialog;
import static boundary.PredefinedDialog.showSuccessDialog;
import boundary.SignupPageBoundary;
import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import dao.MainControlClass;
import entity.Location;
import entity.User;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static utils.Validation.validateAddress;
import static utils.Validation.validateAge;
import static utils.Validation.validateContactNo;
import static utils.Validation.validateEmail;
import static utils.Validation.validateName;
import static utils.Validation.validatePassword;
import static utils.Validation.validateUsername;
import utils.builders.CompanyBuilder;
import utils.builders.StudentBuilder;

/**
 *
 * @author Raymond
 */
public class SignupPageControl {
    
    private SignupPageBoundary boundary;
    
    public SignupPageControl(SignupPageBoundary boundary){
        this.boundary = boundary;
    }
    
    public void validateFieldsAndSignUp(boolean isStudent) {
        boolean valid = true;
        TextField signupStudentUsernameTextField = boundary.getSignupStudentUsernameTextField();
        TextField signupStudentPasswordTextField = boundary.getSignupStudentPasswordTextField();
        TextField signupStudentContactNoTextField = boundary.getSignupStudentContactNoTextField();
        TextField signupStudentEmailTextField = boundary.getSignupStudentEmailTextField();
        TextField signupStudentNameTextField = boundary.getSignupStudentNameTextField();
        TextField signupStudentAgeTextField = boundary.getSignupStudentAgeTextField();
        TextArea signupStudentAddressTextArea = boundary.getSignupStudentAddressTextArea();
        
        TextField signupCompanyUsernameTextField = boundary.getSignupCompanyUsernameTextField();
        TextField signupCompanyPasswordTextField = boundary.getSignupCompanyPasswordTextField();
        TextField signupCompanyContactNoTextField = boundary.getSignupCompanyContactNoTextField();
        TextField signupCompanyEmailTextField = boundary.getSignupCompanyEmailTextField();
        TextField signupCompanyCompNameTextField = boundary.getSignupCompanyCompNameTextField();
        TextArea signupCompanyAddressTextArea = boundary.getSignupCompanyAddressTextArea();
        
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
                        .location(new Location(boundary.getSignupStudentCityComboBox().getSelectionModel().getSelectedItem().toString(), signupStudentAddressTextArea.getText()))
                        .name(signupStudentNameTextField.getText())
                        .age(Integer.parseInt(signupStudentAgeTextField.getText()))
                        .build();
            } else {
                tempUser = new CompanyBuilder()
                        .username(signupCompanyUsernameTextField.getText())
                        .password(signupCompanyPasswordTextField.getText())
                        .contactNo(signupCompanyContactNoTextField.getText())
                        .email(signupCompanyEmailTextField.getText())
                        .location(new Location(boundary.getSignupCompanyCityComboBox().getSelectionModel().getSelectedItem().toString(), signupStudentAddressTextArea.getText()))
                        .companyName(signupCompanyCompNameTextField.getText())
                        .industryType(boundary.getSignupCompanyIndustryTypeComboBox().getSelectionModel().getSelectedItem())
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
}
