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

public class Scene1Controller extends CashierProperties
{

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Button logout;

//////////////////////////////////

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

/////////////////////////////////////////////

    @FXML
    private TableView<Movie> movieTable;

    @FXML
    private TableColumn<Movie, String> genreColumn;

    @FXML
    private TableColumn<Movie, String> titleColumn;

/////////////////////////////////////////////

    @FXML
    private GridPane buySceneGrid;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView moviePoster;

    @FXML
    private Button buyButton;

    
    //////////////////////
    protected List<Movie> listedMovies;


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
    void initialize()
    {
        // CLEAR THE CASHIER PROPERTY
        CashierProperties.prepareStaticVariables();

        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        genreChoiceBox.setItems(FXCollections.observableArrayList(
        "all", "Sci-Fi", "Animation", "Horror", "Comedy", "Love", "Fantasy", "Drama", "Action", "Adventure", "Crime", "Documentary", "Sports"));
        genreChoiceBox.setValue("all");
        genreChoiceBox.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");


        listedMovies = DatabaseConnection.getAllMovies();
        
        // Set up the columns to use Movie's properties
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Populate the table with movie data
        movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        movieTable.setPlaceholder(new Label("No available movies right now"));

        //fireButton.visibleProperty().bind(employeeTable.getSelectionModel().selectedItemProperty().isNotNull());
        buySceneGrid.visibleProperty().bind(movieTable.getSelectionModel().selectedItemProperty().isNotNull());

        // Bind the selected movie to the right side
        movieTable.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null)
                updateRightSide(newValue);
            else
                clearRightSide();
        });

        // Initially clear the right side
        clearRightSide();
    }

    public void setPosterImage(Movie movie)
    {
        if (movie != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(movie.poster);
            Image image = new Image(inputStream);
            this.moviePoster.setImage(image);
        } else {
            this.moviePoster.setImage(null); // Clear the ImageView if no poster
        }
    }

    private void updateRightSide(Movie movie)
    {
        titleLabel.setText(movie.title);
        summaryLabel.setText(movie.summary);
        setPosterImage(movie);
        buyButton.setDisable(false); // Enable the Buy button
    }

    private void clearRightSide()
    {
        titleLabel.setText("");
        summaryLabel.setText("");
        setPosterImage(null);
        buyButton.setDisable(true); // Disable the Buy button
    }

    @FXML
    void buyClicked(MouseEvent event) throws Exception
    {
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        System.out.println(selectedMovie.id + "   " + selectedMovie.title + "  " + selectedMovie.genre); //debug

        if (selectedMovie != null)
        {
            CashierProperties.selectedMovie = selectedMovie;
            CashierProperties.listedSessionsAtScene2 = DatabaseConnection.getSessionsOfMovie(selectedMovie.id);
            App.loadScene("cashierScene2_3.fxml");
        }
    }

    @FXML
    void searchClicked(MouseEvent event)
    {
        // Get the selected genre from the ChoiceBox
        String selectedGenre = genreChoiceBox.getValue();

        // Get the text from the searchBar
        String searchTitle = searchBar.getText().trim();


        if(searchTitle.isEmpty() && !selectedGenre.equals("all"))
        {
            listedMovies = DatabaseConnection.SearchBy_Genre(selectedGenre);

            // Set up the columns to use Movie's properties
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        }
        else if (searchTitle.isEmpty() && selectedGenre.equals("all"))
        {
            listedMovies = DatabaseConnection.getAllMovies();

            // Set up the columns to use Movie's properties
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        }
        else if (!searchTitle.isEmpty() && selectedGenre.equals("all"))
        {
            listedMovies = DatabaseConnection.SearchBy_Title(searchTitle);

            // Set up the columns to use Movie's properties
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        }
        else if (!searchTitle.isEmpty() && !selectedGenre.equals("all"))
        {
            listedMovies = DatabaseConnection.searchBy_title_genre(searchTitle, selectedGenre);

            // Set up the columns to use Movie's properties
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
            movieTable.setItems(FXCollections.observableArrayList(listedMovies));
        }
    }

}
