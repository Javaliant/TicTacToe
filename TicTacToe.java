/* Author: Luigi Vincent
Main Class to run the tic tac toe game
Created for learning/fun and as a step towards CodeReview's Ultimate Tic Tac Toe community challenge:
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TicTacToe extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Tic Tac Toe By Legato");
		stage.setScene(new Scene(new TicTacToeGame(stage)));
		stage.show();
	}
}