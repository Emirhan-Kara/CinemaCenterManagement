import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

public class CashierScene5_Bill extends CashierProperties {

    @FXML
    private WebView billWebView;

    @FXML
    private Button endButton;
    
    @FXML
    public void initialize()
    {
        // to run the command in JavaFX thread
        Platform.runLater(() -> {
            billWebView.getEngine().load(CashierProperties.billURL);
        });

        
    }

    @FXML
    void endButtonClicked(MouseEvent event) throws Exception
    {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        App.loadScene("cashierScene1.fxml");
    }   
}
