package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.internal.Internal;

/**
 * A class containing shared Images and ImageDescriptors for use by clients.
 * @author hudsonr
 * @since 2.1 */
public class SharedImages {

/**
 * A 16x16 Icon representing the Selection Tool
 */
public static final ImageDescriptor DESC_SELECTION_TOOL_16;

/**
 * A 24x24 Icon representing the Selection Tool
 */
public static final ImageDescriptor DESC_SELECTION_TOOL_24;

/**
 * A 16x16 Icon representing the Marquee Tool
 */
public static final ImageDescriptor DESC_MARQUEE_TOOL_16;

/**
 * A 24x24 Icon representing the Marquee Tool
 */
public static final ImageDescriptor DESC_MARQUEE_TOOL_24;

static {
	DESC_SELECTION_TOOL_16 = createDescriptor("icons/arrow16.gif"); //$NON-NLS-1$
	DESC_SELECTION_TOOL_24 = createDescriptor("icons/arrow24.gif"); //$NON-NLS-1$
	DESC_MARQUEE_TOOL_16 = createDescriptor("icons/marquee16.gif"); //$NON-NLS-1$
	DESC_MARQUEE_TOOL_24 = createDescriptor("icons/marquee24.gif"); //$NON-NLS-1$
}

private static Image create(String filename) {
	return ImageDescriptor.createFromFile(
		Internal.class,
		filename).createImage();
}

private static ImageDescriptor createDescriptor(String filename) {
	return ImageDescriptor.createFromFile(Internal.class, filename);
}

} 	