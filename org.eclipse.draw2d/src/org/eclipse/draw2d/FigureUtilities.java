package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Provides miscellaneous Figure operations.
 */
public class FigureUtilities {

private static class ColorVector{
	final static double RED_INTENSITY = 0.7, BLUE_INTENSITY = 1.3;
	double r,g,b;
	protected ColorVector(Color source){
		r = source.getRed();
		g = source.getGreen();
		b = source.getBlue();
	}
	
	protected double getIntensity(){
		return r*r*RED_INTENSITY + g*g + b*b*BLUE_INTENSITY;
	}

	protected void normalize(double length){
	}
};

private static final float RGB_VALUE_MULTIPLIER = 0.6f;
private static GC gc;
private static Font appliedFont;
private static FontMetrics metrics;
private static Object LOCK = new Object();
private static Color ghostFillColor = new Color(null,31,31,31);

/**
 * Returns a Color the same as the passed color in a darker hue.
 * @since 2.0
 */
public static Color darker(Color color){
	return new Color(null,
		(int)(color.getRed()   * RGB_VALUE_MULTIPLIER),
		(int)(color.getGreen() * RGB_VALUE_MULTIPLIER),
		(int)(color.getBlue()  * RGB_VALUE_MULTIPLIER));
}

/**
 * Returns the FontMetrics associated with the passed Font.
 * 
 * @since 2.0
 */
public static FontMetrics getFontMetrics(Font f){
	setFont(f);
	return metrics;
}

protected static GC getGC(){
	if (gc == null){
		//Synchonize to prevent creating more than one GC
		synchronized(LOCK){
			if (gc == null){
				Display.getDefault().syncExec( new Runnable(){
					public void run(){
						gc = new GC(new Shell());
					}
				});
			}
		}
	}
	return gc;
}

protected static org.eclipse.swt.graphics.Point getTextDimension(String s, Font f){
	setFont(f);
	return getGC().textExtent(s);
}

/**
 * Returns the largest substring of <i>s</i> in Font <i>f</i> that can
 * be confined to the number of pixels in <i>availableWidth<i>.
 * 
 * @since 2.0
 */
static int getLargestSubstringConfinedTo(String s, Font f, int availableWidth){
	FontMetrics metrics = getFontMetrics(f);
	int min, max;
	float avg = metrics.getAverageCharWidth();
	min=0;
	max = s.length()+1;

	//The size of the current guess
	int guess = 0,
	    guessSize = 0;
	while ((max-min)>1){
		//Pick a new guess size
		//	New guess is the last guess plus the missing width in pixels
		//	divided by the average character size in pixels
		guess = guess + (int)((availableWidth-guessSize)/avg);

		if (guess >= max) guess = max-1;
		if (guess <= min) guess = min+1;

		//Measure the current guess
		guessSize = getTextExtents(s.substring(0,guess),f).width;

		if (guessSize < availableWidth)
			//We did not use the available width
			min = guess;
		else
			//We exceeded the available width
			max = guess;
	}
	return min;
}

/**
 * Returns the Dimensions of <i>s</i> in Font <i>f</i>.
 * 
 * @since 2.0
 */
public static Dimension getTextExtents(String s, Font f){
	return new Dimension(getTextDimension(s,f));
}

/**
 * Returns the width of <i>s</i> in Font <i>f</i>.
 * 
 * @since 2.0
 */
public static int getTextWidth(String s, Font f){
	return getTextDimension(s,f).x;
}

/**
 * Returns a Color the same as the passed color in a lighter hue.
 * @since 2.0
 */
public static Color lighter(Color rgb){
	int r = rgb.getRed(),
	    g = rgb.getGreen(),
	    b = rgb.getBlue();

	return new Color(null,
		Math.max(2,Math.min((int)(r/RGB_VALUE_MULTIPLIER), 255)),
		Math.max(2,Math.min((int)(g/RGB_VALUE_MULTIPLIER), 255)),
		Math.max(2,Math.min((int)(b/RGB_VALUE_MULTIPLIER), 255))
	);
}

/**
 * Produces a ghosting effect on <i>s</i>/
 * 
 * @since 2.0
 */
public static Shape makeGhostShape(Shape s){
	s.setBackgroundColor(ghostFillColor);
	s.setFillXOR(true);
	return s;
}

/**
 * Mixes the passed Colors and returns the resulting 
 * Color.
 * 
 * @since 2.0
 */
public static Color mixColors(Color c1, Color c2){
	return new Color(null,
		(c1.getRed()+c2.getRed())/2,
		(c1.getGreen()+c2.getGreen())/2,
		(c1.getBlue()+c2.getBlue())/2);
}

/**
 * Paints a border with an etching effect,
 * having a shadow of Color <i>shadow</i> and
 * highlight of Color <i>highlight</i>.
 * 
 * @since 2.0
 */
public static void paintEtchedBorder(
	Graphics g,
	Rectangle r,
	Color shadow,
	Color highlight)
{
	int   x = r.x,
		y = r.y,
		w = r.width,
		h = r.height;

	g.setLineStyle(Graphics.LINE_SOLID);
	g.setLineWidth(1);
	g.setXORMode(false);

   	w -= 2;
	h -= 2;

	g.setForegroundColor(shadow);
	g.drawRectangle(x,y,w,h);

	x++;y++;
	g.setForegroundColor(highlight);
	g.drawRectangle(x,y,w,h);
}

/**
 * Paints a border with an etching effect,
 * having a shadow of a darker version of g's background color,
 * and a highlight a lighter version of g's background color.
 * 
 * @since 2.0
 */
public static void paintEtchedBorder(Graphics g, Rectangle r){
	Color rgb = g.getBackgroundColor(),
	    shadow = darker(rgb),
	    highlight = lighter(rgb);
	paintEtchedBorder(g,r,shadow, highlight);
}

/**
 * Sets Font to passed value.
 * 
 * @since 2.0
 */
protected static void setFont(Font f){
	if (appliedFont == f || f.equals(appliedFont))
		return;
	getGC().setFont(f);
	appliedFont = f;
	metrics = gc.getFontMetrics();
}

}

