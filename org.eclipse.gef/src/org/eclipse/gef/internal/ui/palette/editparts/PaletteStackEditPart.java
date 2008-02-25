/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.MenuManager;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ButtonBorder;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.actions.SetActivePaletteToolAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.editparts.PaletteEditPart;

/**
 * The EditPart for a PaletteStack.
 * 
 * @author Whitney Sorenson
 * @since 3.0
 */
public class PaletteStackEditPart 
	extends PaletteEditPart
{
	
private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);
private static final Border BORDER_TOGGLE = new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR);

// listen to changes of clickable tool figure
private ChangeListener clickableListener = new ChangeListener() {
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY))
			getClickableFigure().getModel().setMouseOver(activeFigure.getModel().isMouseOver());
		else if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY))
			getClickableFigure().getModel().setArmed(activeFigure.getModel().isArmed());
	}
};

// listen to changes of arrow figure
private ChangeListener clickableArrowListener = new ChangeListener() {
	public void handleStateChanged(ChangeEvent event) {
		if (event.getPropertyName().equals(ButtonModel.MOUSEOVER_PROPERTY))
			activeFigure.getModel().setMouseOver(getClickableFigure().getModel().isMouseOver());
		if (event.getPropertyName().equals(ButtonModel.ARMED_PROPERTY))
			activeFigure.getModel().setArmed(getClickableFigure().getModel().isArmed());
	}
};

// listen to see if arrow is pressed
private ActionListener actionListener = new ActionListener() {
	public void actionPerformed(ActionEvent event) {
		openMenu();		
	}
};

// listen to see if active tool is changed in palette
private PaletteListener paletteListener = new PaletteListener() {
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (getStack().getChildren().contains(tool)) {
			if (!getClickableFigure().getModel().isSelected())
				getClickableFigure().getModel().setSelected(true);
			if (!getStack().getActiveEntry().equals(tool))
				getStack().setActiveEntry(tool);
		} else
			getClickableFigure().getModel().setSelected(false);
	}	
};

private Clickable activeFigure;
private Clickable clickableFigure;
private RolloverArrow arrowFigure;
private Figure contentsFigure;
private Menu menu;

/**
 * Creates a new PaletteStackEditPart with the given PaletteStack as its model.
 * 
 * @param model the PaletteStack to associate with this EditPart.
 */
public PaletteStackEditPart(PaletteStack model) {
	super(model);
}

/**
 * @see org.eclipse.gef.EditPart#activate()
 */
public void activate() {
	// in case the model is out of sync
	checkActiveEntrySync();
	getPaletteViewer().addPaletteListener(paletteListener);
	super.activate();
}

/**
 * Called when the active entry has changed.
 * 
 * @param oldValue the old model value (can be null)
 * @param newValue the new model value (can be null)
 */
private void activeEntryChanged(Object oldValue, Object newValue) {
	GraphicalEditPart part = null;
	Clickable clickable = null;

	if (newValue != null) {
		part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(newValue);
		clickable = (Clickable)part.getFigure();
		clickable.setVisible(true);
		clickable.addChangeListener(clickableListener);
		activeFigure = clickable;
	} else {
	    activeFigure = null;
	}

	if (oldValue != null) {
		part = (GraphicalEditPart)getViewer().getEditPartRegistry().get(oldValue);
		// if part is null, its no longer a child.
		if (part != null) {
			clickable = (Clickable)part.getFigure();
			clickable.setVisible(false);
			clickable.removeChangeListener(clickableListener);
		}
	}
}

private void checkActiveEntrySync() {
	if (activeFigure == null)
		activeEntryChanged(null, getStack().getActiveEntry());
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
 */
public IFigure createFigure() {
    
    IFigure stackFigure;
    arrowFigure = new RolloverArrow();

    if (isToolbarItem()) {
        // the entire stack figure is clickable on the toolbar
        stackFigure = new Clickable() {
            public boolean hasFocus() {
                return false;
            }
            public Dimension getPreferredSize(int wHint, int hHint) {
                if (PaletteStackEditPart.this.getChildren().isEmpty())
                    return EMPTY_DIMENSION;
                return super.getPreferredSize(wHint, hHint);
            }
        };
        ((Clickable)stackFigure).setRolloverEnabled(true);
        stackFigure.setBorder(BORDER_TOGGLE);
        
        // Set up the arrow figure.  Disable the arrow figure so clicks go to the stack figure. 
        arrowFigure.setBackgroundColor(ColorConstants.black);
        arrowFigure.setEnabled(false);
        
        clickableFigure = ((Clickable)stackFigure);        
    } else {
        // the stack figure is not clickable on the palette so that drag and drop still works
        stackFigure = new Figure() {
            public Dimension getPreferredSize(int wHint, int hHint) {
                if (PaletteStackEditPart.this.getChildren().isEmpty())
                    return EMPTY_DIMENSION;
                return super.getPreferredSize(wHint, hHint);
            }
        };
        
        // Set up the arrow figure. 
        arrowFigure.setBackgroundColor(PaletteColorUtil.WIDGET_DARK_SHADOW);
        
        clickableFigure = arrowFigure;        
    }
 	
	contentsFigure = new Figure();
	StackLayout stackLayout = new StackLayout();
	// make it so the stack layout does not allow the invisible figures to contribute
	// to its bounds
	stackLayout.setObserveVisibility(true);
	contentsFigure.setLayoutManager(stackLayout);
	
	stackFigure.add(contentsFigure);
	stackFigure.add(arrowFigure);
    
    getClickableFigure().addChangeListener(clickableArrowListener);
    getClickableFigure().addActionListener(actionListener);

    return stackFigure;
}

/**
 * Returns the <code>Clickable</code> figure. This differs depending on
 * whether or not this palette stack is on the palette toolbar.
 * 
 * @return the <code>Clickable</code> figure
 */
private Clickable getClickableFigure() {
    return clickableFigure;
}

/**
 * @see org.eclipse.gef.EditPart#deactivate()
 */
public void deactivate() {
	if (activeFigure != null) 
		activeFigure.removeChangeListener(clickableListener);
	getClickableFigure().removeActionListener(actionListener);
	getClickableFigure().removeChangeListener(clickableArrowListener);
	getPaletteViewer().removePaletteListener(paletteListener);
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
		
		menuManager.add(new SetActivePaletteToolAction(getPaletteViewer(), 
				entry.getLabel(), entry.getSmallIcon(), 
				getStack().getActiveEntry().equals(entry), (ToolEntry)entry));
	}
	
	menu = menuManager.createContextMenu(getPaletteViewer().getControl());
	
	// make the menu open below the figure
	Rectangle figureBounds = getFigure().getBounds().getCopy();
	getFigure().translateToAbsolute(figureBounds);
	
	Point menuLocation = getPaletteViewer().getControl().toDisplay(
			figureBounds.getBottomLeft().x, figureBounds.getBottomLeft().y);
	
	// remove feedback from the arrow Figure and children figures
	getClickableFigure().getModel().setMouseOver(false);
	eraseTargetFeedback(new Request(RequestConstants.REQ_SELECTION));
	
	menu.setLocation(menuLocation);
	menu.addMenuListener(new StackMenuListener(menu, getViewer().getControl().getDisplay()));
	menu.setVisible(true);
}

/**
 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
 */
public void propertyChange(PropertyChangeEvent event) {
	if (event.getPropertyName().equals(PaletteStack.PROPERTY_ACTIVE_ENTRY))
		activeEntryChanged(event.getOldValue(), event.getNewValue());
	else
		super.propertyChange(event);
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
 */
protected void refreshChildren() {
	super.refreshChildren();
	checkActiveEntrySync();
	Iterator children = getChildren().iterator();
	while (children.hasNext()) {
		PaletteEditPart editPart = (PaletteEditPart)children.next();
		if (!editPart.getFigure().equals(activeFigure))
			editPart.getFigure().setVisible(false);
	}
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
    int layoutMode = getLayoutSetting();
    if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST
            || layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
        getFigure().setLayoutManager(new StackLayout() {
            public void layout(IFigure figure) {
                Rectangle r = figure.getClientArea();
                List children = figure.getChildren();
                IFigure child;
                for (int i = 0; i < children.size(); i++) {
                    child = (IFigure)children.get(i);
                    if (child == arrowFigure) {
                        Rectangle.SINGLETON.setBounds(r);
                        Rectangle.SINGLETON.width = 11;
                        child.setBounds(Rectangle.SINGLETON);
                    } else {
                        child.setBounds(r);
                    }
                }
            }
        });
    } else {
        getFigure().setLayoutManager(new BorderLayout());
        getFigure().setConstraint(contentsFigure, BorderLayout.CENTER);
        getFigure().setConstraint(arrowFigure, BorderLayout.RIGHT);    
    }
}

/**
 * @see org.eclipse.gef.EditPart#showTargetFeedback(org.eclipse.gef.Request)
 */
public void showTargetFeedback(Request request) {
	// if menu is showing, don't show feedback. this is a fix
	// for the occasion when show is called after forced erase
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

class StackMenuListener 
	implements MenuListener
{

private Menu menu;
private Display d;

/**
 * Creates a new listener to listen to the menu that it used to select the active tool
 * on a stack. Disposes the stack with an asyncExec after hidden is called.
 */
StackMenuListener(Menu menu, Display d) {
	this.menu = menu;
	this.d = d;
}

/**
 * @see org.eclipse.swt.events.MenuListener#menuShown(org.eclipse.swt.events.MenuEvent)
 */
public void menuShown(MenuEvent e) { }

/**
 * @see org.eclipse.swt.events.MenuListener#menuHidden(org.eclipse.swt.events.MenuEvent)
 */
public void menuHidden(MenuEvent e) {
	d.asyncExec(new Runnable() {
		public void run() {
			if (menu != null) {
				if (!menu.isDisposed())
					menu.dispose();
				menu = null;
			}
		}
	});
}

}

class RolloverArrow
	extends Clickable 
{

/**
 * Creates a new Clickable that paints a triangle figure.
 */
RolloverArrow() {
	super();
	setRolloverEnabled(true);
	setBorder(null);
	setOpaque(false);
	setPreferredSize(11, -1);
}

/**
 * @return false so that the focus rectangle is not drawn.
 */
public boolean hasFocus() {
	return false;
}

public void paintFigure(Graphics graphics) {
	Rectangle rect = getBounds().getCopy();

	graphics.translate(getLocation());
	
	// fill the arrow
	int[] points = new int[8];
	
	points[0] = 4;
	points[1] = rect.height / 2;
	points[2] = 9;
	points[3] = rect.height / 2;
	points[4] = 6;
	points[5] = 3 + rect.height / 2;
	points[6] = 4;
	points[7] = rect.height / 2;
	
	graphics.fillPolygon(points);
	
	graphics.translate(getLocation().getNegated());
}

}
