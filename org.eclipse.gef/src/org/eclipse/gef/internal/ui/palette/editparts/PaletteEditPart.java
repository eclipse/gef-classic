package org.eclipse.gef.internal.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.draw2d.Label;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerImpl;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

abstract class PaletteEditPart 
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener
{

private AccessibleEditPart acc;
private PropertyChangeListener childListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(PaletteEntry.PROPERTY_VISIBLE)) {
			refreshChildren();
		}
	}
};
private Image image;
private ImageDescriptor imgDescriptor;

public PaletteEditPart(PaletteEntry model) {
	setModel(model);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
 */
public void activate() {
	super.activate();
	PaletteEntry model = (PaletteEntry)getModel();
	model.addPropertyChangeListener(this);
	traverseChildren(model, true);
}

/**
 * returns the AccessibleEditPart for this EditPart.   This method is called lazily from
 * {@link #getAccessibleEditPart()}.
 */
protected AccessibleEditPart createAccessible() {
	return null;
}

public void createEditPolicies() { }

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
 */
public void deactivate() {
	PaletteEntry model = (PaletteEntry)getModel();
	model.removePropertyChangeListener(this);
	traverseChildren(model, false);
	if (image != null) {
		image.dispose();
		image = null;
	}
	super.deactivate();
}


protected AccessibleEditPart getAccessibleEditPart() {
	if (acc == null)
		acc = createAccessible();
	return acc;
}

class SingleSelectionTracker extends SelectEditPartTracker {
	SingleSelectionTracker() {
		super(PaletteEditPart.this);
	}
	protected void performSelection() {
		if (hasSelectionOccurred())
			return;
		setFlag(FLAG_SELECTION_PERFORMED, true);
		getCurrentViewer().select(getSourceEditPart());
	}
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(Request)
 */
public DragTracker getDragTracker(Request request) {
	return new SingleSelectionTracker();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
 */
public List getModelChildren() {
	List modelChildren;
	if (getModel() instanceof PaletteContainer) {
		modelChildren = new ArrayList(((PaletteContainer)getModel()).getChildren());
	} else {
		modelChildren = Collections.EMPTY_LIST;
	}
	
	List childrenToBeRemoved = new ArrayList();
	for (Iterator iter = modelChildren.iterator(); iter.hasNext();) {
		PaletteEntry entry = (PaletteEntry) iter.next();
		if (!entry.isVisible()) {
			childrenToBeRemoved.add(entry);
		}
	}
	modelChildren.removeAll(childrenToBeRemoved)	;
	
	return modelChildren;
}

protected PaletteEntry getPaletteEntry() {
	return (PaletteEntry)getModel();
}

PaletteViewer getPaletteViewer() {
	return (PaletteViewer)getViewer();
}

protected PaletteViewerPreferences getPreferenceSource() {
	return ((PaletteViewerImpl)getViewer()).getPaletteViewerPreferences();
}

/**
 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent evt) {
	String property = evt.getPropertyName();
	if (property.equals(PaletteContainer.PROPERTY_CHILDREN)) {
		traverseChildren((List)evt.getOldValue(), false);
		refreshChildren();
		traverseChildren((List)evt.getNewValue(), true);
		
	} else if (property.equals(PaletteEntry.PROPERTY_LABEL)
			|| property.equals(PaletteEntry.PROPERTY_SMALL_ICON)
			|| property.equals(PaletteEntry.PROPERTY_LARGE_ICON)
			|| property.equals(PaletteEntry.PROPERTY_DESCRIPTION)) {
		refreshVisuals();
	}
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	PaletteEntry entry = (PaletteEntry)getModel();
	String desc = entry.getDescription();
	if (desc == null) { 
		desc = ""; //$NON-NLS-1$
	}
	if (getFigure().getToolTip() == null) {
		getFigure().setToolTip(new Label());
	}
	((Label)getFigure().getToolTip()).setText(entry.getLabel() + " " +  //$NON-NLS-1$
					PaletteMessages.NAME_DESCRIPTION_SEPARATOR + " " + desc);  //$NON-NLS-1$
}

protected void setImageDescriptor(ImageDescriptor desc) {
	if (desc == imgDescriptor) {
		return;
	}
	imgDescriptor = desc;
	if (image != null) {
		image.dispose();
		image = null;
	}
	if (desc != null)
		image = desc.createImage();
	setImageInFigure(image);		
}

protected void setImageInFigure(Image image) { }

private void traverseChildren(PaletteEntry parent, boolean add) {
	if (!(parent instanceof PaletteContainer)) {
		return;
	}
	PaletteContainer container = (PaletteContainer)parent;
	traverseChildren(container.getChildren(), add);
}

private void traverseChildren(List children, boolean add) {	
	for (Iterator iter = children.iterator(); iter.hasNext();) {
		PaletteEntry entry = (PaletteEntry) iter.next();
		if (add) {
			entry.addPropertyChangeListener(childListener);
		} else {
			entry.removePropertyChangeListener(childListener);
		}		
	}
}

}