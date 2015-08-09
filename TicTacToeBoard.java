


import javafx.scene.layout.GridPane;

public class TicTacToeBoard extends GridPane {
	private final int NUMBER_OF_SQUARES = 9;
	private final int BOARD_FILLED = 8;
	public int boardCounter;
	private TicTacToeSquare[] board = new TicTacToeSquare[NUMBER_OF_SQUARES];
	private TicTacToeGame game;

	TicTacToeBoard(TicTacToeGame game) {
		this.game = game;

		for (int i = 0; i < board.length; i++) {
			board[i] = new TicTacToeSquare(this.game, this);
			add(board[i].button(), i / 3, i % 3);
		}
	}

	public void evaluateState() {
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

		if (boardCounter == BOARD_FILLED ) {
			game.endPrompt("It's a tie!");
			return;
		}
		boardCounter++;
	}

	private boolean checkSet(int square1, int square2, int square3) {
		if (boardCounter >= 4) {
			if (board[square1].equivalentTo(board[square2]) 
			&& board[square2].equivalentTo(board[square3])) {
				game.endPrompt(game.checkWinner(board[square1].button().getText()) + " wins!");
				return true;
			}
		}
		return false;
	}

	public void disable() {
		for (int i = 0; i < board.length; i++) {
			board[i].button().setDisable(true);
		}
	}

	public void reset() {
		for (int i = 0; i < board.length; i++) {
			board[i].reset();
		}
	}
}