package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is responsible for wrapping an instance of Javafx Rectangle. Instances of this class
 * represent one square in the 2d array of OthelloSquare which make up the Board. Most of this
 * class is responsible for graphically updating the game after a move is played, but some methods
 * have been overloaded to eliminate the need for a Pane parameter to allow the ComputerPlayer
 * to accurately update the state of hypothetical boards.
 */
public class OthelloSquare {
    private OthelloPiece piece;
    private Rectangle gameSquare;
    private int value;
    private Pane pane;
    private int arrayXPos;
    private int arrayYPos;

    /**
     * This constructor for OthelloSquare takes in a Pane, and is thus used to create the OthelloSquares
     * on the visible Board displayed in the center of the BorderPane root node. This constructor
     * instantiates relevant instance variables, and calls a helper method to graphically set up the
     * square.
     */
    public OthelloSquare(Color color, int arrayXPos, int arrayYPos, Pane pane) {
        this.pane=pane;
        this.arrayXPos=arrayXPos;
        this.arrayYPos=arrayYPos;

        this.setUpSquare(color);
    }

    /**
     * This constructor for OthelloSquare does not take in a Pane, and thus is used by the ComputerPlayer
     * when creating hypothetical boards when determining the best move to play. This constructor only
     * initializes the two instance variables corresponding to this OthelloSquare's position in the
     * board 2d array.
     */
    public OthelloSquare(int arrayXPos, int arrayYPos) {
        this.arrayXPos=arrayXPos;
        this.arrayYPos=arrayYPos;
    }

    /**
     * This method graphically sets up the OthelloSquare and adds it to the Pane in the center of the
     * BorderPane.
     */
    private void setUpSquare(Color color) {
        this.gameSquare=new Rectangle(this.arrayXPos*Constants.SQUARE_DIM,this.arrayYPos*Constants.SQUARE_DIM,
                Constants.SQUARE_DIM, Constants.SQUARE_DIM);
        this.gameSquare.setFill(color);
        this.gameSquare.setStroke(Color.BLACK);

        this.pane.getChildren().add(this.gameSquare);
    }

    /**
     * This method is called by the Board class to assign each non-border OthelloSquare a positional
     * value used by the ComputerPlayer when calculating the best move.
     */
    public void setValue(int value) {
        this.value=value;
    }

    /**
     * This is a getter method for the value instance variable, and is used by the ComputerPlayer
     * when calculating the positional advantage in potential boards to determine the best
     * possible move.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * This method is called by the Board class whenever a Player determines that they would like to
     * play a piece. The method causes the OthelloSquare that the player would like to place a piece
     * in to initialize an instance of OthelloPiece as an instance variable.
     */
    public void createPiece(boolean isBlack) {
        if (this.pane!=null) {
            this.piece = new OthelloPiece(this.arrayYPos, this.arrayXPos, isBlack, this.pane);
        } else {
            this.piece=new OthelloPiece(this.arrayYPos, this.arrayXPos, isBlack);
        }
    }

    /**
     * This method allows the instance of Board to determine whether a particular OthelloSquare
     * contains a piece or not, and if it does, what color piece the OthelloSquare contains. The method
     * achieves this by returning an integer value corresponding to whether the square is empty, contains
     * a white piece, or contains a black piece.
     */
    public int checkPiece() {
        if (this.piece==null) {
            return 0;
        } if (this.piece.isBlack()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * This method is called by the Board class, and causes the instance of OthelloPiece stored as an
     * instance variable for this particular OthelloSquare to flip. The functionality of a piece
     * flipping is delegated to the OthelloPiece class.
     */
    public void flipPiece() {
        this.piece.flipPiece();
    }

    /**
     * This method is called by the Board class to highlight a particular OthelloSquare yellow,
     * indicating that it is a valid move for the current player's turn.
     */
    public void setValidMove() {
        this.gameSquare.setFill(Constants.VALID_MOVE_COLOR);
    }

    /**
     * This method returns an OthelloSquare's color to its original color.
     */
    public void resetSquareColor() {
        this.gameSquare.setFill(Constants.SQUARE_DEFAULT_COLOR);
    }

    /**
     * This method is called by the Board class to denote that a particular OthelloSquare was
     * the position where the opposing player last played by highlighting the square blue.
     */
    public void setLastMove() {
        this.gameSquare.setFill(Constants.LAST_MOVE_COLOR);
    }

    /**
     * This method tells the instance of OthelloPiece to remove itself graphically from the Pane, and
     * also logically removes the OthelloPiece instance variable from a particular OthelloSquare. This
     * method is called when the game is reset.
     */
    public void removePiece() {
        if (this.piece!=null) {
            this.piece.removePiece();
            this.piece=null;
        }

    }
}
