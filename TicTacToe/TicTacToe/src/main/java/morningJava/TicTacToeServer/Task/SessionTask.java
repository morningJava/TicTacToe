package morningJava.TicTacToeServer.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import morningJava.TicTacToe.Constants.Constants;


public class SessionTask implements Runnable, Constants {
    private Socket player1;
    private Socket player2;

    // create and initialize cells
    private char[][] cell = new char[3][3];

    private DataInputStream fromPlayer1;
    private DataInputStream toPlayer1;
    private DataInputStream fromPlayer2;
    private DataInputStream toPlayer2;

    // Continue to play
    private boolean continueToPlay = true;

    /**
     * default constructor all @param set to null;
     */
    public SessionTask() {
	this(null, null);
    }

    /**
     * construct a thread
     */
    public SessionTask(Socket player1, Socket player2) {
	this.player1 = player1;
	this.player2 = player2;

	// initialize cells
	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
		cell[i][j] = ' ';
	    }
	}
    }

    /**
     * Implement run() method for threads
     */

    public void run() {
	try {
	    // create data input and output streams
	    DataInputStream fromPlayer1 = new DataInputStream(
		    player1.getInputStream());
	    DataOutputStream toPlayer1 = new DataOutputStream(
		    player1.getOutputStream());
	    DataInputStream fromPlayer2 = new DataInputStream(
		    player2.getInputStream());
	    DataOutputStream toPlayer2 = new DataOutputStream(
		    player2.getOutputStream());

	    // let player 1 know to start
	    toPlayer1.writeInt(1);

	    // Continuously serve and report to players
	    while (true) {
		// recieve a move from player1
		int row = fromPlayer1.readInt();
		int column = fromPlayer1.readInt();

		cell[row][column] = 'X';

		// check if player1 won
		if (isWon('X')) {
		    toPlayer1.writeInt(PLAYER1_WON);
		    toPlayer2.writeInt(PLAYER1_WON);
		    sendMove(toPlayer2, row, column);
		    break;// exit loop
		}
		// Check if all cells are filled
		else if (isFull()) {// Check if all cells are filled
		    toPlayer1.writeInt(DRAW);
		    toPlayer2.writeInt(DRAW);
		    sendMove(toPlayer2, row, column);
		    break;
		} else {
		    // notify player 2 of turn
		    toPlayer2.writeInt(CONTINUE);

		    // send player1's move to player2
		    sendMove(toPlayer2, row, column);

		}
		// Receive player2's move
		row = fromPlayer2.readInt();
		column = fromPlayer2.readInt();
		cell[row][column] = 'O';

		// now to check if Player2 won
		if (isWon('O')) {
		    toPlayer1.writeInt(PLAYER2_WON);
		    toPlayer2.writeInt(PLAYER2_WON);
		    sendMove(toPlayer1, row, column);
		    break;
		} else if (isFull()) {
		    toPlayer1.writeInt(DRAW);
		    toPlayer2.writeInt(DRAW);
		    sendMove(toPlayer1, row, column);
		    break;
		} else {
		    // notify player 1 of turn and player2's move
		    toPlayer1.writeInt(CONTINUE);
		    sendMove(toPlayer1, row, column);
		}
	    }

	} catch (IOException ex) {
	    System.err.println(ex);
	}

    }

    /**
     * Send move to other player
     */
    private void sendMove(DataOutputStream out, int row, int column)
	    throws IOException {
	out.writeInt(row);
	out.writeInt(column);
    }

    /**
     * Determine if all cells are occupied
     */
    private boolean isFull() {

	for (int i = 0; i < 3; i++) {

	    for (int j = 0; j < 3; j++) {

		if (cell[i][j] == ' ') {

		    return false;// at least one cell is empty
		}
	    }
	}
	return true;// all cells are full

    }

    /**
     * Determine if player with current token wins
     */
    private boolean isWon(char token) {

	// check all rows
	for (int i = 0; i < 3; i++) {
	    if ((cell[i][0] == token) && (cell[i][1] == token)
		    && (cell[i][2] == token)) {
		return true;
	    }
	}
	// check all columns
	for (int j = 0; j < 3; j++) {
	    if ((cell[0][j] == token) && (cell[1][j] == token)
		    && (cell[2][j] == token)) {
		return true;
	    }
	}
	// check diagnal left to right
	if ((cell[0][0] == token) && (cell[1][1] == token)
		&& (cell[2][2]) == token) {
	    return true;
	}
	// check diagnal right to left
	if ((cell[0][2] == token) && (cell[1][1] == token)
		&& (cell[2][0] == token)) {
	    return true;
	}
	// all checked, no winner
	return false; 
    }

}
