package tetris;

import javafx.scene.paint.Color;

/**
 * This class contains all the relevant constants used throughout the Tetris program
 */
public class Constants {

    // width of each square
    public static final int SQUARE_WIDTH = 30;
    public static final int SCREEN_WIDTH=360;
    public static final int SCREEN_HEIGHT=660;
    public static final int NUM_SQUARES=4;

    // coordinates for squares in each tetris piece

    public static final int[][] I_PIECE_COORDS = {{5, 1}, {5, 2}, {5, 3}, {5, 4}};
    public static final int[][] T_PIECE_COORDS = {{5,1}, {5,2}, {4, 2}, {6, 2}};
    public static final int[][] L_PIECE_COORDS = {{5, 1}, {5, 2}, {5, 3}, {6, 3}};
    public static final int[][] BACKWARDS_L_COORDS={{5, 1}, {5, 2}, {5, 3}, {4, 3}};
    public static final int[][] SQUARE_COORDS={{5, 1}, {5, 2}, {6, 1}, {6, 2}};
    public static final int[][] ZIG_ZAG_COORDS={{5, 1}, {5, 2}, {6, 2}, {6, 3}};
    public static final int[][] BACKWARDS_ZIG_COORDS={{5, 1}, {5, 2}, {4, 2}, {4, 3}};

    public static final int BOARD_WIDTH=12;
    public static final int BOARD_HEIGHT=22;
    public static final Color BORDER_COLOR = Color.GREY;

    public static final double DURATION = 0.4;
    public static final Color I_COLOR=new Color(0.0, 191/255.0, 125/255.0, 1.0);
    public static final Color T_COLOR = new Color(0.0, 180/255.0, 197/255.0, 1.0);
    public static final Color L_COLOR= new Color(0.0, 115/255.0, 230/255.0, 1.0);
    public static final Color BACK_L_COLOR= new Color(139/255.0, 0/255.0, 139/255.0, 1.0);
    public static final Color SQUARE_COLOR= new Color(89/255.0, 40/255.0, 237/255.0, 1.0);
    public static final Color ZIGZAG_COLOR= new Color(255/255.0, 142/255.0, 0/255.0, 1.0);
    public static final Color BACK_ZIG_COLOR= new Color(255/255.0, 80/255.0, 3/255.0, 1.0);



}
