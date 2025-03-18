package utils;

import javafx.scene.control.TextField;

/**
 *
 * @author rttz159
 */
public class Validation {

    public static boolean validateText(String desc) {
        return (desc != null && !desc.isBlank() && !desc.isEmpty());
    }

    public static boolean validateDigit(String text) {
        if (text == null || text.isBlank() || text.isEmpty()) {
            return false;
        }
        boolean valid = true;
        for (char x : text.toCharArray()) {
            if (!Character.isDigit(x)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    public static boolean isTextFieldNotEmpty(TextField textField) {
        String text = textField.getText();
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(\\+?6?01)[0-46-9]-*[0-9]{7,8}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    public static boolean isValidDouble(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
