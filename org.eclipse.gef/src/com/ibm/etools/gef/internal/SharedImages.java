package com.ibm.etools.gef.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2001 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.*;

public class SharedImages {

public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER;
public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT;
public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT;

public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE;
public static final ImageDescriptor DESC_VERT_ALIGN_TOP;
public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM;

public static final ImageDescriptor ICON_MATCH_WIDTH;
public static final ImageDescriptor ICON_MATCH_HEIGHT;

public static final Image ICON_DOWN;
public static final Image ICON_DOWN_GRAYED;
public static final Image ICON_DOWN_PRESSED;

public static final Image ICON_UP;
public static final Image ICON_UP_GRAYED;
public static final Image ICON_UP_PRESSED;

static {
	DESC_VERT_ALIGN_BOTTOM = createDescriptor("icons/abottom.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_CENTER = createDescriptor("icons/acenter.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_LEFT = createDescriptor("icons/aleft.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_MIDDLE = createDescriptor("icons/amiddle.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_RIGHT = createDescriptor("icons/aright.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_TOP = createDescriptor("icons/atop.gif"); //$NON-NLS-1$

	ICON_MATCH_WIDTH = createDescriptor("icons/sizehz.gif"); //$NON-NLS-1$
	ICON_MATCH_HEIGHT = createDescriptor("icons/sizevt.gif"); //$NON-NLS-1$

	ICON_DOWN = create("icons/down.gif"); //$NON-NLS-1$
	ICON_DOWN_GRAYED = create("icons/downgray.gif"); //$NON-NLS-1$
	ICON_DOWN_PRESSED = create("icons/downpressed.gif"); //$NON-NLS-1$

	ICON_UP = create("icons/up.gif"); //$NON-NLS-1$
	ICON_UP_GRAYED = create("icons/upgray.gif"); //$NON-NLS-1$
	ICON_UP_PRESSED = create("icons/uppressed.gif"); //$NON-NLS-1$
}

static private Image create(String filename){
	return ImageDescriptor.createFromFile(
		SharedImages.class,
		filename).createImage();
}

static private ImageDescriptor createDescriptor(String filename){
	return ImageDescriptor.createFromFile(SharedImages.class,filename);
}

} 	