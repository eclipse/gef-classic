/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides miscellaneous Figure operations.
 */
public class FigureUtilities {

private static final float RGB_VALUE_MULTIPLIER = 0.6f;
private static GC gc;
private static Font appliedFont;
private static FontMetrics metrics;
private static Color ghostFillColor = new Color(null, 31, 31, 31);

/**
 * Returns a new Color the same as the passed color in a darker hue.
 * 
 * @param color the color to darken
 * @return the darkened color
 * @since 2.0
 */
public static Color darker(Color color) {
	return new Color(null,
		(int)(color.getRed()   * RGB_VALUE_MULTIPLIER),
		(int)(color.getGreen() * RGB_VALUE_MULTIPLIER),
		(int)(color.getBlue()  * RGB_VALUE_MULTIPLIER));
}

/**
 * Returns the FontMetrics associated with the passed Font.
 * 
 * @param f the font
 * @return the FontMetrics for the given font
 * @see GC#getFontMetrics()
 * @since 2.0
 */
public static FontMetrics getFontMetrics(Font f) {
	setFont(f);
	if (metrics == null)
		metrics = getGC().getFontMetrics();
	return metrics;
}

/**
 * Returns the GC used for various utilities.
 * @return the GC
 */
protected static GC getGC() {
	if (gc == null) {
		gc = new GC(new Shell());
		appliedFont = gc.getFont();
	}
	return gc;
}

/**
 * Returns the dimensions of the String <i>s</i> using the font <i>f</i>.  Tab expansion 
 * and carriage return processing are performed.
 * @param s the string
 * @param f the font
 * @return the text's dimensions
 * @see GC#textExtent(String)
 */
protected static org.eclipse.swt.graphics.Point getTextDimension(String s, Font f) {
	setFont(f);
	return getGC().textExtent(s);
}

/**
 * Returns the highest ancestor for the given figure
 * @since 3.0
 * @param figure a figure
 * @return the root ancestor
 */
public static IFigure getRoot(IFigure figure) {
	while (figure.getParent() != null)
		figure = figure.getParent();
	return figure;
}

/**
 * Returns the dimensions of the String <i>s</i> using the font <i>f</i>. No tab
 * expansion or carriage return processing will be performed.
 * @param s the string
 * @param f the font
 * @return the string's dimensions
 * @see GC#stringExtent(java.lang.String)
 */
protected static org.eclipse.swt.graphics.Point getStringDimension(String s, Font f) {
	setFont(f);
	return getGC().stringExtent(s);
}

/**
 * Returns the largest substring of <i>s</i> in Font <i>f</i> that can be confined to the 
 * number of pixels in <i>availableWidth<i>.
 * 
 * @param s the original string
 * @param f the font
 * @param availableWidth the available width
 * @return the largest substring that fits in the given width
 * @since 2.0
 */
static int getLargestSubstringConfinedTo(String s, Font f, int availableWidth) {
	FontMetrics metrics = getFontMetrics(f);
	int min, max;
	float avg = metrics.getAverageCharWidth();
	min = 0;
	max = s.length() + 1;

	//The size of the current guess
	int guess = 0,
	    guessSize = 0;
	while ((max - min) > 1) {
		//Pick a new guess size
		//	New guess is the last guess plus the missing width in pixels
		//	divided by the average character size in pixels
		guess = guess + (int)((availableWidth - guessSize) / avg);

		if (guess >= max) guess = max - 1;
		if (guess <= min) guess = min + 1;

		//Measure the current guess
		guessSize = getTextExtents(s.substring(0, guess), f).width;

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
 * Returns the Dimensions of the given text, converting newlines and tabs appropriately.
 * 
 * @param text the text
 * @param f the font
 * @return the dimensions of the given text
 * @since 2.0
 */
public static Dimension getTextExtents(String text, Font f) {
	return new Dimension(getTextDimension(text, f));
}

/**
 * Returns the Dimensions of <i>s</i> in Font <i>f</i>.
 * 
 * @param s the string
 * @param f the font
 * @return the dimensions of the given string
 * @since 2.0
 */
public static Dimension getStringExtents(String s, Font f) {
	return new Dimension(getStringDimension(s, f));
}

/**
 * Returns the Dimensions of the given text, converting newlines and tabs appropriately.
 * 
 * @param s the string
 * @param f the font
 * @param result the Dimension that will contain the result of this calculation
 * @since 2.0
 */
public static void getTextExtents(String s, Font f, Dimension result) {
	org.eclipse.swt.graphics.Point pt = getTextDimension(s, f);
	result.width = pt.x;
	result.height = pt.y;
}

/**
 * Returns the width of <i>s</i> in Font <i>f</i>.
 * 
 * @param s the string
 * @param f the font
 * @return the width
 * @since 2.0
 */
public static int getTextWidth(String s, Font f) {
	return getTextDimension(s, f).x;
}

/**
 * Returns a Color the same as the passed color in a lighter hue.
 * 
 * @param rgb the color
 * @return the lighter color
 * @since 2.0
 */
public static Color lighter(Color rgb) {
	int r = rgb.getRed(),
	    g = rgb.getGreen(),
	    b = rgb.getBlue();

	return new Color(null,
		Math.max(2, Math.min((int)(r / RGB_VALUE_MULTIPLIER), 255)),
		Math.max(2, Math.min((int)(g / RGB_VALUE_MULTIPLIER), 255)),
		Math.max(2, Math.min((int)(b / RGB_VALUE_MULTIPLIER), 255))
	);
}

/**
 * Produces a ghosting effect on the shape <i>s</i>.
 * 
 * @param s the shape
 * @return the ghosted shape
 * @since 2.0
 */
public static Shape makeGhostShape(Shape s) {
	s.setBackgroundColor(ghostFillColor);
	s.setFillXOR(true);
	s.setOutlineXOR(true);
	return s;
}

/**
 * Mixes the passed Colors and returns the resulting Color.
 * 
 * @param c1 the first color
 * @param c2 the second color
 * @param weight the first color's weight from 0-1
 * @return the new color
 * @since 2.0
 */
public static Color mixColors(Color c1, Color c2, double weight) {
	return new Color(null,
			(int)(c1.getRed() * weight + c2.getRed() * (1 - weight)),
			(int)(c1.getGreen() * weight + c2.getGreen() *(1 - weight)),
			(int)(c1.getBlue() * weight + c2.getBlue() * (1 - weight)));
}


/**
 * Mixes the passed Colors and returns the resulting Color.
 * 
 * @param c1 the first color
 * @param c2 the second color
 * @return the new color
 * @since 2.0
 */
public static Color mixColors(Color c1, Color c2) {
	return new Color(null,
		(c1.getRed() + c2.getRed()) / 2,
		(c1.getGreen() + c2.getGreen()) / 2,
		(c1.getBlue() + c2.getBlue()) / 2);
}

/**
 * Paints a border with an etching effect, having a shadow of Color <i>shadow</i> and
 * highlight of Color <i>highlight</i>.
 * 
 * @param g the graphics object
 * @param r the bounds of the border
 * @param shadow the shadow color
 * @param highlight the highlight color
 * @since 2.0
 */
public static void paintEtchedBorder(Graphics g, Rectangle r, 
										Color shadow, Color highlight) {
	int x = r.x,
		y = r.y,
		w = r.width,
		h = r.height;

	g.setLineStyle(Graphics.LINE_SOLID);
	g.setLineWidth(1);
	g.setXORMode(false);

   	w -= 2;
	h -= 2;

	g.setForegroundColor(shadow);
	g.drawRectangle(x, y, w, h);

	x++;
	y++;
	g.setForegroundColor(highlight);
	g.drawRectangle(x, y, w, h);
}

/**
 * Helper method to paint a grid.  Painting is optimized as it is restricted to the
 * Graphics' clip.
 * 
 * @param	g			The Graphics object to be used for painting
 * @param	f			The figure in which the grid is to be painted
 * @param	origin		Any point where the grid lines are expected to intersect
 * @param	distanceX	Distance between vertical grid lines; if 0 or less, vertical grid
 * 						lines will not be drawn
 * @param	distanceY	Distance between horizontal grid lines; if 0 or less, horizontal
 * 						grid lines will not be drawn
 * 
 * @since 3.0
 */
public static void paintGrid(Graphics g, IFigure f, 
		org.eclipse.draw2d.geometry.Point origin, int distanceX, int distanceY) {
	Rectangle clip = g.getClip(Rectangle.SINGLETON);
	
	if (distanceX > 0) {
		if (origin.x >= clip.x)
			while (origin.x - distanceX >= clip.x)
				origin.x -= distanceX;
		else
			while (origin.x < clip.x)
				origin.x += distanceX;
		for (int i = origin.x; i < clip.x + clip.width; i += distanceX)
			g.drawLine(i, clip.y, i, clip.y + clip.height);
	}
	
	if (distanceY > 0) {
		if (origin.y >= clip.y)
			while (origin.y - distanceY >= clip.y)
				origin.y -= distanceY;
		else
			while (origin.y < clip.y)
				origin.y += distanceY;
		for (int i = origin.y; i < clip.y + clip.height; i += distanceY)
			g.drawLine(clip.x, i, clip.x + clip.width, i);
	}
}

/**
 * Paints a border with an etching effect, having a shadow of a darker version of g's 
 * background color, and a highlight a lighter version of g's background color.
 * 
 * @param g the graphics object
 * @param r the bounds of the border
 * @since 2.0
 */
public static void paintEtchedBorder(Graphics g, Rectangle r) {
	Color rgb = g.getBackgroundColor(),
	    shadow = darker(rgb),
	    highlight = lighter(rgb);
	paintEtchedBorder(g, r, shadow, highlight);
}

/**
 * Sets Font to passed value.
 * 
 * @param f the new font
 * @since 2.0
 */
protected static void setFont(Font f) {
	if (appliedFont == f || f.equals(appliedFont))
		return;
	getGC().setFont(f);
	appliedFont = f;
	metrics = null;
}

/**
 * Returns the figure which contains both of the given figures or null
 * if no such figure exists.
 * @since 3.1
 * @param l one figure
 * @param r the other figure
 * @return the common ancestor
 */
public static IFigure findCommonAncestor(IFigure l, IFigure r) {
	if (l == r)
		return l;
	ArrayList left = new ArrayList();
	ArrayList right = new ArrayList();
	while (l != null) {
		left.add(l);
		l = l.getParent();
	}
	while (r != null) {
		right.add(r);
		r = r.getParent();
	}
	if (left.isEmpty() || right.isEmpty())
		return null;
	int il = left.size() - 1;
	int ir = right.size() - 1;
	while (left.get(il) == right.get(ir)) {
		il--;
		ir--;
	}
	++il;
	if (il < left.size())
		return (IFigure)left.get(il);
	return null;
}

}
