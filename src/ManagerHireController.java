import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ManagerHireController {

    @FXML
    private Button backButton;

    @FXML
    private Button logout;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Label roleLabel;

    


    @FXML
    private TextField surnameInput;

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField nameInput;

    @FXML
    private Button addButton;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> roleInput;


    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label surnameErrorLabel;

    @FXML
    private Label usernameErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "ManagerMainMenu.fxml");
    }

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "ManagerMainMenu.fxml");
    }

    @FXML
    void logoutClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    @FXML
    void logoutPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }




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

    private String capitalizeFirstLetter(String input)
    {
        if (input == null || input.isEmpty())
            return input;
        
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

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
