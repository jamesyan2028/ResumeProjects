package tetris;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is responsible for wrapping an instance of Javafx Rectangle. Instances of this
 * class represent one square in the Tetris game, and are the building blocks of the whole
 * program.
 */
public class TetrisSquare {
    private Rectangle tetrisSquare;
    private int arrayXPos, arrayYPos;
    private Pane gamePane;

    /**
     * The constructor for the TetrisSquare class initializes some instance variables
     * and calls a helper method to set up the TetrisSquare.
     */
    public TetrisSquare(Pane gamePane, Color color, int xPos, int yPos) {
        this.setUpSquare(gamePane, xPos, yPos, color);
        this.arrayXPos=xPos;
        this.arrayYPos=yPos;
        this.gamePane=gamePane;

    }

    /**
     * This method graphically adds the instance of Javafx Rectangle that TetrisSquare wraps
     * to the Pane where the tetris game is played.
     */
    private void setUpSquare(Pane gamePane, int xPos, int yPos, Color color) {
        this.tetrisSquare=new Rectangle(xPos,yPos, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.tetrisSquare.setFill(color);
        this.tetrisSquare.setX(xPos*Constants.SQUARE_WIDTH);
        this.tetrisSquare.setY(yPos*Constants.SQUARE_WIDTH);
        this.tetrisSquare.setStroke(Color.BLACK);
        this.gamePane=gamePane;
        this.gamePane.getChildren().add(this.tetrisSquare);
    }

    /**
     * This method allows other classes, namely the TetrisPiece class, to update the position
     * of each TetrisSquare. This method allows the TetrisPiece class to move instances of
     * TetrisSquares on the screen as determined by the Game class.
     */
    public void setPosition(int xPos, int yPos) {
        this.tetrisSquare.setX(xPos*Constants.SQUARE_WIDTH);
        this.tetrisSquare.setY(yPos*Constants.SQUARE_WIDTH);
        this.arrayXPos=xPos;
        this.arrayYPos=yPos;
    }

    /**
     * This method moves an instance of TetrisSquare down the screen by one TetrisSquare
     * unit. The method also updates the position of the TetrisSquare.
     */
    public void moveDown() {
        this.tetrisSquare.setY(this.tetrisSquare.getY()+Constants.SQUARE_WIDTH);
        this.arrayYPos=this.arrayYPos+1;
    }

    /**
     * This method moves an instance of TetrisSquare left on screen by one TetrisSquare
     * unit. The method also updates the position of the TetrisSquare.
     */
    public void moveLeft() {
        this.tetrisSquare.setX(this.tetrisSquare.getX()-Constants.SQUARE_WIDTH);
        this.arrayXPos=this.arrayXPos-1;
    }

    /**
     * This method moves an instance of TetrisSquare right on screen by one TetrisSquare
     * unit. The method also updates the position of the TetrisSquare.
     */
    public void moveRight() {
        this.tetrisSquare.setX(this.tetrisSquare.getX()+Constants.SQUARE_WIDTH);
        this.arrayXPos=this.arrayXPos+1;
    }

    /**
     * This method an instance of TetrisSquare about a given axis of rotation. The
     * new position of TetrisSquare is passed directly into the method as an argument,
     * and the method also updates the position of the TetrisSquare.
     */
    public void rotate(int newX, int newY) {
        this.tetrisSquare.setX(newX * Constants.SQUARE_WIDTH);
        this.tetrisSquare.setY(newY*Constants.SQUARE_WIDTH);
        this.arrayXPos=newX;
        this.arrayYPos=newY;
    }

    /**
     * This method graphically removes a TetrisSquare from the game Pane.
     */
    public void removeFromPane() {
        this.gamePane.getChildren().remove(this.tetrisSquare);
    }

    /**
     * This method returns the X position of a TetrisSquare.
     */
    public int getXPos() {
        return this.arrayXPos;
    }

    /**
     * This method returns the Y position of a TetrisSquare.
     */
    public int getYPos() {
        return this.arrayYPos;
    }
}
