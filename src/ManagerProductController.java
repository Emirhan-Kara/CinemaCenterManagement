import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for prices scene
 */
public class ManagerProductController
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
     * Displays new beverage price.
     */
    @FXML
    private Label beverageNewPrice;

    /**
     * Displays current beverage price.
     */
    @FXML
    private Label beveragePrice;

    /**
     * Slider for beverage stock adjustment.
     */
    @FXML
    private Slider beverageSlider;

    /**
     * Activates discount settings.
     */
    @FXML
    private Button discountButton;

    /**
     * Displays new discount value.
     */
    @FXML
    private Label discountNewValue;

    /**
     * Displays current discount rate.
     */
    @FXML
    private Label discountRate;

    /**
     * Slider for adjusting discount rate.
     */
    @FXML
    private Slider discountSlider;

    /**
     * Opens food-related actions.
     */
    @FXML
    private Button foodButton;

    /**
     * Displays new food price.
     */
    @FXML
    private Label foodNewPrice;

    /**
     * Displays current food price.
     */
    @FXML
    private Label foodPrice;

    /**
     * Slider for food stock adjustment.
     */
    @FXML
    private Slider foodSlider;

    /**
     * Logs out the user.
     */
    @FXML
    private Button logout;

    /**
     * Opens ticket-related actions.
     */
    @FXML
    private Button ticketButton;

    /**
     * Displays new ticket price.
     */
    @FXML
    private Label ticketNewPrice;

    /**
     * Displays current ticket price.
     */
    @FXML
    private Label ticketPrice;

    /**
     * Slider for ticket stock adjustment.
     */
    @FXML
    private Slider ticketSlider;

    /**
     * Opens toy-related actions.
     */
    @FXML
    private Button toyButton;

    /**
     * Displays new toy price.
     */
    @FXML
    private Label toyNewPrice;

    /**
     * Displays current toy price.
     */
    @FXML
    private Label toyPrice;

    /**
     * Slider for toy stock adjustment.
     */
    @FXML
    private Slider toySlider;

    /**
     * List of all products.
     */
    private List<Product> allProducts;


    /**
     * Initializer to setup the scene, get product data
     */
    @FXML
    public void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        connectTextWithSlider(beverageSlider, beverageNewPrice);
        connectTextWithSlider(discountSlider, discountNewValue);
        connectTextWithSlider(foodSlider, foodNewPrice);
        connectTextWithSlider(ticketSlider, ticketNewPrice);
        connectTextWithSlider(toySlider, toyNewPrice);

        // product index -> ticket / beverage / Food / Toy
        allProducts = DatabaseConnection.getProducts(null);

        updateLabels();

    }

    /**
     * Update the labels on the scene with the changed values
     */
    private void updateLabels()
    {
        // set the existing prices to the related labels
        double ticketPriceDB = allProducts.get(0).getPrice();
        ticketPrice.setText(String.format("%.2f", ticketPriceDB) + " ₺");

        double foodPricePriceDB = allProducts.get(1).getPrice();
        foodPrice.setText(String.format("%.2f", foodPricePriceDB) + " ₺");

        double beveragePriceDB = allProducts.get(2).getPrice();
        beveragePrice.setText(String.format("%.2f", beveragePriceDB) + " ₺");

        double toyPriceDB = allProducts.get(3).getPrice();
        toyPrice.setText(String.format("%.2f", toyPriceDB) + " ₺");

        double discountRateDB = allProducts.get(4).get_agebased_disc_rate_(); // it is between 0 and 1
        // normalize it, between
        int normalizedDiscountRate = (int)(discountRateDB * 100);
        discountRate.setText("% " + String.valueOf(normalizedDiscountRate));
    }


    /**
     * Helper method to bind the proper labels with proper sliders
     * @param slider slider
     * @param newPriceLabel label
     */
    private void connectTextWithSlider(Slider slider, Label newPriceLabel)
    {
        // Bind the new price label to the slider's value
        newPriceLabel.textProperty().bind(slider.valueProperty().asString("%.2f"));
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
     * When a product price has changed:
     * 1 - Change the label on the screen with the new price derived from the slider value
     * 2 - Update the price of the product from the list
     * 3 - Update that product in the database
     * 4 - Reset the slider value 
     * @param oldPrice
     * @param newPrice
     * @param slider
     * @param productIdx
     */
    private void updateSelectedProduct(Slider slider, int productIdx)
    {
        // get the new value to be set, as double
        double newVal = slider.getValue();

        // update the price of the product from the list
        allProducts.get(productIdx).setPrice(newVal);

        // update the database
        DatabaseConnection.updateProduct(allProducts.get(productIdx));

        // update the prices with the values from database
        updateLabels();

        // reset the slider
        slider.setValue(slider.getMin());
    }


    /**
     * Update tickets in database
     * @param event mouse event
     */
    @FXML
    void ticketClicked(MouseEvent event)
    {
        updateSelectedProduct(ticketSlider, 0);
    }

    /**
     * Update beverages in database
     * @param event mouse event
     */
    @FXML
    void beverageClicked(MouseEvent event)
    {
        updateSelectedProduct(beverageSlider, 2);
    }

    /**
     * Update foods in database
     * @param event mouse event
     */
    @FXML
    void foodClicked(MouseEvent event)
    {
        updateSelectedProduct(foodSlider, 1);
    }

    /**
     * Update toys in database
     * @param event mouse event
     */
    @FXML
    void toyClicked(MouseEvent event)
    {
        updateSelectedProduct(toySlider, 3);
    }

    /**
     * Update discount rate in database
     * @param event mouse event
     */
    @FXML
    void discountClicked(MouseEvent event)
    {
        // get the value of the slider
        double normalizedDiscountVal = discountSlider.getValue(); // this is between 0-100

        // normalize it to become between 0-1
        double discountVal = normalizedDiscountVal / 100.0;

        allProducts.get(4).set_agebased_disc_rate(discountVal);
        allProducts.get(4).setPrice(allProducts.get(0).getPrice());
        // update the database
        DatabaseConnection.updateProduct(allProducts.get(4));

        // update the prices with the values from database
        updateLabels();

        // reset the slider
        discountSlider.setValue(discountSlider.getMin());
    }
}
