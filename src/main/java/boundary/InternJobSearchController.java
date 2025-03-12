package boundary;

import entity.InternPost;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 *
 * @author rttz159
 */
public class InternJobSearchController implements Initializable {

    @FXML
    private ListView<InternPost> internJobListView;
    
    private ObservableList<InternPost> post;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        internJobListView.setCellFactory(new CustomListCellFactory());
        post = FXCollections.observableArrayList(
                new 
        );
    }

}

class CustomListCellFactory implements Callback<ListView<InternPost>, ListCell<InternPost>> {

    @Override
    public ListCell<InternPost> call(ListView<InternPost> listView) {
        return new ListCell<InternPost>() {
            private VBox content;
            private Label titleLabel;
            private Label descriptionLabel;

            {
                titleLabel = new Label();
                descriptionLabel = new Label();
                content = new VBox(titleLabel, descriptionLabel);
            }

            @Override
            protected void updateItem(InternPost item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.setText(item.getTitle());
                    descriptionLabel.setText(item.getDesc());
                    setGraphic(content);
                }
            }
        };
    }
}
