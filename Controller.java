import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

public class Controller extends JFrame   implements MouseListener{
	
	private boolean debugging = false;
	private boolean gameInitialized = false;
	private int points;
	
    private Container gameContentPane;
    private boolean gameIsReady = false;
    
    private Deck myDeck;

    Card[] cardsOnTable;
    Card[] selectedCards;
    private int numberOfCardsOnTheTable;
    private int numberOfSelectedCards;
    
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
		int width = (int)screenSize.getWidth()/2;
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

     // register this class as a mouse event listener for the JFrame
        this.addMouseListener(this);
        
        initialize();
        
        repaint();
	}
	
	private void initialize()
	{
		points = 0;
		numberOfCardsOnTheTable = 0;
		numberOfSelectedCards = 0;
		
		//eventually make these variables dynamic, but for now they are hardcoded in.
		numberOfVariables = 3;
		cardsToASet = 3;
		rows = 3;
		columns = 9;
		cardWidth = 75;
		cardHeight = 150;
		firstCardX = 200;
		firstCardY = 50;
		cardBufferX = 10;
		cardBufferY = 10;
		minimumCards = 18;
		
		if(minimumCards>rows*columns)
		{
			throw new IllegalArgumentException("require more cards than can fit on the table.");
		}
		
		dealingStyle = VERTICALLY;
		//dealingStyle = HORIZONTALLY;
		
		myDeck = new Deck(numberOfVariables,cardsToASet);
		selectedCards = new Card[cardsToASet];
		cardsOnTable = new Card[rows*columns];
        for (int i = 0; (i<cardsOnTable.length && i<minimumCards); i++)
        {
        	cardsOnTable[i] = myDeck.deal();
        	if(cardsOnTable[i]!=null)
        		{numberOfCardsOnTheTable++;}
        }
        
        gameInitialized = true;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if(gameInitialized)
		{
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
	}
	
	private int howManySets()
	{
		if(!gameInitialized)
	  		{throw new IllegalArgumentException("game has not been initialized");}
		
		int count = 0;
		//TODO
		count = 1; // TODO delete this later when the function works
		return count;
	}
	
	private boolean isASet(Card[] cards)
	{
		if(!gameInitialized)
  			{throw new IllegalArgumentException("game has not been initialized");}
		//TODO
		return true;
	}
	
	private void resetGame()
	{
		//TODO
	}
	
	private void select(Card c)
	{
		boolean added = false;
		for(int count = 0; (!added && count<selectedCards.length) ; count++)
		{
			if(selectedCards[count]==null)
			{
				selectedCards[count] = c;
				added = true;
			}
		}
		c.select();
		numberOfSelectedCards++;
	}
	private void deselect(Card c)
	{
		boolean removed = false;
		for(int count = 0; (!removed && count<selectedCards.length) ; count++)
		{
			if(selectedCards[count]!=null && selectedCards[count].equals(c))
			{
				selectedCards[count] = null;
				removed = true;
			}
		}
		c.deselect();
		numberOfSelectedCards--;
	}
	
	private void dealCardsToTable()
	{
		for(int i = 0; i<cardsToASet; i++) // deal as many as it takes to make a set
		{
			//stick it in the first available spot
			boolean added = false;
			for(int count = 0; (!added && count<cardsOnTable.length) ; count++)
			{
				if(cardsOnTable[count]==null)
				{
					cardsOnTable[count] = myDeck.deal();
					added = true;
				}
			}
			numberOfCardsOnTheTable++;
			if(!added)//room wasn't found
			{
				throw new IllegalArgumentException("Tried to deal more cards than can fit on the table.");
			}
		}
	}
	
	public void mousePressed(MouseEvent event) {
		if(gameInitialized)
		{
			int xMousePosition = event.getX()-xMouseOffsetToContentPaneFromJFrame;
			int yMousePosition = event.getY()-yMouseOffsetToContentPaneFromJFrame;
			
			/*if(debugging)
				{System.out.println("clicked at position ("+xMousePosition+","+yMousePosition+").");}*/
			
			//determine which card, if any, the user clicked on, and select or deselect it as [relevent]
			for(int i = 0; i<cardsOnTable.length; i++)
			{
				int xPosition;
				int yPosition;
				if(dealingStyle == HORIZONTALLY)
        		{
	        		xPosition = firstCardX+(cardWidth+cardBufferX)*(i%columns);
	    			yPosition = firstCardY+(cardHeight+cardBufferY)*(i/columns);
        		}
        		else //if(dealingStyle == VERTICALLY)
    			{
    				xPosition = firstCardX+(cardWidth+cardBufferX)*(i/rows);
    				yPosition = firstCardY+(cardHeight+cardBufferY)*(i%rows);
    			}
				
				if ((xPosition <= xMousePosition && xMousePosition <= xPosition + cardWidth) 
						&& (yPosition <= yMousePosition && yMousePosition <= yPosition + cardHeight))
				{
					if(cardsOnTable[i].isSelected()) // card is currently selected, and this click will deselect it
					{
						deselect(cardsOnTable[i]);
					}
					else //card is currently deselected, and this click will select it
					{
						select(cardsOnTable[i]);
					}
					
					repaint();
					
					/*if(debugging)
					{
						System.out.println("clicked on card #"+i+" (row "+( ( dealingStyle == HORIZONTALLY?(i/columns):(i%rows) ) +1)
								+", column "+(( dealingStyle == HORIZONTALLY?(i%columns):(i/rows) )+1)+"), "+(cardsOnTable[i].isSelected()?"selecting":"deselecting")+" it.");
						System.out.println("\n"+numberOfSelectedCards+" card"+(numberOfSelectedCards==1?" is":"s are")+" selected: ");
						for(int j = 0; j<selectedCards.length;j++)
						{
							System.out.println(selectedCards[j] + " ");
						}
						System.out.println("");
					}*/
				}
			}
			
			if(numberOfSelectedCards == cardsToASet) //if the number of cards selected is the number of cards needed for a set.
			{
				if(isASet(selectedCards))//if it is a set
				{
					//give points
					points++;
					// remove selected cards from table, but not yet from the array selectedCards
					for(int i = 0; i<selectedCards.length; i++)
					{
						boolean removed = false;
						if(selectedCards[i]!=null)//this should always be true, considering where we are, but just in case
						{
							for(int count = 0; (!removed && count<cardsOnTable.length) ; count++)
							{
								if(cardsOnTable[count]!=null && cardsOnTable[count].equals(selectedCards[i]))
								{
									cardsOnTable[count] = null;
									removed = true;
								}//end if
							}//end for
							numberOfCardsOnTheTable--;
						}//end if
					}//end for
					
					boolean dealing = true;
					while(dealing)
					{
						if (howManySets() == 0) // there are no sets on the table
						{
							if(debugging)
							{
								System.out.println("No sets on the table");
							}
							if(myDeck.cardsRemaining()) //if there are still more cards in the deck
							{
								dealCardsToTable();
							}
							else //there are no cards left in the deck
							{
								if(debugging)
								{
									System.out.println("deck is empty");
								}
								//TODO game over
								dealing = false;
							}
						}
						else // there is at least one set on the table
						{
							if(numberOfCardsOnTheTable<minimumCards)// if there are less cards on the table than we want there to be cards on the table
							{
								if(debugging)
								{
									System.out.println(numberOfCardsOnTheTable+" cards on the table: not enough cards on the table");
								}
								
								if(myDeck.cardsRemaining()) //if there are still more cards in the deck
								{
									dealCardsToTable();
								}
								else //no cards left in the deck
								{
									if(debugging)
									{
										System.out.println("deck is empty");
									}
									dealing = false;
								}
								if(debugging)
								{
									System.out.println(numberOfCardsOnTheTable+" cards on the table.");
								}
							}
							else //there are enough cards on the table
							{
								dealing = false;
							}
						}//end else
					}//end while
				}//end if
				else //it is not a set
				{
					//scold, maybe deduct points
					System.out.println("Sorry, not a set.");//TODO change to something on the JFrame
				}
				
				//empty selectedCards
				for(int i = 0; i<selectedCards.length; i++)
				{
					if(selectedCards[i]!=null)//this should always be true, considering where we are, but just in case
					{
						deselect(selectedCards[i]);
					}
				}
			}
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {
		;
	}

	public void mouseExited(MouseEvent arg0) {
		;
	}

	public void mouseClicked(MouseEvent arg0) {
		;
	}

	public void mouseReleased(MouseEvent arg0) {
		;
	}
}
