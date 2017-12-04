import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;

public class FillIcon implements Icon
{
	private int iWidth;
	private int iHeight;
	private int iconFill;
	
	public FillIcon(int width, int height, int fill)
	{
	
		iWidth  = width;
	    iHeight = height;
	    iconFill = fill;
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
	    Card.drawfill(g, iconFill, x, y, iWidth, iHeight);
	}
}
	
