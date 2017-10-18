import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JFrame; // for JFrame
import javax.swing.JLabel;
import javax.swing.JPanel; // for JPanel
import javax.swing.border.EmptyBorder;


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
		  variables[i]=0;
	  }
	  variables[NUMBER]=1;
	  selected = false;
	  initialized = true;
  }
  
  public Card(int numberOfVariables,int numberOfCardstoaSet,int[] variables)
  {
	  initialized = false;
	  
	  cardsInASet = numberOfCardstoaSet;
	  
	  if(variables.length!=numberOfVariables)
	  {
		  throw new IllegalArgumentException("You must give an array of variables consistent with the number of variables stated.");
	  }
	  
	  this.variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  if( (variables[i]<cardsInASet || (i==NUMBER&&variables[i]<cardsInASet+1) ) && variables[i]>=0 )
		  {
			  this.variables[i]=variables[i];
		  }
		  else
			  {throw new IllegalArgumentException("variable is out of bounds");}
	  }
	  if(variables[NUMBER]<1)
	  {
		  throw new IllegalArgumentException("A card must have at least one symbol on it.");
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
	  if(!initialized)
	  	{throw new IllegalArgumentException("card is not initialized");}
	  
	  Color currentColor = g.getColor();
	  
	  g.setColor(COLOR_WHITE);
	  g.fillRoundRect(x, y, width, height, 10, 10);//fill card
	  g.setColor(COLOR_BLACK);
	  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
	  
	  int numberOfStripes = 10;
	  int yBuffer = 10;
	  int xBuffer = 10;
	  
	  int h = (height-(2*yBuffer))/variables[NUMBER]-yBuffer/2;
	  h -= h%numberOfStripes;
	  int w = width-2*xBuffer;
	  w -= w%numberOfStripes;
	  
	  g.setColor(COLOR_ARRAY[variables[COLOR]]);
	  for(int i =0; i<variables[NUMBER];i++)
	  {
		  if(variables[SHAPE]==SQUARE)
		  {
			  if(variables[FILL]==STRIPED)
			  {
				  int xDistance = w/(numberOfStripes/2);
				  int yDistance = h/(numberOfStripes/2);
				  for(int j = 1; j<=numberOfStripes/2; j++)
				  {
					  g.drawLine(x+xBuffer+j*xDistance , y+yBuffer+(yBuffer/2+h)*i , x+xBuffer , y+yBuffer+(yBuffer/2+h)*i+j*yDistance);
					  g.drawLine(x+xBuffer+j*xDistance , y+yBuffer+(yBuffer/2+h)*i+h , x+xBuffer+w , y+yBuffer+(yBuffer/2+h)*i+j*yDistance);
				  }
			  }
			  if(variables[FILL]==SOLID)
			  {
				  g.fillRect(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
			  }
			  g.drawRect(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
		  }
		  
		  if(variables[SHAPE]==TRIANGLE)
		  {
			  int[] xPoints = {x+xBuffer, x+(width-xBuffer), x+(width/2)};
			  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i};
			  
			  if(variables[FILL]==STRIPED)
			  {
				  
			  }
			  if(variables[FILL]==SOLID)
			  {
				  g.fillPolygon(xPoints, yPoints, 3);
			  }
			  g.drawPolygon(xPoints, yPoints, 3);
		  }
		  
		  if(variables[SHAPE]==CIRCLE)
		  {
			  if(variables[FILL]==STRIPED)
			  {
				  
			  }
			  if(variables[FILL]==SOLID)
			  {
				  g.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
			  }
			  
			  g.drawOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
		  }
		  
	  }
	  
	  
	  
	  g.setColor(currentColor);
  }
  
  
  
  public void select()
  {
	  if(!initialized)
		  {throw new IllegalArgumentException("card is not initialized");}
	  
	  selected = true;
  }
  
  public void deselect()
  {
	  if(!initialized)
		  {throw new IllegalArgumentException("card is not initialized");}
	  
	  selected = false;
  }
  
}
