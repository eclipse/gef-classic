package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.geometry.*;

/**
 * Provides a raised border.
 */
public class SimpleRaisedBorder
	extends SchemeBorder
{

private static final Scheme DOUBLE = new Scheme(
	new Color[] {ColorConstants.buttonLightest, ColorConstants.button},
	new Color[] {ColorConstants.buttonDarkest,  ColorConstants.buttonDarker}
);

/**
 * Constructs a SimpleRaisedBorder with the predefined  
 * SCHEMES.BUTTON_RAISED Scheme set as default.
 * 
 * @since 2.0
 */
public SimpleRaisedBorder(){
	super(SCHEMES.BUTTON_RAISED);
}

/**
 * Constructs a SimpleRaisedBorder with the width of
 * all sides provided as input. 
 *
 * @param width  Width of all the sides of the border.
 *                If width == 2, this SimpleRaisedBorder
 *                will use the local DOUBLE Scheme,
 *                Else this SimpleRaisedButton will
 *                use the SCHEMES.BUTTON_RAISED Scheme.
 * 
 * @since 2.0
 */
public SimpleRaisedBorder(int width){
	super(width == 2 ? DOUBLE : SCHEMES.BUTTON_RAISED);
}

}