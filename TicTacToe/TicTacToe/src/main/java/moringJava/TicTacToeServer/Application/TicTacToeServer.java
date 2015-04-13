package moringJava.TicTacToeServer.Application;

import java.awt.BorderLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import morningJava.TicTacToe.Constants.Constants;
import morningJava.TicTacToeServer.Task.SessionTask;

public class TicTacToeServer extends JFrame implements Constants {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create frame, establish server socket on port 8000
     */
    public TicTacToeServer() {
	JTextArea jtaLog = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(jtaLog);
	add(scrollPane, BorderLayout.CENTER);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(300, 300);
	setTitle("Tic Tac Toe SERVER");
	setVisible(true);

	try {
	    ServerSocket serverSocket = new ServerSocket(8000);

	    jtaLog.append(new Date() + ": Server started on port 8000.");

	    // number the session
	    int sessionNo = 1;

	    // create a session for every two players
	    while (true) {
		
		jtaLog.append(new Date()
			+ ": Waiting for players to join session " + sessionNo
			+ '\n');
		// connect to player 1 (first to connect)
		Socket player1 = serverSocket.accept();

		// Log player's session
		jtaLog.append(new Date() + ": Player 1 joined session "
			+ sessionNo + "." + '\n');

		jtaLog.append("From "
			+ player1.getInetAddress().getHostAddress() + '\n');

		// notify player
		new DataOutputStream(player1.getOutputStream())
			.writeInt(PLAYER1);

		// connect to player two
		Socket player2 = serverSocket.accept();

		// log player's session
		jtaLog.append(new Date() + ": player 2 joined session "
			+ sessionNo + "." + '\n');
		jtaLog.append("From "
			+ player2.getInetAddress().getHostAddress() + '\n');

		// notify player
		new DataOutputStream(player2.getOutputStream())
			.writeInt(PLAYER2);

		
		// start a thread for next session
		 SessionTask task = new SessionTask(player1,player2);
		 new Thread(task).start();

	    }

	} catch (IOException ex) {
	    System.err.println(ex);
	}

    }

    public static void main(String[] args) {
	TicTacToeServer frame = new TicTacToeServer();

    }

}
