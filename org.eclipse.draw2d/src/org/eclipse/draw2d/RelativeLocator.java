package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * A locator that specfies a point that is relative to the bounds
 * of a {@link Figure}.  There are two <code>double</code> values that
 * determine where the target Figure will be placed.  These amounts 
 * represent the percentage of the reference Figure's bounds the 
 * target Figure should be displaced from the reference Figure's 
 * top-left corner.  The values 0.0 and 0.0 will result in the target 
 * Figure being located at the reference Figure's top-left corner.
 * The values 1.0 and 1.0 will result in the target Figure being 
 * located at the reference Figure's bottom-right corner.
 */
public class RelativeLocator
	implements Locator
{

private double  relativeX;
private double  relativeY;
private IFigure reference;

/**
 * Creates a new RelativeLocator which will locate its
 * target Figure in the top-left corner of the reference
 * Figure (which must be set separately).
 * 
 * @since 2.0
 */
public RelativeLocator() {
	relativeX = 0.0;
	relativeY = 0.0;
}

/**
 * Creates a new RelativeLocator with the given reference
 * Figure, that will locate its target figure based on
 * <code>location</code>, which is one of the compass
 * directions defined in {@link PositionConstants}.
 * 
 * @since 2.0
 */
public RelativeLocator(IFigure reference, int location){
	setReferenceFigure(reference);
	switch (location & PositionConstants.NORTH_SOUTH){
		case PositionConstants.NORTH:
			relativeY = 0; break;
		case PositionConstants.SOUTH:
			relativeY = 1.0; break;
		default:
			relativeY = 0.5;
	}

	switch (location & PositionConstants.EAST_WEST){
		case PositionConstants.WEST:
			relativeX = 0; break;
		case PositionConstants.EAST:
			relativeX = 1.0; break;
		default:
			relativeX = 0.5;
	}
}

/**
 * Creates a new RelativeLocator with the given reference Figure
 * that locates its target Figure based on <code>_relativeX</code>
 * and <code>_relativeY</code>.
 * 
 * @since 2.0
 */
public RelativeLocator(IFigure reference, double _relativeX, double _relativeY) {
	setReferenceFigure(reference);
	relativeX = _relativeX;
	relativeY = _relativeY;
}

/**
 * Returns the Reference Box in the Reference Figure's coordinate system.
 * The returned Rectangle may be by reference, and should <b>not</b> be modified.
 * 
 * @since 2.0
 */
protected Rectangle getReferenceBox(){
	return getReferenceFigure().getBounds();
}

/**
 * Returns the Figure this locator is relative to.
 * 
 * @since 2.0
 */
protected IFigure getReferenceFigure(){
	return reference;
}

public void relocate(IFigure target){
	IFigure reference = getReferenceFigure();
	Rectangle targetBounds = getReferenceBox().getCopy();
	reference.translateToAbsolute(targetBounds);
	target.translateToRelative(targetBounds);

	Dimension targetSize = target.getPreferredSize();

	targetBounds.x += (int)(targetBounds.width*relativeX - ((targetSize .width+1)/2));
	targetBounds.y += (int)(targetBounds.height*relativeY - ((targetSize .height+1)/2));
	targetBounds.setSize(targetSize );
	target.setBounds(targetBounds);
}

/**
 * Sets the Figure this locator should be relative to.
 * 
 * @since 2.0
 */
public void setReferenceFigure(IFigure reference){
	this.reference = reference;
}

}
