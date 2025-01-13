import java.io.ByteArrayInputStream;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * Controller for the first scene in the Cashier application.
 */
public class Scene1Controller extends CashierProperties {

        /**
     * Label to display the logged-in user's name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Label to display the role of the logged-in user.
     */
    @FXML
    private Label roleLabel;

    /**
     * Button to log out the current user.
     */
    @FXML
    private Button logout;

    /**
     * ChoiceBox to allow the user to select a movie genre for filtering.
     */
    @FXML
    private ChoiceBox<String> genreChoiceBox;

    /**
     * TextField to allow the user to input a movie title for searching.
     */
    @FXML
    private TextField searchBar;

    /**
     * Button to trigger the search functionality based on genre and/or title.
     */
    @FXML
    private Button searchButton;

    /**
     * TableView to display a list of movies.
     */
    @FXML
    private TableView<Movie> movieTable;

    /**
     * TableColumn to display the genre of movies in the table.
     */
    @FXML
    private TableColumn<Movie, String> genreColumn;

    /**
     * TableColumn to display the title of movies in the table.
     */
    @FXML
    private TableColumn<Movie, String> titleColumn;

    /**
     * GridPane that contains the elements for the "Buy" section.
     * It displays details of the selected movie.
     */
    @FXML
    private GridPane buySceneGrid;

    /**
     * Label to display the summary of the selected movie.
     */
    @FXML
    private Label summaryLabel;

    /**
     * Label to display the title of the selected movie.
     */
    @FXML
    private Label titleLabel;

    /**
     * ImageView to display the poster of the selected movie.
     */
    @FXML
    private ImageView moviePoster;

    /**
     * Button to initiate the process of buying tickets for the selected movie.
     */
    @FXML
    private Button buyButton;

    /**
     * List to hold the currently listed movies for display in the table.
     */
    protected List<Movie> listedMovies;
    /**
     * Handles the logout button click event.
     * 
     * @param event MouseEvent triggered when the logout button is clicked.
     * @throws Exception If the action cannot be handled.
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * Handles the logout button press event using the keyboard.
     * 
     * @param event KeyEvent triggered when the logout button is pressed.
     * @throws Exception If the action cannot be handled.
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "Login.fxml");
    }

    /**
     * Initializes the controller and sets up the UI elements.
     */
    @FXML
    void initialize() {
        // Clear static properties related to the cashier.
        CashierProperties.prepareStaticVariables();

        // Set the name and role of the logged-in employee.
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Populate the genre choice box with available options.
        genreChoiceBox.setItems(FXCollections.observableArrayList(
                "all", "Sci-Fi", "Animation", "Horror", "Comedy", "Love", "Fantasy", "Drama", "Action", "Adventure", "Crime", "Documentary", "Sports"));
        genreChoiceBox.setValue("all");
        genreChoiceBox.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Fetch all movies from the database.
        listedMovies = DatabaseConnection.getAllMovies();

        // Set up the table columns to display movie properties.
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Populate the table with the list of movies.
        movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        movieTable.setPlaceholder(new Label("No available movies right now"));

        // Bind the visibility of the buy scene grid to the table selection.
        buySceneGrid.visibleProperty().bind(movieTable.getSelectionModel().selectedItemProperty().isNotNull());

        // Add a listener to update the right-side details when a movie is selected.
        movieTable.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null)
                updateRightSide(newValue);
            else
                clearRightSide();
        });

        // Clear the right side details on initialization.
        clearRightSide();
    }

    /**
     * Sets the poster image for the selected movie.
     * 
     * @param movie The selected movie.
     */
    public void setPosterImage(Movie movie) {
        if (movie != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(movie.poster);
            Image image = new Image(inputStream);
            this.moviePoster.setImage(image);
        } else {
            this.moviePoster.setImage(null); // Clear the ImageView if no poster
        }
    }

    /**
     * Updates the right-side details with the selected movie's information.
     * 
     * @param movie The selected movie.
     */
    private void updateRightSide(Movie movie) {
        titleLabel.setText(movie.title);
        summaryLabel.setText(movie.summary);
        setPosterImage(movie);
        buyButton.setDisable(false); // Enable the Buy button
    }

    /**
     * Clears the right-side details.
     */
    private void clearRightSide() {
        titleLabel.setText("");
        summaryLabel.setText("");
        setPosterImage(null);
        buyButton.setDisable(true); // Disable the Buy button
    }

    /**
     * Handles the Buy button click event.
     * 
     * @param event MouseEvent triggered when the Buy button is clicked.
     * @throws Exception If the action cannot be handled.
     */
    @FXML
    void buyClicked(MouseEvent event) throws Exception {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            CashierProperties.selectedMovie = selectedMovie;
            CashierProperties.listedSessionsAtScene2 = DatabaseConnection.getSessionsOfMovie(selectedMovie.id);
            App.loadScene("cashierScene2_3.fxml");
        }
    }

    /**
     * Handles the Search button click event and filters movies based on genre and title.
     * 
     * @param event MouseEvent triggered when the Search button is clicked.
     */
    @FXML
    void searchClicked(MouseEvent event) {
        // Get the selected genre and search title.
        String selectedGenre = genreChoiceBox.getValue();
        String searchTitle = searchBar.getText().trim();

        // Filter movies based on the search criteria.
        if (searchTitle.isEmpty() && !selectedGenre.equals("all")) {
            listedMovies = DatabaseConnection.SearchBy_Genre(selectedGenre);
        } else if (searchTitle.isEmpty() && selectedGenre.equals("all")) {
            listedMovies = DatabaseConnection.getAllMovies();
        } else if (!searchTitle.isEmpty() && selectedGenre.equals("all")) {
            listedMovies = DatabaseConnection.SearchBy_Title(searchTitle);
        } else if (!searchTitle.isEmpty() && !selectedGenre.equals("all")) {
            listedMovies = DatabaseConnection.searchBy_title_genre(searchTitle, selectedGenre);
        }

        // Update the table with the filtered movies.
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        movieTable.setItems(FXCollections.observableArrayList(listedMovies));
    }
}
