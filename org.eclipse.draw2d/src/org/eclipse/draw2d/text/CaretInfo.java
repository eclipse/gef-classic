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

public int getBaseline() {
	return baseline;
}

public int getHeight() {
	return ascent + descent;
}

public int getX() {
	return x;
}

public void performScale(double factor) {
	x *= factor;
	baseline *= factor;
	descent *= factor;
	ascent *= factor;
}

public void performTranslate(int dx, int dy) {
	x += dx;
	baseline += dy;
}

public int top() {
	return baseline - ascent;
}

}
