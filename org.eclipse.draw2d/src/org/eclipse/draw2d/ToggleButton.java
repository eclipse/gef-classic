package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

/**
 * A Toggle that appears like a 3-dimensional button.
 */
public class ToggleButton
	extends Toggle{

protected Label label = null;

public ToggleButton(){
}

/**
 * Constructs a ToggleButton with the passed
 * IFigure as its contents.
 * 
 * @since 2.0
 */
public ToggleButton(IFigure contents){
	super(contents);
}

/**
 * Constructs a ToggleButton with the passed
 * string as its text.
 * 
 * @since 2.0
 */
public ToggleButton(String text){
	this(text, null);
}

/**
 * Constructs a ToggleButton with a Label
 * containing the passed text and icon.
 * 
 * @since 2.0
 */
public ToggleButton(String text, Image normalIcon){
	super(text, normalIcon);
}

/**
 * Initializes this Clickable by setting a default model
 * and adding a clickable event handler for that model.
 * 
 * @since 2.0
 */
protected void init (){
	setStyle(STYLE_BUTTON);
	super.init();
}

}