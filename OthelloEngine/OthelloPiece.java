package othello;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * This class is responsible for wrapping an instance of Javafx Circle, which represents an Othello piece
 * played by either player. The class is responsible for graphically managing the pieces played by
 * both players.
 */
public class OthelloPiece {
    private Circle piece;
    private Pane pane;
    private boolean isBlack;

    /**
     * This constructor for the OthelloPiece class takes in a Pane, and thus is called
     * by the instance of the Board class that is visible on screen to the user in the center of
     * the BorderPane. The initializes the relevant instance variable, and then calls a helper
     * method to set up the wrapped instance of Javafx circle.
     */
    public OthelloPiece(int arrayYPos, int arrayXPos, boolean isBlack, Pane pane) {
        this.pane=pane;
        this.setUpPiece(arrayYPos, arrayXPos, isBlack);
    }

    /**
     * This constructor for the OthelloPiece class does not take in a Pane, and thus is called
     * by instances of the board class generated from teh ComputerPlayer when determining the best
     * move to play. The constructor calls a helper method to set up the wrapped instance of Javafx
     * circle.
     */
    public OthelloPiece(int arrayYPos, int arrayXPos, boolean isBlack) {
        this.setUpPiece(arrayYPos, arrayXPos, isBlack);
    }

    /**
     * This method sets up an instance of Javafx circle that is wrapped by every instance of
     * OthelloPiece. If the Pane instance variable has been initialized, the circle is added to
     * the pane.
     */
    private void setUpPiece(int arrayYPos, int arrayXPos, boolean isBlack) {
        this.piece=new Circle(arrayXPos*Constants.SQUARE_DIM+Constants.SQUARE_DIM/2,
                arrayYPos*Constants.SQUARE_DIM+Constants.SQUARE_DIM/2, Constants.PIECE_RADIUS);
        if (isBlack) {
            this.piece.setFill(Color.BLACK);
        } else {
            this.piece.setFill(Color.WHITE);
        }
        if (this.pane!=null) {
            this.pane.getChildren().add(this.piece);
        }
        this.isBlack=isBlack;
    }

    /**
     * This method returns a boolean representing whether the wrapped instance of Javafx circle is
     * the color black or not.
     */
    public boolean isBlack() {
        return this.isBlack;
    }

    /**
     * This method is responsible for "flipping" an OthelloPiece. The method does this graphically
     * through an animation, and then logically updates the instance variable associated with the piece
     * color to reflect this change logically.
     */
    public void flipPiece() {
        Color newColor;
        if (this.isBlack) {
            newColor = Color.WHITE;
        } else {
            newColor=Color.BLACK;
        }
        KeyFrame kf = new KeyFrame(Duration.seconds(0.4), new KeyValue(this.piece.fillProperty(), newColor));
        Timeline timeline = new Timeline(kf);
        timeline.setCycleCount(1);
        timeline.play();
        this.isBlack=!this.isBlack;
    }

    /**
     * This method graphically removes the wrapped Javafx circle from the Pane. The method
     * is called in order for the reset functionality to work.
     */
    public void removePiece() {
        this.pane.getChildren().remove(this.piece);
    }
}
