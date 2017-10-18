import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class Controller extends JFrame   implements MouseListener{
	
    private Container gameContentPane;
    private boolean gameIsReady = false;

    Card[] cardsOnTable;

    private int xMouseOffsetToContentPaneFromJFrame = 0;
    private int yMouseOffsetToContentPaneFromJFrame = 0;

	public static void main(String[] args) 
	{
		
        Controller myController = new Controller();
	}
	
	Controller()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		
        this.setSize(width, height);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameContentPane = this.getContentPane();
        gameContentPane.setLayout(null); // not need layout, will use absolute system
        //gameContentPane.setBackground(Color.white);
        this.setVisible(true);
        // Event mouse position is given relative to JFrame, where dolphin's image in JLabel is given relative to ContentPane,
        //  so adjust for the border
        int borderWidth = (width - gameContentPane.getWidth())/2;  // 2 since border on either side
        xMouseOffsetToContentPaneFromJFrame = borderWidth;
        yMouseOffsetToContentPaneFromJFrame = height - gameContentPane.getHeight()-borderWidth; // assume side border = bottom border; ignore title bar
        
        repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		int[] variables0 = {1,0,0,1};
        Card trial = new Card(4,3,variables0);
        trial.draw(g,100, 100, 200, 400);
        
        int[] variables1 = {2,1,0,1};
        Card trial2 = new Card(4,3,variables1);
        trial2.draw(g, 350, 100, 200, 400);
        
        int[] variables2 = {3,2,0,1};
        Card trial3 = new Card(4,3,variables2);
        trial3.draw(g, 600, 100, 200, 400);
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
