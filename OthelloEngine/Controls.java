package othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/*
 * Controls sets up the GUI for the game menu, allowing the user to pick the
 * game modes and to start and track games. Controls holds a one-way reference
 * to the Game, so it can control the Game's player settings.
 */
public class Controls {

  private SetupGame game;

  private VBox controlsPane;

  // Arrays for player buttons. Each button is checked to see if it is
  // selected when the user starts each game.
  private RadioButton[][] playerButtons;
  private Label whiteScore;
  private Label blackScore;
  private Label moveLabel;

  /**
   * This is the constructor for the Controls class. The constructor is responsible for
   * instantiating the controls VBox which will be added to the BorderPane root node, and
   * then calls other helper methods to populate the VBox.
   */
  public Controls(SetupGame othello) {
    this.game = othello;
    this.controlsPane = new VBox();
    this.controlsPane.setPadding(new Insets(10));
    this.controlsPane.setSpacing(30);
    this.controlsPane.setAlignment(Pos.CENTER);
    this.controlsPane.setStyle("-fx-background-color: grey;");

    this.setUpControls();
  }

  /**
   * This helper method factors out the multiple helper methods called in the constructor to
   * populate the controls VBox.
   */
  private void setUpControls() {
    this.setupInstructions();
    this.setUpMovePane();
    this.setUpScorePane();
    this.setupMenu();
    this.setupGameButtons();
  }

  /**
   * This method sets up the Pane which displays the score. The score is determined by the number
   * of pieces each player has on the board.
   */
  private void setUpScorePane() {
    Pane scorePane = new Pane();
    HBox container = new HBox();
    container.setLayoutX(45);
    container.setSpacing(90);
    this.whiteScore = new Label("White: 2");
    this.blackScore=new Label("Black: 2");
    this.whiteScore.setAlignment(Pos.CENTER);
    this.blackScore.setAlignment(Pos.CENTER);
    container.getChildren().addAll(this.whiteScore, this.blackScore);
    scorePane.getChildren().add(container);
    this.controlsPane.getChildren().add(scorePane);
  }

  /**
   * This method sets up the Pane which displays which players turn to move it is.
   */
  private void setUpMovePane() {
    Pane movePane=new Pane();
    this.moveLabel = new Label("");
    this.moveLabel.setLayoutX(90);
    movePane.getChildren().add(this.moveLabel);
    this.controlsPane.getChildren().add(movePane);
  }

  /**
   * This is a getter method for the controls VBox. It is used by PaneOrganizer to add teh Controls
   * VBox to the BorderPane root node.
   */
  public Pane getPane() {
    return this.controlsPane;
  }

  /**
   * This method sets up the Pane which tells the user how to begin the game.
   */
  private void setupInstructions() {
    Label instructionsLabel = new Label(
        "Select options, then press Apply Settings");
    this.controlsPane.getChildren().add(instructionsLabel);
  }

  /*
   * Sets up the two halves of the player mode menu.
   */
  private void setupMenu() {
    this.playerButtons = new RadioButton[2][4];

    HBox playersMenu = new HBox();
    playersMenu.setSpacing(10);
    playersMenu.setAlignment(Pos.CENTER);
    playersMenu.getChildren().addAll(this.playerMenu(Constants.WHITE),
        this.playerMenu(Constants.BLACK));

    this.controlsPane.getChildren().add(playersMenu);
  }

  /*
   * Provides the menu for each player mode.
   */
  private VBox playerMenu(int player) {
    VBox playerMenu = new VBox();
    playerMenu.setPrefWidth(Constants.CONTROLS_PANE_WIDTH / 2);
    playerMenu.setSpacing(10);
    playerMenu.setAlignment(Pos.CENTER);

    // Player color.
    String playerColor = "White";
    if (player == Constants.BLACK) {
      playerColor = "Black";
    }
    Label playerName = new Label(playerColor);

    // Radio button group for player mode.
    ToggleGroup toggleGroup = new ToggleGroup();

    // Human player.
    RadioButton humanButton = new RadioButton("Human         ");
    humanButton.setToggleGroup(toggleGroup);
    humanButton.setSelected(true);
    this.playerButtons[player][0] = humanButton;

    // Computer Players.
    for (int i = 1; i < 4; i++) {
      RadioButton computerButton = new RadioButton("Computer " + i + "  ");
      computerButton.setToggleGroup(toggleGroup);
      this.playerButtons[player][i] = computerButton;

      // Enables deterministic button when Computer player selected.

    }

    // Checkbox for deterministic play. Only enabled when computer player
    // selected. This is ONLY for Bells&Whistles

    // Visually add the player mode menu.
    playerMenu.getChildren().add(playerName);
    for (RadioButton rb : this.playerButtons[player]) {
      playerMenu.getChildren().add(rb);
    }

    return playerMenu;
  }

  /**
   * This method sets up the Buttons which allows the user to begin playing the game, reset the game,
   * or quit the program.
   */
  private void setupGameButtons() {
    Button applySettingsButton = new Button("Apply Settings");
    applySettingsButton.setOnAction((ActionEvent e)->this.applySettings(e));
    applySettingsButton.setFocusTraversable(false);

    Button resetButton = new Button("Reset");
    resetButton.setOnAction((ActionEvent e)-> this.resetHandler(e));
    resetButton.setFocusTraversable(false);

    Button quitButton = new Button("Quit");
    quitButton.setOnAction((ActionEvent e)->Platform.exit());
    quitButton.setFocusTraversable(false);

    this.controlsPane.getChildren().addAll(applySettingsButton, resetButton,
        quitButton);
  }

  /*
   * Handler for Apply Settings button.
   */

    public void applySettings(ActionEvent e) {
      // Determine game play mode for each player.
      int whitePlayerMode = 0;
      int blackPlayerMode = 0;
      for (int player = 0; player < 2; player++) {
        for (int mode = 0; mode < 4; mode++) {
          if (this.playerButtons[player][mode].isSelected()) {
            if (player == Constants.WHITE) {
              whitePlayerMode = mode;
            } else {
              blackPlayerMode = mode;
            }
          }
        }
    } this.game.createPlayers(whitePlayerMode, blackPlayerMode, this.whiteScore, this.blackScore, this.moveLabel);

  }

  /**
   * This method is called when the Reset button is pressed. The method informs the SetupGame class
   * that the reset button was pressed, and the logic and delegation of responsibilities in
   * resetting the game is handled by the SetupGame class.
   */
    public void resetHandler(ActionEvent e){
      if (this.game.gameStarted()) {
        this.game.resetGame();
      }
    }



}
