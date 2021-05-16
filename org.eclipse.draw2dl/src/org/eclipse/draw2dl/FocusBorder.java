/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl;

import org.eclipse.draw2dl.geometry.Insets;

/**
 * A Border that looks like the system's focus rectangle.
 */
public class FocusBorder extends AbstractBorder {

	/**
	 * Constructs a new FocusBorder.
	 */
	public FocusBorder() {
	}

	/**
	 * @see Border#getInsets(org.eclipse.draw2dl.IFigure)
	 */
	public Insets getInsets(org.eclipse.draw2dl.IFigure figure) {
		return new Insets(1);
	}

	/**
	 * @see Border#isOpaque()
	 */
	public boolean isOpaque() {
		return true;
	}

	/**
	 * Paints a focus rectangle.
	 * 
	 * @see Border#paint(org.eclipse.draw2dl.IFigure, org.eclipse.draw2dl.Graphics, Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		tempRect.setBounds(getPaintRectangle(figure, insets));
		tempRect.width--;
		tempRect.height--;
		graphics.setForegroundColor(org.eclipse.draw2dl.ColorConstants.black);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.drawFocus(tempRect);
	}

}
