import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for manager's employee control scene
 */
public class ManagerEmployeeController
{
    /**
     * table that lists the employees
     */
    @FXML
    private TableView<Employee> employeeTable;

    /**
     * column of the employee table that shows the user id
     */
    @FXML
    private TableColumn<Employee, String> idCol;

    /**
     * column of the employee table that shows the user role
     */
    @FXML
    private TableColumn<Employee, String> roleCol;

    /**
     * column of the employee table that shows the username
     */
    @FXML
    private TableColumn<Employee, String> usernameCol;

    /**
     * column of the employee table that shows the user name
     */
    @FXML
    private TableColumn<Employee, String> nameCol;

    /**
     * column of the employee table that shows the user surname
     */
    @FXML
    private TableColumn<Employee, String> surnameCol;

    /**
     * column of the employee table that shows the user password
     */
    @FXML
    private TableColumn<Employee, String> passwordCol;

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
     * fire button to delete the employee
     */
    @FXML
    private Button fireButton;

    /**
     * edit button to edit the employee data
     */
    @FXML
    private Button editButton;

    /**
     * hire button
     */
    @FXML
    private Button hireButton;

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
     * selected employee to be editted
     */
    public static Employee edittedEmployee;    

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
     * Initilazer method that sets up the scene, fills the table
     */
    @FXML
    private void initialize()
    {
        this.nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        this.roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Configure columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Fetch data and populate TableView
        List<Employee> employees = DatabaseConnection.listEmployees();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
        employeeTable.setItems(employeeList);

        // Bind the visibility of the fireButton and editButton to table selection 
        fireButton.visibleProperty().bind(employeeTable.getSelectionModel().selectedItemProperty().isNotNull());
        editButton.visibleProperty().bind(employeeTable.getSelectionModel().selectedItemProperty().isNotNull());
    }

    /**
     * Event handler for fire button
     * @param event mouse event
     */
    @FXML
    private void fireButtonClicked(MouseEvent event)
    {
        // Get the selected employee
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee != null)
        {
    
            if (DatabaseConnection.deleteEmployee(selectedEmployee.getId()))
            {
                // Remove the employee from the TableView
                employeeTable.getItems().remove(selectedEmployee);
                employeeTable.getSelectionModel().clearSelection(); // Clear the selection
            }
            else
            {
                System.out.println("Failed to delete the employee from the database.");
            }
        }
    }

    /**
     * Event handler for hire button
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    private void hireButtonClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "HireEmployeeScene.fxml");
    }
    
    /**
     * Event handler for edit button
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    private void editButtonClicked(MouseEvent event) throws Exception
    {
        // Get the selected employee
        edittedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        ManagerController.handleAction(event, null, "ManagerEditScene.fxml");

    }
}
