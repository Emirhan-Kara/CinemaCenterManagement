import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for product buying scene
 * It extends CashierProperties class to access all the related data within the cashier scenes
 */
public class Scene4Controller extends CashierProperties
{
    /**
     * root for the scene
     */
    @FXML
    private AnchorPane rootScene;
    
    /**
     * enter click handler
     * @param event keyboard event
     * @throws Exception for laod scene
     */
    @FXML
    void enterClickedForNext(KeyEvent event) throws Exception
    {
        if (event.getCode() != KeyCode.ENTER)
            return;

            App.loadScene("cashierScene4.fxml");
    }

    /**
     * Label to display user's name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Label to display user's role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Button to navigate back to the previous screen.
     */
    @FXML
    private Button backButton;

    /**
     * Button to log out the user.
     */
    @FXML
    private Button logout;

    ///////////////////////////////

    /**
     * Label to display current beverage price.
     */
    @FXML
    private Label currentBeveragePrice;

    /**
     * Label to display current food price.
     */
    @FXML
    private Label currentFoodPrice;

    /**
     * Label to display current toy price.
     */
    @FXML
    private Label currentToyPrice;

    ///////////////////////////////

    /**
     * Button to decrease beverage quantity.
     */
    @FXML
    private Button minusBeverageButton;

    /**
     * Button to decrease food quantity.
     */
    @FXML
    private Button minusFoodButton;

    /**
     * Button to decrease toy quantity.
     */
    @FXML
    private Button minusToyButton;

    /**
     * Button to increase beverage quantity.
     */
    @FXML
    private Button plusBeverageButton;

    /**
     * Button to increase food quantity.
     */
    @FXML
    private Button plusFoodButton;

    /**
     * Button to increase toy quantity.
     */
    @FXML
    private Button plusToyButton;

    /**
     * Label to display the product price.
     */
    @FXML
    private Label productPriceLabel;

    /**
     * Label to display the product tax amount.
     */
    @FXML
    private Label productTaxLabel;

    /**
     * Label to display the selected beverage details.
     */
    @FXML
    private Label selectedBeverageLabel;

    /**
     * Label to display the selected food details.
     */
    @FXML
    private Label selectedFoodLabel;

    /**
     * Label to display the selected toy details.
     */
    @FXML
    private Label selectedToyLabel;

    /**
     * Label to display the ticket price.
     */
    @FXML
    private Label ticketPriceLabel;

    /**
     * Label to display the ticket tax amount.
     */
    @FXML
    private Label ticketTaxLabel;

    /**
     * Label to display the final price.
     */
    @FXML
    private Label finalPriceLabel;

    /**
     * Button to confirm purchase.
     */
    @FXML
    private Button buyButton;


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
     * Current number of beverages selected.
     */
    private int curBeverageNum = 0;

    /**
     * Current number of food items selected.
     */
    private int curFoodNum = 0;

    /**
     * Current number of toys selected.
     */
    private int curToyNum = 0;

    /**
     * Total price of selected products.
     */
    private double totalProductPrice = 0;

    /**
     * Total tax amount for selected products.
     */
    private double totalProductTax = 0;

    /**
     * Final price including products and tickets.
     */
    private double finalPrice = 0;

    /**
     * Current price of food.
     */
    private final double curFoodPrice = CashierProperties.currentProductPrices.get(1).getPrice();

    /**
     * Current price of beverages.
     */
    private final double curBeveragePrice = CashierProperties.currentProductPrices.get(2).getPrice();

    /**
     * Current price of toys.
     */
    private final double curToyPrice = CashierProperties.currentProductPrices.get(3).getPrice();

    /**
     * Stock quantity of food.
     */
    private final int stockFood = CashierProperties.currentProductPrices.get(1).getStock();

    /**
     * Stock quantity of beverages.
     */
    private final int stockBeverage = CashierProperties.currentProductPrices.get(2).getStock();

    /**
     * Stock quantity of toys.
     */
    private final int stockToy = CashierProperties.currentProductPrices.get(3).getStock();

    /**
     * Tax rate for products.
     */
    private final double productTaxRate = 0.1;

    /**
     * Total ticket price from the previous scene.
     */
    private final double ticketPrice = CashierProperties.totalTicketPrice;

    /**
     * Total ticket tax amount from the previous scene.
     */
    private final double ticketTax = CashierProperties.totalTicketTax;


    /**
     * Initializer to setup the scene
     */
    @FXML
    void initialize()
    {
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());
        
        ticketPriceLabel.setText(String.format("%.2f ₺", ticketPrice));
        ticketTaxLabel.setText(String.format("%.2f ₺",ticketTax));

        currentBeveragePrice.setText(String.format("%.2f ₺", curBeveragePrice));
        currentFoodPrice.setText(String.format("%.2f ₺", curFoodPrice));
        currentToyPrice.setText(String.format("%.2f ₺", curToyPrice));

        updateTotalProductPriceAndTax();
    }

    /**
     * Helper method to update the price and tax labels
     */
    private void updateAllLabels()
    {
        selectedBeverageLabel.setText(String.valueOf(curBeverageNum));
        selectedFoodLabel.setText(String.valueOf(curFoodNum));
        selectedToyLabel.setText(String.valueOf(curToyNum));

        productPriceLabel.setText(String.format("%.2f ₺", totalProductPrice));
        productTaxLabel.setText(String.format("%.2f ₺", totalProductTax));
        finalPriceLabel.setText(String.format("%.2f ₺", finalPrice));

    }

    /**
     * Helper method to update the ticket price
     */
    private void updateTotalProductPriceAndTax()
    {
        totalProductPrice = (curBeverageNum*curBeveragePrice) + (curFoodNum*curFoodPrice) + (curToyNum*curToyPrice);
        totalProductTax = totalProductPrice*productTaxRate;
        finalPrice = ticketPrice + ticketTax + totalProductPrice + totalProductTax;
        updateAllLabels();
    }

    /**
     * Event handler of beverage - button
     * @param event mouse event
     */
    @FXML
    void minusBeverage(MouseEvent event)
    {
        if (curBeverageNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curBeverageNum--;
        updateTotalProductPriceAndTax();
    }

    /**
     * Event handler of food - button
     * @param event mouse event
     */
    @FXML
    void minusFood(MouseEvent event)
    {
        if (curFoodNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curFoodNum--;
        updateTotalProductPriceAndTax();
    }

    /**
     * Event handler of toy - button
     * @param event mouse event
     */
    @FXML
    void minusToy(MouseEvent event)
    {
        if (curToyNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curToyNum--;
        updateTotalProductPriceAndTax();
    }

    /**
     * Event handler of beverage + button
     * @param event mouse event
     */
    @FXML
    void plusBeverage(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) ||curBeverageNum >= stockBeverage)
            return;
        curBeverageNum++;
        updateTotalProductPriceAndTax();
    }

    /**
     * Event handler of food + button
     * @param event mouse event
     */
    @FXML
    void plusFood(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) || curFoodNum >= stockFood)
            return;
        curFoodNum++;
        updateTotalProductPriceAndTax();
    }

    /**
     * Event handler of toy + button
     * @param event mouse event
     */
    @FXML
    void plusToy(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) || curToyNum >= stockToy)
            return;
        curToyNum++;
        updateTotalProductPriceAndTax();
    }


    /**
     * Event handler for clicking buy button
     * It finalizes the cashier scenes
     * It sends local variables to the parent class
     * Calls the method to create the bill
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    void buyClicked(MouseEvent event) throws Exception 
    {
        CashierProperties.soldBeverageNum = this.curBeverageNum;
        CashierProperties.soldFoodNum = this.curFoodNum;
        CashierProperties.soldToyNum = this.curToyNum;

        CashierProperties.totalProductPrice = this.totalProductPrice;
        CashierProperties.totalProductTax = this.totalProductTax;

        CashierProperties.createBillForScene5();
    }
}
