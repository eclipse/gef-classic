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

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

/**
 * @author hudsonr
 * @since 2.1
 */
public class CreationToolEntry extends ToolEntry {

/**
 * The creation factory used with the returned creation tool.
 */
protected final CreationFactory factory;

/**
 * Constructor for CreationToolEntry.
 * @param label the label
 * @param shortDesc the description
 * @param factory the CreationFactory
 * @param iconSmall the small icon
 * @param iconLarge the large icon
 */
public CreationToolEntry(
	String label,
	String shortDesc,
	CreationFactory factory,
	ImageDescriptor iconSmall,
	ImageDescriptor iconLarge) {

	super(label, shortDesc, iconSmall, iconLarge);
	this.factory = factory;
}

/**
 * @see org.eclipse.gef.palette.ToolEntry#createTool()
 */
public Tool createTool() {
	return new CreationTool(factory);
}

}
