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

import java.util.List;

import org.eclipse.swt.SWT;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PositionConstants;

/**
 * The layout for {@link BlockFlow} figures.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 * @author hudsonr
 * @since 2.1 */
public class BlockFlowLayout
	extends FlowContainerLayout
{

BlockBox blockBox;
private boolean continueOnSameLine = false;
private LineBox previousLine = null;
boolean blockInvalid = false;

/**
 * Creates a new BlockFlowLayout with the given BlockFlow.
 * @param blockFlow the BlockFlow
 */
public BlockFlowLayout(BlockFlow blockFlow) {
	super(blockFlow);
}

/**
 * Marks the blocks contents as changed.  This means that children will be invalidated
 * during validation.
 * @since 3.1
 */
public void blockContentsChanged() {
	blockInvalid = true;
}

/**
 * @see FlowContainerLayout#cleanup()
 */
protected void cleanup() {
	super.cleanup();
	currentLine = previousLine = null;
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#getContinueOnSameLine()
 */
public boolean getContinueOnSameLine() {
	return continueOnSameLine;
}

/**
 * @see FlowContainerLayout#createNewLine()
 */
protected void createNewLine() {
	currentLine = new LineBox();
	setupLine(currentLine);
}

/**
 * Called by flush(), adds the BlockBox associated with this BlockFlowLayout
 * to the current line and then ends the line.
 */
protected void endBlock() {
	if (getContext() != null) {
		getContext().addToCurrentLine(blockBox);
		getContext().endLine();
	}
	if (blockInvalid) {
		blockInvalid = false;
		List v = getFlowFigure().getChildren();
		for (int i = 0; i < v.size(); i++)
			((FlowFigure)v.get(i)).postValidate();
	}
}

/**
 * @see FlowContext#endLine()
 */
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
 * @see org.eclipse.draw2d.text.FlowContext#getCurrentY()
 */
public int getCurrentY() {
	return getCurrentLine().y;
}

/**
 * Returns the BlockFlow associated with this BlockFlowLayout
 * @return the BlockFlow
 */
protected final BlockFlow getBlockFlow() {
	return (BlockFlow)getFlowFigure();
}

/**
 * Align the line horizontally and then commit it.
 */
protected void layoutLine() {
	// align the current line
	currentLine.x = 0;
	int alignment = getBlockFlow().getHorizontalAligment();
	if (getBlockFlow().getOrientation() == SWT.RIGHT_TO_LEFT) {
		if (alignment == PositionConstants.LEFT)
			alignment = PositionConstants.RIGHT;
		else if (alignment == PositionConstants.RIGHT)
			alignment = PositionConstants.LEFT;
	}
	switch (alignment) {
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

boolean forceChildInvalidation(Figure f) {
	return blockInvalid;
}

/**
 * @see FlowContainerLayout#flush()
 */
protected void flush() {
	if (currentLine != null)
		layoutLine();
	endBlock();
}

/**
 * @see FlowContainerLayout#preLayout()
 */
protected void preLayout() {
	setContinueOnSameLine(false);
	blockBox = getBlockFlow().getBlockBox();
	setupBlock();
	//Probably could setup current and previous line here, or just previous
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#setContinueOnSameLine(boolean)
 */
public void setContinueOnSameLine(boolean value) {
	continueOnSameLine = value;
}

/**
 * sets up the single block that contains all of the lines.
 */
protected void setupBlock() {
	//Ask for a new line, in case we are in the middle of a line
	getContext().endLine();
	LineBox line = getContext().getCurrentLine();
	int recommended = line.getAvailableWidth();
	if (recommended != blockBox.recommendedWidth) {
		blockInvalid = true;
		blockBox.clear();
		blockBox.setRecommendedWidth(recommended);
	}
	//Setup the one fragment for this Block with the correct X and available width
	blockBox.y = getContext().getCurrentY();
	blockBox.x = 0;
}

/**
 * Override to setup the line's x, remaining, and available width.
 * @param line the LineBox to set up
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