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
package org.eclipse.gef.ui.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.*;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * KeyHandler for the {@link org.eclipse.gef.ui.palette.PaletteViewer Palette}.
 * Handles selection traversal of Palette entries and collapse/expand of categories.
 */
public class PaletteViewerKeyHandler
	extends GraphicalViewerKeyHandler
{

/**
 * Constructs a key handler for the specified palette viewer.
 * @param viewer the palette viewer
 */
public PaletteViewerKeyHandler(PaletteViewer viewer) {
	super(viewer);
}

private boolean acceptCollapseDrawer(KeyEvent event) {
	return event.keyCode == SWT.ARROW_LEFT
		&& isExpandedDrawer(getFocusEditPart());
}

private boolean acceptExpandDrawer(KeyEvent event) {
	return event.keyCode == SWT.ARROW_RIGHT
		&& isCollapsedDrawer(getFocusEditPart());
}

private boolean acceptIntoExpandedDrawer(KeyEvent event) {
	return (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_RIGHT)
		&& isExpandedDrawer(getFocusEditPart());
}

private boolean acceptSetFocusOnDrawer(KeyEvent event) {
	return (event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_UP)
				&& getFocusEditPart().getParent() instanceof DrawerEditPart;
}		

private boolean acceptNextContainer(KeyEvent event) {
	return event.keyCode == SWT.ARROW_DOWN;
}		

private void buildNavigationList(
	EditPart palettePart,
	EditPart exclusion,
	ArrayList navList) {
	if (palettePart != exclusion) {
		if (isCollapsedDrawer(palettePart)) {
			navList.add(palettePart);
			return;
		} else if (palettePart instanceof ToolEntryEditPart 
		  || palettePart instanceof DrawerEditPart
		  || palettePart instanceof TemplateEditPart) {
			navList.add(palettePart);
		}
	}

	for (int k = 0; k < palettePart.getChildren().size(); k++) {
		EditPart ep = (EditPart)(palettePart.getChildren().get(k));
		buildNavigationList(ep, exclusion, navList);
	}
}

private void collapseDrawer() {
	DrawerEditPart drawer = (DrawerEditPart)getFocusEditPart();
	drawer.setExpanded(false);
}

private void expandDrawer() {
	DrawerEditPart drawer = (DrawerEditPart)getFocusEditPart();
	drawer.setExpanded(true);
}

protected Point getNavigationPoint(IFigure figure) {
	return figure.getBounds().getTopLeft();
}

/**
 * Returns a list of {@link org.eclipse.gef.EditPart EditParts}
 * eligible for selection.
 */
protected List getNavigationSiblings() {
	ArrayList siblingsList = new ArrayList();
	if (getFocusEditPart().getParent() instanceof GroupEditPart)
		buildNavigationList(
			getFocusEditPart().getParent().getParent(),
			getFocusEditPart().getParent().getParent(),
			siblingsList);
	else
		buildNavigationList(getFocusEditPart().getParent(), getFocusEditPart().getParent(), siblingsList);
	return siblingsList;
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is a collapsed 
 * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer}, false otherwise.
 */
boolean isCollapsedDrawer(EditPart part) {
	return part instanceof DrawerEditPart
		&& !((DrawerEditPart)part).isExpanded();
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is an expanded 
 * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer},
 * false otherwise.
 */
boolean isExpandedDrawer(EditPart part) {
	return part instanceof DrawerEditPart
		&& ((DrawerEditPart)part).isExpanded();
}

/**
 * Extends keyPressed to look for palette navigation keys.
 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
 */
public boolean keyPressed(KeyEvent event) {
	if (acceptExpandDrawer(event)) {
		expandDrawer();
		return true;
	}	
	if (acceptCollapseDrawer(event)) {
		collapseDrawer();
		return true;
	}
	if (acceptIntoExpandedDrawer(event)) {
		if (navigateIntoExpandedDrawer(event))
			return true;
	}
	if (super.keyPressed(event))
		return true;
	if (acceptSetFocusOnDrawer(event)) {
		if (navigateToDrawer(event))
			return true;
	}
	if (acceptNextContainer(event)) {
		if (navigateToNextContainer(event))
			return true;
	}
	return false;
}

private boolean navigateIntoExpandedDrawer(KeyEvent event) {
	ArrayList potentials = new ArrayList();
	buildNavigationList(getFocusEditPart(), getFocusEditPart(), potentials);
	if (!potentials.isEmpty()) {
		navigateTo((EditPart)potentials.get(0), event);
		return true;
	}	
	return false;
}

protected void navigateTo(EditPart part, KeyEvent event) {
	if (part == null)
		return;
	getViewer().select(part);
	getViewer().reveal(part);
}

private boolean navigateToDrawer(KeyEvent event) {
	boolean found = false;
	EditPart parent = getFocusEditPart().getParent();
	while (parent != null && !found) {
		if (parent instanceof DrawerEditPart) {
			navigateTo(parent, event);
			found = true;
		}
		parent = parent.getParent();
	}
	return false;
}

private boolean navigateToNextContainer(KeyEvent event) {
	EditPart current = getFocusEditPart();
	while (current != null) {
		if (current instanceof DrawerEditPart	|| current instanceof GroupEditPart) {
			List siblings = current.getParent().getChildren();
			int index = siblings.indexOf(current);
			if (index != -1 && siblings.size() > index + 1) {
				EditPart part = (EditPart)siblings.get(index + 1);
				if (part instanceof GroupEditPart && part.getChildren().size() > 0) {
					EditPart child = (EditPart)part.getChildren().get(0);
					navigateTo(child, event);
				} else
					navigateTo(part, event);
				return true;
			}
			return false;
		}
		current = current.getParent();
	}
	return false;
}

}