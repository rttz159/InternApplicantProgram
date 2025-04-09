package control.studentapplication;

import boundary.studentapplication.ApplicationCardController;
import dao.MainControlClass;
import entity.Application;
import entity.InternPost;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Raymond
 */
public class ApplicationCardControl {
    private final ApplicationCardController boundary;
    private Application application;
    private InternPost post;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ApplicationCardControl(ApplicationCardController boundary) {
        this.boundary = boundary;
    }

    public void initialize() {
        boundary.getDetailsButton().setOnAction(eh -> handleDetailsButton());
    }

    public void setApplication(Application application) {
        this.application = application;
        this.post = MainControlClass.getInternPostMap().get(application.getInternPostId());
        updateUI();
    }

    private void updateUI() {
        String titleText = post.getTitle() + 
                         String.format(" [%s]", formatter.format(application.getInterview().getDate())) + 
                         String.format(" [%s]", post.getLocation().getState()) + 
                         String.format(" [%s]", application.getStatus().toString());
        
        boundary.getTitleLabel().setText(titleText);
        boundary.getDescriptionLabel().setText(post.getDesc());
    }

    private void handleDetailsButton() {
        try {
            boundary.showDetailsDialog(application);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
