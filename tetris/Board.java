package tetris;


import javafx.scene.layout.Pane;

/**
 * The main purpose of the Board class is to wrap a 2D array of TetrisSquares, which logically
 * keeps track of the state of the Board where the game is played. The class also contains some
 * methods which checks the state of the 2d array, allowing the Game class to know about
 * the state of the board.
 */
public class Board {
    private TetrisSquare[][] board;
    private Pane pane;

    /**
     * The constructor of the Board class sets up the 2d array of TetrisSquares as an
     * instance variable, and calls a helper method to set up the squares at the border
     * of the array.
     */
    public Board(Pane pane) {
        this.board=new TetrisSquare[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        this.pane=pane;
        this.setUpBoard();
    }

    /**
     * This method instantiates TetrisSquare objects along the border of the 2d TetrisSquare
     * array.
     */
    private void setUpBoard() {
        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board[i].length; j++) {
                if ((i==0) || (i==board.length-1) || (j==0) || j==board[i].length-1) {
                    board[i][j]=new TetrisSquare(this.pane, Constants.BORDER_COLOR,
                            j, i);
                }
            }
        }
    }

    /**
     * This method is called by TetrisPiece so that the current TetrisPiece can add itself to
     * the board when the piece can no longer move downwards.
     */
    public void addPiece(TetrisSquare square, int xPos, int yPos) {
        this.board[yPos][xPos]=square;
    }

    /**
     * This method clears the overlapping squares when a new TetrisPiece is generated
     * on a Square already occupied in the Board 2d array. The method insures that the restart
     * functionality works properly when the last TetrisPiece is generated on top of an existing
     * one.
     */
    public void clearSquares(int[][] coords) {
        for (int i=0; i<coords.length; i++) {
                if (this.board[coords[i][1]][coords[i][0]]!=null) {
                    this.board[coords[i][1]][coords[i][0]].removeFromPane();
                    this.board[coords[i][1]][coords[i][0]]=null;
            }
        }
    }

    /**
     * This method checks all the coordinates of a parameter array of coordinates correspond to
     * empty positions on the 2d array of tetrisSquares. If all the coordinates are empty, the
     * method returns true. If any one of the coordinates is occupied, the method returns false.
     */
    public boolean checkEmpty(int[][] coords) {
        for (int i=0; i<coords.length; i++) {
                if (this.board[coords[i][1]][coords[i][0]]!=null) {
                    return false;
            }
        } return true;
    }

    /**
     * This method check to see if any of the rows of the 2d array of TetrisSquares is full.
     * If a given row is full, the method calls a helper method to clear the row,
     * and that helper calls another helper method to pull all the above rows down one square.
     * The method then returns the number of rows cleared, which is used to calculate and
     * update the score in the Game class.
     */
    public int checkCompleteRow() {
        int rowsCleared=0;
        for (int i=1; i<this.board.length-1; i++) {
            int count=0;

            for (int j=1; j<this.board[i].length-1; j++) {
                //Count increments by 1 for every square in a row
                if (this.board[i][j]!=null) {
                    count++;
                }
            }
            //if count reaches 10, the row must be full
            if (count==10) {
                this.clearRow(i);
                i--;
                rowsCleared++;
            }
        } return rowsCleared;
    }

    /**
     * This method is called when checkCompleteRow() determines the location of a complete
     * row. The method clears the row graphically and logically, and calls another helper method
     * to move above rows down.
     */
    private void clearRow(int rowToClear) {
        for (int i=1; i<this.board[rowToClear].length-1; i++) {
            this.board[rowToClear][i].removeFromPane();
            this.board[rowToClear][i]=null;
        }
        this.moveRowsDown(rowToClear);
    }

    /**
     * This method moves all the rows above a cleared row down by one TetrisSquare unit.
     */
    private void moveRowsDown(int startRow) {
        for (int i=startRow-1; i>0; i--) {
            for (int j=1; j<this.board[startRow].length-1; j++) {
                if (this.board[i][j]!=null) {
                    this.board[i][j].moveDown();
                    this.board[i+1][j]=this.board[i][j];
                    this.board[i][j]=null;
                }
            }
        }
    }

    /**
     * This method checks to see if there are any tetrisSquares in the highest non-border row.
     * The method returns true if there are any, and false otherwise.
     */
    public boolean checkGameOver() {
        for (int i=1; i<this.board[1].length-1; i++) {
            if (board[1][i]!=null) {
                return true;
            }
        } return false;
    }

    /**
     * This method restarts the game by clearing all the non-border TetrisSquares from the Board
     * 2d array graphically and logically.
     */
    public void restartGame() {
        for (int i=1; i<this.board.length-1; i++) {
            for (int j=1; j<this.board[i].length-1; j++) {
                if (board[i][j]!=null) {
                    this.board[i][j].removeFromPane();
                    this.board[i][j]=null;
                }
            }
        }
    }

}
