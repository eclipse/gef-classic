/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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

Rectangle toRectangle() {
	return new Rectangle(x, y, Math.max(width, recommendedWidth), height);
}

public void setHeight(int h) {
	height = h;
}

protected void unionInfo(FlowBox box) {
	width = Math.max(width, box.width);
	height = Math.max(height, box.y + box.height);
}

}