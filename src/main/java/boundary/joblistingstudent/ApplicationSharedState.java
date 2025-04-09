package boundary.joblistingstudent;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @Raymond 
 */
public class ApplicationSharedState {
    private static ApplicationSharedState instance;

    private final BooleanProperty applied = new SimpleBooleanProperty(false);

    private ApplicationSharedState() {}

    public static ApplicationSharedState getInstance() {
        if (instance == null) {
            instance = new ApplicationSharedState();
        }
        return instance;
    }

    public boolean isApplied() {
        return applied.get();
    }

    public void setApplied(boolean value) {
        applied.set(value);
    }

    public BooleanProperty appliedProperty() {
        return applied;
    }

    public void addAppliedListener(ChangeListener<Boolean> listener) {
        applied.addListener(listener);
    }

}
