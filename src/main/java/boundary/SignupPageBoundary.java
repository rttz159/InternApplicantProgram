package boundary;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import control.SignupPageControl;
import entity.Experience;
import entity.Location;
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

/**
 *
 * @author Raymond
 */
public class SignupPageBoundary implements Initializable {

    @FXML private Button logInSignUpBtn;
    @FXML private TextArea signupCompanyAddressTextArea;
    @FXML private ComboBox<Location.MalaysianRegion> signupCompanyCityComboBox;
    @FXML private TextField signupCompanyCompNameTextField;
    @FXML private TextField signupCompanyContactNoTextField;
    @FXML private TextField signupCompanyEmailTextField;
    @FXML private ComboBox<Experience.IndustryType> signupCompanyIndustryTypeComboBox;
    @FXML private TextField signupCompanyPasswordTextField;
    @FXML private TextField signupCompanyUsernameTextField;
    @FXML private TextArea signupStudentAddressTextArea;
    @FXML private TextField signupStudentAgeTextField;
    @FXML private Button signupStudentBtn;
    @FXML private ComboBox<Location.MalaysianRegion> signupStudentCityComboBox;
    @FXML private TextField signupStudentContactNoTextField;
    @FXML private TextField signupStudentEmailTextField;
    @FXML private TextField signupStudentNameTextField;
    @FXML private TextField signupStudentPasswordTextField;
    @FXML private TextField signupStudentUsernameTextField;
    @FXML private Button singupCompanyBtn;
    @FXML private TabPane signupTabPane;
    
    private SignupPageControl control;

    public Button getLogInSignUpBtn() {
        return logInSignUpBtn;
    }

    public TextArea getSignupCompanyAddressTextArea() {
        return signupCompanyAddressTextArea;
    }

    public ComboBox<Location.MalaysianRegion> getSignupCompanyCityComboBox() {
        return signupCompanyCityComboBox;
    }

    public TextField getSignupCompanyCompNameTextField() {
        return signupCompanyCompNameTextField;
    }

    public TextField getSignupCompanyContactNoTextField() {
        return signupCompanyContactNoTextField;
    }

    public TextField getSignupCompanyEmailTextField() {
        return signupCompanyEmailTextField;
    }

    public ComboBox<Experience.IndustryType> getSignupCompanyIndustryTypeComboBox() {
        return signupCompanyIndustryTypeComboBox;
    }

    public TextField getSignupCompanyPasswordTextField() {
        return signupCompanyPasswordTextField;
    }

    public TextField getSignupCompanyUsernameTextField() {
        return signupCompanyUsernameTextField;
    }

    public TextArea getSignupStudentAddressTextArea() {
        return signupStudentAddressTextArea;
    }

    public TextField getSignupStudentAgeTextField() {
        return signupStudentAgeTextField;
    }

    public Button getSignupStudentBtn() {
        return signupStudentBtn;
    }

    public ComboBox<Location.MalaysianRegion> getSignupStudentCityComboBox() {
        return signupStudentCityComboBox;
    }

    public TextField getSignupStudentContactNoTextField() {
        return signupStudentContactNoTextField;
    }

    public TextField getSignupStudentEmailTextField() {
        return signupStudentEmailTextField;
    }

    public TextField getSignupStudentNameTextField() {
        return signupStudentNameTextField;
    }

    public TextField getSignupStudentPasswordTextField() {
        return signupStudentPasswordTextField;
    }

    public TextField getSignupStudentUsernameTextField() {
        return signupStudentUsernameTextField;
    }

    public Button getSingupCompanyBtn() {
        return singupCompanyBtn;
    }

    public TabPane getSignupTabPane() {
        return signupTabPane;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new SignupPageControl(this);
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
        signupCompanyPasswordTextField.setPromptText("6 character or above");

        singupCompanyBtn.setOnAction(ev -> {
            control.validateFieldsAndSignUp(false);
        });

        signupStudentBtn.setOnAction(ev -> {
            control.validateFieldsAndSignUp(true);
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
}
