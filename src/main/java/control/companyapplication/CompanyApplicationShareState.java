package control.companyapplication;

import entity.Application;
import javafx.scene.control.ListView;

/**
 *
 * @author Raymond
 */

public class CompanyApplicationShareState {

    private static CompanyApplicationShareState instance;

    private ListView<Application> listview = null;

    private CompanyApplicationShareState() {
    }

    public static CompanyApplicationShareState getInstance() {
        if (instance == null) {
            instance = new CompanyApplicationShareState();
        }
        return instance;
    }

    public void refresh() {
        this.listview.refresh();
    }

    public void setListView(ListView<Application> listview) {
        this.listview = listview;
    }

}
