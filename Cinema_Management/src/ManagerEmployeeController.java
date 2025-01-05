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

public class ManagerEmployeeController
{
    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> idCol;

    @FXML
    private TableColumn<Employee, String> roleCol;

    @FXML
    private TableColumn<Employee, String> usernameCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TableColumn<Employee, String> surnameCol;

    @FXML
    private TableColumn<Employee, String> passwordCol;

    @FXML
    private Button backButton;

    @FXML
    private Button logout;

    @FXML
    private Button fireButton;

    @FXML
    private Button editButton;

    @FXML
    private Button hireButton;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Label roleLabel;

    public static Employee edittedEmployee;    

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

    @FXML
    private void hireButtonClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "HireEmployeeScene.fxml");
    }
    // hire ve edit eklencek

    @FXML
    private void editButtonClicked(MouseEvent event) throws Exception
    {
        // Get the selected employee
        edittedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        ManagerController.handleAction(event, null, "ManagerEditScene.fxml");

    }
}
