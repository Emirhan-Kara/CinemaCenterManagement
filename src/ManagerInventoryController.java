import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for stocks scene
 */
public class ManagerInventoryController
{
    /**
     * Displays user role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Displays user name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Navigates back to the previous screen.
     */
    @FXML
    private Button backButton;

    /**
     * Opens beverage-related actions.
     */
    @FXML
    private Button beverageButton;

    /**
     * Displays new beverage stock.
     */
    @FXML
    private Label beverageNewStock;

    /**
     * Slider for beverage stock adjustment.
     */
    @FXML
    private Slider beverageSlider;

    /**
     * Displays current beverage stock.
     */
    @FXML
    private Label beverageStock;

    /**
     * Opens food-related actions.
     */
    @FXML
    private Button foodButton;

    /**
     * Displays new food stock.
     */
    @FXML
    private Label foodNewStock;

    /**
     * Slider for food stock adjustment.
     */
    @FXML
    private Slider foodSlider;

    /**
     * Displays current food stock.
     */
    @FXML
    private Label foodStock;

    /**
     * Logs out the user.
     */
    @FXML
    private Button logout;

    /**
     * Opens toy-related actions.
     */
    @FXML
    private Button toyButton;

    /**
     * Displays new toy stock.
     */
    @FXML
    private Label toyNewStock;

    /**
     * Slider for toy stock adjustment.
     */
    @FXML
    private Slider toySlider;

    /**
     * Displays current toy stock.
     */
    @FXML
    private Label toyStock;

    /**
     * List of all products.
     */
    private List<Product> allProducts;


    /**
     * Initializer to setup the scene and get all the product data
     */
    @FXML
    public void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
        
        // product index -> ticket / beverage / Food / Toy
        this.allProducts = DatabaseConnection.getProducts(null);
        updatePriceLabels();

        connectTextWithSlider(beverageSlider, beverageNewStock);
        connectTextWithSlider(foodSlider, foodNewStock);
        connectTextWithSlider(toySlider, toyNewStock);
    }

    /**
     * Helper method to bind the proper labels with proper sliders
     * @param slider slider
     * @param newStockLabel label
     */
    private void connectTextWithSlider(Slider slider, Label newStockLabel)
    {
        // Bind the new stock label to the slider's value
        newStockLabel.textProperty().bind(slider.valueProperty().asString("%.0f"));
    }

    /**
     * Update the labels on the scene
     */
    private void updatePriceLabels()
    {
        // Get the stocks from database
        int foodStockDB = allProducts.get(1).getStock();
        int beverageStockDB = allProducts.get(2).getStock();
        int toyStockDB = allProducts.get(3).getStock();

        // put them to the reletad text labels on the screen 
        this.beverageStock.setText(String.valueOf(beverageStockDB));
        this.foodStock.setText(String.valueOf(foodStockDB));
        this.toyStock.setText(String.valueOf(toyStockDB));
    }

    /**
     * updates the selected product both in database and in scene
     * @param slider slider to be extracted of its value
     * @param productIdx prouct index
     */
    private void updateSelectedProduct(Slider slider, int productIdx)
    {
        // get the number to be added to the stock from the slider
        int addStock = (int) slider.getValue();

        // get the current stock, add the new value to it and set it
        int oldStock = allProducts.get(productIdx).getStock();
        allProducts.get(productIdx).setStock(oldStock + addStock);

        // update the database
        DatabaseConnection.updateProduct(allProducts.get(productIdx));

        // update the labels shown on the screen
        updatePriceLabels();

        slider.setValue(slider.getMin());
    }


    /**
     * event handler for back button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "ManagerMainMenu.fxml");
    }

    /**
     * event handler for back button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "ManagerMainMenu.fxml");
    }

    /**
     * event handler for logout button click
     * @param event mouns event
     * @throws Exception for load scene
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * event handler for logout button press
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "Login.fxml");
    }
  

    /**
     * event handler for beverage adding
     * @param event mouse event
     */
    @FXML
    void beverageClicked(MouseEvent event)
    {
        
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(beverageSlider, 2);
    }

    /**
     * event handler for food adding
     * @param event mouse event
     */
    @FXML
    void foodClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(foodSlider, 1);
    }

    /**
     * event handler for toy adding
     * @param event mouse event
     */
    @FXML
    void toyClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(toySlider, 3);
    }

}
