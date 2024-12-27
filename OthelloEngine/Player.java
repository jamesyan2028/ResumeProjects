package othello;

/**
 * The Player class is an abstract class which acts as the superclass for both the ComputerPlayer class
 * and the HumanPlayer class. The class factors out functionality shared by both classes, which is
 * primarily the ability of both classes to place pieces. The class also factors out a makeMove() method
 * which is implemented by both of the subclasses differently, because HumanPlayers and ComputerPlayers
 * determine which move to place in different ways. The class also contains an abstract method
 * editGameRunning() to allow the referee class to stop players from placing pieces after the
 * game ends.
 */
public abstract class Player {
    private Board board;
    private boolean isBlack;
    private Referee ref;

    /**
     * The constructor for the Player class initializes relevant instance variables.
     */
    public Player(Board board, boolean isBlack, Referee ref) {
        this.board=board;
        this.isBlack=isBlack;
        this.ref=ref;
    }

    /**
     * This method determines how ComputerPlayers and HumanPlayers determine which move to play.
     * Because HumanPlayers and ComputerPlayers determine which move to play differently, this method
     * is abstract and implementation is left to the subclasses.
     */
    public abstract void makeMove();

    /**
     * This method factors out the commonality that both HumanPlayers and ComputerPlayers should not
     * continue making moves after the game has ended. Although this method is only really needed
     * in the ComputerPlayer class, the existence of this abstract method is necessary because the
     * Referee does not know whether the player with the white pieces or the player with the black
     * pieces is a HumanPlayer or ComputerPlayer, so this method must exist in the superclass to
     * guarantee that the referee can call this method on players regardless of whether they are
     * human or a computer.
     */
    public abstract void editGameRunning(boolean isRunning);

    /**
     * This method is a commonality factored out of HumanPlayer and ComputerPlayer. Because
     * both players place their piece on the board in the exact same way, this method can
     * be factored out into the superclass.
     */
    public void placePiece(int arrayXPos, int arrayYPos) {
        this.board.placePiece(arrayXPos, arrayYPos, this.isBlack);
        this.board.checkFlipPieces(arrayXPos, arrayYPos, this.isBlack);
        this.board.resetBoardSquareColors();
        this.board.highlightLastMove();
        this.ref.updateTurnTracker();
    }

    /**
     * This method is a common method shared by both HumanPlayers and ComputerPlayers, which is used
     * by the referee to determine whether to highlight all the valid moves for the current player
     * or not. Because the implementation is different for HumanPlayers or ComputerPlayers, the
     * method is abstract.
     */
    public abstract boolean isHuman();



}
