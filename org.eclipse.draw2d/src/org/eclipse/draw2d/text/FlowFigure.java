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

import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The base implementation for text flow figures. A flow figure is used to render a
 * document in which elements are laid out horizontally within a "line" until that line is
 * filled. Layout continues on the next line.
 * 
 * <p>WARNING: This class is not intended to be subclassed by clients. Future versions may
 * contain additional abstract methods.
 * 
 * @author hudsonr
 * @since 2.1 */
public abstract class FlowFigure
	extends Figure
{

private int[] bidiLevels;
private boolean prependJoiner, appendJoiner;

/**
 * Constructs a new FlowFigure. */
public FlowFigure() {
	setLayoutManager(createDefaultFlowLayout());
}

/**
 * If the child is a <code>FlowFigure</code>, its FlowContext is passed to it.
 * @see org.eclipse.draw2d.IFigure#add(IFigure, Object, int) */
public void add(IFigure child, Object constraint, int index) {
	super.add(child, constraint, index);
	FlowFigure ff = (FlowFigure) child;
	ff.setFlowContext((FlowContext)getLayoutManager());
	revalidateBidi(this);
}

/**
 * Same as invoking {@link #prependJoiner(String)} and {@link #appendJoiner(String)} with
 * the given text.
 * @param text the text to be formatted for shaping
 * @return the given text with shaping characters appended as needed
 * @since 3.1
 */
public String addJoiners(String text) {
	text = prependJoiner(text);
	return appendJoiner(text);
}

/**
 * Appends the Zero-Width Joiner character at the end of the given String if required.
 * 
 * @param text the String to be formatted for shaping
 * @return the given text with the shaping character appended if needed
 * @see #setAppendJoiner(boolean)
 * @since 3.1
 */
public String appendJoiner(String text) {
	if (appendJoiner)
		text += "\u200d"; //$NON-NLS-1$
	return text;
}

/**
 * FlowFigures can contribute text for their block to the given {@link BidiProcessor}, 
 * which will process the contributions to determine Bidi levels and shaping requirements.
 * <p>
 * This method is invoked as part of validating Bidi.
 * 
 * @param proc the BidiProcessor to which contributions should be made
 * @see BidiProcessor#add(FlowFigure, String)
 * @since 3.1
 */
protected void contributeBidi(BidiProcessor proc) {
	for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		((FlowFigure)iter.next()).contributeBidi(proc);
}

/**
 * Creates the default layout manager
 * @return The default layout */
protected abstract FlowFigureLayout createDefaultFlowLayout();

/**
 * For the format of the bidi levels, see {@link BidiProcessor#process()}.
 * 
 * @return the bidi levels assigned by the BidiProcessor; can be <code>null</code> if
 * there is no Bidi text
 * @see #setBidiValues(int[])
 * @since 3.1
 */
public int[] getBidiValues() {
	return bidiLevels;
}

/**
 * Throws away the cached Bidi state for this figure and all its children.  This method
 * is invoked by {@link BlockFlow#revalidateBidi(IFigure)}.
 * @since 3.1
 */
protected void invalidateBidi() {
	bidiLevels = null;
	prependJoiner = false;
	appendJoiner = false;
	for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
		FlowFigure flowFig = (FlowFigure) iter.next();
		flowFig.invalidateBidi();
	}
}

/**
 * Called after validate has occurred. This is used to update the bounds of the FlowFigure
 * to encompass its new flow boxed created during validate.
 */
public abstract void postValidate();

/**
 * Prepends the Zero-Width Joiner character to the beginning of the given text if 
 * required.
 * 
 * @param text the String that needs to be formatted for shaping
 * @return the given text with shaping character prepended if needed
 * @see #setPrependJoiner(boolean)
 * @since 3.1
 */
public String prependJoiner(String text) {
	if (prependJoiner)
		text = "\u200d" + text; //$NON-NLS-1$
	return text;
}

/**
 * Overridden to revalidateBidi when fragments are removed.
 * @see org.eclipse.draw2d.IFigure#remove(org.eclipse.draw2d.IFigure)
 */
public void remove(IFigure figure) {
	super.remove(figure);
	revalidateBidi(this);
}

/**
 * This method should be invoked whenever a change that can potentially affect the
 * Bidi evaluation is made (eg., adding or removing children, setting text, etc.).
 * <p>
 * The default implementation delegates the revaliation task to the parent.  Only
 * {@link BlockFlow#revalidateBidi(IFigure) blocks} perform the actual revaliation. 
 * <p>
 * The given IFigure is the one that triggered the revalidation.  This can be used to
 * optimize bidi evaluation.
 * 
 * @param origin the figure that was revalidated
 * @since 3.1
 */
protected void revalidateBidi(IFigure origin) {
	if (getParent() != null)
		((FlowFigure)getParent()).revalidateBidi(origin);
}

/**
 * This method is invoked by the BidiProcessor if it determines that a shaping character 
 * needs to be appended to the text contributed by this Figure for it to appear properly 
 * on the screen.
 * 
 * @param append the ZWJ will be appended if <code>true</code>
 * @see #appendJoiner(String)
 * @since 3.1
 */
public void setAppendJoiner(boolean append) {
	appendJoiner = append;
}

/**
 * This method is invoked by the BidiProcessor to set the Bidi levels for the text
 * contributed by this figure.  For the format of the Bidi levels, set 
 * {@link BidiProcessor#process()}.
 * 
 * @param levels the bidi levels
 * @see #getBidiValues()
 * @since 3.1
 */
protected void setBidiValues(int[] levels) {
	bidiLevels = levels;
}

/**
 * FlowFigures override setBounds() to prevent translation of children. "bounds" is a
 * derived property for FlowFigures, calculated from the fragments that make up the
 * FlowFigure.
 * @see Figure#setBounds(Rectangle)
 */
public void setBounds(Rectangle r) {
	if (getBounds().equals(r))
		return;
	erase();
	bounds.x = r.x;
	bounds.y = r.y;
	bounds.width = r.width;
	bounds.height = r.height;
	fireMoved();
	repaint();
}

/**
 * Sets the flow context.
 * @param flowContext the flow context for this flow figure */
public void setFlowContext(FlowContext flowContext) {
	((FlowFigureLayout)getLayoutManager()).setFlowContext(flowContext);
}

/**
 * This method is invoked by the BidiProcessor if it determines that a shaping character
 * needs to be prepended to the text contributed by this Figure for it to appear properly
 * on the screen.
 * 
 * @param prepend the ZWJ will be prepended if <code>true</code>
 * @see #prependJoiner(String) 
 * @since 3.1
 */
public void setPrependJoiner(boolean prepend) {
	prependJoiner = prepend;
}

}