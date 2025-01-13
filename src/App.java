import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * Main class for the application
 */
public class App extends Application
{
    /**
     * Main stage that holds the scenes
     */
    private static Stage primaryStage;

    
    /**
     * Overried start method from the Application class
     */
    @Override
    public void start(Stage stage)
    {
        try
        {
            primaryStage = stage;
            loadScene("Login.fxml");
            //loadScene("Login.fxml");
            primaryStage.setTitle("Group 17 CinemaCenter");
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * To switch scenes throughout the application
     * @param fxmlPath name of the fxmlFile
     * @throws Exception
     */
    public static void loadScene(String fxmlFileName) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlFileName));
        primaryStage.setScene(new Scene(loader.load()));
    }

    /**
     * main method to call launch method
     * @param args arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
