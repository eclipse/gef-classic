package org.eclipse.draw2d.text;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A CompositeBox suitable for containing multiple LineBox fragments.
 * @author hudsonr
 * @since 2.1 */
public class BlockBox
	extends CompositeBox
{

public int getAvailableWidth() {
	return recommendedWidth;
}

Rectangle toRectangle() {
	validate();
	return new Rectangle(x, y, width, height);
}

/**
 * @see org.eclipse.draw2d.text.CompositeBox#unionInfo(FlowBox)
 */
protected void unionInfo(FlowBox box) {
	width = Math.max(width, box.x + box.width);
	height = Math.max(height, box.y + box.height);
}

}