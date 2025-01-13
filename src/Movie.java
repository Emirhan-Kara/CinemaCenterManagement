import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Movie {
    public int id; // Movie ID (primary key in the database)
    public String title; // Movie title
    public String genre; // Movie genre
    public byte[] poster; // Poster image stored as a byte array
    public String summary; // Movie summary

    /**
     * Constructor for the Movie class
     * @param id
     * @param title
     * @param genre
     * @param poster
     * @param summary
     */
    public Movie(int id, String title, String genre, byte[] poster, String summary) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.poster = poster;
        this.summary = summary;
    }

    /**
     * Default constructor for the Movie class
     */
    public Movie() {
        
    }

    /**
     * Copy constructor for the Movie class
     * @param movie
     */
    public Movie(Movie movie) {
        this.title = movie.title;
        this.genre = movie.genre;
        this.summary = movie.summary;
        this.poster = movie.poster != null ? movie.poster.clone() : null;
        this.id = movie.id;
    }

    /**
     * Method to set the movie poster, ImageView, from a file
     * @param file
     */
    public void setPosterFromFile(File file) {
        try {
            this.poster = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to save the movie poster to a file
     * @param file
     */
    public void savePosterToFile(File file) {
        try {
            if (this.poster != null) {
                Files.write(file.toPath(), this.poster);
            } else {
                System.out.println("No poster data to save.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get string representation of the Movie object
     * @return
     */
    @Override
    public String toString() {
        return "Movie{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", summary='" + summary + '\'' +
               '}';
    }

    /**
     * Getter for title
     * @return string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for genre
     * @return genre of the movie
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Setter for genre
     * @param genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Getter for summary
     * @return summary of the movie
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Setter for summary
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Getter for poster
     * @return poster in byte array format
     */
    public byte[] getPoster() {
        return poster;
    }

    /**
     * Setter for poster
     * @param poster
     */
    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    /**
     * Getter for id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
}
