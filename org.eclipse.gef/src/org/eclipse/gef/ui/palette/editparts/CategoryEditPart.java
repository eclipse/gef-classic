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
import org.eclipse.gef.palette.PaletteCategory;

/**
 * EditPart for a PaletteCategory
 * 
 * @author Pratik Shah
 */
public class CategoryEditPart 
	extends PaletteEditPart
{

/**
 * Constructor
 * 
 * @param category	The PaletteCategory that this EditPart is representing
 */
public CategoryEditPart(PaletteCategory category) {
	super(category);
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
	CategoryFigure fig = new CategoryFigure(getViewer().getControl());
	fig.setExpanded(getCategory().isInitiallyOpen());
	fig.setPinned(getCategory().isInitiallyPinned());
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
 * Convenience method that provides access to the PaletteCategory that is the model.
 * @return The model PaletteCategory
 */
public PaletteCategory getCategory() {
	return (PaletteCategory)getPaletteEntry();
}

/**
 * Convenience method to get the CategoryFigure for the model category.
 * 
 * @return The CategoryFigure created in {@link #createFigure()}
 */
protected CategoryFigure getCategoryFigure() {
	return (CategoryFigure)getFigure();
}

/**
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return getCategoryFigure().getContentPane();
}

private CategoryAnimationController getAnimationController() {
	CategoryAnimationController controller;
	controller = (CategoryAnimationController)getViewer()
		.getEditPartRegistry()
		.get(CategoryAnimationController.class);
	if (controller == null) {
		controller = new CategoryAnimationController(getPreferenceSource());
		getViewer().getEditPartRegistry().put(
			CategoryAnimationController.class, 
			controller);
	}
	return controller;
}

/**
 * Returns the expansion state of the category
 * @return <code>true</code> if the category is expanded; false otherwise
 */
public boolean isExpanded() {
	return getCategoryFigure().isExpanded();
}

/**
 * Returns <code>true</code> if the category is pinned open.
 * @return boolean */
public boolean isPinnedOpen() {
	return getCategoryFigure().isPinnedOpen();
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
	// That will update the Tooltip for the CategoryFigure.  But CategoryFigure has its
	// own tooltip that is displayed when the text in the header is truncated.
	getCategoryFigure().setTitle(getPaletteEntry().getLabel());
	setImageDescriptor(getPaletteEntry().getSmallIcon());
	getCategoryFigure().setLayoutMode(getPreferenceSource().getLayoutSetting());
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
 */
protected void register() {
	super.register();
	getAnimationController().addCategory(this);
}


/**
 * Sets the expansion state of the CategoryFigure
 * 
 * @param expanded	<code>true</code> if the category is expanded; false otherwise.
 */
public void setExpanded(boolean expanded) {
	getCategoryFigure().setExpanded(expanded);
}

/**
 * @see org.eclipse.gef.ui.palette.editparts.PaletteEditPart#setImageInFigure(Image)
 */
protected void setImageInFigure(Image image) {
	getCategoryFigure().setTitleIcon(image);
}

/**
 * Sets the category's pinned state to the specified value.
 * @param pinned <code>true</code> if the category should be pinned when opened */
public void setPinnedOpen(boolean pinned) {
	getCategoryFigure().setPinned(pinned);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#unregister()
 */
protected void unregister() {
	getAnimationController().removeCategory(this);
	super.unregister();
}

private class ToggleListener implements ChangeListener {
	public boolean internalChange = false;
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY) 
			&& !getAnimationController().isAnimationInProgress()) {
				getAnimationController().animate(CategoryEditPart.this);
		}
	}
}

}