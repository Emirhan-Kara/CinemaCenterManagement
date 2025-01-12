import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class RefundController {

    @FXML
    private Label errorMessage;

    @FXML
    private Button backButton;

    @FXML
    private TextField bill_id_input;

    @FXML
    private Label hall_id_label;

    @FXML
    private Button inputButton;

    @FXML
    private Button logout;

    @FXML
    private Label nameSurnameLabel;

    @FXML
    private Button refundProduct;

    @FXML
    private Label roleLabel;


    @FXML
    private Label session_id_label;

    @FXML
    private Label totalRefundedLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    void backPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }

    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "Login.fxml");
    }

    @FXML
    void backClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    private Map<String, Object> billMap;

    @FXML
    void initialize()
    {
        clearData();
        errorMessage.setText("");
        refundProduct.setDisable(true);
        //nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        //roleLabel.setText(LoginController.loggedEmployee.getUserRole());

        // Bind seatNumber_col to Ticket's seatNumber
        seatNumber_col.setCellValueFactory(cellData -> 
            new SimpleIntegerProperty(cellData.getValue().getSeatNumber()).asObject()
        );

        // Bind seatsPricePerUnit_col to Ticket's seatPrice
        seatsPricePerUnit_col.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getSeatPrice()).asObject()
        );

        ticketsTable.getItems().setAll(ticketList);



        productID_col.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().getId()).asObject()
        );

        pricePerUnit_col.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject()
        );

        productsTable.getItems().setAll(productList);



        // Bind the ticket selection to the table
        ticketsTable.getSelectionModel().selectedItemProperty().addListener((_, _, clickedTicket) -> {
            if (clickedTicket != null)
            {
                if (selectedProduct != null)
                    totalRefundedMoney = selectedProduct.getPrice() + clickedTicket.getSeatPrice();
                else
                    totalRefundedMoney = clickedTicket.getSeatPrice();
                
                totalRefundedLabel.setText(String.valueOf(totalRefundedMoney) + " ₺");

                refundProduct.setDisable(false);
                selectedTicket = clickedTicket;
            }
        });

        // Bind the product selection to the table
        productsTable.getSelectionModel().selectedItemProperty().addListener((_, _, clickedProduct) -> {
            if (clickedProduct != null)
            {
                if (selectedTicket != null)
                    totalRefundedMoney = selectedTicket.getSeatPrice() + clickedProduct.getPrice();
                else
                    totalRefundedMoney = clickedProduct.getPrice();
   
                totalRefundedLabel.setText(String.valueOf(totalRefundedMoney) + " ₺");

                refundProduct.setDisable(false);
                selectedProduct = clickedProduct;
            }
        });
    }

    private Product selectedProduct;
    private Ticket selectedTicket;

    private void fillBillInfoLabels()
    {
        totalPriceLabel.setText(String.valueOf(this.totalPrice) + " ₺");
        hall_id_label.setText(String.valueOf(this.hall_id));
        session_id_label.setText(String.valueOf(this.session_id));
    }

    @FXML
    void inputButtonClicked(MouseEvent event)
    {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        try
        {
            int billId = Integer.parseInt(bill_id_input.getText());
            searchAndGetBillID(billId);
            fillBillInfoLabels();
        }
        catch (NumberFormatException e)
        {
            errorMessage.setText("Enter a Valid Bill ID !");
            System.out.println("Please enter a valid integer.");
        }
    }

    @FXML
    void enterClicked(KeyEvent event)
    {
        if (event.getCode() != KeyCode.ENTER)
            return;

        try
        {
            int billId = Integer.parseInt(bill_id_input.getText());
            searchAndGetBillID(billId);
            fillBillInfoLabels();
        }
        catch (NumberFormatException e)
        {
            errorMessage.setText("Enter a Valid Bill ID !");
            System.out.println("Please enter a valid integer.");
        }
    }

    @FXML
    void refundClicked(MouseEvent event)
    {
        if (selectedTicket != null)
        {
            DatabaseConnection.ticketRefund(this.hall_id, this.session_id, selectedTicket.getSeatNumber(), selectedTicket.getSeatPrice(), selectedTicket.getProductid(), 1);
            
            boolean isDeleted = ticketList.removeIf(ticket -> ticket.getSeatNumber() == selectedTicket.getSeatNumber());
            if(isDeleted)
            {
                System.out.println("SİLİNDİİ!");
                fillSeatsTable();
            }
            else
            {
                System.out.println("SİLİNEMEDİ :((");
                return;
            }   
        }

        if (selectedProduct != null)
        {
            DatabaseConnection.updateStocksBCofRefund(selectedProduct.getId(), selectedProduct.getPrice(), 1);

            // Remove only the first product with the matching ID
            for (Iterator<Product> onlyOne = productList.iterator(); onlyOne.hasNext();)
            {
                Product product = onlyOne.next();

                if (product.getId() == selectedProduct.getId())
                {
                    onlyOne.remove(); // Remove the current product
                    System.out.println("Silindi product");
                    fillProductsTable();
                    break; // Exit the loop after removing one product
                }
            }
        }

        createUpdatedMaps();

        fillBillInfoLabels();

        int billId = Integer.parseInt(bill_id_input.getText());
        searchAndGetBillID(billId);
        fillBillInfoLabels();
    }


    private int bill_id;
    private double totalPrice;
    private int hall_id;
    private int session_id;
    private Map<Integer, Integer> productQuantities;
    private Map<Integer, Double> productPrices;
    private Map<Integer, String> discountedTicketCustomers;
    private Map<Integer, String> normalTicketCustomers;

    

    private ArrayList<Ticket> ticketList = new ArrayList<>();
    private ArrayList<Product> productList = new ArrayList<>();

    @SuppressWarnings("unchecked")
    private void searchAndGetBillID(int bill_id)
    {
        clearData();
        billMap = DatabaseConnection.getReceiptfrDB(bill_id);

        if (billMap.size() <= 0) {
            errorMessage.setText("Invalid Bill ID !");
            return;
        }

        this.bill_id = (int) billMap.get("billId");
        this.totalPrice = (double) billMap.get("totalPrice");
        this.hall_id = (int) billMap.get("hallID");
        this.session_id = (int) billMap.get("sessionID");
        this.productQuantities = (Map<Integer, Integer>) billMap.get("productQuantities");
        this.productPrices = (Map<Integer, Double>) billMap.get("productPrices");
        this.discountedTicketCustomers = (Map<Integer, String>) billMap.get("discountedTicketCustomers");
        this.normalTicketCustomers = (Map<Integer, String>) billMap.get("normalTicketCustomers");

        // get the tickets and fill the tickets table
        for (Map.Entry<Integer, String> entry : this.discountedTicketCustomers.entrySet())
        {
            int seat = entry.getKey();
            double price = productPrices.get(5);
            ticketList.add(new Ticket(seat, price, 5));
        }
        for (Map.Entry<Integer, String> entry : this.normalTicketCustomers.entrySet())
        {
            int seat = entry.getKey();
            double price = productPrices.get(1);
            ticketList.add(new Ticket(seat, price, 1));
        }

        fillSeatsTable();
        

        // get the products and fill the products table
        for (Map.Entry<Integer, Integer> entry : this.productQuantities.entrySet())
        {
            int prod_id = entry.getKey();
            if (prod_id == 1 || prod_id == 5)
                continue;
            
            int quantity = entry.getValue();
            double price = productPrices.get(prod_id);

            // itereta as the quantity of the element and create product objects and put them to the list
            for (int i = 0; i < quantity; i++)
            {
                productList.add(new Product(prod_id, price));
            }
        }

        fillProductsTable();
    }

    @FXML
    private TableView<Ticket> ticketsTable;

    @FXML
    private TableColumn<Ticket, Integer> seatNumber_col;
    @FXML
    private TableColumn<Ticket, Double> seatsPricePerUnit_col;



    @FXML
    private TableColumn<Product, Double> pricePerUnit_col;

    @FXML
    private TableColumn<Product, Integer> productID_col;

    @FXML
    private TableView<Product> productsTable;

    private double totalRefundedMoney;


    private void clearData()
    {
        refundProduct.setDisable(true);
        this.totalRefundedMoney = 0.0;
        this.totalRefundedLabel.setText("0 ₺");
        // Reset fields
        this.bill_id = 0;
        this.totalPrice = 0.0;
        this.hall_id = 0;
        this.session_id = 0;
        this.productQuantities = null;
        this.productPrices = null;
        this.discountedTicketCustomers = null;
        this.normalTicketCustomers = null;
        
        this.selectedTicket = null;
        this.selectedProduct = null;


        // Clear ticketList
        if (ticketList != null)
            ticketList.clear();
        if (productList != null)
            productList.clear();

    
        // Clear table
        ticketsTable.getItems().clear();
        productsTable.getItems().clear();
    
        // Clear error message
        errorMessage.setText("");
    }

    private void fillProductsTable()
    {
        productsTable.getItems().clear();

        productsTable.getItems().addAll(productList);
    }

    private void fillSeatsTable()
    {
        // Clear existing items in the table
        ticketsTable.getItems().clear();

        // Add the updated ticketList to the table
        ticketsTable.getItems().addAll(ticketList);
    }


    private Map<Integer, Integer> updatedProductQuantities = new HashMap<>();
    private Map<Integer, String> updatedDiscountedTicketCustomers = new HashMap<>();
    private Map<Integer, String> updatedNormalTicketCustomers = new HashMap<>();

    private void createUpdatedMaps() {
        // Reset updated maps
        updatedProductQuantities.clear();
        updatedDiscountedTicketCustomers.clear();
        updatedNormalTicketCustomers.clear();

        // Populate updatedProductQuantities from productList
        for (Product product : productList) {
            int productId = product.getId();
            updatedProductQuantities.put(productId, 
                updatedProductQuantities.getOrDefault(productId, 0) + 1);
        }

        // Populate updatedDiscountedTicketCustomers and updatedNormalTicketCustomers from ticketList
        for (Ticket ticket : ticketList) {
            int seatNumber = ticket.getSeatNumber();
            int productId = ticket.getProductid();

            updatedProductQuantities.put(productId, updatedProductQuantities.getOrDefault(productId, 0) + 1);

            if (productId == 5) {
                updatedDiscountedTicketCustomers.put(seatNumber, discountedTicketCustomers.get(seatNumber));
            } else if (productId == 1) {
                updatedNormalTicketCustomers.put(seatNumber, normalTicketCustomers.get(seatNumber));
            }
        }

        generateAndSaveHTML();
    }

    /**
     * Updates the HTML receipt in the database and ensures the local HTML file is synchronized.
     
    public void updateBillhtml() {
        // Prepare updated values for the database
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("totalPrice", this.totalPrice - this.totalRefundedMoney);
        updatedValues.put("hallID", this.session_id);
        updatedValues.put("sessionID", this.session_id);
        updatedValues.put("productQuantities", updatedProductQuantities);
        updatedValues.put("productPrices", productPrices);
        updatedValues.put("discountedTicketCustomers", updatedDiscountedTicketCustomers);
        updatedValues.put("normalTicketCustomers", updatedNormalTicketCustomers);

        // Call the database method to generate and update the HTML content
        boolean dbUpdated = DatabaseConnection.updateHTMLReceiptInDB(this.bill_id, updatedValues);

        if (!dbUpdated) {
            throw new RuntimeException("Failed to update the HTML receipt in the database.");
        }    
    } */
    public void generateAndSaveHTML()
    {
        String filePath = "bill_" + this.bill_id + ".html"; // File path
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            // Generate HTML content
            StringBuilder html = new StringBuilder();

            html.append("<html>\n");
            html.append("<head>\n<title>Bill ").append(this.bill_id).append("</title>\n</head>\n");
            html.append("<body>\n");
            html.append("<h1>Bill ID: ").append(this.bill_id).append("</h1>\n");
            html.append("<h2>Total Price: $").append(totalPrice - totalRefundedMoney).append("</h2>\n");
            html.append("<h3>Hall ID: ").append(this.hall_id).append("</h3>\n");
            html.append("<h3>Session ID: ").append(this.session_id).append("</h3>\n");

            html.append("<h3>Products:</h3>\n<ul>\n");
            for (Map.Entry<Integer, Integer> entry : updatedProductQuantities.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                double price = productPrices.getOrDefault(productId, 0.0);
                html.append("<li>Product ID: ").append(productId)
                    .append(", Quantity: ").append(quantity)
                    .append(", Price per Unit: $").append(price)
                    .append("</li>\n");
            }
            html.append("</ul>\n");

            html.append("<h3>Discounted Tickets:</h3>\n<ul>\n");
            for (Map.Entry<Integer, String> entry : updatedDiscountedTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>\n");
            }
            html.append("</ul>\n");

            html.append("<h3>Normal Tickets:</h3>\n<ul>\n");
            for (Map.Entry<Integer, String> entry : updatedNormalTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>\n");
            }
            html.append("</ul>\n");

            html.append("</body>\n");
            html.append("</html>\n");

            // Write to file
            writer.write(html.toString());

            // Save the HTML content to the database
            DatabaseConnection.saveHTMLToDB(this.bill_id, html.toString());
            DatabaseConnection.updatetotalpricebill(bill_id, totalPrice - totalRefundedMoney);

            // Return the file URI for WebView or other purposes
            //return file.toURI().toString();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            //return null;
        }
    }


}
