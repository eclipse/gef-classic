package org.eclipse.gef.handles;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * A Locator used to place {@link MoveHandle}s.  By default, a
 * MoveHandle's bounds are equal to its owner figure's bounds,
 * expanded by the handle's {@link Insets}.
 */
public class MoveHandleLocator
	implements Locator
{

private IFigure reference;

/**
 * Creates a new MoveHandleLocator and sets its reference
 * figure to <code>ref</code>.  The reference figure should 
 * be the handle's owner figure.
 */
public MoveHandleLocator(IFigure ref){
	setReference(ref);
}

/**
 * Returns the reference figure for this locator.
 */
protected IFigure getReference(){
	return reference;
}

/**
 * Sets the handle's bounds to that of its owner figure's
 * bounds, expanded by the handle's Insets.
 */
public void relocate(IFigure target) {
	Insets insets = target.getInsets();
	Rectangle bounds;
	if (getReference() instanceof HandleBounds)
		bounds = ((HandleBounds)getReference()).getHandleBounds();
	else
		bounds = getReference().getBounds();
	bounds = bounds.getExpanded(insets.left, insets.top);
	getReference().translateToAbsolute(bounds);
	target.translateToRelative(bounds);
	target.setBounds(bounds);
}

/**
 * Sets the reference figure.
 */
public void setReference(IFigure follow){
	this.reference = follow;
}

}


