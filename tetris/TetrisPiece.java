package tetris;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class is responsible for creating TetrisPieces by creating TetrisSquares in
 * particular arrangements.
 */
public class TetrisPiece {
    private TetrisSquare[] tetrisSquares;
    private int[][] coords;
    private boolean isSquare;

    /**
     * The constructor for the TetrisPiece class initializes relevant instance variables, and calls
     * a helper method to build and arrange the tetris piece.
     */
    public TetrisPiece(Pane gamePane, int[][] coords, Board board, Color color, boolean isSquare) {
        this.tetrisSquares=new TetrisSquare[Constants.NUM_SQUARES];
        this.coords=this.copyCoords(coords);
        this.isSquare=isSquare;

        this.setUpPiece(gamePane, color);
        this.arrangeSquares();
    }

    /**
     * This method creates a copy of the coordinates passed to the constructor of TetrisPiece.
     * This method is necessary because the coordinates of each instance of TetrisPiece will be
     * modified by methods in the Game class which move and rotate the current tetris piece.
     * When this method was not used, the movement methods in the Game class were able to
     * modify the original coordinates in the Constants class, causing the next piece to
     * generate in the right location. This method ensures that the coordinates of a piece
     * can be updated without changing the original coordinates of the piece in the Constants
     * class.
     */
    private int[][] copyCoords(int[][] coords) {
        int[][] currentcoords=new int[coords.length][coords[0].length];
        for (int i=0; i< currentcoords.length; i++) {
            for (int j=0; j<currentcoords[i].length; j++) {
                currentcoords[i][j]=coords[i][j];
            }
        } return currentcoords;
    }

    /**
     * This method is responsible for populating the tetrisSquares instance variable, which
     * is an array of four TetrisSquares which make up a TetrisPiece.
     */
    private void setUpPiece(Pane gamePane, Color color) {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            this.tetrisSquares[i]=new TetrisSquare(gamePane, color, 5, 2);
        }
    }

    /**
     * This method graphically arranges the individual TetrisSquares properly on screen by
     * referencing the 2d array of coordinates passed into the TetrisPiece constructor.
     */
    private void arrangeSquares() {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            this.tetrisSquares[i].setPosition(this.coords[i][0], this.coords[i][1]);
        }
    }

    /**
     * This method causes each individual TetrisSquare in the TetrisSquares array to move down
     * graphically by one TetrisSquare unit. The method also updates the position of the
     * TetrisSquares by updating the coords instance variable.
     */
    public void moveDown() {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            this.tetrisSquares[i].moveDown();
            this.coords[i][1]+=1;
        }
    }

    /**
     * This method causes each individual TetrisSquare in the TetrisSquares array to move left
     * graphically by one TetrisSquare unit. The method also updates the position of the
     * TetrisSquares by updating the coords instance variable.
     */
    public void moveLeft() {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            this.tetrisSquares[i].moveLeft();
            this.coords[i][0]=this.coords[i][0]-1;
        }
    }

    /**
     * This method causes each individual TetrisSquare in the TetrisSquares array to move right
     * graphically by one TetrisSquare unit. The method also updates the position of the
     * TetrisSquares by updating the coords instance variable.
     */
    public void moveRight() {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            this.tetrisSquares[i].moveRight();
            this.coords[i][0]=this.coords[i][0]+1;
        }
    }

    /**
     * This method causes each individual TetrisSquare in the TetrisSquares array to graphically
     * rotate about an axis of rotation, as defined by the parameters passed into the method.
     * The method also updates the position of the TetrisSquares by updating the coords
     * instance variable.
     */
    public void rotate(int centerOfRotX, int centerOfRotY) {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            int temp = this.coords[i][0];
            this.coords[i][0]=centerOfRotX-centerOfRotY+this.coords[i][1];
            this.coords[i][1]=centerOfRotY+centerOfRotX-temp;
            this.tetrisSquares[i].rotate(this.coords[i][0], this.coords[i][1]);
        }
    }

    /**
     * This method returns the coords instance variable, which is a 2d array of integers which
     * represent where the TetrisSquares in the TetrisSquares array are.
     */
    public int[][] getCoords() {
        return this.coords;
    }

    /**
     * This method allows each TetrisSquare in the TetrisSquares array to logically add itself
     * to the Board after a TetrisPiece is no longer able to move downwards.
     */
    public void addToBoard(Board board) {
        for (int i=0; i<this.tetrisSquares.length; i++) {
            board.addPiece(this.tetrisSquares[i], this.tetrisSquares[i].getXPos(), this.tetrisSquares[i].getYPos());
        }
    }

    /**
     * This method is used to determine if the current TetrisPiece in the game is a square or
     * not. The method returns the relevant boolean instance variable isSquare().
     */
    public boolean checkSquare() {
        return this.isSquare;
    }
    
}
