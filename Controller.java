import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Controller extends JFrame   implements MouseListener{
	
	private boolean debugging = false;
	private boolean halfsize = true;
	
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
    
    private int width;
    private int height;

	public static void main(String[] args) 
	{
		
        Controller myController = new Controller();
	}
	
	Controller()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int)screenSize.getWidth()/(halfsize?2:1);
		height = (int)screenSize.getHeight()-40;
		
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
        
        Graphics g = this.getGraphics();
        
        // "Hint" button
		JButton hint = new JButton("Hint");
		hint.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(gameInitialized)
				{
					//TODO hint button -- Make this useful
					System.out.println("you clicked the hint button.");
					
					Font current = g.getFont();
					Color currentColor = g.getColor();
					g.setColor(Color.white);
					g.setFont(new Font(null, Font.CENTER_BASELINE, 50) );
					g.fillRoundRect(width/4, height/4, 550, 200, 10, 10);
					g.setColor(Color.magenta);
					g.drawString("There is at least one", width/4+15 ,height/4+65);
					g.drawString("set on the table.", width/4+15 ,height/4+65+50);
					g.drawString("Not telling you where.", width/4+15 ,height/4+65+100);
					g.setFont(current);
					g.setColor(currentColor);
				}
			}
		});
		
		// "New game" button
		JButton start = new JButton("New Game");
		start.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(gameInitialized)
				{
					int sure = JOptionPane.showConfirmDialog(null, "Are you sure? This will restart the entire game and you will lose all your progress."); //this will return 0 for yes, 1 for no, 2 for cancel, and -1 for X
					if(sure == 0)//yes, they're sure.
					{
						start();
					}
				}
				else
				{
					start();
				}
			}
		});
		
		hint.setBounds(10, 10, 100, 30);
		start.setBounds(120, 10, 100, 30);
		this.add(hint);
		this.add(start);
		
	}
	
	private void start()
	{
		
		
		gameInitialized = false;
		
		//TODO start menu
		//set up the JFrame
		Container startContent;
		JFrame startMenu = new JFrame();
		startMenu.setSize(width, height);
		startMenu.setVisible(true);
		startMenu.addMouseListener(this);
	    startMenu.setLocation(0, 0);
	    startMenu.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    startContent = startMenu.getContentPane();
        startContent.setLayout(null); // not need layout, will use absolute system
		Graphics g = startMenu.getGraphics();
		
		JTextField vars = new JTextField("  # of variables");
		vars.setBounds(width/2-175, 50, 100, 32);
		startMenu.add(vars);
		vars.setColumns(10);
		
		JTextField size = new JTextField(" size of a set");
		size.setBounds(width/2-50, 50, 100, 32);
		startMenu.add(size);
		vars.setColumns(10);
		
		//g.drawString("Please enter the following values and click 'create'",width/2-75,70);
		
		JButton create = new JButton("Select Deck Parameters");
		create.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				
				//try to set the variables to what the user chose but if they did a stupid or tried to break it then just make them the defaults.
				try{
					numberOfVariables = Integer.parseInt( vars.getText() );
				}catch(Exception broke)
				{
					numberOfVariables = 4;
				}
				try{
					cardsToASet = Integer.parseInt( size.getText() );
				}catch(Exception broke)
				{
					cardsToASet = 3; 
				}
				//make sure they're within the allowable bounds
				// up to Card.numberOfVariablesAvailable
				if(numberOfVariables>Card.numberOfVariablesAvailable)
				{
					numberOfVariables = Card.numberOfVariablesAvailable;
				}
				if(numberOfVariables<1)
				{
					numberOfVariables = 1;
				}
				// 3, 4, or 5
				if(cardsToASet>5)
				{
					cardsToASet = 5;
				}
				if(cardsToASet<3)
				{
					cardsToASet = 3;
				}
				
				int[][] v = new int[numberOfVariables][cardsToASet];
				
				//have the user choose whether to go with the default or to customize their deck
				JButton defaultDeck = new JButton("Default Deck");
				JButton customDeck = new JButton("Custom Deck");
				
				defaultDeck.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e) 
					{
						for(int i = 0; i<numberOfVariables; i++)
						{
							for(int j = 0; j<cardsToASet; j++)
							{
								v[i][j]= (i==Card.NUMBER?(j+1):(j)); //if this is the NUMBER variable, set this to j+1, else set this to j. That way all variables but NUMBER go from 0 to setSize-1 and NUMBER goes from 1 to setSize
							}
						}
						initialize(numberOfVariables,cardsToASet,v);
						startMenu.dispose();
					}
				});
				
				customDeck.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e) 
					{
						//TODO
						// have the user select options for each variable except for numbers, which are automatically assigned based on how many cards there are to a set
						// (for example: if there are three cards to a set, the user will now select three colors, three shapes, three shadings, etc.)
						// The selected variables should be represented in the v array as integers.
						// (you can call things such as Card.SQUARE, Card.colors[i] and other public Card variables as a reference)
						int count = 0;
						//for each variable, choose options for each card in a set
						for(int i = 0 ; i<numberOfVariables; i++)
						{
							for(int j = 0; j<cardsToASet; j++)
							{
								if(i==Card.COLOR)
								{
									JComboBox<Color> style = new JComboBox<Color>();
									for(int c = 0; c<Card.colors.length; c++)
									{
										style.addItem(Card.colors[c]);
									}
								}
								if(i==Card.NUMBER)
								{
									;
								}
								if(i==Card.SHAPE)
								{
									
								}
								if(i==Card.FILL)
								{
									
								}
								if(i==Card.BORDER)
								{
									
								}
							}
							
							count++;
						}
						 initialize(numberOfVariables,cardsToASet,v);
						 startMenu.dispose();
					}
				});
				
				defaultDeck.setBounds(width/2-200, 100, 150, 32);
				customDeck.setBounds(width/2+50, 100, 150, 32);
				startMenu.add(defaultDeck);
				startMenu.add(customDeck);
				startMenu.setVisible(false);
				startMenu.setVisible(true);
			}
		});
		
		create.setBounds(width/2+75, 50, 200, 32);
		startMenu.add(create);
	}
	
	private void initialize(int variables, int setSize, int[][] v)
	{
		points = 0;
		numberOfCardsOnTheTable = 0;
		numberOfSelectedCards = 0;
		
		int height_width_ratio = 2;
		int border_proportion = 5; //this determines the space between cards based on the width of the cards. The higher the number, the smaller the distance.
		
		//these are requested from the user in start(), which passes them in to here.
		numberOfVariables = variables;
		cardsToASet = setSize;
		
		//where to draw everything
		int borderWidth = (width - gameContentPane.getWidth())/2; //this is identical to the calcualtion above in the constructor
		firstCardX = borderWidth+20;
		firstCardY = borderWidth+80;
		
		//TODO choose rows and columns based on the size of the deck. (rows*columns MUST be greater than the maximum number of cards that can be on the table w/out a set)
		int min = 20; // TODO this is the variable that needs to be modified based on the deck size.
		int cols = 0;
		columns = 0;
		do
		{
			columns++;
			cols = columns+((cardsToASet+2)/2); //columns of full-sized cards + setSize+1 columns of half-sized cards
			rows = (int) height*cols/width/height_width_ratio; //using the ratio between cardWidth and cardHeight and the number of columns that are on the table, calculate how many rows will fit
			
			if(debugging)
			{
				System.out.println(columns+" column"+(columns==1?"":"s")+", "+rows+" row"+(rows==1?"":"s")+": can hold "+rows*columns+" card"+(rows*columns==1?"":"s")+"; need "+min+" cardslots."+(rows*columns<min?" Resizing...":" Sufficient."));
			}
		}while(rows*columns < min);
		
		
		cardWidth = (width-borderWidth-firstCardX)/cols*border_proportion/(border_proportion+1);
		cardHeight = cardWidth*height_width_ratio;
		cardBufferX = cardWidth/border_proportion;
		cardBufferY = cardBufferX;
		
		//dealingStyle = VERTICALLY;
		dealingStyle = HORIZONTALLY;
		
		minimumCards = dealingStyle==HORIZONTALLY?(rows-1)*columns:rows*(columns-1);
		
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
        repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if(gameInitialized)
		{
			g.drawString("Sets found: "+points, 250, 60);
			
			//paint all cards on the table
			int counter = 0;
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
	        		int x = firstCardX + (cardWidth+cardBufferX)*(columns) + (cardWidth+cardBufferX)/2*( (cardsToASet-(c%cardsToASet)) + (c/(cardsToASet*rows*2))*(cardsToASet+1) ) ; // starting position + combined width of all the dealt cards + width of one minicard *( (how many will be in this row - how many are already in this row /*because I am painting them in reverse order but wish them to be drawn across the row in the order they were selected*/) + (how many rows are before this row)*(how many cards are in each of those rows + 1 (as a buffer space))
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
		
		if(cardsToASet ==3 )
		{
			
			for(int card1=0;card1<tableCards.length-2; card1++)
				{
				for(int card2=card1+1;card2<tableCards.length-1; card2++)
					{
					for(int card3=card2+1;card3<tableCards.length; card3++)
						{
						test[0] = tablecards[card1];
						test[1] = tablecards[card2];
						test[2] = tablecards[card3];
						if (isASet(test)==true)
							{numberOfSets++;}
						//System.out.println(numberOfSets);
						}
					}
				}
			System.out.println(numberOfSets);
			return numberOfSets;
		}
		else if(cardsToASet==4)
		{
			return 1;
		}
		else if(cardsToASet==5)
		{
			return 1;
		}
		else
		{
			return 1;
		}
		
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
			int[] v = new int[Card.colors.length]; // (colors is currently our variable with the most options)
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
	
	private void cardClick(int xMousePosition, int yMousePosition)
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
	
	private void checkSet()
	{
		Graphics g = this.getGraphics();
		repaint();
		if(isASet(selectedCards))//if it is a set
		{
			//show the user that it is a set
			Font current = g.getFont();
			Color currentColor = g.getColor();
			g.setColor(Color.white);
			g.setFont(new Font(null, Font.CENTER_BASELINE, 80) );
			g.fillRoundRect(width/2, height/2, 90, 90, 10, 10);
			g.setColor(Color.blue);
			g.drawString(":)", width/2+15 ,height/2+65);
			try { Thread.sleep(1500);}catch (Exception e){;}
			g.setFont(current);
			g.setColor(currentColor);
			
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
			//TODO scold, maybe deduct points
			System.out.println("Sorry, not a set.");
			
			//TODO change to something on the JFrame (maybe turn them all red for a moment?)
			Font current = g.getFont();
			Color currentColor = g.getColor();
			g.setColor(Color.white);
			g.setFont(new Font(null, Font.CENTER_BASELINE, 80) );
			g.fillRoundRect(width/2, height/2, 130, 90, 10, 10);
			g.setColor(Color.red);
			g.drawString(">:(", width/2+15 ,height/2+65);
			try { Thread.sleep(1500);}catch (Exception e){;}
			g.setFont(current);
			g.setColor(currentColor);
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
	 * private void dealTilFull()
	 * This method will deal cards onto the table until there is at least one set on the table and 
	 * there are at least minimumCards on the table, or until there are no cards left in the
	 * deck, whichever comes first.
	 */
	private void dealTilFull()
	{
		boolean gameOver = false;
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
					gameOver = true;
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
		
		if(gameOver)
		{
			gameOver();
		}
	}
	
	private void gameOver()
	{
		//TODO game over. There are no sets on the table, nor are there cards left in the deck. 
		//You'll want to do different things based on whether or not there are any cards on the table still. (The latter is definitely a win, the former not so much)
		
		boolean win = true;
		//if there are any cards still on the table, then you didn't win.
		for(int c = 0; c<cardsOnTable.length; c++)
		{
			if(cardsOnTable[c]!=null)//there is a card here
			{
				win = false;
			}
		}
		if(win)
		{
			//TODO "you win!"
		}
		else
		{
			//TODO "you lose. :("
		}
		
		gameInitialized = false;
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
