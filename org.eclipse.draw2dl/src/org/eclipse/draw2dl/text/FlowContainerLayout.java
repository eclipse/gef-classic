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

import org.eclipse.draw2dl.Figure;

/**
 * A layout for FlowFigures with children.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public abstract class FlowContainerLayout extends org.eclipse.draw2dl.text.FlowFigureLayout implements
    org.eclipse.draw2dl.text.FlowContext {

	/**
	 * the current line
	 */
	org.eclipse.draw2dl.text.LineBox currentLine;

	/**
	 * @see org.eclipse.draw2dl.text.FlowFigureLayout#FlowFigureLayout(org.eclipse.draw2dl.text.FlowFigure)
	 */
	protected FlowContainerLayout(FlowFigure flowFigure) {
		super(flowFigure);
	}

	/**
	 * Adds the given box the current line and clears the context's state.
	 * 
	 * @see org.eclipse.draw2dl.text.FlowContext#addToCurrentLine(org.eclipse.draw2dl.text.FlowBox)
	 */
	public void addToCurrentLine(org.eclipse.draw2dl.text.FlowBox child) {
		getCurrentLine().add(child);
		setContinueOnSameLine(false);
	}

	/**
	 * Flush anything pending and free all temporary data used during layout.
	 */
	protected void cleanup() {
		currentLine = null;
	}

	/**
	 * Used by getCurrentLine().
	 */
	protected abstract void createNewLine();

	/**
	 * Called after {@link #layoutChildren()} when all children have been laid
	 * out. This method exists to flush the last line.
	 */
	protected abstract void flush();

	/**
	 * FlowBoxes shouldn't be added directly to the current line. Use
	 * {@link #addToCurrentLine(FlowBox)} for that.
	 * 
	 * @see org.eclipse.draw2dl.text.FlowContext#getCurrentLine()
	 */
	LineBox getCurrentLine() {
		if (currentLine == null)
			createNewLine();
		return currentLine;
	}

	/**
	 * @see org.eclipse.draw2dl.text.FlowContext#getRemainingLineWidth()
	 */
	public int getRemainingLineWidth() {
		return getCurrentLine().getAvailableWidth();
	}

	/**
	 * @see FlowContext#isCurrentLineOccupied()
	 */
	public boolean isCurrentLineOccupied() {
		return currentLine != null && currentLine.isOccupied();
	}

	/**
	 * @see FlowFigureLayout#layout()
	 */
	protected void layout() {
		preLayout();
		layoutChildren();
		flush();
		cleanup();
	}

	/**
	 * Layout all children.
	 */
	protected void layoutChildren() {
		List children = getFlowFigure().getChildren();
		for (int i = 0; i < children.size(); i++) {
			Figure f = (Figure) children.get(i);
			if (forceChildInvalidation(f))
				f.invalidate();
			f.validate();
		}
	}

	boolean forceChildInvalidation(Figure f) {
		return true;
	}

	/**
	 * Called before layoutChildren() to setup any necessary state.
	 */
	protected abstract void preLayout();

}
