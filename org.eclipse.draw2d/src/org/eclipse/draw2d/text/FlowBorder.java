/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.text;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @since 3.1
 */
public interface FlowBorder extends Border {

/**
 * A stub implementation of FlowBorder.
 * @since 3.1
 */
class Stub implements FlowBorder {
	public int getBottomMargin() {
		return 0;
	}

	public Insets getInsets(IFigure figure) {
		return IFigure.NO_INSETS;
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBorder#getLeftMargin()
	 */
	public int getLeftMargin() {
		return 0;
	}

	public Dimension getPreferredSize(IFigure figure) {
		return null;
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBorder#getRightMargin()
	 */
	public int getRightMargin() {
		return 0;
	}

	public int getTopMargin() {
		return 0;
	}

	public boolean isOpaque() {
		return true;
	}

	public void paint(FlowFigure figure, Graphics g, Rectangle where, boolean left, boolean right) {
	}

	public final void paint(IFigure figure, Graphics graphics, Insets insets) { }
	
}

/**
 * Returns the collapsable bottom margin in pixels.  Margin is space outside of the bounds
 * and content area of a figure.  Vertical margins (top and bottom) may collapse in some
 * situations described by the CSS2 specification.
 * @return the bottom margin
 * @since 3.1
 */
int getBottomMargin();

Insets getInsets(IFigure figure);

int getLeftMargin();

Dimension getPreferredSize(IFigure figure);

int getRightMargin();

/**
 * Returns the collapsable top margin in pixels.
 * @see #getBottomMargin()
 * @return the top margin
 * @since 3.1
 */
int getTopMargin();

/**
 * Returns true if clipping of the GC is not required when painting the figure's client
 * area.
 * @see Border#isOpaque()
 */
boolean isOpaque();

/**
 * Paints the border around the given fragment.
 * @param figure the flow figure whose border is being painted
 * @param g the graphics
 * @param box the fragment being rendered
 * @param left <code>true</code> if the left side should be rendered
 * @param right <code>true</code> if the right side should be rendered
 * @since 3.1
 */
void paint(FlowFigure figure, Graphics g, Rectangle where, boolean left, boolean right);

}
