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
package org.eclipse.gef.tools;

import org.eclipse.gef.EditPart;

/**
 * Specializes selection to do nothing, the native Tree provides selection for
 * free.
 *
 * @author hudsonr
 */
public class DragTreeItemsTracker extends SelectEditPartTracker {

	/**
	 * Constructs a new DragTreeItemsTracker.
	 *
	 * @param sourceEditPart the source edit part
	 */
	public DragTreeItemsTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	@Override
	protected String getDebugName() {
		return "Tree Tracker: " + getCommandName();//$NON-NLS-1$
	}

	/**
	 * Does nothing. The native tree provides selection for free.
	 *
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
	 */
	@Override
	protected void performSelection() {
	}

}
