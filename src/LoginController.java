import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller class for the login page
 */
public class LoginController
{
    /**
     * Employee object that holds the data of the logged in user
     */
    public static Employee loggedEmployee;

    /**
     * Vbox
     */
    @FXML
    private VBox loginContainer;

    /**
     * Label to show error messages
     */
    @FXML
    private Label errorMessage;

    /**
     * login button
     */
    @FXML
    private Button loginButton;

    /**
     * password entry field
     */
    @FXML
    private PasswordField password;

    /**
     * username entry field
     */
    @FXML
    private TextField username;

    /**
     * event handler method for login button
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    void logicClicked(MouseEvent event) throws Exception 
    {
        if (event.getButton().equals(MouseButton.PRIMARY))
            handleLogin();
    }

    /**
     * event handler method for enter key pressed action
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void enterKeyPressed(KeyEvent event) throws Exception
    {
        Object eventCode = event.getCode();
        if (eventCode.equals(KeyCode.ENTER))
            handleLogin();
        else if (eventCode.equals(KeyCode.ESCAPE))
        {
            Stage stage = (Stage) loginContainer.getScene().getWindow();  // Use loginContainer as reference to get the stage
            stage.close();
        }
    }

    /**
     * Condiiton checker method
     * @throws Exception
     */
    public void handleLogin() throws Exception
    {
        // check in the database
        String uname = this.username.getText();
        String pass = this.password.getText();

        if (uname.isEmpty() && pass.isEmpty())
        {
            this.errorMessage.setText("Username and Password cannot be empty");
            resetTextFields();
            return;
        }
        else if (uname.isEmpty())
        {
            this.errorMessage.setText("Username cannot be empty");
            resetTextFields();
            return;
        }
        else if (pass.isEmpty())
        {
            this.errorMessage.setText("Password cannot be empty");
            resetTextFields();
            return;
        }

        // burda database'e gönder
        
        loggedEmployee = DatabaseConnection.authenticate(uname, pass);
        // if it is unsuccessful login, error message and clear the input fields
        if (loggedEmployee == null)
        {
            this.errorMessage.setText("INVALID Username or Password");
            resetTextFields();
        }
        else // if it is a successful login, OK message and direct to the new scene
        {
            switch (loggedEmployee.getUserRole())
            {
                case "manager":
                    App.loadScene("ManagerMainMenu.fxml");
                    break;
                case "admin":
                    App.loadScene("AdminMainPage.fxml");
                    break;
                case "cashier":
                    App.loadScene("cashierScene1.fxml");
                    break;
            }
        }
    }

    /**
     * To clear the input fields
     */
    private void resetTextFields()
    {
        this.password.clear();
        this.username.clear();
    }
}
