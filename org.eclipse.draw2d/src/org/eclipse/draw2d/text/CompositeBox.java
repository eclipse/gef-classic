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

import java.util.*;

/**
 * A FlowBox that can contain other BlockInfos. The contained BlockInfos are called
 * <i>fragments</i>.
 * @author hudsonr
 * @since 2.1 */
public abstract class CompositeBox
	extends FlowBox
{

/**
 * The contained fragments.
 */
protected List fragments = new ArrayList();
int recommendedWidth;

/**
 * Adds the specified FlowBox. Updates the width, height, and ascent properties.
 * @param block the FlowBox being added */
public void add(FlowBox block) {
	fragments.add(block);
	unionInfo(block);
}

/**
 * Removes all owned fragments and invalidates this CompositeBox.
 */
public void clear() {
	fragments.clear();
	resetInfo();
}

/**
 * Overridden to ensure that the CompositeBox is valid.
 * @see FlowBox#getBounds() */
//public Rectangle getBounds() {
//	validate();
//	return this;
//}

/** * @return the List of fragments */
public List getFragments() {
	return fragments;
}

/**
 * Returns the recommended width for this CompositeBox.
 * @return the recommended width
 */
public int getRecommendedWidth() {
	return recommendedWidth;
}

//public int getInnerTop() {
//	validate();
//	return y;
//}

/** * @see org.eclipse.draw2d.geometry.Rectangle#isEmpty() */
public boolean isOccupied() {
	return !fragments.isEmpty();
}

/**
 * resets fields before unioning the data from the fragments.
 */
protected void resetInfo() {
	width = height = 0;
}

/**
 * Sets the recommended width for this CompositeBox.
 * @param w the width */
public void setRecommendedWidth(int w) {
	recommendedWidth = w;
}

/**
 * unions the fragment's width, height, and ascent into this composite.
 * @param box the fragment */
protected void unionInfo(FlowBox box) {
	int right = Math.max(x + width, box.x + box.width);
	int bottom = Math.max(y + height, box.y + box.height);
	x = Math.min(x, box.x);
	y = Math.min(y, box.y);
	width = right - x;
	height = bottom - y;
}

}