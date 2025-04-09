package control.companyprofile;

import atlantafx.base.theme.Styles;
import static boundary.PredefinedDialog.showConfirmationDialog;
import boundary.companyprofile.CompanyProfileManagmentBoundary;
import dao.CompanyDAO;
import dao.MainControlClass;
import entity.Company;
import entity.Location;
import java.util.Optional;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import utils.Validation;

/**
 *
 * @author Raymond
 */
public class CompanyProfileManagementControl {

    private Company currentCompany = (Company) MainControlClass.getCurrentUser();
    private CompanyProfileManagmentBoundary boundary;

    public Company getCurrentCompany() {
        return this.currentCompany;
    }

    public CompanyProfileManagementControl(CompanyProfileManagmentBoundary boundary) {
        this.boundary = boundary;
    }

    public void saveCompanyInfo() {
        {
            boolean valid = true;

            boolean nameValid = Validation.validateText(boundary.getNameTextField().getText());
            boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !nameValid);
            boundary.getNameTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, nameValid);
            valid &= nameValid;

            boolean contactNoValid = Validation.isValidPhoneNumber(boundary.getContactNoTextField().getText());
            boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !contactNoValid);
            boundary.getContactNoTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, contactNoValid);
            valid &= contactNoValid;

            boolean emailValid = Validation.isValidEmail(boundary.getEmailTextField().getText());
            boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_DANGER, !emailValid);
            boundary.getEmailTextField().pseudoClassStateChanged(Styles.STATE_SUCCESS, emailValid);
            valid &= emailValid;

            boolean addressValid = Validation.validateText(boundary.getAddressTextArea().getText());
            boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_DANGER, !addressValid);
            boundary.getAddressTextArea().pseudoClassStateChanged(Styles.STATE_SUCCESS, addressValid);
            valid &= addressValid;

            if (valid) {
                Optional<ButtonType> result = showConfirmationDialog("The action is irreversible.");

                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {

                    currentCompany.setCompanyName(boundary.getNameTextField().getText());
                    currentCompany.setContactno(boundary.getContactNoTextField().getText());
                    currentCompany.setEmail(boundary.getEmailTextField().getText());
                    currentCompany.setIndustryType(boundary.getIndsutryTypeComboBox().getSelectionModel().getSelectedItem());
                    currentCompany.setLocation(new Location(boundary.getStateComboBox().getSelectionModel().getSelectedItem().toString(), boundary.getAddressTextArea().getText()));

                    CompanyDAO.updateCompanyById(currentCompany);
                    boundary.enrichFields();
                    boundary.setUpForReadOnly();
                    boundary.resetPseudoClass();
                }

            }
        }
    }
}
