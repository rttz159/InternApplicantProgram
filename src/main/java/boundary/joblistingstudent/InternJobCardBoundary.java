package boundary.joblistingstudent;

import control.joblistingstudent.InternJobCardControl;
import entity.InternPost;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author ziyang 
 */
public class InternJobCardBoundary {

    @FXML private Label descriptionLabel;
    @FXML private Button detailsButton;
    @FXML private Label titleLabel;

    private InternJobCardControl control;

    public Label getDescriptionLabel() {
        return descriptionLabel;
    }

    public Button getDetailsButton() {
        return detailsButton;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }
    
    public void setInternPost(InternPost post, double similarityScore) {
        control = new InternJobCardControl(this,post);
        control.setUp(similarityScore);
    }

}
