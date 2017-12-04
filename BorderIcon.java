import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;

import javax.swing.Icon;

public class BorderIcon implements Icon
{
	private int iWidth;
	private int iHeight;
	private Color  color;
	  private Insets insets;
	
	public BorderIcon(int width, int height, Color c)
	{
		iWidth  = width;
	    iHeight = height;
	    color   = c;
	    insets  = new Insets(1,1,1,1);
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
		g.setColor(color);
	    g.drawRect(x,y,iWidth-1, iHeight-2);
	    int w = iWidth  - insets.left - insets.right;
	    int h = iHeight - insets.top  - insets.bottom -1;
	    g.drawRect(x,y, w,h);
	}
}
	
