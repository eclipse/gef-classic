package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.*;

/**
 * ArrowButton - Button containing an arrow in it, and providing
 * directional support for the arrow.
 */
final public class ArrowButton
	extends Button
	implements Orientable
{

{
	createTriangle();
	setRequestFocusEnabled(false);
	setFocusTraversable(false);
}

/**
 * Constructs a default ArrowButton with the arrow pointing 
 * north.
 * 
 * @since 2.0
 * 
 */
public ArrowButton(){}

/**
 * Constructs an ArrowButton with the arrow having the direction
 * given in the input.
 *
 * @param direction  Direction of the arrow. Can be one of 
 * many directional constants defined in {@link PositionConstants}.
 * @since 2.0
 */
public ArrowButton(int direction){setDirection(direction);}

/**
 * Contructs a triangle with a black background pointing north,
 * and sets it as the contents of the button.
 * 
 * @since 2.0
 */
protected void createTriangle(){
	Triangle tri = new Triangle();
	tri.setOutline(false);
	tri.setBackgroundColor(ColorConstants.black);
	tri.setBorder(new MarginBorder(new Insets(2)));
	setContents(tri);
}

public void setDirection(int value){
	setChildrenDirection(value);
}

public void setOrientation(int value){
	setChildrenOrientation(value);
}

}