import java.awt.Color;
import java.awt.Graphics;


public class Card {
  
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
  
  public Card(int numberOfVariables,int numberOfCardstoaSet,int[] variables)
  {
	  initialized = false;
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
  
  public void draw(Graphics g, int x, int y, int width, int height)
  {
	  g.setColor(COLOR_BLACK);
	  g.drawRoundRect(x, width, y, height, 10, 10);//draw border of card
	  
	  int h = (height-20)/variables[NUMBER]-5;
	  int w = width-20;
	  
	  g.setColor(COLOR_ARRAY[variables[COLOR]]);
	  for(int i =0; i<variables[NUMBER];i++)
	  {
		  
	  }
	  
	  
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
