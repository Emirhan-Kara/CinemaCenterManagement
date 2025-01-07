import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Movie {
    public int id; // Movie ID (primary key in the database)
    public String title; // Movie title
    public String genre; // Movie genre
    public byte[] poster; // Poster image stored as a byte array
    public String summary; // Movie summary

    // Constructor
    public Movie(int id, String title, String genre, byte[] poster, String summary) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.poster = poster;
        this.summary = summary;
    }

    // Method to set the poster from a file
    public void setPosterFromFile(File file) {
        try {
            this.poster = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save the poster to a file
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

    @Override
    public String toString() {
        return "Movie{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", genre='" + genre + '\'' +
               ", summary='" + summary + '\'' +
               '}';
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Getter for genre
    public String getGenre() {
        return genre;
    }

    public String getSummary()
    {
        return summary;
    }
 
    public byte[] getPoster()
    {
        return poster;
    }

    public int getId()
    {
        return id;
    }
}
