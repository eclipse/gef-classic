package org.eclipse.gef.internal.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public final class PaletteScrollBar 
	extends ScrollBar {

protected Label upLabel;
protected Label downLabel;

private static final int BUTTON_HEIGHT = 12;
private static final int SCROLL_TIME = 400;
private static final Border SCROLL_BAR_BORDER = new RaisedBorder();

public PaletteScrollBar() {
	super();
}

/**
 * @see org.eclipse.draw2d.Figure#findFigureAt(int, int, TreeSearch)
 */
public IFigure findFigureAt(int x, int y, TreeSearch search) {
	IFigure result = super.findFigureAt(x, y, search);
	if (result != this)
		return result;
	return null;
}


protected Clickable createDefaultDownButton() {
	downLabel = new Label(ImageConstants.down);
	downLabel.setOpaque(true);
	downLabel.setIcon(ImageConstants.down);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event) {
			updateDownLabel();
		}
	});
	Clickable button = new Clickable(downLabel);
	button.setRequestFocusEnabled(false);

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setBorder(SCROLL_BAR_BORDER);
	button.setOpaque(false);
	return button;
}

protected Clickable createDefaultUpButton() {
	upLabel = new Label(ImageConstants.up);
	upLabel.setOpaque(true);
	upLabel.setIcon(ImageConstants.up);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event) {
			updateUpLabel();
		}
	});
	Clickable button = new Clickable(upLabel);
	button.setRequestFocusEnabled(false);

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setBorder(SCROLL_BAR_BORDER);
	button.setOpaque(false);
	return button;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return new Dimension(wHint, hHint);
}

protected void initialize() {
	super.initialize();
	setLayoutManager(new ScrollBarLayout(transposer) {
		protected Rectangle layoutButtons(ScrollBar scrollBar){
			Rectangle bounds = transposer.t(scrollBar.getClientArea());
			Dimension buttonSize = new Dimension(bounds.width, BUTTON_HEIGHT);
		
			if (getButtonUp() != null)
				getButtonUp().setBounds(transposer.t(
					new Rectangle(bounds.getTopLeft(), buttonSize)));
			if (getButtonDown() != null){
				Rectangle r = new Rectangle (
					bounds.x, bounds.bottom() - buttonSize.height,
					buttonSize.width, buttonSize.height);
				getButtonDown().setBounds(transposer.t(r));
			}
		
			Rectangle trackBounds = bounds.getCropped(
				new Insets(
					(getButtonUp()   == null) ? 0 : buttonSize.height, 0,
					(getButtonDown() == null) ? 0 : buttonSize.height, 0));
			return trackBounds;
		}
	});
	setPageUp(null);
	setPageDown(null);
	setThumb(null);
	setOpaque(false);
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepDown()
 */
protected void stepDown() {
	timedStep(false);
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepUp()
 */
protected void stepUp() {
	timedStep(true);
}

protected void timedStep(boolean up) {
	int increment = getStepIncrement();
	int value = getValue();
	long startTime = System.currentTimeMillis();
	long elapsedTime = System.currentTimeMillis() - startTime;
	while( elapsedTime < SCROLL_TIME ) {
		int step = (int)(increment * elapsedTime / SCROLL_TIME);
		int newValue = up ? value - step : value + step;
		setValue(newValue);
		getUpdateManager().performUpdate();
		elapsedTime = System.currentTimeMillis() - startTime;
	}	
}

protected void updateDownLabel() {
	getButtonDown().setVisible(getValue() < (getMaximum() - getExtent()));
}

protected void updateUpLabel() {
	getButtonUp().setVisible(getValue() > getMinimum());
}

}