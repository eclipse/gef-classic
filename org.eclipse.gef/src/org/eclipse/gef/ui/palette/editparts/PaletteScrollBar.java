package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public final class PaletteScrollBar 
	extends ScrollBar {

protected Label upLabel;
protected Label downLabel;

private static final int BUTTON_HEIGHT = 15;
private static final Border dropshadow = new DropShadowButtonBorder();
private static final Border margin = new MarginBorder(2, 0, 2, 2);

public PaletteScrollBar() {
	super();
}

protected Clickable createDefaultDownButton() {
	downLabel = new Label(ImageConstants.down);
	downLabel.setOpaque(true);
	downLabel.setIconAlignment(PositionConstants.BOTTOM);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event) {
			updateDownLabel();
		}
	});
	Clickable button = new Clickable(downLabel);
	button.setRequestFocusEnabled(false);
	button.getModel().addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event) {
			updateDownLabel();
		}
	});

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setRolloverEnabled(true);
	button.setBorder(dropshadow);
	button.setOpaque(false);
	return button;
}

protected Clickable createDefaultUpButton() {
	upLabel = new Label(ImageConstants.up);
	upLabel.setOpaque(true);
	upLabel.setIconAlignment(PositionConstants.BOTTOM);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event) {
			updateUpLabel();
		}
	});
	Clickable button = new Clickable(upLabel);
	button.setRequestFocusEnabled(false);
	button.getModel().addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event) {
			updateUpLabel();
		}
	});

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setRolloverEnabled(true);
	button.setBorder(dropshadow);
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
	setBorder(margin);	
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepDown()
 */
protected void stepDown() {
	super.stepDown();
	getUpdateManager().performUpdate();
	super.stepDown();
	getUpdateManager().performUpdate();
	super.stepDown();
}

/**
 * @see org.eclipse.draw2d.ScrollBar#stepUp()
 */
protected void stepUp() {
	super.stepUp();
	getUpdateManager().performUpdate();
	super.stepUp();
	getUpdateManager().performUpdate();
	super.stepUp();
}

protected void updateDownLabel() {
	Image icon = null;
	if (((Clickable) getButtonDown()).getModel().isPressed() ||
	   !((Clickable)getButtonDown()).getModel().isMouseOver())
		icon = ImageConstants.downPressed;
	if (getValue() >= (getMaximum() - getExtent())) {
		icon = ImageConstants.downGrayed;
		getButtonDown().setVisible(false);
		getButtonDown().setEnabled(false);
	} else {
		getButtonDown().setEnabled(true);
		getButtonDown().setVisible(true);
		if (icon == null)
			icon = ImageConstants.down;
	}
	downLabel.setIcon(icon);
}

protected void updateUpLabel() {
	Image icon = null;
	if (((Clickable) getButtonUp()).getModel().isPressed() ||
	   !((Clickable)getButtonUp()).getModel().isMouseOver())
		icon = ImageConstants.upPressed;
	if (getValue() <= getMinimum()) {
		icon = ImageConstants.upGrayed;
		getButtonUp().setVisible(false);
		getButtonUp().setEnabled(false);
	} else {
		getButtonUp().setEnabled(true);
		getButtonUp().setVisible(true);
		if (icon == null)
			icon = ImageConstants.up;
	}
	upLabel.setIcon(icon);
}

}