import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.MapValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller class for editing schedules
 */
public class ScheduleEdit
{

    /**
     * Currently selected session.
     */
    Sessions currentSession = new Sessions();

    /**
     * Currently selected movie.
     */
    Movie currentMovie = new Movie();

    /**
     * Button to update a session.
     */
    @FXML
    private Button updateSession;

    /**
     * Button to delete a session.
     */
    @FXML
    private Button deleteSession;

    /**
     * Button to apply changes to the schedule.
     */
    @FXML
    private Button applyChangesSchedule;

    /**
     * ImageView for displaying the movie poster.
     */
    @FXML
    private ImageView moviePoster;

    /**
     * Label to display the schedule list.
     */
    @FXML
    private Label scheduleList;

    /**
     * Table column for movie names in sessions.
     */
    @FXML
    private TableColumn<Sessions, String> movieColumn;

    /**
     * Table column for session dates.
     */
    @FXML
    private TableColumn<Sessions, String> dateColumn;

    /**
     * Table column for hall information in sessions.
     */
    @FXML
    private TableColumn<Sessions, String> hallColumn;

    /**
     * Table column for session times.
     */
    @FXML
    private TableColumn<Sessions, String> timeColumn;

    /**
     * Button to navigate back to the previous screen.
     */
    @FXML
    private Button backButton;

    /**
     * ComboBox for selecting a schedule date.
     */
    @FXML
    private ComboBox<String> dateSchedule;

    /**
     * ComboBox for selecting a hall for scheduling.
     */
    @FXML
    private ComboBox<String> hallSchedule;

    /**
     * Button to log out the user.
     */
    @FXML
    private Button logout;

    /**
     * ComboBox for selecting a movie for scheduling.
     */
    @FXML
    private ComboBox<String> movieSchedule;

    /**
     * Label to display user's name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Label to display the title of the current movie.
     */
    @FXML
    private Label movieTitle;

    /**
     * Label to display user's role.
     */
    @FXML
    private Label roleLabel;

    /**
     * TableView to display the schedule.
     */
    @FXML
    private TableView<Schedule> scheduleTable;

    /**
     * ComboBox for selecting a time for scheduling.
     */
    @FXML
    private ComboBox<String> timeSchedule;

    /**
     * List of all sessions.
     */
    protected List<Sessions> sessionList;

    /**
     * List of schedule entries for the table.
     */
    protected List<Schedule> scheduleTableList = new ArrayList<Schedule>();

    /**
     * Currently selected schedule entry.
     */
    private Schedule selectedSchedule;


    /**
     * Data structure to hold schedules and track locked sessions
     * Title to boolean mapping
     */
    private final Map<String, Boolean> scheduleLock = new HashMap<>();


    /**
     * Initializes the ScheduleEdit page with the sessions from the database.
     * Populates the combo boxes with the unique values from the sessions.
     * Populates the table view with the sessions from the database.
     * Sets the cell value factories for the table view.
     * Sets the on click event for the table view to update the movie side.
     * 
     */
    @FXML
    public void initialize() {

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("movie"));
        // Fetch session data from the database
        sessionList = DatabaseConnection.getSessions();
                    // Observable lists for combo boxes
        
        ObservableList<Date> dates = FXCollections.observableArrayList();
        ObservableList<String> movies1 = FXCollections.observableArrayList(); // Changed to String for movie titles
        ObservableList<Date> dates1 = FXCollections.observableArrayList();
        ObservableList<Integer> halls1 = FXCollections.observableArrayList();
        ObservableList<Time> times1 = FXCollections.observableArrayList();
        // TODO: Fetch movie details by ID and add the movie title to the search list
        // Populate the lists by extracting unique values from sessionList
        for(Sessions session : sessionList){
            movies1.add(String.valueOf(DatabaseConnection.getMovie_byID(session.getMovieId()).getTitle()));
            dates1.add(session.getDate());
            halls1.add(session.getHallId());
            times1.add(session.getTime());
        }
        // Set items for the combo boxes
        List<Movie> moviesList = DatabaseConnection.getAllMovies();
        ObservableList<String> movieTitles = FXCollections.observableArrayList();
        for (Movie movie : moviesList) {
            movieTitles.add(movie.getTitle());
        }
        movieSchedule.setItems(movieTitles);
        dates.add(new Date(System.currentTimeMillis()));
        ObservableList<String> dateStrings = FXCollections.observableArrayList();
        Date date = dates.get(0);
        for (int i = 0; i < 30; i++) {
            dateStrings.add(date.toString());
            date = new Date(date.getTime() + 86400000); // Add one day in milliseconds
        }
        dateSchedule.setItems(dateStrings);
    
        ObservableList<String> hallStrings = FXCollections.observableArrayList();
        for (Integer hall : DatabaseConnection.getHalls()) {
            hallStrings.add(hall.toString());
        }
        hallSchedule.setItems(hallStrings);
    
        ObservableList<String> timeStrings = FXCollections.observableArrayList();
        for (int hour = 12; hour < 24; hour += 2) {
            timeStrings.add(String.format("%02d:00:00", hour));
        }
        timeSchedule.setItems(timeStrings);

        ObservableList<String> dateString1 = FXCollections.observableArrayList();
        for(Date date1 : dates1){
            dateString1.add(date1.toString());
        }
        List<Date> dateList = new ArrayList<Date>();
        for(int i = 0; i < dates1.size(); i++){
            String movieName = movies1.get(i);
            dateList.add(Date.valueOf(dateString1.get(i)));
            Time time = times1.get(i);
            Integer hallId = halls1.get(i);
            Schedule schedule = new Schedule(hallId, movieName, dateList.get(i), time, sessionList.get(i).getId());
            scheduleTableList.add(schedule);
        } 
        scheduleTable.setItems(FXCollections.observableArrayList(scheduleTableList));

        scheduleTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Single click
                int selectedIndex = scheduleTable.getSelectionModel().getSelectedIndex();
                if(selectedIndex < 0){
                    System.out.println("No proper selection.");
                    return;
                }
                selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
                selectedSchedule.setId(sessionList.get(selectedIndex).getId());
                
                if (selectedSchedule != null) {
                    currentMovie = DatabaseConnection.getMovie_byTitle(selectedSchedule.getMovie());
                    updateMovieSide();
                }
                updateComboBoxes(selectedSchedule);
            }
        });
    }

    /**
     * Updates the combo boxes with the values from the selected schedule
     * @param schedule selected schedule object 
     */
    void updateComboBoxes(Schedule schedule) {
        movieSchedule.setValue(schedule.getMovie());
        dateSchedule.setValue(schedule.getDate().toString());
        timeSchedule.setValue(schedule.getTime().toString());
        hallSchedule.setValue(String.valueOf(schedule.getHallId()));
    }

    /**
     * Updates the schedule table with the sessions from the database
     * and populates the scheduleTableList with the sessions from the database
     */
    void updateScheduleTable(){
        sessionList = DatabaseConnection.getSessions();
        scheduleTableList.clear();
        ObservableList<String> movies1 = FXCollections.observableArrayList(); // Changed to String for movie titles
        ObservableList<Date> dates1 = FXCollections.observableArrayList();
        ObservableList<Integer> halls1 = FXCollections.observableArrayList();
        ObservableList<Time> times1 = FXCollections.observableArrayList();
        for(Sessions session : sessionList){
            movies1.add(String.valueOf(DatabaseConnection.getMovie_byID(session.getMovieId()).getTitle()));
            dates1.add(session.getDate());
            halls1.add(session.getHallId());
            times1.add(session.getTime());
        }
        ObservableList<String> dateString1 = FXCollections.observableArrayList();
        for(Date date1 : dates1){
            dateString1.add(date1.toString());
        }
        List<Date> dateList = new ArrayList<Date>();
        for(int i = 0; i < dates1.size(); i++){
            String movieName = movies1.get(i);
            dateList.add(Date.valueOf(dateString1.get(i)));
            Time time = times1.get(i);
            Integer hallId = halls1.get(i);
            Schedule schedule = new Schedule(hallId, movieName, dateList.get(i), time, sessionList.get(i).getId());
            scheduleTableList.add(schedule);
        } 
        scheduleTable.setItems(FXCollections.observableArrayList(scheduleTableList));
    }
    
    /**
     * Updates the movie elements with the selected movie's title and poster
     * when the session chosen have been changed
     */
    void updateMovieSide(){
        if(currentMovie == null){
            return;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(currentMovie.getPoster());
        Image image = new Image(inputStream);
        movieTitle.setText(currentMovie.getTitle());
        moviePoster.setImage(image);
    }

    
    /**
     * Adds a new session to the database with the values from the combo boxes.
     * Checks for incomplete selection, if not shows an alert.
     * Gives failed to add session alert if there is conflict.
     * updates the tableview to show the newly added session
     * 
     * @param event mouse event
     */
    @FXML
    void applyChangesClicked(MouseEvent event) {
        String selectedMovie = movieSchedule.getValue();
        String selectedDate = dateSchedule.getValue();
        String selectedTime = timeSchedule.getValue();
        String selectedHall = hallSchedule.getValue();
    
        if (selectedMovie == null || selectedDate == null || selectedTime == null || selectedHall == null) {
            showAlert("Incomplete Selection", "Please select a movie, date, time, and hall.");
            return;
        }
        if (selectedMovie == null || selectedDate == null || selectedTime == null || selectedHall == null) {
            showAlert("Incomplete Selection", "Please select a movie, date, session, and hall.");
            return;
        }
        currentSession.setMovieId(Integer.valueOf(DatabaseConnection.getMovie_byTitle(selectedMovie).getId()));
        currentSession.setDate(Date.valueOf(selectedDate));
        currentSession.setTime(Time.valueOf(selectedTime));
        currentSession.setHallId(Integer.parseInt(selectedHall));
        // Check if the session is locked
        if(DatabaseConnection.addSession(currentSession)){
            sessionList = DatabaseConnection.getSessions();
            updateScheduleTable();
            showAlert("Success", "Schedule has been successfully added.");
            return;  
        }
        else{
            showAlert("Error", "Session is already reserved.");
            return;
        }
    }
    

    /**
     * Adds a new session to the database with the values from the combo boxes.
     * Checks for incomplete selection, if not shows an alert.
     * Gives failed to add session alert if there is conflict.
     * updates the tableview to show the newly added session
     * 
     * @param event keyboard event
     */
    @FXML
    void applyChangesPressed(KeyEvent event) {
        String selectedMovie = movieSchedule.getValue();
        String selectedDate = dateSchedule.getValue();
        String selectedTime = timeSchedule.getValue();
        String selectedHall = hallSchedule.getValue();
    
        if (selectedMovie == null || selectedDate == null || selectedTime == null || selectedHall == null) {
            showAlert("Incomplete Selection", "Please select a movie, date, session, and hall.");
            return;
        }

        currentSession.setMovieId(Integer.valueOf(DatabaseConnection.getMovie_byTitle(selectedMovie).getId()));
        currentSession.setDate(Date.valueOf(selectedDate));
        currentSession.setTime(Time.valueOf(selectedTime));
        currentSession.setHallId(Integer.parseInt(selectedHall));
        
            // TODO: Connect to database here for inserting session
            
            if(DatabaseConnection.addSession(currentSession)){
                sessionList = DatabaseConnection.getSessions();
                updateScheduleTable();
                showAlert("Success", "Schedule has been successfully added.");
                return;  
            }
            else{
                showAlert("Error", "Session is already reserved.");
                return;
            }
    }


    /**
     * Updates the selected session with the new values from the combo boxes.
     * Changes the schedule object in the tableview, and turns it into a new session object.
     * To get the id of the session, it gets the id from the sessionList.
     * Checks for a non-null selection, if not shows an alert.
     * Checks for incomplete selection, if not shows an alert.
     * Gives failed to update session alert if there is conflict.
     * @param event mouse event
     */
    @FXML
    void updateSessionClicked(MouseEvent event) {
        if (selectedSchedule == null) {
            showAlert("No Selection", "Please select a session to update.");
            return;
        }
    
        String selectedMovie = movieSchedule.getValue();
        String selectedDate = dateSchedule.getValue();
        String selectedTime = timeSchedule.getValue();
        String selectedHall = hallSchedule.getValue();
    
        if (selectedMovie == null || selectedDate == null  || selectedTime == null || selectedHall == null) {
            showAlert("Incomplete Selection", "Please select a movie, date, time, and hall.");
            return;
        }

        currentSession.setMovieId(Integer.valueOf(DatabaseConnection.getMovie_byTitle(selectedSchedule.getMovie()).getId()));
        currentSession.setDate(selectedSchedule.getDate());
        currentSession.setTime(selectedSchedule.getTime());
        currentSession.setHallId(selectedSchedule.getHallId());
        Sessions newSession = new Sessions();
        newSession.setDate(Date.valueOf(selectedDate));
        newSession.setTime(Time.valueOf(selectedTime));
        newSession.setHallId(Integer.valueOf(selectedHall));
    
        for(Sessions session : sessionList){
            if(session.getDate().equals(currentSession.getDate()) && session.getTime().equals(currentSession.getTime()) && session.getHallId() == currentSession.getHallId()) {
                newSession.setId(session.getId());
                newSession.setMovieId(DatabaseConnection.getMovie_byTitle(selectedMovie).getId());
            }
        }
        if(currentSession.getDate().equals(newSession.getDate()) && currentSession.getTime().equals(newSession.getTime()) && currentSession.getHallId() == newSession.getHallId() && currentSession.getMovieId() == newSession.getMovieId()){
            showAlert("No Change", "No changes have been made.");
            return;
        }
        if(currentSession == newSession){
            showAlert("No Change", "No changes have been made.");
            return;

        }
        if(DatabaseConnection.updateSession(currentSession, newSession)){
            sessionList = DatabaseConnection.getSessions();
            currentSession = newSession;
            selectedSchedule = null;
            updateScheduleTable();
            showAlert("Success", "Schedule has been successfully added.");
            return;  
        }
        else{
            showAlert("Error", "There was a conflict between sessions.");
            return;
        }
    }


    /**
     * Updates the selected session with the new values from the combo boxes.
     * Changes the schedule object in the tableview, and turns it into a new session object.
     * To get the id of the session, it gets the id from the sessionList.
     * Checks for a non-null selection, if not shows an alert.
     * Checks for incomplete selection, if not shows an alert.
     * Gives failed to update session alert if there is conflict.
     * @param event keyboard event
     */
    @FXML
    void updateSessionPressed(KeyEvent event) {
        if (selectedSchedule == null) {
            showAlert("No Selection", "Please select a session to update.");
            return;
        }
    
        String selectedMovie = movieSchedule.getValue();
        String selectedDate = dateSchedule.getValue();
        String selectedTime = timeSchedule.getValue();
        String selectedHall = hallSchedule.getValue();
    
        if (selectedMovie == null || selectedDate == null  || selectedTime == null || selectedHall == null) {
            showAlert("Incomplete Selection", "Please select a movie, date, time, and hall.");
            return;
        }

        currentSession.setMovieId(Integer.valueOf(DatabaseConnection.getMovie_byTitle(selectedSchedule.getMovie()).getId()));
        currentSession.setDate(selectedSchedule.getDate());
        currentSession.setTime(selectedSchedule.getTime());
        currentSession.setHallId(selectedSchedule.getHallId());
        Sessions newSession = new Sessions();
        newSession.setDate(Date.valueOf(selectedDate));
        newSession.setTime(Time.valueOf(selectedTime));
        newSession.setHallId(Integer.valueOf(selectedHall));
    
        for(Sessions session : sessionList){
            if(session.getDate() == newSession.getDate() || session.getTime() == newSession.getTime() || session.getHallId() == newSession.getHallId()){
                newSession.setId(session.getId());
                newSession.setMovieId(DatabaseConnection.getMovie_byTitle(selectedMovie).getId());
            }
        }
        if(DatabaseConnection.updateSession(currentSession, newSession)){
            sessionList = DatabaseConnection.getSessions();
            updateScheduleTable();
            showAlert("Success", "Schedule has been successfully added.");
            return;  
        }
        else{
            showAlert("Error", "Failed to update the session.");
            return;
        }
    }


    /**
     * Deletes the selected session from the database.
     * Checks if any session is selected, if not shows an alert.
     * @param event mouse event
     */
    @FXML
    void deleteSessionClicked(MouseEvent event) {
        //Sessions newSession =  new Schedule.getSession(selectedSchedule);//get from the list
        if (selectedSchedule == null) {
            showAlert("No Selection", "Please select a session to delete.");
            return;
        }
        if(DatabaseConnection.deleteSession(selectedSchedule.getId(), selectedSchedule.getHallId())){
            sessionList = DatabaseConnection.getSessions();
            updateScheduleTable();
            showAlert("Success", "Session has been successfully deleted.");
            return;
        }
        else{
            showAlert("Error", "Cannot delete the session due to booked seats.");
        }
    }


    /**
     * Deletes the selected session from the database.
     * Checks if any session is selected, if not shows an alert.
     * @param event keyboard event
     */
    @FXML
    void deleteSessionPressed(KeyEvent event) {
        //Sessions newSession =  new Schedule.getSession(selectedSchedule);//get from the list
        if (selectedSchedule == null) {
            showAlert("No Selection", "Please select a session to delete.");
            return;
        }

        if(DatabaseConnection.deleteSession(selectedSchedule.getId(), selectedSchedule.getHallId())){
            sessionList = DatabaseConnection.getSessions();
            updateScheduleTable();
            showAlert("Success", "Session has been successfully deleted.");
            return;
        }
        else{
            showAlert("Error", "Cannot delete the session due to booked seats.");
        }
    }

    
    /**
     * event handler for back button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }
    
    /**
     * event handler for back button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception{
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }
    
    /**
     * event handler for logout button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * event handler for logout button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "Login.fxml");
    }
    
    
    /**
     * Shows an alert with the given title and message.
     * @param title title
     * @param message message
     */ 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
