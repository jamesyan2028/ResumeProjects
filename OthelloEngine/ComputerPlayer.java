package othello;

import java.util.ArrayList;

/**
 * This class represents a computer player playing the Othello game. The class primarily contains methods
 * governing how a computer player determines what piece to play.
 */
public class ComputerPlayer extends Player{
    private Board board;
    private int difficulty;
    private boolean isBlack;
    private boolean gameRunning;

    /**
     * The constructor for the ComputerPlayer class initializes the Player superclass, and initializes
     * relevant instance variables.
     */
    public ComputerPlayer(int difficulty, Board board, boolean isBlack, Referee referee) {
        super(board, isBlack, referee);

        this.gameRunning=true;
        this.board=board;
        this.difficulty=difficulty;
        this.isBlack=isBlack;
    }

    /**
     * This method overrides the abstract makeMove() method inherited from the Player superclass. The
     * method calls a helper method to determine the best move given the current board state, and then
     * plays that move using the placePiece() method inherited from Player.
     */
    @Override
    public void makeMove() {
        if (this.gameRunning) {
            int bestMoveXPos = this.getBestMove(this.board, this.difficulty, this.isBlack).getArrayXPos();
            int bestMoveYPos = this.getBestMove(this.board, this.difficulty, this.isBlack).getArrayYPos();
            this.placePiece(bestMoveXPos, bestMoveYPos);
        }
    }

    /**
     * This method determines the best possible move in the current board state for a particular
     * instance of ComputerPlayer using a minimax algorithm. The method gathers an arrayList of all
     * possible moves, and after checking that the list is not empty, recursively calls itself a variable
     * number of times to simulate all possible boards that could come from a sequence of moves. The method
     * then determines the move that leads to the most advantageous board state, and returns that move.
     */
    private Move getBestMove(Board currBoard, int movesToBaseCase, boolean isBlack) {
        if (currBoard.checkGameOver()) {
            if (currBoard.countPieces(isBlack)>currBoard.countPieces(!isBlack)) {
                Move dummyMove = new Move(0,0);
                dummyMove.setMoveValue(10000);
                return dummyMove;
            } if (currBoard.countPieces(isBlack)<currBoard.countPieces(!isBlack)) {
                Move dummyMove = new Move(0,0);
                dummyMove.setMoveValue(-10000);
                return dummyMove;
            } if (currBoard.countPieces(isBlack)==currBoard.countPieces(!isBlack)) {
                Move dummyMove = new Move(0,0);
                dummyMove.setMoveValue(0);
                return dummyMove;
            }
        }
        ArrayList<Move> legalMoves = currBoard.getALlLegalMoves(isBlack);
        if (legalMoves.size()==0) {
            if (movesToBaseCase==1) {
                Move dummyMove = new Move(0,0);
                dummyMove.setMoveValue(-10000);
                return dummyMove;
            } else {
                Move returnMove = this.getBestMove(currBoard, movesToBaseCase-1, !isBlack);
                returnMove.setMoveValue(returnMove.getMoveValue()*-1);
                return returnMove;
            }
        } else {
            Move bestMove = legalMoves.get(0);

            for (Move currMove: legalMoves) {
                Board newBoard = new Board(currBoard);
                newBoard.placePiece(currMove.getArrayXPos(), currMove.getArrayYPos(), isBlack);
                newBoard.checkFlipPieces(currMove.getArrayXPos(), currMove.getArrayYPos(), isBlack);
                if (movesToBaseCase==1) {
                    currMove.setMoveValue(newBoard.calcBoardScore(isBlack));
                } else {
                    currMove.setMoveValue(-1 * this.getBestMove(newBoard, movesToBaseCase-1, !isBlack).getMoveValue());
                }
                if (currMove.getMoveValue()>bestMove.getMoveValue()) {
                    bestMove=currMove;
                }
            }

            return bestMove;
        }
    }

    /**
     * This method overrides the inherited abstract method editGameRunning() from the Player superclass.
     * The method ensures that the referee can stop the ComputerPlayer from making moves once the
     * game has ended.
     */
    @Override
    public void editGameRunning(boolean gameRunning) {
        this.gameRunning=gameRunning;
    }

    /**
     * This method lets the referee know that the player is a computer, to help the referee
     * know whether to highlight valid moves or not.
     */
    @Override
    public boolean isHuman() {
        return false;
    }
}
