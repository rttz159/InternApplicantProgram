package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 *
 * @author Raymond
 */
public class ReportGenerator {

    public static void showReport(String content) {
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(600, 400);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report");
        alert.getDialogPane().setContent(scrollPane);
        alert.showAndWait();
    }

    public static void generateReport(String reportContent) {
        showReport(reportContent);
    }
}
