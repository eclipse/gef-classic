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
package org.eclipse.gef.internal;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchImages;

public class InternalImages {

public static final ImageDescriptor DESC_ZOOM_IN;
public static final ImageDescriptor DESC_ZOOM_OUT;

public static final ImageDescriptor DESC_MATCH_WIDTH;
public static final ImageDescriptor DESC_MATCH_HEIGHT;

public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER;
public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT;
public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT;

public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE;
public static final ImageDescriptor DESC_VERT_ALIGN_TOP;
public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM;

public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER_BW;
public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT_BW;
public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT_BW;

public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE_BW;
public static final ImageDescriptor DESC_VERT_ALIGN_TOP_BW;
public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM_BW;

public static final ImageDescriptor DESC_HORZ_ALIGN_CENTER_DIS;
public static final ImageDescriptor DESC_HORZ_ALIGN_LEFT_DIS;
public static final ImageDescriptor DESC_HORZ_ALIGN_RIGHT_DIS;

public static final ImageDescriptor DESC_VERT_ALIGN_MIDDLE_DIS;
public static final ImageDescriptor DESC_VERT_ALIGN_TOP_DIS;
public static final ImageDescriptor DESC_VERT_ALIGN_BOTTOM_DIS;

public static final ImageDescriptor ICON_MATCH_WIDTH;
public static final ImageDescriptor ICON_MATCH_HEIGHT;

public static final ImageDescriptor DESC_SEPARATOR;
public static final ImageDescriptor DESC_FOLDER_OPEN;
public static final ImageDescriptor DESC_FOLDER_CLOSED;

static {
	DESC_ZOOM_IN = createDescriptor("icons/zoomplus.gif"); //$NON-NLS-1$
	DESC_ZOOM_OUT = createDescriptor("icons/zoomminus.gif"); //$NON-NLS-1$

	DESC_MATCH_WIDTH = createDescriptor("icons/matchwidth.gif"); //$NON-NLS-1$
	DESC_MATCH_HEIGHT = createDescriptor("icons/matchheight.gif"); //$NON-NLS-1$
			
	DESC_VERT_ALIGN_BOTTOM = createDescriptor("icons/alignbottom.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_CENTER = createDescriptor("icons/aligncenter.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_LEFT = createDescriptor("icons/alignleft.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_MIDDLE = createDescriptor("icons/alignmid.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_RIGHT = createDescriptor("icons/alignright.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_TOP = createDescriptor("icons/aligntop.gif"); //$NON-NLS-1$

	DESC_VERT_ALIGN_BOTTOM_BW = createDescriptor("icons/alignbottom_bw.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_CENTER_BW = createDescriptor("icons/aligncenter_bw.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_LEFT_BW = createDescriptor("icons/alignleft_bw.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_MIDDLE_BW = createDescriptor("icons/alignmid_bw.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_RIGHT_BW = createDescriptor("icons/alignright_bw.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_TOP_BW = createDescriptor("icons/aligntop_bw.gif"); //$NON-NLS-1$

	DESC_VERT_ALIGN_BOTTOM_DIS = createDescriptor("icons/alignbottom_d.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_CENTER_DIS = createDescriptor("icons/aligncenter_d.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_LEFT_DIS = createDescriptor("icons/alignleft_d.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_MIDDLE_DIS = createDescriptor("icons/alignmid_d.gif"); //$NON-NLS-1$
	DESC_HORZ_ALIGN_RIGHT_DIS = createDescriptor("icons/alignright_d.gif"); //$NON-NLS-1$
	DESC_VERT_ALIGN_TOP_DIS = createDescriptor("icons/aligntop_d.gif"); //$NON-NLS-1$

	DESC_SEPARATOR = createDescriptor("icons/separator.gif"); //$NON-NLS-1$
	DESC_FOLDER_OPEN = WorkbenchImages.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
	DESC_FOLDER_CLOSED = createDescriptor("icons/folder_closed.gif"); //$NON-NLS-1$

	ICON_MATCH_WIDTH = createDescriptor("icons/sizehz.gif"); //$NON-NLS-1$
	ICON_MATCH_HEIGHT = createDescriptor("icons/sizevt.gif"); //$NON-NLS-1$
}

private static Image create(String filename) {
	return ImageDescriptor.createFromFile(
		InternalImages.class,
		filename).createImage();
}

private static ImageDescriptor createDescriptor(String filename) {
	return ImageDescriptor.createFromFile(InternalImages.class, filename);
}

} 	