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
boolean invalid = true;
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
	invalidate();
}

/**
 * Returns the width available to child fragments.
 * @return the width in pixels */
public abstract int getAvailableWidth();

/**
 * Overridden to unsure that the CompositeBox is valid.
 * @see FlowBox#getBounds() */
//public Rectangle getBounds() {
//	validate();
//	return this;
//}

/** * @return the List of fragments */
public List getFragments() {
	return fragments;
}

/** * @see FlowBox#getHeight() */
public int getHeight() {
	validate();
	return height;
}

//public int getInnerTop() {
//	validate();
//	return y;
//}

/**
 * Marks this composite as needing validation.
 * @see #validate()
 */
protected void invalidate() {
	invalid = true;
}

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
 * @param blockInfo the fragment */
protected void unionInfo(FlowBox box) {
	int right = Math.max(x + width, box.x + box.width);
	int bottom = Math.max(y + height, box.y + box.height);
	x = Math.min(x, box.x);
	y = Math.min(y, box.y);
	width = right - x;
	height = bottom - y;
}

/**
 * updates the overall width, height, and ascent for this CompositeBox.
 */
public void validate() {
	if (!invalid)
		return;
	invalid = false;
	resetInfo();
	for (int i = 0; i < fragments.size(); i++)
		unionInfo((FlowBox)fragments.get(i));
}

}