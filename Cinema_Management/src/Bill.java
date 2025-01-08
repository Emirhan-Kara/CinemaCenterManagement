import java.sql.*;
import java.util.*;
import java.io.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Bill {
    public int id; // Bill ID to save and check
    public Map<Integer, Integer> productQuantities; // Product ID -> desired Quantity
    public Map<Integer, Double> productPrices;
    public Map<Integer, String> discountedTicketCustomers; // Seat Number -> Customer Name
    public Map<Integer, String> normalTicketCustomers; // Seat Number -> Customer Name

    public double totalPrice; // Total price of the bill
    //🔺🟡
    public int hallID;
    public int sessionID;


    // Constructor
    public Bill(Map<Integer, String> discountedTicketCustomers, Map<Integer, String> normalTicketCustomers) {
        this.productQuantities = new HashMap<>();
        this.discountedTicketCustomers =discountedTicketCustomers;
        this.normalTicketCustomers = normalTicketCustomers;
        this.totalPrice = 0.0;
        this.id = createBillInDatabase(); // Generate a new Bill ID and save to DB
    }

    /**
     * Creates a new bill in the database and retrieves the generated bill ID.
     *
     * @return The generated Bill ID.
     */
    private int createBillInDatabase() {
        String query = "INSERT INTO Bill (total_price) VALUES (0)"; // Placeholder total_price = 0

        //try() block returns bill id by getting generated keys
        try (PreparedStatement pstmt = DatabaseConnection.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
     * Updates the Hall table for seat assignments and populates the `discountedTicketCustomers` map.
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

            // Update seat assignment in the Hall table
            int seatValue = isDiscounted ? -id : id; // -id for discounted tickets, id for normal tickets
            DatabaseConnection.updateSeatInHall(hallId, sessionId, seatNumber, seatValue);            
        }

        // Add the tickets quatities to the  map
        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);

        //update the total price
        double productPrice = DatabaseConnection.getProductPrice(productId);
        totalPrice += productPrice * quantity;
        productPrices.put(productId,productPrice);

        // Update product stock and sales
        DatabaseConnection.updateProductStockAndSales_DB(productId, quantity);

        // Increment the number of booked seats
        DatabaseConnection.incrementBookedSeats(hallId, sessionId, quantity);
    }

    /**AGAIN FIRST ENABLE CUSTOMER TO APPROVE THE BILL
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

        productPrices.put(productId,productPrice);

        //Don't forget stock and sales in the database
        DatabaseConnection.updateProductStockAndSales_DB(productId, quantity);
    }

     /**
     * Generates an HTML file for the bill, saves it to the database, and returns the file URI.
     *
     * @return The URI of the generated HTML file.
     */
    public String generateAndSaveHTML() {
        String filePath = "bill_" + id + ".html"; // File path
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Generate HTML content
            StringBuilder html = new StringBuilder();

            html.append("<html>");
            html.append("<head><title>Bill ").append(id).append("</title></head>");
            html.append("<body>");
            html.append("<h1>Bill ID: ").append(id).append("</h1>");
            html.append("<h2>Total Price: $").append(totalPrice).append("</h2>");
            html.append("<h3>Hall ID: ").append(hallID).append("</h3>");
            html.append("<h3>Session ID: ").append(sessionID).append("</h3>");

            html.append("<h3>Products:</h3><ul>");
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();
                double price = productPrices.getOrDefault(productId, 0.0);
                html.append("<li>Product ID: ").append(productId)
                    .append(", Quantity: ").append(quantity)
                    .append(", Price per Unit: $").append(price)
                    .append("</li>");
            }
            html.append("</ul>");

            html.append("<h3>Discounted Tickets:</h3><ul>");
            for (Map.Entry<Integer, String> entry : discountedTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>");
            }
            html.append("</ul>");


            html.append("<h3>Normal Tickets:</h3><ul>");
            for (Map.Entry<Integer, String> entry : normalTicketCustomers.entrySet()) {
                html.append("<li>Seat Number: ").append(entry.getKey())
                    .append(", Customer Name: ").append(entry.getValue()).append("</li>");
            }


            html.append("</ul>");
            html.append("</body>");
            html.append("</html>");

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
     * Save the total price of the bill to the database. You know the id already, 
     * it is returned by the method at top.
     */
    public String finalizeBill() {
        String query = "UPDATE Bill SET total_price = ? WHERE id = ?";
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

    /**
     * Adds normal tickets to the bill after ensuring the customer's decision.
     * Updates the Hall table for seat assignments and increments the number of booked seats.
     * Stores seat numbers in a map with null values since customer names are not needed.
     *
     * @param quantity    The quantity of tickets.
     * @param seatNumbers The list of seat numbers.
     * @param hallId      The Hall ID.
     * @param sessionId   The Session ID.
     */
    /* 
    public void addNormalTicket(int quantity, List<Integer> seatNumbers, int hallId, int sessionId) {
        if (seatNumbers.size() != quantity) {
            throw new IllegalArgumentException("The number of seats must match the quantity.");
        }

        int productId = 1; // Normal ticket product ID

        // Update seat assignments in the Hall table
        for (int seatNumber : seatNumbers) {
            DatabaseConnection.updateSeatInHall(hallId, sessionId, seatNumber, id); // Normal ticket = positive bill ID
            discountedTicketCustomers.put(seatNumber, null); // Store seat number with null value
        }

        // Increment the number of booked seats
        DatabaseConnection.incrementBookedSeats(hallId, sessionId, quantity);

        // Add the tickets to the product quantities map
        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);

        // Update the total price
        double productPrice = DatabaseConnection.getProductPrice(productId);
        totalPrice += productPrice * quantity;

        // Update product stock and sales in the database
        DatabaseConnection.updateProductStockAndSales_DB(productId, quantity);
    }


    /**
     * Adds discounted tickets with customer names to the bill.
     *
     * @param seatNumbers   The list of seat numbers for the tickets.
     * @param hallId        The Hall ID.
     * @param sessionId     The Session ID.
     * @param customerNames The list of customer names corresponding to seat numbers.
     

    public void addDiscountedTickets(List<Integer> seatNumbers, int hallId, int sessionId, List<String> customerNames) {
        if (seatNumbers.size() != customerNames.size()) {
            throw new IllegalArgumentException("Seat numbers and customer names must have the same size.");
        }

        int quantity = seatNumbers.size();

        for (int i = 0; i < seatNumbers.size(); i++) {
            int seatNumber = seatNumbers.get(i);
            String customerName = customerNames.get(i);

            // Add seat number and customer name to the map
            discountedTicketCustomers.put(seatNumber, customerName);

            // Update seat assignment in the Hall table
            DatabaseConnection.updateSeatInHall(hallId, sessionId, seatNumber, -id); // -id for discounted tickets
        }

        // Add the tickets to the product quantities map (product ID 5 for discounted tickets)
        productQuantities.put(5, productQuantities.getOrDefault(5, 0) + quantity);

        // Increment the total price
        double productPrice = DatabaseConnection.getProductPrice(5); // Get price for discounted ticket
        totalPrice += productPrice * quantity;

        // Update product stock and sales
        DatabaseConnection.updateProductStockAndSales_DB(5, quantity);
    }
    */


}
