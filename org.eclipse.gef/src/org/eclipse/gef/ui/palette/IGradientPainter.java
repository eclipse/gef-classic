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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.ui.palette.editparts.DrawerFigure;

/**
 * This interface can be used together with the {@link IDrawerFigure} and
 * {@link PaletteCustomizer}, to adapt the the background painted in a
 * {@link DrawerFigure}.
 *
 * @since 3.20
 */
public interface IGradientPainter {
	/**
	 * Paints the background gradient on the drawer toggle figure.
	 *
	 * @param g    the graphics object
	 * @param rect the rectangle which the background gradient should cover
	 */
	void paint(Graphics g, Rectangle rect);
}
