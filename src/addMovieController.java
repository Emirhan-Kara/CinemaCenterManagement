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

    /**
     * This method will be called when the logout button is clicked
     * To load the login screen
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        handleAction(event, null, "Login.fxml");
    }

    /**
     * This method will be called when the logout button is pressed
     * To load the login screen
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }

    /**
     * This method will be called when the back button is clicked
     * To load the Admin Main page
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    /**
     * This method will be called when the back button is pressed
     * To load the Admin Main page
     * 
     * @param event
     * @throws Exception
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }
    

    /**
     * This is the initializer for the addMovieController
     * Loads the necessary objects in the FXML file
     * Adds the genres to the choice box and sets the default value
     * Adds listener to the choice box to update the movie genre
     */
    @FXML
    public void initialize() {

        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
        // Initialization code if needed
        // Add items to the ChoiceBox
        genreChoiceBox.getItems().addAll("Sci-Fi", "Animation", "Horror", "Drama", "Love", "Fantasy");

        // Set a default value (when no selection is made)
        genreChoiceBox.setValue("Select Genre"); // This shows as the current value initially

        genreChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                movie.setGenre(newValue); // Update the movie genre
            }
        });
    }

    /**
     * This method will handle the action when the mouse is clicked or the enter key is pressed
     * for loading another page
     * 
     * @param mouseEvent
     * @param keyEvent
     * @param fxmlPath
     * @throws Exception
     */
    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); // Mouse left click
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); // Enter key
        }
    }

    /**
     * This method will be called when the upload button is pressed
     * For loading the Imageview and setting the image for movie object
     * @param event
     * @throws Exception
     */
    @FXML
    public void uploadImagePressed(MouseEvent event) throws Exception {
        // This method will be called when the upload button is clicked
        handleChooseImageButtonAction();
    }

    /**
     * This method will be called when the apply changes button is pressed
     * For handling the changes and adding the movie to the database
     * @param event
     * @throws Exception
     */
    @FXML
    public void applyChangesPressed(KeyEvent event) throws Exception {
        // This method will be called when the apply changes button is clicked
        if(movie.getTitle() != null && movie.getSummary() != null && movie.getGenre() != null && movie.getPoster() != null){
            if(!movie.getTitle().isBlank() && !movie.getSummary().isBlank() && !movie.getGenre().equals("Select Genre") && movie.getPoster().length != 0){
            handleChanges(movie);
            showAlert("Success", "Movie added successfully");
            }
            else{
                showAlert("Error", "Please fill all the fields");
            }
        }
        else{
            showAlert("Error", "Please fill all the fields");
        }
    }

    /**
     * This method will be called when the apply changes button is clicked
     * For handling the changes and adding the movie to the database
     * @param event mouse click event
     * @throws Exception
     */
    @FXML
    public void applyChangesClicked(MouseEvent event) throws Exception {
        // This method will be called when the apply changes button is clicked
        if(movie.getTitle() != null && movie.getSummary() != null && movie.getGenre() != null && movie.getPoster() != null){
            if(!movie.getTitle().isBlank() && !movie.getSummary().isBlank() && !movie.getGenre().equals("Select Genre") && movie.getPoster().length != 0){
            handleChanges(movie);
            showAlert("Success", "Movie added successfully");
            }
            else{
                showAlert("Error", "Please fill all the fields");
            }
        }
        else{
            showAlert("Error", "Please fill all the fields");
        }
    }

    /**
     * This method will be called when an alert is needed for warning
     * or success message
     * @param title of the alert
     * @param message content of the alert
     * @throws Exception
     */
    public static void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method will be called when the changes are made
     * For adding the movie to the database
     * @param movie object to be loaded to the database
     * @throws Exception
     */
    public static void handleChanges(Movie movie) throws Exception {
        DatabaseConnection.addMovie(movie);
    }

    /**
     * This method will be called when the title text field is typed
     * to save the content to the movie object
     * @param event
     * @throws Exception
     */
    @FXML
    public void titleTextFieldTyped(KeyEvent event) throws Exception {
        // This method will be called when typing in the title text field
        // Add your implementation here
        movie.setTitle(titleTextField.getText());
    }

    /**
     * This method will be called when the summary text area is typed
     * to save the content to the movie object
     * @param event
     * @throws Exception
     */
    @FXML
    public void summaryTextAreaTyped(KeyEvent event) throws Exception {
        // This method will be called when the summary text area is typed
        movie.setSummary(summaryTextArea.getText());
    }

    /**
     * This method will be called when the upload Image is clicked
     * for selecting an image file to be uploaded
     */
    @FXML
    public void uploadImageClicked() {
        // This method will be called when the upload button is clicked
        handleChooseImageButtonAction();
    }

    /**
     * This method will be called after the choose image button is clicked
     * for selecting an image file to be uploaded
     * Imageview is fixed to show the whatever image ratio is, it is fixed
     * to the format of the imageview
     * 
     * Image object is created and the image is set to the imageview
     * movie poster is set to the image bytes
     */
    @FXML
    public void handleChooseImageButtonAction() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select an Image File");

        // Set extension filters to accept only image files
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

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
            }
        }
    }

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    /**
     * This method will be used to get the content of the genre choice box
     * For setting genre for the movie object
     * @return selected value
     */
    public String getSelectedValue() {
        return genreChoiceBox.getValue();
    }

}
