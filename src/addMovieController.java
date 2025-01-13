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
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;

/**
 * Controller class for adding a new movie to the application.
 * This class manages the user interface for adding movie details and saving them to the database.
 */
public class AddMovieController {
    /**
     * Movie object to store from scene and upload to database
     */
    public static Movie movie = new Movie();

    /**
     * role label for user
     */
    @FXML
    private Label roleLabel;

    /**
     * name surname label of the user
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Summary of movie will be given here
     */
    @FXML
    private TextArea summaryTextArea;

    /**
     * title of the movie will be taken from here
     */
    @FXML
    private TextField titleTextField;

    /**
     * adding movies will be on this button
     */
    @FXML
    private Button applyChangesButton;

    /**
     * uploading image from local machine will be
     */
    @FXML
    private Button uploadImageButton;

    /**
     * imageView to show the uploaded image
     */
    @FXML
    private ImageView imageView;

    /**
     * logout button to return to login page
     */
    @FXML
    private Button logout;
}
    /**
     * Handles the logout button click event to navigate to the login screen.
     * 
     * @param event Mouse event triggered when the logout button is clicked.
     * @throws Exception if there is an issue loading the login screen FXML file.
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    /**
     * Handles the logout button key press event to navigate to the login screen.
     * 
     * @param event Key event triggered when the logout button is pressed.
     * @throws Exception if there is an issue loading the login screen FXML file.
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }

    /**
     * Handles the back button click event to navigate to the Admin Main page.
     * 
     * @param event Mouse event triggered when the back button is clicked.
     * @throws Exception if there is an issue loading the Admin Main page FXML file.
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    /**
     * Handles the back button key press event to navigate to the Admin Main page.
     * 
     * @param event Key event triggered when the back button is pressed.
     * @throws Exception if there is an issue loading the Admin Main page FXML file.
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }


/**
 * This is the initializer for the addMovieController.
 * Loads the necessary objects in the FXML file.
 * Adds the genres to the choice box and sets the default value.
 * Adds a listener to the choice box to update the movie genre.
 */
@FXML
public void initialize() {
    nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
    roleLabel.setText(LoginController.loggedEmployee.getUserRole());
    genreChoiceBox.getItems().addAll("Sci-Fi", "Animation", "Horror", "Drama", "Love", "Fantasy");
    genreChoiceBox.setValue("Select Genre"); // Set a default value.

    genreChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            movie.setGenre(newValue); // Update the movie genre.
        }
    });
}

/**
 * Handles action triggered by a mouse click or the Enter key to load another page.
 *
 * @param mouseEvent the mouse click event.
 * @param keyEvent the key press event.
 * @param fxmlPath the path to the FXML file to load.
 * @throws Exception if an error occurs during scene loading.
 */
public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
    if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
        App.loadScene(fxmlPath);
    } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
        App.loadScene(fxmlPath);
    }
}

/**
 * Handles the upload button press event to upload an image and set it for the movie.
 *
 * @param event the mouse click event.
 * @throws Exception if an error occurs during the operation.
 */
@FXML
public void uploadImagePressed(MouseEvent event) throws Exception {
    handleChooseImageButtonAction();
}

/**
 * Handles the apply changes button press event to validate inputs and add the movie to the database.
 *
 * @param event the key press event.
 * @throws Exception if an error occurs during the operation.
 */
@FXML
public void applyChangesPressed(KeyEvent event) throws Exception {
    if (movie.getTitle() != null && movie.getSummary() != null && movie.getGenre() != null && movie.getPoster() != null) {
        if (!movie.getTitle().isBlank() && !movie.getSummary().isBlank() && !movie.getGenre().equals("Select Genre") && movie.getPoster().length != 0) {
            handleChanges(movie);
            showAlert("Success", "Movie added successfully");
        } else {
            showAlert("Error", "Please fill all the fields");
        }
    } else {
        showAlert("Error", "Please fill all the fields");
    }
}

/**
 * Handles the apply changes button click event to validate inputs and add the movie to the database.
 *
 * @param event the mouse click event.
 * @throws Exception if an error occurs during the operation.
 */
@FXML
public void applyChangesClicked(MouseEvent event) throws Exception {
    if (movie.getTitle() != null && movie.getSummary() != null && movie.getGenre() != null && movie.getPoster() != null) {
        if (!movie.getTitle().isBlank() && !movie.getSummary().isBlank() && !movie.getGenre().equals("Select Genre") && movie.getPoster().length != 0) {
            handleChanges(movie);
            showAlert("Success", "Movie added successfully");
        } else {
            showAlert("Error", "Please fill all the fields");
        }
    } else {
        showAlert("Error", "Please fill all the fields");
    }
}

/**
 * Displays an alert message for warnings or success notifications.
 *
 * @param title the title of the alert.
 * @param message the content of the alert.
 */
public static void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

/**
 * Adds the movie to the database when changes are made.
 *
 * @param movie the movie object to be saved to the database.
 * @throws Exception if an error occurs during database interaction.
 */
public static void handleChanges(Movie movie) throws Exception {
    DatabaseConnection.addMovie(movie);
}

/**
 * Updates the movie's title based on input in the title text field.
 *
 * @param event the key press event.
 * @throws Exception if an error occurs during the operation.
 */
@FXML
public void titleTextFieldTyped(KeyEvent event) throws Exception {
    movie.setTitle(titleTextField.getText());
}

/**
 * Updates the movie's summary based on input in the summary text area.
 *
 * @param event the key press event.
 * @throws Exception if an error occurs during the operation.
 */
@FXML
public void summaryTextAreaTyped(KeyEvent event) throws Exception {
    movie.setSummary(summaryTextArea.getText());
}

/**
 * Handles the upload image button click to select and upload an image.
 */
@FXML
public void uploadImageClicked() {
    handleChooseImageButtonAction();
}

/**
 * Handles the selection of an image file for upload, sets it to the image view, and updates the movie poster.
 */
@FXML
public void handleChooseImageButtonAction() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select an Image File");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
    );
    File selectedFile = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());
    if (selectedFile != null) {
        Image image = new Image(selectedFile.toURI().toString());
        imageView.setImage(image);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(imageView.getParent().getLayoutBounds().getWidth());
        imageView.setFitHeight(imageView.getParent().getLayoutBounds().getHeight());
        try {
            movie.setPoster(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    @FXML
    private ChoiceBox<String> genreChoiceBox;

 /**
 * Retrieves the currently selected genre from the choice box.
 *
 * @return the selected genre.
 */
public String getSelectedValue() {
    return genreChoiceBox.getValue();
}
