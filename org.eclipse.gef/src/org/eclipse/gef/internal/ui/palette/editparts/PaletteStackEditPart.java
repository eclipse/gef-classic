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

import java.util.Iterator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.actions.SetActivePaletteToolAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * The EditPart for a PaletteStack.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStackEditPart 
	extends PaletteEditPart
{
	
private PropertyChangeListener paletteLayoutListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PaletteViewerPreferences.PREFERENCE_LAYOUT)) {
			int newLayoutMode = ((Integer)event.getNewValue()).intValue();
			int oldLayoutMode = -1;
			if (event.getOldValue() != null) 
				oldLayoutMode = ((Integer)event.getOldValue()).intValue();
			if (newLayoutMode == PaletteViewerPreferences.LAYOUT_LIST
					|| newLayoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
				if (oldLayoutMode == -1 
						|| oldLayoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS
						|| oldLayoutMode == PaletteViewerPreferences.LAYOUT_ICONS)
					getFigure().add(arrowFigure, BorderLayout.RIGHT);
			} else {
				if (oldLayoutMode == -1
						|| oldLayoutMode == PaletteViewerPreferences.LAYOUT_LIST
						|| oldLayoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
					getFigure().remove(arrowFigure);
			}
		}
	}
};

private PropertyChangeListener stackListener = new PropertyChangeListener() {
	
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(PaletteStack.PROPERTY_ACTIVE_ENTRY)) {
			GraphicalEditPart part = null;
			Clickable clickable = null;
			
			part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(event.getNewValue());
			clickable = (Clickable)part.getFigure();
			clickable.setVisible(true);
			clickable.addChangeListener(clickableListener);
			activeFigure = clickable;
			
			if (event.getOldValue() != null) {
				part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(event.getOldValue());
				clickable = (Clickable)part.getFigure();
				clickable.setVisible(false);
				clickable.removeChangeListener(clickableListener);
			} else {
				Iterator children = getChildren().iterator();
				while (children.hasNext()) {
					PaletteEditPart editPart = (PaletteEditPart)children.next();
					
					if (!editPart.equals(part))
						editPart.getFigure().setVisible(false);
				}
			}
			
		}
	}
};

private ChangeListener clickableListener = new ChangeListener() {
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals("mouseover"))
			arrowFigure.getModel().setMouseOver(activeFigure.getModel().isMouseOver());
		else if (event.getPropertyName().equals("armed"))
			arrowFigure.getModel().setArmed(activeFigure.getModel().isArmed());
	}
};

private ActionListener actionListener = new ActionListener() {
	public void actionPerformed(ActionEvent event) {
		openMenu();		
	}
};

private Clickable activeFigure;
private RolloverArrow arrowFigure;
private Figure contentsFigure;
private Menu menu;

private PaletteListener paletteListener = new PaletteListener() {
	
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (getStack().getChildren().contains(tool)) {
			if (!arrowFigure.getModel().isSelected())
				arrowFigure.getModel().setSelected(true);
			if (!getStack().getActiveEntry().equals(tool))
				getStack().setActiveEntry(tool);
		} else
			arrowFigure.getModel().setSelected(false);
	}	
};

/**
 * Creates a new PaletteStackEditPart with the given PaletteStack as its model.
 * 
 * @param model the PaletteStack to associate with this EditPart.
 */
public PaletteStackEditPart(PaletteStack model) {
	super(model);
	model.addPropertyChangeListener(stackListener);
}

/**
 * @see org.eclipse.gef.EditPart#activate()
 */
public void activate() {
	getStack().getActiveEntry();
	getPaletteViewer().addPaletteListener(paletteListener);
	getPaletteViewer().getPaletteViewerPreferences().addPropertyChangeListener(paletteLayoutListener);
	super.activate();
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
	Figure figure = new Figure() {
		public void paintBorder(Graphics graphics) {
			int layoutMode = getPreferenceSource().getLayoutSetting();
			if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
			  || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
				return;
			
			Rectangle rect = getBounds().getCopy();

			graphics.translate(getLocation());
			graphics.setBackgroundColor(ColorConstants.black);
			
			// fill the corner arrow
			int[] points = new int[6];
			
			points[0] = rect.width ;
			points[1] = rect.height - 5;
			points[2] = rect.width ;
			points[3] = rect.height;
			points[4] = rect.width - 5;
			points[5] = rect.height;
		
			graphics.fillPolygon(points);
			
			graphics.translate(getLocation().getNegated());
		}
	};
	figure.setLayoutManager(new BorderLayout());
	
	contentsFigure = new Figure();
	
	StackLayout stackLayout = new StackLayout();
	// make it so the stack layout does not allow the invisible figures to contribute
	// to its bounds
	stackLayout.setObserveVisibility(true);
	contentsFigure.setLayoutManager(stackLayout);
	
	figure.add(contentsFigure, BorderLayout.CENTER);
	
	arrowFigure = new RolloverArrow();
	
	arrowFigure.addActionListener(actionListener);

	int layoutMode = getPreferenceSource().getLayoutSetting();
	if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
	  || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS)
		figure.add(arrowFigure, BorderLayout.RIGHT);
	return figure;
}

/**
 * @see org.eclipse.gef.EditPart#deactivate()
 */
public void deactivate() {
	// remove active figure listner?
	arrowFigure.removeActionListener(actionListener);
	getStack().removePropertyChangeListener(stackListener);
	getPaletteViewer().removePaletteListener(paletteListener);
	getPaletteViewer().getPaletteViewerPreferences().removePropertyChangeListener(paletteLayoutListener);
	super.deactivate();
}

/**
 * @see org.eclipse.gef.EditPart#eraseTargetFeedback(org.eclipse.gef.Request)
 */
public void eraseTargetFeedback(Request request) {	
	Iterator children = getChildren().iterator();
	
	while (children.hasNext()) {
		PaletteEditPart part = (PaletteEditPart)children.next();
		part.eraseTargetFeedback(request);
	}
	super.eraseTargetFeedback(request);
}

/**
 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
 */
public IFigure getContentPane() {
	return contentsFigure;
}

private PaletteStack getStack() {
	return (PaletteStack)getModel();
}

/**
 * Opens the menu to display the choices for the active entry.
 */
public void openMenu() {
	MenuManager menuManager = new MenuManager();
	
	Iterator children = getChildren().iterator();
	PaletteEditPart part = null;
	PaletteEntry entry = null;
	while (children.hasNext()) {
		part = (PaletteEditPart)children.next();
		entry = (PaletteEntry)part.getModel();
		
		menuManager.add(new SetActivePaletteToolAction(getPaletteViewer(), entry.getLabel(), 
				entry.getSmallIcon(), 
				getStack().getActiveEntry().equals(entry), (ToolEntry)entry));
	}
	
	menu = menuManager.createContextMenu(getPaletteViewer().getControl());
	
	// make the menu open below the figure
	Rectangle figureBounds = getFigure().getBounds().getCopy();
	getFigure().translateToAbsolute(figureBounds);
	
	Point menuLocation = getPaletteViewer().getControl().toDisplay(
			figureBounds.getBottomLeft().x, figureBounds.getBottomLeft().y);
	
	// remove feedback from the arrow Figure and children figures
	arrowFigure.getModel().setMouseOver(false);
	eraseTargetFeedback(new Request(RequestConstants.REQ_SELECTION));
	
	menu.setLocation(menuLocation);

	menu.setVisible(true);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
 */
protected void refreshChildren() {
	super.refreshChildren();
	refreshVisuals();
}

/**
 * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
 */
public void showTargetFeedback(Request request) {
	// if menu is showing, don't show feedback. this is a fix
	// for the occashion when show is called after forced erase
	if (menu != null && !menu.isDisposed() && menu.isVisible())
		return;
		
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		PaletteEditPart part = (PaletteEditPart)children.next();
		part.showTargetFeedback(request);
	}
	
	super.showTargetFeedback(request);
}

}

class RolloverArrow
	extends Clickable 
{

/**
 * Creates a new Clickable with a TriangleFigure as its child.
 */
RolloverArrow() {
	super(new TriangleFigure());
	setRolloverEnabled(true);
	setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	setOpaque(false);
	setStyle(Clickable.STYLE_BUTTON);
}

/**
 * Draws a checkered pattern to emulate a toggle button that is in the selected state.
 * @param graphics	The Graphics object used to paint
 */
protected void fillCheckeredRectangle(Graphics graphics) {
	// method taken from ToggleButton - because this figure *isn't* a toggle button,
	// but should match the checkered look of the tool's toggle button
	graphics.setBackgroundColor(ColorConstants.button);
	graphics.setForegroundColor(ColorConstants.buttonLightest);
	Rectangle rect = getClientArea(Rectangle.SINGLETON).crop(new Insets(1, 1, 0, 0));
	graphics.fillRectangle(rect.x, rect.y, rect.width, rect.height);
	
	graphics.clipRect(rect);
	graphics.translate(rect.x, rect.y);
	int n = rect.width + rect.height;
	for (int i = 1; i < n; i += 2) {
		graphics.drawLine(0, i, i, 0);
	}
	graphics.restoreState();
}

/**
 * Return false so that the focus rectangle is not drawn.
 */
public boolean hasFocus() {
	return false;
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
 */
protected void paintFigure(Graphics graphics) {
	if (isSelected() && isOpaque())
		fillCheckeredRectangle(graphics);
	else
		super.paintFigure(graphics);
}

}

class TriangleFigure
	extends Figure 
{

/**
 * Creates a new TriangleFigure with preferred size 7, -1
 */
TriangleFigure() {
	super();
	setPreferredSize(7, - 1);
}

/**
 * @see org.eclipse.draw2d.IFigure#paint(org.eclipse.draw2d.Graphics)
 */
public void paint(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();

	graphics.translate(getLocation());
	graphics.setBackgroundColor(ColorConstants.black);
	
	// fill the arrow
	int[] points = new int[8];
	
	points[0] = 1;
	points[1] = rect.height / 2;
	points[2] = 6;
	points[3] = rect.height / 2;
	points[4] = 3;
	points[5] = 3 + rect.height / 2;
	points[6] = 1;
	points[7] = rect.height / 2;
	
	graphics.fillPolygon(points);
	
	graphics.translate(getLocation().getNegated());
}

}