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

import java.util.*;
import org.eclipse.draw2d.*;

/**
 * A layout for FlowFigures with children.
 * 
 * <P>WARNING: This class is not intended to be subclassed by clients.
 * @author hudsonr
 * @since 2.1 */
public abstract class FlowContainerLayout
	extends FlowFigureLayout
	implements FlowContext
{

/**
 * the current line
 */
protected LineBox currentLine;

/** * @see org.eclipse.draw2d.text.FlowFigureLayout#FlowFigureLayout(FlowFigure) */
protected FlowContainerLayout(FlowFigure flowFigure) {
	super(flowFigure);
}

/** * @see org.eclipse.draw2d.text.FlowContext#addToCurrentLine(FlowBox) */
public void addToCurrentLine(FlowBox block) {
	getCurrentLine().add(block);
}

/**
 * Flush anything pending and free all temporary data used during layout.
 */
protected abstract void cleanup();

/**
 * Used by getCurrentLine().
 */
protected abstract void createNewLine();

/**
 * Called after {@link #layoutChildren()} when all children have been laid out. This
 * method exists to flush the last line.
 */
protected abstract void flush();

/** * @see org.eclipse.draw2d.text.FlowContext#getCurrentLine() */
public LineBox getCurrentLine() {
	if (currentLine == null)
		createNewLine();
	return currentLine;
}

/**
 * @see org.eclipse.draw2d.text.FlowContext#isCurrentLineOccupied
 */
public boolean isCurrentLineOccupied() {
	return currentLine != null && currentLine.isOccupied();
}

/** * @see org.eclipse.draw2d.text.FlowFigureLayout#layout() */
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
		Figure f = (Figure)children.get(i);
		f.invalidate();
		f.validate();
	}
}

/**
 * Called before layoutChildren() to setup any necessary state.
 */
protected abstract void preLayout();

}