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
package org.eclipse.draw2dl.text;

import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * A CompositeBox suitable for containing multiple LineBox fragments.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class BlockBox extends org.eclipse.draw2dl.text.CompositeBox {

	int height;
	private int y;
	org.eclipse.draw2dl.text.BlockFlow owner;

	BlockBox(BlockFlow owner) {
		this.owner = owner;
	}

	/**
	 * @see org.eclipse.draw2dl.text.CompositeBox#add(org.eclipse.draw2dl.text.FlowBox)
	 */
	public void add(org.eclipse.draw2dl.text.FlowBox box) {
		width = Math.max(width, box.getWidth());
		height = Math.max(height, box.getBaseline() + box.getDescent());
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBox#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return true;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBox#getAscent()
	 */
	public int getAscent() {
		return 0;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowBox#getBaseline()
	 */
	public int getBaseline() {
		return y;
	}

	int getBottomMargin() {
		return owner.getBottomMargin();
	}

	/**
	 * @see FlowBox#getDescent()
	 */
	public int getDescent() {
		return height;
	}

	/**
	 * @return Returns the height.
	 */
	public int getHeight() {
		return height;
	}

	LineRoot getLineRoot() {
		return null;
	}

	int getTopMargin() {
		return owner.getTopMargin();
	}

	/**
	 * Sets the height.
	 * 
	 * @param h
	 *            The height
	 */
	public void setHeight(int h) {
		height = h;
	}

	/**
	 * @see CompositeBox#setLineTop(int)
	 */
	public void setLineTop(int y) {
		this.y = y;
	}

	Rectangle toRectangle() {
		return new Rectangle(getX(), y, Math.max(getWidth(), recommendedWidth),
				height);
	}

}
