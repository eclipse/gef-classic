package org.eclipse.gef.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class SharedImages {

public static final ImageDescriptor DESC_ZOOM_IN;
public static final ImageDescriptor DESC_ZOOM_OUT;

public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER;
public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT;
public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT;

public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE;
public static final ImageDescriptor DESC_VERT_ALIGN_TOP;
public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM;

public static final ImageDescriptor ICON_MATCH_WIDTH;
public static final ImageDescriptor ICON_MATCH_HEIGHT;

static {
	DESC_ZOOM_IN = createDescriptor("icons/zoomplus.gif"); //$NON-NLS-1$
	DESC_ZOOM_OUT = createDescriptor("icons/zoomminus.gif"); //$NON-NLS-1$

	DESC_VERT_ALIGN_BOTTOM = createDescriptor("icons/abottom.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_CENTER = createDescriptor("icons/acenter.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_LEFT = createDescriptor("icons/aleft.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_MIDDLE = createDescriptor("icons/amiddle.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_RIGHT = createDescriptor("icons/aright.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_TOP = createDescriptor("icons/atop.gif"); //$NON-NLS-1$

	ICON_MATCH_WIDTH = createDescriptor("icons/sizehz.gif"); //$NON-NLS-1$
	ICON_MATCH_HEIGHT = createDescriptor("icons/sizevt.gif"); //$NON-NLS-1$
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