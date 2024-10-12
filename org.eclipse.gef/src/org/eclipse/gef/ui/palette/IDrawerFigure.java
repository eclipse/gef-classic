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

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;

/**
 * Public interface of the {@link DrawerFigure}.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IDrawerFigure extends IFigure {
	void setGradientPainter(IGradientPainter painter);

	/**
	 * @return The {@link Clickable} that is used to expand/collapse the drawer.
	 */
	Clickable getCollapseToggle();

	/**
	 * @return The content pane for this figure, i.e. the Figure to which children
	 *         can be added.
	 */
	IFigure getContentPane();
}
