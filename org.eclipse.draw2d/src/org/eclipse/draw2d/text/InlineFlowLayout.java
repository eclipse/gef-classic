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
 * The layout manager for {@link InlineFlow} figures.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 * @author hudsonr
 * @since 2.1 */
public class InlineFlowLayout
	extends FlowContainerLayout
{

/**
 * Creates a new InlineFlowLayout with the given FlowFigure.
 * @param flow The FlowFigure
 */
public InlineFlowLayout(FlowFigure flow) {
	super(flow);
}

/**
 * Adds the given FlowBox to the current line of this InlineFlowLayout.
 * @param block the FlowBox to add to the current line
 */
public void addToCurrentLine(FlowBox block) {
	super.addToCurrentLine(block);
	((InlineFlow)getFlowFigure()).getFragments().add(currentLine);
}

/**
 * @see FlowContainerLayout#createNewLine()
 */
protected void createNewLine() {
	currentLine = new LineBox();
	setupLine(currentLine);
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#endLine()
 */
public void endLine() {
	if (currentLine == null)
		return;
	//If nothing was ever placed in the line, ignore it.
	if (currentLine.isOccupied())
		getContext().addToCurrentLine(currentLine);
	getContext().endLine();
	currentLine = null;
}

/**
 * @see FlowContainerLayout#flush()
 */
protected void flush() {
	if (currentLine != null) {
		// We want to preserve the state when a linebox is being added
		boolean sameLine = getContext().getContinueOnSameLine();
		getContext().addToCurrentLine(currentLine);
		getContext().setContinueOnSameLine(sameLine);
	}
}

/**
 * InlineFlowLayout gets this information from its context.
 * @see org.eclipse.draw2d.text.FlowContext#getContinueOnSameLine()
 */
public boolean getContinueOnSameLine() {
	return getContext().getContinueOnSameLine();
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#getCurrentY()
 */
public int getCurrentY() {
	return getCurrentLine().y;
}

/**
 * tried to find word termination first among children
 * @see FlowContext#getWordWidthFollowing(FlowFigure, int[])
 */
public boolean getWordWidthFollowing(FlowFigure child, int[] width) {
	boolean result = super.getWordWidthFollowing(child, width); 
	if (!result && getContext() != null)
		return getContext().getWordWidthFollowing(getFlowFigure(), width);
	return result;
}

/**
 * @see org.eclipse.draw2d.text.FlowContainerLayout#isCurrentLineOccupied()
 */
public boolean isCurrentLineOccupied() {
	return (currentLine != null && !currentLine.getFragments().isEmpty())
		|| getContext().isCurrentLineOccupied();
}

/**
 * Clears out all fragments prior to the call to layoutChildren().
 */
public void preLayout() {
	((InlineFlow)getFlowFigure()).getFragments().clear();
}

/**
 * InlineFlow passes this information to its context.
 * @see org.eclipse.draw2d.text.FlowContext#setContinueOnSameLine(boolean)
 */
public void setContinueOnSameLine(boolean value) {
	getContext().setContinueOnSameLine(value);
}

/**
 * Initializes the given LineBox. Called by createNewLine().
 * @param line The LineBox to initialize.
 */
protected void setupLine(LineBox line) {
	LineBox parent = getContext().getCurrentLine();
	line.x = 0;
	line.y = getContext().getCurrentY();
	line.setRecommendedWidth(parent.getAvailableWidth());
}

}