package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * A labeled border intended to house a Figure with a group of children.
 * The label should serve as a description of the group.
 */
public class GroupBoxBorder
	extends AbstractLabeledBorder
{

/**
 * Constructs a GroupBoxBorder with the name of this
 * class as its label.
 * 
 * @since 2.0
 */
public GroupBoxBorder(){}

/**
 * Constructs a GroupBoxBorder with label s.
 * 
 * @since 2.0
 */
public GroupBoxBorder(String s){super(s);}

/**
 * Calculates and returns the Insets for this GroupBoxBorder.
 *
 * @param figure   IFigure on which the calculations
 *                  should be made. Generally this is the 
 *                  IFigure of which this GroupBoxBorder is surrounding.
 * @return  The Insets for this GroupBoxBorder. 
 * @since 2.0
 */
protected Insets calculateInsets(IFigure figure){
	int height = getTextExtents(figure).height;
	return new Insets(height);
}

public Dimension getPreferredSize( IFigure fig ){
	Dimension textSize = getTextExtents(fig);
	return textSize.getCopy().expand(textSize.height * 2, 0);
}

/*
 * Returns false, making the Figure
 * for which this is the border clip the non-
 * border region and fill it up itself. An 
 * optimization for drawing borders.
 */
public boolean isOpaque(){return false;}

public void paint(IFigure figure, Graphics g, Insets insets){
	tempRect.setBounds(getPaintRectangle(figure, insets));
	Rectangle r = tempRect;
	if (r.isEmpty())
		return;

	Rectangle textLoc = new Rectangle(r.getTopLeft(), getTextExtents(figure));
	r.crop(new Insets(getTextExtents(figure).height/2));
	FigureUtilities.paintEtchedBorder(g, r);

	textLoc.x += getInsets(figure).left;
	g.setFont(getFont(figure));
	g.setForegroundColor(getTextColor());
	g.clipRect(textLoc);
	g.fillText(getLabel(), textLoc.getTopLeft());
}

}