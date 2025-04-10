package boundary.companyprofile;

import atlantafx.base.theme.Styles;
import control.companyprofile.CompanyProfileManagementControl;
import entity.Company;
import entity.Experience;
import entity.Location;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author Raymond
 */
public class CompanyProfileManagmentBoundary implements Initializable {

    @FXML private TextArea addressTextArea;
    @FXML private Button cancelBtn;
    @FXML private TextField contactNoTextField;
    @FXML private TextField emailTextField;
    @FXML private ComboBox<Experience.IndustryType> indsutryTypeComboBox;
    @FXML private Button modifyBtn;
    @FXML private TextField nameTextField;
    @FXML private Button saveBtn;
    @FXML private ComboBox<Location.MalaysianRegion> stateComboBox;

    private CompanyProfileManagementControl control;
    private Company currentCompany;

    public TextArea getAddressTextArea() {
        return addressTextArea;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public TextField getContactNoTextField() {
        return contactNoTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public ComboBox<Experience.IndustryType> getIndsutryTypeComboBox() {
        return indsutryTypeComboBox;
    }

    public Button getModifyBtn() {
        return modifyBtn;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public ComboBox<Location.MalaysianRegion> getStateComboBox() {
        return stateComboBox;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        control = new CompanyProfileManagementControl(this);
        currentCompany = control.getCurrentCompany();
        enrichFields();
        setUpForReadOnly();
        modifyBtn.setOnAction(eh -> {
            setUpForModify();
        });

        saveBtn.setOnAction(eh -> control.saveCompanyInfo());

        cancelBtn.setOnAction(eh -> {
            enrichFields();
            setUpForReadOnly();
        });
    }

    public void setUpForReadOnly() {
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

    public void resetPseudoClass() {
        nameTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        nameTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        contactNoTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        contactNoTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        emailTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        emailTextField.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
        addressTextArea.pseudoClassStateChanged(Styles.STATE_DANGER, false);
        addressTextArea.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);
    }

    public void enrichFields() {
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
