package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.draw2d.geometry.*;

public abstract class Graphics {

static final protected class TransparencyFlag{
	boolean val;
	protected TransparencyFlag(boolean b){
		val = b;
	}
	public boolean toBoolean(){return val;}
};

/**@deprecated use (draw/fill)(String/Text) methods instead*/
public static TransparencyFlag
	TRANSPARENT = new TransparencyFlag(true);
/**@deprecated use (draw/fill)(String/Text) methods instead*/
public static TransparencyFlag
	NON_TRANSPARENT = new TransparencyFlag(false);

public static final int
	LINE_SOLID = SWT.LINE_SOLID,
	LINE_DASH  = SWT.LINE_DASH,
	LINE_DASHDOT = SWT.LINE_DASHDOT,
	LINE_DASHDOTDOT = SWT.LINE_DASHDOTDOT,
	LINE_DOT	= SWT.LINE_DOT;

public abstract void clipRect(Rectangle r);
public abstract void drawArc(Rectangle r, int offset, int length);
public abstract void fillArc(Rectangle r, int offset, int length);

public abstract void drawFocus(Rectangle r);
public abstract void drawFocus(int x, int y, int w, int h);

public abstract void drawImage(Image srcImage, Point p);
public abstract void drawImage(Image srcImage, int x, int y);
public abstract void drawImage(Image srcImage, Rectangle src, Rectangle dest);
public abstract void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2);

public abstract void drawLine(Point p1, Point p2);
public abstract void drawLine(int x1, int y1, int x2, int y2);

public abstract void drawOval(Rectangle r);
public abstract void fillOval(Rectangle r);

public abstract void drawPolygon(PointList points);
public abstract void fillPolygon(PointList points);

public abstract void drawPolyline(PointList points);

public abstract void drawRectangle(Rectangle r);
public abstract void drawRectangle(int x1, int x2, int width, int height);
public abstract void fillRectangle(Rectangle r);
public abstract void fillRectangle(int x1, int x2, int width, int height);

public abstract void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight);
public abstract void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight);

/** 
 * @deprecated use {@link #drawString(String, Point)} and 
 * {@link #fillString(String, Point)}
 */
public abstract void drawString(String s, Point p, TransparencyFlag transparent);

/** 
 * @deprecated use {@link #drawText(String, Point)} and 
 * {@link #fillText(String, Point)}
 */
public abstract void drawText(String s, Point p, TransparencyFlag transparent);

/** 
 * @deprecated use {@link #drawString(String, int, int)} and 
 * {@link #fillString(String, int, int)}
 */
public abstract void drawString(String s, int x, int y, TransparencyFlag transparent);

/** 
 * @deprecated use {@link #drawText(String, int, int)} and 
 * {@link #fillText(String, int, int)}
 */
public abstract void drawText(String s, int x, int y, TransparencyFlag transparent);

public void drawText(String s, int x, int y){
	drawText(s, x, y, TRANSPARENT);
}

public void fillText(String s, int x, int y){
	drawText(s, x, y, NON_TRANSPARENT);
}

public void drawString(String s, int x, int y){
	drawText(s, x, y, TRANSPARENT);
}

public void fillString(String s, int x, int y){
	drawString(s, x, y, NON_TRANSPARENT);
}

public void drawText(String s, Point p){
	drawText(s, p.x, p.y, TRANSPARENT);
}

public void fillText(String s, Point p){
	drawText(s, p.x, p.y, NON_TRANSPARENT);
}

public void drawString(String s, Point p){
	drawString(s, p.x, p.y, TRANSPARENT);
}

public void fillString(String s, Point p){
	drawString(s, p.x, p.y, NON_TRANSPARENT);
}

public abstract int getAdvanceWidth(char c);
public abstract Color getBackgroundColor();
public abstract int getCharWidth(char c);
public abstract Rectangle getClip(Rectangle rect);
public abstract Font getFont();
public abstract FontMetrics getFontMetrics();
public abstract Color getForegroundColor();
public abstract int getLineStyle();
public abstract int getLineWidth();
public abstract Dimension getStringExtent(String s);
public abstract Dimension getTextExtent(String s);
public abstract boolean getXORMode();

public abstract void popState();
public abstract void pushState();
public abstract void restoreState();

public abstract void scale(float amount);
public abstract void setBackgroundColor(Color rgb);
public abstract void setClip(Rectangle r);
public abstract void setFont(Font f);
public abstract void setForegroundColor(Color rgb);

public abstract void setLineStyle(int style);
public abstract void setLineWidth(int width);
public abstract void setXORMode(boolean b);
public abstract void translate(Point pt);
public abstract void translate(int x, int y);

}