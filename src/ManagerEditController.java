import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for manager's edit scene
 */
public class ManagerEditController
{
    // top of the screen
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
     * name surname label
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * role label
     */
    @FXML
    private Label roleLabel;

    

    // TextFields for the inputs
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
     * role input field
     */
    @FXML
    private TextField roleInput;



    /**
     * update button
     */
    @FXML
    private Button updateButton;

    /**
     * name error label
     */
    @FXML
    private Label nameErrorLabel;

    /**
     * surname error label
     */
    @FXML
    private Label surnameErrorLabel;

    /**
     * username error label
     */
    @FXML
    private Label usernameErrorLabel;

    /**
     * password error label
     */
    @FXML
    private Label passwordErrorLabel;

    /////////////////////////////////////////////////////

    /**
     * radio button for name
     */
    @FXML
    private RadioButton editNameSelection;

    /**
     * radio button for password
     */
    @FXML
    private RadioButton editPasswordSelection;

    /**
     * radio button for surname
     */
    @FXML
    private RadioButton editSurnameSelection;

    /**
     * radio button for username
     */
    @FXML
    private RadioButton editUsernameSelection;

    /**
     * radio button for role
     */
    @FXML
    private RadioButton editRoleSelection;

    ///////////////////////////////////////////////////////

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
     * current name of the selected user
     */
    private String curName;

    /**
     * current surname of the selected user
     */
    private String curSurname;

    /**
     * current username of the selected user
     */
    private String curUsername;

    /**
     * current password of the selected user
     */
    private String curPassword;

    /**
     * current role of the selected user
     */
    private String curRole;

    /**
     * Initialize method to setup the scene, bind the selectionss
     */
    @FXML
    private void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // get the selected user's data
        curName = ManagerEmployeeController.edittedEmployee.getFirstname();
        curSurname = ManagerEmployeeController.edittedEmployee.getLastname();
        curUsername = ManagerEmployeeController.edittedEmployee.getUsername();
        curPassword = ManagerEmployeeController.edittedEmployee.getPassword();
        curRole = ManagerEmployeeController.edittedEmployee.getUserRole();



        // Set default values with the current employee data
        nameInput.setText(curName);
        surnameInput.setText(curSurname);
        usernameInput.setText(curUsername);
        passwordInput.setText(curPassword);
        roleInput.setText(curRole);

        // Set the error lables empty
        nameErrorLabel.setText("");
        surnameErrorLabel.setText("");
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");


        // Bind the editable property of the text fields to the selected property of the radio buttons
        nameInput.editableProperty().bind(editNameSelection.selectedProperty());
        surnameInput.editableProperty().bind(editSurnameSelection.selectedProperty());
        usernameInput.editableProperty().bind(editUsernameSelection.selectedProperty());
        passwordInput.editableProperty().bind(editPasswordSelection.selectedProperty());
        roleInput.editableProperty().bind(editRoleSelection.selectedProperty());


        // Bind the update button's disable property to the state of all radio buttons
        // So if all of them are not (indicating they are not selected), the updateButton
        // is disabled
        updateButton.disableProperty().bind(
            editNameSelection.selectedProperty().not()
            .and(editSurnameSelection.selectedProperty().not())
            .and(editUsernameSelection.selectedProperty().not())
            .and(editPasswordSelection.selectedProperty().not())
            .and(editRoleSelection.selectedProperty().not())
        );
    }
    
    /**
     * To switch role
     */
    @FXML
    void handleRoleSelection()
    {
        if (editRoleSelection.isSelected())
            roleInput.setText(curRole.equals("admin") ? "cashier" : "admin");
        else
            roleInput.setText(curRole);
    }

    /**
     * update button clicked event handler
     * Checks the conditions
     * @param event mounse vent
     * @throws Exception for load scene
     */
    @FXML
    private void updateClicked(MouseEvent event) throws Exception
    {
        // Determine which field is being updated
        if (editNameSelection.isSelected())
        {
            String newName = nameInput.getText();
            newName = capitalizeFirstLetter(newName);
            if (checkName(newName))
            {
                curName = newName;
                ManagerEmployeeController.edittedEmployee.setFirstname(newName);
            }
            else
                return; // Stop if validation fails
        }

        if (editSurnameSelection.isSelected())
        {
            String newSurname = surnameInput.getText();
            newSurname = capitalizeFirstLetter(newSurname);
            if (checkSurname(newSurname))
            {
                curSurname = newSurname;
                ManagerEmployeeController.edittedEmployee.setLastname(newSurname);
            }
            else
                return; // Stop if validation fails
        }

        if (editUsernameSelection.isSelected())
        {
            String newUsername = usernameInput.getText();
            if (checkUsername(newUsername))
            {
                curUsername = newUsername;
                ManagerEmployeeController.edittedEmployee.setUsername(newUsername);
            }
            else
                return; // Stop if validation fails
        }

        if (editPasswordSelection.isSelected())
        {
            String newPassword = passwordInput.getText();
            if (checkPassword(newPassword))
            {
                curPassword = newPassword;
                ManagerEmployeeController.edittedEmployee.setPassword(newPassword);
            }
            else
                return; // Stop if validation fails
        }

        if (editRoleSelection.isSelected())
        {
            ManagerEmployeeController.edittedEmployee.setUserRole(roleInput.getText());
        }

        // Send the updated employee object to the database
        boolean isUpdated = DatabaseConnection.updateEmployee(ManagerEmployeeController.edittedEmployee);
        if (isUpdated)
        {
            System.out.println("Employee updated successfully.");
            App.loadScene("EmployeeListScene.fxml"); // Redirect back to the employee list
        }
        else
        {
            System.err.println("Failed to update employee.");
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
            passwordInput.setText(ManagerEmployeeController.edittedEmployee.getPassword());
            return false;
        }
        if (password.contains(" "))
        {
            passwordErrorLabel.setText("Password cannot contain spaces.");
            passwordInput.setText(ManagerEmployeeController.edittedEmployee.getPassword());
            return false;
        }
        if (!password.matches(".*[A-Z].*"))
        {
            passwordErrorLabel.setText("Password must contain capital letter.");
            passwordInput.setText(ManagerEmployeeController.edittedEmployee.getPassword());
            return false;
        }
        if (password.length() < 8)
        {
            passwordErrorLabel.setText("Password must be at least 8 characters.");
            passwordInput.setText(ManagerEmployeeController.edittedEmployee.getPassword());
            return false;
        }
        if (password.equals(curPassword))
        {
            passwordErrorLabel.setText("New password cannot be the same.");
            passwordInput.setText(ManagerEmployeeController.edittedEmployee.getPassword());
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
            usernameInput.setText(ManagerEmployeeController.edittedEmployee.getUsername());
            return false;
        }
        if (username.contains(" "))
        {
            usernameErrorLabel.setText("Username cannot contain spaces.");
            usernameInput.setText(ManagerEmployeeController.edittedEmployee.getUsername());
            return false;
        }
        if (username.equals(curUsername))
        {
            usernameErrorLabel.setText("New username cannot be the same.");
            usernameInput.setText(ManagerEmployeeController.edittedEmployee.getUsername());
            return false;
        }
        if (DatabaseConnection.isValueInColumnOfTable("users", "username", username))
        {
            usernameErrorLabel.setText("Username is already in use.");
            usernameInput.setText(ManagerEmployeeController.edittedEmployee.getUsername());
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
            surnameInput.setText(ManagerEmployeeController.edittedEmployee.getLastname());
            return false;
        }
        if (!surname.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+"))
        {
            surnameErrorLabel.setText("Surname must contain only letters.");
            surnameInput.setText(ManagerEmployeeController.edittedEmployee.getLastname());
            return false;
        }
        if (surname.equals(curSurname))
        {
            surnameErrorLabel.setText("New surname cannot be the same.");
            surnameInput.setText(ManagerEmployeeController.edittedEmployee.getLastname());
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
            nameInput.setText(ManagerEmployeeController.edittedEmployee.getFirstname());;
            return false;
        }
        if (!name.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+"))
        {
            nameErrorLabel.setText("Name must contain only letters.");
            nameInput.setText(ManagerEmployeeController.edittedEmployee.getFirstname());;
            return false;
        }
        if(name.equals(curName))
        {
            nameErrorLabel.setText("New name cannot be the same.");
            nameInput.setText(ManagerEmployeeController.edittedEmployee.getFirstname());;
            return false;
        }
        nameErrorLabel.setText(""); // Clear the error if valid
        return true;
    }
}
