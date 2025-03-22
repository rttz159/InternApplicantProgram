package boundary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 *
 * @author Raymond
 */
public class DrawerMenuStudentController implements Initializable {

    @FXML
    private Button applicationBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button profileBtn;
    
    @FXML
    private Button logoutBtn;

    public void setUp() {
        this.homeBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 0) {
                MainSharedState.getInstance().setSelectedIdx(0);
            }
        });
        this.applicationBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 1) {
                MainSharedState.getInstance().setSelectedIdx(1);
            }
        });
        this.profileBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 2) {
                MainSharedState.getInstance().setSelectedIdx(2);
            }
        });
        this.logoutBtn.setOnAction(ev -> {
            if (MainSharedState.getInstance().getSelectedIdx() != 3) {
                MainSharedState.getInstance().setSelectedIdx(3);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUp();
    }
}
