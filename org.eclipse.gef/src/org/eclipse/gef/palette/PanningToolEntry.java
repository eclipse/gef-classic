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
package org.eclipse.gef.palette;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.tools.PanningTool;

/**
 * A ToolEntry for a {@link PanningTool}.
 * @author msorens
 * @since 3.0
 */
public class PanningToolEntry extends ToolEntry {

/**
 * Creates a new PanningToolEntry.
 */
public PanningToolEntry() {
	this(GEFMessages.SelectionTool_Label);
}

/**
 * Constructor for PanningToolEntry.
 * @param label the label
 */
public PanningToolEntry(String label) {
	this(label, null);
}

/**
 * Constructor for PanningToolEntry.
 * @param label the label
 * @param shortDesc the description
 */
public PanningToolEntry(String label, String shortDesc) {
	super(
		label,
		shortDesc,
		SharedImages.DESC_SELECTION_TOOL_16,
		SharedImages.DESC_SELECTION_TOOL_24);
	setUserModificationPermission(PERMISSION_NO_MODIFICATION);
}

/**
 * @see org.eclipse.gef.palette.ToolEntry#createTool()
 */
public Tool createTool() {
	return new PanningTool();
}

}
