public class Movie {
    private int id; // Movie ID (primary key in the database)
    private String title;
    private String genre;
    private byte[] poster; // Poster image stored as a byte array
    private String summary;

    // Constructor
    public Movie(int id, String title, String genre, byte[] poster, String summary) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.poster = poster;
        this.summary = summary;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
