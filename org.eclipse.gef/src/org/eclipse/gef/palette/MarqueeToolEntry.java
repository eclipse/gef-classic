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

import java.util.Map;

import org.eclipse.gef.SharedImages;
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
 * Creates a new MarqueeToolEntry that can select nodes
 */
public MarqueeToolEntry() {
	this(null, null);
}

/**
 * Constructor for MarqueeToolEntry.
 * @param label the label
 */
public MarqueeToolEntry(String label) {
	this(label, null);
}

/**
 * Constructor for MarqueeToolEntry.
 * @param label the label; can be <code>null</code>
 * @param description the description (can be <code>null</code>)
 */
public MarqueeToolEntry(String label, String description) {
	super(label, description, SharedImages.DESC_MARQUEE_TOOL_16, 
			SharedImages.DESC_MARQUEE_TOOL_24, MarqueeSelectionTool.class);
	if (label == null || label.length() == 0)
		setLabel(GEFMessages.MarqueeTool_Label);
	setUserModificationPermission(PERMISSION_NO_MODIFICATION);
}

/**
 * @see org.eclipse.gef.palette.PaletteEntry#getDescription()
 */
public String getDescription() {
	String desc = super.getDescription();
	if (desc == null) {
		desc = GEFMessages.MarqueeTool_Nodes_Desc;
		Map properties = getToolProperties();
		if (properties != null) {
			Object value = properties.get(MarqueeSelectionTool.PROPERTY_SELECTION_TYPE);
			if (value instanceof Integer) {
				int selectionType = ((Integer)value).intValue() 
						& (MarqueeSelectionTool.SELECT_CONNECTIONS 
						| MarqueeSelectionTool.SELECT_NODES);
				if (selectionType == MarqueeSelectionTool.SELECT_CONNECTIONS)
					desc = GEFMessages.MarqueeTool_Connections_Desc;
				else if (selectionType != MarqueeSelectionTool.SELECT_NODES)
					desc = GEFMessages.MarqueeTool_Desc;
			}
		}
	}
	return desc;
}

}