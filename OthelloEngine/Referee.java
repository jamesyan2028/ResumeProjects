package othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The Referee class is responsible for instantiating the proper Players to play a game of Othello,
 * as determined by the inputs from the user in the controls VBox. This class also makes decisions
 * regarding how the game should proceed given information of the current state of the board.
 */
public class Referee {
    private Player white;
    private Player black;
    private Board board;
    private int turnTracker;
    private int whiteScore;
    private int blackScore;
    private Label whiteScoreLabel;
    private Label blackScoreLabel;
    private Label turnLabel;
    private Timeline timeline;
    private Pane pane;

    /**
     * The constructor for the Referee class sets up all the instance variables relevant to keeping track
     * of the state of the game, and calls helper methods to set up players and additional functionality
     * to begin the game.
     */
    public Referee(int whitePlayerMode, int blackPlayerMode, Board board, Pane pane, Label whiteScore,
                   Label blackScore, Label turnLabel) {
        this.pane=pane;
        this.board=board;
        this.turnTracker=1;
        this.whiteScore=2;
        this.blackScore=2;
        this.whiteScoreLabel=whiteScore;
        this.blackScoreLabel=blackScore;
        this.turnLabel=turnLabel;

        this.setUpReferee(whitePlayerMode, blackPlayerMode, pane);
    }

    /**
     * This helper method factors out the many helper methods called in the constructor of Referee
     * for better readability. These helper methods set up both Players, the TurnLabel, and the Timeline
     * responsible for creating delay between computer moves.
     */
    private void setUpReferee(int whitePlayerMode, int blackPlayerMode, Pane pane) {
        this.setUpWhite(whitePlayerMode, pane);
        this.setUpBlack(blackPlayerMode, pane);
        this.setUpTimeline();
        this.updateTurnLabel();
    }

    /**
     * This helper method is responsible for setting up the Timeline which creates delay between
     * the moves of a ComputerPlayer. This will allow the users to watch a Computer vs Computer
     * game unfold.
     */
    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(0.1), (ActionEvent e) -> this.checkValidMove());
        this.timeline=new Timeline(kf);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    /**
     * This method instantiates the player with the white pieces, given the choices the user
     * selected from the Controls VBox.
     */
    private void setUpWhite(int whitePlayerMode, Pane pane) {
        if (whitePlayerMode==0) {
            this.white=new HumanPlayer(this.board, false, pane ,this);
        } else {
            this.white=new ComputerPlayer(whitePlayerMode, this.board, false, this);
        }
    }

    /**
     * This method instantiates the Player with the black pieces, given the choices the user selected
     * from the Controls VBox.
     */
    private void setUpBlack(int blackPlayerMode, Pane pane) {
        if (blackPlayerMode==0) {
            this.black=new HumanPlayer(this.board, true, pane, this);
        } else {
            this.black=new ComputerPlayer(blackPlayerMode, this.board, true, this);
        }
    }

    /**
     * This method is responsible for the turn taking functionality of the game. The method
     * is regularly called in the Timeline, and tells each player when it is their turn to make
     * a move. The method then calls another helper method to update the game. This method also
     * highlights all the valid moves if the player taking their turn is human to help the user
     * place their piece.
     */
    private void checkValidMove() {
        if (this.turnTracker==1) {
            if (this.white.isHuman()) {
                this.board.highlightValidMoves(false);
            }
           this.white.makeMove();
           this.updateGame();
        } else if (this.turnTracker==-1){
            if (this.black.isHuman()) {
                this.board.highlightValidMoves(true);
            }
            this.black.makeMove();
            this.updateGame();
        }
    }

    /**
     * This method is called by the Players when they have finished making their move, and
     *helps the referee to manage turn taking.
     */
    public void updateTurnTracker() {
        this.turnTracker=this.turnTracker*-1;
    }

    /**
     * This method is called whenever a Player places a new piece on the board. The method
     * updates all relevant Panes existing in the controls VBox with new information about the
     * board, checks whether the game has ended or whether a player needs to repeat a turn, and
     * creates the delay between computer moves by pausing and resuming the Timeline.
     */
    private void updateGame() {
        this.timeline.pause();
        this.updateScoreLabel();
        this.updateTurnLabel();
        if (this.board.checkGameOver()) {
            this.endGame();
        } if (this.board.checkPlayerRepeatTurn(this.turnTracker==-1)) {
            this.turnTracker=this.turnTracker*-1;
        }
        this.timeline.play();
    }

    /**
     * This method updates the Label displaying each of the player's score in the Controls Vbox.
     * The method is called after either player makes a move to update the Labels to reflect
     * the new board state.
     */
    private void updateScoreLabel() {
        this.whiteScore=this.board.countPieces(false);
        this.blackScore=this.board.countPieces(true);
        this.whiteScoreLabel.setText("White: "+this.whiteScore);
        this.blackScoreLabel.setText("Black: "+this.blackScore);
    }

    /**
     * This method updates the Label displaying information on which player's turn it is to the user.
     * The method is called after either player places a piece to reflect new information about
     * the state of the game.
     */
    private void updateTurnLabel() {
        if (this.turnTracker==1) {
            this.turnLabel.setText("White to Move");
        } else {
            this.turnLabel.setText("Black to Move");
        }
    }

    /**
     * This method resets the Label displaying information about the current player's turn to its
     * original state once the reset button has been pressed.
     */
    private void resetTurnLabel() {
        this.turnLabel.setText("");
    }

    /**
     * This method is called whenever the reset button is pressed. The delegates resetting the board
     * to the instance of the Board class, and then resets all instance variables and other information
     * about the state of the game managed in the referee class.
     */
    public void resetGame() {
        this.board.resetGame();
        this.resetTurnLabel();
        this.updateScoreLabel();
        this.timeline.stop();
        this.pane.setOnMouseClicked(null);
        this.white=null;
        this.black=null;
    }

    /**
     * This method is called whenever the Game has ended. The method stops both players from
     * placing pieces, and updates the turn Label to display the winner of the game, or whether
     * it was a draw.
     */
    private void endGame() {
        this.white.editGameRunning(false);
        this.black.editGameRunning(false);
        if (this.whiteScore>this.blackScore) {
            this.turnLabel.setText("Game Over. White wins");
        } if (this.blackScore>this.whiteScore) {
            this.turnLabel.setText("Game Over. Black wins");
        } if (this.whiteScore==this.blackScore) {
            this.turnLabel.setText("Game Over. It's a Draw");
        }
    }

}
