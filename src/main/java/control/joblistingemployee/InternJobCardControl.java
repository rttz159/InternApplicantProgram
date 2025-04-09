package control.joblistingemployee;

import boundary.joblistingemployee.InternJobCardBoundary;
import boundary.joblistingemployee.InternJobPostDetailsBoundary;
import control.joblistingstudent.ApplicationSharedState;
import com.rttz.assignment.App;
import entity.InternPost;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class InternJobCardControl {
    
    private InternPost internPost;
    private InternJobCardBoundary boundary;
    
    public InternJobCardControl(InternJobCardBoundary boundary, InternPost internPost){
        this.boundary = boundary;
        this.internPost = internPost;
    }
    
    public void setUp() {
        boundary.getTitleLabel().setText(internPost.getTitle() + " [ " + internPost.getLocation().getState() + " ]" + " [ " + (internPost.getStatus()?"Active":"Archived") + " ]");
        boundary.getDescriptionLabel().setWrapText(true);
        boundary.getDescriptionLabel().setText(internPost.getDesc());
        boundary.getDetailsButton().setOnAction(ev -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("InternJobManager/InternJobPostDetails.fxml"));
                Node node = fxmlLoader.load();
                InternJobPostDetailsBoundary controller = fxmlLoader.getController();
                controller.setInternPost(internPost);
                ApplicationSharedState.getInstance().setApplied(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Intern Post Details");
                alert.setHeaderText("");
                alert.getDialogPane().setContent(node);
                alert.getButtonTypes().clear();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setOnCloseRequest(event -> stage.close());
                alert.showAndWait();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
    
}
