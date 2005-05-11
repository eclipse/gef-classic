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

import org.eclipse.draw2d.geometry.Translatable;

/**
 * Stores positional information about where a caret should be placed. This structure
 * currently only offers integer precision. Scaling operations will result in rounding.
 * @since 3.1
 */
public class CaretInfo implements Translatable {
private int ascent;
private int baseline;
private int descent;

private int x;

/**
 * Constructs a new instance.
 * @param x the x location
 * @param y the y location of the top of the caret
 * @param ascent the ascent
 * @param descent the descent
 * @since 3.1
 */
public CaretInfo(int x, int y, int ascent, int descent) {
	this.x = x;
	this.baseline = y + ascent;
	this.ascent = ascent;
	this.descent = descent;
}

/**
 * Returns the y location of the baseline.
 * @return the y coordinate of the baseline
 * @since 3.1
 */
public int getBaseline() {
	return baseline;
}

/**
 * Returns the total height of the caret. The height is the sum of the ascent and descent.
 * @return the height
 * @since 3.1
 */
public int getHeight() {
	return ascent + descent;
}

/**
 * Returns the x location of the caret;
 * @return the x coordinate
 * @since 3.1
 */
public int getX() {
	return x;
}

/**
 * Returns the y location of the caret;
 * @return the y coordinate
 * @since 3.1
 */
public int getY() {
	return baseline - ascent;
}

/**
 * @see Translatable#performScale(double)
 */
public void performScale(double factor) {
	x *= factor;
	baseline *= factor;
	descent *= factor;
	ascent *= factor;
}

/**
 * @see Translatable#performTranslate(int, int)
 */
public void performTranslate(int dx, int dy) {
	x += dx;
	baseline += dy;
}

/**
 * Returns the y coordinate of the top of the caret.
 * @return the top location
 * @since 3.1
 */
public int top() {
	return baseline - ascent;
}

}
