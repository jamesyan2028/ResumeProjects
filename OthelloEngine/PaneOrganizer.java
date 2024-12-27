package othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * The PaneOrganizer is responsible for setting up the root node, and all the nodes under the
 * root node. The PaneOrganizer also instantiates the SetUpGame class, which creates a VBox which
 * contains all the features relevant to setting up and managing a game of Othello.
 */
public class PaneOrganizer {
    private BorderPane root;

    /**
     * The constructor for the PaneOrganizer class. The constructor creates a new instance of
     * BorderPane and sets it as the root node. The constructor also creates a new instance
     * of the SetupGame class, which helps to start the Othello game, and adds the Controls
     * VBox created in the SetUpGame class to the root node.
     */
    public PaneOrganizer() {
        this.root=new BorderPane();
        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);
        this.root.setRight(new SetupGame(gamePane).getControls().getPane());
    }

    /**
     * Getter method for the BorderPane root node. Called by the App class to add the root node
     * to the scene.
     */
    public BorderPane getRoot() {
        return this.root;
    }
}
