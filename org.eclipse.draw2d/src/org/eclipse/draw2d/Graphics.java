package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.draw2d.geometry.*;

public abstract class Graphics {

public static final int
	LINE_SOLID = SWT.LINE_SOLID,
	LINE_DASH  = SWT.LINE_DASH,
	LINE_DASHDOT = SWT.LINE_DASHDOT,
	LINE_DASHDOTDOT = SWT.LINE_DASHDOTDOT,
	LINE_DOT	= SWT.LINE_DOT;

public abstract void clipRect(Rectangle r);

public abstract void dispose();

public final void drawArc(Rectangle r, int offset, int length){
	drawArc(r.x, r.y, r.width, r.height, offset, length);
}

public abstract void drawArc(int x, int y, int w, int h, int offset, int length);

public abstract void fillArc(int x, int y, int w, int h, int offset, int length);
public final void fillArc(Rectangle r, int offset, int length){
	fillArc(r.x, r.y, r.width, r.height, offset, length);
}

public final void drawFocus(Rectangle r){
	drawFocus(r.x, r.y, r.width, r.height);
}

public abstract void drawFocus(int x, int y, int w, int h);

public final void drawImage(Image srcImage, Point p){
	drawImage(srcImage, p.x, p.y);
}

public abstract void drawImage(Image srcImage, int x, int y);
public final void drawImage(Image srcImage, Rectangle src, Rectangle dest){
	drawImage(srcImage, src.x,src.y,src.width,src.height,dest.x,dest.y,dest.width,dest.height);
}
public abstract void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2);

public final void drawLine(Point p1, Point p2){
	drawLine(p1.x, p1.y, p2.x, p2.y);
}
public abstract void drawLine(int x1, int y1, int x2, int y2);

public final void drawOval(Rectangle r){
	drawOval(r.x, r.y, r.width, r.height);
}
public abstract void drawOval(int x, int y, int w, int h);

public final void fillOval(Rectangle r){
	fillOval(r.x, r.y, r.width, r.height);
}
public abstract void fillOval(int x, int y, int w, int h);

public abstract void drawPolygon(PointList points);
public abstract void fillPolygon(PointList points);

public abstract void drawPolyline(PointList points);

public final void drawRectangle(Rectangle r){
	drawRectangle(r.x, r.y, r.width, r.height);
}
public abstract void drawRectangle(int x1, int x2, int width, int height);
public final void fillRectangle(Rectangle r){
	fillRectangle(r.x, r.y, r.width, r.height);
}
public abstract void fillRectangle(int x1, int x2, int width, int height);

public abstract void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight);
public abstract void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight);

public abstract void drawText(String s, int x, int y);
public abstract void drawString(String s, int x, int y);

public final void drawString(String s, Point p){
	drawString(s, p.x, p.y);
}

public final void drawText(String s, Point p){
	drawText(s, p.x, p.y);
}

public final void fillString(String s, Point p){
	fillString(s, p.x, p.y);
}

public abstract void fillString(String s, int x, int y);

public final void fillText(String s, Point p){
	fillText(s, p.x, p.y);
}

public abstract void fillText(String s, int x, int y);

public abstract Color getBackgroundColor();
public abstract Rectangle getClip(Rectangle rect);
public abstract Font getFont();
public abstract FontMetrics getFontMetrics();
public abstract Color getForegroundColor();
public abstract int getLineStyle();
public abstract int getLineWidth();
public abstract boolean getXORMode();

public abstract void popState();
public abstract void pushState();
public abstract void restoreState();

public abstract void scale(double amount);
public abstract void setBackgroundColor(Color rgb);
public abstract void setClip(Rectangle r);
public abstract void setFont(Font f);
public abstract void setForegroundColor(Color rgb);

public abstract void setLineStyle(int style);
public abstract void setLineWidth(int width);
public abstract void setXORMode(boolean b);
public final void translate(Point pt){
	translate(pt.x, pt.y);
}
public abstract void translate(int x, int y);

}