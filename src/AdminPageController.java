import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

/**
 * Controller class for the admin page.
 */
public class AdminPageController {
    
    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button addMovieButton;

    @FXML
    private Button updateMovieButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Button logout;

    @FXML
    private Button refundsButton;

    @FXML
    private Button backButton;

    /**
     * Initializes the controller class.
     * Sets the name and role labels based on the logged-in employee.
     * @return void
     */
    @FXML
    public void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
    }

/**
     * Common method for handling button actions via MouseEvent or KeyEvent.
     * 
     * @param mouseEvent The mouse event.
     * @param keyEvent The key event.
     * @param fxmlPath The path to the FXML file to load.
     * @throws Exception If an error occurs while loading the scene.
     */    
    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); // Mouse left click
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); // Enter key
        }
    }

    /**
     * Handles the add movie button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void addMovieClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "AddMovie.fxml");
    }

    /**
     * Handles the add movie button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void addMoviePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "AddMovie.fxml");
    }

    /**
     * Handles the back button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * Handles the back button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }

    /**
     * Handles the update movie button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void upMovieClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "UpdateMovieScene.fxml");
    }

    /**
     * Handles the update movie button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void upMoviePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "UpdateMovieScene.fxml");
    }

    /**
     * Handles the schedule button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void scheduleClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ScheduleScene.fxml");
    }

    /**
     * Handles the schedule button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void schedulePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ScheduleScene.fxml");
    }

    /**
     * Handles the refunds button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void refundsClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "refundsScene.fxml");
    }

    /**
     * Handles the refunds button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void refundsPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "refundsScene.fxml");
    }

    /**
     * Handles the logout button click event.
     * 
     * @param event The mouse event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    /**
     * Handles the logout button key press event.
     * 
     * @param event The key event.
     * @throws Exception If an error occurs while loading the scene.
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }



}
