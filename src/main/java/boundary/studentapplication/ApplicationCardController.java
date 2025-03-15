package boundary.studentapplication;

import control.MainControlClass;
import entity.Application;
import entity.InternPost;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author rttz159
 */
public class ApplicationCardController {

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button detailsButton;

    @FXML
    private Label titleLabel;

    private Application application;

    private InternPost post;
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void setApplication(Application app) {
        this.application = app;
        this.post = MainControlClass.getInternPostMap().get(app.getInternPostId());
        setUp();
    }

    private void setUp() {
        this.titleLabel.setText(post.getTitle() + String.format(" [%s]", formatter.format(application.getInterview().getDate())));
        this.descriptionLabel.setText(post.getDesc());
    }

}
