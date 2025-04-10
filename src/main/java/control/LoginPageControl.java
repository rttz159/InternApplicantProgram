package control;

import atlantafx.base.theme.Styles;
import boundary.LoginPageBoundary;
import boundary.MainSharedState;
import static boundary.PredefinedDialog.showErrorDialog;
import static boundary.PredefinedDialog.showSuccessDialog;
import dao.MainControlClass;
import static utils.Validation.validatePassword;
import static utils.Validation.validateUsername;

/**
 *
 * @author Raymond
 */
public class LoginPageControl {
    
    private LoginPageBoundary boundary;
    
    public LoginPageControl(LoginPageBoundary boundary){
        this.boundary = boundary;
    }
    
    public void validateFieldsAndLogin(boolean isStudent) {

        boolean valid = true;
        if (!validateUsername(boundary.getLoginUsernameTextField().getText())) {
            boundary.getLoginUsernameTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
            boundary.getLoginUsernameTextField().pseudoClassStateChanged(Styles.STATE_DANGER, true);
            valid = false;
        } else {
            boundary.getLoginUsernameTextField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
            boundary.getLoginUsernameTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        }

        if (!validatePassword(boundary.getLoginPasswordPasswordField().getText())) {
            boundary.getLoginPasswordPasswordField().pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
            boundary.getLoginPasswordPasswordField().pseudoClassStateChanged(Styles.STATE_DANGER, true);
            valid = false;
        } else {
            boundary.getLoginPasswordPasswordField().pseudoClassStateChanged(Styles.STATE_DANGER, false);
            boundary.getLoginPasswordPasswordField().pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        }

        if (valid) {
            if (MainControlClass.logIn(boundary.getLoginUsernameTextField().getText(), boundary.getLoginPasswordPasswordField().getText(), isStudent)) {
                MainSharedState.getInstance().setIsStudent(isStudent);
                MainSharedState.getInstance().setIsLogined(true);
                showSuccessDialog("Login Successfully");
                return;
            }
        }
        showErrorDialog("Invalid Credentials || User not found.");
    }

}
