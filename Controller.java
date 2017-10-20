import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class Controller extends JFrame   implements MouseListener{
	
	private boolean debugging;
	
	
    private Container gameContentPane;
    private boolean gameIsReady = false;
    
    private Deck myDeck;

    Card[] cardsOnTable;
    Card[] selectedCards;
    private int numberOfCardsOnTheTable;
    
    private int numberOfVariables;
    private int cardsToASet;
    private int minimumCards;
    
    private int cardWidth;
    private int cardHeight;
    private int firstCardX;
    private int firstCardY;
    private int cardBufferX;
    private int cardBufferY;
    private int rows;
    private int columns;
    
    private int dealingStyle;

    private int xMouseOffsetToContentPaneFromJFrame = 0;
    private int yMouseOffsetToContentPaneFromJFrame = 0;
    
    private static final int HORIZONTALLY =  0;
    private static final int VERTICALLY = 1;

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

        initialize();
        
        repaint();
	}
	
	private void initialize()
	{
		//eventually make these variables dynamic, but for now they are hardcoded in.
		numberOfVariables = 3;
		cardsToASet = 3;
		
		myDeck = new Deck(numberOfVariables,cardsToASet);
		selectedCards = new Card[cardsToASet];
		
		rows = 3;
		columns = 9;
		cardWidth = 100;
		cardHeight = 200;
		firstCardX = 200;
		firstCardY = 50;
		cardBufferX = 10;
		cardBufferY = 10;
		minimumCards = 18;
		
		dealingStyle = VERTICALLY;
		//dealingStyle = HORIZONTALLY;
		
		cardsOnTable = new Card[rows*columns];
		numberOfCardsOnTheTable = 0;
        for (int i = 0; (i<cardsOnTable.length && i<minimumCards); i++)
        {
        	cardsOnTable[i] = myDeck.deal();
        	if(cardsOnTable[i]!=null)
        		{numberOfCardsOnTheTable++;}
        }
        
        if(debugging)
        	{System.out.println("There are "+numberOfCardsOnTheTable+" cards on the table.");}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
        for (int i = 0; i<cardsOnTable.length; i++)
        {
        	if(cardsOnTable[i]!=null)
        	{
        		int x;
        		int y;
        		if(dealingStyle == HORIZONTALLY)
        		{
	        		x = firstCardX+(cardWidth+cardBufferX)*(i%columns);
	    			y = firstCardY+(cardHeight+cardBufferY)*(i/columns);
        		}
        		else //if(dealingStyle == VERTICALLY)
    			{
    				x = firstCardX+(cardWidth+cardBufferX)*(i/rows);
    				y = firstCardY+(cardHeight+cardBufferY)*(i%rows);
    			}
        		cardsOnTable[i].draw(g, x, y, cardWidth, cardHeight);
        	}
        }
		
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
	
	public void mouseClicked(MouseEvent event) {
		int xMousePosition = event.getX()-xMouseOffsetToContentPaneFromJFrame;
		int yMousePosition = event.getY()-yMouseOffsetToContentPaneFromJFrame;
		
		if(debugging)
			{System.out.println("clicked at position ("+xMousePosition+","+yMousePosition+").");}
		
		//determine which card, if any, the user clicked on, and select or deselect it as [relevent]
		for(int i = 0; i<cardsOnTable.length; i++)
		{
			int xPosition = firstCardX+(cardWidth+cardBufferX)*(i%columns);
			int yPosition = firstCardY+(cardHeight+cardBufferY)*(i/columns);
			if ((xPosition <= xMousePosition && xMousePosition <= xPosition + cardWidth) 
					&& (yPosition <= yMousePosition && yMousePosition <= yPosition + cardHeight))
			{
				if(cardsOnTable[i].isSelected()) // card is currently selected, and this click will deselect it
				{
					// remove from selectedCards
				}
				else //card is currently deselected, and this click will select it
				{
					// add to selectedCards
				}
				cardsOnTable[i].switchSelectedOrDeselected();
				
				if(debugging)
					{System.out.println("clicked on card # "+i+" (row "+i/columns+", column "+i%columns+")");}
			}
		}
		
		//if the number of cards selected is the number of cards needed for a set.
		{
			//if it is a set
			{
				//give points
				//remove selected cards from table
				
				boolean dealing = true;
				//while(dealing)
				{
					if (howManySets() == 0) // there are no sets on the table
					{
						//if there are cards in the deck
						{
							//dealCardsToTable(numberOfCardsInASet)
						}
						//else //there are no cards left in the deck
						{
							//game over
							dealing = false;
						}
					}
					else // there is at least one set on the table
					{
						//if there are less cards on the table than we want there to be cards on the table
						{
							//if there are cards in the deck
							{
								//dealCardsToTable(numberOfCardsInASet)
							}
							//else //no cards left in the deck
							{
								dealing = false;
							}
						}
						//else //there are enough cards on the table
						{
							dealing = false;
						}
					}//end else
				}//end while
			}//end if
			//else //it is not a set
			{
				//scold, maybe deduct points
			}
			//empty selectedCards
		}
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
