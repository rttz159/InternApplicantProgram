package boundary.companyprofile;

import atlantafx.base.theme.Styles;
import static boundary.PredefinedDialog.showConfirmationDialog;
import dao.CompanyDAO;
import dao.MainControlClass;
import entity.Company;
import entity.Experience;
import entity.Location;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utils.Validation;

/**
 *
 * @author rttz159
 */
public class CompanyProfileManagmentBoundary implements Initializable {

    @FXML
    private TextArea addressTextArea;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField contactNoTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<Experience.IndustryType> indsutryTypeComboBox;

    @FXML
    private Button modifyBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button saveBtn;

    @FXML
    private ComboBox<Location.MalaysianRegion> stateComboBox;

    private Company currentCompany = (Company) MainControlClass.getCurrentUser();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enrichFields();
        setUpForReadOnly();
        modifyBtn.setOnAction(eh -> {
            setUpForModify();
        });

        saveBtn.setOnAction(eh -> {
            boolean valid = true;

            boolean nameValid = Validation.validateText(nameTextField.getText());
            nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;
            
            boolean contactNoValid = Validation.isValidPhoneNumber(contactNoTextField.getText());
            contactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !contactNoValid);
            contactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, contactNoValid);
            valid &= contactNoValid;

            boolean emailValid = Validation.isValidEmail(emailTextField.getText());
            emailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
            emailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
            valid &= emailValid;

            boolean addressValid = Validation.validateText(addressTextArea.getText());
            addressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            addressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            if (valid) {
                Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");

                if (result.isPresent() && result.get().getButtonData() == ButtonData.YES) {
                    
                    currentCompany.setCompanyName(nameTextField.getText());
                    currentCompany.setContactno(contactNoTextField.getText());
                    currentCompany.setEmail(emailTextField.getText());
                    currentCompany.setIndustryType(indsutryTypeComboBox.getSelectionModel().getSelectedItem());
                    currentCompany.setLocation(new Location(stateComboBox.getSelectionModel().getSelectedItem().toString(), addressTextArea.getText()));

                    CompanyDAO.updateCompanyById(currentCompany);
                    enrichFields();
                    setUpForReadOnly();
                    nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    contactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    contactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    emailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    emailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                    addressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    addressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
                }

            }
        });
        
        cancelBtn.setOnAction(eh -> {
            enrichFields();
            setUpForReadOnly();
        });
    }

    private void setUpForReadOnly() {
        cancelBtn.setVisible(false);
        cancelBtn.setManaged(false);
        saveBtn.setVisible(false);
        saveBtn.setManaged(false);

        modifyBtn.setVisible(true);
        modifyBtn.setManaged(true);

        addressTextArea.setEditable(false);
        contactNoTextField.setEditable(false);
        emailTextField.setEditable(false);
        nameTextField.setEditable(false);
        stateComboBox.setDisable(true);
        indsutryTypeComboBox.setDisable(true);

    }

    private void setUpForModify() {
        cancelBtn.setVisible(true);
        cancelBtn.setManaged(true);
        saveBtn.setVisible(true);
        saveBtn.setManaged(true);

        modifyBtn.setVisible(false);
        modifyBtn.setManaged(false);

        addressTextArea.setEditable(true);
        contactNoTextField.setEditable(true);
        emailTextField.setEditable(true);
        nameTextField.setEditable(true);
        stateComboBox.setDisable(false);
        indsutryTypeComboBox.setDisable(false);

    }

    private void enrichFields() {
        addressTextArea.setText(currentCompany.getLocation().getFullAddress());
        contactNoTextField.setText(currentCompany.getContactno());
        emailTextField.setText(currentCompany.getEmail());
        nameTextField.setText(currentCompany.getCompanyName());
        stateComboBox.getItems().addAll(Location.MalaysianRegion.values());
        stateComboBox.getSelectionModel().select(Location.MalaysianRegion.valueOf(currentCompany.getLocation().getState()));
        indsutryTypeComboBox.getItems().addAll(Experience.IndustryType.values());
        indsutryTypeComboBox.getSelectionModel().select(currentCompany.getIndustryType());
    }
}
