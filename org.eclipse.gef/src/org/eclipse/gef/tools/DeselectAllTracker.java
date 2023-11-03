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
 * A DragTracker whose job it is to deselect all {@link EditPart EditParts}.
 */
public class DeselectAllTracker extends SelectEditPartTracker {

	/**
	 * Constructs a new DeselectAllTracker.
	 *
	 * @param ep the edit part that returned this tracker
	 */
	public DeselectAllTracker(EditPart ep) {
		super(ep);
	}

	/**
	 * Calls {@link org.eclipse.gef.EditPartViewer#deselectAll()}.
	 *
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
	 */
	@Override
	protected boolean handleButtonDown(int button) {
		getCurrentViewer().deselectAll();
		return true;
	}

}
