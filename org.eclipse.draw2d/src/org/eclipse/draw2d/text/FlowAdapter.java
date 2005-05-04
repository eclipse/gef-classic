/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Adapts non-flow figures for use within a parent hierarchy requiring flow figures. 
 * Normal draw2d figures can be added as children.  If a normal LayoutManager is set, the
 * children will be positioned by that layout manager.  The size of this figure within
 * the flow will be determined by its preferred size.
 * 
 * @author Pratik Shah
 * @since 3.1
 */
public class FlowAdapter
	extends FlowFigure
{

private FlowContext context;
private FigureBox box = new FigureBox();

/**
 * This FlowFigure contributes an Object Replacement Character.
 * @see FlowFigure#contributeBidi(BidiProcessor)
 */
protected void contributeBidi(BidiProcessor proc) {
	box.setBidiLevel(-1);
	// contributes a single object replacement char
	proc.add(this, BidiChars.OBJ);
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
	box.setSize(getPreferredSize());
	if (context.isCurrentLineOccupied() && context.getRemainingLineWidth() < box.width)
		context.endLine();
	context.addToCurrentLine(box);
}

/**
 * Updates the bounds of this figure to match that of its content box.  The calls
 * <code>validate()</code> in case the size has changed.
 * @see FlowFigure#postValidate()
 */
public void postValidate() {
	setBounds(new Rectangle(box.getX(), box.getBaseline() - box.ascent,
			box.width, box.ascent));
	super.validate();
}

/**
 * Sets the bidi level of the content box associated with this Figure
 * @see FlowFigure#setBidiInfo(BidiInfo)
 */
public void setBidiInfo(BidiInfo info) {
	box.setBidiLevel(info.levelInfo[0]);
}

/**
 * @see FlowFigure#setFlowContext(FlowContext)
 */
public void setFlowContext(FlowContext flowContext) {
	context = flowContext;
}

private class FigureBox extends ContentBox {
	private int ascent;
	public boolean containsPoint(int x, int y) {
		return FlowAdapter.this.containsPoint(x, y);
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
