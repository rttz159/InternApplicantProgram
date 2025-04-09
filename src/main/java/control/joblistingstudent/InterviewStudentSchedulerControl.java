package control.joblistingstudent;

import adt.ArrayList;
import adt.ListInterface;
import adt.interval.Interval;
import adt.interval.TimeInterval;
import static boundary.PredefinedDialog.showConfirmationDialog;
import static boundary.PredefinedDialog.showErrorDialog;
import boundary.joblistingstudent.ApplicationSharedState;
import boundary.joblistingstudent.InterviewStudentSchedulerController;
import control.InterviewScheduler;
import dao.CompanyDAO;
import dao.MainControlClass;
import dao.StudentDAO;
import entity.Application;
import entity.Company;
import entity.InternPost;
import entity.Interview;
import entity.Student;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.builders.ApplicationBuilder;
import utils.builders.InterviewBuilder;

/**
 *
 * @author Raymond
 */
public class InterviewStudentSchedulerControl {

    private InternPost internPost;
    private Company company;
    private LocalDate selectedDate;
    private TimeInterval selectedTime;
    private Interview interview;

    private InterviewStudentSchedulerController boundary;
    
    public InterviewStudentSchedulerControl(InterviewStudentSchedulerController boundary, InternPost internpost){
        this.boundary = boundary;
        this.internPost = internpost;
    }
    
    private LocalDate workingDate() {
        LocalDate workingDate = LocalDate.now().plusDays(1);
        while (workingDate.getDayOfWeek() == DayOfWeek.SATURDAY || workingDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            workingDate = workingDate.plusDays(1);
        }
        return workingDate;
    }

    private void updatePagination() {
        ListInterface<TimeInterval> allTimeInterval = InterviewScheduler.showAllSlots();
        int itemsPerPage = 9;
        int totalPages = (int) Math.ceil((double) allTimeInterval.getNumberOfEntries() / itemsPerPage);

        boundary.getInterviewSchedulerPagination().setPageCount(Math.max(totalPages, 1));
        boundary.getInterviewSchedulerPagination().setPageFactory(pageIndex -> createGridPaneForPage(pageIndex));
    }

    private VBox createGridPaneForPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        boundary.getToggleGroup().getToggles().clear();
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
            button.setToggleGroup(boundary.getToggleGroup());
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
        boundary.getJobtitleTextLabel().setText(this.internPost.getTitle().toUpperCase() + "'s Interview Slot");
        selectedDate = workingDate();

        boundary.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                selectedTime = (TimeInterval) selectedButton.getUserData();
            } else {
                selectedTime = null;
            }

        });

        boundary.getInterviewSchedulerDatePicker().setValue(selectedDate);

        boundary.getInterviewSchedulerDatePicker().setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now()) || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                }
            }
        });

        boundary.getInterviewSchedulerDatePicker().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                this.selectedDate = newValue;
                updatePagination();
            }
        });

        boundary.getCancelBtn().setOnAction(eh -> {
            ((Stage) boundary.getCancelBtn().getScene().getWindow()).close();
        });

        boundary.getConfirmBtn().setOnAction(eh -> {
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
                if (result.get().getButtonData() == ButtonBar.ButtonData.YES) {
                    Student tempStudent = (Student) MainControlClass.getCurrentUser();
                    Interview tempInterview = new InterviewBuilder()
                            .date(selectedDate)
                            .startTime(selectedTime.start)
                            .build();
                    Application tempApp = new ApplicationBuilder()
                            .internPostId(internPost.getInterPostId())
                            .applicantId(tempStudent.getUserId())
                            .interview(tempInterview)
                            .status(Application.Status.PENDING)
                            .build();
                    tempStudent.getStudentApplications().append(tempApp);
                    company.getInterviewManager().interviewBooking(selectedDate, selectedTime.start);
                    internPost.getInternPostApplications().append(tempApp);
                    MainControlClass.getStudentApplicationMap().put(tempApp.getApplicationId(), tempApp);

                    StudentDAO.updateStudentById(tempStudent);
                    CompanyDAO.updateCompanyById(company);

                    System.out.println(String.format("Selected Date and Time Slot: %s, %s", selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), selectedTime.toString()));
                    ApplicationSharedState.getInstance().setApplied(true);
                    ((Stage) boundary.getCancelBtn().getScene().getWindow()).close();
                }
            }
        });

        updatePagination();
    }

    private void findCompany(InternPost internPost) {
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
