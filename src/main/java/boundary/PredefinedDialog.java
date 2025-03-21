package boundary;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author rttz159
 */
public class PredefinedDialog {

    public static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.setResizable(false);
        alert.setWidth(600);
        alert.showAndWait();
    }

    public static void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.setWidth(600);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialog(String message) {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(message);
        alert.setWidth(600);

        ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelBtn = new ButtonType(
                "Cancel", ButtonBar.ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(yesBtn, cancelBtn);
        return alert.showAndWait();
    }
}