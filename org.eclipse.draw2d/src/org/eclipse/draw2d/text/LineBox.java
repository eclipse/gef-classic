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

/**
 *  * @see org.eclipse.draw2d.text.FlowBox#getAscent() */
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

public int getBaseline() {
	return y + getAscent();
}

public void makeBaseline(int value) {
	super.makeBaseline(value);
	commit();
}

protected void resetInfo() {
	super.resetInfo();
	ascent = 0;
}

protected void unionInfo(FlowBox blockInfo) {
	int descent = height - ascent;
	ascent = Math.max(ascent, blockInfo.getAscent());
	height = ascent + Math.max(descent, blockInfo.getDescent());
	width += blockInfo.width;
}

}