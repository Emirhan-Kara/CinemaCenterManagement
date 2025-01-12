import java.sql.Date;
import java.sql.Time;

public class Schedule {
    Integer id;
    private int hallId;
    private String movie;
    private Date date;
    private Time time;

    public Schedule(int hallId, String movie, Date date, Time time) {
        this.hallId = hallId;
        this.movie = movie;
        this.date = date;
        this.time = time;
    }

    public Schedule(int hallId, String movie, Date date, Time time, Integer id) {
        this.hallId = hallId;
        this.movie = movie;
        this.date = date;
        this.time = time;
    }

    Sessions getSession(Schedule schedule) {
        return new Sessions(this.hallId, DatabaseConnection.getMovie_byTitle(this.movie).getId(), this.date, this.time);
    }

    public Schedule() {
        // Default constructor
    }

    public Date getDate() {
        return this.date;
    }

    public Integer getHallId() {
        return this.hallId;
    }

    public Time getTime() {
        return this.time;
    }

    public String getMovie() {
        return this.movie;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    void setId(Integer id) {
        this.id = id;
    }

    Integer getId() {
        return this.id;
    }
}
