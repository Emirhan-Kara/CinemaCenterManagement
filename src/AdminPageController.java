import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

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

    @FXML
    public void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
    }

    // Common method for handling button actions via MouseEvent or KeyEvent
    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); // Mouse left click
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); // Enter key
        }
    }

    @FXML
    void addMovieClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "AddMovie.fxml");
    }

    @FXML
    void addMoviePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "AddMovie.fxml");
    }

    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }

    @FXML
    void upMovieClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "UpdateMovieScene.fxml");
    }

    @FXML
    void upMoviePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "UpdateMovieScene.fxml");
    }

    @FXML
    void scheduleClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ScheduleScene.fxml");
    }

    @FXML
    void schedulePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ScheduleScene.fxml");
    }

    @FXML
    void refundsClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "refundScene.fxml");
    }

    @FXML
    void refundsPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "refundScene.fxml");
    }

    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }



}
