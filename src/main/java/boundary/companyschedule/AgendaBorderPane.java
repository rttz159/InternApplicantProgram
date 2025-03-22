package boundary.companyschedule;

import adt.MapInterface;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.rttz.assignment.App;
import dao.MainControlClass;
import entity.Application;
import entity.Company;
import entity.InternPost;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Raymond
 */
public class AgendaBorderPane extends BorderPane {

    private MapInterface<String, Application> applications = MainControlClass.getStudentApplicationMap();
    private CalendarView calendarView;
    private Calendar interviewCalendar;

    public AgendaBorderPane() {
        this.setWidth(USE_PREF_SIZE);
        this.setHeight(USE_PREF_SIZE);
        this.getStylesheets().add(App.class.getResource("css/internpostDetails.css").toExternalForm());

        calendarView = new CalendarView();
        calendarView.setShowAddCalendarButton(false);
        calendarView.setEntryFactory(param -> null);
        interviewCalendar = new Calendar("Interviews");
        interviewCalendar.setReadOnly(true);
        CalendarSource calendarSource = new CalendarSource("My Calendars");
        calendarSource.getCalendars().add(interviewCalendar);
        calendarView.getCalendarSources().add(calendarSource);
        calendarView.showAddCalendarButtonProperty().set(false);
        calendarView.getDayPage().getAgendaView().getStylesheets().add(App.class.getResource("css/overwriteListview.css").toExternalForm());

        loadAppointments();

        Button prevButton = new Button("← Previous");
        prevButton.getStyleClass().add("custom_button");
        Button nextButton = new Button("Next →");
        nextButton.getStyleClass().add("custom_button");

        prevButton.setOnAction(e -> calendarView.goBack());
        nextButton.setOnAction(e -> calendarView.goForward());

        ComboBox<String> viewSelector = new ComboBox<>();
        viewSelector.getItems().addAll("Day View", "Week View", "Month View");
        viewSelector.setValue("Day View");
        viewSelector.setOnAction(e -> switchView(viewSelector.getValue()));

        HBox controls = new HBox(10, prevButton, viewSelector, nextButton);
        this.setTop(controls);
        this.setCenter(calendarView);
    }

    private void switchView(String viewName) {
        switch (viewName) {
            case "Day View":
                calendarView.showDayPage();
                break;
            case "Week View":
                calendarView.showWeekPage();
                break;
            case "Month View":
                calendarView.showYearPage();
                break;
        }
    }

    private void loadAppointments() {
        Company tempCompany = (Company)MainControlClass.getCurrentUser();
        if (applications.values() != null && !applications.values().isEmpty()) {
            for (Application app : applications.values()) {
                InternPost tempJob = MainControlClass.getInternPostMap().get(app.getInternPostId());
                if (tempCompany.getInternPosts().contains(tempJob) && app.getInterview() != null && app.getStatus().equals(Application.Status.PENDING)) {
                    LocalDate interviewDate = app.getInterview().getDate();
                    LocalDateTime startTime = LocalDateTime.of(interviewDate, app.getInterview().getStart_time());
                    LocalDateTime endTime = startTime.plusMinutes(30);

                    Entry<String> entry = new Entry<>("Interviewee: " + MainControlClass.getStudentsIdMap().get(app.getApplicantId()).getName() + " [Job Title: " + tempJob.getTitle() + "]");
                    entry.setInterval(startTime, endTime);
                    entry.setLocation(tempJob.getLocation().getState() + ", " + tempJob.getLocation().getFullAddress());
                    interviewCalendar.addEntry(entry);
                }
            }
        }
    }
}
