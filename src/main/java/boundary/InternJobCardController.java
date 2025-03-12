package boundary;

import entity.InternPost;
import javafx.fxml.FXML;
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
        this.titleLabel.setText(internPost.getTitle());
        this.descriptionLabel.setWrapText(true);
        this.descriptionLabel.setText(internPost.getDesc());
        this.detailsButton.setOnAction(ev -> {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hello how are you");
            alert.setResizable(false);
            alert.showAndWait();

        }
        );
    }

}
