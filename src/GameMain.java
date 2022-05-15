import java.awt.*;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;


public class GameMain extends JPanel implements ActionListener{


	//Constants for game 
	// number of ROWS by COLS cell constants 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	 int chance = 0;
	  Random random = new Random();
	  boolean playerchance;
	
	/*declare game object variables*/
	// the game board 
	private Board board;
	 	 
	//TODO: create the enumeration for the variable below (GameState currentState)
	//HINT all of the states you require are shown in the code within GameMain
	private GameState currentState; 
	
	// the current player
	private Player currentPlayer; 
	// for displaying game status message
	private JLabel statusBar;       
	
	JFrame frame = new JFrame(TITLE);
	JPanel btn_panel = new JPanel();
	JButton[] buttons = new JButton[9];
	/** Constructor to setup the UI and game components on the panel */
	public GameMain() {   
		
		
	    //Initialize the board
		board=new Board();
		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  
		
		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
		
		//TODO: create the new GameMain panel and add it to the frame
		btn_panel.setLayout(new GridLayout(3,3));
		btn_panel.setBackground(new Color(150,150,150));
		
		frame.add(btn_panel);

		for(int i=0;i<ROWS*COLS;i++) {
			buttons[i] = new JButton();
			
			btn_panel.add(buttons[i]);
			buttons[i].setFont(new Font("MV Boli",Font.BOLD,120));
			buttons[i].setFocusable(false);
			// TODO: This JPanel fires a MouseEvent on MouseClicked so add required event listener to 'this'.    
			buttons[i].addActionListener(this);
		
			
		}
		
		//TODO: set the default close operation of the frame to exit_on_close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(400,400);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		
		
	}
	
	public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
	
				//all the panels are created here in constructor because in static method we cannot use non static things.
				new GameMain();
				
				
				
	         }
		 
	
	
	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
		//fill background and set color to white
		super.paintComponent(g);
		setBackground(Color.WHITE);
		//ask the game board to paint itself
		board.paint(g);
		
		//set status bar message
		if (currentState == GameState.Playing) {          
			statusBar.setForeground(Color.BLACK);          
			if (currentPlayer == Player.Cross) {   
				
				statusBar.setText("X's Turn");  // setting up status bar for the display of the message 
			} else {    
				
				statusBar.setText("O's Turn"); 
			}       
			} else if (currentState == GameState.Draw) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("It's a Draw! Click to play again.");       
			} else if (currentState == GameState.Cross_won) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("'X' Won! Click to play again.");       
			} else if (currentState == GameState.Nought_won) {          
				statusBar.setForeground(Color.RED);          //setting up of red color in case of naught 
				statusBar.setText("'O' Won! Click to play again.");       
			}
		}
		
	
	  /** Initialise the game-board contents and the current status of GameState and Player) */
		public void initGame() {
			for (int row = 0; row < ROWS; ++row) {          
				for (int col = 0; col < COLS; ++col) {  
					// all cells empty
					board.cells[row][col].content = Player.Empty;           
				}
			}
			 currentState = GameState.Playing;
			 currentPlayer = Player.Cross;
		}
		
		
		/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
		 * If they have the GameState is set to won for that player
		 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
		 *   
		 */
		public void updateGame(Player thePlayer, int row, int col) {//this function is for updating the game state  
			//check for win after play
			if(board.hasWon(thePlayer, row, col)) {
				
				// TODO: check which player has won and update the current state to the appropriate game state for the winner
				if(thePlayer == Player.Cross)
					currentState = GameState.Cross_won;//setting the current state of the game after being moved is occur 
				else
					currentState = GameState.Nought_won;//in case of the Naught 
			} else 
				if (board.isDraw ()) {
				
					currentState = GameState.Draw;//In case when game is drawn 
			}
			//otherwise no change to current state of playing
			currentState = GameState.Playing;
		}
		
				
	
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
	
	public void mouseClicked(MouseEvent e) {  
	    // get the coordinates of where the click event happened  
		System.out.println(e.getX()+" "+e.getY());
		int mouseX = e.getX();             
		int mouseY = e.getY();             
		// Get the row and column clicked             
		int rowSelected = mouseY / CELL_SIZE;             
		int colSelected = mouseX / CELL_SIZE; 
		if (currentState == GameState.Playing) {                
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
				// move  
				board.cells[rowSelected][colSelected].content = currentPlayer; 
				// update currentState                  
				updateGame(currentPlayer, rowSelected, colSelected); 
				// Switch player
				if (currentPlayer == Player.Cross) {
					currentPlayer =  Player.Nought;
				}
				else {
					currentPlayer = Player.Cross;
				}
			}             
		} else {        
			// game over and restart              
			initGame();            
		}   
		
		
           
	}
		
	// this function is responsible for ending game this will accept the string and show the result 
	 public void gameEnd(String s){
	        chance = 0;
	        Object[] option={"Restart","Exit"};//asking if the user wants to restart the game 
	        int n=JOptionPane.showOptionDialog(frame, "Game Over\n"+s,"Game Over",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
	        if(n==0){
	            
	            new GameMain();//call the constructor in case to restart  the game 
	        }
	        else{
	            frame.dispose();
	        }
	    
	    }
       // This function is for checking all the results like if the conditions are in diagonal or if the X is in a same row or same column
	 public void Check() {
		   //condition if the X is in a same row
	        if ((buttons[0].getText() == "X") && (buttons[1].getText() == "X") && (buttons[2].getText() == "X")) {
	            buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	        //if the "X " is in diagonal form
	        else if ((buttons[0].getText() == "X") && (buttons[4].getText() == "X") && (buttons[8].getText() == "X")) {
	        	 buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	        //condition if the x is in right diagonal
	        else if ((buttons[0].getText() == "X") && (buttons[3].getText() == "X") && (buttons[6].getText() == "X")) {
	        	 buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	        //condition if the X is in straight line like all three X in a straight center row.
	        else if ((buttons[1].getText() == "X") && (buttons[4].getText() == "X") && (buttons[7].getText() == "X")) {
	        	 buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	        //condition if the in the middle of the board
	        else if ((buttons[2].getText() == "X") && (buttons[4].getText() == "X") && (buttons[6].getText() == "X")) {
	        	 buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	        
	        else if ((buttons[2].getText() == "X") && (buttons[5].getText() == "X") && (buttons[8].getText() == "X")) {
	        	 buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	       else if ((buttons[3].getText() == "X") && (buttons[4].getText() == "X") && (buttons[5].getText() == "X")) {
	    	   buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	       else if ((buttons[6].getText() == "X") && (buttons[7].getText() == "X") && (buttons[8].getText() == "X")) {
	    	   buttons=Board.xWins(buttons,0, 1, 2);
	            statusBar.setText("X wins");
		        gameEnd("X Wins");
	        }
	      
	        else if ((buttons[0].getText() == "O") && (buttons[1].getText() == "O") && (buttons[2].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,0, 1, 2);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[0].getText() == "O") && (buttons[3].getText() == "O") && (buttons[6].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,0, 3, 6);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[0].getText() == "O") && (buttons[4].getText() == "O") && (buttons[8].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,0, 4, 8);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[1].getText() == "O") && (buttons[4].getText() == "O") && (buttons[7].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,1, 4, 7);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[2].getText() == "O") && (buttons[4].getText() == "O") && (buttons[6].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,2, 4, 6);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[2].getText() == "O") && (buttons[5].getText() == "O") && (buttons[8].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,2, 5, 8);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if ((buttons[3].getText() == "O") && (buttons[4].getText() == "O") && (buttons[5].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,3, 4, 5);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        } else if ((buttons[6].getText() == "O") && (buttons[7].getText() == "O") && (buttons[8].getText() == "O")) {
	        	 buttons=Board.oWins(buttons,6, 7, 8);
	            statusBar.setText("O Wins");
		        gameEnd("O Wins");
	        }
	        else if(chance==9) {
	            statusBar.setText("Match Tie");
	             gameEnd("Match Tie");
	        }
	    }

	   
	    
	@Override
	public void actionPerformed(ActionEvent e) {//this is seprate function called when every button is clicked 
		 for (int i = 0; i < 9; i++) {
	            if (e.getSource() == buttons[i]) {
	                if (playerchance) {//this is for checking the playerchance 
	                    if (buttons[i].getText() == "") {
	                        buttons[i].setForeground(new Color(255, 0, 0));
	                        buttons[i].setText("X");
	                        playerchance = false;
	                        statusBar.setText("O turn");
	                        chance++;
	                        Check();
	                    }
	                } else {//this one is for another turn.
	                    if (buttons[i].getText() == "") {
	                        buttons[i].setForeground(new Color(0, 0, 255));
	                        buttons[i].setText("O");
	                        playerchance = true;
	                        statusBar.setText("X turn");
	                        chance++;
	                        Check();
	                    }
	                }
	            }
	        }
		
	}

}
