package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

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
protected DrawerFigure getDrawerFigure() {
	return (DrawerFigure)getFigure();
}

/**
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return getDrawerFigure().getContentPane();
}

private DrawerAnimationController getAnimationController() {
	DrawerAnimationController controller;
	controller = (DrawerAnimationController)getViewer()
		.getEditPartRegistry()
		.get(DrawerAnimationController.class);
	if (controller == null) {
		controller = new DrawerAnimationController(getPreferenceSource());
		getViewer().getEditPartRegistry().put(
			DrawerAnimationController.class, 
			controller);
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
 * @return boolean */
public boolean isPinnedOpen() {
	return getDrawerFigure().isPinnedOpen();
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
 * Sets the minimum preferred size of certain figures, as
 * not doing so results in the figure's minimum size being
 * its preferred sizes, or the preferred size of the scrollbar's
 * which in-turn doesnt allow for compression.
 * Sometimes the scrollbar's minimum size if far more than
 * the compression desired, hence specific setting is required.
 * 
 * @see #refreshChildren()
 */
public void refreshChildren() {
	super.refreshChildren();
//	Dimension minSize = collapseToggle.getPreferredSize();
//	minSize.height+=getFigure().getInsets().getHeight();
//	getFigure().setMinimumSize(new Dimension(0,minSize.height));
//	scrollpane.setMinimumSize(new Dimension(0,0));
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	// Do not call super.refreshVisuals()
	// That will update the Tooltip for the DrawerFigure.  But DrawerFigure has its
	// own tooltip that is displayed when the text in the header is truncated.
	boolean showPin = getPreferenceSource().getAutoCollapseSetting() == 
					PaletteViewerPreferences.COLLAPSE_AS_NEEDED;
	getDrawerFigure().setTitle(getPaletteEntry().getLabel());
	setImageDescriptor(getPaletteEntry().getSmallIcon());
	getDrawerFigure().setLayoutMode(getPreferenceSource().getLayoutSetting());
	getDrawerFigure().showPin(showPin);
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
 * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	getDrawerFigure().setTitleIcon(image);
}

/**
 * Sets the drawer's pinned state to the specified value.
 * @param pinned <code>true</code> if the drawer should be pinned when opened */
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