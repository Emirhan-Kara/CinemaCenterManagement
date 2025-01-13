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

public class UpdateMovieController {

    public static Movie movie = new Movie();
    public static Movie oldMovie = new Movie();

    @FXML
    private Button deleteMovieButton;

    @FXML
    private Label deleteMovieLabel;

    @FXML
    private Label selectLabel;

    @FXML
    private Label selectLabel1;

    @FXML
    private Label selectLabel2;

    @FXML
    private GridPane coverGrid;

    @FXML
    private ChoiceBox<String> moviePicker;

    @FXML
    private Button genreButton;

    @FXML
    private Label emptyFieldsLabel;

    @FXML
    private Label noChangeLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea summaryTextArea;

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private Button titleButton;

    @FXML
    private Button summaryButton;

    @FXML
    private Button posterButton;

    @FXML
    private Button revertChangesButton;

    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button applyChangesButton;

    @FXML
    private Button uploadImageButton;

    @FXML
    private Button logout;

    /**
     * This method is called when the back button is clicked
     * to load the AdminMainPage.fxml file
     * @param event
     * @throws Exception
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    /**
     * This method is called when the back button is pressed
     * to load the AdminMainPage.fxml file
     * @param event
     * @throws Exception
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }
    
    /**
     * This method is called when the overlaying buttons are clicked
     * To give the effect of loading the upload Movie fields
     * @param event
     * @throws Exception
     */
    @FXML
    void disappear(MouseEvent event) throws Exception {
        
        Button clickedButton = (Button) event.getSource();
        clickedButton.setVisible(false);
        ((Pane) clickedButton.getParent()).getChildren().remove(clickedButton);
    }

    /**
     * This method is called when the revert changes button is clicked
     * It reverts the changes made to the movie object and the frontend objects
     * by reloading the scene
     * @param event
     * @throws Exception
     */
    @FXML
    void revertChangesClicked(MouseEvent event) throws Exception {
        // This method will be called when the revert changes button is clicked
        ManagerController.handleAction(event, null, "UpdateMovieScene.fxml");
    }

    /**
     * This method is called when the revert changes button is pressed
     * It reverts the changes made to the movie object and the frontend objects
     * by reloading the scene
     * @param event
     * @throws Exception
     */
    @FXML
    void revertChangesPressed(KeyEvent event) throws Exception {
        // This method will be called when the revert changes button is clicked
        ManagerController.handleAction(null, event, "UpdateMovieScene.fxml");
    }

    /**
     * This method is called when the delete movie button is pressed
     * Handles movie deletion and alerts the user
     * @param event
     * @throws Exception
     */
    @FXML
    public void deleteMoviePressed(KeyEvent event) throws Exception {

        if(moviePicker.getValue().equals("Select Movie")){
            labelVisibility(selectLabel2);
            return;
        }
        else{
            selectLabel2.setVisible(false);
        }
        
        DatabaseConnection.deleteMovie_byID(movie.getId());
        labelVisibility(deleteMovieLabel);
        emptyMovieFields(movie);
    }

    /**
     * This method is called when the delete movie button is clicked
     * Handles movie deletion and alerts the user
     * @param event
     * @throws Exception
     */
    @FXML
    public void deleteMovieClicked(MouseEvent event) throws Exception {
        if(moviePicker.getValue().equals("Select Movie")){
            labelVisibility(selectLabel2);
            return;
        }
        else{
            selectLabel.setVisible(false);
        }
        //Check if the movie exists in the database
        if(DatabaseConnection.getMovie_byID(movie.getId()) == null){
            labelVisibility(selectLabel2);
            return;
        }
            
        // This method will be called when the delete movie button is clicked
        DatabaseConnection.deleteMovie_byID(movie.getId());
        labelVisibility(deleteMovieLabel);
        emptyMovieFields(movie);
    }

    /**
     * This method empties the movie fields
     * @param movie Movie object to empty fields
     */
    public void emptyMovieFields(Movie movie){
        imageView.setImage(null);
        titleTextField.setText("");
        summaryTextArea.setText("");
        genreChoiceBox.setValue("Select Genre");
    }

    /**
     * This method is called when the apply changes button is pressed
     * Handles movie object changes and updates the database
     * Handles errors and alerts the user
     * @param event
     * @return
     * @throws Exception
     */
    @FXML
    public void applyChangesPressed(KeyEvent event) throws Exception {
        // This method will be called when the apply changes button is clicked
        // Database apply addmovie
        if (moviePicker.getValue().equals("Select Movie") || this.imageView == null || this.titleTextField == null || this.summaryTextArea == null ||
            this.titleTextField.getText().isEmpty() || this.summaryTextArea.getText().isEmpty() ||
            genreChoiceBox.getValue().equals("Select Genre")) {
            // Alert user that all fields must be filled
            labelVisibility(emptyFieldsLabel);
            return;
        } else {
            this.emptyFieldsLabel.setVisible(false);
        }
        if (oldMovie.getTitle().equals(movie.getTitle()) &&
            oldMovie.getGenre().equals(movie.getGenre()) &&
            oldMovie.getSummary().equals(movie.getSummary()) &&
            Arrays.equals(oldMovie.getPoster(), movie.getPoster())) {
            labelVisibility(noChangeLabel);
            // Alert user that no changes were made
            return;
        } else {
            this.noChangeLabel.setVisible(false);
        }

        DatabaseConnection.updateMovie(movie);
        labelVisibility(selectLabel1);
    }

    /**
     * This method is called when the apply changes button is clicked
     * Handles movie object changes and updates the database
     * Handles errors and alerts the user
     * @param event
     * @return
     * @throws Exception
     */
    @FXML
    public void applyChangesClicked(MouseEvent event) throws Exception {
        if(moviePicker.getValue().equals("Select Movie")){
            labelVisibility(selectLabel);
            return;
        }
        else{
            this.selectLabel.setVisible(false);
        }
        if (this.imageView == null || this.titleTextField == null || this.summaryTextArea == null ||
            this.titleTextField.getText().isEmpty() || this.summaryTextArea.getText().isEmpty() ||
            genreChoiceBox.getValue().equals("Select Genre")) {
            // Alert user that all fields must be filled
            labelVisibility(emptyFieldsLabel);
            return;
        } else {
            this.emptyFieldsLabel.setVisible(false);
        }
        if(DatabaseConnection.getMovie_byID(movie.getId()) == null){
            labelVisibility(selectLabel);
            return;
        }
        if (oldMovie.getTitle().equals(movie.getTitle()) &&
            oldMovie.getGenre().equals(movie.getGenre()) &&
            oldMovie.getSummary().equals(movie.getSummary()) &&
            Arrays.equals(oldMovie.getPoster(), movie.getPoster())) {
            labelVisibility(noChangeLabel);
            // Alert user that no changes were made
            return;
        } else {
            this.noChangeLabel.setVisible(false);
        }

        DatabaseConnection.updateMovie(movie);
        labelVisibility(this.selectLabel1);
        oldMovie = new Movie(movie);
    }

    /**
     * This method is called when the logout button is pressed
     * to load login page
     * @param event
     * @throws Exception
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * This method is called when the logout button is pressed
     * to load login page
     * @param event
     * @throws Exception
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        handleAction(null, event, "Login.fxml");
    }

    /**
     * This method changes the visibility of the labels to only show one at a time
     * @param label
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
     * This method is called when the FXML file is loaded
     * It initializes the moviePicker with movie titles
     * and sets the default value of the genreChoiceBox
     * Gets all movies to populate the moviePicker
     * sets the poster when the genreChoiceBox is changed
     */
    @FXML
    public void initialize() {

        List<Movie> movieList = DatabaseConnection.getAllMovies(); // Assuming this method fetches all movies from the database
        
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Initialize moviePicker with movie titles
        moviePicker.setValue("Select Movie");
        for (Movie movie : movieList) {
            moviePicker.getItems().add(movie.getTitle());
        }
        // Initialization code if needed
        // Add items to the ChoiceBox
        moviePicker.setValue("Select Movie");
        genreChoiceBox.getItems().addAll("Sci-Fi", "Animation", "Horror", "Drama", "Love", "Fantasy");

        // Set a default value (when no selection is made)
        genreChoiceBox.setValue("Select Genre"); // This shows as the current value initially

        genreChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                movie.setGenre(newValue); // Update the movie genre
            }
        });

        moviePicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //changeVisibility();
                movie.setGenre(newValue);
                for(Movie m : movieList) {
                    if (m.getTitle().equals(newValue)) {
                        movie = m;
                        genreChoiceBox.setValue(movie.getGenre());
                        imageView.setImage(new Image(new ByteArrayInputStream(movie.getPoster())));
                        imageView.setPreserveRatio(false);
                        imageView.setFitWidth(imageView.getParent().getLayoutBounds().getWidth());
                        imageView.setFitHeight(imageView.getParent().getLayoutBounds().getHeight());
                        titleTextField.setText(movie.getTitle());
                        summaryTextArea.setText(movie.getSummary());
                        oldMovie = new Movie(movie);
                    }
                }
            }
        });
    }

    /**
     * This method is called when another FXML file is going to be loaded
     * @param mouseEvent Last clicked element
     * @param keyEvent Last pressed key
     * @param fxmlPath Path to the FXML file
     * @throws Exception If the FXML file is not found
     */
    public static void handleAction(MouseEvent mouseEvent, KeyEvent keyEvent, String fxmlPath) throws Exception {
        if (mouseEvent != null && mouseEvent.getButton() == MouseButton.PRIMARY) {
            App.loadScene(fxmlPath); 
        } else if (keyEvent != null && keyEvent.getCode() == KeyCode.ENTER) {
            App.loadScene(fxmlPath); 
        }
    }

    /**
     * This method is called when the Image Upload button is pressed
     * @param event
     * @throws Exception
     */
    @FXML
    public void uploadImagePressed(MouseEvent event) throws Exception {
        handleChooseImageButtonAction();
    }

    /**
     * This method changes the movie's title instance when the title text field is typed
     * @param event Keystroke
     * @throws Exception
     */
    @FXML
    public void titleTextFieldTyped(KeyEvent event) throws Exception {
        // Add your implementation here
        movie.setTitle(titleTextField.getText());
    }

    /**
     * This method changes the movie's summary instance when the summary text area is typed
     * @param event Keystroke
     * @throws Exception
     */
    @FXML
    public void summaryTextAreaTyped(KeyEvent event) throws Exception {
        // This method will be called when the summary text area is typed
        movie.setSummary(summaryTextArea.getText());
    }

    /**
     * This method will be called when the upload image button is clicked
     */
    @FXML
    public void uploadImageClicked() {
        // This method will be called when the upload button is clicked
        handleChooseImageButtonAction();
    }

    /**
     * This method will be called when the user clicks the choose image button
     * Loads the image from the file system and displays it in the ImageView
     * according to the imageview's parent's layout bounds
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
            // Display the selected image in the ImageView
            try {
                byte[] imageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                imageView.setImage(image);
                imageView.setImage(image);
                imageView.setPreserveRatio(false);
                imageView.setFitWidth(imageView.getParent().getLayoutBounds().getWidth());
                imageView.setFitHeight(imageView.getParent().getLayoutBounds().getHeight());
                movie.setPoster(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the selected value from the movie picker or genre choice box
     * @return pickMovieValue if not null or default, return genreValue it is
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

}
