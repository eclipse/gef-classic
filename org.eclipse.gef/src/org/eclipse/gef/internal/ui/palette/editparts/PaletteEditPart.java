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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import org.eclipse.gef.*;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.gef.ui.palette.*;

abstract class PaletteEditPart 
	extends AbstractGraphicalEditPart
	implements PropertyChangeListener
{

private static ImageCache globalImageCache;
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
 * Returns the image cache.
 * The cache is global, and is shared by all palette edit parts. This has the disadvantage
 * that once an image is allocated, it is never freed until the display is disposed. 
 * However, it has the advantage that the same image in different palettes is
 * only ever created once.
 */
protected static ImageCache getImageCache() {
	ImageCache cache = globalImageCache;
	if (cache == null) {
		globalImageCache = cache = new ImageCache();
		Display display = Display.getDefault();
		if (display != null) {
			display.disposeExec(new Runnable() {
				public void run() {
					if (globalImageCache != null) {
						globalImageCache.dispose();
						globalImageCache = null;
					}
				}	
			});
		}
	}
	return cache;
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
	return ((PaletteViewer)getViewer()).getPaletteViewerPreferences();
}

protected IFigure getToolTipFigure() {
	return getFigure();
}

protected String getToolTipText() {
	String text = null;
	boolean needName = getPreferenceSource().getLayoutSetting() 
				== PaletteViewerPreferences.LAYOUT_ICONS;
	PaletteEntry entry = (PaletteEntry)getModel();
	String desc = entry.getDescription();
	if (desc == null || desc.trim().equals(entry.getLabel()) || desc.trim().equals("")) { //$NON-NLS-1$
		if (needName) {
			text = entry.getLabel();
		}
	} else {
		if (needName) {
			text = entry.getLabel() + " " + PaletteMessages.NAME_DESCRIPTION_SEPARATOR //$NON-NLS-1$
					+ " " + desc; //$NON-NLS-1$
		} else {
			text = desc;
		}
	}
	if (text != null && text.trim().equals("")) { //$NON-NLS-1$
		text = null;
	}
	return text;
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
	String tooltip = getToolTipText();
	if (tooltip == null) {
		getToolTipFigure().setToolTip(null);
		return;
	}

	if (getToolTipFigure().getToolTip() == null) {
		getToolTipFigure().setToolTip(new Label());
	}
	((Label)getToolTipFigure().getToolTip()).setText(tooltip);
}

protected void setImageDescriptor(ImageDescriptor desc) {
	if (desc == imgDescriptor) {
		return;
	}
	imgDescriptor = desc;
	setImageInFigure(getImageCache().getImage(imgDescriptor));
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

protected static class ImageCache {
	/** Map from ImageDescriptor to Image */
	private Map images = new HashMap(11);
	
	Image getImage(ImageDescriptor desc) {
		if (desc == null) {
			return null;
		}
		Image img = null;
		Object obj = images.get(desc);
		if (obj != null) {
			img = (Image)obj;
		} else {
			img = desc.createImage();
			images.put(desc, img);
		}
		return img;
	}

	Image getMissingImage() {
		return getImage(ImageDescriptor.getMissingImageDescriptor());
	}
	
	void dispose() {
		for (Iterator i = images.values().iterator(); i.hasNext();) {
			Image img = (Image) i.next();
			img.dispose();
		}
		images.clear();
	}
}

}