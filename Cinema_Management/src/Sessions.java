import java.sql.Date;
import java.sql.Time;

public class Sessions {
    private int id;
    private int hallId;
    private int movieId;
    private Date date;
    private Time time;

    public Sessions(int id, int hallId, int movieId, Date date, Time time) {
        this.id = id;
        this.hallId = hallId;
        this.movieId = movieId;
        this.date = date;
        this.time = time;
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
