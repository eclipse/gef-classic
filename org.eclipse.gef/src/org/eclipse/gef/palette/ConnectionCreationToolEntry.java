package org.eclipse.gef.palette;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ConnectionCreationToolEntry extends CreationToolEntry {

/**
 * Constructor for ConnectionCreationToolEntry.
 * @param label the label
 * @param shortDesc the description
 * @param factory the CreationFactory
 * @param iconSmall the small icon
 * @param iconLarge the large icon
 */
public ConnectionCreationToolEntry(
	String label,
	String shortDesc,
	CreationFactory factory,
	ImageDescriptor iconSmall,
	ImageDescriptor iconLarge) {
	super(label, shortDesc, factory, iconSmall, iconLarge);
}

/**
 * @see org.eclipse.gef.palette.CreationToolEntry#createTool()
 */
public Tool createTool() {
	return new ConnectionCreationTool(factory);
}

}
