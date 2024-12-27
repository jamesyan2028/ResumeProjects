package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * This class is responsible for logically keeping track of the Othello board, which is represented as a
 * 2d array of OthelloSquares. This class also contains much of the logic for determining information
 * about the state of the board, which is often passed the Referee class to make decisions about
 * how the game should proceed.
 */
public class Board {
    private OthelloSquare[][] board;
    private int lastPieceArrayXPos;
    private int lastPieceArrayYPos;

    /**
     * This is the constructor for the board of Othellosquares that is visible to the user of the program
     * on the screen. The method sets up the board instance variables, and calls helper methods to
     * set up the board.
     */
    public Board(Pane pane) {
        this.board=new OthelloSquare[Constants.BOARD_DIM][Constants.BOARD_DIM];

        this.setUpBorder(pane);
        this.setUpBoard(pane);
        this.setUpStartPieces();
    }

    /**
     * This is the constructor for the hypothetical boards analyzed by instances of the ComputerPlayer
     * class when determining the best move to play. The constructor takes in an instance of the Board
     * class, and makes a copy of the this.board instance variable to allow the ComputerPlayer
     * to test out potential moves.
     */
    public Board(Board currBoard) {
        this.board=new OthelloSquare[Constants.BOARD_DIM][Constants.BOARD_DIM];
        for (int arrayYPos=0; arrayYPos<this.board.length; arrayYPos++) {
            for (int arrayXPos=0; arrayXPos<this.board[arrayYPos].length; arrayXPos++) {
                this.board[arrayYPos][arrayXPos]=new OthelloSquare(arrayXPos, arrayYPos);

                if (currBoard.getBoard()[arrayYPos][arrayXPos].checkPiece()==1) {
                    this.board[arrayYPos][arrayXPos].createPiece(false);
                } if (currBoard.getBoard()[arrayYPos][arrayXPos].checkPiece()==-1) {
                    this.board[arrayYPos][arrayXPos].createPiece(true);
                }
            }
        } this.setUpCopyBoardValues();
    }

    /**
     * This method is called by the constructor for the hypothetical Boards created when a ComputerPlayer
     * determines the best move to play. The method makes sure that all the newly created non-border
     * OthelloSquares have a positional advantage value that matches that of the board on screen.
     * It was necessary to create this method due to the nature of the nested for loop needed to create
     * a copy of the current board, which prevented the hypothetical board from being set
     * up in the same way as the board on screen.
     */
    private void setUpCopyBoardValues() {
        for (int arrayYPos=1; arrayYPos<this.board.length-1; arrayYPos++) {
            for (int arrayXPos=1; arrayXPos<this.board[arrayYPos].length-1; arrayXPos++) {
                this.board[arrayYPos][arrayXPos].setValue(Constants.SQUARE_VALUES[arrayYPos-1][arrayXPos-1]);
            }
        }
    }

    /**
     * This method returns the board instance variable, and is used by the constructor for the
     * hypothetical Boards in order to make a copy of the current board.
     */
    public OthelloSquare[][] getBoard() {
        return this.board;
    }

    /**
     * This method sets up the OthelloSquares lying on the edge of the Board, and visually makes them
     * a different color than the other OthelloSquares to let the user know that pieces cannot be played
     * on the border.
     */
    private void setUpBorder(Pane pane) {
        for (int i=0; i<this.board[0].length; i++) {
            for (int j=0; j<this.board.length; j++) {
                if (i==0 || i==this.board.length-1 || j==0 || j==this.board[i].length-1) {
                    this.board[i][j] = new OthelloSquare(Color.DARKRED, i, j, pane);
                }
            }
        }
    }

    /**
     * This helper method is called by the constructor of the board on screen, and sets up
     * all the non-border OthelloSquares.
     */
    private void setUpBoard(Pane pane) {
        for (int i=1; i<this.board[0].length-1; i++) {
            for (int j=1; j<this.board.length-1; j++) {
                this.board[j][i]=new OthelloSquare(Color.DARKGREEN, i,j, pane);
                this.board[j][i].setValue(Constants.SQUARE_VALUES[j-1][i-1]);
            }
        }
    }

    /**
     * This method sets up all the starting four pieces on the Othello board.
     */
    private void setUpStartPieces() {
        this.board[4][4].createPiece(true);
        this.board[4][5].createPiece(false);
        this.board[5][4].createPiece(false);
        this.board[5][5].createPiece(true);
        this.resetBoardSquareColors();
    }

    /**
     * This method is called by instances of HumanPlayer and ComputerPlayer when each player wants
     * to play a piece on the Board.
     */
    public void placePiece(int arrayXPos, int arrayYPos, boolean isBlack) {
        this.lastPieceArrayXPos=arrayXPos;
        this.lastPieceArrayYPos=arrayYPos;
        this.board[arrayYPos][arrayXPos].createPiece(isBlack);
    }

    /**
     * This method is called to highlight the last move played, using the instance variables
     * updated whenever placePiece() is called.
     */
    public void highlightLastMove() {
        this.board[this.lastPieceArrayYPos][this.lastPieceArrayXPos].setLastMove();
    }

    /**
     * This method checks whether a move is valid or not, by checking whether at a particular OthelloSquare
     * in the 2d array, for a particular direction and a particular color, whether there exists a valid
     * sandwich or not. If there exists a valid sandwich for this particular location in the array,
     * direction, and color, the method returns true. If not, the method returns false.
     */
    private boolean checkValidMoveDirection(int arrayXPos, int arrayYPos, int rowDir, int colDir,
                                            boolean blackTurn, boolean seenOpp) {
        if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==1 && !blackTurn && seenOpp) {
            return true;
        } if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==-1 && blackTurn && seenOpp) {
            return true;
        } if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==1 && blackTurn) {
            return this.checkValidMoveDirection(arrayXPos+rowDir, arrayYPos+colDir, rowDir, colDir,
                    true, true);
        } if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==-1 && !blackTurn) {
            return this.checkValidMoveDirection(arrayXPos+rowDir, arrayYPos+colDir, rowDir, colDir,
                    false, true);
        }  return false; //this returns false whenever we find an empty square at the end of recursion
    }

    /**
     * This method is also used to check whether a move is valid or not. However, this method is used to
     * check whether the square is occupied or not. If the square is already occupied by a piece, it
     * cannot be a valid move, and the method returns false. If the particular square is empty, the method
     * calls checkValidMoveDirection() to determine whether the move is valid or not.
     */
    private boolean checkValidMove(int arrayXPos, int arrayYPos, int rowDir, int colDir, boolean blackTurn) {
        if (this.board[arrayYPos][arrayXPos].checkPiece()!=0) {
            return false;
        }  else {
            return this.checkValidMoveDirection(arrayXPos, arrayYPos, rowDir, colDir, blackTurn, false);
        }
    }

    /**
     * This method determines whether a move is a valid or not given a particular position on the board,
     * and a particular color. The method does this by calling checkValidMove(), passing in the particular
     * location and color, for all possible directions.
     */
    public boolean checkAllDirections(int arrayXPos, int arrayYPos, boolean blackTurn) {
        if (arrayXPos<1 || arrayXPos>8 || arrayYPos<1 || arrayYPos>8) {
            return false;
        }
        for (int rowDir=-1; rowDir<2; rowDir++) {
            for (int colDir=-1; colDir<2; colDir++) {
                if (this.checkValidMove(arrayXPos, arrayYPos, rowDir, colDir, blackTurn)) {
                    return true;
                }
            }
        } return false;
    }

    /**
     * This method is responsible for flipping all the pieces in a sandwhich after a move is played
     * at a particular location, direction, and color.
     */
    private void flipPieces(int arrayXPos, int arrayYPos, int rowDir, int colDir, boolean blackTurn) {
         if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==1 && blackTurn) {
             this.board[arrayYPos+colDir][arrayXPos+rowDir].flipPiece();
             this.flipPieces(arrayXPos+rowDir, arrayYPos+colDir, rowDir, colDir, true);
        } if (this.board[arrayYPos+colDir][arrayXPos+rowDir].checkPiece()==-1 && !blackTurn) {
            this.board[arrayYPos+colDir][arrayXPos+rowDir].flipPiece();
            this.flipPieces(arrayXPos+rowDir, arrayYPos+colDir, rowDir, colDir, false);
        }
    }

    /**
     * This method is responsible for flipping all newly created sandwiches after a piece is played.
     * The method does this by taking in the location and color of the move, and calling the helper
     * method flipPieces() for all possible directions.
     */
    public void checkFlipPieces(int arrayXPos, int arrayYPos, boolean blackTurn) {
        for (int rowDir=-1; rowDir<2; rowDir++) {
            for (int colDir=-1; colDir<2; colDir++) {
                if (this.checkValidMoveDirection(arrayXPos, arrayYPos, rowDir, colDir, blackTurn, false)) {
                    this.flipPieces(arrayXPos, arrayYPos, rowDir, colDir, blackTurn);
                }
            }
        }
    }

    /**
     * This method is responsible for highlighting all possible legal moves for the current board
     * configuration.
     */
    public void highlightValidMoves(boolean blackTurn) {
        for (int arrayXPos=1; arrayXPos<this.board[0].length-1; arrayXPos++) {
            for (int arrayYPos=1; arrayYPos<this.board.length-1; arrayYPos++) {
                if (this.checkAllDirections(arrayXPos, arrayYPos, blackTurn)) {
                    this.board[arrayYPos][arrayXPos].setValidMove();
                }
            }
        }
    }

    /**
     * This method is responsible for resetting all OthelloSquares back to their default
     * color after a piece is played. This is necessary to accurately graphically update which
     * pieces are valid moves or not, and which move was the last move currently played
     */
    public void resetBoardSquareColors() {
        for (int arrayYPos=1; arrayYPos<this.board.length-1; arrayYPos++) {
            for (int arrayXPos=1; arrayXPos<this.board[arrayYPos].length-1; arrayXPos++) {
                this.board[arrayXPos][arrayYPos].resetSquareColor();
            }
        }
    }

    /**
     * This move counts the number of pieces on the board for a particular player, and then returns
     * that number. The method is called by the Referee to accurately determine the state of the game
     * and update the label that displays player scores.
     */
    public int countPieces(boolean isBlack) {
        int count=0;
        for (int arrayXPos=1; arrayXPos<this.board[0].length-1; arrayXPos++) {
            for (int arrayYPos=1; arrayYPos<this.board.length-1; arrayYPos++) {
                if (isBlack && this.board[arrayYPos][arrayXPos].checkPiece()==-1) {
                    count++;
                } else if (!isBlack && this.board[arrayYPos][arrayXPos].checkPiece()==1) {
                    count++;
                }

            }
        } return count;
    }

    /**
     * This method logically resets the board of OthelloSquares back to its original state when the
     * program was first opened.
     */
    public void resetGame() {
        for (int i=1; i<this.board.length-1; i++) {
            for (int j=1; j<this.board[i].length-1; j++) {
                this.board[i][j].removePiece();
            }
        }
        this.resetBoardSquareColors();
        this.setUpStartPieces();
    }

    /**
     * This method is called by the referee to determine whether the current game has ended after a player
     * makes their move. The method does this by determining whether there are any valid moves left
     * for either player.
     */
    public boolean checkGameOver() {
        for (int i=1; i<this.board.length-1; i++) {
            for (int j=1; j<this.board[i].length-1; j++) {
                if (this.checkAllDirections(i, j, true)) {
                    return false;
                } if (this.checkAllDirections(i, j, false)) {
                    return false;
                }
            }
        } return true;
    }

    /**
     * This method is called by the Referee to determine whether a player has to repeat their turn
     * towards the end of a game because their opponent has no valid moves.
     */
    public boolean checkPlayerRepeatTurn(boolean isBlack) {
        for (int i=1; i<this.board.length-1; i++) {
            for (int j=1; j<this.board[i].length-1; j++) {
                if (this.checkAllDirections(i, j, isBlack)) {
                    return false;
                }
            }
        } return true;
    }

    /**
     * This method is used to calculate the positional advantage a player has over another given
     * the current state of the Board. It is called by the ComputerPlayer in order to determine what the
     * best move to play is.
     */
    public int calcBoardScore(boolean isBlack) {
        int score=0;
        for(int i=1; i<this.board.length-1; i++) {
            for (int j=1; j<this.board[i].length-1; j++) {
                if (isBlack && this.board[i][j].checkPiece()==-1) {
                    score+=this.board[i][j].getValue();
                } if (isBlack && this.board[i][j].checkPiece()==1) {
                    score-=this.board[i][j].getValue();
                }


                if (!isBlack && this.board[i][j].checkPiece()==1) {
                    score+=this.board[i][j].getValue();
                } if (!isBlack && this.board[i][j].checkPiece()==-1) {
                    score-=this.board[i][j].getValue();
                }
            }
        } return score;
    }

    /**
     * This method returns an ArrayList containing all the possible legal moves for the current
     * board state. It is utilized by the ComputerPlayer to check all possible moves for a given
     * board in order to determine which move is the best to play.
     */
    public ArrayList<Move> getALlLegalMoves(boolean blackTurn) {
        ArrayList<Move> allMoves = new ArrayList<>();
        for (int arrayYPos=1; arrayYPos<this.board.length-1; arrayYPos++) {
            for (int arrayXPos=1; arrayXPos<this.board[arrayYPos].length-1; arrayXPos++) {
                if (this.checkAllDirections(arrayXPos, arrayYPos, blackTurn)) {
                    allMoves.add(new Move(arrayXPos, arrayYPos));
                }
            }
        } return allMoves;
    }
}
