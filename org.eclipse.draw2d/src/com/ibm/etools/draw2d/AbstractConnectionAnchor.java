package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.draw2d.geometry.*;

/**
 * Provides support for anchors which depend on a figure
 * for thier location. 
 */
public abstract class AbstractConnectionAnchor
	extends ConnectionAnchorBase
	implements AncestorListener
{

IFigure owner;

/**
 * Constructs an AbstractConnectionAnchor with no owner.
 * 
 * @since 2.0
 */
public AbstractConnectionAnchor() {}

/**
 * Constructs an AbstractConnectionAnchor with the owner 
 * supplied as input.
 *
 * @param owner  Owner of this anchor.
 * @since 2.0
 */
public AbstractConnectionAnchor(IFigure owner) {
	setOwner(owner);
}

/**
 * Adds the given listener to the listeners to be 
 * notified of anchor location changes.
 *
 * @param listener   Listener to be added.
 * @see  #removeAnchorListener(AnchorListener)
 * @since 2.0
 */
public void addAnchorListener(AnchorListener listener){
	if (listener == null)
		return;
	if (listeners.size() == 0)
		getOwner().addAncestorListener(this);
	super.addAnchorListener(listener);
}

/**
 * Notifies all the listeners of this anchor's location change.
 *
 * @param figure  Anchor-owning Figure which has moved.
 * @since 2.0
 */
public void ancestorMoved(IFigure figure) {
	fireAnchorMoved();
}

public void ancestorAdded(IFigure ancestor) {}

public void ancestorRemoved(IFigure ancestor) {}

/** 
 * Returns the owner Figure on which this anchor's
 * location is dependent.
 *
 * @return  Owner of this anchor.
 * @see #setOwner(IFigure)
 * @since 2.0
 */
public IFigure getOwner() {
	return owner;
}

/**
 * Returns the point which is used as the reference by this 
 * AbstractConnectionAnchor. It is generally dependent on the Figure
 * which is the owner of this AbstractConnectionAnchor.
 *
 * @return  The reference point of this anchor.
 * @since 2.0
 */
public Point getReferencePoint() {
	if (getOwner() == null)
		return null;
	else{
		Point ref = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(ref);
		return ref;
	}
}

/**
 * Removes the given listener from this anchor. 
 * If all the listeners are removed, then this anchor
 * removes itself from its owner.
 *
 * @param listener  Listener to be removed from this anchors listeners list.
 * @see  #addAnchorListener(AnchorListener)
 * @since 2.0
 */
public void removeAnchorListener(AnchorListener listener){
	super.removeAnchorListener(listener);
	if (listeners.size() == 0)
		getOwner().removeAncestorListener(this);
}

/**
 * Sets the owner of this anchor, on whom this anchors location
 * is dependent.
 *
 * @param owner  Owner of this anchor.
 * @since 2.0
 */
public void setOwner(IFigure owner){
	this.owner = owner;
}

}