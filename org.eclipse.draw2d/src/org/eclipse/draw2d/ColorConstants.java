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
public interface ColorConstants 
{

public final static Display display = Display.getDefault();

public final static Color

buttonLightest          = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
button	                = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
buttonDarker            = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
buttonDarkest           = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW),

listBackground          = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
listForeground          = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND),

menuBackground          = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
menuForeground          = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND),
menuBackgroundSelected  = display.getSystemColor(SWT.COLOR_LIST_SELECTION),
menuForegroundSelected  = display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT),

titleBackground         = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND),
titleGradient           = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT),
titleForeground         = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND),
titleInactiveForeground = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND),
titleInactiveBackground = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND),
titleInactiveGradient   = display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND),

tooltipForeground       = display.getSystemColor(SWT.COLOR_INFO_FOREGROUND),
tooltipBackground       = display.getSystemColor(SWT.COLOR_INFO_BACKGROUND),

white      = new Color(null,255, 255, 255),
lightGray  = new Color(null,192, 192, 192),
gray       = new Color(null,128, 128, 128),
darkGray   = new Color(null, 64,  64,  64),
black      = new Color(null,  0,   0,   0),
red        = new Color(null,255,   0,   0),
orange     = new Color(null,255, 196,   0),
yellow     = new Color(null,255, 255,   0),
green      = new Color(null,  0, 255,   0),
lightGreen = new Color(null, 96, 255,  96),
darkGreen  = new Color(null,  0, 127,   0),
cyan       = new Color(null,  0, 255, 255),
lightBlue  = new Color(null,127, 127, 255),
blue       = new Color(null,  0,   0, 255),
darkBlue   = new Color(null,  0,   0, 127);
}