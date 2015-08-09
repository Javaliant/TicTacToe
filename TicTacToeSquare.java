/* Author: Luigi Vincent

*/

import javafx.scene.control.Button;

public class TicTacToeSquare {
	private Button button = new Button();
	private final int SQUARE_LENGTH = 100;

	TicTacToeSquare(TicTacToeGame game, TicTacToeBoard board) {
		button.setMinSize(SQUARE_LENGTH, SQUARE_LENGTH);
		button.setOnAction(e -> {
			if (button.getText().isEmpty()) {
				button.setText(game.getCurrentPlayer().toString());
				button.setStyle(game.getCurrentPlayer().getStyle());
				board.evaluateState();
				game.endTurn();
			}
		});
	}

	public Button button() {
		return button;
	}

	public boolean equivalentTo(TicTacToeSquare target) {
		return !button.getText().isEmpty() && button.getText().equals(target.button().getText());
	}

	public void reset() {
		button.setText("");
		button.setDisable(false);
	}
}