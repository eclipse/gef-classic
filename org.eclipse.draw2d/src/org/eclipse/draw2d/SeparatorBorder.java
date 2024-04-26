/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
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
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This border behaves similar to a {@link LineBorder} with width {@code 1},
 * except that individual sides are drawn. The sides are specified via
 * {@link PositionConstants#LEFT}, {@link PositionConstants#RIGHT},
 * {@link PositionConstants#TOP} and {@link PositionConstants#BOTTOM}. Multiple
 * sides can be specified by logically or-ing these constants.
 *
 * @since 3.16
 */
public final class SeparatorBorder extends MarginBorder {
	private final int position;
	private Color color;

	/**
	 * The sides are specified via {@link PositionConstants#LEFT},
	 * {@link PositionConstants#RIGHT}, {@link PositionConstants#TOP},
	 * {@link PositionConstants#BOTTOM}. Multiple sides can be specified by
	 * logically or-ing these constants.
	 *
	 * @param insets The Insets for the border
	 * @param sides  The integer-encoded sides that should be drawn.
	 * @see PositionConstants#LEFT
	 * @see PositionConstants#RIGHT
	 * @see PositionConstants#TOP
	 * @see PositionConstants#BOTTOM
	 */
	public SeparatorBorder(Insets insets, int sides) {
		super(insets);
		this.position = sides;
	}

	/**
	 * If {@code null} is given as a parameter, the current foreground color of the
	 * graphics object is used while painting.
	 *
	 * @param color The color used for drawing each side.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void paint(IFigure f, Graphics g, Insets i) {
		Rectangle r = getPaintRectangle(f, i);
		if (color != null) {
			g.setForegroundColor(color);
		}
		g.setLineWidth(1);
		g.setLineStyle(Graphics.LINE_SOLID);
		if ((position & PositionConstants.LEFT) > 0) {
			g.drawLine(r.getTopLeft(), r.getBottomLeft());
		}
		if ((position & PositionConstants.RIGHT) > 0) {
			r.width--;
			g.drawLine(r.getTopRight(), r.getBottomRight());
			r.width++;
		}
		if ((position & PositionConstants.TOP) > 0) {
			g.drawLine(r.getTopLeft(), r.getTopRight());
		}
		if ((position & PositionConstants.BOTTOM) > 0) {
			r.height--;
			g.drawLine(r.getBottomLeft(), r.getBottomRight());
			r.height++;
		}
	}
}
