/**
 * Represents a ticket for a cinema seat, including its price, seat number, and associated details.
 */
public class Ticket {

    /**
     * The price of the seat.
     */
    private double seatPrice;

    /**
     * The number of the seat.
     */
    private int seatNumber;

    /**
     * The ID of the product associated with the ticket.
     */
    private int productID;

    /**
     * The ID of the hall where the seat is located.
     */
    private int hallId;

    /**
     * The ID of the session associated with the ticket.
     */
    private int sessionId;

    /**
     * Constructs a new Ticket with the specified seat number, price, and product ID.
     *
     * @param seatNum   the seat number.
     * @param seatPrice the price of the seat.
     * @param productID the ID of the associated product.
     */
    public Ticket(int seatNum, double seatPrice, int productID) {
        this.seatNumber = seatNum;
        this.seatPrice = seatPrice;
        this.productID = productID;
    }

    /**
     * Gets the product ID associated with the ticket.
     *
     * @return the product ID.
     */
    public int getProductid() {
        return this.productID;
    }

    /**
     * Gets the seat number associated with the ticket.
     *
     * @return the seat number.
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * Sets the seat number for the ticket.
     *
     * @param seatNumber the new seat number.
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * Gets the hall ID associated with the ticket.
     *
     * @return the hall ID.
     */
    public int getHallId() {
        return hallId;
    }

    /**
     * Sets the hall ID for the ticket.
     *
     * @param hallId the new hall ID.
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    /**
     * Gets the session ID associated with the ticket.
     *
     * @return the session ID.
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session ID for the ticket.
     *
     * @param sessionId the new session ID.
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the price of the seat.
     *
     * @return the seat price.
     */
    public double getSeatPrice() {
        return seatPrice;
    }

    /**
     * Sets the price of the seat.
     *
     * @param seatPrice the new seat price.
     */
    public void setSeatPrice(double seatPrice) {
        this.seatPrice = seatPrice;
    }
}
