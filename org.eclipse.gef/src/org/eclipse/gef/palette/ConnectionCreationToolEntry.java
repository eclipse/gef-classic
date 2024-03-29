/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.palette;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.ConnectionCreationTool;

/**
 * A palette ToolEntry for a
 * {@link org.eclipse.gef.tools.ConnectionCreationTool}.
 *
 * @author hudsonr
 * @since 2.1
 */
public class ConnectionCreationToolEntry extends CreationToolEntry {

	/**
	 * Constructor for ConnectionCreationToolEntry.
	 *
	 * @param label     the label
	 * @param shortDesc the description
	 * @param factory   the CreationFactory
	 * @param iconSmall the small icon
	 * @param iconLarge the large icon
	 */
	public ConnectionCreationToolEntry(String label, String shortDesc, CreationFactory factory,
			ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
		super(label, shortDesc, factory, iconSmall, iconLarge);
		setToolClass(ConnectionCreationTool.class);
		setUserModificationPermission(PERMISSION_NO_MODIFICATION);
	}

}
