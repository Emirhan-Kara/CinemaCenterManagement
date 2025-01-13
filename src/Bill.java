import java.sql.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Bill class that holds the values related with the receipt
 */
public class Bill
{
    /**
     * Bill id is primary key in database
     */
    public int id; // Bill ID to save and check

    /**
     * Hashmap with the product id, product quantity mapping
     */
    public Map<Integer, Integer> productQuantities; // Product ID -> desired Quantity

    /**
     * Hashmap with the product id, product price mapping
     */
    public Map<Integer, Double> productPrices;

    /**
     * Hashmap with the seat number, customer name mapping for discounted tickets
     */
    public Map<Integer, String> discountedTicketCustomers; // Seat Number -> Customer Name

    /**
     * Hashmap with the seat number, customer name mapping for non-discounter tickets
     */
    public Map<Integer, String> normalTicketCustomers; // Seat Number -> Customer Name

    /**
     * Total price of the bill
     */
    public double totalPrice; // Total price of the bill
    
    /**
     * Hall id of the bought tickets
     */
    public int hallID;

    /**
     * Session id of the bought tickets
     */
    public int sessionID;

    /**
     * Default cosntructor to initilze the parameters
     */
    public Bill() {
        this.productQuantities = new HashMap<>();
        this.discountedTicketCustomers = new HashMap<>();
        this.normalTicketCustomers = new HashMap<>();
        this.productPrices = new HashMap<>();
        this.totalPrice = 0.0;
        this.id = createBillInDatabase(); // Generate a new Bill ID and save to DB
    }

    /**
     * Creates a new bill in the database and retrieves the generated bill ID.
     * Bill id is a primary key in the database, used to identify the bills
     * 
     * @return The generated Bill ID.
     */
    private int createBillInDatabase() {
        String query = "INSERT INTO bills (total_price) VALUES (0)"; // Placeholder total_price = 0

        // try() block returns bill id by getting generated keys
        try (PreparedStatement pstmt = DatabaseConnection.connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated Bill ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Failed to create a new bill in the database.");
    }

    /**
     * Adds tickets (normal or discounted) to the bill.
     * Also fills the discounted and normal ticket maps while iterating
     *
     * @param seatCustomerMap A map of seat numbers to customer names.
     * @param hallId          The Hall ID.
     * @param sessionId       The Session ID.
     * @param isDiscounted    True if the tickets are discounted, false otherwise.
     */
    public void addTickets(Map<Integer, String> seatCustomerMap, int hallId, int sessionId, boolean isDiscounted) {

        this.hallID = hallId;
        this.sessionID = sessionId;

        int productId = isDiscounted ? 5 : 1; // 5 for discounted tickets, 1 for normal tickets
        int quantity = seatCustomerMap.size();

        for (Map.Entry<Integer, String> entry : seatCustomerMap.entrySet()) {
            int seatNumber = entry.getKey();
            String customerName = entry.getValue();

            if (isDiscounted)
                this.discountedTicketCustomers.put(seatNumber, customerName);
            else
                this.normalTicketCustomers.put(seatNumber, customerName);

            // Update seat assignment in the Hall table
            int seatValue = isDiscounted ? -id : id; // -id for discounted tickets, id for normal tickets
            DatabaseConnection.updateSeatInHall(hallId, sessionId, seatNumber, seatValue);
        }

        // Add the tickets quatities to the map
        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);

        // update the total price
        double productPrice = DatabaseConnection.getProductPrice(productId);
        totalPrice += productPrice * quantity;
        productPrices.put(productId, productPrice);

        // Update product stock and sales
        DatabaseConnection.updateProductStockAndSales_DB(productId, quantity);

        // Increment the number of booked seats
        DatabaseConnection.incrementBookedSeats(hallId, sessionId, quantity);
    }

    /**
     * Adds the general product (non-ticket) to the bill.
     *
     * @param productId The product ID.
     * @param quantity  The quantity of the product.
     */
    public void addProduct(int productId, int quantity) {
        if (productId == 1 || productId == 5) {
            throw new IllegalArgumentException("Use addTicket for tickets.");
        }

        // Add product to the map
        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);

        // Update the total price
        double productPrice = DatabaseConnection.getProductPrice(productId);
        totalPrice += productPrice * quantity;

        productPrices.put(productId, productPrice);

        // Don't forget stock and sales in the database
        DatabaseConnection.updateProductStockAndSales_DB(productId, quantity);
    }


    /**
     * Generates an HTML file for the bill, saves it to the database, and returns the file URI.
     *
     * @return The URI of the generated HTML file.
     */
    public String generateAndSaveHTML()
    {
        String filePath = "bill_" + id + ".html"; // File path
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            // Generate HTML content
            StringBuilder html = new StringBuilder();

            html.append("<html>\n");
            html.append("<head>\n<title>Bill ").append(id).append("</title>\n</head>\n");
            html.append("<body>\n");
            html.append("<h1>Bill ID: ").append(id).append("</h1>\n");
            html.append("<h2>Total Price: $").append(totalPrice).append("</h2>\n");
            html.append("<h3>Hall ID: ").append(hallID).append("</h3>\n");
            html.append("<h3>Session ID: ").append(sessionID).append("</h3>\n");

            html.append("<h3>Products:</h3>\n<ul>\n");
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
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
            for (Map.Entry<Integer, String> entry : discountedTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>\n");
            }
            html.append("</ul>\n");

            html.append("<h3>Normal Tickets:</h3>\n<ul>\n");
            for (Map.Entry<Integer, String> entry : normalTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>\n");
            }
            html.append("</ul>\n");

            html.append("</body>\n");
            html.append("</html>\n");

            // Write to file
            writer.write(html.toString());

            // Save the HTML content to the database
            DatabaseConnection.saveHTMLToDB(id, html.toString());

            // Return the file URI for WebView or other purposes
            return file.toURI().toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Saves the total price to the database, and calls generateAndSaveHTML method.
     * Basically, creates a bill and puts it to the database
     * @return file URI of the HTML receipt
     */
    public String finalizeBill() {
        String query = "UPDATE bills SET total_price = ? WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.connection.prepareStatement(query)) {
            pstmt.setDouble(1, totalPrice);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return generateAndSaveHTML(); /////NEW 🟡🔺
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to finalize the bill in the database.");
        }
    }

}
