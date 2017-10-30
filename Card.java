import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

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
  final int GREEN = 0;
  final int BLUE = 1;
  final int YELLOW = 2;
  final int PURPLE = 3;
  final int RED = 4;
  final int ORANGE = 5;
  //shape
  public static final int MOUNTAINS = 0;
  public static final int DIAMOND = 1;
  public static final int HOURGLASS = 2;
  public static final int BOWTIE = 3;
  public static final int TRIANGLE = 4;
  public static final int SQUARE = 5;
  public static final int CIRCLE = 6;
  final int STAR = 20;
  final int LIGHTNING_BOLT = 21;
  //fill
  public static final int OUTLINE = 2;
  public static final int STRIPED = 3;
  public static final int SOLID = 1;
  public static final int GRADIENT = 0;
  public static final int DOUBLE_OUTLINE = 4;
  
  
	final Color red = new Color(255,0,0);
	final Color orange = new Color(255,140,0);
	final Color yellow = new Color(204,204,0);
	final Color green = new Color(0,255,0);
	final Color blue = new Color(0,0,255);
	final Color purple = new Color(128,0,128);
	
	Color[][] colorschemes = {
			{green, blue, yellow, purple, red, orange},
			{red, orange, yellow, green, blue, purple},
			{},
	};
	
  
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
	  
	  g.setColor(colorschemes[0][variables[COLOR]]);
	  
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
					  if(variables[FILL]==GRADIENT)
					  {
						  	GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				            Graphics2D g2d = (Graphics2D) g;
				            g2d.setPaint(gp);
				            g2d.fillRect(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
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
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 3);
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
							  g.drawOval(x+xBuffer+xDistance*j, y+yBuffer+(yBuffer/2+h)*i+yDistance*j, w-xDistance*j*2, h-yDistance*j*2);
						  }
					  }
					  if(variables[FILL]==SOLID)
					  {
						  g.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  	GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
						  	Graphics2D g2d = (Graphics2D) g;
				            g2d.setPaint(gp);
				            g2d.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);
					  }
				  }
				  else //fill is not a variable
				  {
					  g.fillOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);//solid fill
				  }
				  g.drawOval(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, w, h);//outline
			  }//end circle
			  
			  
			  if(variables[SHAPE]==HOURGLASS)
			  {
//				  int[] xPoints = {x+xBuffer, x+xBuffer+w*2/3, x+xBuffer+w*1/3, x+xBuffer+w};
//				  int[] yPoints = {y+yBuffer+h*1/3+(h+yBuffer)*i, y+yBuffer+(yBuffer+h)*i, y+yBuffer+h+(h+yBuffer)*i, y+yBuffer+h*2/3+(h+yBuffer)*i };
				  
				  int[] xPoints = {x+xBuffer, x+xBuffer+w , x+xBuffer, x+xBuffer+w };
				  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i , y+yBuffer+(yBuffer/2+h)*i , y+yBuffer+h+(yBuffer/2+h)*i , y+yBuffer+h+(yBuffer/2+h)*i};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  int xDistance = w/(numberOfStripes/2);
						  for(int j = 1; j<=numberOfStripes/2; j++)
						  {
							  g.drawLine(xPoints[0]+j*xDistance, yPoints[0], (xPoints[2]+xPoints[3])/2, (yPoints[1]+yPoints[2])/2);
							  g.drawLine(xPoints[0]+j*xDistance, yPoints[3], (xPoints[2]+xPoints[3])/2, (yPoints[1]+yPoints[2])/2);
						  }
					  }
					  
					  if(variables[FILL]==SOLID)
					  {
						g.fillPolygon(xPoints, yPoints, 4);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 4);
					  }
				  }
				  else // fill is not a variable
				  {
					  g.fillPolygon(xPoints, yPoints, 4);
				  }
				  g.drawPolygon(xPoints, yPoints, 4);
			  }//end hourglass
			  
			  if(variables[SHAPE]==BOWTIE)
			  {
				  int[] xPoints = {x+xBuffer, x+xBuffer, x+xBuffer+w , x+xBuffer+w };
				  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i , y+yBuffer+h+(yBuffer/2+h)*i , y+yBuffer+(yBuffer/2+h)*i , y+yBuffer+h+(yBuffer/2+h)*i};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  int yDistance = h/(numberOfStripes/2);
						  for(int j = 1; j<=numberOfStripes/2; j++)
						  {
							  g.drawLine( xPoints[0], yPoints[0]+j*yDistance, (xPoints[1]+xPoints[2])/2, (yPoints[2]+yPoints[3])/2);
							  g.drawLine( xPoints[3], yPoints[0]+j*yDistance, (xPoints[1]+xPoints[2])/2, (yPoints[2]+yPoints[3])/2);
						  }
					  }
					  
					  if(variables[FILL]==SOLID)
					  {
						g.fillPolygon(xPoints, yPoints, 4);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 4);
					  }
				  }
				  else // fill is not a variable
				  {
					  g.fillPolygon(xPoints, yPoints, 4);
				  }
				  g.drawPolygon(xPoints, yPoints, 4);
			  }// end bowtie
			  
			  if(variables[SHAPE]==DIAMOND)
			  {
				  int[] xPoints = {x+width/2 , x+xBuffer , x+width/2 , x+width-xBuffer };
				  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i, y+yBuffer+h/2+(yBuffer/2+h)*i, y+yBuffer+h+(yBuffer/2+h)*i,y+yBuffer+h/2+(yBuffer/2+h)*i};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  int yDistance = h/(numberOfStripes/2);
						  int xDistance = w/(numberOfStripes/2);
						  for(int j = 1; j<=numberOfStripes/2; j++)
						  {
							  g.drawLine(xPoints[1],yPoints[1],xPoints[0],yPoints[0]+yDistance*j);
							  g.drawLine(xPoints[3],yPoints[3],xPoints[0],yPoints[0]+yDistance*j);
							  g.drawLine(xPoints[0],yPoints[0],xPoints[1]+xDistance*j,yPoints[1]);
							  g.drawLine(xPoints[2],yPoints[2],xPoints[1]+xDistance*j,yPoints[1]);
						  }
					  }
					  
					  if(variables[FILL]==SOLID)
					  {
						g.fillPolygon(xPoints, yPoints, 4);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 4);
					  }
				  }
				  else // fill is not a variable
				  {
						g.fillPolygon(xPoints, yPoints, 4);
				  }
				  g.drawPolygon(xPoints, yPoints, 4);
			  }//end diamond
			  
			  if(variables[SHAPE] == MOUNTAINS)
			  {
				  int[][]xPoints = {
						  {x+xBuffer,x+xBuffer+w/5*3,(x+xBuffer+x+xBuffer+w/5*3)/2},
						  {x+xBuffer+w/5*2,x+xBuffer+w,(x+xBuffer+w/5*2+x+xBuffer+w)/2},
						  {x+xBuffer+w/5*1,x+xBuffer+w/5*4,(x+xBuffer+w/5*1+x+xBuffer+w/5*4)/2}};
				  int[][]yPoints = {
						  {y+yBuffer+(yBuffer/2+h)*i+h/7*6 , y+yBuffer+(yBuffer/2+h)*i+h/7*6 , y+yBuffer+(yBuffer/2+h)*i},
						  {y+yBuffer+(yBuffer/2+h)*i+h/7*5 , y+yBuffer+(yBuffer/2+h)*i+h/7*5 , y+yBuffer+(yBuffer/2+h)*i+h/7*2},
						  {y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i+h/7}};
				  
				  Color temp = g.getColor();
				  Color background = (selected?Color.black:Color.WHITE);
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==OUTLINE)
					  {
						  for(int triangle = 0; triangle < 3; triangle++)
						  {
							  g.drawPolygon(xPoints[triangle], yPoints[triangle], 3);
						  }
						  g.setColor(background);
						  g.fillPolygon(xPoints[2], yPoints[2], 3);
						  g.setColor(temp);
						  g.drawPolygon(xPoints[2], yPoints[2], 3);
					  }
					  if(variables[FILL]==STRIPED)
					  {
						  for(int triangle = 0; triangle < 3; triangle++)
						  {
							  int xDistance = (xPoints[triangle][1]-xPoints[triangle][0])/(numberOfStripes/2);
							  if (triangle == 2)
							  {
								  g.setColor(background);
								  g.fillPolygon(xPoints[2], yPoints[2], 3);
								  g.setColor(temp);
							  }
							  for(int j = 1; j<=numberOfStripes/2; j++)
							  {
								  g.drawLine(xPoints[triangle][0]+j*xDistance, yPoints[triangle][0], xPoints[triangle][2], yPoints[triangle][2]);
							  }
							  g.drawPolygon(xPoints[triangle], yPoints[triangle], 3);
						  }
					  }
					  
					  if(variables[FILL]==SOLID)
					  {	
						  for(int triangle = 0; triangle < 3; triangle++)
						  {
							  g.fillPolygon(xPoints[triangle], yPoints[triangle], 3);
							  g.setColor(background);
							  g.drawPolygon(xPoints[triangle], yPoints[triangle], 3);
							  g.setColor(temp);
						  }
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          for(int triangle = 0; triangle < 3; triangle++)
						  {
							  g2d.fillPolygon(xPoints[triangle], yPoints[triangle], 3);
							  g.setColor(background);
							  g.drawPolygon(xPoints[triangle], yPoints[triangle], 3);
							  g2d.setPaint(gp);
						  }
				          g.setColor(temp);
					  }
				  }
				  else // fill is not a variable
				  {
					  for(int triangle = 0; triangle < 3; triangle++)
					  {
						  g.fillPolygon(xPoints[triangle], yPoints[triangle], 3);
						  g.setColor(background);
						  g.drawPolygon(xPoints[triangle], yPoints[triangle], 3);
						  g.setColor(temp);
					  }
				  }
			  }// end mountains
			  
			  if(variables[SHAPE]==STAR)
			  {
				  int[] xPoints = {x+xBuffer*2 , x+(width/2) , x+(width-xBuffer*2) , x+xBuffer , x+(width-xBuffer)};
				  int[] yPoints = {y+yBuffer+(yBuffer/2+h)*i+h  , y+yBuffer+(yBuffer/2+h)*i , y+yBuffer+(yBuffer/2+h)*i+h , y+yBuffer+(yBuffer/2+h)*i+h/3 , y+yBuffer+(yBuffer/2+h)*i+h/3};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  //TODO make stripes in the star
						  
					  }
					  
					  if(variables[FILL]==SOLID)
					  {
						g.fillPolygon(xPoints, yPoints, 5);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 5);
					  }
				  }
				  else // fill is not a variable
				  {
						g.fillPolygon(xPoints, yPoints, 5);
				  }
				  g.drawPolygon(xPoints, yPoints, 5);
			  }//end star
			  
			  if(variables[SHAPE]==LIGHTNING_BOLT)
			  {
				  int[] xPoints = {x+xBuffer+w/5, x+xBuffer+w*2/5, x+xBuffer, x+xBuffer+w*4/5, x+xBuffer+w*3/5, x+xBuffer+w};
				  int[] yPoints = {y+h+(yBuffer/2+h)*i , y+h*4/7+(yBuffer/2+h)*i , y+h*5/9+(yBuffer/2+h)*i , y+(yBuffer/2+h)*i , y+h*3/7+(yBuffer/2+h)*i , y+h*4/9+(yBuffer/2+h)*i , y+h+(yBuffer/2+h)*i};
				  
				  if(variables.length>FILL)//if the variables includes the fill
				  {
					  if(variables[FILL]==STRIPED)
					  {
						  //TODO make stripes in the lightningbolt
					  }
					  
					  if(variables[FILL]==SOLID)
					  {
						g.fillPolygon(xPoints, yPoints, 6);
					  }
					  
					  if(variables[FILL]==GRADIENT)
					  {
						  GradientPaint gp = new GradientPaint(x+xBuffer, y+yBuffer+(yBuffer/2+h)*i, g.getColor(), x+xBuffer+w, y+yBuffer+(yBuffer/2+h)*i+h, Color.white) ; //(x,y,Color,x2,y2,Color)
				          Graphics2D g2d = (Graphics2D) g;
				          g2d.setPaint(gp);
				          g2d.fillPolygon(xPoints, yPoints, 6);
					  }
				  }
				  else // fill is not a variable
				  {
					  g.fillPolygon(xPoints, yPoints, 6);
				  }
				  g.drawPolygon(xPoints, yPoints, 6);
			  }//end lightningbolt
			  
		  }//end for loop
	  }
	  else //shape is not a variable
	  {
		  g.fillRoundRect(x+10, y+10, width-20, height-20, 10, 10);//fill card with color
		  
		  
	  }
	  if(variables.length>NUMBER)
	  {
		  if(selected)
		  {g.setColor(Color.WHITE);}
		  else
		  { g.setColor(Color.BLACK);}
		  g.setFont(new Font(null, Font.CENTER_BASELINE, 20) );
		  g.drawString(""+variables[NUMBER], x+10, y+30);
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
