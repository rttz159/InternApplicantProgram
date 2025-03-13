package boundary;

import com.rttz.assignment.App;
import entity.InternPost;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author rttz159
 */
public class InternJobCardController {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private Label titleLabel;

    private InternPost internPost;

    public void setInternPost(InternPost internpost) {
        this.internPost = internpost;
        setUp();
    }

    private void setUp() {
        this.titleLabel.setText(internPost.getTitle() + " [ " + internPost.getLocation().getState() + " ]");
        this.descriptionLabel.setWrapText(true);
        this.descriptionLabel.setText(internPost.getDesc());
        this.detailsButton.setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsController controller = fxmlLoader.getController();
                controller.setInternPost(internPost);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Internship Details");
                alert.setHeaderText("");
                alert.getDialogPane().setContent(node);
                alert.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(InternJobCardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}
