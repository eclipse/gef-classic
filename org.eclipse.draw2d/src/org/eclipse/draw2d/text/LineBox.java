package org.eclipse.draw2d.text;

public class LineBox
	extends CompositeBox
{

private int ascent = 0;

public void add(FlowBox block) {
	block.y = y;
	super.add(block);
}

/**
 * Committing a LineBox will adjust all children boxes to have the same baseline.
 */
public void commit() {
	int baseline = getBaseline();
	int xLocation = x;
	for (int i=0; i<fragments.size(); i++) {
		FlowBox block = (FlowBox)fragments.get(i);
		block.x = xLocation;
		xLocation += block.width;
		block.makeBaseline(baseline);
	}
}

public int getAscent() {
	validate();
	return ascent;
}

public int getAvailableWidth() {
	if (invalid)
		validate();
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