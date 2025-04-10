package utils;

import javafx.scene.control.TextField;

/**
 *
 * @author Raymond
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

    public static boolean validateUsername(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            return false;
        }

        return true;
    }

    public static boolean validatePassword(String password) {
        if (password == null || password.isEmpty() || password.isBlank() || password.length() < 6) {
            return false;
        }

        return true;
    }

    public static boolean validateContactNo(String contactNo) {
        return Validation.isValidPhoneNumber(contactNo);
    }

    public static boolean validateEmail(String email) {
        return Validation.isValidEmail(email);
    }

    public static boolean validateAddress(String address) {
        return (address != null && !address.isBlank() && !address.isEmpty());
    }

    public static boolean validateName(String name) {
        return (name != null && !name.isBlank() && !name.isEmpty());
    }

    public static boolean validateAge(String age) {
        if (age == null || age.isBlank() || age.isEmpty()) {
            return false;
        }
        boolean valid = true;
        for (char x : age.toCharArray()) {
            if (!Character.isDigit(x)) {
                valid = false;
                break;
            }
        }
        return valid;
    }
    
}
