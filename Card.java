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
  public static final int COLOR = 0;
  public static final int NUMBER = 1;
  public static final int SHAPE = 2;
  public static final int FILL = 3;
  
  //color
  public static final int GREEN = 0;
  public static final int BLUE = 1;
  public static final int YELLOW = 2;
  //shape
  public static final int SQUARE = 0;
  public static final int TRIANGLE = 1;
  public static final int CIRCLE = 2;
  //fill
  public static final int OUTLINE = 0;
  public static final int STRIPED = 1;
  public static final int SOLID = 2;
  
  
	final Color COLOR_RED = new Color(255,0,0);
	final Color COLOR_BLUE = new Color(0,0,255);
	final Color COLOR_YELLOW = new Color(204,204,0);
	final Color COLOR_BLACK = new Color(0,0,0);
	final Color COLOR_GREEN = new Color(0,255,0);
	final Color COLOR_WHITE = new Color(255,255,255);
	
	final Color[] COLOR_ARRAY = {COLOR_GREEN, COLOR_BLUE, COLOR_YELLOW};
  
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
	  if(variables.length>NUMBER)
	  {
		  if(variables[NUMBER]<1)
		  {
			  throw new IllegalArgumentException("Number of symbols must be at least 1");
		  }
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
	  
	  /*g.setColor(Color.white);
	  g.fillRoundRect(x, y, width, height, 10, 10);//fill card
	  if(!selected)
		  {g.setColor(Color.black);}
	  else
	  	{g.setColor(Color.red);}
	  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
*/	  
	  
	  if(!selected)
	  {
		  g.setColor(Color.white);
		  g.fillRoundRect(x, y, width, height, 10, 10);//fill card
		  g.setColor(Color.black);
		  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
	  }
	  else
	  {
		  g.setColor(Color.black);
		  g.fillRoundRect(x, y, width, height, 10, 10);//fill card
		  g.setColor(Color.white);
		  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
	  }
	  
	  g.setColor(COLOR_ARRAY[variables[COLOR]]);
	  
	  if(variables.length>SHAPE)//if the variables includes shape
	  {
		  int numberOfStripes = 20;
		  int yBuffer = 10;
		  int xBuffer = 10;
		  
		  int h = (height-(2*yBuffer))/variables[NUMBER]-yBuffer/2;
		  int w = width-2*xBuffer;
		  
		  for(int i =0; i<variables[NUMBER];i++)
		  {
			  if(variables[SHAPE]==SQUARE)
			  {
				  if(variables.length>FILL)//if the variables includes the fill
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
				  }
				  else // fill is not a variable
				  {
					  g.fillRect(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h); //solid fill
				  }
				  g.drawRect(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);//outline
			  }//end square
			  
			  if(variables[SHAPE]==TRIANGLE)
			  {
				  int[] xPoints = {x+xBuffer, x+(width-xBuffer), x+(width/2)};
				  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  int xDistance = w/(numberOfStripes/2);
						  for(int j = 1; j<=numberOfStripes/2; j++)
						  {
							  g.drawLine(xPoints[0]+j*xDistance, yPoints[0], xPoints[2], yPoints[2]);
						  }
					  }
					  if(variables[FILL]==SOLID)
					  {
						  g.fillPolygon(xPoints, yPoints, 3);
					  }
				  }
				  else //fill is not a variable
				  {
					  g.fillPolygon(xPoints, yPoints, 3);//solid fill
				  }
				  g.drawPolygon(xPoints, yPoints, 3);//outline
			  }//end triangle
			  
			  if(variables[SHAPE]==CIRCLE)
			  {
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  int xDistance = w/(numberOfStripes);
						  int yDistance = h/(numberOfStripes);
						  for(int j = 1; j<=numberOfStripes; j++)
						  {
							  g.drawOval(x+xBuffer+xDistance*j, y+yBuffer+(yBuffer/2+h)*i+yDistance*j, w-xDistance*j*2, h-yDistance*j*2);//outline
						  }
					  }
					  if(variables[FILL]==SOLID)
					  {
						  g.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
					  }
				  }
				  else //fill is not a variable
				  {
					  g.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);//solid fill
				  }
				  g.drawOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);//outline
			  }//end circle
		  }//end for loop
	  }
	  else //shape is not a variable
	  {
		  g.fillRoundRect(x, y, width, height, 10, 10);//fill card with color
		  if(!selected)
			  {g.setColor(Color.black);}
		  else
		  	{g.setColor(Color.red);}
		  g.drawRoundRect(x, y, width, height, 10, 10);//draw border of card
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
  
  public void switchSelectedOrDeselected()
  {
	  if(!initialized)
	  {throw new IllegalArgumentException("card is not initialized");}
	  
	  if(selected)
		  {selected = false;}
	  else
		  {selected = true;}
  }
  
  public String toString()
  {
	  if(!initialized)
	  {throw new IllegalArgumentException("card is not initialized");}
	  
	  String s = "";
	  String color = "";
	  String shape = "";
	  String fill = "";
	  
	  if(variables[COLOR]==GREEN)
	  {
		  color = "green";
	  }
	  if(variables[COLOR]==BLUE)
	  {
		  color = "blue";
	  }
	  if(variables[COLOR]==YELLOW)
	  {
		  color = "yellow";
	  }
	  
	  if(variables.length>NUMBER)
	  {
		int number = variables[NUMBER];
		if(variables.length>SHAPE)
		{
			if(variables[SHAPE]==SQUARE)
			{
				shape = "square";
			}
			if(variables[SHAPE]==CIRCLE)
			{
				shape = "circle";
			}
			if(variables[SHAPE]==TRIANGLE)
			{
				shape = "triangle";
			}
			
			if(variables.length>FILL)
			{
				if(variables[FILL]==OUTLINE)
				{
					fill = "outline";
				}
				if(variables[FILL]==STRIPED)
				{
					fill = "striped";
				}
				if(variables[FILL]==SOLID)
				{
					fill = "solid";
				}
				
				s = (number + " "+color+" "+fill+" "+shape+(number>1?"s":""));
			}
			else//color, number, and shape, but no fill
			{
				s = (number + " "+color+" "+shape+(number>1?"s":""));
			}
		}
		else //color and number, but no shape (there is currently no reason for this though...)
		{
			s = (number+" "+color+(number>1?"s":""));
		}
	  }
	  else // no variables but the color
	  {
		  s = (color);
	  }
	  
	  return s;
  }
  
}
