package org.eclipse.draw2d.text;

import java.util.List;

import org.eclipse.draw2d.PositionConstants;

/**
 * A <code>FlowFigure</code> represented by a single {@link BlockBox} fragment containing
 * one or more lines. A BlockFlow is a creator of LineBoxes, which its children require
 * during layout. A BlockFlow can be thought of as a paragraph.
 * <P>
 * BlockFlows should be nested inside other BlockFlows, but it is also valid to place then
 * in InlineFlows. {@link FlowPage} can be used as a "root" block and can be added to
 * normal draw2d Figures.
 * <P>
 * Only {@link FlowFigure}s can be added to a BlockFlow.
 * @author hudsonr
 * @since 2.1
 */
public class BlockFlow extends FlowFigure {

final BlockBox blockBox;

private int aligment;

/**
 * Constructs a new BlockFlow. */
public BlockFlow() {
	blockBox = createBlockBox();
}

BlockBox createBlockBox() {
	return new BlockBox();
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return new BlockFlowLayout(this);
}

BlockBox getBlockBox() {
	return blockBox;
}

/**
 * Returns the horizontal aligment.
 * @return the hotizontal aligment
 */
public int getHorizontalAligment() {
	return aligment & PositionConstants.LEFT_CENTER_RIGHT;
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#postValidate()
 */
public void postValidate() {
	setBounds(getBlockBox().toRectangle().expand(getInsets()));
	List v = getChildren();
	for (int i = 0; i < v.size(); i++)
		((FlowFigure)v.get(i)).postValidate();
}

/**
 * Sets the horitontal aligment of the block. Valid values are:
 * <UL>
 *   <LI>{@link org.eclipse.draw2d.PositionConstants#LEFT}
 *   <LI>{@link org.eclip
 * @param value the aligment */
public void setHorizontalAligment(int value) {
	if (!(value == PositionConstants.LEFT
		|| value == PositionConstants.RIGHT
		|| value == PositionConstants.CENTER))
		throw new IllegalArgumentException(
			"Horizontal Aligment must be one of: LEFT, CENTER, RIGHT");//$NON-NLS-1$
	this.aligment &= ~PositionConstants.LEFT_CENTER_RIGHT;
	this.aligment |= value;
	revalidate();
}

/**
 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
 */
protected boolean useLocalCoordinates() {
	return true;
}

}
