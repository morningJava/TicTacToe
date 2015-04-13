package morningJava.TicTacToeClient.Application;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import morningJava.TicTacToe.Constants.Constants;

public class Cell extends JPanel{
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
      addMouseListener(new ClickListener());   
  }
  
  public static char getToken(){
      return token;
  }
}
 

class ClickListener extends MouseAdapter {
      ClickListener(){
     if (getToken() ==' '&& myTurn){
	 setToken(getToken());
	 myTurn= false;
     }
      }
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
 }

}



