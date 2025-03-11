package boundary;

import adt.ArrayList;
import java.net.URL;
import adt.ListInterface;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author rttz159
 */
public class InterviewStudentSchedulerController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private DatePicker interviewSchedulerDatePicker;

    @FXML
    private Pagination interviewSchedulerPagination;

    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        interviewSchedulerPagination.setPageFactory(pageIndex -> {
            GridPane gridPane = createGridPaneForPage(pageIndex);
            HBox tempHBox = new HBox();
            VBox tempVBox = new VBox();
            VBox.setVgrow(tempVBox, Priority.ALWAYS);
            HBox.setHgrow(gridPane, Priority.ALWAYS);
            tempHBox.setAlignment(Pos.CENTER);
            tempVBox.setAlignment(Pos.CENTER);
            tempHBox.getChildren().add(gridPane);
            tempVBox.getChildren().add(tempHBox);
            return tempVBox;
        });

        toggleGroup = new ToggleGroup();
        ListInterface<String> items = new ArrayList<>();
        items.append("hello");
        items.append("hello1");
        items.append("hello2");
        items.append("hello3");
    }

    private GridPane createGridPaneForPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int itemsPerPage = 9;
        int start = pageIndex * itemsPerPage;
        for (int i = 0; i < itemsPerPage; i++) {
            int itemIndex = start + i;
            Button button = new Button("Item " + itemIndex);
            int col = i % 3;
            int row = i / 3;
            gridPane.add(button, col, row);
        }
        return gridPane;
    }

}
