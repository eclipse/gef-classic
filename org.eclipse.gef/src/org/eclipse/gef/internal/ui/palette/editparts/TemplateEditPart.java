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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.ui.IMemento;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * @author Eric Bordeau, Pratik Shah
 */
public class TemplateEditPart
	extends PaletteEditPart
{

private static final String SELECTION_STATE = "selection"; //$NON-NLS-1$
private static final Border BORDER = new MarginBorder(1, 1, 1, 2);
private static final Border COLUMNS_BORDER = new MarginBorder(2, 1, 3, 2);

/**
 * Constructor
 * 
 * @param	entry	The model entry */
public TemplateEditPart(PaletteTemplateEntry entry) {
	super(entry);
}

/** * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#createAccessible() */
protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart (){
		public void getDescription(AccessibleEvent e) {
			e.result = getTemplateEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getTemplateEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LISTITEM;
		}
	};
}

/** * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure() */
public IFigure createFigure() {
	IFigure fig = new DetailedLabelFigure() {
		public IFigure getToolTip() {
			return createToolTip();
		}
	};
	fig.setRequestFocusEnabled(true);
	return fig;
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#deactivate()
 */
public void deactivate() {
	((DetailedLabelFigure)getFigure()).dispose();
	super.deactivate();
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SingleSelectionTracker() {
		protected boolean handleButtonDown(int button) {
			getFigure().requestFocus();
			return super.handleButtonDown(button);
		}
	};
}

private PaletteTemplateEntry getTemplateEntry() {
	return (PaletteTemplateEntry)getModel();
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
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure();
	return label.isNameTruncated() || super.nameNeededInToolTip();
}

/** * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals() */
protected void refreshVisuals() {
	DetailedLabelFigure fig = (DetailedLabelFigure)getFigure();
	PaletteEntry entry = getPaletteEntry();
	fig.setName(entry.getLabel());
	fig.setDescription(entry.getDescription());
	if (getPreferenceSource().useLargeIcons())
		setImageDescriptor(entry.getLargeIcon());
	else
		setImageDescriptor(entry.getSmallIcon());
	int layoutMode = getPreferenceSource().getLayoutSetting();
	fig.setLayoutMode(layoutMode);
	if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS
	  || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
		fig.setBorder(COLUMNS_BORDER);
	else
		fig.setBorder(BORDER);
	super.refreshVisuals();
}

public void restoreState(IMemento memento) {
	setSelected(memento.getInteger(SELECTION_STATE).intValue());
	super.restoreState(memento);
}

public void saveState(IMemento memento) {
	memento.putInteger(SELECTION_STATE, getSelected());
	super.saveState(memento);
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	DetailedLabelFigure fig = (DetailedLabelFigure)getFigure();
	fig.setImage(image);
}

/** * @see org.eclipse.gef.EditPart#setSelected(int) */
public void setSelected(int value) {
	super.setSelected(value);
	DetailedLabelFigure label = (DetailedLabelFigure)getFigure();
	if (value == SELECTED_PRIMARY) {
		label.requestFocus();
		label.setSelected(true);
	} else {
		label.setSelected(false);
	}		
}

}