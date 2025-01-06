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
