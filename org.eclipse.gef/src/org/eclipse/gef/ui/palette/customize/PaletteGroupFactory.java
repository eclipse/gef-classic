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
package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * Factory to create {@link org.eclipse.gef.palette.PaletteGroup groups}
 * 
 * @author Pratik Shah
 */
public class PaletteGroupFactory extends PaletteContainerFactory {

	/**
	 * Constructor
	 */
	public PaletteGroupFactory() {
		setLabel(PaletteMessages.MODEL_TYPE_GROUP);
	}

	/**
	 * @see org.eclipse.gef.ui.palette.customize.PaletteEntryFactory#createNewEntry(Shell)
	 */
	@Override
	protected PaletteEntry createNewEntry(Shell shell) {
		PaletteGroup group = new PaletteGroup(PaletteMessages.NEW_GROUP_LABEL);
		group.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		return group;
	}

}
