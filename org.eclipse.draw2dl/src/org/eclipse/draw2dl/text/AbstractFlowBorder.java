/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.text;

import org.eclipse.draw2dl.AbstractBorder;
import org.eclipse.draw2dl.Border;
import org.eclipse.draw2dl.Graphics;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.geometry.Insets;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * A basis for implementing {@link org.eclipse.draw2dl.text.FlowBorder}. Subclassing this class will
 * possibly guarantee compatibility with future changes to the FlowBorder
 * interface. This class also returns default values for many of the required
 * methods as a convenience.
 * 
 * @since 3.1
 */
public abstract class AbstractFlowBorder extends AbstractBorder implements
    org.eclipse.draw2dl.text.FlowBorder {
	/**
	 * @see org.eclipse.draw2dl.text.FlowBorder#getBottomMargin()
	 */
	public int getBottomMargin() {
		return 0;
	}

	/**
	 * @see Border#getInsets(IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return IFigure.NO_INSETS;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBorder#getLeftMargin()
	 */
	public int getLeftMargin() {
		return 0;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBorder#getRightMargin()
	 */
	public int getRightMargin() {
		return 0;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBorder#getTopMargin()
	 */
	public int getTopMargin() {
		return 0;
	}

	/**
	 * This method is not called on FlowBorders. For this reason it is
	 * implemented here and made <code>final</code> so that clients override the
	 * correct method.
	 * 
	 * @param figure
	 *            the figure
	 * @param graphics
	 *            the graphics
	 * @param insets
	 *            the insets
	 * @see org.eclipse.draw2dl.text.FlowBorder#paint(org.eclipse.draw2dl.text.FlowFigure, Graphics, Rectangle, int)
	 */
	public final void paint(IFigure figure, Graphics graphics, Insets insets) {
	}

	/**
	 * Subclasses should override this method to paint each box's border.
	 * 
	 * @see FlowBorder#paint(org.eclipse.draw2dl.text.FlowFigure, Graphics, Rectangle, int)
	 */
	public void paint(FlowFigure figure, Graphics g, Rectangle where, int sides) {
	}

}