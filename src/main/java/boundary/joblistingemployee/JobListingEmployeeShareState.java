package boundary.joblistingemployee;

import entity.InternPost;
import javafx.scene.control.ListView;

/**
 *
 * @author USER
 */

public class JobListingEmployeeShareState {

    private static JobListingEmployeeShareState instance;

    private ListView<InternPost> listview = null;

    private JobListingEmployeeShareState() {
    }

    public static JobListingEmployeeShareState getInstance() {
        if (instance == null) {
            instance = new JobListingEmployeeShareState();
        }
        return instance;
    }

    public void refresh() {
        this.listview.refresh();
    }

    public void setListView(ListView<InternPost> listview) {
        this.listview = listview;
    }

}
