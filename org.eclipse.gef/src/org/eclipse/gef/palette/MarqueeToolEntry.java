/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.palette;

import org.eclipse.jface.util.Assert;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.tools.MarqueeSelectionTool;

/**
 * A palette ToolEntry for a {@link org.eclipse.gef.tools.MarqueeSelectionTool}.
 * @author hudsonr
 * @since 2.1
 */
public class MarqueeToolEntry 
	extends ToolEntry 
{
	
/**
 * For marquee tools that should select nodes.  This is the default type for this
 * tool.
 * @since 3.1
 */
public static final int SELECT_NODES = 1;
/**
 * For marquee tools that should select connections
 * @since 3.1
 */
public static final int SELECT_CONNECTIONS = 2;

private int selectionType;

/**
 * Creates a new MarqueeToolEntry that can select nodes
 */
public MarqueeToolEntry() {
	this(GEFMessages.MarqueeTool_Label_Nodes, null, SELECT_NODES);
}

/**
 * Creates a new MarqueeToolEntry of the given type
 * @param type {@link #SELECT_NODES} and/or {@link #SELECT_CONNECTIONS}
 * @since 3.1
 */
public MarqueeToolEntry(int type) {
	this(null, null, type);
}

/**
 * Constructor for MarqueeToolEntry.
 * @param label the label
 */
public MarqueeToolEntry(String label) {
	this(label, null, SELECT_NODES);
}

/**
 * Constructor for MarqueeToolEntry.
 * @param label the label
 * @param shortDesc the description (can be <code>null</code>)
 */
public MarqueeToolEntry(String label, String shortDesc) {
	this(label, shortDesc, SELECT_NODES);
}

/**
 * Constructor
 * @param label the label (can be <code>null</code>)
 * @param shortDesc the description (can be <code>null</code>)
 * @param type SELECT_NODES and/or SELECT_CONNECTIONS
 * @since 3.1
 */
public MarqueeToolEntry(String label, String shortDesc, int type) {
	super(label, shortDesc, SharedImages.DESC_MARQUEE_TOOL_16, 
			SharedImages.DESC_MARQUEE_TOOL_24);
	selectionType = type & 3;
	Assert.isTrue(selectionType != 0);
	if (label == null) {
		if (selectionType == SELECT_CONNECTIONS)
			setLabel(GEFMessages.MarqueeTool_Label_Connections);
		else if (selectionType == SELECT_NODES)
			setLabel(GEFMessages.MarqueeTool_Label_Nodes);
		else
			setLabel(GEFMessages.MarqueeTool_Label);
	}
	setUserModificationPermission(PERMISSION_NO_MODIFICATION);
}

/**
 * @see org.eclipse.gef.palette.ToolEntry#createTool()
 */
public Tool createTool() {
	return new MarqueeSelectionTool(selectionType);
}

}