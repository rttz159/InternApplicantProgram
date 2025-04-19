package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 *
 * @author Raymond
 */
public class ReportGenerator {

    public static void showReport(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(850, 600);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report");
        alert.setHeaderText("Reports in Tabular Form");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(scrollPane);
        dialogPane.setPrefSize(870, 620); 

        alert.show();

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setWidth(900);
        stage.setHeight(650);
    }

    public static void generateReport(String reportContent) {
        showReport(reportContent);
    }
}
