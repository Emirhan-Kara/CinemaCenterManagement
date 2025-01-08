import java.sql.Time;
import java.sql.Date;

public class Sessions {
    public int id;
    public int hallId;
    public int movieId;
    public Date date; // Fully qualified name
    public Time time; // Fully qualified name

    public Sessions(int id, int hallId, int movieId, Date date, Time time) {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
    }
     // Getters and Setters
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", hallId=" + hallId +
                ", movieId=" + movieId +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
