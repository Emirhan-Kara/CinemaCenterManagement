import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for hiring option
 */
public class ManagerHireController
{

    /**
     * back button
     */
    @FXML
    private Button backButton;

    /**
     * logout button
     */
    @FXML
    private Button logout;

    /**
     * name surname label at top
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * role label at top
     */
    @FXML
    private Label roleLabel;

    

    /**
     * surname input field
     */
    @FXML
    private TextField surnameInput;

    /**
     * username input field
     */
    @FXML
    private TextField usernameInput;

    /**
     * password input field
     */
    @FXML
    private TextField passwordInput;

    /**
     * name input field
     */
    @FXML
    private TextField nameInput;

    /**
     * add button
     */
    @FXML
    private Button addButton;

    /**
     * error label
     */
    @FXML
    private Label errorLabel;

    /**
     * choice box for role
     */
    @FXML
    private ChoiceBox<String> roleInput;


    /**
     * name error field
     */
    @FXML
    private Label nameErrorLabel;

    /**
     * surname error field
     */
    @FXML
    private Label surnameErrorLabel;

    /**
     * username error field
     */
    @FXML
    private Label usernameErrorLabel;

    /**
     * password error field
     */
    @FXML
    private Label passwordErrorLabel;

    /**
     * event handler for back button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "ManagerMainMenu.fxml");
    }

    /**
     * event handler for back button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "ManagerMainMenu.fxml");
    }

    /**
     * event handler for logout button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * event handler for logout button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }
  

    /**
     * Initilaizer to setup the scene 
     */
    @FXML
    private void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Set fixed choices for the roleInput ChoiceBox
        roleInput.setItems(FXCollections.observableArrayList("admin", "cashier"));

        // Set a default value
        roleInput.setValue("cashier");

        // Set the error lables empty
        nameErrorLabel.setText("");
        surnameErrorLabel.setText("");
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }


    /**
     * Handles after the employee add button is clicked. Controls the username, validates the inputs
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    void addClicked(MouseEvent event) throws Exception
    {
        // Fetch input from text fields
        String name = nameInput.getText();
        String surname = surnameInput.getText();
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        String role = roleInput.getValue();

        // capitalize the first letters of the name and the surname
        name = capitalizeFirstLetter(name);
        surname = capitalizeFirstLetter(surname);

        // control the constrains
        boolean isNameValid = checkName(name);
        boolean isSurnameValid = checkSurname(surname);
        boolean isUsernameValid = checkUsername(username);
        boolean isPasswordValid = checkPassword(password);

        // if all the related information is valid, add the employee
        if (isNameValid && isSurnameValid && isUsernameValid && isPasswordValid)
        {
            Employee newEmployee = new Employee(0, role, username, name, surname, password);
            if(DatabaseConnection.hireEmployee(newEmployee))
                System.out.println("employee successfuly hired");
            else
                System.err.println("newEmployee cannot added to the db");
            
            App.loadScene("EmployeeListScene.fxml");
        }
    }

    /**
     * Capitalizes the first letter of the name and surname
     * @param input name or surname
     * @return first letter capitalized string
     */
    private String capitalizeFirstLetter(String input)
    {
        if (input == null || input.isEmpty())
            return input;
        
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Control cases for passwords
     * @param password password to be controlled
     * @return true if the conditions satisfied, else false
     */
    private boolean checkPassword(String password)
    {
        if (password == null || password.trim().isEmpty())
        {
            passwordErrorLabel.setText("Password cannot be empty.");
            passwordInput.clear();
            return false;
        }
        if (password.contains(" "))
        {
            passwordErrorLabel.setText("Password cannot contain spaces.");
            passwordInput.clear();
            return false;
        }
        if (!password.matches(".*[A-Z].*"))
        {
            passwordErrorLabel.setText("Password must contain capital letter.");
            passwordInput.clear();
            return false;
        }
        if (password.length() < 8)
        {
            passwordErrorLabel.setText("Password must be at least 8 characters.");
            passwordInput.clear();
            return false;
        }
        passwordErrorLabel.setText(""); // Clear the error if valid
        return true;
    }

    /**
     * Control cases for username
     * @param username username to be controlled
     * @return true if the conditions satisfied, else false
     */
    private boolean checkUsername(String username)
    {
        if (username == null || username.trim().isEmpty())
        {
            usernameErrorLabel.setText("Username cannot be empty.");
            usernameInput.clear();
            return false;
        }
        if (username.contains(" "))
        {
            usernameErrorLabel.setText("Username cannot contain spaces.");
            usernameInput.clear();
            return false;
        }
        if (DatabaseConnection.isValueInColumnOfTable("users", "username", username))
        {
            usernameErrorLabel.setText("Username is already in use.");
            usernameInput.clear();
            return false;
        }
        usernameErrorLabel.setText(""); // Clear the error if valid
        return true;
    }

    /**
     * Control cases for surname
     * @param surname surname to be controlled
     * @return true if the conditions satisfied, else false
     */
    private boolean checkSurname(String surname)
    {
        if (surname == null || surname.trim().isEmpty())
        {
            surnameErrorLabel.setText("Surname cannot be empty.");
            surnameInput.clear();
            return false;
        }
        if (!surname.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+"))
        {
            surnameErrorLabel.setText("Surname must contain only letters.");
            surnameInput.clear();
            return false;
        }
        surnameErrorLabel.setText(""); // Clear the error if valid
        return true;
    }

    /**
     * Control cases for name
     * @param name name to be controlled
     * @return true if the conditions satisfied, else false
     */
    private boolean checkName(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            nameErrorLabel.setText("Name cannot be empty.");
            nameInput.clear();
            return false;
        }
        if (!name.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+"))
        {
            nameErrorLabel.setText("Name must contain only letters.");
            nameInput.clear();
            return false;
        }
        nameErrorLabel.setText(""); // Clear the error if valid
        return true;
    }
}
