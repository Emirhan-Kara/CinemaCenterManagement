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

public class TakeNameAndAgeController {
    @FXML
    private Label errorText;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private Button okButton;

    private int age; // Store the inputted age
    private String name; // Store the inputted name
    private boolean confirmed = false; // Flag to check if input was confirmed

    @FXML
    void initialize()
    {
        errorText.setText("");
    }


    public boolean isConfirmed() {
        return this.confirmed;
    }
    public int getAge()
    {
        return this.age;
    }
    public String getName()
    {
        return this.name;
    }


    private boolean checkConditions()
    {
        String ageStr = ageField.getText().trim();
        this.name = nameField.getText().trim();

        if(this.name.isEmpty())
        {
            errorText.setText("Enter your name");
            return false;
        }
        if(ageStr.isEmpty())
        {
            errorText.setText("Enter an age");
            return false;
        }
        
        
        try
        {
            this.age = Integer.parseInt(ageStr);

            if (age < 1 || age > 110)
            {
                errorText.setText("Enter a valid age");
                ageField.clear();
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            errorText.setText("Age must be a number");
            ageField.clear();
            return false;
        }

        if (!this.name.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+"))
        {
            errorText.setText("Name must contain only letters.");
            nameField.clear();
            return false;
        }

        System.out.println("Name: " + name + ", Age: " + age);
        return true; // Mark as confirmed
    }

    @FXML
    private GridPane gridPane;
    @FXML
    void enterClicked(KeyEvent event)
    {
        if (event.getCode() != KeyCode.ENTER)
            return;
        
            this.confirmed = checkConditions();

        if(this.confirmed)
        {
            // Close the stage
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void okClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        
        this.confirmed = checkConditions();

        if(this.confirmed)
        {
            // Close the stage
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }
}
