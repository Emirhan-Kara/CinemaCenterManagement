import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseConnection {
    private static Connection connection;

    // Static block to initialize the connection
    static {
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

                // Return the appropriate object based on user_role
                switch (userRole.toLowerCase()) {
                    case "manager":
                        return new Manager(id, username, firstname, lastname, password);
                    case "cashier":
                        return new Cashier(id, username, firstname, lastname, password);
                    case "admin":
                        return new Admin(id, username, firstname, lastname, password);
                    default:
                        throw new IllegalArgumentException("Unknown user role: " + userRole);
                }
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
    // Method to fetch all or filtered products
    public static List<Product> getProducts(String filterQuery, Object... params) {
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
                int normalsell = rs.getInt("normalsell");
                byte[] visuals = rs.getBytes("visuals");
                int agebased_discountedSell = rs.getInt("agebased_discountedSell");
                double agebased_disc_rate = rs.getDouble("agebased_disc_rate");

                // Add the product to the list
                products.add(new Product(id, productName, price, stock, normalsell, agebased_discountedSell, visuals, agebased_disc_rate));
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
    public static boolean updateProduct(Product product) {
        String query = "UPDATE products SET product_name = ?, price = ?, stock = ?, normalsell = ?, agebased_discountedSell = ?, agebased_disc_rate = ?, visuals = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set query placeholder parameters
            pstmt.setString(1, product.getProductName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setInt(4, product.getNormalsell());
            pstmt.setInt(5, product.get_agebased_discountedSell());
            pstmt.setDouble(6,product.get_agebased_disc_rate_());
            pstmt.setBytes(7, product.getVisuals()); // Binary data for the image
            pstmt.setInt(8, product.getId());
    
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

    public static Map<String, Object> calculateRevenueAndTaxes() {
    List<Product> products = getProducts(null); // Fetch products from the database
    double totalRevenue = 0.0;
    double totalTax = 0.0;

    for (Product product : products) {
        String productName = product.getProductName();
        double price = product.getPrice();
        double agebased_disc_rate = product.get_agebased_disc_rate_();
        int normalSell = product.getNormalsell();
        int ageBasedDiscountedSell = product.get_agebased_discountedSell();
        int totalSold = normalSell + ageBasedDiscountedSell;


        // Tax rate: 20% for tickets, 10% for others
        double taxRate = productName.equalsIgnoreCase("ticket") ? 0.2 : 0.1;

        // Calculate revenue
        double normalRevenue = normalSell * (price * (1 + taxRate));
        double discountedRevenue = ageBasedDiscountedSell *((price * (1 - agebased_disc_rate))*(1 + taxRate));
        double revenue = normalRevenue + discountedRevenue; // discounts and taxes added

        // Calculate tax
        double taxAmount = (normalSell * price * taxRate) + (ageBasedDiscountedSell * (price * (1 - agebased_disc_rate)) * taxRate);

        // Add to totals
        totalRevenue += revenue;
        totalTax += taxAmount;

        // Set additional fields in the product object
        product.setTaxRate(taxRate); // Convert to percentage
        product.setRevenue(revenue);
        product.setTaxAmount(taxAmount);
        product.setTotalSold(totalSold); // Total products sold
    }

    // Return the results as a map
    Map<String, Object> result = new HashMap<>();
    result.put("products", products); // List of product details
    result.put("totalRevenue", totalRevenue); // Total revenue
    result.put("totalTax", totalTax); // Total tax

    return result;
}


}
