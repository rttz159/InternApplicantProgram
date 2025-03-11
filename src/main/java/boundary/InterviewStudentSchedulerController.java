package boundary;

import adt.ArrayList;
import adt.ListInterface;
import adt.interval.Interval;
import adt.interval.TimeInterval;
import control.InterviewScheduler;
import java.net.URL;
import control.MainControlClass;
import entity.Company;
import entity.InternPost;
import entity.Interview;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author rttz159
 */
public class InterviewStudentSchedulerController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private DatePicker interviewSchedulerDatePicker;

    @FXML
    private Pagination interviewSchedulerPagination;

    @FXML
    private Label jobtitleTextLabel;

    private ToggleGroup toggleGroup;

    private InternPost internPost;

    private Company company;

    private LocalDate selectedDate;

    private TimeInterval selectedTime;

    private Interview interview;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        internPost = MainControlClass.getCompanies().getEntry(0).getInternPosts().getEntry(0); // Added due to testing purpose
        toggleGroup = new ToggleGroup();
        selectedDate = workingDate();

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                selectedTime = (TimeInterval) selectedButton.getUserData();
            } else {
                selectedTime = null;
            }

        });

        interviewSchedulerDatePicker.setValue(selectedDate);

        interviewSchedulerDatePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now()) || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                }
            }
        });

        interviewSchedulerDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                this.selectedDate = newValue;
                updatePagination();
            }
        });

        cancelBtn.setOnAction(eh -> {
            ((Stage) cancelBtn.getScene().getWindow()).close();
        });

        confirmBtn.setOnAction(eh -> {
            if (selectedDate == null) {
                showErrorDialog("Please select a date");
                return;
            }

            if (selectedTime == null) {
                showErrorDialog("Please select a time");
                return;
            }

            Optional<ButtonType> result = showConfirmationDialog(String.format("Selected Date and Time Slot: %s, %s", selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), selectedTime.toString()));

            if (result.isPresent()) {
                if (result.get().getButtonData() == ButtonData.YES) {
                    ((Stage) cancelBtn.getScene().getWindow()).close();
                    System.out.println(String.format("Selected Date and Time Slot: %s, %s", selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), selectedTime.toString()));
                }
            }
        });

        updatePagination();
        setUp(); // Added due to testing purpose
    }

    public void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.setResizable(false);
        alert.showAndWait();
    }

    public Optional<ButtonType> showConfirmationDialog(String message) {
        var alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(message);

        ButtonType yesBtn = new ButtonType("Yes", ButtonData.YES);
        ButtonType cancelBtn = new ButtonType(
                "Cancel", ButtonData.CANCEL_CLOSE
        );

        alert.getButtonTypes().setAll(yesBtn, cancelBtn);
        return alert.showAndWait();
    }

    private LocalDate workingDate() {
        LocalDate workingDate = LocalDate.now();
        while (workingDate.getDayOfWeek() == DayOfWeek.SATURDAY || workingDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            workingDate.plusDays(1);
        }
        return workingDate;
    }

    private void updatePagination() {
        ListInterface<TimeInterval> allTimeInterval = InterviewScheduler.showAllSlots();
        int itemsPerPage = 9;
        int totalPages = (int) Math.ceil((double) allTimeInterval.getNumberOfEntries() / itemsPerPage);

        interviewSchedulerPagination.setPageCount(Math.max(totalPages, 1));
        interviewSchedulerPagination.setPageFactory(pageIndex -> createGridPaneForPage(pageIndex));
    }

    private VBox createGridPaneForPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        toggleGroup.getToggles().clear();
        ListInterface<TimeInterval> allTimeInterval = InterviewScheduler.showAllSlots();
        InterviewScheduler tempInterviewScheduler = null;
        try {
            tempInterviewScheduler = company.getInterviewManager().getParticularDaySchedule(selectedDate);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        ListInterface<Interval<LocalTime>> bookedTimeInterval;
        if (tempInterviewScheduler != null) {
            bookedTimeInterval = company.getInterviewManager().getParticularDaySchedule(selectedDate).showBookedSlots();
        } else {
            bookedTimeInterval = new ArrayList<>();
        }
        int itemsPerPage = 9;
        int start = pageIndex * itemsPerPage;
        boolean choseBtn = false;
        for (int i = 0; i < itemsPerPage && (start + i) < allTimeInterval.getNumberOfEntries(); i++) {
            TimeInterval node = allTimeInterval.getEntry(start + i);
            ToggleButton button = new ToggleButton(node.start.format(DateTimeFormatter.ofPattern("hh:mm a")));
            button.setUserData(node);
            button.setToggleGroup(toggleGroup);
            Interval<LocalTime> tempInterval = new Interval(node.start, node.end);
            for (var x : bookedTimeInterval) {
                if (tempInterval.overlaps(x)) {
                    button.setDisable(true);
                    break;
                }
            }
            if (!button.isDisabled() && !choseBtn) {
                button.setSelected(true);
                choseBtn = true;
            }

            int col = i % 3;
            int row = i / 3;
            gridPane.add(button, col, row);
        }
        HBox tempHBox = new HBox();
        VBox tempVBox = new VBox();
        VBox.setVgrow(tempVBox, Priority.ALWAYS);
        HBox.setHgrow(gridPane, Priority.NEVER);
        tempHBox.setAlignment(Pos.CENTER);
        tempVBox.setAlignment(Pos.CENTER);
        tempHBox.getChildren().add(gridPane);
        tempVBox.getChildren().add(tempHBox);

        return tempVBox;
    }

    public void setUp() {
        findCompany(this.internPost);
        jobtitleTextLabel.setText(this.internPost.getTitle().toUpperCase() + "'s Interview Slot");
    }

    public void setInternPost(InternPost internPost) {
        this.internPost = internPost;
        setUp();
    }

    public void findCompany(InternPost internPost) {
        for (var x : MainControlClass.getCompanies()) {
            if (x.getInternPosts().contains(internPost)) {
                this.company = x;
                break;
            }
        }
    }

    public LocalDate getSelectedDate() {
        return this.selectedDate;
    }

    public TimeInterval getSelectedTime() {
        return this.selectedTime;
    }
}
