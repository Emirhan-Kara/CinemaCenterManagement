import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller for Scene 2 and 3 in the cashier application.
 * Extends {@link CashierProperties} to utilize shared properties and methods.
 */

public class Scene2_3Controller extends CashierProperties
{
     /**
     * The root container for the scene.
     * Represents the main {@link AnchorPane} that holds all UI elements.
     */
    @FXML
    private AnchorPane rootSc
    /**
     * Handles the event triggered when the "Enter" key is pressed.
     * If the "Enter" key is detected, transitions to the next scene.
     *
     * @param event The key event triggered by user input.
     * @throws Exception If there is an issue loading the next scene.
     */
    @FXML
    void enterClickedForNext(KeyEvent event) throws Exception {
        if (event.getCode() != KeyCode.ENTER) {
            return;
        }

        // Load the next scene
        App.loadScene("cashierScene4.fxml");
    }
     /**
     * Label displaying the user's name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Label displaying the user's role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Button to navigate back to the previous scene.
     */
    @FXML
    private Button backButton;

    /**
     * Button to log out the user and navigate to the login screen.
     */
    @FXML
    private Button logout;

    /**
     * ImageView to display the movie poster for the selected session.
     */
    @FXML
    private ImageView moviePoster;

    /**
     * TableView to display available sessions for the selected movie.
     */
    @FXML
    private TableView<Sessions> sessionTable;

   /**
     * TableColumn for displaying session dates.
     */
    @FXML
    private TableColumn<Sessions, Date> dateColumn;

    /**
     * TableColumn for displaying session times.
     */
    @FXML
    private TableColumn<Sessions, Time> timeColumn;

    /**
     * TableColumn for displaying session hall IDs.
     */
    @FXML
    private TableColumn<Sessions, Integer> hallColumn;

     /**
     * GridPane for displaying seats for the selected session.
     */
    @FXML
    private GridPane seatGrid;

    /**
     * GridPane for displaying the scene where tickets are purchased.
     */
    @FXML
    private GridPane buySceneGrid;

    /**
     * Label displaying the ticket price.
     */
    @FXML
    private Label ticketPriceLabel;

    /**
     * Label displaying the calculated tax for tickets.
     */
    @FXML
    private Label taxLabel;

        /**
     * Label displaying the final price after tax.
     */
    @FXML
    private Label finalPriceLabel;

    /**
     * Button for purchasing tickets.
     */
    @FXML
    private Button buyButton;
    
   /**
 * Handles navigation back to the previous scene when the back button is pressed on the keyboard.
 *
 * @param event The {@link KeyEvent} triggered by the user's key press.
 * @throws Exception if scene loading fails.
 */
@FXML
void backPressed(KeyEvent event) throws Exception {
    ManagerController.handleAction(null, event, "cashierScene1.fxml");
}

/**
 * Handles user logout when the logout button is clicked.
 *
 * @param event The {@link MouseEvent} triggered by the user's mouse click.
 * @throws Exception if scene loading fails.
 */
@FXML
void logoutClicked(MouseEvent event) throws Exception {
    ManagerController.handleAction(event, null, "Login.fxml");
}

/**
 * Handles user logout when the logout button is pressed on the keyboard.
 *
 * @param event The {@link KeyEvent} triggered by the user's key press.
 * @throws Exception if scene loading fails.
 */
@FXML
void logoutPressed(KeyEvent event) throws Exception {
    ManagerController.handleAction(null, event, "Login.fxml");
}

/**
 * Handles navigation back to the previous scene when the back button is clicked.
 *
 * @param event The {@link MouseEvent} triggered by the user's mouse click.
 * @throws Exception if scene loading fails.
 */
@FXML
void backClicked(MouseEvent event) throws Exception {
    ManagerController.handleAction(event, null, "cashierScene1.fxml");
}

/**
 * List of sessions displayed in the current scene.
 * This list contains all sessions relevant to the user's selection.
 */
protected List<Sessions> listedSessions;

/**
 * The currently selected session from the available list of sessions.
 */
protected Sessions selectedSession;

/**
 * List of seat numbers currently selected by the user.
 */
private ArrayList<Integer> selectedSeats;

/**
 * Map storing normal ticket sales, where the key is the seat number
 * and the value is the customer's name.
 */
private Map<Integer, String> normalMap;

/**
 * Map storing discounted ticket sales, where the key is the seat number
 * and the value is the customer's name.
 */
private Map<Integer, String> discountedMap;

/**
 * The price of a discounted ticket for the selected movie session.
 * Retrieved from {@link CashierProperties#currentProductPrices}.
 */
private double currentDiscountedTicketPrice = CashierProperties.currentProductPrices.get(4).getPrice();

/**
 * The price of a normal ticket for the selected movie session.
 * Retrieved from {@link CashierProperties#currentProductPrices}.
 */
private double currentNormalTicketPrice = CashierProperties.currentProductPrices.get(0).getPrice();

/**
 * Initializes the controller and sets up the UI components with data from {@link CashierProperties}.
 * This method is automatically called after the FXML file has been loaded.
 */
@FXML
void initialize() {
    // Get the information from CashierProperties
    if (CashierProperties.selectedSession != null) {
        // Create a new session object based on the selected session
        selectedSession = new Sessions(CashierProperties.selectedSession);
    } else {
        selectedSession = null;
    }

    // Retrieve and calculate pricing information
    currentTotalPrice = CashierProperties.totalTicketPrice;
    currentTotalTax = CashierProperties.totalTicketTax;
    currentFinalPrice = currentTotalTax + currentTotalPrice;

    // Create local copies of selected seats and ticket maps
    selectedSeats = new ArrayList<>(CashierProperties.selectedSeatsAtScene2);
    normalMap = new HashMap<>(CashierProperties.normalTicketMap);
    discountedMap = new HashMap<>(CashierProperties.discountedTicketMap);

    // Fetch available sessions
    listedSessions = new ArrayList<>(CashierProperties.listedSessionsAtScene2);

    // Set the movie poster image
    ByteArrayInputStream inputStream = new ByteArrayInputStream(CashierProperties.selectedMovie.poster);
    Image image = new Image(inputStream);
    this.moviePoster.setImage(image);

    // Initialize pricing and tax labels to 0
    updatePriceAndTaxLabel(0);

    // Set employee name and role in the UI
    nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
    roleLabel.setText(LoginController.loggedEmployee.getUserRole());

    // Set up the columns in the TableView with appropriate properties
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // Maps to getDate()
    timeColumn.setCellValueFactory(new PropertyValueFactory<>("time")); // Maps to getTime()
    hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId")); // Maps to getHallId()

    // Populate the TableView with the listed sessions
    sessionTable.setItems(FXCollections.observableArrayList(listedSessions));

    // If a session was previously selected, reselect it and display its seats
    if (selectedSession != null) {
        sessionTable.getSelectionModel().select(selectedSession);
        openSeatScreen(selectedSession);

        // Simulate user selection in the TableView to ensure focus and visibility
        int index = sessionTable.getItems().indexOf(selectedSession);
        if (index != -1) {
            sessionTable.getFocusModel().focus(index); // Focus the item
            sessionTable.getSelectionModel().clearAndSelect(index); // Select the item
            sessionTable.requestFocus(); // Ensure visibility of the selection
        }
    }

    // Add a listener to handle session selection changes
    sessionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            openSeatScreen(newValue);
        } else {
            buySceneGrid.setVisible(false);
        }
    });

    // Display a placeholder message when there are no sessions available
    sessionTable.setPlaceholder(new Label("No available sessions right now"));
}

   /**
 * Updates the UI to display the seating arrangement for a selected session.
 * This method makes the seat selection grid visible, clears any previous selections, 
 * and fetches the seat availability for the given session.
 *
 * @param selection The selected session for which to display the seat screen.
 */
private void openSeatScreen(Sessions selection) {
    selectedSession = selection;

    // Make the buy scene grid visible
    buySceneGrid.setVisible(true);

    // Clear the grid of previous seat selections
    seatGrid.getChildren().clear();

    // Reset selected seats and re-fetch them
    selectedSeats.clear();

    // Check if the session has changed and reset seats if necessary
    if (CashierProperties.selectedSession != null && selectedSession.getId() != CashierProperties.selectedSession.getId()) {
        CashierProperties.selectedSeatsAtScene2.clear();
        selectedSeats = new ArrayList<>(CashierProperties.selectedSeatsAtScene2);
        currentTotalPrice = 0;
        currentTotalTax = 0;
        currentFinalPrice = currentTotalTax + currentTotalPrice;
        updatePriceAndTaxLabel(0);
    } else {
        selectedSeats = new ArrayList<>(CashierProperties.selectedSeatsAtScene2);
        if (selectedSeats.isEmpty()) {
            currentTotalPrice = 0;
            currentTotalTax = 0;
            currentFinalPrice = currentTotalTax + currentTotalPrice;
        }
        // Update the price labels
        updatePriceAndTaxLabel(0);
    }

    // Fetch and display seat availability for the selected session
    int[] seatAvailability = DatabaseConnection.Seat_Availability_Array(selection.getId(), selection.getHallId());
    displaySeats(seatAvailability, selection.getHallId());
}

/**
 * Displays the seats for the selected session and hall in the grid layout.
 * Seats are represented as buttons, indicating their availability and booking status.
 *
 * @param seatAvailability An array representing the availability status of each seat.
 *                         Values in the array may indicate booked or available seats.
 * @param hallId           The ID of the hall where the session is being held.
 *                         Determines the seat layout (e.g., rows and columns).
 */
private void displaySeats(int[] seatAvailability, int hallId) {
    // Determine the number of rows and columns based on the hall ID
    int cols = 8;
    int rows = (hallId == 1) ? 2 : 6;
    seatIndex = 0;

    // Iterate through rows and columns to add seats to the grid
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            boolean isBooked = selectedSeats.contains(seatIndex + 1);
            addButtonToGrid(i, j, seatIndex, seatAvailability[seatIndex], isBooked);
            seatIndex++;
        }
    }
}

   /**
 * Adds a seat button to the seat grid at the specified row and column. 
 * The button's style and behavior are determined by its availability and booking status.
 *
 * @param row               The row in the grid where the button should be placed.
 * @param column            The column in the grid where the button should be placed.
 * @param seatIDX           The seat index used for identifying the seat.
 * @param isAvailable       The availability status of the seat:
 *                          0 indicates available, any other value indicates booked in the database.
 * @param isBookedPreviously A boolean indicating if the seat was booked in a previous session
 *                           (but not yet committed to the database).
 */
public void addButtonToGrid(int row, int column, int seatIDX, int isAvailable, boolean isBookedPreviously) {
    // Calculate the button text as row + column + 1
    String buttonText = String.valueOf(seatIDX + 1);

    // Create a new button
    Button button = new Button(buttonText);
    button.setPrefSize(40, 40); // Fixed size for buttons

    // Center the button in the cell
    GridPane.setHalignment(button, javafx.geometry.HPos.CENTER);
    GridPane.setValignment(button, javafx.geometry.VPos.CENTER);

    // Add the button to the specified row and column
    seatGrid.add(button, column, row);

    // Set style and behavior based on availability and booking status
    if (isBookedPreviously) {
        button.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 12;");
        button.setOnAction(event -> selectSeat(seatIDX + 1, button)); // Update selected seats
    } else if (isAvailable == 0) {
        button.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 12;");
        button.setOnAction(event -> selectSeat(seatIDX + 1, button)); // Update selected seats
    } else {
        button.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12;");
        button.setDisable(true); // Disable booked seats
    }
}

/**
 * Handles seat selection and updates the state of the seat, including its pricing, maps, 
 * and visual representation in the UI. Opens a dialog for entering customer details (name and age) 
 * when a seat is selected for the first time.
 *
 * @param seatId     The ID of the seat being selected or deselected.
 * @param seatButton The button representing the seat in the UI.
 */
public void selectSeat(int seatId, Button seatButton) {
    // Check if the seat is already selected
    if (selectedSeats.contains(seatId)) {
        // Remove the seat from the selectedSeats list
        selectedSeats.remove((Integer) seatId);

        // Remove the seat from the appropriate map
        if (discountedMap.containsKey(seatId)) {
            discountedMap.remove(seatId);
            System.out.println("Removed from discounted map: Seat " + seatId);

            // Update the total price label
            updatePriceAndTaxLabel(-currentDiscountedTicketPrice);
        } else if (normalMap.containsKey(seatId)) {
            normalMap.remove(seatId);
            System.out.println("Removed from normal map: Seat " + seatId);

            // Update the total price label
            updatePriceAndTaxLabel(-currentNormalTicketPrice);
        }

        // Reset the button color to green (available)
        seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 12;");
        return;
    }

    try {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("askAgeAndName.fxml"));
        Parent root = loader.load();

        // Get the controller
        TakeNameAndAgeController controller = loader.getController();

        // Create a new stage for the input window
        Stage stage = new Stage();
        stage.setTitle("Enter Name and Age");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        stage.showAndWait(); // Wait until the window is closed

        // Check if the input was confirmed
        if (controller.isConfirmed()) {
            int age = controller.getAge();
            String name = controller.getName();

            // Debug output
            if ((age < 18) || (age > 60)) {
                System.out.println("DISCOUNT! Age: " + age + ", Name: " + name);

                // Update the discounted sales map
                discountedMap.put(seatId, name);
                updatePriceAndTaxLabel(currentDiscountedTicketPrice);
            } else {
                System.out.println("Normal: Age: " + age + ", Name: " + name);

                // Update the normal sales map
                normalMap.put(seatId, name);

                // Update the total price label
                updatePriceAndTaxLabel(currentNormalTicketPrice);
            }

            // Save the seatId to selectedSeats
            selectedSeats.add(seatId);

            // Change the button color to orange
            seatButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 12;");
        }
    } catch (IOException e) {
        System.err.println("Name surname age screen cannot open");
        e.printStackTrace();
    }
}
/**
 * The current total ticket price, excluding tax.
 */
private double currentTotalPrice;

/**
 * The current total tax calculated based on the ticket price.
 */
private double currentTotalTax;

/**
 * The final total price, including tax.
 */
private double currentFinalPrice;

/**
 * Updates the ticket price, tax, and final price based on the given value.
 * Adjusts the state of the "Buy" button based on the calculated final price.
 * Also updates the UI labels to reflect the updated values.
 *
 * @param val The value to add or subtract from the current total ticket price.
 */
private void updatePriceAndTaxLabel(double val) {
    // Update the current ticket price
    currentTotalPrice += val;

    // Update the tax
    currentTotalTax = currentTotalPrice * 0.2;

    // Update the final price
    currentFinalPrice = currentTotalPrice + currentTotalTax;

    // Enable or disable the buy button based on the final price
    if (currentFinalPrice != 0.0)
        buyButton.setDisable(false); // Enables the button
    else
        buyButton.setDisable(true); // Disables the button

    // Update the UI labels with the new values
    ticketPriceLabel.setText(String.format("%.2f ₺", currentTotalPrice));
    taxLabel.setText(String.format("%.2f ₺", currentTotalTax));
    finalPriceLabel.setText(String.format("%.2f ₺", currentFinalPrice));
}

/**
 * Handles the event when the "Buy" button is clicked.
 * Stores the current session, selected seats, ticket prices, and seat maps
 * in the {@link CashierProperties} class and transitions to the next scene.
 *
 * @throws Exception If there is an error loading the next scene.
 */
@FXML
public void buyClicked() throws Exception {
    // Save the current session and selected seats
    CashierProperties.selectedSession = selectedSession;
    CashierProperties.selectedSeatsAtScene2 = selectedSeats;

    // Save the ticket price and tax
    CashierProperties.totalTicketPrice = currentTotalPrice;
    CashierProperties.totalTicketTax = currentTotalTax;

    // Save the seat maps
    CashierProperties.normalTicketMap = normalMap;
    CashierProperties.discountedTicketMap = discountedMap;

    // Load the next scene
    App.loadScene("cashierScene4.fxml");
}
