import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import controller.Scheduler;

/**
 * Entry point for the JavaFX application.
 * 
 * This class initializes the primary stage and sets up the main GUI
 * by invoking the Scheduler controller.
 */

public class Main extends Application {

    /**
     * Called automatically when the JavaFX application is launched.
     * 
     * Sets up the main window (stage), loads the root scene from Scheduler,
     * and displays the application window.
     *
     * @param primaryStage the primary stage for this application
     */

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Scheduler");

        // Get the singleton instance of the Scheduler controller
        Scheduler scheduler = Scheduler.getScheduler();

        // Create a scene using the scheduler's root UI node
        Scene scene = new Scene(scheduler.getRoot(), 800, 600);

        // Set the scene and display the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main method that launches the JavaFX application.
     *
     * @param args command-line arguments passed to the program
     */
    public static void main(String[] args) {
        launch(args);
    }
}
