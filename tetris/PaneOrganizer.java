package tetris;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * The PaneOrganizer class is responsible for setting up the root node, and all the child nodes
 * under the root node. The PaneOrganizer class also instantiates the Game class, which populates
 * the nodes and controls the logic for the program.
 */
public class PaneOrganizer {
    private BorderPane root;

    /**
     * The constructor for the PaneOrganizer class creates a new instance of the BorderPane class as
     * a root node, and also instantiates the child nodes of the root. The constructor then creates
     * a new instance of the Game class to begin the program.
     */
    public PaneOrganizer() {
        this.root=new BorderPane();

        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);

        //This section creates two HBoxes wrapped inside a VBox to display the Buttons and Score
        VBox wrapperBox = new VBox();
        HBox buttonBox = new HBox();
        HBox scoreBox = new HBox();
        wrapperBox.getChildren().addAll(scoreBox, buttonBox);
        wrapperBox.setStyle("-fx-background-color: #87CEFA");
        wrapperBox.setAlignment(Pos.CENTER);
        this.root.setBottom(wrapperBox);

        new Game(gamePane, scoreBox, buttonBox);

    }

    public BorderPane getRoot() {
        return this.root;
    }
}
