/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color-related constants.
 */
public interface ColorConstants {

/**
 * @see SWT#COLOR_WIDGET_HIGHLIGHT_SHADOW
 */
Color buttonLightest
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
/**
 * @see SWT#COLOR_WIDGET_BACKGROUND
 */
Color button
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
/**
 * @see SWT#COLOR_WIDGET_NORMAL_SHADOW
 */
Color buttonDarker
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
/**
 * @see SWT#COLOR_WIDGET_DARK_SHADOW
 */
Color buttonDarkest           
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);

/**
 * @see SWT#COLOR_LIST_BACKGROUND
 */
Color listBackground          
				= Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
/**
 * @see SWT#COLOR_LIST_FOREGROUND
 */
Color listForeground          
				= Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

/**
 * @see SWT#COLOR_WIDGET_BACKGROUND
 */
Color menuBackground          
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
/**
 * @see SWT#COLOR_WIDGET_FOREGROUND
 */
Color menuForeground          
				= Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
/**
 * @see SWT#COLOR_LIST_SELECTION
 */
Color menuBackgroundSelected  
				= Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION);
/**
 * @see SWT#COLOR_LIST_SELECTION_TEXT
 */
Color menuForegroundSelected  
				= Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);

/**
 * @see SWT#COLOR_TITLE_BACKGROUND
 */
Color titleBackground         
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
/**
 * @see SWT#COLOR_TITLE_BACKGROUND_GRADIENT
 */
Color titleGradient           
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
/**
 * @see SWT#COLOR_TITLE_FOREGROUND
 */
Color titleForeground         
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
Color titleInactiveForeground 
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
Color titleInactiveBackground 
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
Color titleInactiveGradient   
				= Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);

/**
 * @see SWT#COLOR_INFO_FOREGROUND
 */
Color tooltipForeground       
				= Display.getCurrent().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
/**
 * @see SWT#COLOR_INFO_BACKGROUND
 */
Color tooltipBackground       
				= Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND);

/**
 * Misc. colors
 */
Color 
	white      = new Color(null, 255, 255, 255),
	lightGray  = new Color(null, 192, 192, 192),
	gray       = new Color(null, 128, 128, 128),
	darkGray   = new Color(null,  64,  64,  64),
	black      = new Color(null,   0,   0,   0),
	red        = new Color(null, 255,   0,   0),
	orange     = new Color(null, 255, 196,   0),
	yellow     = new Color(null, 255, 255,   0),
	green      = new Color(null,   0, 255,   0),
	lightGreen = new Color(null,  96, 255,  96),
	darkGreen  = new Color(null,   0, 127,   0),
	cyan       = new Color(null,   0, 255, 255),
	lightBlue  = new Color(null, 127, 127, 255),
	blue       = new Color(null,   0,   0, 255),
	darkBlue   = new Color(null,   0,   0, 127);

}
