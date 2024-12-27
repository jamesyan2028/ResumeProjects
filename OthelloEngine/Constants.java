package othello;

import javafx.scene.paint.Color;

/**
 * This class contains all the relevant constants used in the Othello program.
 */
public class Constants {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int CONTROLS_PANE_WIDTH = 250;
    public static final int SQUARE_DIM=50;
    public static final int PIECE_RADIUS=20;

    public static final int[][] SQUARE_VALUES={{220, -20, 30, 25, 25, 30, -20, 220},
                                            {-20, -40, -10, -10, -10, -10, -40, -20},
                                             {30, -10, 4, 4, 4, 4, -10, 30},
                                            {25, -10, 4, 2, 2, 4, -10, 25},
                                            {25, -10, 4, 2, 2, 4, -10, 25},
                                            {30, -10, 4, 4, 4, 4, -10, 30},
                                            {-20, -40, -10, -10, -10, -10, -40, -20},
                                            {220, -20, 30, 25, 25, 30, -20, 220}};
    public static final Color SQUARE_DEFAULT_COLOR = Color.DARKGREEN;
    public static final Color VALID_MOVE_COLOR = Color.YELLOW;
    public static final Color LAST_MOVE_COLOR = Color.BLUE;
    public static final int BOARD_DIM=10;
}
