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

/**
 * Controller class for refund page
 */
public class RefundController
{

    /**
     * Label to display error messages.
     */
    @FXML
    private Label errorMessage;

    /**
     * Button to navigate back to the previous screen.
     */
    @FXML
    private Button backButton;

    /**
     * TextField for entering bill ID.
     */
    @FXML
    private TextField bill_id_input;

    /**
     * Label to display hall ID.
     */
    @FXML
    private Label hall_id_label;

    /**
     * Button to submit input data.
     */
    @FXML
    private Button inputButton;

    /**
     * Button to log out the user.
     */
    @FXML
    private Button logout;

    /**
     * Label to display user's name and surname.
     */
    @FXML
    private Label nameSurnameLabel;

    /**
     * Button to process product refunds.
     */
    @FXML
    private Button refundProduct;

    /**
     * Label to display user's role.
     */
    @FXML
    private Label roleLabel;

    /**
     * Label to display session ID.
     */
    @FXML
    private Label session_id_label;

    /**
     * Label to display the total refunded amount.
     */
    @FXML
    private Label totalRefundedLabel;

    /**
     * Label to display the total price.
     */
    @FXML
    private Label totalPriceLabel;


    /**
     * Handles the back button click event.
     * @param event keyboard event
     * @throws Exception for load scene
     */
    @FXML
    void backPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "AdminMainPage.fxml");
    }

    /**
     * Handles the logout button click event.
     * @param event mouse event
     * @throws Exception for laod scene
     */
    @FXML
    void logoutClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "Login.fxml");
    }

    /**
     * Handles the logout button key press event.
     * @param event keyboard event
     * @throws Exception for laod scene
     */
    @FXML
    void logoutPressed(KeyEvent event) throws Exception {
        ManagerController.handleAction(null, event, "Login.fxml");
    }

    /**
     * Handles the back button click event.
     * @param event mouse event
     * @throws Exception for load scene
     */
    @FXML
    void backClicked(MouseEvent event) throws Exception {
        ManagerController.handleAction(event, null, "AdminMainPage.fxml");
    }

    /**
     * String to Object map that holds all the related data within the receipt
     */
    private Map<String, Object> billMap;

    /**
     * This method is automatically called after the fxml file has been loaded.
     * It initializes the table columns and binds them to the data.
     * load tickettable and producttable with the data from the database
     */
    @FXML
    void initialize()
    {
        clearData();
        errorMessage.setText("");
        refundProduct.setDisable(true);
        nameSurnameLabel.setText(LoginController.loggedEmployee.getFirstname() + " " + LoginController.loggedEmployee.getLastname());
        roleLabel.setText(LoginController.loggedEmployee.getUserRole());

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

    /**
     * Product object to store selected product
     */
    private Product selectedProduct;

    /**
     * Ticket object to store the selected ticket
     */
    private Ticket selectedTicket;

    /**
     * fill in the bill information labels with the data
     */
    private void fillBillInfoLabels()
    {
        totalPriceLabel.setText(String.valueOf(this.totalPrice) + " ₺");
        hall_id_label.setText(String.valueOf(this.hall_id));
        session_id_label.setText(String.valueOf(this.session_id));
    }

    /**
     * Handles the input button click event.
     * Checks for a valid billd ID and fills the bill information labels.
     * @param event mouse event
     * @throws Exception for laod scene 
     */
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

    /**
     * Handles the enter key press event.
     * Checks for a valid billd ID and fills the bill information labels.
     * @param event keyboard event
     * @throws Exception for load scene
     */
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

    /**
     * Handles the refund product button click event.
     * Refunds the selected product or ticket and updates the tables.
     * @param event mouse event
     */
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


    /**
     * Unique identifier for the bill.
     */
    private int bill_id;

    /**
     * Total price of the bill.
     */
    private double totalPrice;

    /**
     * Identifier for the hall associated with the bill.
     */
    private int hall_id;

    /**
     * Identifier for the session associated with the bill.
     */
    private int session_id;

    /**
     * Map storing product quantities by product ID.
     */
    private Map<Integer, Integer> productQuantities;

    /**
     * Map storing product prices by product ID.
     */
    private Map<Integer, Double> productPrices;

    /**
     * Map storing discounted ticket customers by ticket ID.
     */
    private Map<Integer, String> discountedTicketCustomers;

    /**
     * Map storing normal ticket customers by ticket ID.
     */
    private Map<Integer, String> normalTicketCustomers;

    /**
     * List of tickets associated with the bill.
     */
    private ArrayList<Ticket> ticketList = new ArrayList<>();

    /**
     * List of products associated with the bill.
     */
    private ArrayList<Product> productList = new ArrayList<>();


    /**
     * Searches the database for the given bill ID and retrieves the bill data.
     * Fills the ticket and product lists with the data.
     * @param bill_id primary key for bills
     */
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

    /**
     * Table for displaying tickets.
     */
    @FXML
    private TableView<Ticket> ticketsTable;

    /**
     * Column for seat numbers in tickets table.
     */
    @FXML
    private TableColumn<Ticket, Integer> seatNumber_col;

    /**
     * Column for seat prices per unit in tickets table.
     */
    @FXML
    private TableColumn<Ticket, Double> seatsPricePerUnit_col;

    /**
     * Column for product prices per unit in products table.
     */
    @FXML
    private TableColumn<Product, Double> pricePerUnit_col;

    /**
     * Column for product IDs in products table.
     */
    @FXML
    private TableColumn<Product, Integer> productID_col;

    /**
     * Table for displaying products.
     */
    @FXML
    private TableView<Product> productsTable;

    /**
     * Total refunded money amount.
     */
    private double totalRefundedMoney;



    /**
     * Clears the data and resets the fields.
     */
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

    /**
     * Fills the products table with the data from the productList.
     */
    private void fillProductsTable()
    {
        productsTable.getItems().clear();

        productsTable.getItems().addAll(productList);
    }

    /**
     * Fills the seats table with the data from the ticketList.
     */
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

    /**
     * Creates updated maps for the product quantities and ticket customers.
     * Updates the HTML receipt in the database and ensures the local HTML file is synchronized.
     * 
     */
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
    * Generates and saves the HTML receipt to a file.
    * The file is saved in the same directory as the program.
    * the HTML content is saved to the database.
    * @throws IOException if the file cannot be written. 
    */
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
