package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Transparent Figure intended to be added exclusively to 
 * a {@link LayeredPane}, who has the responsibilty of managing 
 * its Layers. 
 */
public class Layer
	extends TransparentFigure
{

protected Layer nextLayer, prevLayer;

/**
 * Returns the next Layer.
 * 
 * @since 2.0
 */
public Layer getNextLayer(){
	return nextLayer;
}

/**
 * Returns the previous Layer.
 * 
 * @since 2.0
 */
public Layer getPreviousLayer(){
	return prevLayer;
}

public UpdateManager getUpdateManager(){
	//If someone has set the manager, get it.
	UpdateManager manager = super.getUpdateManager();

	if ((getParent() != null && getParent().getUpdateManager() != manager)
		|| (getParent() == null && manager != NO_MANAGER))
			return manager;

	if (getNextLayer() != null)
		return getNextLayer().getUpdateManager();

	return manager;
}

public void paint(Graphics graphics){
	if (getPreviousLayer() != null){
		graphics.pushState();
		getPreviousLayer().paint(graphics);
		graphics.popState();
	}
	if (isVisible())
		super.paint(graphics);
}

/**
 * Sets the next Layer to the passed value.
 * 
 * @since 2.0
 */
public void setNextLayer(Layer layer){
	nextLayer = layer;
}

/**
 * Sets the previous Layer to the passed value.
 * 
 * @since 2.0
 */
public void setPreviousLayer(Layer layer){
	prevLayer = layer;
}

}