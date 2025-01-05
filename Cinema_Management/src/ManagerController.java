import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

public class ManagerController {

    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button economyButton;

    @FXML
    private Button employeeButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button logout;

    @FXML
    private Button productButton;

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
    void economyClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerAccountingScene.fxml");
    }

    @FXML
    void economyPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerAccountingScene.fxml");
    }

    @FXML
    void employeeClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "EmployeeListScene.fxml");
    }

    @FXML
    void employeePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "EmployeeListScene.fxml");
    }

    @FXML
    void inventoryClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerInventoryScene.fxml");
    }

    @FXML
    void inventoryPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerInventoryScene.fxml");
    }

    @FXML
    void productClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerProductScene.fxml");
    }

    @FXML
    void productPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerProductScene.fxml");
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
