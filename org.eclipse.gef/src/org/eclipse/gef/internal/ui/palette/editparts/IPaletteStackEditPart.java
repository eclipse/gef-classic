/*******************************************************************************
 * Copyright (c) 2008, 2010 IBM Corporation and others.
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

package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * An interface to define the common behavior between all palette stack
 * editparts (i.e. the type on the palette toolbar and the type in a drawer or
 * group on the palette pane).
 *
 * @author crevells
 * @since 3.4
 */
public interface IPaletteStackEditPart {

	/**
	 * Opens/expands the palette stack.
	 */
	void openMenu();

	/**
	 * Returns the active palette entry editpart in the stack.
	 *
	 * @return the active part
	 */
	PaletteEditPart getActiveEntry();
}
