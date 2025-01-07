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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Scene2_3Controller extends CashierProperties
{

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button logout;

//////////////////////////////////

    @FXML
    private ImageView moviePoster;

    ////////////////////////////////////
    
    @FXML
    private TableView<Sessions> sessionTable;

    @FXML
    private TableColumn<Sessions, Date> dateColumn;

    @FXML
    private TableColumn<Sessions, Time> timeColumn;

    @FXML
    private TableColumn<Sessions, Integer> hallColumn;

    @FXML
    private GridPane seatGrid;

    @FXML
    private GridPane buySceneGrid;

    /////////////////////////////
    
    @FXML
    private Label totalPriceLabel;

    @FXML
    private Button buyButton;
    
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "cashierScene1.fxml");
    }

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
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "cashierScene1.fxml");
    }


    //////////////////////
    protected List<Sessions> listedSessions;

    @FXML
    void initialize()
    {
        // get the information from CashierProperties
        normalMap = CashierProperties.normalTicketMap;
        discountedMap = CashierProperties.discountedTicketMap;


        ByteArrayInputStream inputStream = new ByteArrayInputStream(CashierProperties.selectMovie.poster);
        Image image = new Image(inputStream);
        this.moviePoster.setImage(image);

        totalPriceLabel.setText("0 ₺");

        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Fetch sessions
        listedSessions = DatabaseConnection.getSessionsOfMovie(CashierProperties.selectMovie.id);

        // Set up the TableView columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));   // Maps to getDate()
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));   // Maps to getTime()
        hallColumn.setCellValueFactory(new PropertyValueFactory<>("hallId")); // Maps to getHallId()

        sessionTable.setItems(FXCollections.observableArrayList(listedSessions));

        // Bind the seats image with the: .visibleProperty().bind(sessionTable.getSelectionModel().selectedItemProperty().isNotNull());
        // Add listener to handle row selection
        sessionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Make buySceneGrid visible
                buySceneGrid.setVisible(true);

                // remove the seats of the previous selection
                seatGrid.getChildren().clear();

                // Display seats for the selected session
                int[] seatAvailability = DatabaseConnection.Seat_Availability_Array(newValue.getId(), newValue.getHallId());
                displaySeats(seatAvailability, newValue.getHallId());
            } else {
                buySceneGrid.setVisible(false);
            }
        });
    }


    private int seatIndex;
    private void displaySeats(int[] seatAvailability, int hallId)
    {
        // HallA: 2x8, HallB: 6x8
        int cols = 8; 
        int rows = (hallId == 1) ? 2 : 6;
        seatIndex = 0;
    
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++)
            {
                addButtonToGrid(i, j, seatIndex, seatAvailability[seatIndex]);
                seatIndex++;
            }
        }
    }

    public void addButtonToGrid(int row, int column, int seatIDX, int isAvailable)
    {
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

        // Set style based on availability
        if (isAvailable == 0) {
            button.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 12;"); // Available
            button.setOnAction(event -> selectSeat(seatIDX + 1, button)); // Update selected seats
        }
        else
        {
            button.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12;"); // Booked
            button.setDisable(true); // Disable booked seats
        }
    }

    private ArrayList<Integer> selectedSeats = new ArrayList<>();

    // maps: key->seat number   -   value->customer name
    private Map<Integer, String> normalMap;
    private Map<Integer, String> discountedMap;

    private double currentDiscountedTicketPrice = CashierProperties.currentProductPrices.get(4).getPrice();
    private double currentNormalTicketPrice = CashierProperties.currentProductPrices.get(0).getPrice();
    private double currentTotalPrice = 0;
    public void selectSeat(int seatId, Button seatButton)
    {
        // Check if the seat is already selected
        if (selectedSeats.contains(seatId)) {
            // Remove the seat from the selectedSeats list
            selectedSeats.remove((Integer) seatId);

            // Remove the seat from the appropriate map
            if (discountedMap.containsKey(seatId))
            {
                discountedMap.remove(seatId);
                System.out.println("Removed from discounted map: Seat " + seatId);

                // update the total price label 
                currentTotalPrice -= currentDiscountedTicketPrice;
                totalPriceLabel.setText(currentTotalPrice + " ₺");
            }
            else if (normalMap.containsKey(seatId))
            {
                normalMap.remove(seatId);
                System.out.println("Removed from normal map: Seat " + seatId);

                // update the total price label
                currentTotalPrice -= currentNormalTicketPrice;
                totalPriceLabel.setText(currentTotalPrice + " ₺");
            }

            // Reset the button color to green (available)
            seatButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 12;");
            return;
        }

        try
        {
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
            if (controller.isConfirmed())
            {
                int age = controller.getAge();
                String name = controller.getName();

                // Debug output
                if ((age < 18) || (age > 60))
                {
                    System.out.println("DISCOUNT! Age: " + age + ", Name: " + name);

                    // update the discounted sale map
                    discountedMap.put(seatId, name);
                    
                    currentTotalPrice += currentDiscountedTicketPrice;
                    totalPriceLabel.setText(currentTotalPrice + " ₺");
                }
                else
                {
                    System.out.println("normal: Age: " + age + ", Name: " + name);

                    // update the normalPrices sale map
                    normalMap.put(seatId, name);

                    // update the total price label
                    currentTotalPrice += currentNormalTicketPrice;
                    totalPriceLabel.setText(currentTotalPrice + " ₺");
                }

                // Save the seatId to selectedSeats
                selectedSeats.add(seatId);

                // Change the button color to orange
                seatButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 12;");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void buyClicked() throws Exception
    {
        CashierProperties.totalTicketPrice = currentTotalPrice;
        CashierProperties.totalTicketTax = Math.round(currentTotalPrice*0.2);

        CashierProperties.normalTicketMap = normalMap;
        CashierProperties.discountedTicketMap = discountedMap;

        App.loadScene("cashierScene4.fxml");
    }
}
