package boundary;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author rttz159
 */
public class DrawerMenuStudentController {

    @FXML
    private Button applicationBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Button profileBtn;

    private int selectedIdx = 0;

    private MainStudentDashboardController parentController;

    public void setDashboardController(MainStudentDashboardController parentController) {
        this.parentController = parentController;
        setUp();
    }

    public void setUp() {
        this.homeBtn.setOnAction(ev -> {
            if (selectedIdx != 0) {
                System.out.println("Clicked 0");
                parentController.changeMainContent("InternJobSearch");
                selectedIdx = 0;
            }
        });
        this.applicationBtn.setOnAction(ev -> {
            if (selectedIdx != 1) {
                System.out.println("Clicked 1");
                //parentController.changeMainContent("InternJobSearch");
                selectedIdx = 1;
            }
        });
        this.profileBtn.setOnAction(ev -> {
            if (selectedIdx != 2) {
                System.out.println("Clicked 2");
                //parentController.changeMainContent("InternJobSearch");
                selectedIdx = 2;
            }
        });
    }
}
