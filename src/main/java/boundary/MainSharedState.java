package boundary;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author Raymond
 */
public class MainSharedState {

    private static MainSharedState instance;
    private final BooleanProperty isStudent = new SimpleBooleanProperty(true);
    private final IntegerProperty selectedIdx = new SimpleIntegerProperty(0);
    private final BooleanProperty isLogined = new SimpleBooleanProperty(false);

    private MainSharedState() {
    }

    public static MainSharedState getInstance() {
        if (instance == null) {
            instance = new MainSharedState();
        }
        return instance;
    }

    public int getSelectedIdx() {
        return selectedIdx.get();
    }

    public void setSelectedIdx(int value) {
        selectedIdx.set(value);
    }

    public IntegerProperty appliedProperty() {
        return selectedIdx;
    }

    public void addSelectedIdxListener(ChangeListener<Number> listener) {
        selectedIdx.addListener(listener);
    }

    public boolean isStudent() {
        return this.isStudent.get();
    }

    public void setIsStudent(boolean value) {
        isStudent.set(value);
    }

    public void addIsStudentListener(ChangeListener<Boolean> listener) {
        isStudent.addListener(listener);
    }

    public boolean isLogined() {
        return this.isLogined.get();
    }

    public void setIsLogined(boolean value) {
        isLogined.set(value);
    }

    public void addIsLoginedListener(ChangeListener<Boolean> listener) {
        isLogined.addListener(listener);
    }

}
