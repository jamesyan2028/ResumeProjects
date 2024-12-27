package tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the highest level class of the tetris project. The App class instantiates the Pane Organizer
 * class, and establishes the stage and scene that allows the project to be shown to the user.
 */

public class App extends Application {

    /**
     *
     * The constructor for the App class. The constructor instantiates PaneOrganizer, and sets up
     * the Scene and Stage instances so that the project can be displayed to the user.
     */
    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT + 50);
        stage.setScene(scene);
        stage.setTitle("Tetris");
        stage.show();
    }

    /*
     * Here is the mainline! No need to change this.
     */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
