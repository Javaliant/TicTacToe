/* Author: Luigi Vincent

*/

import javafx.scene.control.Button;

public class TicTacToeSquare {
	private boolean filled;
	static boolean turnTracker;
	private Button square = new Button();
	private final int SQUARE_SIZE = 100;

	TicTacToeSquare() {
		square.setMinHeight(SQUARE_SIZE);
		square.setMinWidth(SQUARE_SIZE);
		square.setOnAction(e -> {
			if (square.getText().isEmpty()) {
				square.setText(turnTracker ? "O" : "X");
				square.setStyle(
					turnTracker ? "-fx-text-fill: gold;" : "-fx-text-fill: darkred;");
				filled = true;
				turnTracker = !turnTracker;
				TicTacToe.evaluateBoard();
			}
		});
	}

	public Button button() {
		return square;
	}

	public boolean equals(TicTacToeSquare target) {
		return filled && square.getText().equals(target.button().getText());
	}

	public void clear() {
		filled = false;
		square.setText("");
		square.setDisable(false);
	}
}