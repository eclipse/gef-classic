package org.eclipse.gef.palette;

import org.eclipse.gef.SharedImages;
import org.eclipse.gef.Tool;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.tools.SelectionTool;

/**
 * @author hudsonr
 * @since 2.1
 */
public class SelectionToolEntry extends ToolEntry {

public SelectionToolEntry() {
	this(GEFMessages.SelectionTool_Label);
}

/**
 * Constructor for SelectionToolEntry.
 * @param label the label
 */
public SelectionToolEntry(String label) {
	this(label, null);
}

/**
 * Constructor for SelectionToolEntry.
 * @param label the label
 * @param shortDesc the description
 */
public SelectionToolEntry(String label, String shortDesc) {
	super(
		label,
		shortDesc,
		SharedImages.DESC_SELECTION_TOOL_16,
		SharedImages.DESC_SELECTION_TOOL_24);
}

/**
 * @see org.eclipse.gef.palette.ToolEntry#createTool()
 */
public Tool createTool() {
	return new SelectionTool();
}

}
