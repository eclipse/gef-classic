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
 * A composite box representing a single line. LineBox calculates its ascent and descent
 * from the child boxes it contains. Clients can call {@link #getAscent()} or {@link
 * #getHeight()} at any time and expect valid values. The child boxes that are added to a
 * line have unspecied locations until {@link #commit()} is called, at which time the
 * child boxes are layed out in left-to-right order, and their baselines are all aligned
 * vertically.
 * @author hudsonr
 * @since 2.1 */
public class LineBox
	extends CompositeBox
{

private int ascent = 0;

/**
 * Committing a LineBox will position its children correctly. All children boxes are made
 * to have the same baseline, and are layed out from left-to-right.
 */
public void commit() {
	int baseline = getBaseline();
	int xLocation = x;
	for (int i = 0; i < fragments.size(); i++) {
		FlowBox block = (FlowBox)fragments.get(i);
		block.x = xLocation;
		xLocation += block.width;
		block.makeBaseline(baseline);
	}
}

/** * @see org.eclipse.draw2d.text.FlowBox#getAscent() */
public int getAscent() {
	return ascent;
}

/**
 * Returns the width available to child fragments.
 * @return the width in pixels
 */
public int getAvailableWidth() {
	if (recommendedWidth < 0)
		return Integer.MAX_VALUE;
	return recommendedWidth - width;
}

/**
 * Returns the baseline of this LineBox, which is the y value plus the ascent.
 * @return the baseline value.
 */
public int getBaseline() {
	return y + getAscent();
}

/**
 * @see FlowBox#makeBaseline(int)
 */
public void makeBaseline(int value) {
	super.makeBaseline(value);
	commit();
}

/**
 * @see CompositeBox#resetInfo()
 */
protected void resetInfo() {
	super.resetInfo();
	ascent = 0;
}

/**
 * @see CompositeBox#unionInfo(FlowBox)
 */
protected void unionInfo(FlowBox blockInfo) {
	int descent = height - ascent;
	ascent = Math.max(ascent, blockInfo.getAscent());
	height = ascent + Math.max(descent, blockInfo.getDescent());
	width += blockInfo.width;
}

}