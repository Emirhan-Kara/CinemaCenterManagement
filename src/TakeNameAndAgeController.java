import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Controller for handling user input for name and age in a dialog window.
 */
public class TakeNameAndAgeController {

    /**
     * Label for displaying error messages.
     */
    @FXML
    private Label errorText;

    /**
     * Text field for entering the user's name.
     */
    @FXML
    private TextField nameField;

    /**
     * Text field for entering the user's age.
     */
    @FXML
    private TextField ageField;

    /**
     * Button to confirm the input.
     */
    @FXML
    private Button okButton;

    /**
     * Root grid pane for the dialog layout.
     */
    @FXML
    private GridPane gridPane;

    /**
     * Stores the inputted age.
     */
    private int age;

    /**
     * Stores the inputted name.
     */
    private String name;

    /**
     * Indicates whether the input has been confirmed.
     */
    private boolean confirmed = false;

    /**
     * Initializes the controller and sets the error label to an empty state.
     */
    @FXML
    void initialize() {
        errorText.setText("");
    }

    /**
     * Checks whether the input has been confirmed.
     *
     * @return true if the input is confirmed, false otherwise.
     */
    public boolean isConfirmed() {
        return this.confirmed;
    }

    /**
     * Retrieves the user's inputted age.
     *
     * @return the age entered by the user.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Retrieves the user's inputted name.
     *
     * @return the name entered by the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Validates the user's input for name and age.
     *
     * @return true if the conditions are met and input is valid, false otherwise.
     */
    private boolean checkConditions() {
        String ageStr = ageField.getText().trim();
        this.name = nameField.getText().trim();

        if (this.name.isEmpty()) {
            errorText.setText("Enter your name");
            return false;
        }
        if (ageStr.isEmpty()) {
            errorText.setText("Enter an age");
            return false;
        }

        try {
            this.age = Integer.parseInt(ageStr);

            if (age < 1 || age > 110) {
                errorText.setText("Enter a valid age");
                ageField.clear();
                return false;
            }
        } catch (NumberFormatException e) {
            errorText.setText("Age must be a number");
            ageField.clear();
            return false;
        }

        if (!this.name.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+")) {
            errorText.setText("Name must contain only letters.");
            nameField.clear();
            return false;
        }

        System.out.println("Name: " + name + ", Age: " + age);
        return true; // Input is valid
    }

    /**
     * Handles the Enter key event to validate input and close the dialog if valid.
     *
     * @param event the key event triggered by pressing Enter.
     */
    @FXML
    void enterClicked(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER)
            return;

        this.confirmed = checkConditions();

        if (this.confirmed) {
            // Close the stage
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Handles the mouse click event on the OK button to validate input and close the dialog if valid.
     *
     *
