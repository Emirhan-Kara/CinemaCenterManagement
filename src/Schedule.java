import java.sql.Date;
import java.sql.Time;

/**
 * Schedule class for showing sessions in the tableview of admin page
 * Sessions store movie id, schedule store movie title 
 */
public class Schedule {
    Integer id;
    private int hallId;
    private String movie;
    private Date date;
    private Time time;

    /**
     * Schedule constructor
     * @param hallId
     * @param movie
     * @param date
     * @param time
     */
    public Schedule(int hallId, String movie, Date date, Time time) {
        this.hallId = hallId;
        this.movie = movie;
        this.date = date;
        this.time = time;
    }

    /**
     * Schedule constructor with id
     * @param hallId
     * @param movie
     * @param date
     * @param time
     * @param id
     */
    public Schedule(int hallId, String movie, Date date, Time time, Integer id) {
        this.id = id;
        this.hallId = hallId;
        this.movie = movie;
        this.date = date;
        this.time = time;
    }

    /**
     * Getter for a session object from the related schedule object
     * @param schedule
     * @return
     */
    Sessions getSession(Schedule schedule) {
        return new Sessions(this.hallId, DatabaseConnection.getMovie_byTitle(this.movie).getId(), this.date, this.time);
    }

    /**
     * Schedule default constructor
     */
    public Schedule() {
    }

    /**
     * Getter for the date
     * @return
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Getter for the hall id
     * @return
     */
    public Integer getHallId() {
        return this.hallId;
    }

    /**
     * Getter for the time
     * @return
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Getter for the movie title
     * @return
     */
    public String getMovie() {
        return this.movie;
    }

    /**
     * Setter for the date
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter for the hall id
     * @param hallId
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    /**
     * Setter for the time
     * @param time
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Setter for the movie title
     * @param movie
     */
    public void setMovie(String movie) {
        this.movie = movie;
    }

    /**
     * Setter for the id
     * @param id
     */
    void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the id
     * @return
     */
    Integer getId() {
        return this.id;
    }
}
