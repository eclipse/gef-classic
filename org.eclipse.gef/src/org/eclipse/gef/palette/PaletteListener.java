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

import java.util.EventListener;

import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * Listens to changes in the palette.
 */
public interface PaletteListener extends EventListener {

	/**
	 * A new tool was activated in the palette.
	 *
	 * @param palette the source of the change
	 * @param tool    the new tool that was activated
	 */
	void activeToolChanged(PaletteViewer palette, ToolEntry tool);

}
