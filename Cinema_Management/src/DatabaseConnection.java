import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Statement;
import java.sql.Time;       // java.sql.Time
import java.sql.Date; 



public class DatabaseConnection {
    private static Connection connection;

    // Static block to initialize the connection
    static 
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cinemacenter?useSSL=false&serverTimezone=UTC",
                "myuser",
                "1234"
            );
            System.out.println("Database connected!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed.");
        }
    }


    /*
    *
    *
    */
    // Authentication method
    public static Employee authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set parameters
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // Execute query
            ResultSet rs = pstmt.executeQuery();

            // If a matching user is found
            if (rs.next()) {
                int id = rs.getInt("id");
                String userRole = rs.getString("user_role");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                return new Admin(id, username, firstname, lastname, password);
            } else {
                System.out.println("Invalid username or password.");
                return null; // Authentication failed
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /*
    *
    *
    */
    ////////////////////////////////////////////////
    // Method to list all employees except managers
    public static List<Employee> listEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_role != 'manager'";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String userRole = rs.getString("user_role");
                String username = rs.getString("username");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String password = rs.getString("password");

                // Create Employee objects based on role
                Employee employee;
                switch (userRole.toLowerCase()) {
                    case "cashier":
                        employee = new Cashier(id, username, firstname, lastname, password);
                        break;
                    case "admin":
                        employee = new Admin(id, username, firstname, lastname, password);
                        break;
                    default:
                        employee = new Employee(id, userRole, username, firstname, lastname, password);
                        break;
                }
                employees.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }
    /*  
    *
    *
    */

    public static boolean updateEmployee(Employee employee) {
        String query = "UPDATE users SET user_role = ?, username = ?, firstname = ?, lastname = ?, password = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set query parameters
            pstmt.setString(1, employee.getUserRole());
            pstmt.setString(2, employee.getUsername());
            pstmt.setString(3, employee.getFirstname());
            pstmt.setString(4, employee.getLastname());
            pstmt.setString(5, employee.getPassword());
            pstmt.setInt(6, employee.getId());
    
            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    
    public static boolean deleteEmployee(int id)
    {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean hireEmployee(Employee employee)
    {
        String query = "INSERT INTO users (firstname, lastname, username, password, user_role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query))
        {
            pstmt.setString(1, employee.getFirstname());
            pstmt.setString(2, employee.getLastname());
            pstmt.setString(3, employee.getUsername());
            pstmt.setString(4, employee.getPassword());
            pstmt.setString(5, employee.getUserRole());
    
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the employee was added
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    
    public static boolean isValueInColumnOfTable(String tableName, String columnName, String value)
    {
        String query = "SELECT 1 FROM " + tableName + " WHERE " + columnName + " = ? LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, value);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a match is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false if an exception occurs
        }
    }
    
    
    /*  
    *
    *
    */
    /////////////////////////////////////////////////
    // Method to fetch all or filtered products
    public static List<Product> getProducts(String filterQuery, Object... params) 
    {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products " + (filterQuery != null ? filterQuery : "");

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set query placeholders if provided
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Retrieve product data
                int id = rs.getInt("id");
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                byte[] visuals = rs.getBytes("visuals");
                double agebased_disc_rate = rs.getDouble("agebased_disc_rate");
                int sold = rs.getInt("sold");

                // Add the product to the list
                products.add(new Product(id, productName, price, stock, visuals, agebased_disc_rate, sold));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
    /*
    *
    *
    */
    public static boolean updateProduct(Product product) 
    {
        String query = "UPDATE products SET product_name = ?, price = ?, stock = ?, agebased_disc_rate = ?, visuals = ?, sold = ?, WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set query placeholder parameters
            pstmt.setString(1, product.getProductName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setDouble(4,product.get_agebased_disc_rate_());
            pstmt.setBytes(5, product.getVisuals()); // Binary data for the image
            pstmt.setInt(6, product.getSold());
            pstmt.setInt(7, product.getId());
    
            // Execute update
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if the update was successful
    
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
    *
    *
    */
    public static List<Product> calculateRevenueAndTaxes() 
    {
        List<Product> products = getProducts(null); // Fetch products from the database

        for (Product product : products) {
            String productName = product.getProductName();
            double price = product.getPrice();
            
            int sold = product.getSold();

            // Tax rate: 20% for tickets, 10% for others
            double taxRate = (productName.equalsIgnoreCase("ticket") || productName.equalsIgnoreCase("discountedTicket"))? 0.2 : 0.1;
            // Calculate revenue
            product.productsRevenue = sold * (price * (1 + taxRate));
            // Calculate tax
            product.taxAmount = sold*price*taxRate;
        }
        return products;
    }
/*
 * MOVIE THINGS
 */
///////////////////////////////////////////////////

    public static boolean addMovie(Movie movie) 
    {
        String query = "INSERT INTO Movie (title, genre, poster, summary) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setBytes(3, movie.getPoster());
            pstmt.setString(4, movie.getSummary());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
     * 
     * 
     */
    public static boolean updateMovie(Movie movie) {
        String query = "UPDATE Movie SET title = ?, genre = ?, poster = ?, summary = ? WHERE id = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, movie.getTitle());
            pstmt.setString(2, movie.getGenre());
            pstmt.setBytes(3, movie.getPoster());
            pstmt.setString(4, movie.getSummary());
            pstmt.setInt(5, movie.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
     * 
     *
     */
    public static boolean deleteMovie_byID(int movieId) {
        String query = "DELETE FROM Movie WHERE id = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, movieId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /*
     * 
     * for listing all movies
     */
    public static List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM movie";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getBytes("poster"),
                    rs.getString("summary")
                );
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    /*
     * 
     * To list a specific movie
     */
    public static Movie getMovie_byID(int movieId) {
        String query = "SELECT * FROM movie WHERE id = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, movieId);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getBytes("poster"),
                        rs.getString("summary")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if no movie is found or an error occurs
    }
    /*
     * CASHIER GUY WILL USE THESE
     * get movies by selected genre
     */
    public static List<Movie> SearchBy_Genre(String genre) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT id, title, poster FROM movie WHERE genre LIKE ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + genre + "%"); // Use wildcards for partial matches
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        null, // Genre not needed for display
                        rs.getBytes("poster"),
                        null // Summary not needed for display
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return movies;
    }
    /*
     * 
     * Partially search, unknown title
     */
    public static List<Movie> SearchBy_Title(String partialTitle) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT id, title, poster FROM movie WHERE title LIKE ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + partialTitle + "%"); // thanks to wilcards
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        null, // Genre not needed for display
                        rs.getBytes("poster"),
                        null // Summary not needed for display
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return movies;
    }
    /*
     * 
     * to shorten and simplify the code genre non-genre full title partial title
     */
    public static List<Movie> searchBy_title_genre(String inputTitle, String genre) {
        List<Movie> movies = new ArrayList<>();
    
        // Base query
        String query = "SELECT id, title, poster FROM movie WHERE title LIKE ?";
    
        // Add genre filter if provided
        if (genre != null && !genre.trim().isEmpty()) {
            query += " AND genre = ?";
        }
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + inputTitle + "%"); // Partial match for title
    
            // Set genre parameter if provided
            if (genre != null && !genre.trim().isEmpty()) {
                pstmt.setString(2, genre);
            }
    
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        null, // Genre not needed for display
                        rs.getBytes("poster"),
                        null // Summary not needed for display
                    );
                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return movies;
    }
    /*
     *
     *
     *
     *
     * 
     * Bill issues
     * 
     */
    public static boolean saveBillToDB(Bill bill) { ///updates stocks
        String insertBillQuery = "INSERT INTO bills (total_price) VALUES (?)";
        String insertBillProductsQuery = "INSERT INTO bill_products (bill_id, product_id, quantity) VALUES (?, ?, ?)";
    
        try {
            // Insert bill and get generated bill ID
            PreparedStatement billStmt = connection.prepareStatement(insertBillQuery, Statement.RETURN_GENERATED_KEYS);
            billStmt.setDouble(1, bill.getTotalPrice());
            billStmt.executeUpdate();
            ResultSet rs = billStmt.getGeneratedKeys();
            if (rs.next()) {
                bill.setId(rs.getInt(1));
            }
    
            // Insert products related to the bill
            PreparedStatement productsStmt = connection.prepareStatement(insertBillProductsQuery);
            for (Map.Entry<Integer, Integer> entry : bill.getProductQuantities().entrySet()) {
                productsStmt.setInt(1, bill.getId());
                productsStmt.setInt(2, entry.getKey());
                productsStmt.setInt(3, entry.getValue());
                productsStmt.addBatch();

                updateProductStockAndSales_DB(entry.getKey(), entry.getValue());
            }
            productsStmt.executeBatch();
    
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    ////////////////
    public static Bill getBillById(int billId) {
        Bill bill = new Bill(billId);
    
        // Fetch bill metadata
        String billQuery = "SELECT total_price FROM bills WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(billQuery)) {
            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bill.setId(billId);
                bill.calculateTotalPrice();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Fetch bill items
        String itemQuery = "SELECT product_id, quantity FROM bill_items WHERE bill_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(itemQuery)) {
            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                bill.addProduct(productId, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return bill;
    }
    /**
     * 
     */
    public static double getProductPrice(int productId) {
        String query = "SELECT price FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productId); // Set product ID in the query
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price"); // Return the price column value
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Return 0.0 if the price is not found
    }
    
    public static boolean updateProductStockAndSales_DB(int productId, int quantity) {
        String query = "UPDATE products SET stock = stock - ?, sold = sold + ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity); // Reduce stock
            pstmt.setInt(2, quantity); // Increase sold count
            pstmt.setInt(3, productId); // Product ID
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    ////////////////////CHECK THE METHODS ABOVE IM NOT SURE ABOUT THEM
    /// 
    
    // AFTER CUSTOMER SELECTS A MOVIE, TO SEE THE SESSIONS CALL THIS
    public static List<Sessions> getSessionsOfMovie(int movieId) {
        List<Sessions> sessionsList = new ArrayList<>();

        String query = "SELECT id, hall_id, movie_id, date, time FROM sessions WHERE movie_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, movieId); // Set the movieId in the query

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Retrieve data from the ResultSet
                int id = rs.getInt("id");
                int hallId = rs.getInt("hall_id");
                int movieIdResult = rs.getInt("movie_id");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");

                // Create a Sessions object and add it to the list
                sessionsList.add(new Sessions(id, hallId, movieIdResult, date, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sessionsList;
    }
    /////////////SEAT AVAILABILITY ARRAY AND TOTAL BOOKED
    public static int[] Seat_Availability_Array(int sessionId, int hallId) {
        String hallTable = (hallId == 1) ? "HallA" : "HallB"; // Determine the hall table
        String query = "SELECT * FROM " + hallTable + " WHERE sessionID = ?";
        int[] seat01 = null;
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, sessionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int seatCount = (hallId == 1) ? 16 : 48; // HallA has 16 seats, HallB has 48 seats
                seat01 = new int[seatCount + 1]; // Create array to include seats and numberOfBooked
    
                // Fill seat availability
                for (int i = 1; i <= seatCount; i++) {
                    seat01[i - 1] = rs.getInt("seat" + i); //see bill id and and if the ticket is discounted
                }
    
                // Add numberOfBooked as the last element
                seat01[seatCount] = rs.getInt("numberOfBooked");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return seat01;
    }
    ////////////////////marking seat as full
    /// 
    public static boolean buySeats_updateDB(int sessionId, int hallId, List<Integer> selectedSeats) {
        String hallTable = (hallId == 1) ? "HallA" : "HallB"; // Determine the hall table
        StringBuilder query = new StringBuilder("UPDATE " + hallTable + " SET ");
    
        // Create SET clause dynamically based on selected seats
        for (int i = 0; i < selectedSeats.size(); i++) {
            query.append("seat").append(selectedSeats.get(i)).append(" = 1"); // Mark seat as full (1)
            if (i < selectedSeats.size() - 1) {
                query.append(", "); // Add commas for multiple seats
            }
        }
        query.append(" WHERE sessionID = ?");
    
        try (PreparedStatement pstmt = connection.prepareStatement(query.toString())) {
            pstmt.setInt(1, sessionId); // Set the sessionId to update for the correct session
            int rowsUpdated = pstmt.executeUpdate(); // Execute the update query
    
            // If rowsUpdated is greater than 0, the update was successful
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // If update fails
    }
    
    
    
    
    
    
    
    
    
    

}


    
    
    






