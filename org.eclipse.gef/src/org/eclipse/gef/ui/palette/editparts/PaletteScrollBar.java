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

final public class PaletteScrollBar 
	extends ScrollBar {

protected Label upLabel;
protected Label downLabel;

private static Border dropshadow =
	new DropShadowButtonBorder();

private static Border margin = new MarginBorder(2,0,2,2);

public PaletteScrollBar(){
	setBorder(margin);	
	setPreferredSize(15,15);
}

protected Clickable createDefaultDownButton(){
	downLabel = new Label(ImageConstants.down);
	downLabel.setOpaque(true);
	downLabel.setIconAlignment(PositionConstants.BOTTOM);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event){
			updateDownLabel();
		}
	});
	Clickable button = new Clickable(downLabel);
	button.setRequestFocusEnabled(false);
	button.getModel().addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event){
			updateDownLabel();
		}
	});

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setRolloverEnabled(true);
	button.setBorder(dropshadow);
	button.setOpaque(false);
	return button;
}

protected Clickable createDefaultUpButton(){
	upLabel = new Label(ImageConstants.up);
	upLabel.setOpaque(true);
	upLabel.setIconAlignment(PositionConstants.BOTTOM);
	addPropertyChangeListener(new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent event){
			updateUpLabel();
		}
	});
	Clickable button = new Clickable(upLabel);
	button.setRequestFocusEnabled(false);
	button.getModel().addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event){
			updateUpLabel();
		}
	});

	button.setFiringMethod(Clickable.REPEAT_FIRING);
	button.setRolloverEnabled(true);
	button.setBorder(dropshadow);
	button.setOpaque(false);
	return button;
}

protected void initialize(){
	super.initialize();
	setPageUp(null);
	setPageDown(null);
	setThumb(null);
	setOpaque(false);
}

protected void updateDownLabel(){
	Image icon = null;
	if(((Clickable)getButtonDown()).getModel().isPressed() || 
	   !((Clickable)getButtonDown()).getModel().isMouseOver())
		icon = ImageConstants.downPressed;
	if(getValue()>=(getMaximum()-getExtent())){
		icon = ImageConstants.downGrayed;
		getButtonDown().setEnabled(false);
	}else{
		getButtonDown().setEnabled(true);
		if(icon==null)
			icon = ImageConstants.down;
	}
	downLabel.setIcon(icon);
}

protected void updateUpLabel(){
	Image icon = null;
	if(((Clickable)getButtonUp()).getModel().isPressed() || 
	   !((Clickable)getButtonUp()).getModel().isMouseOver())
		icon = ImageConstants.upPressed;
	if(getValue()<=getMinimum()){
		icon = ImageConstants.upGrayed;
		getButtonUp().setEnabled(false);
	}else{
		getButtonUp().setEnabled(true);
		if(icon==null)
			icon = ImageConstants.up;
	}
	upLabel.setIcon(icon);
}

}