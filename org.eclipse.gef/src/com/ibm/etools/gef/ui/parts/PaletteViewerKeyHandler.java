package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.ui.palette.*;

/**
 * KeyHandler for the {@link com.ibm.etools.gef.ui.palette.PaletteViewerImpl Palette}.
 * Handles traversal of Palette entries and collapse/expand of
 * {@link com.ibm.etools.gef.ui.palette.CategoryEditPart categories}.
 */
public class PaletteViewerKeyHandler
	extends GraphicalViewerKeyHandler {

public PaletteViewerKeyHandler(PaletteViewer viewer){
	super(viewer);
}

private boolean acceptCollapseCategory(KeyEvent event){
	return event.keyCode == SWT.ARROW_LEFT
		&& isExpandedCategory(getFocus());
}

private boolean acceptExpandCategory(KeyEvent event){
	return event.keyCode == SWT.ARROW_RIGHT
		&& isCollapsedCategory(getFocus());
}

private boolean acceptIntoExpandedCategory(KeyEvent event){
	return (event.keyCode == SWT.ARROW_DOWN || event.keyCode == SWT.ARROW_RIGHT)
		&& isExpandedCategory(getFocus());
}

private boolean acceptSetFocusOnCategory(KeyEvent event){
	return event.keyCode == SWT.ARROW_LEFT;
}		

private void buildNavigationList(EditPart palettePart, EditPart exclusion, ArrayList navList){
	if (palettePart == exclusion)
		return;
	if (isCollapsedCategory(palettePart)){
		navList.add(palettePart);
		return;
	} 
	else if (palettePart instanceof EntryEditPart)
		navList.add(palettePart);

	for(int k=0; k<palettePart.getChildren().size(); k++){
		EditPart ep = (EditPart)(palettePart.getChildren().get(k));
		buildNavigationList(ep, exclusion, navList);
	}
}

private void collapseCategory(){
	CategoryEditPart category = (CategoryEditPart)getFocus();
	category.setExpanded(false);
}

private void expandCategory(){
	CategoryEditPart category = (CategoryEditPart)getFocus();
	category.setExpanded(true);
}

/**
 * Returns a list of {@link com.ibm.etools.gef.EditPart EditParts}
 * eligible for selection.
 */
List getNavigationSiblings(){
	ArrayList siblingsList = new ArrayList();
	EditPart exclude = null;
	if (isExpandedCategory(getFocus()))
		exclude = getFocus();
	buildNavigationList(getViewer().getContents(), exclude, siblingsList);
	return siblingsList;
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is a collapsed 
 * {@link com.ibm.etools.gef.ui.palette.CategoryEditPart Category},
 * false otherwise.
 */
boolean isCollapsedCategory(EditPart part){
	return part instanceof CategoryEditPart
		&& !((CategoryEditPart)part).isExpanded();
}

/**
 * Returns <code>true</code> if the passed
 * Editpart is an expanded 
 * {@link com.ibm.etools.gef.ui.palette.CategoryEditPart Category},
 * false otherwise.
 */
boolean isExpandedCategory(EditPart part){
	return part instanceof CategoryEditPart
		&& ((CategoryEditPart)part).isExpanded();
}

public boolean keyPressed(KeyEvent event) {
	if (acceptIntoExpandedCategory(event)){
		
		if (navigateIntoExpandedCategory(event))
			return true;
	}
	if (acceptCollapseCategory(event)){
		collapseCategory();
		return true;
	}
	if (acceptExpandCategory(event)){
		expandCategory();
		return true;
	}	
	if (acceptSetFocusOnCategory(event)){
		if (navigateToCategory(event))
			return true;
	}	
	return super.keyPressed(event);
}

private boolean navigateIntoExpandedCategory(KeyEvent event){
	ArrayList potentials = new ArrayList();
	buildNavigationList(getFocus(), null, potentials);
	if (!potentials.isEmpty()){
		navigateTo((EditPart)potentials.get(0), event);
		return true;
	}	
	return false;
}

private boolean navigateToCategory(KeyEvent event){
	boolean found = false;
	EditPart parent = getFocus().getParent();
	while(parent != null && !found){
		if (parent instanceof CategoryEditPart){
			navigateTo(parent,event);
			found = true;
		}
		parent = parent.getParent();
	}
	return false;
}

}