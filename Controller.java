//"Fine, this is a sarcasm" Hann "Flyin" Solo
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.*;
import javax.swing.*;

public class Controller extends JFrame   implements MouseListener{
	
	private boolean debugging = false;
	private boolean halfsize = true;
	
	private boolean gameInitialized = false;
	private int points;
	int width;
	int height;
	
    private Container gameContentPane;
    private boolean gameIsReady = false;
    private Deck myDeck;
    private int numberOfCardsOnTheTable;
    private int numberOfSelectedCards;
    
    private int numberOfVariables;
    private int cardsToASet;
    private int minimumCards;
    private int[][] variables; //this holds the number values of the variables in the deck and is initialized within the start method. It currently is only called in the method isASet
    private int largestVariable;
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
		
		width = (int)screenSize.getWidth()/(halfsize?2:1);
		height = (int)screenSize.getHeight();
		
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
					g.fillRoundRect(width/2-120, height/4, 500, 150, 10, 10);
					g.setColor(Color.magenta);
					int howMany = howManySets(cardsOnTable,cardsToASet);
					g.drawString( ("There "+(howMany==1?"is ":"are ")+howMany), width/2-100 ,height/4+65);
					g.drawString("set"+(howMany==1?" ":"s ")+" on the table.", width/2-100 ,height/4+65+50);
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
		
		//choose the deck size
		JComboBox<String> vars = new JComboBox<String>();
		vars.addItem("# of variables");
		for(int v = 1; v<=Card.numberOfVariablesAvailable;v++)
		{
			vars.addItem(v+"");
		}
		JComboBox<String> size = new JComboBox<String>();
		size.addItem("Size of a SET");
		for(int s = 3; s<=5; s++)
		{
			size.addItem(s+"");
		}
		
		//have the user choose whether to go with the default or to customize their deck
		JButton defaultDeck = new JButton("Default Deck");
		JButton customDeck = new JButton("Custom Deck");
		
		defaultDeck.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//set to selected values
				if(vars.getSelectedIndex()==0)
				{
					numberOfVariables = 4;
				}
				else
				{
					numberOfVariables = Integer.parseInt((String)vars.getSelectedItem());
				}
				if(size.getSelectedIndex()==0)
				{
					cardsToASet = 4;
				}
				else
				{
					cardsToASet = Integer.parseInt((String)size.getSelectedItem());
				}
				variables = new int[numberOfVariables][cardsToASet];
				
				for(int i = 0; i<numberOfVariables; i++)
				{
					for(int j = 0; j<cardsToASet; j++)
					{
						variables[i][j]= (i==Card.NUMBER?(j+1):(j)); //if this is the NUMBER variable, set this to j+1, else set this to j. That way all variables but NUMBER go from 0 to setSize-1 and NUMBER goes from 1 to setSize
						System.out.print(variables[i][j]+" ");
					}
					System.out.println();
				}
				initialize(numberOfVariables,cardsToASet,variables);
				startMenu.dispose();
			}
		});
		
		customDeck.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				//set to selected values
				if(vars.getSelectedIndex()==0)
				{
					numberOfVariables = 4;
				}
				else
				{
					numberOfVariables = Integer.parseInt((String)vars.getSelectedItem());
				}
				if(size.getSelectedIndex()==0)
				{
					cardsToASet = 4;
				}
				else
				{
					cardsToASet = Integer.parseInt((String)size.getSelectedItem());
				}
				variables = new int[numberOfVariables][cardsToASet];
				
				int w = 30;
				int h = 30;
				
				//for each variable, choose options for each card in a set
				
				JComboBox[][] choices = new JComboBox[numberOfVariables][cardsToASet];
				for(int variable = 0 ; variable<numberOfVariables; variable++)
				{
					for(int card = 0; card<cardsToASet; card++)
					{
						if(variable== Card.COLOR)
						{
							choices[variable][card] = new JComboBox<ColorIcon>();
							for(int color = 0; color<Card.numberOfColorsAvailable; color++)
							{
								choices[variable][card].addItem(new ColorIcon(w,h,Card.colors[color]));
							}
						}
						else if(variable==Card.NUMBER)
						{
							choices[variable][card] = new JComboBox<Integer>();
							for(int number = 1 ; number < cardsToASet+1; number++)
							{
								choices[variable][card].addItem(number);
							}
						}
						else if(variable==Card.SHAPE)
						{
							choices[variable][card] = new JComboBox<ShapeIcon>();
							for(int shape = 0; shape<Card.numberOfShapesAvailable; shape++)
							{
								choices[variable][card].addItem(new ShapeIcon(w,h,shape));
							}
						}
						else if(variable==Card.FILL)
						{
							choices[variable][card] = new JComboBox<FillIcon>();
							for(int fill = 0; fill<Card.numberOfFillsAvailable; fill++)
							{
								choices[variable][card].addItem(new FillIcon(w,h,fill));
							}
						}
						else if(variable==Card.BORDER)
						{
							
							choices[variable][card] = new JComboBox<BorderIcon>();
							for(int border = 0 ; border<Card.numberOfBorderColorsAvailable; border++)
							{
								choices[variable][card].addItem(new BorderIcon(w,h,Card.colors[border]));
							}
						}
						
						choices[variable][card].setSelectedIndex(card);//have each JComboBox for this variable select a different value by default.
						choices[variable][card].setBounds(width/2-(numberOfVariables*(w+80)/2)+variable*(w+80), 200+card*(h*2), w+40, h+10);
						startMenu.add(choices[variable][card]);
					}//end for
				}//end for
				g.drawString("Please select your variables.", width/2-200, 150);
				
				JButton ready = new JButton("Ready");

				ready.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e) 
					{
						//check to make sure that no duplicates exist
						boolean noDuplicates = true;
						for(int variable = 0; variable<numberOfVariables && noDuplicates; variable++)
						{
							for(int card = 0; card<cardsToASet && noDuplicates;card++)
							{
								for(int check = 0; check<card && noDuplicates; check++)
								{
									if(choices[variable][card].getSelectedIndex()==(choices[variable][check].getSelectedIndex()))
									{
										noDuplicates = false;
										//System.out.println("User attempted a duplicate in variable "+variable+" at options "+check+" and "+card+": "+choices[variable][card].getSelectedIndex());
									}
								}
							}
						}
						
						if(noDuplicates)
						{
							largestVariable = 0;
							//copy all the things to variables double array
							for(int variable = 0; variable<numberOfVariables; variable++)
							{
								for(int card = 0; card<cardsToASet;card++)
								{
									int v = choices[variable][card].getSelectedIndex()+(variable==Card.NUMBER?1:0);//grab the value of the variable (increase by 1 if it's a number
									variables[variable][card] = v;//put it in the variables array
									if(v>largestVariable)//if this variable is bigger than the current biggest
									{
										largestVariable = v;//it is now the current biggest.
									}
									System.out.print(v+" ");
								}
								System.out.println();
							}
							
							initialize(numberOfVariables,cardsToASet,variables);
							startMenu.dispose();
						}
						else//there is a duplicate
						{
							//scold the user. They cannot select the same variable twice. (cannot select light pink twice, cannot select 'solid' twice)
							g.drawString("You cannot use duplicate values!", width/2-50, 200+cardsToASet*(h*2)+30);
							//System.out.println("Bad user. No duplicates allowed.");
						}
					}
				});
				ready.setBounds(width/2-50,200+cardsToASet*(h*2)+50,100,40);
				startMenu.add(ready);
				startMenu.setVisible(false);
				startMenu.setVisible(true);
			}//end mouseclicked
		});
		
		defaultDeck.setBounds(width/2-200, 100, 150, 32);
		customDeck.setBounds(width/2+50, 100, 150, 32);
		startMenu.add(defaultDeck);
		startMenu.add(customDeck);
		
		vars.setBounds(width/2-175, 50, 150, 32);
		startMenu.add(vars);
		
		size.setBounds(width/2+25, 50, 150, 32);
		startMenu.add(size);
		
		startMenu.setVisible(false);
		startMenu.setVisible(true);
	}
	
	private void initialize(int variables, int setSize, int[][] v)
	{
		points = 0;
		numberOfCardsOnTheTable = 0;
		numberOfSelectedCards = 0;
		
		//these are requested from the user in start(), which passes them in to here.
		numberOfVariables = variables;
		cardsToASet = setSize;
		
		int borderWidth = (width - gameContentPane.getWidth())/2; //this is identical to the calcualtion above in the constructor
		firstCardX = borderWidth+20;
		firstCardY = borderWidth+80;
		
		int height_width_ratio = 2;
		int border_proportion = 5; //this determines the space between cards based on the width of the cards. The higher the number, the smaller the distance.
		//TODO choose rows and columns based on the size of the deck. (rows*columns MUST be greater than the maximum number of cards that can be on the table w/out a set)
		int min = 20; // TODO this is the variable that needs to be modified based on the deck size.
		int cols = 0;
		columns = 0;
		
		do
		{
			columns++;
			cols = columns+((cardsToASet+2)/2); //columns of full-sized cards + setSize+1 columns of half-sized cards
			rows = (int) height*cols/width/height_width_ratio; //using the ratio between cardWidth and cardHeight and the number of columns that are on the table, calculate how many rows will fit
		
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
	        		int x = firstCardX + (cardWidth+cardBufferX)*(columns) + (cardWidth+cardBufferX)/2*( (cardsToASet-(c%cardsToASet)) + (c/(cardsToASet*rows*2))*(cardsToASet+1) ) ; // starting position + combined width of all the dealt cards + width of one minicard *( (how many will be in this row - how many are already in this row /*because I am painting them in reverse order but wish them to be drawn across the row in the order they were selected*/) + (how many rows are before this row)*(how many cards are in each of those rows + 1 (as a buffer space))
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
			int[] v = new int[22]; // 7 is the numberOfShapes available, which is currently our variable with the most options
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
				//clear the table
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
			//scold, maybe deduct points
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
