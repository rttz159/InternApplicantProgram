package boundary;

import atlantafx.base.theme.Styles;
import static boundary.PredefinedDialog.showErrorDialog;
import static boundary.PredefinedDialog.showSuccessDialog;
import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import dao.MainControlClass;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Raymond
 */
public class LoginPageController implements Initializable {

    @FXML
    private Button logInSignUpBtn;

    @FXML
    private Button loginLoginCompanyBtn;

    @FXML
    private Button loginLoginStudentBtn;

    @FXML
    private PasswordField loginPasswordPasswordField;

    @FXML
    private TextField loginUsernameTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logInSignUpBtn.setOnAction(eh -> {
            try {
                Scene scene = new Scene(loadFXML("signup"), 800, 600);
                App.getPrimaryStage().setScene(scene);
                App.getPrimaryStage().setTitle("Sign Up Page");
            } catch (IOException ex) {
                System.out.println("There are exceptions when loading the Sign Up Page.");
            }
        });

        loginLoginStudentBtn.setOnAction(ev -> {
            validateFieldsAndLogin(true);
        });

        loginLoginCompanyBtn.setOnAction(ev -> {
            validateFieldsAndLogin(false);
        });
    }

    private void validateFieldsAndLogin(boolean isStudent) {

        boolean valid = true;
        if (!validateUsername(loginUsernameTextField.getText())) {
            loginUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
            loginUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            valid = false;
        } else {
            loginUsernameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
            loginUsernameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        }

        if (!validatePassword(loginPasswordPasswordField.getText())) {
            loginPasswordPasswordField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
            loginPasswordPasswordField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            valid = false;
        } else {
            loginPasswordPasswordField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
            loginPasswordPasswordField.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        }

        if (valid) {
            if (MainControlClass.logIn(loginUsernameTextField.getText(), loginPasswordPasswordField.getText(), isStudent)) {
                MainSharedState.getInstance().setIsStudent(isStudent);
                MainSharedState.getInstance().setIsLogined(true);
                showSuccessDialog("Login Successfully");
                return;
            }
        }
        showErrorDialog("Invalid Credentials || User not found.");
    }

    private boolean validateUsername(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if (password == null || password.isEmpty() || password.isBlank() || password.length() < 6) {
            return false;
        }

        return true;
    }
}
