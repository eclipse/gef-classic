package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


/**
 * Contains constants used in conjunction with {@link GEFPlugin}.
 * @see GEFPlugin#getImage(String)
 * @see GEFPlugin#getImageDescriptor(String)
 */
public interface SharedImageConstants {

/**
 * Use this key to obtain the Icon
 * <img src="doc-files/marquee16.gif" border=1 style="background-color: #C0C0C0;
 * border-style: outset; padding-left: 2; padding-right: 1; padding-top: 2;
 * padding-bottom: 1"/> from {@link GEFPlugin#getImage(String)}.
 */
public static String ICON_TOOL_MARQUEE_16
	= "ICON_TOOLMARQUEE16"; //$NON-NLS-1$

/**
 * Use this key to obtain the Icon
 * <img src="doc-files/arrow16.gif" border=1 style="background-color: #C0C0C0;
 * border-style: outset; padding-left: 2; padding-right: 1; padding-top: 2;
 * padding-bottom: 1"/> from {@link GEFPlugin#getImage(String)}.
 */
public static String ICON_TOOL_ARROW_16
	= "ICON_TOOLARROW16"; //$NON-NLS-1$

/**
 * Use this key to obtain the Icon <img src="doc-files/arrow32.gif" border=1
 * style="background-color: #C0C0C0; border-style: outset; padding-left: 2; padding-right:
 * 1; padding-top: 2; padding-bottom: 1"/> from {@link GEFPlugin#getImage(String)}.
 */
public static String ICON_TOOL_ARROW_32
	= "ICON_TOOLARROW32"; //$NON-NLS-1$

}