//"Fine, this is a sarcasm" Hann "Flyin" Solo
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
	
	private int howManySets(Card[] tableCards, int setSize)
	{
		if(!gameInitialized)
	  		{throw new IllegalArgumentException("game has not been initialized");}
		
		//Card [] tablecards = new Card [tableCards.length];
		Card [] test = new Card[setSize];
		int[] placeOnTable = new int[setSize];

		int numberOfSets = 0;
		int current=setSize-1; //the spot in place on table that is getting incremented

		int end=numberOfCardsOnTheTable; //this is just a convenient shortcut so that I do not have to write tablecards.length everytime
		
		for(int x =0; x<test.length; x++)
				{placeOnTable[x]=x;}
		while(placeOnTable[0]<=end-setSize+current)
		{	
			while(placeOnTable[current]<end)
			{
				
//				for(int i=0; i<placeOnTable.length; i++)
//				{
//					System.out.println(placeOnTable[i]);
//				}
				
				for(int x=0; x<test.length; x++)
					{test[x] = new Card();
					test[x] = tableCards[placeOnTable[x]];
					//System.out.println(tablecards[placeOnTable[x]].toString());
					//System.out.println(placeOnTable[x]);
					//System.out.println(tableCards[x].toString());
					//System.out.println(test[x].toString());
					}
				
				if (isASet(test))
					{numberOfSets++;
					System.out.println("sets so far" + numberOfSets);
					for (int i =0; i<3; i++)
						{
							System.out.println(placeOnTable[i]);
						}
						}
				placeOnTable[current]++;
				//System.out.println(" ");
				
				//System.out.println(" ");
			}
//			System.out.println("end");
//			System.out.println(end);
//			System.out.println("Current");
//			System.out.println(current);
//			System.out.println(" pl o cur");
//			System.out.println(placeOnTable[current]);
//			System.out.println(" end -");
//			System.out.println(end-setSize+current);
			while ((placeOnTable[current]>=end-setSize+current)&&current>0)
			{
				current--;
			}
			placeOnTable[current]++;
			for(int x=current+1;x<setSize;x++)
				{placeOnTable[x] = placeOnTable[x-1]+1;}
			current = setSize-1;
		}
		System.out.println("num of s"+numberOfSets);
		return numberOfSets;
		
	}
	
	private boolean isASet(Card[] cards)
	{
		//System.out.println("we arrived");
		if(!gameInitialized)
  			{throw new IllegalArgumentException("game has not been initialized");}
		/*for(int i=0; i<cards.length; i++)
			{
			System.out.println(i);	
			System.out.println(cards[i].toString());
			}*/
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
			//check to see if you do not have a set
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
		if (isASet)
		{for(int i=0; i<cards.length; i++)
			{
				System.out.println(cards[i].toString());
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
				//throw new IllegalArgumentException("Tried to deal more cards than can fit on the table.");
				for(int c=0; c<cardsOnTable.length; c++)
				{
					cardsOnTable[c]=null;
				}
				//shuffle the deck
				myDeck.shuffle();
				//deal to the table again
				dealTilFull();
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
			if (howManySets(cardsOnTable, cardsToASet ) == 0) // there are no sets on the table
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
