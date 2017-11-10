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
    private int numberOfCardsOnTheTable;
    private int numberOfSelectedCards;
    
    private int numberOfVariables;
    private int cardsToASet;
    private int minimumCards;
    //variables for drawing the cards
    Card[] cardsOnTable;
    Card[] selectedCards;
    Card[] foundSets;
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
        
        start();
	}
	
	private void start()
	{
		//TODO start menu
		
		//TODO have the user choose how many variables and how many cards to a set
		int variables = 4; // up to Card.numberOfVariablesAvailable
		int setSize = 3; // 3, 4, or 5
		
		int[][] v = new int[variables][setSize];
		
		//TODO have the user choose whether to go with the default or to customize their deck
		boolean custom = false;
		
		if(custom)
		{
			//TODO
			// have the user select options for each variable except for numbers, which are automatically assigned based on how many cards there are to a set
			// (for example: if there are three cards to a set, the user will now select three colors, three shapes, three shadings, etc.)
			// The selected variables should be represented in the v array as integers.
			// (you can call things such as Card.SQUARE, Card.colors[i] and other public Card variables as a reference)
			
		}
		else //default
		{
			//TODO set v to the default values
			for(int i = 0; i<variables; i++)
			{
				for(int j = 0; j<setSize; j++)
				{
					v[i][j]= (i==Card.NUMBER?(j+1):(j)); //if this is the NUMBER variable, set this to j+1, else set this to j. That way all variables but NUMBER go from 0 to setSize-1 and NUMBER goes from 1 to setSize
				}
			}
		}
		
		
        initialize(variables,setSize,v);
        repaint();
	}
	
	private void initialize(int variables, int setSize, int[][] v)
	{
		points = 0;
		numberOfCardsOnTheTable = 0;
		numberOfSelectedCards = 0;
		
		//these are requested from the user in start(), which passes them in to here.
		numberOfVariables = variables;
		cardsToASet = setSize;
		
		//TODO eventually make these variables dynamic based on the screen size, number of variables, and size of a set, but for now they are hardcoded in.
		//where to draw everything
		rows = 6;
		columns = 6;
		cardWidth = 75;
		cardHeight = 150;
		firstCardX = 150;
		firstCardY = 50;
		cardBufferX = 10;
		cardBufferY = 10;
		//dealingStyle = VERTICALLY;
		dealingStyle = HORIZONTALLY;
		if(dealingStyle == VERTICALLY)
		{
			minimumCards = rows*(columns-2);
		}
		else
		{
			minimumCards = (rows-2)*columns;
		}
		
		if(minimumCards>rows*columns)
		{
			throw new IllegalArgumentException("require more cards than can fit on the table.");
		}
		
		//initialize deck and all arrays
		myDeck = new Deck(numberOfVariables,cardsToASet,v);
		//myDeck.shuffle();
		foundSets = new Card[myDeck.numberOfCards()];
		selectedCards = new Card[cardsToASet];
		cardsOnTable = new Card[rows*columns];
		
		//deal cards to the table
        for (int i = 0; (i<cardsOnTable.length && i<minimumCards); i++)
        {
        	cardsOnTable[i] = myDeck.deal();
        	if(cardsOnTable[i]!=null)
        		{numberOfCardsOnTheTable++;}
        }
        
        gameInitialized = true;
        
        //make sure that there is at least one set on the table (as well as the minimum number of cards, but that has been satisfied above)
        dealTilFull();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if(gameInitialized)
		{
			int counter = 0;
			//paint all cards on the table
	        for (int i = 0; i<cardsOnTable.length; i++)
	        {
	        	if(cardsOnTable[i]!=null)
	        	{
	        		int x;
	        		int y;
	        		if(dealingStyle == HORIZONTALLY)
	        		{
		        		x = firstCardX+(cardWidth+cardBufferX)*(counter%columns);
		    			y = firstCardY+(cardHeight+cardBufferY)*(counter/columns);
	        		}
	        		else //if(dealingStyle == VERTICALLY)
	    			{
	    				x = firstCardX+(cardWidth+cardBufferX)*(counter/rows);
	    				y = firstCardY+(cardHeight+cardBufferY)*(counter%rows);
	    			}
	        		cardsOnTable[i].draw(g, x, y, cardWidth, cardHeight);
	        		counter++;
	        	}
	        }
	        
	        //paint all cards in sets already claimed, in reverse order, at half the size of the regular cards
	        int c = 0;
	        for( int i = foundSets.length-1 ; i>=0; i--)
	        {
	        	if(foundSets[i]!=null)
	        	{
		        	//int x = firstCardX + (cardWidth+cardBufferX)*(columns) + (cardWidth+cardBufferX)/2*( 1 + c%cardsToASet + (c/(cardsToASet*rows*2))*(cardsToASet+1) ) ; // starting position + combined width of all the dealt cards + width of one minicard *( 1 (as a buffer space) + how many are already in this row + (how many rows are before this row)*(how many cards are in each of those rows + 1 (as a buffer space))
	        		int x = firstCardX + (cardWidth+cardBufferX)*(columns) + (cardWidth+cardBufferX)/2*( 1 + (cardsToASet-(c%cardsToASet)) + (c/(cardsToASet*rows*2))*(cardsToASet+1) ) ; // starting position + combined width of all the dealt cards + width of one minicard *( 1 (as a buffer space) + (how many will be in this row - how many are already in this row /*because I am painting them in reverse order but wish them to be drawn across the row in the order they were selected*/) + (how many rows are before this row)*(how many cards are in each of those rows + 1 (as a buffer space))
		        	int y = firstCardY+(cardHeight+cardBufferY)/2*((c/cardsToASet)%(rows*2));
		        	
		        	foundSets[i].draw(g, x, y, cardWidth/2, cardHeight/2);
		        	c++;
	        	}
	        }
		}
	}
	
	private int howManySets(Card[] tableCards)
	{
		if(!gameInitialized)
	  		{throw new IllegalArgumentException("game has not been initialized");}
		
		Card [] tablecards = new Card [tableCards.length];
		Card [] test = new Card[3];
		int numberOfSets = 0;
		
		for (int i = 0; i<tableCards.length; i++)
		{
			tablecards[i] = tableCards[i];
		}
		
		for(int card1=0;card1<tableCards.length-2; card1++)
			{
			for(int card2=card1+1;card2<tableCards.length-1; card2++)
				{
				for(int card3=card2+1;card3<tableCards.length; card3++)
					{
					test[0] = tablecards[card1];test[1] = tablecards[card2];test[2] = tablecards[card3];
					if (isASet(test)==true)
						{numberOfSets++;}
					//System.out.println(numberOfSets);
					}
				}
			}
		System.out.println(numberOfSets);
		return numberOfSets;
		
		//return 1;
	}
	
	private boolean isASet(Card[] cards)
	{
		if(!gameInitialized)
  			{throw new IllegalArgumentException("game has not been initialized");}
		
		boolean isASet = true;
		//if the number of cards passed in is incorrect
		if(cards.length!=cardsToASet)
		{
			isASet = false;
		}
		//if the array of cards is not full
		for( int i  = 0; i < cards.length; i++)
		{
			if(cards[i]==null)
			{
				isASet = false;
				return isASet;
			}
		}
		//if any variable appears at least twice but not on every card
		for(int i = 0; i<numberOfVariables ; i++)//for each variable
		{
			//int[] v = new int[cardsToASet];
			int[] v = new int[7]; // 7 is the numberOfShapes available, which is currently our variable with the most options
			for(int k = 0 ; k<v.length ; k ++)
			{
				v[k] = 0;
			}
			for(int j = 0 ; j < cards.length ; j++)
			{
				v[(i==Card.NUMBER?(cards[j].getVariable(i)-1):(cards[j].getVariable(i)))]++;
			}
			
			for(int k = 0 ; k<cardsToASet ; k ++)
			{
				if( v[k]>=2 && v[k]!=cardsToASet )
				{
					isASet = false;
				}
				
				/* logically equivalent but a LITTLE messier
				 * if( ! ((v[k]<2) || v[k]==cardsToASet) )
				 * {
				 * 		isASet = false;
				 * }
				 */
			}
		}
		
		return isASet;
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
			
			if(debugging)
				{System.out.println("clicked at position ("+xMousePosition+","+yMousePosition+").");}
			
			//determine which card, if any, the user clicked on, and select or deselect it as applicable
			cardClick(xMousePosition, yMousePosition);
			
			if(numberOfSelectedCards >= cardsToASet) //if the number of cards selected is the number of cards needed for a set.
			{
				checkSet();
			}
		}
	}//end mouse event
	
	public void cardClick(int xMousePosition, int yMousePosition)
	{
		//determine which card, if any, the user clicked on, and select or deselect it as applicable
		int counter = 0;
		for(int i = 0; i<cardsOnTable.length; i++)
		{
			if(cardsOnTable[i]!=null)
			{
				int xPosition;
				int yPosition;
				if(dealingStyle == HORIZONTALLY)
        		{
	        		xPosition = firstCardX+(cardWidth+cardBufferX)*(counter%columns);
	    			yPosition = firstCardY+(cardHeight+cardBufferY)*(counter/columns);
        		}
        		else //if(dealingStyle == VERTICALLY)
    			{
    				xPosition = firstCardX+(cardWidth+cardBufferX)*(counter/rows);
    				yPosition = firstCardY+(cardHeight+cardBufferY)*(counter%rows);
    			}
				
				if ((xPosition <= xMousePosition && xMousePosition <= xPosition + cardWidth) 
						&& (yPosition <= yMousePosition && yMousePosition <= yPosition + cardHeight)
						&& cardsOnTable[i]!= null)
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
				}
				counter++;
			}
		}
	}//end cardClick
	
	public void checkSet()
	{
		if(isASet(selectedCards))//if it is a set
		{
			System.out.println("It's a set!");//TODO make some sort of announcement on screen
			//give points
			points++;
			
			//add selected cards to foundSets array
			for(int i = 0; i<selectedCards.length; i++)
			{
				boolean added = false;
				if(selectedCards[i]!=null)//this should always be true, considering where we are, but just in case
				{
					for(int count = 0; (!added && count<foundSets.length) ; count++)
					{
						if(foundSets[count]==null)
						{
							foundSets[count] = selectedCards[i];
							added = true;
						}//end if
					}//end for
				}//end if
			}//end for
			
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
			
			dealTilFull();
			
		}//end if
		else //it is not a set
		{
			//scold, maybe deduct points
			System.out.println("Sorry, not a set.");//TODO change to something on the JFrame (maybe turn them all red for a moment?)
		}
		
		//empty selectedCards
		for(int i = 0; i<selectedCards.length; i++)
		{
			if(selectedCards[i]!=null)//this should always be true, considering where we are, but just in case
			{
				deselect(selectedCards[i]);
			}
		}
	}//end checkSet
	
	/**
	 * public void dealTilFull()
	 * This method will deal cards onto the table until there is at least one set on the table and 
	 * there are at least minimumCards on the table, or until there are no cards left in the
	 * deck, whichever comes first.
	 */
	public void dealTilFull()
	{
		boolean dealing = true;
		while(dealing)
		{
			if (howManySets(cardsOnTable) == 0) // there are no sets on the table
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
