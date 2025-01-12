import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;

public class addMovieController {

    public static Movie movie = new Movie();

    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private TextArea summaryTextArea;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button applyChangesButton;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Button logout;

    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }

    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }
    

    @FXML
    public void initialize() {

        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
        // Initialization code if needed
        // Add items to the ChoiceBox
        genreChoiceBox.getItems().addAll("Sci-Fi", "Animation", "Horror", "Comedy", "Love", "Fantasy", "Drama", "Action", "Adventure", "Crime", "Documentary", "Sports");

        // Set a default value (when no selection is made)
        genreChoiceBox.setValue("Select Genre"); // This shows as the current value initially

        genreChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                movie.setGenre(newValue); // Update the movie genre
            }
        });

        /* titleTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                titleTextFieldChanged(newValue);
            }
        });

        summaryTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                summaryTextAreaChanged(newValue);
            }
        }); */
    }

    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); // Mouse left click
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); // Enter key
        }
    }

    @FXML
    public void uploadImagePressed(MouseEvent event) throws Exception {
        // This method will be called when the upload button is clicked
        handleChooseImageButtonAction();
    }

    @FXML
    public void applyChangesPressed(KeyEvent event) throws Exception {
        // This method will be called when the apply changes button is clicked
        // Database apply addmovie
        handleChanges(movie);
        DatabaseConnection.updateMovie(movie);
        App.loadScene("AdminMainPage.fxml");
    }

    @FXML
    public void applyChangesClicked(MouseEvent event) throws Exception {
        // This method will be called when the apply changes button is clicked
        // Database apply addmovie
        handleChanges(movie);
        DatabaseConnection.updateMovie(movie);
        App.loadScene("AdminMainPage.fxml");
    }

    public static void handleChanges(Movie movie) throws Exception {
        // Handle the changes here
        //TODO
        DatabaseConnection.addMovie(movie);
    }
    @FXML
    public void titleTextFieldTyped(KeyEvent event) throws Exception {
        // This method will be called when typing in the title text field
        // Add your implementation here
        movie.setTitle(titleTextField.getText());
    }

    @FXML
    public void summaryTextAreaTyped(KeyEvent event) throws Exception {
        // This method will be called when the summary text area is typed
        movie.setSummary(summaryTextArea.getText());
    }

   /*  @FXML
    public void titleTextFieldChanged(String newValue) {
        // Handle the text change event
    }

    @FXML
    public void summaryTextAreaChanged(String newValue) {
        // Handle the text change event
    } */

    @FXML
    public void uploadImageClicked() {
        // This method will be called when the upload button is clicked
        handleChooseImageButtonAction();
    }

    @FXML
    public void handleChooseImageButtonAction() {
        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();

        // Set a title for the FileChooser
        fileChooser.setTitle("Select an Image File");

        // Set extension filters to accept only image files
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Show the FileChooser and get the selected file
        File selectedFile = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());

        if (selectedFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            
            imageView.setImage(image);
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(imageView.getParent().getLayoutBounds().getWidth());
            imageView.setFitHeight(imageView.getParent().getLayoutBounds().getHeight());
            try {
                movie.setPoster(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, e.g., show an error message to the user
            }
        }
    }

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    public String getSelectedValue() {
        // Get the selected value (returns null if no selection is made)
        return genreChoiceBox.getValue();
    }

}
