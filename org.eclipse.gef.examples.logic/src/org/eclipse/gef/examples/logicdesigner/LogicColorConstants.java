package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.FigureUtilities;

public interface LogicColorConstants
	extends ColorConstants{
	public final static Color 
		logicGreen = new Color(null, 40,100,70),
		logicPrimarySelectedColor = new Color(null, 10,36,106),
		logicSecondarySelectedColor = FigureUtilities.mixColors(logicPrimarySelectedColor, button),
		logicBackgroundBlue = new Color(null, 200, 200, 240);

}