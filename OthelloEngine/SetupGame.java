package othello;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * This class is responsible for setting up the Othello game. It achieves this primarily by creating
 * new instances of the Board, Referee, and Controls class.
 */
public class SetupGame {
    private Controls controls;
    private Board board;
    private Referee referee;
    private Pane pane;

    /**
     * The constructor for the SetupGame class. The constructor is called by the PaneOrganizer class
     * to being setting up the Game. The constructor's main roles is to create a new instance
     * of the Controls class by passing in itself as a parameter, and to also create a new instance
     * of the Board class, so that the Othello board can be seen by the user.
     */
    public SetupGame(Pane gamePane){
        this.pane=gamePane;
        this.controls=new Controls(this);
        this.board=new Board(this.pane);

    }

    /**
     * This is a getter method for the controls VBox created by the instance of the Controls
     * class which was instantiated in the constructor of SetupGame. This method is called by
     * PaneOrganizer to add the controls VBox to the BorderPane root node.
     */
    public Controls getControls() {
        return this.controls;
    }

    /**
     * This method is responsible for creating a new instance of the Referee class, which oversees the
     * Players, turn taking, and other important information about the state of the Othello board. The
     * method is called when the "Apply Settings" button, existing in the controls VBox, is clicked.
     */
    public void createPlayers(int whitePlayerMode, int blackPlayerMode,
                              Label whiteScore, Label blackScore, Label turnCounter) {

        if (this.referee==null) {
            this.referee=new Referee(whitePlayerMode, blackPlayerMode, this.board, this.pane,
                    whiteScore, blackScore, turnCounter);
        }
    }

    /**
     * This method returns information about the state of the Othello game, particularly whether the
     * game has started or not, to the Controls class. The purpose of this method is to make
     * sure that another Referee cannot be created while a game is currently unfolding.
     */
    public boolean gameStarted() {
        if (this.referee!=null) {
            return true;
        } return false;
    }

    /**
     * This method is used to reset the game when the Reset button, found in the Controls VBox, is
     * clicked. The method delegates most of the logic of resetting the game to the instance of the
     * Referee class, and then garbage collects the current instance of Referee to allow for
     * players to change after a reset.
     */
    public void resetGame() {
        this.referee.resetGame();
        this.referee=null;
    }
}
