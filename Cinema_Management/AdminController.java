import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminController {
    @FXML private TextField titleField;
    @FXML private TextArea summaryArea;
    @FXML private TextField genreField;

    public void handleAddMovie() {
        String title = titleField.getText();
        String summary = summaryArea.getText();
        String genre = genreField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Movies (title, genre, summary) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, summary);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateMovie() {
        // Similar to handleAddMovie but with an UPDATE query
    }
}
