import java.sql.Date;
import java.sql.Time;

public class Sessions {
    private int id;
    private int hallId;
    private int movieId;
    private Date date;
    private Time time;

    public Sessions() {
        // Default constructor
    }

    public Sessions(int id, int hallId, int movieId, Date date, Time time)
    {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    // overloaded constructor
    public Sessions(int hallId, int movieId, Date date, Time time)
    {
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }

    // Copy Constructor
    public Sessions(Sessions original)
    {
        this.id = original.id;
        this.hallId = original.hallId;
        this.movieId = original.movieId;
        this.date = original.date;
        this.time = original.time;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getHallId() {
        return hallId;
    }

    public int getMovieId() {
        return movieId;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }


    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }


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
