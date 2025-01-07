import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
        // Handle OK button click
        okButton.setOnAction(event -> {
            try {
                // Get age and name input
                age = Integer.parseInt(ageField.getText().trim());
                name = nameField.getText().trim();

                if (age < 0) {
                    System.out.println("Age cannot be negative!");
                    return;
                }

                confirmed = true; // Mark as confirmed
                System.out.println("Name: " + name + ", Age: " + age);

                // Close the stage
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException e) {
                System.out.println("Invalid age entered!");
            }
        });
    }

    // Getters for retrieving the inputs
    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public boolean isConfirmed() {
        return confirmed;
    }


    @FXML
    void okClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;
        
        String ageStr = ageField.getText().trim();
        this.name = nameField.getText().trim();

        if(this.name.isEmpty())
        {
            errorText.setText("Enter your name");
            return;
        }
        if(ageStr.isEmpty())
        {
            errorText.setText("Enter an age");
            return;
        }
        
        
        try
        {
            this.age = Integer.parseInt(ageStr);

            if (age < 1 || age > 110)
            {
                errorText.setText("Enter a valid age");
                return;
            }
        }
        catch (NumberFormatException e)
        {
            errorText.setText("Age must be a number");
            return;
        }

        if (age < 0 || age > 110)
        {
            errorText.setText("Enter valid age");
            return;
        }

        confirmed = true; // Mark as confirmed
        System.out.println("Name: " + name + ", Age: " + age);

        // Close the stage
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

}
