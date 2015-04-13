package morningJava.TicTacToeClient.Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import morningJava.TicTacToe.Constants.Constants;
import morningJava.TicTacToeClient.Application.Cell;

public class TicTacToeClient extends JApplet implements Runnable, Constants {

    // indicate if player's turn
    private boolean myTurn = false;

    /**
     * tokens determined by server
     */
    // player token ('X' or 'O')
    private char myToken = ' ';

    // rival's token
    private char otherToken = ' ';

    // create and initilaize cells
    private Cell[][] cell = new Cell[3][3];

    // Title Label
    private JLabel lblTitle = new JLabel();
    // status label
    private JLabel lblStatus = new JLabel();

    // selected row and column
    private int rowSelected;
    private int columnSelected;

    // input and output streams to/from server
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    // continue option
    private boolean continueToPlay = true;

    // indicate if run as application
    private boolean isStandAlone = false;

    // wait for players turn
    private boolean waiting = true;

    // Host name or IP
    private String host = "localhost";

    /**
     * initialize user interface
     */
    @Override
    public void init() {
	// panel to hold cells
	JPanel cellPanel = new JPanel();
	cellPanel.setLayout(new GridLayout(3, 3, 0, 0));

	for (int i = 0; i < 3; i++) {
	    for (int j = 0; j < 3; j++) {
		cellPanel.add(cell[i][j] = new Cell(i, j));
	    }

	}
	// properties for labels
	cellPanel.setBorder(new LineBorder(Color.black, 1));
	lblTitle.setHorizontalAlignment(JLabel.CENTER);
	lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
	lblTitle.setBorder(new LineBorder(Color.black, 1));
	lblStatus.setBorder(new LineBorder(Color.black, 1));

	// add to panel
	add(lblTitle, BorderLayout.NORTH);
	add(cellPanel, BorderLayout.CENTER);
	add(lblStatus, BorderLayout.SOUTH);

	// connect to server
	// connectToServer();
    }

    private void connectToServer() {
	try {
	    Socket socket;
	    if (isStandAlone) {
		socket = new Socket(host, 8000);
	    } else {
		socket = new Socket(getCodeBase().getHost(), 8000);
	    }
	    // data streams
	    fromServer = new DataInputStream(socket.getInputStream());
	    toServer = new DataOutputStream(socket.getOutputStream());
	} catch (Exception e) {
	    System.err.println(e);
	}

	// make a thread
	Thread thread = new Thread(this);
	thread.start();
    }

    public void run() {
	try {
	    int player = fromServer.readInt();

	    if (player == PLAYER1) {
		myToken = 'X';
		otherToken = 'O';
		lblTitle.setText("You are 'X' ");
		lblStatus.setText("Waiting for 'O' to join");

		fromServer.readInt();
		lblStatus.setText("'O' has Joined. 'X' goes first.");

		myTurn = true;
	    } else if (player == PLAYER2) {
		myToken = 'O';
		otherToken = 'X';
		lblTitle.setText("You are 'O'");
		lblStatus.setText("X's Turn");
	    }

	    while (continueToPlay) {
		if (player == PLAYER1) {
		    waitForPlayerAction();
		    sendMove();
		    recieveInfoFromServer();
		} else if (player == PLAYER2) {
		    recieveInfoFromServer();
		    waitForPlayerAction();
		    sendMove();
		}
	    }
	} catch (Exception e) {

	}
    }

    private void waitForPlayerAction() throws InterruptedException {
	while (waiting) {
	    Thread.sleep(100);
	}
	waiting = true;

    }

    /**
     * send move to server
     * */
    private void sendMove() throws IOException {
	toServer.writeInt(rowSelected);
	toServer.writeInt(columnSelected);
    }

    /**
     * Receive move from server
     */
    private void recieveInfoFromServer() throws IOException {
	int status = fromServer.readInt();

	if (status == PLAYER1_WON) {
	    continueToPlay = false;
	    if (myToken == 'X') {
		lblStatus.setText(" X Wins!");
	    } else if (myToken == 'O') {
		lblStatus.setText("X has won");
		recieveMove();
	    }
	} else if (status == PLAYER2_WON) {
	    continueToPlay = false;
	    if (myToken == 'X') {
		lblStatus.setText("O has won.");
		recieveMove();
	    } else if (myToken == 'O') {
		lblStatus.setText(" O Wins!");
	    }
	} else if (status == DRAW) {
	    continueToPlay = false;
	    lblStatus.setText("Draw!");
	    if (myToken == 'O') {
		recieveMove();
	    }
	} else {
	    recieveMove();
	    lblStatus.setText("Your turn");
	    myTurn = true;
	}
    }

    private void recieveMove() throws IOException {
	// get info from server
	int row = fromServer.readInt();
	int column = fromServer.readInt();

	cell[row][column].setToken(otherToken);
    }
}
 class Cell extends JPanel{
    private int row;
    private int column;
    
    private char token = ' ';
    

    public Cell () {
	this(0,0);
    }
  public Cell(int row, int column){
      this.row = row;
      this.column = column;
      
      setBorder(new LineBorder(Color.BLACK, 1));
      addMouseListener(new ClickListener()) ;   
  }
  


 public int getRow() {
	return row;
 }


 public void setRow(int row) {
	this.row = row;
 }


 public int getColumn() {
	return column;
 }


 public void setColumn(int column) {
	this.column = column;
 }


 public char getToken() {
	return token;
 }


 public void setToken(char token) {
	this.token = token;
	repaint();
 }
}



