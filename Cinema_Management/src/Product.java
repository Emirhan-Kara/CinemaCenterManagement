import java.util.Arrays;

public class Product {
    /////////////////////////////////////////////
    /// FROM DATABASE
    private int id;
    private String productName; // VARCHAR
    private double price;       // DOUBLE
    private int stock;          // INT
    private byte[] visuals;     // BLOB (PNG stored as byte[])
    private double agebased_disc_rate;
    private int sold;

    public double productsRevenue;
    public double taxAmount;


    //////////////////////////////////////////////
    /// FROM calculateRevenueAndTaxes METHOD


    // Constructor
    public Product(int id, String productName, double price, int stock, byte[] visuals, double agebased_disc_rate, int sold) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.visuals = visuals;
        this.agebased_disc_rate = agebased_disc_rate;
        this.sold = sold;
    }


    // Getters and setters for new fields
    public double get_agebased_disc_rate_() {
        return agebased_disc_rate;
    }

    public void set_agebased_disc_rate(double agebased_disc_rate) {
        this.agebased_disc_rate = agebased_disc_rate;
    }

    public void sold_amount(int amount)
    {
        this.sold += amount;
    }
    public int getSold()
    {
        return sold;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        
        this.price = price - (price * agebased_disc_rate);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public byte[] getVisuals() {
        return visuals;
    }

    public void setVisuals(byte[] visuals) {
        this.visuals = visuals;
    }

    // toString method for displaying product information
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
