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

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

import org.eclipse.gef.internal.InternalImages;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * EditPart for a PaletteDrawer
 * 
 * @author Pratik Shah
 */
public class DrawerEditPart 
	extends PaletteEditPart
{

/**
 * Constructor
 * 
 * @param drawer	The PaletteDrawer that this EditPart is representing
 */
public DrawerEditPart(PaletteDrawer drawer) {
	super(drawer);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
	DrawerFigure fig = new DrawerFigure(getViewer().getControl());
	fig.setExpanded(getDrawer().isInitiallyOpen());
	fig.setPinned(getDrawer().isInitiallyPinned());
	fig.getCollapseToggle().addChangeListener(new ToggleListener());	
	fig.getCollapseToggle().setRequestFocusEnabled(true);
	fig.getCollapseToggle().addFocusListener(new FocusListener() {
		public void focusGained(FocusEvent fe) {
			getViewer().select(DrawerEditPart.this);
		}
		public void focusLost(FocusEvent fe) {
		}
	});
	return fig;
}

/**
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
 */
public Object getAdapter(Class key) {
	if (key == ExposeHelper.class) {
		ViewportExposeHelper helper = new ViewportExposeHelper(this);
		helper.setMinimumFrameCount(6);
		return helper;
	}
	return super.getAdapter(key);
}

/**
 * Convenience method that provides access to the PaletteDrawer that is the model.
 * @return The model PaletteDrawer
 */
public PaletteDrawer getDrawer() {
	return (PaletteDrawer)getPaletteEntry();
}

/**
 * Convenience method to get the DrawerFigure for the model drawer.
 * 
 * @return The DrawerFigure created in {@link #createFigure()}
 */
public DrawerFigure getDrawerFigure() {
	return (DrawerFigure)getFigure();
}

/**
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return getDrawerFigure().getContentPane();
}

/** * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#getToolTipFigure() */
protected IFigure getToolTipFigure() {
	return getDrawerFigure().getCollapseToggle();
}

private DrawerAnimationController getAnimationController() {
	DrawerAnimationController controller;
	controller = (DrawerAnimationController)getViewer()
		.getEditPartRegistry()
		.get(DrawerAnimationController.class);
	if (controller == null) {
		controller = new DrawerAnimationController(getPreferenceSource());
		getViewer().getEditPartRegistry().put(DrawerAnimationController.class, controller);
	}
	return controller;
}

/**
 * Returns the expansion state of the drawer
 * @return <code>true</code> if the drawer is expanded; false otherwise
 */
public boolean isExpanded() {
	return getDrawerFigure().isExpanded();
}

/**
 * Returns <code>true</code> if the drawer is pinned open.
 * @return boolean
 */
public boolean isPinnedOpen() {
	return getDrawerFigure().isPinnedOpen();
}

/** * @return <code>true</code> if the DrawerFigure can be pinned open.  This is only true
 * when the drawer is expanded and the auto-collapse strategy is
 * <code>PaletteViewerPreferences.COLLAPSE_AS_NEEDED</code>.
 */
public boolean canBePinned() {
	return getDrawerFigure().isPinShowing();
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteEditPart#createAccessible()
 */
protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getDescription(AccessibleEvent e) {
			e.result = getPaletteEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getPaletteEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TREE;
		}

		public void getState(AccessibleControlEvent e) {
			e.detail = isExpanded() ? ACC.STATE_EXPANDED : ACC.STATE_COLLAPSED;
		}
	};
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	super.refreshVisuals();
	
	getDrawerFigure().setToolTipText(getToolTipText());

	ImageDescriptor img = getPaletteEntry().getSmallIcon();
	if (img == null) {
		img = InternalImages.DESC_FOLDER_OPEN;
	}
	setImageDescriptor(img);

	getDrawerFigure().setTitle(getPaletteEntry().getLabel());
	getDrawerFigure().setLayoutMode(getPreferenceSource().getLayoutSetting());

	boolean showPin = getPreferenceSource().getAutoCollapseSetting()
					== PaletteViewerPreferences.COLLAPSE_AS_NEEDED;
	getDrawerFigure().showPin(showPin);

	Color background = getDrawer().getDrawerType().equals(
		PaletteTemplateEntry.PALETTE_TYPE_TEMPLATE) ? ColorConstants.listBackground : null;
	getDrawerFigure().getContentPane().setBackgroundColor(background);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
 */
protected void register() {
	super.register();
	getAnimationController().addDrawer(this);
}

/**
 * Sets the expansion state of the DrawerFigure
 * 
 * @param expanded	<code>true</code> if the drawer is expanded; false otherwise.
 */
public void setExpanded(boolean expanded) {
	getDrawerFigure().setExpanded(expanded);
}

/**
 * @see org.eclipse.gef.internal.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	getDrawerFigure().setTitleIcon(image);
}

/**
 * Sets the drawer's pinned state to the specified value.
 * @param pinned <code>true</code> if the drawer should be pinned when opened
 */
public void setPinnedOpen(boolean pinned) {
	getDrawerFigure().setPinned(pinned);
}

/**
 * @see org.eclipse.gef.EditPart#setSelected(int)
 */
public void setSelected(int value) {
	super.setSelected(value);
	getDrawerFigure().getCollapseToggle().requestFocus();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
 */
protected void unregister() {
	getAnimationController().removeDrawer(this);
	super.unregister();
}

private class ToggleListener implements ChangeListener {
	public boolean internalChange = false;
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY) 
			&& !getAnimationController().isAnimationInProgress()) {
				getAnimationController().animate(DrawerEditPart.this);
		}
	}
}

}