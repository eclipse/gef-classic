package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;

/**
 * SchemeBorder allows the creation of borders based on
 * {@link SchemeBorder.Scheme Schemes}. A <it>Scheme</it> is a class whose
 * only purpose is to carry border specific information.
 * SchemeBorder renders the border based on the information
 * given by the <it>Scheme</it> set to it.
 */
public class SchemeBorder
	extends AbstractBorder
	implements ColorConstants
{

protected Scheme scheme = null;

protected static final Color
	DARKEST_DARKER[]   = new Color[] {buttonDarkest,  buttonDarker},
	LIGHTER_DARKER[]   = new Color[] {buttonLightest, buttonDarker},
	DARKER_LIGHTER[]   = new Color[] {buttonDarker, buttonLightest};

/**
 * Holds a set of information about a border, which 
 * can be changed to create a wide range of schemes.
 * Provides support for border opacity, size, highlight 
 * side and shadow side colors.
 */
public static class Scheme {
	private Insets insets;
	private boolean isOpaque = false;
	protected Color
		highlight[],
		shadow[];

	/**
	 * Constructs a default border Scheme with no
	 * border sides.
	 * @since 2.0
	 */
	protected Scheme(){}

	/**
	 * Constructs a border Scheme with the specified
	 * highlight and shadow colors. The size of the
	 * border depends on the number of colors passed 
	 * in for each parameter. Hightlight colors are
	 * used in the top and left sides of the border, 
	 * and Shadow colors are used in the bottom and
	 * right sides of the border.
	 *
	 * @param highlight  Hightlight colors to be used.
	 * @param shadow  Shadow colors to be used.
	 * @since 2.0
	 */
	public Scheme(Color[] highlight, Color[] shadow){
		this.highlight= highlight;
		this.shadow = shadow;
		init();
	}

	/**
	 * Constructs a border scheme with the specified
	 * colors. The input colors serve as both highlight
	 * and shadow colors. The size of the border is the
	 * number of colors passed in as input. Hightlight 
	 * colors are used in the top and left sides of the 
	 * border, and Shadow colors are used in the bottom 
	 * and right sides of the border.
	 *
	 * @param  Colors to be used for the border.
	 * @since 2.0
	 */
	public Scheme(Color[] colors){
		highlight = shadow = colors;
		init();
	}

	/**
	 * Calculates and returns the Insets for this border
	 * Scheme. The calculations depend on the number of
	 * colors passed in as input. 
	 *
	 * @return  Insets used by this border.
	 * @since 2.0
	 */
	protected Insets calculateInsets(){
		int tl = getHighlight().length;
		int br = getShadow().length;
		return new Insets(tl,tl,br,br);
	}

	/**
	 * Retuns the opaque state of this border scheme.
	 * Returns <code>null<code> if any of the border
	 * colors is <code>null</code>. This is done to 
	 * prevent the appearance of underlying pixels since
	 * the border color is null.
	 * 
	 * @since 2.0
	 */
	protected boolean calculateOpaque(){
		Color colors [] = getHighlight();
		for (int i=0; i<colors.length; i++)
			if (colors[i] == null)
				return false;
		colors = getShadow();
		for (int i=0; i<colors.length; i++)
			if (colors[i] == null)
				return false;
		return true;
	}
	
	/**
	 * Returns the highlight colors being used.
	 *
	 * @return  Highlight colors of this border scheme as an
	 *           array of Colors.
	 * @since 2.0
	 */
	protected Color[] getHighlight(){
		return highlight;
	}

	/**
	 * Returns the Insets required by this Scheme.
	 *
	 * @return  Insets required by this Scheme.
	 * @since 2.0
	 */
	protected Insets getInsets(){
		return insets;
	}
	
	/**
	 * Returns the shadow colors being used.
	 *
	 * @return  Shadow colors used by this border scheme as an
	 *           array of Colors.
	 * @since 2.0
	 */
	protected Color[] getShadow(){
		return shadow;
	}

	/**
	 * Calculates and initializes the properties of this
	 * border scheme.
	 *
	 * @since 2.0
	 */
	protected void init(){
		insets = calculateInsets();
		isOpaque = calculateOpaque();
	}

	/**
	 * Returns whether this border should be opaque
	 * or not.
	 *
	 * @return  The opaque nature of this border scheme.
	 * @since 2.0
	 */
	protected boolean isOpaque(){
		return isOpaque;
	}
}

/**
 * Interface which defines some commonly used schemes
 * for the border. These schemes can be given as input
 * to the {@link SchemeBorder SchemeBorder} to generate
 * appropriate borders.
 */
public static interface SCHEMES {
	public Scheme
		BUTTON_CONTRAST = new Scheme(
			new Color[]{button, buttonLightest},
			DARKEST_DARKER
		),
		BUTTON_RAISED = new Scheme(
			new Color[]{buttonLightest},
			DARKEST_DARKER
		),
		BUTTON_PRESSED = new Scheme(
			DARKEST_DARKER,
			new Color[]{buttonLightest}
		),
		RAISED = new Scheme(
			new Color[]{buttonLightest},
			new Color[]{buttonDarkest}
		),
		LOWERED = new Scheme(
			new Color[]{buttonDarkest},
			new Color[]{buttonLightest}
		),
		RIDGED = new Scheme(LIGHTER_DARKER, DARKER_LIGHTER),
		ETCHED = new Scheme(DARKER_LIGHTER, LIGHTER_DARKER);
}

/** 
 * Constructs a default SchemeBorder with no scheme defined.
 * @since 2.0
 */
protected SchemeBorder(){}

/**
 * Constructs a SchemeBorder with the Scheme given as input.
 *
 * @param Scheme to be used by this border.
 * @since 2.0
 */
public SchemeBorder(Scheme scheme){
	setScheme(scheme);
}

/*
 * Returns the Insets used by this border, based
 * upon the scheme set to it.
 */
public Insets getInsets(IFigure figure){
	return getScheme().getInsets();
}

/**
 * Returns the scheme used by this border.
 *
 * @return  Scheme used by this border.
 * @since 2.0
 */
protected Scheme getScheme(){
	return scheme;
}

/*
 * Returns the opaque state of this border. Returns <code>
 * true</code> indicating that this will fill in the
 * area enclosed by the border.
 */
public boolean isOpaque(){
	return true;
}

/**
 * Sets the Scheme for this border to the Scheme
 * given as input.
 *
 * @param scheme  Scheme for this border.
 * @see  #getScheme()
 * @since 2.0
 */
protected void setScheme(Scheme scheme){
	this.scheme = scheme;
}

/*
 * Paints the border using the information in the 
 * set Scheme and the inputs given. Side widths are 
 * determined by the number of colors in the Scheme 
 * for each side. 
 */
public void paint(IFigure figure, Graphics g, Insets insets){
	Color [] tl = scheme.getHighlight();
	Color [] br = scheme.getShadow();

	paint (g, figure, insets, tl, br);
}

protected void paint(Graphics graphics, IFigure fig, Insets insets,
	Color[] tl, Color[] br)
{
	graphics.setLineWidth(1);
	graphics.setLineStyle(Graphics.LINE_SOLID);
	graphics.setXORMode(false);

	Rectangle rect = getPaintRectangle(fig, insets);

	int top    = rect.y;
	int left   = rect.x;
	int bottom = rect.bottom()-1;
	int right  = rect.right()-1;
	Color color;

	for (int i=0; i < br.length; i++){
		color = br[i];
		graphics.setForegroundColor(color);
		graphics.drawLine(right-i, bottom-i, right-i,top+i);
		graphics.drawLine(right-i, bottom-i, left+i, bottom-i);
	}

	right--;
	bottom--;

	for (int i=0; i < tl.length; i++){
		color = tl[i];
		graphics.setForegroundColor(color);
		graphics.drawLine(left+i, top+i, right-i, top+i);
		graphics.drawLine(left+i, top+i, left+i, bottom-i);
	}
}

}