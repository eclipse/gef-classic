package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * LabelAnchors must have an owner of type {@link Label Label}.
 * The LabelAnchor behaves like {@link ChopboxAnchor ChopboxAnchor}
 * but {@link Connection Connections} will point to the center of its 
 * owner's icon as opposed to the center of the entire owning Label.
 */
public class LabelAnchor 
	extends ChopboxAnchor{

/**
 * Constructs a LabelAnchor with no owner.
 * 
 * @since 2.0
 */
public LabelAnchor(){}

/**
 * Constructs a LabelAnchor with owner aLabelFigure.
 *
 * @since 2.0
 */
public LabelAnchor(Label aLabelFigure){
	super(aLabelFigure);
}

/**
 * Returns the bounds of this LabelAnchor's owning Label icon.
 * 
 * @since 2.0
 */
protected Rectangle getBox(){
	Label label = (Label) getOwner();
	return label.getIconBounds();
}

}