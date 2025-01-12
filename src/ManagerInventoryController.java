import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ManagerInventoryController {

    @FXML
    private Label roleLabel;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button backButton;

    @FXML
    private Button beverageButton;

    @FXML
    private Label beverageNewStock;

    @FXML
    private Slider beverageSlider;

    @FXML
    private Label beverageStock;

    @FXML
    private Button foodButton;

    @FXML
    private Label foodNewStock;

    @FXML
    private Slider foodSlider;

    @FXML
    private Label foodStock;

    @FXML
    private Button logout;

    @FXML
    private Button toyButton;

    @FXML
    private Label toyNewStock;

    @FXML
    private Slider toySlider;

    @FXML
    private Label toyStock;

    private List<Product> allProducts;

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

    private void connectTextWithSlider(Slider slider, Label newStockLabel)
    {
        // Bind the new stock label to the slider's value
        newStockLabel.textProperty().bind(slider.valueProperty().asString("%.0f"));
    }

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



    @FXML
    void beverageClicked(MouseEvent event)
    {
        
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(beverageSlider, 2);
    }

    @FXML
    void foodClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(foodSlider, 1);
    }

    @FXML
    void toyClicked(MouseEvent event)
    {
        if(event.getButton() != MouseButton.PRIMARY)
            return;

        updateSelectedProduct(toySlider, 3);
    }

}
