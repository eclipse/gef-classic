/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.internal.Internal;

/**
 * A class containing shared Images and ImageDescriptors for use by clients.
 * @author hudsonr
 * @since 2.1
 */
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

private static ImageDescriptor createDescriptor(String filename) {
	return ImageDescriptor.createFromFile(Internal.class, filename);
}

} 	
