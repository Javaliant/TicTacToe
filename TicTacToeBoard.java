/* Author: Luigi Vincent
TicTacToe
*/

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TicTacToeBoard extends BorderPane {
	private TicTacToeSquare[] board = new TicTacToeSquare[9];
	private GridPane layout = new GridPane();
	private int turnCounter;
	private StringProperty xPlayer = new SimpleStringProperty("X player");
	private StringProperty oPlayer = new SimpleStringProperty("O player");
	private IntegerProperty xScore = new SimpleIntegerProperty(0);
	private IntegerProperty oScore = new SimpleIntegerProperty(0);
	private IntegerProperty tieScore = new SimpleIntegerProperty(0);
	private boolean scoreDisplayed;
	private Player currentPlayer = Player.X;

	TicTacToeBoard(Stage stage) {
		setTop(addMenuBar(stage, this));

		for (int i = 0; i < board.length; i++) {
			board[i] = new TicTacToeSquare(this);
			layout.add(board[i].button(), i / 3, i % 3);
		}

		setCenter(layout);
	}

	public void evaluateBoard() {
		for (int i = 0, j = 0; i < 3; i++) {
			// Horizontal
			if(checkSet(j++, j++, j++)) {
				return;
			}
			// Vertical
			if(checkSet(i, i + 3, i + 6)) {
				return;
			}
		}
		// Diagonal
		if(checkSet(0, 4, 8) || checkSet(2, 4, 6)) {
			return;
		}

		if (turnCounter == 8) {
			gameEndPrompt("It's a tie!");
			tieScore.setValue(tieScore.getValue() + 1);
			return;
		}
	}

	private MenuBar addMenuBar(Stage stage, BorderPane root) {
		MenuItem newGameItem = new MenuItem("_New Game");
		newGameItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
		newGameItem.setOnAction(e -> newGame());

		MenuItem exitItem = new MenuItem("E_xit");
		exitItem.setOnAction(e -> Platform.exit());

		Menu gameMenu = new Menu("_Game");
		gameMenu.getItems().addAll(newGameItem, exitItem);

		MenuItem addItem = new MenuItem("_Add player name(s)");
		addItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN));
		addItem.setOnAction(e -> addName(stage));

		Text xText = new Text();
		xText.textProperty().bind(
			Bindings.concat(xPlayer).concat(" wins: ").concat(xScore.asString())
		);

		Text oText = new Text();
		oText.textProperty().bind(
			Bindings.concat(oPlayer).concat(" wins: ").concat(oScore.asString())
		);

		Text tieText = new Text();
		tieText.textProperty().bind(
			Bindings.concat("Ties: ").concat(tieScore.asString())
		);

		VBox scoreLayout = new VBox(5);
		scoreLayout.getChildren().addAll(xText, oText, tieText);
		scoreLayout.setPadding(new Insets(5));
		scoreLayout.setAlignment(Pos.CENTER);

		MenuItem trackItem = new MenuItem("_Toggle score display");
		trackItem.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));
		trackItem.setOnAction(e -> {
			if (!scoreDisplayed) {
				root.setRight(scoreLayout);
				scoreDisplayed = true;
				stage.sizeToScene();
			} else {
				root.setRight(null);
				scoreDisplayed = false;
				stage.sizeToScene();
			}
		});

		MenuItem resetItem = new MenuItem("_Reset score");
		resetItem.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));
		resetItem.setOnAction( e -> {
			xScore.set(0);
			oScore.set(0);
			tieScore.set(0);
		});

		Menu scoreMenu = new Menu("_Score");
		scoreMenu.getItems().addAll(
			addItem,
			trackItem,
			resetItem
		);

		activateMnemonics(
			gameMenu,
			newGameItem,
			exitItem,
			scoreMenu,
			addItem,
			trackItem,
			resetItem
		);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(gameMenu, scoreMenu);
		return menuBar;
	}

	private boolean checkSet(int square1, int square2, int square3) {
		if (turnCounter >= 4) {
			if (board[square1].equivalentTo(board[square2]) 
			&& board[square2].equivalentTo(board[square3])) {
				gameEndPrompt(checkWinner(board[square1].button().getText()) + " wins!");
				return true;
			}
		}
		return false;
	}

	private void gameEndPrompt(String message) {
		endGame();

		Stage stage = new Stage();
		Label label = new Label(message);
		label.setStyle("-fx-font-weight: bold;");

		final int BUTTON_WIDTH = 80;

		Button reset = new Button("New Game");
		reset.setMinWidth(BUTTON_WIDTH);
		reset.setOnAction(e -> {
			stage.close();
			newGame();
		});
		reset.setDefaultButton(true);

		Button quit = new Button("Quit");
		quit.setMinWidth(BUTTON_WIDTH);
		quit.setOnAction(e -> Platform.exit());

		HBox gameLayout = new HBox(5);
		gameLayout.getChildren().addAll(reset, quit);
		gameLayout.setAlignment(Pos.CENTER);

		VBox layout = new VBox(5);
		layout.getChildren().addAll(label, gameLayout);
		layout.setAlignment(Pos.CENTER);

		stage.setScene(new Scene(layout, 175 + new Text(message).getLayoutBounds().getWidth(), 75));
		stage.sizeToScene();

		stage.setTitle("Game Over");
		stage.show();
	}

	private void reset(TicTacToeSquare[] board) {
		for (int i = 0; i < board.length; i++) {
			board[i].reset();
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void endGame() {
		for (int i = 0; i < board.length; i++) {
			board[i].button().setDisable(true);
		}
	}

	private void activateMnemonics(MenuItem... items) {
		for (MenuItem item : items) {
			item.setMnemonicParsing(true);
		}
	}

	private void newGame() {
		turnCounter = 0;
		currentPlayer = Player.X;
		reset(board);
	}

	private void addName(Stage primaryStage) {
		Stage stage = new Stage();
		
		Label xName = new Label("Enter X player: ");
		GridPane.setConstraints(xName, 0, 0);
		TextField xPlayerField = new TextField();
		GridPane.setConstraints(xPlayerField, 1, 0);

		Label oName = new Label("Enter O player: ");
		GridPane.setConstraints(oName, 0, 1);
		TextField oPlayerField = new TextField();
		GridPane.setConstraints(oPlayerField, 1, 1);

		Button submit = new Button("Submit");
		submit.setOnAction(e -> {
			String xString = xPlayerField.getText();
			String oString = oPlayerField.getText();
			if (!xString.replaceAll("[^a-zA-Z]", "").isEmpty()) {
				xPlayer.setValue(xString);
			}
			if (!oString.replaceAll("[^a-zA-Z]", "").isEmpty()) {
				oPlayer.setValue(oString);
			}
			primaryStage.sizeToScene();
			stage.close();
		});
		submit.setDefaultButton(true);
		GridPane.setConstraints(submit, 0, 2);

		GridPane layout = new GridPane();
		layout.getChildren().addAll(
			xName,
			xPlayerField,
			oName,
			oPlayerField,
			submit
		);

		stage.setScene(new Scene(layout));
		stage.setTitle("Set name(s): ");
		stage.sizeToScene();
		stage.show();
	}

	private String checkWinner(String winner) {
		if (winner.equals("X")) {
			xScore.setValue(xScore.getValue() + 1);
			return xPlayer.getValue();
		} else {
			oScore.setValue(oScore.getValue() + 1);
			return oPlayer.getValue();
		}
	}

	public void endTurn() {
		turnCounter++;
		currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;
	}
}