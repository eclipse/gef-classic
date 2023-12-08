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

import org.eclipse.gef.DragTracker;

/**
 * A marqueeSelectionTool that implements the DragTracker interface.
 */
public class MarqueeDragTracker extends MarqueeSelectionTool implements DragTracker {

	/**
	 * Called when the mouse button is released. Overridden to do nothing, since a
	 * drag tracker does not need to unload when finished.
	 */
	@Override
	protected void handleFinished() {
	}

}
