package othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the highest level class of the Othello project. The App class instantiates the PaneOrganizer
 * class, and establishes the stage and scene that lets the program be shown to the user.
  *
  */

public class App extends Application {

    /**
     * The constructor for the App class. This method instantiates PaneOrganizer, and creates the
     * scene and stage that lets the project be shown to the user.
     */
    @Override
    public void start(Stage stage) {
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot());
        stage.setScene(scene);
        stage.setTitle("Othello");
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
