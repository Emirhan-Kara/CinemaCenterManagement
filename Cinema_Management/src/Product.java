import java.util.Arrays;

public class Product {
    /////////////////////////////////////////////
    /// FROM DATABASE
    private int id;
    private String productName; // VARCHAR
    private double price;       // DOUBLE
    private int stock;          // INT
    private int normalsell;           // INT
    private int agebased_discountedSell;           // INT
    private byte[] visuals;     // BLOB (PNG stored as byte[])
    private double agebased_disc_rate;


    //////////////////////////////////////////////
    /// FROM calculateRevenueAndTaxes METHOD

    private double taxRate; // New field
    private double revenue; // net revenue
    private double taxAmount; // paid taxes 
    private int totalSold;

    

    // Constructor
    public Product(int id, String productName, double price, int stock, int normalsell,int agebased_discountedSell, byte[] visuals, double agebased_disc_rate) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.normalsell = normalsell;
        this.agebased_discountedSell = agebased_discountedSell;
        this.visuals = visuals;
        this.agebased_disc_rate = agebased_disc_rate;
    }


    // Getters and setters for new fields
    public double get_agebased_disc_rate_() {
        return agebased_disc_rate;
    }

    public void set_agebased_disc_rate(double agebased_disc_rate) {
        this.agebased_disc_rate = agebased_disc_rate;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
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
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getNormalsell() {
        return normalsell;
    }

    public void setNormalsell(int sold) {
        this.normalsell = sold;
    }

    public int get_agebased_discountedSell() {
        return agebased_discountedSell;
    }

    public void set_agebased_discountedSell(int agebased_discountedSell) {
        this.agebased_discountedSell = agebased_discountedSell;
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
                ", sold=" + normalsell +
                ", visuals=" + (visuals != null ? "PNG image loaded" : "No image") +
                '}';
    }
}
