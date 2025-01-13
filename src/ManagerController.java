import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

/**
 * Manager main menu scene controller class
 */
public class ManagerController {

    /**
     * role label
     */
    @FXML
    private Label roleLabel;

    /**
     * name surname label
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * accounting button
     */
    @FXML
    private Button economyButton;

    /**
     * employee operations button
     */
    @FXML
    private Button employeeButton;

    /**
     * stocks button
     */
    @FXML
    private Button inventoryButton;

    /**
     * logout button
     */
    @FXML
    private Button logout;

    /**
     * prices button
     */
    @FXML
    private Button productButton;

    /**
     * to set the name surname and role at top
     */
    @FXML
    public void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
    }

    /**
     * to handle actions
     * @param mouseEvent mouse event
     * @param keyEvent keyboard event
     * @param fxmlPath loaded scene
     * @throws Exception for load scene
     */
    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); // Mouse left click
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); // Enter key
        }
    }

    /**
     * accounting button click action
     * @param event evnt
     * @throws Exception for load scene
     */
    @FXML
    void economyClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerAccountingScene.fxml");
    }

    /**
     * accounting button entered 
     * @param event event
     * @throws Exception for load scene
     */
    @FXML
    void economyPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerAccountingScene.fxml");
    }

    /**
     * employee button click action
     * @param event evnt
     * @throws Exception for load scene
     */
    @FXML
    void employeeClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "EmployeeListScene.fxml");
    }

    /**
     * employee button entered 
     * @param event event
     * @throws Exception for load scene
     */
    @FXML
    void employeePressed(KeyEvent event) throws Exception {
        handleAction(null, event, "EmployeeListScene.fxml");
    }

    /**
     * stocks button click action
     * @param event evnt
     * @throws Exception for load scene
     */
    @FXML
    void inventoryClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerInventoryScene.fxml");
    }

    /**
     * stocks button entered 
     * @param event event
     * @throws Exception for load scene
     */
    @FXML
    void inventoryPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerInventoryScene.fxml");
    }

    /**
     * product button click action
     * @param event evnt
     * @throws Exception for load scene
     */
    @FXML
    void productClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "ManagerProductScene.fxml");
    }

    /**
     * prices button entered 
     * @param event event
     * @throws Exception for load scene
     */
    @FXML
    void productPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "ManagerProductScene.fxml");
    }


    /**
     * logout button click action
     * @param event evnt
     * @throws Exception for load scene
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    /**
     * logout button entered 
     * @param event event
     * @throws Exception for load scene
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }

}
