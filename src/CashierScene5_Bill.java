import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

/**
 * Controller class for the scene 5 (receipt showing)
 */
public class CashierScene5_Bill extends CashierProperties {

    /**
     * webview to show the HTML receipt
     */
    @FXML
    private WebView billWebView;

    /**
     * end button
     */
    @FXML
    private Button endButton;
    
    /**
     * initializer that gets the bill from the database and puts it into webvies
     */
    @FXML
    public void initialize()
    {
        // to run the command in JavaFX thread
        Platform.runLater(() -> {
            billWebView.getEngine().load(CashierProperties.billURL);
        });

        
    }

    /**
     * event handler for end button
     * @param event mouse event
     * @throws Exception load scene
     */
    @FXML
    void endButtonClicked(MouseEvent event) throws Exception
    {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        App.loadScene("cashierScene1.fxml");
    }   
}
