/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

/**
 * A Geometric object for representing a region on a line of Text. This class adds the
 * notion of a baseline to {@link org.eclipse.draw2d.geometry.Rectangle}. <i>Ascent</i> is
 * the distance above the baseline. <i>Descent</i> is the distance below the baseline.
 * <P>
 * This class should not be treated as a <code>Rectangle</code> by clients.
 * @author hudsonr
 * @since 2.1
 */
public class FlowBox {

/**
 * The x location
 */
public int x;
/**
 * The y location
 */
public int y;
int width;
int height;

/**
 * This method must be called on a block that is completely positioned and committed.
 * @param x X
 * @param y Y
 * @return <code>true</code> if the FlowBox contains the point */
public boolean containsPoint(int x, int y) {
	return x >= this.x
		&& y >= this.y
		&& x < this.x + this.width
		&& y < this.y + this.height;
}

/**
 * By default, a FlowBox is all ascent, and no descent, so the height is returned.
 * @return the <i>ascent</i> in pixels above the baseline
 */
public int getAscent() {
	return getHeight();
}

/**
 * By default, a simple FlowBox is all ascent, and no descent. Zero is returned.
 * @return the <i>descent</i> in pixels below the baseline
 */
public final int getDescent() {
	return getHeight() - getAscent();
}

/**
 * Returns the height
 * @return height
 */
public int getHeight() {
	return height;
}

/**
 * Returns the width
 * @return width */
public int getWidth() {
	return width;
}

/**
 * Used to set the baseline of this FlowBox to the specified value.
 * @param value the new baseline
 */
public void makeBaseline(int value) {
	y = (value - getAscent());
}

}