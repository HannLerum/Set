import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame; // for JFrame
import javax.swing.JOptionPane; // messages are displayed using JOptionPane
import javax.swing.ImageIcon; // messages have an icon
import java.awt.*; // for graphics & MouseListener 
import java.awt.event.*; // need for events and MouseListener
//import java.util.TimerTask; //for the eventual timer

public class Controller implements MouseListener{

    
    private JFrame gameJFrame;
    private Container gameContentPane;
    private boolean gameIsReady = false;

    Card[] cardsOnTable;

    private int xMouseOffsetToContentPaneFromJFrame = 0;
    private int yMouseOffsetToContentPaneFromJFrame = 0;

	
	//Code for the Timer
   /* 	private java.util.Timer gameTimer = new java.util.Timer();
	    private int timerCounter = 0;
	    public static final int TIME_TO_CLICK_IN_MILLISECONDS = 5000;
    	public static final int TIME_TO_MOVE_CARD_IN_MILLISECONDS = 70;*/

    
    public Controller(String passedInWindowTitle, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight){
        /**
	 * The code to create the jframe and subsequent graphics panel was taken from the dolphin project
	 */
    	gameJFrame = new JFrame(passedInWindowTitle);
        gameJFrame.setSize(gameWindowWidth, gameWindowHeight);
        gameJFrame.setLocation(gameWindowX, gameWindowY);
        gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameContentPane = gameJFrame.getContentPane();
        gameContentPane.setLayout(null); // not need layout, will use absolute system
        gameContentPane.setBackground(Color.white);
        gameJFrame.setVisible(true);
        // Event mouse position is given relative to JFrame, where dolphin's image in JLabel is given relative to ContentPane,
        //  so adjust for the border
        int borderWidth = (gameWindowWidth - gameContentPane.getWidth())/2;  // 2 since border on either side
        xMouseOffsetToContentPaneFromJFrame = borderWidth;
        yMouseOffsetToContentPaneFromJFrame = gameWindowHeight - gameContentPane.getHeight()-borderWidth; // assume side border = bottom border; ignore title bar
        
        Card trial = new Card();
        trial.draw(gameContentPane.getGraphics(), 100, 100, 50, 150);
    }
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
        Controller myController = new Controller("Tell me something good!", 50,50, 1000, 1000);// window title, int gameWindowX, int gameWindowY, int gameWindowWidth, int gameWindowHeight){

	}
	
	
	private int howManySets()
	{
		return 0;
	}
	
	private boolean isASet(Card[] cards)
	{
		//TODO
		return true;
	}
	
	private void resetGame()
	{
		//TODO
	}
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public void mouseEntered(MouseEvent arg0) {
		;
	}

	public void mouseExited(MouseEvent arg0) {
		;
	}

	public void mousePressed(MouseEvent arg0) {
		;
	}

	public void mouseReleased(MouseEvent arg0) {
		;
	}

}
