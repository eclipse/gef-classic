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
package org.eclipse.draw2d;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * A Canvas that contains {@link Figure Figures}.
 * TODO: Scrollbar visibility variables should be changed to constants.
 */
public class FigureCanvas
	extends Canvas
{

/** Never show scrollbar */
public static int NEVER = 0;
/** Automatically show scrollbar when needed */
public static int AUTOMATIC = 1;
/** Always show scrollbar */
public static int ALWAYS = 2;

private int vBarVisibility = AUTOMATIC;
private int hBarVisibility = AUTOMATIC;
private Viewport viewport;
private int hBarOffset;
private int vBarOffset;
private PropertyChangeListener horizontalChangeListener = new PropertyChangeListener()
{
	public void propertyChange(PropertyChangeEvent event) {
		RangeModel model = getViewport().getHorizontalRangeModel();
		hBarOffset = Math.max(0, -model.getMinimum());
		getHorizontalBar().setValues(
			model.getValue() + hBarOffset,
			model.getMinimum() + hBarOffset,
			model.getMaximum() + hBarOffset,
			model.getExtent(),
			model.getExtent() / 20,
			model.getExtent() * 3 / 4);
	}
};

private PropertyChangeListener verticalChangeListener = new PropertyChangeListener()
{
	public void propertyChange(PropertyChangeEvent event) {
		RangeModel model = getViewport().getVerticalRangeModel();
		vBarOffset = Math.max(0, -model.getMinimum());
			getVerticalBar().setValues(
				model.getValue() + vBarOffset,
				model.getMinimum() + vBarOffset,
				model.getMaximum() + vBarOffset,
				model.getExtent(),
				model.getExtent() / 20,
				model.getExtent() * 3 / 4);
	}
};

private final LightweightSystem lws;

/**
 * Creates a new FigureCanvas with the given parent.
 * 
 * @param parent the parent
 */
public FigureCanvas(Composite parent) {
	this(parent, new LightweightSystem());
}

/**
 * Constructs a new FigureCanvas with the given parent and LightweightSystem.
 * @param parent the parent
 * @param lws the LightweightSystem
 */
public FigureCanvas(Composite parent, LightweightSystem lws) {
	super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
	this.lws = lws;
	lws.setControl(this);
	hook();
}

/**
 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
 */
public org.eclipse.swt.graphics.Point computeSize(int wHint, int hHint, boolean changed) {
	/* TODO: Need to add the size of the scrollbars.  
	 * Scrollbars could be auto, always on, or never. Based on these settings, the 
	 * available space, and the required space, determine whether scrollbars are shown or 
	 * not, and add their size to the result. You can determine the sizes of scrollbars as
	 * follows:
	 * 
	 * computeTrim(0,0,0,0).width (will return width of vertical scrollbar)
	 */
	Dimension size = getLightweightSystem().getRootFigure().getPreferredSize(wHint, hHint);
	size.union(new Dimension(wHint, hHint));
	return new org.eclipse.swt.graphics.Point(size.width, size.height);
}

/**
 * @return the contents of the {@link Viewport}.
 */
public IFigure getContents() {
	return getViewport().getContents();
}

/**
 * @return the horizontal scrollbar visibility.
 */
public int getHorizontalScrollBarVisibility() {
	return hBarVisibility;
}

/**
 * @return the LightweightSystem
 */
public LightweightSystem getLightweightSystem() {
	return lws;
}

/**
 * @return the vertical scrollbar visibility.
 */
public int getVerticalScrollBarVisibility() {
	return vBarVisibility;
}

/**
 * Returns the Viewport.  If it's <code>null</code>, a new one is created.
 * @return the viewport
 */
public Viewport getViewport() {
	if (viewport == null)
		setViewport(new Viewport(true));
	return viewport;
}

/**
 * Adds listeners for scrolling.
 */
private void hook() {
	getLightweightSystem().getUpdateManager().addUpdateListener(new UpdateListener() {
		public void notifyPainting(Rectangle damage, java.util.Map dirtyRegions) { }
		public void notifyValidating() {
			if (!isDisposed())
				layoutViewport();
		}
	});

	getHorizontalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			scrollToX(getHorizontalBar().getSelection() - hBarOffset);
		}
	});

	getVerticalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			scrollToY(getVerticalBar().getSelection() - vBarOffset);
		}
	});
}

private void hookViewport() {
	getViewport().
		getHorizontalRangeModel().
		addPropertyChangeListener(horizontalChangeListener);
	getViewport().
		getVerticalRangeModel().
		addPropertyChangeListener(verticalChangeListener);
}

private void unhookViewport() {
	getViewport().
		getHorizontalRangeModel().
		removePropertyChangeListener(horizontalChangeListener);
	getViewport().
		getVerticalRangeModel().
		removePropertyChangeListener(verticalChangeListener);
}

private void layoutViewport() {
	ScrollPaneSolver.Result result;
	result = ScrollPaneSolver.solve(new Rectangle(getBounds()).setLocation(0, 0),
		getViewport(),
		getHorizontalScrollBarVisibility(),
		getVerticalScrollBarVisibility(),
		computeTrim(0, 0, 0, 0).width,
		computeTrim(0, 0, 0, 0).height);
	if (getHorizontalBar().getVisible() != result.showH)
		getHorizontalBar().setVisible(result.showH);
	if (getVerticalBar().getVisible() != result.showV)
		getVerticalBar().setVisible(result.showV);
}

/**
 * Scrolls in an animated way to the new x and y location.
 * @param x the x coordinate to scroll to
 * @param y the y coordinate to scroll to
 */
public void scrollSmoothTo(int x, int y) {
	// Ensure newHOffset and newVOffset are within the appropriate ranges
	x = verifyScrollBarOffset(getViewport().getHorizontalRangeModel(), x);
	y = verifyScrollBarOffset(getViewport().getVerticalRangeModel(), y);

	int oldX = getViewport().getViewLocation().x;
	int oldY = getViewport().getViewLocation().y;
	int dx = x - oldX;
	int dy = y - oldY;

	if (dx == 0 && dy == 0)
		return; //Nothing to do.

	Dimension viewingArea = getViewport().getClientArea().getSize();

	int minFrames = 3;
	int maxFrames = 6;
	if (dx == 0 || dy == 0) {
		minFrames = 6;
		maxFrames = 13;
	}
	int frames = (Math.abs(dx) + Math.abs(dy)) / 15;
	frames = Math.max(frames, minFrames);
	frames = Math.min(frames, maxFrames);

	int stepX = Math.min((dx / frames), (viewingArea.width / 3));
	int stepY = Math.min((dy / frames), (viewingArea.height / 3));

	for (int i = 1; i < frames; i++) {
		scrollTo(oldX + i * stepX, oldY + i * stepY);
		getViewport().getUpdateManager().performUpdate();
	}
	scrollTo(x, y);
}

/**
 * Scrolls the contents to the new x and y location.  If this scroll operation only 
 * consists of a vertical or horizontal scroll, a call will be made to 
 * {@link #scrollToY(int)} or {@link #scrollToX(int)}, respectively, to increase 
 * performance.
 * 
 * @param x the x coordinate to scroll to
 * @param y the y coordinate to scroll to
 */
public void scrollTo(int x, int y) {
	x = verifyScrollBarOffset(getViewport().getHorizontalRangeModel(), x);
	y = verifyScrollBarOffset(getViewport().getVerticalRangeModel(), y);
	if (x == getViewport().getViewLocation().x)
		scrollToY(y);
	else if (y == getViewport().getViewLocation().y)
		scrollToX(x);
	else
		getViewport().setViewLocation(x, y);
}
/**
 * Scrolls the contents horizontally so that they are offset by <code>hOffset</code>. 
 * 
 * @param hOffset the new horizontal offset
 */
public void scrollToX(int hOffset) {
	hOffset = verifyScrollBarOffset(getViewport().getHorizontalRangeModel(), hOffset);
	int hOffsetOld = getViewport().getViewLocation().x;
	if (hOffset == hOffsetOld)
		return;
	int dx = -hOffset + hOffsetOld;
	getHorizontalBar().setSelection(hOffset);
	Rectangle clientArea = getViewport().getBounds().getCropped(getViewport().getInsets());
	Rectangle blit = clientArea.getResized(-Math.abs(dx), 0);
	Rectangle expose = clientArea.getCopy();
	Point dest = clientArea.getTopLeft();
	expose.width = Math.abs(dx);
	if (dx < 0) { //Moving left?
		blit.translate(-dx, 0); //Move blit area to the right
		expose.x = dest.x + blit.width;
	} else //Moving right
		dest.x += dx; //Move expose area to the right

	scroll(dest.x, dest.y,
			blit.x, blit.y, blit.width, blit.height,
			true);

	getViewport().setIgnoreScroll(true);
	getViewport().setHorizontalLocation(hOffset);
	getViewport().setIgnoreScroll(false);
	redraw(expose.x, expose.y, expose.width, expose.height, true);
}

/**
 * Scrolls the contents vertically so that they are offset by <code>vOffset</code>. 
 * 
 * @param vOffset the new vertical offset
 */
public void scrollToY(int vOffset) {
	vOffset = verifyScrollBarOffset(getViewport().getVerticalRangeModel(), vOffset);
	int vOffsetOld = getViewport().getViewLocation().y;
	if (vOffset == vOffsetOld)
		return;
	int dy = -vOffset + vOffsetOld;
	getVerticalBar().setSelection(vOffset);
	Rectangle clientArea = getViewport().getBounds().getCropped(getViewport().getInsets());
	Rectangle blit = clientArea.getResized(0, -Math.abs(dy));
	Rectangle expose = clientArea.getCopy();
	Point dest = clientArea.getTopLeft();
	expose.height = Math.abs(dy);
	if (dy < 0) { //Moving up?
		blit.translate(0, -dy); //Move blit area down
		expose.y = dest.y + blit.height; //Move expose area down
	} else //Moving down
		dest.y += dy;

	scroll(dest.x, dest.y,
			blit.x, blit.y, blit.width, blit.height,
			true);

	getViewport().setIgnoreScroll(true);
	getViewport().setVerticalLocation(vOffset);
	getViewport().setIgnoreScroll(false);
	redraw(expose.x, expose.y, expose.width, expose.height, true);
}

/**
 * Sets the contents of the {@link Viewport}.
 * 
 * @param figure the new contents
 */
public void setContents(IFigure figure) {
	getViewport().setContents(figure);
}

/**
 * Sets the horizontal scrollbar visibility.  Possible values are {@link #AUTOMATIC}, 
 * {@link #ALWAYS}, and {@link #NEVER}.
 * 
 * @param v the new visibility
 */
public void setHorizontalScrollBarVisibility(int v) {
	hBarVisibility = v;
}

/**
 * Sets both the horizontal and vertical scrollbar visibility to the given value.  
 * Possible values are {@link #AUTOMATIC}, {@link #ALWAYS}, and {@link #NEVER}.
 * @param both the new visibility
 */
public void setScrollBarVisibility(int both) {
	setHorizontalScrollBarVisibility(both);
	setVerticalScrollBarVisibility(both);
}

/**
 * Sets the vertical scrollbar visibility.  Possible values are {@link #AUTOMATIC}, 
 * {@link #ALWAYS}, and {@link #NEVER}.
 * 
 * @param v the new visibility
 */
public void setVerticalScrollBarVisibility(int v) {
	vBarVisibility = v;
}

/**
 * Sets the Viewport. The given Viewport must use "fake" scrolling. That is, it must be
 * constructed using <code>new Viewport(true)</code>.
 * 
 * @param vp the new viewport
 */
public void setViewport(Viewport vp) {
	if (viewport != null)
		unhookViewport();
	viewport = vp;
	lws.setContents(viewport);
	hookViewport();
}

private int verifyScrollBarOffset(RangeModel model, int value) {
	value = Math.max(model.getMinimum(), value);
	return Math.min(model.getMaximum() - model.getExtent(), value);
}

}
