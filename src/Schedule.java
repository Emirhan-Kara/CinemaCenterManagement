import java.sql.Date;
import java.sql.Time;

/**
 * Schedule class for showing sessions in the tableview of admin page
 * Sessions store movie id, schedule store movie title 
 */
public class Schedule
{
    /**
     * Unique identifier for the session.
     */
    private Integer id;

    /**
     * Identifier for the hall.
     */
    private int hallId;

    /**
     * Name of the movie.
     */
    private String movie;

    /**
     * Date of the session.
     */
    private Date date;

    /**
     * Time of the session.
     */
    private Time time;


    /**
     * Schedule constructor
     * @param hallId hall id
     * @param movie title
     * @param date date
     * @param time time
     */
    public Schedule(int hallId, String movie, Date date, Time time) {
        this.hallId = hallId;
        this.movie = movie;
        this.date = date;
        this.time = time;
    }

    /**
     * Schedule constructor with id
     * @param hallId hall id
     * @param movie title
     * @param date date
     * @param time time
     * @param id id
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
     * @param schedule shcedule obejct to retrive its session
     * @return session object for that schedule
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
     * @return date
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Getter for the hall id
     * @return hall id
     */
    public Integer getHallId() {
        return this.hallId;
    }

    /**
     * Getter for the time
     * @return time
     */
    public Time getTime() {
        return this.time;
    }

    /**
     * Getter for the movie title
     * @return title
     */
    public String getMovie() {
        return this.movie;
    }

    /**
     * Setter for the date
     * @param date date to be set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Setter for the hall id
     * @param hallId hall id to be set
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    /**
     * Setter for the time
     * @param time time to be set
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Setter for the movie title
     * @param movie title to be set
     */
    public void setMovie(String movie) {
        this.movie = movie;
    }

    /**
     * Setter for the id
     * @param id id to be set
     */
    void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the id
     * @return id
     */
    Integer getId() {
        return this.id;
    }
}
