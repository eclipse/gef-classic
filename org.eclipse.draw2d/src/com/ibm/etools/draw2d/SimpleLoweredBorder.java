package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

/**
 * Provides a lowered border.
 */
final public class SimpleLoweredBorder
	extends SchemeBorder
{

private static final Scheme DOUBLE = new Scheme(
	new Color[] {ColorConstants.buttonDarkest,  ColorConstants.buttonDarker},
	new Color[] {ColorConstants.buttonLightest, ColorConstants.button}
);

/**
 * Constructs a SimpleLoweredBorder with the predefined 
 * button-pressed Scheme set as default.
 * 
 * @since 2.0
 */
public SimpleLoweredBorder(){
	super(SCHEMES.BUTTON_PRESSED);
}

/**
 * Constructs a SimpleLoweredBorder with the width of
 * all sides provided as input. 
 *
 * @param width  Width of all the sides of the border.
 *                If width == 2, this SimpleLoweredBorder
 *                will use the local DOUBLE Scheme,
 *                Else this SimpleLoweredButton will
 *                use the SCHEMES.BUTTON_PRESSED Scheme.
 * 
 * @since 2.0
 */
public SimpleLoweredBorder(int width){
	super(width == 2 ? DOUBLE : SCHEMES.BUTTON_PRESSED);
}

}