import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class App extends Application
{
    private static Stage primaryStage;

    
    @Override // Override the start method in the Application class
    public void start(Stage stage)
    {
        try
        {
            primaryStage = stage;
            loadScene("Login.fxml");
            primaryStage.setTitle("Group 17 CinemaCenter");
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void loadScene(String fxmlPath) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
        primaryStage.setScene(new Scene(loader.load()));
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}