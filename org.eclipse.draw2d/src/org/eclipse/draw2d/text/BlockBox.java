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
	return new Rectangle(x, y, Math.max(width,recommendedWidth), height);
}

}