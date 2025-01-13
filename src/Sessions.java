import java.sql.Date;
import java.sql.Time;

public class Sessions {
    private int id;
    private int hallId;
    private int movieId;
    private Date date;
    private Time time;

    
    /**
     * Full constructor for Sessions
     * @param id
     * @param hallId
     * @param movieId
     * @param date
     * @param time
     */
    public Sessions(int id, int hallId, int movieId, Date date, Time time) {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    /**
     * Constructor for Sessions without id for flexibility
     * @param hallId
     * @param movieId
     * @param date
     * @param time
     */
    public Sessions(int hallId, int movieId, Date date, Time time) {
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    /**
     * Default constructor for Sessions
     * @param hallId
     * @param movieId
     * @param time
     */
    public Sessions() {
        
    }

    /**
     * Getter for ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for Hall ID
     * @return
     */
    public int getHallId() {
        return hallId;
    }

    /**
     * Getter for Movie ID
     * @return
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Getter for Date
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Getter for Time
     * @return
     */
    public Time getTime() {
        return time;
    }

    /**
     * Setter for ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for Hall ID
     * @param hallId
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    /**
     * Setter for Movie ID
     * @param movieId
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     * Setter for Date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter for Time
     * @param time
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * toString method for Sessions
     * @return Sessions as a string
     */
    @Override
    public String toString() {
        return "Sessions{" +
                "id=" + id +
                ", hallId=" + hallId +
                ", movieId=" + movieId +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
