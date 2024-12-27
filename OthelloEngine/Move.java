package othello;

/**
 * This class is responsible for storing information about a potential move that a ComputerPlayer is
 * considering. The class contains information about the position of the potential move, and the
 * positional advantage the potential move gives or removes from the current player.
 */
public class Move {
    private int arrayXPos;
    private int arrayYPos;
    private int moveValue;

    /**
     * The constructor for the move class initializes instance variables that stores the location
     * of the move.
     */
    public Move(int arrayXPos, int arrayYPos) {
        this.arrayXPos=arrayXPos;
        this.arrayYPos=arrayYPos;
    }

    /**
     * This is a getter method for the arrayXPos instance variable, and used by the ComputerPlayer
     * class to determine the X position of the best move.
     */
    public int getArrayXPos() {
        return this.arrayXPos;
    }

    /**
     * This is a getter method for the arrayYPos instance variable, and used by the ComputerPlayer
     * class to determine the Y position of the best move.
     */
    public int getArrayYPos() {
        return this.arrayYPos;
    }

    /**
     * This method allows the ComputerPlayer to modify a Move's moveValue instance variable
     * in order for the turn changing functionality of the minimax algorithm to work.
     */
    public void setMoveValue(int moveValue) {
        this.moveValue=moveValue;
    }

    /**
     * This method returns the moveValue instance variable for a particular Move, allowing a
     * ComputerPlayer to compare potential moves to determine the best one.
     * @return
     */
    public int getMoveValue() {
        return this.moveValue;
    }
}
