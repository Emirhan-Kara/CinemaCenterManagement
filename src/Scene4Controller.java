import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Scene4Controller extends CashierProperties
{
    @FXML
    private AnchorPane rootScene;
    
    @FXML
    void enterClickedForNext(KeyEvent event) throws Exception
    {
        if (event.getCode() != KeyCode.ENTER)
            return;

            App.loadScene("cashierScene4.fxml");
    }

    @FXML
    private Label nameSurnameLabel;
    
    @FXML
    private Label roleLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button logout;

    ///////////////////////////////
    @FXML
    private Label currentBeveragePrice;

    @FXML
    private Label currentFoodPrice;

    @FXML
    private Label currentToyPrice;

    ///////////////////////////////

    @FXML
    private Button minusBeverageButton;

    @FXML
    private Button minusFoodButton;

    @FXML
    private Button minusToyButton;

    @FXML
    private Button plusBeverageButton;

    @FXML
    private Button plusFoodButton;

    @FXML
    private Button plusToyButton;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label productTaxLabel;


    @FXML
    private Label selectedBeverageLabel;

    @FXML
    private Label selectedFoodLabel;

    @FXML
    private Label selectedToyLabel;

    @FXML
    private Label ticketPriceLabel;

    @FXML
    private Label ticketTaxLabel;
    
    @FXML
    private Label finalPriceLabel;

    @FXML
    private Button buyButton;

    @FXML
    void backPressed(KeyEvent event) throws Exception
    {
        ManagerController.handleAction(null, event, "cashierScene2_3.fxml");
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
        ManagerController.handleAction(event, null, "cashierScene2_3.fxml");
    }


    private int curBeverageNum = 0;
    private int curFoodNum = 0;
    private int curToyNum = 0;

    private double totalProductPrice = 0;
    private double totalProductTax = 0;
    private double finalPrice = 0;

    
    private final double curFoodPrice = CashierProperties.currentProductPrices.get(1).getPrice();
    private final double curBeveragePrice = CashierProperties.currentProductPrices.get(2).getPrice();
    private final double curToyPrice = CashierProperties.currentProductPrices.get(3).getPrice();

    // dont let them exceed the stocks
    private final int stockFood = CashierProperties.currentProductPrices.get(1).getStock();
    private final int stockBeverage = CashierProperties.currentProductPrices.get(2).getStock();
    private final int stockToy = CashierProperties.currentProductPrices.get(3).getStock();

    private final double productTaxRate = 0.1;

    // önceki sceneden ticket için olan fiyatları alıp koy
    private final double ticketPrice = CashierProperties.totalTicketPrice;
    private final double ticketTax = CashierProperties.totalTicketTax;
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

    private void updateAllLabels()
    {
        selectedBeverageLabel.setText(String.valueOf(curBeverageNum));
        selectedFoodLabel.setText(String.valueOf(curFoodNum));
        selectedToyLabel.setText(String.valueOf(curToyNum));

        productPriceLabel.setText(String.format("%.2f ₺", totalProductPrice));
        productTaxLabel.setText(String.format("%.2f ₺", totalProductTax));
        finalPriceLabel.setText(String.format("%.2f ₺", finalPrice));

    }

    private void updateTotalProductPriceAndTax()
    {
        totalProductPrice = (curBeverageNum*curBeveragePrice) + (curFoodNum*curFoodPrice) + (curToyNum*curToyPrice);
        totalProductTax = totalProductPrice*productTaxRate;
        finalPrice = ticketPrice + ticketTax + totalProductPrice + totalProductTax;
        updateAllLabels();
    }

    @FXML
    void minusBeverage(MouseEvent event)
    {
        if (curBeverageNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curBeverageNum--;
        updateTotalProductPriceAndTax();
    }

    @FXML
    void minusFood(MouseEvent event)
    {
        if (curFoodNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curFoodNum--;
        updateTotalProductPriceAndTax();
    }

    @FXML
    void minusToy(MouseEvent event)
    {
        if (curToyNum < 1 || !event.getButton().equals(MouseButton.PRIMARY))
            return;
        curToyNum--;
        updateTotalProductPriceAndTax();
    }

    @FXML
    void plusBeverage(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) ||curBeverageNum >= stockBeverage)
            return;
        curBeverageNum++;
        updateTotalProductPriceAndTax();
    }

    @FXML
    void plusFood(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) || curFoodNum >= stockFood)
            return;
        curFoodNum++;
        updateTotalProductPriceAndTax();
    }

    @FXML
    void plusToy(MouseEvent event)
    {
        if (!event.getButton().equals(MouseButton.PRIMARY) || curToyNum >= stockToy)
            return;
        curToyNum++;
        updateTotalProductPriceAndTax();
    }


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
