/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;

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