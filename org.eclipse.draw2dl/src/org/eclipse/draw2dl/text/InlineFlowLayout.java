/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.text;

import java.util.List;

/**
 * The layout manager for {@link org.eclipse.draw2dl.text.InlineFlow} figures.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public class InlineFlowLayout extends org.eclipse.draw2dl.text.FlowContainerLayout {

	/**
	 * Creates a new InlineFlowLayout with the given FlowFigure.
	 * 
	 * @param flow
	 *            The FlowFigure
	 */
	public InlineFlowLayout(org.eclipse.draw2dl.text.FlowFigure flow) {
		super(flow);
	}

	/**
	 * Adds the given box as a line below the current line.
	 * 
	 * @param box
	 *            the box to add
	 */
	public void addLine(CompositeBox box) {
		endLine();
		getContext().addLine(box);
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContainerLayout#createNewLine()
	 */
	protected void createNewLine() {
		currentLine = new NestedLine((org.eclipse.draw2dl.text.InlineFlow) getFlowFigure());
		setupLine(currentLine);
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#endLine()
	 */
	public void endLine() {
		flush();
		getContext().endLine();
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContainerLayout#flush()
	 */
	protected void flush() {
		if (currentLine != null && currentLine.isOccupied()) {
			// We want to preserve the state when a linebox is being added
			boolean sameLine = getContext().getContinueOnSameLine();
			getContext().addToCurrentLine(currentLine);
			((org.eclipse.draw2dl.text.InlineFlow) getFlowFigure()).getFragments().add(currentLine);
			currentLine = null;
			getContext().setContinueOnSameLine(sameLine);
		}
	}

	/**
	 * InlineFlowLayout gets this information from its context.
	 * 
	 * @see org.eclipse.draw2dl.text.FlowContext#getContinueOnSameLine()
	 */
	public boolean getContinueOnSameLine() {
		return getContext().getContinueOnSameLine();
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#getWidthLookahead(org.eclipse.draw2dl.text.FlowFigure, int[])
	 */
	public void getWidthLookahead(org.eclipse.draw2dl.text.FlowFigure child, int result[]) {
		List children = getFlowFigure().getChildren();
		int index = -1;
		if (child != null)
			index = children.indexOf(child);

		for (int i = index + 1; i < children.size(); i++)
			if (((FlowFigure) children.get(i))
					.addLeadingWordRequirements(result))
				return;

		getContext().getWidthLookahead(getFlowFigure(), result);
	}

	/**
	 * @see FlowContainerLayout#isCurrentLineOccupied()
	 */
	public boolean isCurrentLineOccupied() {
		return (currentLine != null && !currentLine.getFragments().isEmpty())
				|| getContext().isCurrentLineOccupied();
	}

	/**
	 * Clears out all fragments prior to the call to layoutChildren().
	 */
	public void preLayout() {
		((InlineFlow) getFlowFigure()).getFragments().clear();
	}

	/**
	 * InlineFlow passes this information to its context.
	 * 
	 * @see FlowContext#setContinueOnSameLine(boolean)
	 */
	public void setContinueOnSameLine(boolean value) {
		getContext().setContinueOnSameLine(value);
	}

	/**
	 * Initializes the given LineBox. Called by createNewLine().
	 * 
	 * @param line
	 *            The LineBox to initialize.
	 */
	protected void setupLine(LineBox line) {
		line.setX(0);
		line.setRecommendedWidth(getContext().getRemainingLineWidth());
	}

}
