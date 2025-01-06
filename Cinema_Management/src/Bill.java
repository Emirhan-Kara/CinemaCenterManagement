import java.util.HashMap;
import java.util.Map;

public class Bill {
    public int id; // Bill ID to save and check
    public Map<Integer, Integer> productQuantities; // Product ID from database -> desired Quantity
    public double totalPrice; // Total price of the bill

    // Constructor
    public Bill() {
        this.productQuantities = new HashMap<>();
        this.totalPrice = 0.0;
    }

    // Constructor with bill ID (useful for fetched database bills)
    public Bill(int id) {
        this.id = id;
        this.productQuantities = new HashMap<>();
        this.totalPrice = 0.0;
    }

    // Getters Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Integer> getProductQuantities() {
        return productQuantities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // addProduct will check that is this product added earlier?.
    //if the answer is yes, it will add new ones into it; if no it will basically add a new pair key and quatity
    public void addProduct(int productId, int quantity) {
        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);
    }

    // Remove a product from the bill
    public void removeProduct(int productId, int quantityToRemove) {
        if (productQuantities.containsKey(productId)) {
            int updatedQuantity = productQuantities.get(productId) - quantityToRemove;
            if (updatedQuantity > 0) {
                productQuantities.put(productId, updatedQuantity);
            } else {
                productQuantities.remove(productId);
            }
        }
    }

    // Calculate total price by fetching prices from the database
    public void calculateTotalPrice() {
        totalPrice = 0.0;
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            // Fetch price from the database
            double price = DatabaseConnection.getProductPrice(productId);

            // Add to total price
            totalPrice += price * quantity;
        }
    }

    // Display bill details (optional)
    public void printBillDetails() {
        System.out.println("Bill ID: " + id);
        System.out.println("Products:");
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            System.out.println("  Product ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
        System.out.println("Total Price: " + totalPrice);
    }
    
}
