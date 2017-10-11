import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame; // for JFrame
import javax.swing.JLabel;
import javax.swing.JPanel; // for JPanel
import javax.swing.border.EmptyBorder;


public class Card {
	
	protected JFrame cardJFrame;
    protected JPanel cardJPanel;
  
  int cardsInASet;
  int[] variables;
  boolean selected;
  boolean initialized;
  
  //define variables
  final int NUMBER = 0;
  final int COLOR = 1;
  final int SHAPE = 2;
  final int FILL = 3;
  
  //color
  final int RED = 0;
  final int BLUE = 1;
  final int YELLOW = 2;
  //shape
  final int SQUARE = 0;
  final int TRIANGLE = 1;
  final int CIRCLE = 2;
  //fill
  final int OUTLINE = 0;
  final int STRIPED = 1;
  final int SOLID = 2;
  
  
	public static final Color COLOR_RED = new Color(255,0,0);
	public static final Color COLOR_BLUE = new Color(0,0,255);
	public static final Color COLOR_YELLOW = new Color(255,255,0);
	public static final Color COLOR_BLACK = new Color(0,0,0);
	public static final Color COLOR_GREEN = new Color(0,255,0);
	public static final Color COLOR_WHITE = new Color(255,255,255);
	public static final Color[] COLOR_ARRAY = {COLOR_RED, COLOR_BLUE, COLOR_YELLOW};
  
  public Card(JFrame passedInJFrame)
  {
	  initialized = false;
	  
	  cardJFrame = passedInJFrame;
	  cardJPanel = new JPanel();

	  cardJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	  cardJPanel.setLayout(null);
		
	  cardsInASet = 3;
	  int numberOfVariables = 4;
	  variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  variables[i]=1;
	  }
	  selected = false;
	  initialized = true;
  }
  
  public Card()
  {
	  initialized = false;
		
	  cardsInASet = 3;
	  int numberOfVariables = 4;
	  variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  variables[i]=1;
	  }
	  selected = false;
	  initialized = true;
  }
  
  public Card(JFrame passedInJFrame, int numberOfVariables,int numberOfCardstoaSet,int[] variables)
  {
	  initialized = false;
	  
	  cardJFrame = passedInJFrame;
	  cardJPanel = new JPanel();

	  cardJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	  cardJPanel.setLayout(null);
	  
	  cardsInASet = numberOfCardstoaSet;
	  this.variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  if(variables[i]<cardsInASet)
		  {
			  this.variables[i]=variables[i];
		  }
		  else
			  throw new IllegalArgumentException("variable is out of bounds");
	  }
	  selected = false;
	  initialized = true;
  }
  
  public boolean isSelected()
  {
	  if(initialized)
	  {
		  return selected;
	  }
	  else
		  throw new IllegalArgumentException("card is not initialized");
  }
  
  public int getVariable(int i)
  {
	  if(initialized)
		  {return variables[i];}
	  else
		  {throw new IllegalArgumentException("card is not initialized");}
	  
  }
  
  public void draw( int x, int y, int width, int height)
  {
	  if(!initialized)
	  	{throw new IllegalArgumentException("card is not initialized");}
	  
	  
	  Graphics g = cardJPanel.getGraphics();
	  Color currentColor = g.getColor();
	  g.setColor(COLOR_BLACK);
	  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
	  
	  int h = (height-20)/variables[NUMBER]-5;
	  int w = width-20;
	  
	  g.setColor(COLOR_ARRAY[variables[COLOR]]);
	  for(int i =0; i<variables[NUMBER];i++)
	  {
		  
	  }
	  
	  
	  
	  g.setColor(currentColor);
  }
  
  public void draw(Graphics g, int x, int y, int width, int height)
  {
	  if(!initialized)
	  	{throw new IllegalArgumentException("card is not initialized");}
	  
	  Color currentColor = g.getColor();
	  g.setColor(COLOR_BLACK);
	  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
	  
	  int h = (height-20)/variables[NUMBER]-5;
	  int w = width-20;
	  
	  g.setColor(COLOR_ARRAY[variables[COLOR]]);
	  for(int i =0; i<variables[NUMBER];i++)
	  {
		  
	  }
	  
	  
	  
	  g.setColor(currentColor);
  }
  
  
  
  public void selectOrDeselect()
  {
	  if(!initialized)
		  {throw new IllegalArgumentException("card is not initialized");}
	  if(selected)
		  {selected = false;}
	  else
		  {selected = true;}
  }
  
  
  
  
  
}
