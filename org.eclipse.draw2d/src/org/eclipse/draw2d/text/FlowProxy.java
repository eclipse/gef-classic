/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This is a FlowFigure proxy for regular figures.  It allows regular Figures
 * to be added to FlowFigures, hence allowing Figures to be shown inline with text.  
 * Clients can set the layout manager, border, colors, etc. for this figure just like 
 * they would normal figures.  Other figures can be added to this figure as well.
 *   
 * @author Pratik Shah
 * @since 3.1
 */
public class FlowProxy 
	extends FlowFigure
{

private FlowContext context;
private FigureBox box = new FigureBox();

/**
 * @see IFigure#add(IFigure, java.lang.Object, int)
 */
public void add(IFigure child,Object constraint,int index) {
	try {
		super.add(child, constraint, index);
	} catch (ClassCastException e) {}
}

/**
 * This FlowFigure contributes an Object Replacement Character.
 * @see org.eclipse.draw2d.text.FlowFigure#contributeBidi(org.eclipse.draw2d.text.BidiProcessor)
 */
protected void contributeBidi(BidiProcessor proc) {
	box.setBidiLevel(-1);
	// contributes the object replacement character
	proc.add(this, "\ufffc"); //$NON-NLS-1$
}

/**
 * @return <code>null</code>
 * @see org.eclipse.draw2d.text.FlowFigure#createDefaultFlowLayout()
 */
protected FlowFigureLayout createDefaultFlowLayout() {
	return null;
}

/**
 * Sizes the content box to be big enough to display all figures.  Wraps to the next line
 * if there is not enough room on this one.
 * @see org.eclipse.draw2d.Figure#layout()
 */
protected void layout() {
	if (getLayoutManager() != null)
		box.setSize(getLayoutManager().getPreferredSize(this, -1, -1));
	if (context.isCurrentLineOccupied() && context.getRemainingLineWidth() < box.width)
		context.endLine();
	context.addToCurrentLine(box);
}

/**
 * Updates the bounds of this figure to match that of its content box, and then lays out
 * all children figures.
 * @see org.eclipse.draw2d.text.FlowFigure#postValidate()
 */
public void postValidate() {
	setBounds(new Rectangle(box.getX(), box.getBaseline() - box.getAscent(),
			box.width, box.getAscent()));
	super.layout();
}

/**
 * Sets the bidi level of the content box associated with this Figure
 * @see org.eclipse.draw2d.text.FlowFigure#setBidiInfo(org.eclipse.draw2d.text.BidiInfo)
 */
public void setBidiInfo(BidiInfo info) {
	box.setBidiLevel(info.levelInfo[0]);
}

/**
 * @see org.eclipse.draw2d.text.FlowFigure#setFlowContext(org.eclipse.draw2d.text.FlowContext)
 */
public void setFlowContext(FlowContext flowContext) {
	context = flowContext;
}

private class FigureBox extends ContentBox {
	private int ascent;
	public boolean containsPoint(int x, int y) {
		return FlowProxy.this.containsPoint(x, y);
	}
	public int getAscent() {
		return ascent;
	}
	public int getDescent() {
		return 0;
	}
	public void setSize(Dimension size) {
		ascent = size.height;
		width = size.width;
	}
}

}