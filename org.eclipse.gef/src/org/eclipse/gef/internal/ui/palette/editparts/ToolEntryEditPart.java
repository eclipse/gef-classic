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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import org.eclipse.ui.IMemento;

import org.eclipse.draw2d.*;

import org.eclipse.gef.*;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

public class ToolEntryEditPart
	extends PaletteEditPart
{

class MenuTimer implements Runnable{
	public static final int MENU_TIMER_DELAY = 400;
	
	private boolean enabled = true;
	
	public void disable() {
		enabled = false;
	}
	
	public void run() {
		if (enabled) {
			getButtonModel().setArmed(false);
			getButtonModel().setPressed(false);
			((PaletteStackEditPart)getParent()).openMenu();
		}
	}
}

class GTKToggleButtonTracker extends SingleSelectionTracker {
	int gtkState = 0;
	MenuTimer currTimer = null;
	
	public void deactivate() {
		if (gtkState != 2) {
			getButtonModel().setArmed(false);
			getButtonModel().setPressed(false);
			getPaletteViewer().setActiveTool(null);
		}
		super.deactivate();
	}
	protected boolean handleButtonDown(int button) {
		if (getParent() instanceof PaletteStackEditPart) {
			currTimer = new MenuTimer();
			getViewer().getControl().getDisplay().timerExec(MenuTimer.MENU_TIMER_DELAY, 
					currTimer);
		}
		if (button == 2 && isInState(STATE_INITIAL))
			performConditionalSelection();
		super.handleButtonDown(button);
		if (button == 1) {
			getFigure().internalGetEventDispatcher().requestRemoveFocus(getFigure());
			getButtonModel().setArmed(true);
			getButtonModel().setPressed(true);
		}
		return true;
	}
	protected boolean handleButtonUp(int button) {
		if (gtkState == 0)
			gtkState = 2;
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
		if (currTimer != null)
			currTimer.disable();
		
		gtkState = 1;
		getButtonModel().setPressed(false);
		getButtonModel().setArmed(false);
		return true;
	}
	protected boolean handleNativeDragFinished(DragSourceEvent event) {
		getPaletteViewer().setActiveTool(null);
		return true;
	}
}

class ToggleButtonTracker extends SingleSelectionTracker {
	
	private static final int WIN_THRESHOLD = 3;
	
	private Point mouseDownLoc = null;
	private MenuTimer currTimer = null;
	private boolean menuShown = false;
	
	protected boolean handleButtonDown(int button) {
		if (!menuShown) {
			if (getParent() instanceof PaletteStackEditPart) {
				currTimer = new MenuTimer();
				getViewer().getControl().getDisplay().timerExec(MenuTimer.MENU_TIMER_DELAY, 
						currTimer);
			}
			mouseDownLoc = new Point(getLocation().x, getLocation().y);
			
			if (button == 2 && isInState(STATE_INITIAL))
				performConditionalSelection();
			super.handleButtonDown(button);
			if (button == 1) {
				getFigure().internalGetEventDispatcher().requestRemoveFocus(getFigure());
				getButtonModel().setArmed(true);
				getButtonModel().setPressed(true);
			}
		}
	
		return true;
			
	}
	protected boolean handleButtonUp(int button) {
		if (currTimer != null) 
			currTimer.disable();
		
		if (button == 1 && !menuShown) {
			getButtonModel().setPressed(false);
			getButtonModel().setArmed(false);
			menuShown = false;
		}
		return super.handleButtonUp(button);
	}
	protected boolean handleDrag() {
		org.eclipse.draw2d.geometry.Point point = getLocation().getCopy();
		getFigure().translateToRelative(point);
		if (!getFigure().containsPoint(point)) {
			getButtonModel().setArmed(false);
			if (currTimer != null)
				currTimer.disable();
		} else
			getButtonModel().setArmed(true);
		return true;
	}
	protected boolean handleNativeDragStarted(DragSourceEvent event) {
		// cancel timer
		if (currTimer != null) 
			currTimer.disable();
		
		if (getParent() instanceof PaletteStackEditPart && SWT.getPlatform().equals("win32")) { //$NON-NLS-1$
			Point nds = getPaletteViewer().getControl().toControl(event.display.getCursorLocation());
			if (mouseDownLoc != null && (Math.abs(nds.x - mouseDownLoc.x) 
					+ Math.abs(nds.y - mouseDownLoc.y)) < WIN_THRESHOLD) {
				getButtonModel().setArmed(false);
				getButtonModel().setPressed(false);
				((PaletteStackEditPart)getParent()).openMenu();
				event.doit = false; 
				menuShown = true;
				return false;
			}
		}

		getButtonModel().setPressed(false);
		getButtonModel().setArmed(false);
		return true;
	}

	protected boolean handleNativeDragFinished(DragSourceEvent event) {
		getPaletteViewer().setActiveTool(null);
		return true;
	}
}

private static final String ACTIVE_STATE = "active"; //$NON-NLS-1$
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
			super.getState(e);
			if (getButtonModel().isSelected())
				e.detail |= ACC.STATE_CHECKED;
		}
	};
}

static final Border BORDER_TOGGLE = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);
static final Border COLUMNS_BORDER = new MarginBorder(2, 0, 1, 0);
public IFigure createFigure() {
	class InactiveToggleButton extends ToggleButton {
		InactiveToggleButton(IFigure contents) {
			super(contents);
			setOpaque(false);
			setEnabled(true);
		}
		public IFigure findMouseEventTargetAt(int x, int y) {
			return null;
		}
		public IFigure getToolTip() {
			return createToolTip();
		}
		public void setEnabled(boolean value) {
			super.setEnabled(value);
			if (isEnabled()) {
				setRolloverEnabled(true);
				setBorder(BORDER_TOGGLE);
				setForegroundColor(null);
			} else {
				setBorder(null);
				setRolloverEnabled(false);
				setForegroundColor(ColorConstants.gray);
			}
		}
	}
	
	customLabel = new DetailedLabelFigure();
	Clickable button = new InactiveToggleButton(customLabel);
	button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			getPaletteViewer().setActiveTool(getToolEntry());
		}
	});

	return button;
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#deactivate()
 */
public void deactivate() {
	customLabel.dispose();
	super.deactivate();
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
 * @see PaletteEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	if (SWT.getPlatform().equals("gtk"))  //$NON-NLS-1$
		return new GTKToggleButtonTracker();
	else
		return new ToggleButtonTracker();
}

private ToolEntry getToolEntry() {
	return (ToolEntry)getPaletteEntry();
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#getToolTipText()
 */
protected String getToolTipText() {
	String result = null;
	if (getPreferenceSource().getLayoutSetting()
				!= PaletteViewerPreferences.LAYOUT_DETAILS) {
		result = super.getToolTipText();
	}
	return result;
}

/**
 * If this edit part's name is truncated in its label, the name should be prepended to
 * the tooltip.
 * @return whether the name needs to be included in the tooltip
 */
protected boolean nameNeededInToolTip() {
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure().getChildren().get(0);
	return label.isNameTruncated() || super.nameNeededInToolTip();
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
	int layoutMode = getPreferenceSource().getLayoutSetting();
	customLabel.setLayoutMode(layoutMode);
	if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS
	  || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
		customLabel.setBorder(COLUMNS_BORDER);
	else
		customLabel.setBorder(null);
	super.refreshVisuals();	
}

/**
 * @see org.eclipse.gef.EditPart#removeNotify()
 */
public void removeNotify() {
	if (getButtonModel().isSelected())
		getPaletteViewer().setActiveTool(null);
	super.removeNotify();
}

public void setToolSelected(boolean value) {
	getButtonModel().setSelected(value);
	getFigure().setOpaque(value);
}

public void restoreState(IMemento memento) {
	if (new Boolean(memento.getString(ACTIVE_STATE)).booleanValue())
		getPaletteViewer().setActiveTool(getToolEntry());
	super.restoreState(memento);
}

public void saveState(IMemento memento) {
	memento.putString(ACTIVE_STATE, new Boolean(
			getPaletteViewer().getActiveTool() == getToolEntry()).toString());
	super.saveState(memento);
}

/**
 * @see PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	DetailedLabelFigure fig = (DetailedLabelFigure)(getFigure().getChildren().get(0));
	fig.setImage(image);
}

/**
 * @see org.eclipse.gef.EditPart#setSelected(int)
 */
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