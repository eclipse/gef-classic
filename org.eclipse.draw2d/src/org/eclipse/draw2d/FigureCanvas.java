package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * A Canvas that contains {@link Figure Figures}.
 */
public class FigureCanvas
	extends Canvas
{

public static int NEVER = 0, AUTOMATIC = 1, ALWAYS = 2;  // ScrollBar visibility constants

private static int DEFAULT_INCREMENT = 10;

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
			model.getValue()+hBarOffset,
			model.getMinimum()+hBarOffset,
			model.getMaximum()+hBarOffset,
			model.getExtent(),
			model.getExtent()/20,
			model.getExtent()*3/4);
	}
};

private PropertyChangeListener verticalChangeListener = new PropertyChangeListener()
{
	public void propertyChange(PropertyChangeEvent event) {
		RangeModel model = getViewport().getVerticalRangeModel();
		vBarOffset = Math.max(0, -model.getMinimum());
			getVerticalBar().setValues(
				model.getValue()+vBarOffset,
				model.getMinimum()+vBarOffset,
				model.getMaximum()+vBarOffset,
				model.getExtent(),
				model.getExtent()/20,
				model.getExtent()*3/4);
	}
};

private InternalLightweightSystem lws;

/**
 * Creates a new FigureCanvas with the given parent.
 */
public FigureCanvas(Composite parent) {
	super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
	init();
	hookControl();
}

/**
 * Returns the contents of the {@link Viewport}.
 */
public IFigure getContents() {
	return getViewport().getContents();
}

/**
 * Returns the horizontal scrollbar visibility.
 */
public int getHorizontalScrollBarVisibility() {
	return hBarVisibility;
}

public LightweightSystem getLightweightSystem() {
	return lws;
}

/**
 * Returns the vertical scrollbar visibility.
 */
public int getVerticalScrollBarVisibility() {
	return vBarVisibility;
}

/**
 * Returns the Viewport.  If it's <code>null</code>, a new 
 * one is created.
 */
public Viewport getViewport() {
	if (viewport == null)
		setViewport(new Viewport(true));
	return viewport;
}

/**
 * Adds listeners for scrolling.
 */
private void hookControl() {
	getHorizontalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			scrollToX(getHorizontalBar().getSelection() -hBarOffset);
		}
	});

	getVerticalBar().addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			scrollToY(getVerticalBar().getSelection()-vBarOffset);
		}
	});
}

private void hookViewport(){
	getViewport().
		getHorizontalRangeModel().
		addPropertyChangeListener(horizontalChangeListener);
	getViewport().
		getVerticalRangeModel().
		addPropertyChangeListener(verticalChangeListener);
}

private void unhookViewport(){
	getViewport().
		getHorizontalRangeModel().
		removePropertyChangeListener(horizontalChangeListener);
	getViewport().
		getVerticalRangeModel().
		removePropertyChangeListener(verticalChangeListener);
}

private void init(){
	lws = new InternalLightweightSystem();
	lws.setControl(this);
	getHorizontalBar().setIncrement(DEFAULT_INCREMENT);
	getVerticalBar().setIncrement(DEFAULT_INCREMENT);
}

private void layoutViewport(){
	ScrollPaneSolver.Result result;
	result = ScrollPaneSolver.solve(new Rectangle(getBounds()).setLocation(0, 0),
		getViewport(),
		getHorizontalScrollBarVisibility(),
		getVerticalScrollBarVisibility(),
		computeTrim(0,0,0,0).width,
		computeTrim(0,0,0,0).height);
	getHorizontalBar().setVisible(result.showH);
	getVerticalBar().setVisible(result.showV);
}

/**
 * Scrolls in an animated way to the new x and y location.
 */
public void scrollSmoothTo(int x, int y) {
	// Ensure newHOffset and newVOffset are within the appropriate ranges
	x = verifyScrollBarOffset(getViewport().getHorizontalRangeModel(), x);
	y = verifyScrollBarOffset(getViewport().getVerticalRangeModel(), y);

	int oldX = getViewport().getViewLocation().x;
	int oldY = getViewport().getViewLocation().y;
	int dx = x - oldX;
	int dy = y -oldY;

	if (dx == 0 && dy == 0)
		return; //Nothing to do.

	Dimension viewingArea = getViewport().getClientArea().getSize();

	int MIN_FRAMES = 3;
	int MAX_FRAMES = 6;
	if (dx == 0 || dy == 0){
		MIN_FRAMES = 5;
		MAX_FRAMES = 13;
	}
	int FRAMES = (Math.abs(dx)+Math.abs(dy))/15;
	FRAMES = Math.max(FRAMES, MIN_FRAMES);
	FRAMES = Math.min(FRAMES, MAX_FRAMES);

	int stepX = Math.min((dx / FRAMES), (viewingArea.width/3));
	int stepY = Math.min((dy / FRAMES), (viewingArea.height/3));

	for (int i=1; i < FRAMES; i++){
		scrollTo(oldX + i*stepX, oldY + i*stepY);
		getViewport().getUpdateManager().performUpdate();
	}
	scrollTo(x,y);
}

/**
 * Scrolls the contents to the new x and y location.  
 * If this scroll operation only consists of a vertical or horizontal
 * scroll, a call will be made to {@link #scrollToY(int)} or 
 * {@link #scrollToX(int)}, respectively, to increase performance.
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
 * Scrolls the contents horizontally so that they are offset
 * by <code>newHOffset</code>. 
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
	if (dx < 0){ //Moving left?
		blit.translate(-dx, 0); //Move blit area to the right
		expose.x = dest.x + blit.width;
	} else //Moving right
		dest.x += dx; //Move expose area to the right
	scroll(dest.x, dest.y,
			blit.x, blit.y, blit.width, blit.height,
			false);
	getViewport().setIgnoreScroll(true);
	getViewport().setHorizontalLocation(hOffset);
	getViewport().setIgnoreScroll(false);
	redraw(expose.x, expose.y, expose.width, expose.height, true);
}

/**
 * Scrolls the contents vertically so that they are offset
 * by <code>newVOffset</code>. 
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
	if (dy < 0){ //Moving up?
		blit.translate(0, -dy); //Move blit area down
		expose.y = dest.y + blit.height; //Move expose area down
	} else //Moving down
		dest.y += dy;
	scroll(dest.x, dest.y,
			blit.x, blit.y, blit.width, blit.height,
			false);

	getViewport().setIgnoreScroll(true);
	getViewport().setVerticalLocation(vOffset);
	getViewport().setIgnoreScroll(false);
	redraw(expose.x, expose.y, expose.width, expose.height, true);
}

/**
 * Sets the contents of the {@link Viewport}.
 */
public void setContents(IFigure figure) {
	getViewport().setContents(figure);
}

/**
 * Sets the horizontal scrollbar visibility.  Possible values are
 * {@link #AUTOMATIC}, {@link #ALWAYS}, and {@link #NEVER}.
 */
public void setHorizontalScrollBarVisibility(int v) {
	hBarVisibility = v;
}

public void setScrollBarVisibility(int both){
	setHorizontalScrollBarVisibility(both);
	setVerticalScrollBarVisibility(both);
}

/**
 * Sets the vertical scrollbar visibility.  Possible values are
 * {@link #AUTOMATIC}, {@link #ALWAYS}, and {@link #NEVER}.
 */
public void setVerticalScrollBarVisibility(int v) {
	vBarVisibility = v;
}

/**
 * Sets the Viewport.
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
	return Math.min(model.getMaximum(), value);
}

class InternalLightweightSystem
	extends LightweightSystem
{
	class ProxyRootFigure extends RootFigure{
		public void validate(){
			if (isDisposed())
				return;
			layoutViewport();
			super.validate();
		}
		public boolean isOpaque(){
			return true;
		}
	}
	protected RootFigure createRootFigure(){
		return new ProxyRootFigure();
	}
}

}
