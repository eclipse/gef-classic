package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;

/**
 * A collection of color-related constants.
 */
public interface ColorConstants {

/**
 * @see SWT#COLOR_WIDGET_HIGHLIGHT_SHADOW
 */
public static final Color buttonLightest          
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
/**
 * @see SWT#COLOR_WIDGET_BACKGROUND
 */
public static final Color button	                
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
/**
 * @see SWT#COLOR_WIDGET_NORMAL_SHADOW
 */
public static final Color buttonDarker            
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
/**
 * @see SWT#COLOR_WIDGET_DARK_SHADOW
 */
public static final Color buttonDarkest           
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);

/**
 * @see SWT#COLOR_LIST_BACKGROUND
 */
public static final Color listBackground          
				= Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
/**
 * @see SWT#COLOR_LIST_FOREGROUND
 */
public static final Color listForeground          
				= Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

/**
 * @see SWT#COLOR_WIDGET_BACKGROUND
 */
public static final Color menuBackground          
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
/**
 * @see SWT#COLOR_WIDGET_FOREGROUND
 */
public static final Color menuForeground          
				= Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
/**
 * @see SWT#COLOR_LIST_SELECTION
 */
public static final Color menuBackgroundSelected  
				= Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);
/**
 * @see SWT#COLOR_LIST_SELECTION_TEXT
 */
public static final Color menuForegroundSelected  
				= Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);

/**
 * @see SWT#COLOR_TITLE_BACKGROUND
 */
public static final Color titleBackground         
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
/**
 * @see SWT#COLOR_TITLE_BACKGROUND_GRADIENT
 */
public static final Color titleGradient           
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
/**
 * @see SWT#COLOR_TITLE_FOREGROUND
 */
public static final Color titleForeground         
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
public static final Color titleInactiveForeground 
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
public static final Color titleInactiveBackground 
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
/**
 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
 */
public static final Color titleInactiveGradient   
				= Display.getDefault().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);

/**
 * @see SWT#COLOR_INFO_FOREGROUND
 */
public static final Color tooltipForeground       
				= Display.getDefault().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
/**
 * @see SWT#COLOR_INFO_BACKGROUND
 */
public static final Color tooltipBackground       
				= Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND);

/**
 * Misc. colors
 */
public static final Color 
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