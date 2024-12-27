package othello;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * This class represents a human player in the Othello game, and primarily contains methods
 * governing how a human player is allowed to place a piece.
 */
public class HumanPlayer extends Player{
    private boolean isBlack;
    private Board board;
    private Pane pane;
    private boolean gameRunning;

    /**
     * The constructor for the HumanPlayer class instantiates the Player superclass, and initializes
     * relevant instance variables.
     */
    public HumanPlayer(Board board, boolean isBlack, Pane pane, Referee referee) {
        super(board, isBlack, referee);

        this.gameRunning=true;
        this.board=board;
        this.isBlack=isBlack;
        this.pane=pane;
    }

    /**
     * This method overrides an abstract method inherited from the super class, and governs how a
     * HumanPlayer can make a move. The method achieves this by generating a MouseEvent when the
     * pane in the center of the BorderPane is clicked, which calls a helper method which logically
     * determines how to place a piece.
     */
    @Override
    public void makeMove() {
        if (this.gameRunning) {
            this.pane.setOnMouseClicked((MouseEvent e)-> this.beginTurn(e));
        }
    }

    /**
     * This is the helper method called when the pane in the center of the BorderPane is clicked
     * during a HumanPlayer's turn. This method gets the position of the mouse at the time of clicking,
     * and determines which OthelloSquare the mouse was in when clicked. The method then passes these
     * integers as parameters into another helper method.
     */
    private void beginTurn(MouseEvent e) {
        int arrayXPos=(int)(e.getX()/Constants.SQUARE_DIM);
        int arrayYPos=(int)(e.getY()/Constants.SQUARE_DIM);

        this.checkValidMove(arrayXPos, arrayYPos);
    }

    /**
     * This method determines whether OthelloSquare the mouse was in at the time of click was a
     * valid move or not. If it is a valid move, calls a helper method which places the piece.
     * If the move was not valid, the method does nothing.
     */
    private void checkValidMove(int arrayXPos, int arrayYPos) {
        if (this.board.checkAllDirections(arrayXPos, arrayYPos, this.isBlack)) {
            this.takeTurn(arrayXPos, arrayYPos);
        }
    }

    /**
     * This method places a piece in the OthelloSquare by calling the placePiece() method inherited from
     * the Player superclass. The method then removes the MouseEvent generated from clicking the pane to
     * prevent the human player from going during the opponent's turn.
     */
    private void takeTurn(int arrayXPos, int arrayYPos) {
        this.placePiece(arrayXPos, arrayYPos);
        this.pane.setOnMouseClicked(null);
    }

    /**
     * This method overrides the abstract editGameRunning() method inherited from the player class
     */
    @Override
    public void editGameRunning(boolean gameRunning) {
        this.gameRunning=gameRunning;
    }

    /**
     * This method lets the referee know that the player is human, so the referee can decide
     * whether to highlight all valid moves.
     */
    @Override
    public boolean isHuman() {
        return true;
    }

}
