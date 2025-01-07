import java.io.File;
import java.io.InputStream;
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
    public static Connection connection;
    private File selectedFile; // To store the selected file REMINDER CHECK THIS DONT FORGET

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
                return new Employee(id, userRole, username, firstname, lastname, password);
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

    public static boolean DeleteEmployee(int id) {
        String query = "DELETE users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


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
                double totalrevenue = rs.getDouble("totalrevenue");

                // Add the product to the list
                products.add(new Product(id, productName, price, stock, visuals, agebased_disc_rate, sold, totalrevenue));
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
        String query = "UPDATE products SET product_name = ?, price = ?, stock = ?, agebased_disc_rate = ?, visuals = ?, sold = ?, totalrevenue = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set query placeholder parameters
            pstmt.setString(1, product.getProductName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setDouble(4,product.get_agebased_disc_rate_());
            pstmt.setBytes(5, product.getVisuals()); // Binary data for the image
            pstmt.setInt(6, product.getsold());
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
    /*
    * MOVIE THINGS
    */
    ///////////////////////////////////////////////////

    public static boolean addMovie(Movie movie) {
        String query = "INSERT INTO Movie (title, genre, poster, summary) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, movie.title);
            pstmt.setString(2, movie.genre);

            // Handle the poster (store as LONGBLOB)
            if (movie.poster != null) {
                pstmt.setBytes(3, movie.poster); // Use the byte[] poster
            } else {
                pstmt.setNull(3, java.sql.Types.BLOB); // Set NULL if no poster is provided
            }

            pstmt.setString(4, movie.summary);

            return pstmt.executeUpdate() > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error
        }
    }

    public static boolean updateMovie(Movie movie) {
        String query = "UPDATE Movie SET title = ?, genre = ?, poster = ?, summary = ? WHERE id = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, movie.title);
            pstmt.setString(2, movie.genre);
    
            // Handle the poster (set as a LONGBLOB)
            if (movie.poster != null) {
                pstmt.setBytes(3, movie.poster); // Use the byte[] poster
            } else {
                pstmt.setNull(3, java.sql.Types.BLOB); // Set NULL if no poster is provided
            }
    
            pstmt.setString(4, movie.summary);
            pstmt.setInt(5, movie.id); // Specify which movie to update
    
            return pstmt.executeUpdate() > 0; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error
        }
    }
    
    public static boolean deleteMovie_byID(int movieId) {
        String query = "DELETE FROM Movie WHERE id = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, movieId); // Specify the movie ID to delete
    
            return pstmt.executeUpdate() > 0; // Return true if deletion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an error
        }
    }
    
        /**
     * Retrieves all movies from the database, including posters as byte arrays.
     *
     * @return A list of Movie objects with posters included.
     */
    public static List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movie";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Create Movie objects, including the poster as byte[]
                movies.add(new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getBytes("poster"), // Poster stored as byte[]
                    rs.getString("summary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /**
 * Retrieves a single movie by its ID, including its poster as a byte array.
 *
 * @param movieId The ID of the movie to retrieve.
 * @return A Movie object with the poster included, or null if not found.
 */
public static Movie getMovie_byID(int movieId) {
    String query = "SELECT * FROM Movie WHERE id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setInt(1, movieId);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Return a Movie object with all its fields, including the poster
                return new Movie(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getBytes("poster"), // Poster stored as byte[]
                    rs.getString("summary")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null; // Return null if no movie is found
}


    /*
    * Get movies by selected genre, including genre and summary.
    */
    public static List<Movie> SearchBy_Genre(String genre) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT id, title, genre, poster, summary FROM movie WHERE genre LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + genre + "%"); // Use wildcards for partial matches

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getBytes("poster"),
                        rs.getString("summary")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /*
    * Search movies by partial title, including genre and summary.
    */
    public static List<Movie> SearchBy_Title(String partialTitle) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT id, title, genre, poster, summary FROM movie WHERE title LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + partialTitle + "%"); // Use wildcards for partial matches

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getBytes("poster"),
                        rs.getString("summary")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    /*
    * Search movies by title and genre, including genre and summary.
    */
    public static List<Movie> searchBy_title_genre(String inputTitle, String genre) {
        List<Movie> movies = new ArrayList<>();

        // Base query
        String query = "SELECT id, title, genre, poster, summary FROM movie WHERE title LIKE ?";

        // Add genre filter if provided
        if (genre != null && !genre.trim().isEmpty()) {
            query += " AND genre LIKE ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + inputTitle + "%"); // Partial match for title

            // Set genre parameter if provided
            if (genre != null && !genre.trim().isEmpty()) {
                pstmt.setString(2, "%" + genre + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getBytes("poster"),
                        rs.getString("summary")
                    ));
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
    public static void updateSeatInHall(int hallId, int sessionId, int seatNumber, int seatValue) {
        String hall = (hallId == 1 ? "HallA" : "HallB");
        String query = "UPDATE " + hall + " SET seat" + seatNumber + " = ? WHERE session_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, seatValue);
            pstmt.setInt(2, sessionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void incrementBookedSeats(int hallId, int sessionId, int quantity) {
        String hall = (hallId == 1 ? "HallA" : "HallB");
        String query = "UPDATE " + hall + " SET numberOfBooked = numberOfBooked + ? WHERE session_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, sessionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static double getProductPrice(int productId) {
        String query = "SELECT price FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    
    /*
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
    }*/
    /* 
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
    */
    
    public static boolean updateProductStockAndSales_DB(int productId, int quantity) {
        double price = getProductPrice(productId);
        double fluctuation = price*quantity;
        String query = "UPDATE products SET stock = stock - ?, sold = sold + ?, totalrevenue = totalrevenue + ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, quantity); // Reduce stock
            pstmt.setInt(2, quantity); // Increase sold count
            pstmt.setDouble(3, fluctuation); 
            pstmt.setInt(4, productId); // Product ID
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
    /*
     * 
     * 
     * SESSION ISSUES
     */
    /**
     * Adds a new session to the database after ensuring no conflicts.
     *
     * @param session The Sessions object containing the session details.
     * @return True if the session is added, false otherwise.
     */
    public static boolean addSession(Sessions session) {
        // Check if a session with the same hallId, date, and time already exists
        String checkQuery = "SELECT COUNT(*) FROM sessions WHERE hall_id = ? AND date = ? AND time = ?";
        String insertQuery = "INSERT INTO sessions (hall_id, movie_id, date, time) VALUES (?, ?, ?, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Set parameters for the check query
            checkStmt.setInt(1, session.getHallId());
            checkStmt.setDate(2, session.getDate());
            checkStmt.setTime(3, session.getTime());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // Session already exists
                System.out.println("Conflict: A session already exists with the same hall, date, and time.");
                return false;
            }

            // No conflict, proceed to insert the session
            insertStmt.setInt(1, session.getHallId());
            insertStmt.setInt(2, session.getMovieId());
            insertStmt.setDate(3, session.getDate());
            insertStmt.setTime(4, session.getTime());

            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0; // Return true if insertion is successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    /**
     * Deletes a session from the database and related hall table after checking conditions:
     * - If seats are booked, the session cannot be deleted unless the date has passed.
     * - Removes the session from both the `sessions` table and the related hall table.
     *
     * @param sessionId The ID of the session to be deleted.
     * @param hallId    The ID of the hall associated with the session.
     * @return True if the session is deleted successfully, false otherwise.
     */
    public static boolean deleteSession(int sessionId, int hallId) {
        // Determine the hall table name based on the hall ID
        String hallTableName = hallId == 1 ? "HallA" : hallId == 2 ? "HallB" : null;

        if (hallTableName == null) {
            System.out.println("Invalid hall ID.");
            return false;
        }

        // Queries
        String checkSessionQuery = "SELECT date FROM sessions WHERE id = ?";
        String checkSeatsQuery = "SELECT numberOfbooked FROM " + hallTableName + " WHERE sessionID = ?";
        String deleteFromHall = "DELETE FROM " + hallTableName + " WHERE sessionID = ?";
        String deleteFromSessions = "DELETE FROM sessions WHERE id = ?";

        try (PreparedStatement checkSessionStmt = connection.prepareStatement(checkSessionQuery);
            PreparedStatement checkSeatsStmt = connection.prepareStatement(checkSeatsQuery);
            PreparedStatement deleteHallStmt = connection.prepareStatement(deleteFromHall);
            PreparedStatement deleteSessionStmt = connection.prepareStatement(deleteFromSessions)) {

            // Step 1: Check if the session exists and retrieve the date
            checkSessionStmt.setInt(1, sessionId);
            ResultSet sessionRs = checkSessionStmt.executeQuery();

            if (!sessionRs.next()) {
                System.out.println("Session not found in the sessions table.");
                return false;
            }

            Date sessionDate = sessionRs.getDate("date");
            Date currentDate = new Date(System.currentTimeMillis());

            // Step 2: Check if any seats are booked for the session
            checkSeatsStmt.setInt(1, sessionId);
            ResultSet seatsRs = checkSeatsStmt.executeQuery();

            if (seatsRs.next()) {
                int numberOfBookedSeats = seatsRs.getInt("numberOfbooked");

                // If seats are booked and the session date has not passed, disallow deletion
                if (numberOfBookedSeats > 0 && sessionDate.after(currentDate)) {
                    System.out.println("Cannot delete session: Seats are booked and the session date has not passed.");
                    return false;
                }
            }

            // Step 3: Delete the session from the hall table
            deleteHallStmt.setInt(1, sessionId);
            deleteHallStmt.executeUpdate();

            // Step 4: Delete the session from the `sessions` table
            deleteSessionStmt.setInt(1, sessionId);
            int rowsAffected = deleteSessionStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Session deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete session from the sessions table.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

/**
 * Updates a session in the database after ensuring there are no conflicts.
 * Ensures related hall tables are updated if the hall changes.
 *
 * @param oldSession The existing session details.
 * @param newSession The new session details.
 * @return True if the session is updated successfully, false otherwise.
 */
public static boolean updateSession(Sessions oldSession, Sessions newSession) {
    // Determine old and new hall table names
    String oldHallTableName = oldSession.getHallId() == 1 ? "HallA" : oldSession.getHallId() == 2 ? "HallB" : null;
    String newHallTableName = newSession.getHallId() == 1 ? "HallA" : newSession.getHallId() == 2 ? "HallB" : null;

    if (oldHallTableName == null || newHallTableName == null) {
        System.out.println("Invalid hall ID(s).");
        return false;
    }

    // Query to check for conflicts
    String conflictCheckQuery = "SELECT COUNT(*) FROM sessions WHERE hall_id = ? AND date = ? AND time = ? AND id != ?";
    String deleteFromOldHall = "DELETE FROM " + oldHallTableName + " WHERE sessionID = ?";
    String insertIntoNewHall = "INSERT INTO " + newHallTableName + " (sessionID, numberOfbooked) VALUES (?, 0)";
    String updateSessionQuery = "UPDATE sessions SET hall_id = ?, movie_id = ?, date = ?, time = ? WHERE id = ?";

    try (PreparedStatement conflictCheckStmt = connection.prepareStatement(conflictCheckQuery);
         PreparedStatement deleteStmt = connection.prepareStatement(deleteFromOldHall);
         PreparedStatement insertStmt = connection.prepareStatement(insertIntoNewHall);
         PreparedStatement updateStmt = connection.prepareStatement(updateSessionQuery)) {

        // Step 1: Check for conflicts with the new session details
        conflictCheckStmt.setInt(1, newSession.getHallId());
        conflictCheckStmt.setDate(2, newSession.getDate());
        conflictCheckStmt.setTime(3, newSession.getTime());
        conflictCheckStmt.setInt(4, newSession.getId());

        ResultSet rs = conflictCheckStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            System.out.println("Conflict: A session already exists with the same hall, date, and time.");
            return false;
        }

        // Step 2: If the hall is being changed, update hall tables
        if (newSession.getHallId() != oldSession.getHallId()) {
            // Delete the session from the old hall table
            deleteStmt.setInt(1, oldSession.getId());
            deleteStmt.executeUpdate();

            // Insert the session into the new hall table
            insertStmt.setInt(1, newSession.getId());
            insertStmt.executeUpdate();
        }

        // Step 3: Update the session details in the `sessions` table
        updateStmt.setInt(1, newSession.getHallId());
        updateStmt.setInt(2, newSession.getMovieId());
        updateStmt.setDate(3, newSession.getDate());
        updateStmt.setTime(4, newSession.getTime());
        updateStmt.setInt(5, newSession.getId());

        int rowsAffected = updateStmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Session updated successfully.");
            return true;
        } else {
            System.out.println("Failed to update session.");
            return false;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    
    
    
    
    
    
    
    
    
    

}


    
    
    