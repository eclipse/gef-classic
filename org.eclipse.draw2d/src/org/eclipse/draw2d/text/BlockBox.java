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

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A CompositeBox suitable for containing multiple LineBox fragments.
 * @author hudsonr
 * @since 2.1 */
public class BlockBox
	extends CompositeBox
{

/**
 * @see org.eclipse.draw2d.text.CompositeBox#add(FlowBox)
 */
public void add(FlowBox box) {
	unionInfo(box);
}

/**
 * A BlockBox will always return false for isBidi() since a block's contents are 
 * unaffected by their surroundings and vice versa.
 * @see org.eclipse.draw2d.text.FlowBox#requiresBidi()
 */
public boolean requiresBidi() {
	return false;
}

Rectangle toRectangle() {
	return new Rectangle(x, y, Math.max(width, recommendedWidth), height);
}

/**
 * Sets the height.
 * @param h The height
 */
public void setHeight(int h) {
	height = h;
}

/**
 * Unions the dimensions of this with the dimensions of the passed FlowBox.
 * @param box The FlowBox to union this with
 */
protected void unionInfo(FlowBox box) {
	width = Math.max(width, box.width);
	height = Math.max(height, box.y + box.height);
}

}