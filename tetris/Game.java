package tetris;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This is the top level logic class for the project. The Game class is in charge of determining
 * what the instances of the TetrisPieces class are allowed to do, and governs how the player
 * interacts with the game. The Game class calls methods in the TetrisPiece and Board class to
 * determine the state of the game in order to decide what happens next.
 */
public class Game {
    private TetrisPiece currPiece;
    private Pane gamePane;
    private Pane pausePane;
    private Pane gameOverPane;
    private Board gameBoard;
    private Timeline timeline;
    private boolean isPlaying;
    private int score;
    private Label scoreLabel;

    /**
     * This is the constructor for the Game class. The constructor takes in the relevant nodes created
     * in the PaneOrganizer class, and initializes some class instance variables. The constructor then
     * calls helper methods to populate and set up the game. The constructor then calls the
     * setUpTimeline() method which starts the game.
     */
    public Game(Pane gamePane, HBox scoreBox, HBox buttonBox) {
        this.gamePane=gamePane;
        this.gamePane.setBackground(new Background(new BackgroundFill
                (Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        this.isPlaying=true;
        this.score=0;
        this.pausePane= this.buildTextPane("Game Paused");
        this.gameOverPane=this.buildTextPane("Game Over");
        this.gameBoard= new Board(gamePane);

        this.setUpButtonPane(buttonBox);
        this.setUpScoreBox(scoreBox);
        this.setUpPane();

        this.setUpTimeline();
    }


    /**
     * This helper method sets up one of the two HBoxes passed to the Game class constructor.
     * This method is responsible for building the HBox containing the two buttons,
     * "Quit" and "Restart."
     */
    private void setUpButtonPane(HBox buttonBox) {
        Button quitButton = new Button("Quit");
        quitButton.setOnAction((ActionEvent e) -> System.exit(0));
        quitButton.setFocusTraversable(false);
        quitButton.setPrefSize(60, 25);

        Button restartButton = new Button("Restart");
        restartButton.setOnAction((ActionEvent e) -> this.restartGame());
        restartButton.setFocusTraversable(false);
        restartButton.setPrefSize(60, 25);

        buttonBox.getChildren().addAll(quitButton, restartButton);
        buttonBox.setAlignment(Pos.CENTER);
    }

    /**
     * This method is called by the lambda expression associated with the "Restart" button set up
     * in the setUpButtonPane() method. This method effectively restarts the game by modifying
     * some instance variables and calling other helper methods to logically clear the 2d Array
     * and graphically clear the screen.
     */
    private void restartGame() {
        this.gameBoard.restartGame();
        if (!this.isPlaying) {
            this.timeline.play();
        }
        this.isPlaying=true;
        this.score=0;
        this.updateScore();
        if (this.gamePane.getChildren().contains(this.gameOverPane)) {
            this.gamePane.getChildren().remove(this.gameOverPane);
        } if (this.gamePane.getChildren().contains(this.pausePane)) {
            this.gamePane.getChildren().remove(this.pausePane);
        }
    }

    /**
     * This helper method sets up one of the two HBoxes passed to the Game class constructor.
     * This method sets up the HBox which displays the score instance variable.
     */
    private void setUpScoreBox(HBox scoreBox) {
        this.scoreLabel = new Label();
        this.scoreLabel.setText("Score: "+ this.score);
        this.scoreLabel.setFont(new Font(18));

        scoreBox.getChildren().add(this.scoreLabel);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPrefHeight(25);
    }

    /**
     * This method updates the HBox displaying the score to accurately reflect the current
     * value of the score instance variable. The method is called whenever a row is cleared
     * or the game restarts.
     */
    private void updateScore() {
        this.scoreLabel.setText("Score: "+ this.score);
    }

    /**
     * This method sets up the Pane in the center of the BorderPane root node, allowing for
     * user input to affect the game.
     */
    private void setUpPane() {
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent e) -> this.handleKeyPress(e));
    }

    /**
     * This method determines what should happen when a user presses a key to interact with the game.
     * This method calls a relevant helper method depending on which key was pressed by the user.
     */
    private void handleKeyPress(KeyEvent e) {
        if (this.currPiece!=null) {
            switch (e.getCode()) {
                case LEFT:
                    if (this.isPlaying) {
                        this.movePieceLeft();
                    }
                    break;
                case RIGHT:
                    if (this.isPlaying) {
                        this.movePieceRight();
                    }
                    break;
                case UP:
                    if (this.isPlaying) {
                        this.rotatePiece();
                    }
                    break;
                case DOWN:
                    if (this.isPlaying) {
                        this.movePieceDown();
                    }
                    break;
                case SPACE:
                    if (this.isPlaying) {
                        this.moveToBottom();
                    }
                    break;
                case P:
                    this.handlePauseClick();
                    break;
                default:
                    break;
            }
        }
        e.consume();
    }

    /**
     * This method randomly creates a new instance of the TetrisPiece class, and initializes it
     * as the currPiece instance variable. The method also checks to see if the piece being created
     * immediately overlaps with any existing piece. This is done to fix the bug where after a loss
     * where the newest piece was generated on top of an existing piece, it would be impossible
     * to graphically restart the game because the squares under the newest piece have already
     * been garbage collected.
     */
    private void createNewPiece() {
        int randnum = (int)(Math.random()*7);
        switch (randnum) {
            case 0:
                this.checkOverLap(Constants.I_PIECE_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.I_PIECE_COORDS, this.gameBoard,
                        Constants.I_COLOR, false);
                break;
            case 1:
                this.checkOverLap(Constants.T_PIECE_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.T_PIECE_COORDS, this.gameBoard,
                        Constants.T_COLOR, false);
                break;
            case 2:
                this.checkOverLap(Constants.L_PIECE_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.L_PIECE_COORDS, this.gameBoard,
                        Constants.L_COLOR, false);
                break;
            case 3:
                this.checkOverLap(Constants.BACKWARDS_L_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.BACKWARDS_L_COORDS, this.gameBoard,
                        Constants.BACK_L_COLOR, false);
                break;
            case 4:
                this.checkOverLap(Constants.SQUARE_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.SQUARE_COORDS, this.gameBoard,
                        Constants.SQUARE_COLOR, true);
                break;
            case 5:
                this.checkOverLap(Constants.ZIG_ZAG_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.ZIG_ZAG_COORDS, this.gameBoard,
                        Constants.ZIGZAG_COLOR, false);
                break;
            default:
                this.checkOverLap(Constants.BACKWARDS_ZIG_COORDS);
                this.currPiece=new TetrisPiece(this.gamePane, Constants.BACKWARDS_ZIG_COORDS, this.gameBoard,
                        Constants.BACK_ZIG_COLOR, false);
                break;
        }
    }

    /**
     * This method checks to see if the newest piece created overlaps with any piece
     * currently on the board. If an overlap exists, this method calls a helper method in the
     * board class to graphically and logically remove the old squares from the program.
     */
    private void checkOverLap(int coords[][]) {
        for (int i=0; i<coords.length; i++) {
            if (!this.gameBoard.checkEmpty(coords)) {
                this.gameBoard.clearSquares(coords);
            }

        }
    }

    /**
     * This method instantiates the Timeline instance variable which always ensures that new
     * pieces are being created when old ones are added to the board, and that the current
     * piece always moves down slowly by calling relevant helper methods.
     */
    private void setUpTimeline() {
        KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), (ActionEvent e) -> {
            this.keyFrameActions();
        });
        this.timeline = new Timeline(kf);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.isPlaying=true;
        this.timeline.play();
    }

    /**
     * Helper method which determines whether the current piece should move down or
     * a new piece should be instantiated.
     */
    private void keyFrameActions() {
        if (this.currPiece==null) {
            this.createNewPiece();
        } else{
            this.movePieceDown();
        }
    }

    /**
     * This method checks to see if the TetrisPiece is able to move downwards by on TetrisSquare unit.
     * It does so by creating a new array of coordinates, which is the coordinates one TetrisSquare
     * down from the current coordinates. The method then calls a helper method in the
     * Board class to check to see if the new coordinates are open or not. If they are open,
     * the piece moves down. If they are not open, a helper method is called to update
     * instance variables and call other helper methods.
     */
    private void movePieceDown() {
        //Creating array of coordinates where the TetrisPiece wants to move
        int[][] currCoords = this.currPiece.getCoords();
        int[][] newcoords = new int[currCoords.length][currCoords[0].length];
        for (int i = 0; i < currCoords.length; i++) {
            newcoords[i][0] = currCoords[i][0];
            newcoords[i][1] = currCoords[i][1] + 1;
        }

        //Checks if the TetrisPiece can move to the new location, and determines piece behavior
        if (this.gameBoard.checkEmpty(newcoords)) {
            this.currPiece.moveDown();
        } else {
            this.pieceHitBottom();
        }
    }

    /**
     * This method is called by movePieceDown() when the current TetrisPiece can no longer move
     * down. The method updates instance variables in the Game class, and calls other helper methods
     * to add the piece to the Board and logically progress the game.
     */
    private void pieceHitBottom() {
        this.currPiece.addToBoard(this.gameBoard);
        this.currPiece=null;
        score+= Math.pow(this.gameBoard.checkCompleteRow(), 2)*100;
        if (this.gameBoard.checkGameOver()) {
            this.displayGameOver();
        }
        this.updateScore();
    }

    /**
     * This method is called by handleKeyPress whenever the left arrow key is pressed by the
     * user. The method checks to see if the current TetrisPiece can move to the left using
     * the same logic as movePieceDown(). If the squares to the left are empty, the method calls
     * a helper method in the TetrisPiece class to move the current piece to the left. Otherwise,
     * the method does nothing.
     */
    private void movePieceLeft() {
        int[][] currCoords = this.currPiece.getCoords();
        int[][] newcoords = new int[currCoords.length][currCoords[0].length];
        for (int i = 0; i < currCoords.length; i++) {
            newcoords[i][0] = currCoords[i][0]-1;
            newcoords[i][1] = currCoords[i][1];
        }

        if (this.gameBoard.checkEmpty(newcoords)) {
            this.currPiece.moveLeft();
        }
    }

    /**
     * This method is called by handleKeyPress whenever the right arrow key is pressed by the
     * user. The method checks to see if the current TetrisPiece can move to the right using
     * the same logic as movePieceDown(). If the squares to the right are empty, the method calls
     * a helper method in the TetrisPiece class to move the current piece to the right. Otherwise,
     * the method does nothing.
     */
    private void movePieceRight() {
        int[][] currCoords = this.currPiece.getCoords();
        int[][] newcoords = new int[currCoords.length][currCoords[0].length];
        for (int i = 0; i < currCoords.length; i++) {
            newcoords[i][0] = currCoords[i][0] + 1;
            newcoords[i][1] = currCoords[i][1];
        }
        if (this.gameBoard.checkEmpty(newcoords)) {
            this.currPiece.moveRight();
        }
    }

    /**
     * This method is called by handleKeyPress whenever the up arrow key is pressed by the
     * user. The method checks to see if the current TetrisPiece can rotate using
     * the same logic as movePieceDown(), but with some additional calculations to determine
     * where the piece should be if rotated. If the squares in the Board where the rotated piece would
     * be are empty, the method calls a helper method in the TetrisPiece class to rotate the piece.
     * Otherwise, the method does nothing.
     */
    private void rotatePiece() {
        //Creating array of coordinates of where the piece would be if rotated
        int[][] currCoords = this.currPiece.getCoords();
        int[][] newcoords = new int[currCoords.length][currCoords[0].length];
        int centerOfRotX=this.currPiece.getCoords()[0][0];
        int centerOfRotY=this.currPiece.getCoords()[0][1];
        for (int i = 0; i < currCoords.length; i++) {
            newcoords[i][0] = centerOfRotX-centerOfRotY+currCoords[i][1];
            newcoords[i][1] = centerOfRotY+centerOfRotX-currCoords[i][0];
        }

        //Checkin to see if the squares the current piece would be if rotated are empty
        if (!this.currPiece.checkSquare() && this.gameBoard.checkEmpty(newcoords)) {
            this.currPiece.rotate(centerOfRotX, centerOfRotY);
        }
    }

    /**
     * This method is called by handleKeyPress() whenever the space bar is pressed by the user.
     * The method moves the current TetrisPiece to the lowest point it can go by repeatedly
     * calling the moveDown() method in a while loop.
     */
    private void moveToBottom() {
        while (this.currPiece!=null) {
            this.movePieceDown();
        }
    }

    /**
     * This method displays the GameOver pane instance variable on screen whenever the player
     * loses. The method also updates some instance variables to reflect that the game is over.
     */
    private void displayGameOver() {
        this.gamePane.getChildren().add(this.gameOverPane);
        this.timeline.stop();
        this.isPlaying=false;
    }

    /**
     * This method is called by the handleKeyPress() method whenever the user clicks the
     * "P" key. If the game is currently playing, as determined by the isPlaying instance
     * variable, the method pauses the game by stopping the timeline and displaying the
     * pausePane instance variable by adding it to gamePane. If the game is currently paused,
     * the method resumes the timeline, removes the pausePane from gamePane, and updates
     * the relevant instance variable.
     */
    private void handlePauseClick() {
        if (this.isPlaying) {
            this.timeline.stop();
            this.gamePane.getChildren().add(this.pausePane);
            this.isPlaying=false;
        } else {
            this.timeline.play();
            this.gamePane.getChildren().remove(this.pausePane);
            this.isPlaying=true;
        }
    }

    /**
     * This helper method is called in the constructor of the Game class to instantiate
     * the pausePane and gameOverPane instance variables.
     */
    private Pane buildTextPane(String message) {
        Pane pane = new Pane();
        Label textLabel = new Label();
        textLabel.setText(message);
        textLabel.setAlignment(Pos.CENTER);
        textLabel.setFont(new Font(24));
        textLabel.setTextFill(Color.WHITE);
        pane.getChildren().add(textLabel);
        pane.setLayoutX(120);
        pane.setLayoutY(310);
        return pane;
    }

}
