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
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.TemplateEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * KeyHandler for the {@link org.eclipse.gef.ui.palette.PaletteViewer Palette}.
 * Handles traversal of Palette entries and collapse/expand of
 * {@link org.eclipse.gef.ui.palette.DrawerEditPart categories}.
 */
public class PaletteViewerKeyHandler
	extends GraphicalViewerKeyHandler {

public PaletteViewerKeyHandler(PaletteViewer viewer){
	super(viewer);
}

private boolean acceptCollapseDrawer(KeyEvent event){
	return event.keyCode == SWT.ARROW_LEFT
		&& isExpandedDrawer(getFocus());
}

private boolean acceptExpandDrawer(KeyEvent event){
	return event.keyCode == SWT.ARROW_RIGHT
		&& isCollapsedDrawer(getFocus());
}

private boolean acceptIntoExpandedDrawer(KeyEvent event){
	return (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_RIGHT)
		&& isExpandedDrawer(getFocus());
}

private boolean acceptSetFocusOnDrawer(KeyEvent event) {
	return (event.keyCode == SWT.ARROW_LEFT || event.keyCode == SWT.ARROW_UP)
				&& getFocus().getParent() instanceof DrawerEditPart;
}		

private boolean acceptNextContainer(KeyEvent event) {
	return event.keyCode == SWT.ARROW_DOWN;
}		

private void buildNavigationList(EditPart palettePart, EditPart exclusion, ArrayList navList) {
	if (palettePart != exclusion) {
		if (isCollapsedDrawer(palettePart)) {
			navList.add(palettePart);
			return;
		} 
		else if (palettePart instanceof ToolEntryEditPart 
				|| palettePart instanceof DrawerEditPart
				|| palettePart instanceof TemplateEditPart)
			navList.add(palettePart);
	}

	for (int k=0; k<palettePart.getChildren().size(); k++) {
		EditPart ep = (EditPart)(palettePart.getChildren().get(k));
		buildNavigationList(ep, exclusion, navList);
	}
}

private void collapseDrawer(){
	DrawerEditPart drawer = (DrawerEditPart)getFocus();
	drawer.setExpanded(false);
}

private void expandDrawer(){
	DrawerEditPart drawer = (DrawerEditPart)getFocus();
	drawer.setExpanded(true);
}

Point getInterestingPoint(IFigure figure) {
	return figure.getBounds().getTopLeft();
}

/**
 * Returns a list of {@link org.eclipse.gef.EditPart EditParts}
 * eligible for selection.
 */
List getNavigationSiblings(){
	ArrayList siblingsList = new ArrayList();
	if (getFocus().getParent() instanceof GroupEditPart)
		buildNavigationList(getFocus().getParent().getParent(), getFocus().getParent().getParent(), siblingsList);
	else
		buildNavigationList(getFocus().getParent(), getFocus().getParent(), siblingsList);
	return siblingsList;
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is a collapsed 
 * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer}, false otherwise.
 */
boolean isCollapsedDrawer(EditPart part){
	return part instanceof DrawerEditPart
		&& !((DrawerEditPart)part).isExpanded();
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is an expanded 
 * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer},
 * false otherwise.
 */
boolean isExpandedDrawer(EditPart part){
	return part instanceof DrawerEditPart
		&& ((DrawerEditPart)part).isExpanded();
}

public boolean keyPressed(KeyEvent event) {
	if (!getFocus().getFigure().hasFocus())
		getFocus().getFigure().internalGetEventDispatcher().requestFocus(getFocus().getFigure());
	if (acceptExpandDrawer(event)) {
		expandDrawer();
		return true;
	}	
	if (acceptCollapseDrawer(event)) {
		collapseDrawer();
		return true;
	}
	if (super.keyPressed(event))
		return true;
	if (acceptIntoExpandedDrawer(event)) {
		if (navigateIntoExpandedDrawer(event))
			return true;
	}
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

private boolean navigateIntoExpandedDrawer(KeyEvent event){
	ArrayList potentials = new ArrayList();
	buildNavigationList(getFocus(), getFocus(), potentials);
	if (!potentials.isEmpty()){
		navigateTo((EditPart)potentials.get(0), event);
		return true;
	}	
	return false;
}

void navigateTo(EditPart part, KeyEvent event) {
	if (part == null)
		return;
	getViewer().select(part);
	getViewer().reveal(part);
}

private boolean navigateToDrawer(KeyEvent event){
	boolean found = false;
	EditPart parent = getFocus().getParent();
	while(parent != null && !found){
		if (parent instanceof DrawerEditPart){
			navigateTo(parent,event);
			found = true;
		}
		parent = parent.getParent();
	}
	return false;
}

private boolean navigateToNextContainer(KeyEvent event) {
	EditPart current = getFocus();
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