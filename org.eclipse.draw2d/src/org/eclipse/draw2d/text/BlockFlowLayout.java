package org.eclipse.draw2d.text;

import java.util.*;
import org.eclipse.draw2d.*;

/**
 * The layout for {@link BlockFlow} figures.
 * @author hudsonr
 * @since 2.1 */
public class BlockFlowLayout
	extends FlowContainerLayout
{

private int previousRecommendedWidth = -1;
private LineBox
	previousLine = null;

BlockBox blockBox;

public BlockFlowLayout(BlockFlow blockFlow) {
	super(blockFlow);
}

protected void cleanup() {
	currentLine = previousLine = null;
}

protected void createNewLine() {
	currentLine = new LineBox();
	setupLine(currentLine);
}

protected void endBlock() {
	context.addToCurrentLine(blockBox);
	context.endLine();
}

public void endLine() {
	//If there is no current line, state is equivalent to new line
	if (currentLine == null)
		return;
	if (currentLine.isOccupied())
		layoutLine();
	else
		return;
	LineBox box = currentLine;
	currentLine = previousLine;
	previousLine = box;

	setupLine(getCurrentLine());
}

/**
 * @see org.eclipse.draw2d.text.FlowContainerLayout#layoutChildren()
 */
protected void layoutChildren() {
//	boolean invalidate = invalid
//		|| blockBox.getRecommendedWidth() != previousRecommendedWidth;
	previousRecommendedWidth = blockBox.getRecommendedWidth();
	List children = getFlowFigure().getChildren();
	for (int i = 0; i < children.size(); i++) {
		Figure f = (Figure)children.get(i);
//		if (invalidate)
			f.invalidate();
		f.validate();
	}
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#getCurrentY()
 */
public int getCurrentY() {
	return getCurrentLine().y;
}

protected final BlockFlow getBlockFlow() {
	return (BlockFlow)getFlowFigure();
}

/**
 * Adjust all fragments in the current line to have the same baseline.
 * Do any additional adjustments, such as horizontal alignment.
 */
protected void layoutLine() {
	currentLine.x = 0;
	switch (getBlockFlow().getHorizontalAligment()) {
		case PositionConstants.RIGHT :
			currentLine.x  = blockBox.getRecommendedWidth() - currentLine.getWidth();
			break;
		case PositionConstants.CENTER :
			currentLine.x  = (blockBox.getRecommendedWidth() - currentLine.getWidth()) / 2;
			break;
	}
	currentLine.commit();
	blockBox.add(currentLine);
}

protected void flush() {
	if (currentLine != null)
		layoutLine();
	endBlock();
}

protected void preLayout() {
	blockBox = getBlockFlow().getBlockBox();
	setupBlock();
	//Probably could setup current and previous line here, or just previous
}

/**
 * sets up the single block that contains all of the lines.
 */
protected void setupBlock() {
	//Ask for a new line, in case we are in the middle of a line
	context.endLine();
	LineBox line = context.getCurrentLine();
//	int recommended = line.getAvailableWidth();
//	if (recommended != previousRecommendedWidth)
		//Remove all current Fragments
		blockBox.clear();

	//Setup the one fragment for this Block with the correct X and available width
	blockBox.setRecommendedWidth(line.getAvailableWidth());
	blockBox.y = context.getCurrentY();
	blockBox.x = 0;
}

/**
 * Override to setup the line's x, remaining, and available width
 */
protected void setupLine(LineBox line) {
	line.clear();
	line.setRecommendedWidth(blockBox.getRecommendedWidth());
	if (previousLine == null) {
		line.y = 0;
	} else {
		line.y = previousLine.y + previousLine.getHeight();
	}
//	line.validate();
}

}