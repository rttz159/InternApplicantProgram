package boundary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 *
 * @author rttz159
 */
public class NullSelectionModel<T> extends MultipleSelectionModel<T> {

    @Override
    public void select(int index) {
    }

    @Override
    public void select(T item) {
    }

    @Override
    public void clearSelection(int index) {
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void selectPrevious() {
    }

    @Override
    public void selectNext() {
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<T> getSelectedItems() {
        return FXCollections.observableArrayList();
    }

    @Override
    public void selectIndices(int i, int... ints) {
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void selectFirst() {
    }

    @Override
    public void selectLast() {
    }

    @Override
    public void clearAndSelect(int i) {
    }
}


