package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Image;

/**
 * Basic Rule for Toggle: Whoever creates the toggle is
 * reponsible for response changes for it 
 * (selection / rollover / etc...). Only CheckBox does
 * its own listening.
 */
public class Toggle
	extends Clickable{

/**
 * Constructs a Toggle with no text or icon.
 * 
 * @since 2.0
 */
public Toggle() {
	super();
	setStyle(STYLE_TOGGLE);
}

/**
 * Constructs a Toggle with passed text and icon
 * 
 * @since 2.0
 */
public Toggle(String text, Image icon){
	super(new Label(text, icon), STYLE_TOGGLE);
}

/**
 * Constructs a Toggle with passed IFigure as its 
 * contents.
 * 
 * @since
 */
public Toggle(IFigure contents){
	super(contents, STYLE_TOGGLE);
}

}