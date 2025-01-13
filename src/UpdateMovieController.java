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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * Controller class for managing the "Update Movie" functionality in the application.
 * This class handles interactions with the user interface and performs actions
 * such as updating movie details, reverting changes, and managing movie data.
 */
public class UpdateMovieController {

    /**
     * Represents the movie currently being edited.
     */
    public static Movie movie = new Movie();

    /**
     * Represents the original state of the movie before any changes.
     */
    public static Movie oldMovie = new Movie();

    /**
     * Button to delete the selected movie.
     */
    @FXML
    private Button deleteMovieButton;

    /**
     * Label to display a message after deleting a movie.
     */
    @FXML
    private Label deleteMovieLabel;

    /**
     * Label to indicate the need to select a movie.
     */
    @FXML
    private Label selectLabel;

    /**
     * Label to display confirmation messages for selections.
     */
    @FXML
    private Label selectLabel1;

    /**
     * Label to display an error if no movie is selected.
     */
    @FXML
    private Label selectLabel2;

   /**
     * Grid pane for displaying movie covers or related UI components.
     */
    @FXML
    private GridPane coverGrid;

    /**
     * ChoiceBox for selecting a movie from a list.
     */
    @FXML
    private ChoiceBox<String> moviePicker;

    /**
     * Button to manage movie genres.
     */
    @FXML
    private Button genreButton;

    /**
     * Label to indicate that some fields are empty.
     */
    @FXML
    private Label emptyFieldsLabel;

    /**
     * Label to indicate that no changes were made to the movie.
     */
    @FXML
    private Label noChangeLabel;

    /**
     * ImageView for displaying the movie's poster.
     */
    @FXML
    private ImageView imageView;

    /**
     * TextField for editing the movie's title.
     */
    @FXML
    private TextField titleTextField;

    /**
     * TextArea for editing the movie's summary.
     */
    @FXML
    private TextArea summaryTextArea;

    /**
     * ChoiceBox for selecting the genre of the movie.
     */
    @FXML
    private ChoiceBox<String> genreChoiceBox;

    /**
     * Button to apply changes to the movie's title.
     */
    @FXML
    private Button titleButton;

    /**
     * Button to apply changes to the movie's summary.
     */
    @FXML
    private Button summaryButton;

    /**
     * Button to manage changes to the movie's poster.
     */
    @FXML
    private Button posterButton;

    /**
     * Button to revert changes made to the movie details.
     */
    @FXML
    private Button revertChangesButton;

    /**
     * Label to display the logged-in user's role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Label to display the logged-in user's full name.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Button to apply changes made to the movie details.
     */
    @FXML
    private Button applyChangesButton;

    /**
     * Button to upload a new image for the movie's poster.
     */
    @FXML
    private Button uploadImageButton;

    /**
     * Button to log out of the application.
     */
    @FXML
    private Button logout;
}

   /**
 * Handles the action when the back button is clicked to load the AdminMainPage.fxml file.
 * 
 * @param event the mouse event triggered by clicking the back button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void backClicked(MouseEvent event) throws Exception {
    ManagerController.handleAction(event, null, "AdminMainPage.fxml");
}

/**
 * Handles the action when the back button is pressed to load the AdminMainPage.fxml file.
 * 
 * @param event the key event triggered by pressing the back button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void backPressed(KeyEvent event) throws Exception {
    ManagerController.handleAction(null, event, "AdminMainPage.fxml");
}

/**
 * Handles the action when an overlaying button is clicked to simulate loading 
 * the upload movie fields by removing the button from the scene.
 * 
 * @param event the mouse event triggered by clicking the button
 * @throws Exception if an error occurs during the UI update
 */
@FXML
void disappear(MouseEvent event) throws Exception {
    Button clickedButton = (Button) event.getSource();
    clickedButton.setVisible(false);
    ((Pane) clickedButton.getParent()).getChildren().remove(clickedButton);
}

/**
 * Handles the action when the revert changes button is clicked.
 * Reloads the scene to revert any changes made to the movie object and frontend components.
 * 
 * @param event the mouse event triggered by clicking the revert changes button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void revertChangesClicked(MouseEvent event) throws Exception {
    ManagerController.handleAction(event, null, "UpdateMovieScene.fxml");
}

/**
 * Handles the action when the revert changes button is pressed.
 * Reloads the scene to revert any changes made to the movie object and frontend components.
 * 
 * @param event the key event triggered by pressing the revert changes button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void revertChangesPressed(KeyEvent event) throws Exception {
    ManagerController.handleAction(null, event, "UpdateMovieScene.fxml");
}

/**
 * Handles the action when the delete movie button is pressed.
 * Deletes the selected movie from the database and alerts the user.
 * 
 * @param event the key event triggered by pressing the delete movie button
 * @throws Exception if an error occurs during the movie deletion process
 */
@FXML
public void deleteMoviePressed(KeyEvent event) throws Exception {
    if (moviePicker.getValue().equals("Select Movie")) {
        labelVisibility(selectLabel2);
        return;
    } else {
        selectLabel2.setVisible(false);
    }

    DatabaseConnection.deleteMovie_byID(movie.getId());
    labelVisibility(deleteMovieLabel);
    emptyMovieFields(movie);
}

    /**
 * Handles the action when the delete movie button is clicked.
 * Deletes the selected movie from the database and alerts the user.
 * 
 * @param event the mouse event triggered by clicking the delete movie button
 * @throws Exception if an error occurs during the movie deletion process
 */
@FXML
public void deleteMovieClicked(MouseEvent event) throws Exception {
    if (moviePicker.getValue().equals("Select Movie")) {
        labelVisibility(selectLabel2);
        return;
    } else {
        selectLabel.setVisible(false);
    }
    if (DatabaseConnection.getMovie_byID(movie.getId()) == null) {
        labelVisibility(selectLabel2);
        return;
    }
    DatabaseConnection.deleteMovie_byID(movie.getId());
    labelVisibility(deleteMovieLabel);
    emptyMovieFields(movie);
}

/**
 * Empties the fields of the provided movie object.
 * 
 * @param movie the Movie object whose fields are to be emptied
 */
public void emptyMovieFields(Movie movie) {
    imageView.setImage(null);
    titleTextField.setText("");
    summaryTextArea.setText("");
    genreChoiceBox.setValue("Select Genre");
}

/**
 * Handles the action when the apply changes button is pressed.
 * Validates inputs, updates the movie object in the database, and alerts the user.
 * 
 * @param event the key event triggered by pressing the apply changes button
 * @throws Exception if an error occurs during the update process
 */
@FXML
public void applyChangesPressed(KeyEvent event) throws Exception {
    if (moviePicker.getValue().equals("Select Movie") || imageView == null || titleTextField == null || 
        summaryTextArea == null || titleTextField.getText().isEmpty() || 
        summaryTextArea.getText().isEmpty() || genreChoiceBox.getValue().equals("Select Genre")) {
        labelVisibility(emptyFieldsLabel);
        return;
    } else {
        emptyFieldsLabel.setVisible(false);
    }
    if (oldMovie.getTitle().equals(movie.getTitle()) &&
        oldMovie.getGenre().equals(movie.getGenre()) &&
        oldMovie.getSummary().equals(movie.getSummary()) &&
        Arrays.equals(oldMovie.getPoster(), movie.getPoster())) {
        labelVisibility(noChangeLabel);
        return;
    } else {
        noChangeLabel.setVisible(false);
    }
    DatabaseConnection.updateMovie(movie);
    labelVisibility(selectLabel1);
}

/**
 * Handles the action when the apply changes button is clicked.
 * Validates inputs, updates the movie object in the database, and alerts the user.
 * 
 * @param event the mouse event triggered by clicking the apply changes button
 * @throws Exception if an error occurs during the update process
 */
@FXML
public void applyChangesClicked(MouseEvent event) throws Exception {
    if (moviePicker.getValue().equals("Select Movie")) {
        labelVisibility(selectLabel);
        return;
    } else {
        selectLabel.setVisible(false);
    }
    if (imageView == null || titleTextField == null || summaryTextArea == null || 
        titleTextField.getText().isEmpty() || summaryTextArea.getText().isEmpty() || 
        genreChoiceBox.getValue().equals("Select Genre")) {
        labelVisibility(emptyFieldsLabel);
        return;
    } else {
        emptyFieldsLabel.setVisible(false);
    }
    if (DatabaseConnection.getMovie_byID(movie.getId()) == null) {
        labelVisibility(selectLabel);
        return;
    }
    if (oldMovie.getTitle().equals(movie.getTitle()) &&
        oldMovie.getGenre().equals(movie.getGenre()) &&
        oldMovie.getSummary().equals(movie.getSummary()) &&
        Arrays.equals(oldMovie.getPoster(), movie.getPoster())) {
        labelVisibility(noChangeLabel);
        return;
    } else {
        noChangeLabel.setVisible(false);
    }
    DatabaseConnection.updateMovie(movie);
    labelVisibility(selectLabel1);
    oldMovie = new Movie(movie);
}

/**
 * Handles the action when the logout button is clicked.
 * Loads the login page.
 * 
 * @param event the mouse event triggered by clicking the logout button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void logoutClicked(MouseEvent event) throws Exception {
    ManagerController.handleAction(event, null, "Login.fxml");
}

/**
 * Handles the action when the logout button is pressed.
 * Loads the login page.
 * 
 * @param event the key event triggered by pressing the logout button
 * @throws Exception if an error occurs during the scene transition
 */
@FXML
void logoutPressed(KeyEvent event) throws Exception {
    handleAction(null, event, "Login.fxml");
}

/**
 * Updates the visibility of labels to ensure only the specified label is visible.
 * 
 * @param label the label to make visible
 */
public void labelVisibility(Label label) {
    deleteMovieLabel.setVisible(false);
    selectLabel.setVisible(false);
    selectLabel1.setVisible(false);
    selectLabel2.setVisible(false);
    emptyFieldsLabel.setVisible(false);
    noChangeLabel.setVisible(false);
    label.setVisible(true);
}

/**
 * Initializes the controller when the FXML file is loaded.
 * Populates the movie picker with titles, initializes the genre choice box, 
 * and sets up listeners for changes in movie and genre selection.
 */
@FXML
public void initialize() {
    List<Movie> movieList = DatabaseConnection.getAllMovies();
    nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
    roleLabel.setText(LoginController.loggedEmployee.getUserRole());
    moviePicker.setValue("Select Movie");
    for (Movie movie : movieList) {
        moviePicker.getItems().add(movie.getTitle());
    }
    genreChoiceBox.getItems().addAll("Sci-Fi", "Animation", "Horror", "Drama", "Love", "Fantasy");
    genreChoiceBox.setValue("Select Genre");
    genreChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> movie.setGenre(newValue));
    moviePicker.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        for (Movie m : movieList) {
            if (m.getTitle().equals(newValue)) {
                movie = m;
                genreChoiceBox.setValue(movie.getGenre());
                imageView.setImage(new Image(new ByteArrayInputStream(movie.getPoster())));
                titleTextField.setText(movie.getTitle());
                summaryTextArea.setText(movie.getSummary());
                oldMovie = new Movie(movie);
            }
        }
    });
}

/**
 * Loads a specified FXML file based on the triggered event.
 * 
 * @param mouseEvent the last mouse event
 * @param keyEvent the last key event
 * @param fxmlPath the path to the FXML file
 * @throws Exception if the FXML file is not found
 */
public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
    if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
        App.loadScene(fxmlPath);
    } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
        App.loadScene(fxmlPath);
    }
}

/**
 * Handles the action when the upload image button is pressed.
 * 
 * @param event the mouse event triggered by pressing the upload image button
 * @throws Exception if an error occurs during the image upload process
 */
@FXML
public void uploadImagePressed(MouseEvent event) throws Exception {
    handleChooseImageButtonAction();
}

/**
 * Updates the movie title when the title text field is edited.
 * 
 * @param event the key event triggered by typing in the title text field
 * @throws Exception if an error occurs
 */
@FXML
public void titleTextFieldTyped(KeyEvent event) throws Exception {
    movie.setTitle(titleTextField.getText());
}

/**
 * Updates the movie summary when the summary text area is edited.
 * 
 * @param event the key event triggered by typing in the summary text area
 * @throws Exception if an error occurs
 */
@FXML
public void summaryTextAreaTyped(KeyEvent event) throws Exception {
    movie.setSummary(summaryTextArea.getText());
}

   /**
 * Handles the action when the upload image button is clicked.
 * Calls the method to open the file chooser and load the selected image.
 */
@FXML
public void uploadImageClicked() {
    handleChooseImageButtonAction();
}

/**
 * Handles the action when the choose image button is clicked.
 * Opens a file chooser to allow the user to select an image file from the file system,
 * loads the selected image, and displays it in the ImageView.
 * Updates the movie's poster with the selected image.
 */
@FXML
public void handleChooseImageButtonAction() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select an Image File");

    // Set extension filters to accept only image files
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
    );

    // Show the FileChooser and get the selected file
    File selectedFile = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());

    if (selectedFile != null) {
        try {
            // Read the selected file as a byte array
            byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
            Image image = new Image(new ByteArrayInputStream(imageBytes));

            // Set the image in the ImageView and update its layout
            imageView.setImage(image);
            imageView.setPreserveRatio(false);
            imageView.setFitWidth(imageView.getParent().getLayoutBounds().getWidth());
            imageView.setFitHeight(imageView.getParent().getLayoutBounds().getHeight());

            // Update the movie's poster with the selected image bytes
            movie.setPoster(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Retrieves the selected value from the movie picker or genre choice box.
 * If a movie is selected in the picker, its value is returned. 
 * Otherwise, the selected genre value is returned.
 * 
 * @return the selected movie title or genre
 */
public String getSelectedValue() {
    String pickMovieValue = moviePicker.getValue();
    String genreValue = genreChoiceBox.getValue();

    if (pickMovieValue != null && !pickMovieValue.equals("Select Genre")) {
        return pickMovieValue;
    } else {
        return genreValue;
    }
}
