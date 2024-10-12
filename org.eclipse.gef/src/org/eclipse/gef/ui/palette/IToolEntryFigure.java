/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.ui.palette;

import org.eclipse.draw2d.IFigure;

/**
 * Public interface of the
 * {@link org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart.ToolEntryToggle}.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IToolEntryFigure extends IFigure {
	void setColorPalette(IColorPalette colorPalette);
}
