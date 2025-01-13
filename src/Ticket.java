
/**
 * double seat price hold seat's price
 * int seatNumber indicates the number of seat
 * int productID id of the ticket(discounted 5 or normal 1)
 * int hallID 1->A 2->B
 * int sessionID session's id
 * 
 */
public class Ticket {
    private double seatPrice;
    private int seatNumber;
    private int productID; 


    private int hallId;
    private int sessionId;

    /**
     * Overloaded constructure to create ticket object
     * @param seatNum seat number
     * @param seatPrice price
     * @param productID disconted or normal
     */
    Ticket(int seatNum, double seatPrice, int productID)
    {
        this.seatNumber = seatNum;
        //this.hallId = hall_id;
        //this.sessionId = session_id;
        this.seatPrice = seatPrice;
        this.productID = productID;
    }

    /**
     * call to get id
     * @return id
     */
    public int getProductid()
    {
        return this.productID;
    }

    // Getter and Setter for seatNumber
    /**
     * to get seat number
     * @return seat number
     */
    public int getSeatNumber() {
        return seatNumber;
    }

    /**
     * set seat number
     * @param seatNumber seat number
     */
    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    /**
     * get Hall Id
     * @return hallId
     */
    public int getHallId() {
        return hallId;
    }

    /**
     * set hall id
     * @param hallId hall id
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }


    /**
     * getter
     * @return sessionId
     */
    public int getSessionId() {
        return sessionId;
    }

    /**
     * setter
     * @param sessionId session
     */
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * getter
     * @return seatprice
     */
    public double getSeatPrice() {
        return seatPrice;
    }

    /**
     * setter
     * @param seatPrice prce of seat
     */
    public void setSeatPrice(double seatPrice) {
        this.seatPrice = seatPrice;
    }
}
