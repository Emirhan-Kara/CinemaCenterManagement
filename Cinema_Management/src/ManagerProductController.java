import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class ManagerProductController {

    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button beverageButton;

    @FXML
    private Label beverageNewPrice;

    @FXML
    private Label beveragePrice;

    @FXML
    private Slider beverageSlider;

    @FXML
    private Button discountButton;

    @FXML
    private Label discountNewValue;

    @FXML
    private Label discountRate;

    @FXML
    private Slider discountSlider;

    @FXML
    private Button foodButton;

    @FXML
    private Label foodNewPrice;

    @FXML
    private Label foodPrice;

    @FXML
    private Slider foodSlider;

    @FXML
    private Button logout;

    @FXML
    private Button ticketButton;

    @FXML
    private Label ticketNewPrice;

    @FXML
    private Label ticketPrice;

    @FXML
    private Slider ticketSlider;

    @FXML
    private Button toyButton;

    @FXML
    private Label toyNewPrice;

    @FXML
    private Label toyPrice;

    @FXML
    private Slider toySlider;

    private List<Product> allProducts;

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


    private void updateLabels()
    {
        // set the existing prices to the related labels
        double ticketPriceDB = allProducts.get(0).getPrice();
        ticketPrice.setText(String.format("%.2f", ticketPriceDB) + " ₺");

        double beveragePriceDB = allProducts.get(1).getPrice();
        beveragePrice.setText(String.format("%.2f", beveragePriceDB) + " ₺");

        double foodPricePriceDB = allProducts.get(2).getPrice();
        foodPrice.setText(String.format("%.2f", foodPricePriceDB) + " ₺");

        double toyPriceDB = allProducts.get(3).getPrice();
        toyPrice.setText(String.format("%.2f", toyPriceDB) + " ₺");

        double discountRateDB = allProducts.get(0).get_agebased_disc_rate_(); // it is between 0 and 1
        // normalize it, between
        int normalizedDiscountRate = (int)(discountRateDB * 100);
        discountRate.setText("% " + String.valueOf(normalizedDiscountRate));
    }


    private void connectTextWithSlider(Slider slider, Label newPriceLabel)
    {
        // Bind the new price label to the slider's value
        newPriceLabel.textProperty().bind(slider.valueProperty().asString("%.2f"));
    }

    @FXML
    void backClicked(MouseEvent event) throws Exception
    {
        ManagerController.handleAction(event, null, "ManagerMainMenu.fxml");
    }

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "ManagerMainMenu.fxml");
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


    @FXML
    void ticketClicked(MouseEvent event)
    {
        updateSelectedProduct(ticketSlider, 0);
    }


    @FXML
    void beverageClicked(MouseEvent event)
    {
        updateSelectedProduct(beverageSlider, 1);
    }


    @FXML
    void foodClicked(MouseEvent event)
    {
        updateSelectedProduct(foodSlider, 2);
    }

    @FXML
    void toyClicked(MouseEvent event)
    {
        updateSelectedProduct(toySlider, 3);
    }


    @FXML
    void discountClicked(MouseEvent event)
    {
        // get the value of the slider
        double normalizedDiscountVal = discountSlider.getValue(); // this is between 0-100

        // normalize it to become between 0-1
        double discountVal = normalizedDiscountVal / 100.0;

        allProducts.get(0).set_agebased_disc_rate(discountVal);

        // update the database
        DatabaseConnection.updateProduct(allProducts.get(0));

        // update the prices with the values from database
        updateLabels();

        // reset the slider
        discountSlider.setValue(discountSlider.getMin());
    }
}
