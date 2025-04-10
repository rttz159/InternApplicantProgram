package boundary;

import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import control.LoginPageControl;
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
public class LoginPageBoundary implements Initializable {

    @FXML private Button logInSignUpBtn;
    @FXML private Button loginLoginCompanyBtn;
    @FXML private Button loginLoginStudentBtn;
    @FXML private PasswordField loginPasswordPasswordField;
    @FXML private TextField loginUsernameTextField;
    
    private LoginPageControl control;

    public Button getLogInSignUpBtn() {
        return logInSignUpBtn;
    }

    public Button getLoginLoginCompanyBtn() {
        return loginLoginCompanyBtn;
    }

    public Button getLoginLoginStudentBtn() {
        return loginLoginStudentBtn;
    }

    public PasswordField getLoginPasswordPasswordField() {
        return loginPasswordPasswordField;
    }

    public TextField getLoginUsernameTextField() {
        return loginUsernameTextField;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new LoginPageControl(this);
        
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
            control.validateFieldsAndLogin(true);
        });

        loginLoginCompanyBtn.setOnAction(ev -> {
            control.validateFieldsAndLogin(false);
        });
    }
}
