package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;

import org.eclipse.gef.*;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;

public class ToolEntryEditPart
	extends PaletteEditPart
{

class ToggleButtonTracker extends SingleSelectionTracker {
	protected boolean handleButtonDown(int button) {
		super.handleButtonDown(button);
		getFigure().internalGetEventDispatcher().requestRemoveFocus(getFigure());
		if (button == 1) {
			getButtonModel().setArmed(true);
			getButtonModel().setPressed(true);
		}
		return true;
	}
	protected boolean handleButtonUp(int button) {
		if (button == 1) {
			getButtonModel().setPressed(false);
			getButtonModel().setArmed(false);
		}
		return super.handleButtonUp(button);
	}
	protected boolean handleDrag() {
		return true;
	}
	protected boolean handleNativeDragStarted(DragSourceEvent event) {
		getButtonModel().setPressed(false);
		getButtonModel().setArmed(false);
		return true;
	}

	protected boolean handleNativeDragFinished(DragSourceEvent event) {
		getPaletteViewer().setMode(null);
		return true;
	}
}

private static final Border BORDER_LABEL_MARGIN = new MarginBorder(new Insets(2,2,2,3));

private DetailedLabelFigure customLabel;

public ToolEntryEditPart(PaletteEntry paletteEntry) {
	super(paletteEntry);
}

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart (){
		public void getDescription(AccessibleEvent e) {
			e.result = getPaletteEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getPaletteEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			//Is this correct?
			e.detail = ACC.ROLE_PUSHBUTTON;
		}

		public void getState(AccessibleControlEvent e) {
			e.detail = getButtonModel().isSelected()
				? ACC.STATE_SELECTED
				: ACC.STATE_SELECTABLE;
		}
	};
}

static final Border BORDER_TOGGLE = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);

public IFigure createFigure() {
	class InactiveToggleButton extends ToggleButton {
		InactiveToggleButton(IFigure contents) {
			super(contents);
			setRolloverEnabled(true);
			setBorder(BORDER_TOGGLE);
			setOpaque(false);
		}
		public IFigure findMouseEventTargetAt(int x, int y) {
			return null;
		}
	}
	
	customLabel = new DetailedLabelFigure();
	customLabel.setBorder(new MarginBorder(1,1,0,0));
	Clickable button = new InactiveToggleButton(customLabel);
	button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			getPaletteViewer().setMode(getToolEntry());
		}
	});
	return button;
}

/**
 * @see org.eclipse.gef.EditPart#eraseTargetFeedback(Request)
 */
public void eraseTargetFeedback(Request request) {
	if (RequestConstants.REQ_SELECTION.equals(request.getType()))
		getButtonModel().setMouseOver(false);
	super.eraseTargetFeedback(request);
}

private ButtonModel getButtonModel() {
	Clickable c = (Clickable)getFigure();
	return c.getModel();
}

/**
 * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	return new ToggleButtonTracker();
}

private ToolEntry getToolEntry() {
	return (ToolEntry)getPaletteEntry();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	PaletteEntry entry = getPaletteEntry();

	customLabel.setName(entry.getLabel());
	customLabel.setDescription(entry.getDescription());
	if (getPreferenceSource().useLargeIcons())
		setImageDescriptor(entry.getLargeIcon());
	else
		setImageDescriptor(entry.getSmallIcon());
	customLabel.setLayoutMode(getPreferenceSource().getLayoutSetting());	
	super.refreshVisuals();	
}

/**
 * @see org.eclipse.gef.EditPart#removeNotify()
 */
public void removeNotify() {
	if (getButtonModel().isSelected())
		getPaletteViewer().setMode(null);
	super.removeNotify();
}

public void setToolSelected(boolean value) {
	getButtonModel().setSelected(value);
	getFigure().setOpaque(value);
}

/**
 * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	DetailedLabelFigure fig = (DetailedLabelFigure)(getFigure().getChildren().get(0));
	fig.setImage(image);
}

/** * @see org.eclipse.gef.EditPart#setSelected(int) */
public void setSelected(int value) {
	super.setSelected(value);
	if (value == SELECTED_PRIMARY
	  && getPaletteViewer().getControl() != null
	  && !getPaletteViewer().getControl().isDisposed()
	  && getPaletteViewer().getControl().isFocusControl())
		getFigure().requestFocus();
}

/**
 * @see org.eclipse.gef.EditPart#showTargetFeedback(Request)
 */
public void showTargetFeedback(Request request) {
	if (RequestConstants.REQ_SELECTION.equals(request.getType()))
		getButtonModel().setMouseOver(true);
	super.showTargetFeedback(request);
}

}