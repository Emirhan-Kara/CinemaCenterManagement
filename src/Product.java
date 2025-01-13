import java.util.Arrays;

/**
 * Product class to hold the entites of the products in the product table of
 * database, including the tickets
 */
public class Product {
    /////////////////////////////////////////////
    /// FROM DATABASE
    /**
     * Product ID.
     */
    private int id;

    /**
     * Product name.
     */
    private String productName;

    /**
     * Product price.
     */
    private double price;

    /**
     * Stock quantity.
     */
    private int stock;

    /**
     * Product image as byte array.
     */
    private byte[] visuals;

    /**
     * Age-based discount rate.
     */
    private double agebased_disc_rate;

    /**
     * Units sold.
     */
    private int sold;

    /**
     * Total revenue.
     */
    public double totalrevenue;

    /**
     * Revenue from products.
     */
    public double productsRevenue;

    /**
     * Tax rate.
     */
    public double taxrate;

    /**
     * Total tax amount.
     */
    public double total_taxAmount;

    //////////////////////////////////////////////
    /// FROM calculateRevenueAndTaxes METHOD

    /**
     * Overloaded constructor for Product class
     * 
     * @param id                 id
     * @param productName        product name
     * @param price              price
     * @param stock              stock
     * @param visuals            visuals
     * @param agebased_disc_rate age based discount
     * @param sold               sold
     * @param totalrevenue       total revenue
     */
    public Product(int id, String productName, double price, int stock, byte[] visuals, double agebased_disc_rate,
            int sold, double totalrevenue) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.visuals = visuals;
        this.agebased_disc_rate = agebased_disc_rate;
        this.sold = sold;
        this.totalrevenue = totalrevenue;
        this.taxrate = (id == 1 || id == 5 ? 0.2 : 0.1);
        this.total_taxAmount = totalrevenue * taxrate;
    }

    /**
     * Overloaded constructor for Product class
     * 
     * @param productID id
     * @param price     price
     */
    public Product(int productID, double price) {
        this.id = productID;
        this.price = price;
    }

    // Getters and setters for new fields

    /**
     * Gets the total revenue.
     * 
     * @return Total revenue.
     */
    public double gettotalrevenue() {
        return totalrevenue;
    }

    /**
     * Sets the total revenue.
     * 
     * @param totalrevenue Total revenue to set.
     */
    public void settotalrevenue(double totalrevenue) {
        this.totalrevenue = totalrevenue;
    }

    /**
     * Gets the age-based discount rate.
     * 
     * @return Age-based discount rate.
     */
    public double get_agebased_disc_rate_() {
        return agebased_disc_rate;
    }

    /**
     * Sets the age-based discount rate.
     * 
     * @param agebased_disc_rate Discount rate to set.
     */
    public void set_agebased_disc_rate(double agebased_disc_rate) {
        this.agebased_disc_rate = agebased_disc_rate;
    }

    /**
     * Increases the sold amount.
     * 
     * @param amount Amount to add to sold.
     */
    public void setsold(int amount) {
        this.sold += amount;
    }

    /**
     * Gets the total sold amount.
     * 
     * @return Total sold amount.
     */
    public int getsold() {
        return sold;
    }

    /**
     * Gets the product ID.
     * 
     * @return Product ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the product ID.
     * 
     * @param id Product ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the product name.
     * 
     * @return Product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name.
     * 
     * @param productName Product name to set.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product price.
     * 
     * @return Product price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the product price after applying the age-based discount.
     * 
     * @param price Base price to set.
     */
    public void setPrice(double price) {
        this.price = price - (price * agebased_disc_rate);
    }

    /**
     * Gets the stock quantity.
     * 
     * @return Stock quantity.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock quantity.
     * 
     * @param stock Stock quantity to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the product visuals as a byte array.
     * 
     * @return Visuals byte array.
     */
    public byte[] getVisuals() {
        return visuals;
    }

    /**
     * Sets the product visuals.
     * 
     * @param visuals Visuals byte array to set.
     */
    public void setVisuals(byte[] visuals) {
        this.visuals = visuals;
    }

    /**
     * Converts the product details to a string. For debug
     * 
     * @return String representation of the product.
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", visuals=" + (visuals != null ? "PNG image loaded" : "No image") +
                '}';
    }

}
