import java.sql.Date;
import java.sql.Time;

/**
 * Represents a movie session, containing details about the session's ID, hall, movie, date, and time.
 */
public class Sessions {
    private int id;
    private int hallId;
    private int movieId;
    private Date date;
    private Time time;

    /**
     * Full constructor for creating a Sessions object with all attributes.
     *
     * @param id     the unique identifier for the session.
     * @param hallId the ID of the hall where the session is held.
     * @param movieId the ID of the movie being shown in the session.
     * @param date   the date of the session.
     * @param time   the time of the session.
     */
    public Sessions(int id, int hallId, int movieId, Date date, Time time) {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    /**
     * Constructor for creating a Sessions object without an ID, for use cases where the ID is not yet assigned.
     *
     * @param hallId the ID of the hall where the session is held.
     * @param movieId the ID of the movie being shown in the session.
     * @param date   the date of the session.
     * @param time   the time of the session.
     */
    public Sessions(int hallId, int movieId, Date date, Time time) {
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    /**
     * Default constructor for creating a Sessions object with no initial attributes.
     */
    public Sessions() {
    }

    /**
     * Gets the session's unique identifier.
     *
     * @return the session ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ID of the hall where the session is held.
     *
     * @return the hall ID.
     */
    public int getHallId() {
        return hallId;
    }

    /**
     * Gets the ID of the movie being shown in the session.
     *
     * @return the movie ID.
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Gets the date of the session.
     *
     * @return the session date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the time of the session.
     *
     * @return the session time.
     */
    public Time getTime() {
        return time;
    }

    /**
     * Sets the session's unique identifier.
     *
     * @param id the session ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the ID of the hall where the session is held.
     *
     * @param hallId the hall ID to set.
     */
    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    /**
     * Sets the ID of the movie being shown in the session.
     *
     * @param movieId the movie ID to set.
     */
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    /**
     * Sets the date of the session.
     *
     * @param date the session date to set.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the time of the session.
     *
     * @param time the session time to set.
     */
    public void setTime(Time time) {
        this.time = time;
    }

    /**
     * Provides a string representation of the Sessions object.
     *
     * @return a string describing the session's attributes.
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
