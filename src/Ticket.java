public class Ticket {
    private double seatPrice;
    private int seatNumber;
    private int productID; 


    private int hallId;
    private int sessionId;

    Ticket(int seatNum, double seatPrice, int productID)
    {
        this.seatNumber = seatNum;
        //this.hallId = hall_id;
        //this.sessionId = session_id;
        this.seatPrice = seatPrice;
        this.productID = productID;
    }

    public int getProductid()
    {
        return this.productID;
    }

    // Getter and Setter for seatNumber
    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    // Getter and Setter for hallId
    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    // Getter and Setter for sessionId
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    // Getter and Setter for seatPrice
    public double getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(double seatPrice) {
        this.seatPrice = seatPrice;
    }
}

