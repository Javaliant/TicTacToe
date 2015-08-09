/* Author: Luigi Vincent

*/

import javafx.scene.control.Button;

public class TicTacToeSquare {
	private Button button = new Button();
	private final int SQUARE_LENGTH = 100;

	TicTacToeSquare(TicTacToeBoard board) {
		button.setMinSize(SQUARE_LENGTH, SQUARE_LENGTH);
		button.setOnAction(e -> {
			if (button.getText().isEmpty()) {
				button.setText(board.getCurrentPlayer().toString());
				button.setStyle(board.getCurrentPlayer().getStyle());
				board.evaluateBoard();
				board.endTurn();
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