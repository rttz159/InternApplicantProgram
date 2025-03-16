package boundary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author rttz159
 */
public class DrawerMenuStudentController implements Initializable {

    @FXML
    private Button applicationBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button profileBtn;

    public void setUp() {
        this.homeBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 0) {
                System.out.println("Clicked 0");
                MainSharedState.getInstance().setSelectedIdx(0);
            }
        });
        this.applicationBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 1) {
                System.out.println("Clicked 1");
                MainSharedState.getInstance().setSelectedIdx(1);
            }
        });
        this.profileBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 2) {
                System.out.println("Clicked 2");
                MainSharedState.getInstance().setSelectedIdx(2);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUp();
    }
}
