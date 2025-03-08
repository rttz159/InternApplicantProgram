package boundary;

import com.rttz.assignment.App;
import static com.rttz.assignment.App.loadFXML;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 *
 * @author rttz159
 */
public class LoginPageController implements Initializable{
    @FXML
    private Button logInSignUpBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logInSignUpBtn.setOnAction(eh -> {
            try {
                Scene scene = new Scene(loadFXML("signup"), 640, 480);
                App.getPrimaryStage().setScene(scene);
                App.getPrimaryStage().setTitle("Sign Up Page");
            } catch (IOException ex) {
                System.out.println("There are exceptions when loading the Sign Up Page.");
            }
        });
        
    }
    
    
}
