/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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

public InlineFlowLayout(FlowFigure flow) {
	super(flow);
}

public void addToCurrentLine(FlowBox block) {
	getCurrentLine().add(block);
	((InlineFlow)getFlowFigure()).getFragments().add(currentLine);
}

protected void createNewLine() {
	currentLine = new LineBox();
	setupLine(currentLine);
}

protected void cleanup() {
	currentLine = null;
}

protected void flush() {
	if (currentLine != null)
		context.addToCurrentLine(currentLine);
}

public void endLine() {
	if (currentLine == null)
		return;
	//If nothing was ever placed in the line, ignore it.
	if (currentLine.isOccupied())
		context.addToCurrentLine(currentLine);
	context.endLine();
	currentLine = null;
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#getCurrentY()
 */
public int getCurrentY() {
	return getCurrentLine().y;
}

/**
 * @see org.eclipse.draw2d.text.FlowContainerLayout#isCurrentLineOccupied()
 */
public boolean isCurrentLineOccupied() {
	return !currentLine.getFragments().isEmpty()
		|| context.isCurrentLineOccupied();
}

public void preLayout() {
	((InlineFlow)getFlowFigure()).getFragments().clear();
}

protected void setupLine(LineBox line) {
	LineBox parent = context.getCurrentLine();
	line.x = 0;
	line.y = context.getCurrentY();
	line.setRecommendedWidth(parent.getAvailableWidth());
}

}