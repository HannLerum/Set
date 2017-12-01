import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;

public class ShapeIcon implements Icon
{
	private int iWidth;
	private int iHeight;
	private int shape;
	
	public ShapeIcon(int width, int height, int shape)
	{
		iWidth  = width;
	    iHeight = height;
	    this.shape = shape;
	}
	  
	public int getIconWidth()
	{
		return iWidth;
	}

	public int getIconHeight()
	{
	    return iHeight;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(Color.black);
	    Card.drawshape(g, shape, x, y, iWidth, iHeight);
	}
}
	
