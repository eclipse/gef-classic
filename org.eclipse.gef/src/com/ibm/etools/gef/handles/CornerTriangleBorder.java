package com.ibm.etools.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import com.ibm.etools.gef.*;

/**
 * A {@link Border} with a triangle in each corner.
 */
final public class CornerTriangleBorder
	extends AbstractBorder
{

private GraphicalEditPart ownerEditPart;
private boolean isPrimary = true;

/**
 * Creates a new <code>CornerTriangleBorder</code>.
 *
 * @param isPrimary Determines this border's color.
 */
public CornerTriangleBorder(boolean isPrimary) {
	super();
	this.isPrimary = isPrimary;
}

public boolean isOpaque() {
	return true;
}

public Insets getInsets(IFigure figure) {
	return new Insets(1);
}

/**
 * Paints the border.  If this is a border for the primary 
 * selection, it's painted black.  Otherwise, it's painted white.
 *
 * @param figure   The <code>IFigure</code> this border will be painted on
 * @param graphics The <code>Graphics</code>.
 * @param insets   The <code>Insets</code>.
 */
public void paint(IFigure figure, Graphics graphics, Insets insets) {
	// Don't paint the center of the figure.
	int width = 1, edgeSize;
	Rectangle rect = tempRect;
	tempRect.setBounds(getPaintRectangle(figure, insets));
	rect.width--;
	rect.height--;
	rect.shrink(width/2,width/2);
	graphics.setLineWidth(width);
	
	//Draw the primary handles one pixel larger than the secondary
	//handles.  Primary which paints as black with white border looks
	//smaller than secondary which paints as white with black border.
	edgeSize = isPrimary() ? 7 : 6;
	
	//Top left corner
	PointList pList = new PointList();
	pList.addPoint(rect.getTopLeft());
	pList.addPoint(new Point(rect.x, rect.y+edgeSize));
	pList.addPoint(new Point(rect.x+edgeSize, rect.y));
	
	graphics.setBackgroundColor(getFillColor());
	graphics.fillPolygon(pList);
	graphics.setForegroundColor(getBorderColor()); 
	graphics.drawPolygon(pList);

	//Bottom left corner
	pList = new PointList();
	pList.addPoint(rect.getBottomLeft());
	pList.addPoint(new Point((rect.x+edgeSize),(rect.y+rect.height)));
	pList.addPoint(new Point(rect.x,(rect.y+rect.height-edgeSize)));
	
	graphics.setBackgroundColor(getFillColor());
	graphics.fillPolygon(pList);
	graphics.setForegroundColor(getBorderColor()); 
	graphics.drawPolygon(pList);

	//Top right corner
	pList = new PointList();
	pList.addPoint(rect.getTopRight());
	pList.addPoint(new Point(((rect.x+rect.width)-edgeSize),rect.y));
	pList.addPoint(new Point((rect.x+rect.width),(rect.y+edgeSize)));
	
	graphics.setBackgroundColor(getFillColor());
	graphics.fillPolygon(pList);
	graphics.setForegroundColor(getBorderColor()); 
	graphics.drawPolygon(pList);

	//Bottom right corner
	pList = new PointList();
	pList.addPoint(rect.getBottomRight());
	pList.addPoint(new Point(((rect.x+rect.width)-edgeSize),(rect.y+rect.height)));
	pList.addPoint(new Point((rect.x+rect.width),(rect.y+rect.height-edgeSize)));
	
	graphics.setBackgroundColor(getFillColor());
	graphics.fillPolygon(pList);
	graphics.setForegroundColor(getBorderColor()); 
	graphics.drawPolygon(pList);
}

/**
 * Returns the outline color based on what is returned
 * by {@link #isPrimary()}.
 *
 * @return The outline color.
 */
protected Color getBorderColor() {
	return (isPrimary()) ? 
		ColorConstants.white : 
		ColorConstants.black;
}

/**
 * Returns the fill color based on what is returned
 * by {@link #isPrimary()}.
 *
 * @return The fill color.
 */
protected Color getFillColor() {
	return (isPrimary()) ? 
		ColorConstants.black : 
		ColorConstants.white;
}

/**
 * Returns <code>true</code> if this border is for the 
 * primary object in the selection. Otherwise, returns
 * <code>false</code>.
 *
 * @return Whether or not this is a primary border.
 */
public boolean isPrimary() {
	return isPrimary;
}

/**
 * Sets this border as primary if <code>isPrimary</code>
 * is <code>true</code>.
 *
 * @param isPrimary True if this border is for the primary object in the selection.
 */
public void setPrimary(boolean isPrimary) {
	this.isPrimary = isPrimary;
}

}
