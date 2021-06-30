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

import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.LayoutManager;
import org.eclipse.draw2dl.geometry.Dimension;

/**
 * A LayoutManager for use with FlowFigure.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public abstract class FlowFigureLayout implements LayoutManager {

	/**
	 * The flow context in which this LayoutManager exists.
	 */
	private org.eclipse.draw2dl.text.FlowContext context;

	/**
	 * The figure passed by layout(Figure) is held for convenience.
	 */
	private final org.eclipse.draw2dl.text.FlowFigure flowFigure;

	/**
	 * Constructs a new FlowFigureLayout with the given FlowFigure.
	 * 
	 * @param flowfigure
	 *            the FlowFigure
	 */
	protected FlowFigureLayout(org.eclipse.draw2dl.text.FlowFigure flowfigure) {
		this.flowFigure = flowfigure;
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#getConstraint(IFigure)
	 */
	public Object getConstraint(IFigure child) {
		return null;
	}

	/**
	 * Returns this layout's context or <code>null</code>.
	 * 
	 * @return <code>null</code> or a context
	 * @since 3.1
	 */
	protected org.eclipse.draw2dl.text.FlowContext getContext() {
		return context;
	}

	/**
	 * @return the FlowFigure
	 */
	protected FlowFigure getFlowFigure() {
		return flowFigure;
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#getMinimumSize(IFigure,
	 *      int, int)
	 */
	public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
		return null;
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#getPreferredSize(IFigure,
	 *      int, int)
	 */
	public Dimension getPreferredSize(IFigure container, int wHint, int hHint) {
		return null;
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#invalidate()
	 */
	public void invalidate() {
	}

	/**
	 * Called during {@link #layout(IFigure)}.
	 */
	protected abstract void layout();

	/**
	 * @see LayoutManager#layout(IFigure)
	 */
	public final void layout(IFigure figure) {
		layout();
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#remove(IFigure)
	 */
	public void remove(IFigure child) {
	}

	/**
	 * Not applicable.
	 * 
	 * @see LayoutManager#setConstraint(IFigure,
	 *      java.lang.Object)
	 */
	public void setConstraint(IFigure child, Object constraint) {
	}

	/**
	 * Sets the context for this layout manager.
	 * 
	 * @param flowContext
	 *            the context of this layout
	 */
	public void setFlowContext(FlowContext flowContext) {
		context = flowContext;
	}

}
