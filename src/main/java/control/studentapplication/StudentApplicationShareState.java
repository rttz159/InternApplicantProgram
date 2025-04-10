package control.studentapplication;

import entity.Application;
import javafx.scene.control.ListView;

/**
 *
 * @author ziyang
 */
public class StudentApplicationShareState {

    private static StudentApplicationShareState instance;

    private ListView<Application> listview = null;

    private StudentApplicationShareState() {
    }

    public static StudentApplicationShareState getInstance() {
        if (instance == null) {
            instance = new StudentApplicationShareState();
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
