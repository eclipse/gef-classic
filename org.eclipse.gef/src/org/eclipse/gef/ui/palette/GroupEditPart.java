package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.gef.palette.PaletteContainer;

public class GroupEditPart 
	extends PaletteEditPart 
{
	
private Figure innerFigure;
private int cachedLayout = -1;

public GroupEditPart(PaletteContainer group) {
	super(group);
}

public IFigure createFigure() {
	innerFigure = new Figure();
	innerFigure.setBorder(BORDER_TITLE_MARGIN);
	innerFigure.setOpaque(true);
	return innerFigure;
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	updateLayout();
}

protected void updateLayout() {
	int layout = getPreferenceSource().getLayoutSetting();
	if (cachedLayout == layout) {
		return;
	}
	
	cachedLayout = layout;
	
	LayoutManager manager;
	if (layout == PaletteViewerPreferences.LAYOUT_FOLDER) {
		manager = new FolderLayout();
	} else if (layout == PaletteViewerPreferences.LAYOUT_ICONS) {
		manager = new FlowLayout();
	} else {
		ToolbarLayout toolbarLayout = new ToolbarLayout();
		toolbarLayout.setSpacing(3);
		manager = toolbarLayout;
	}
	innerFigure.setLayoutManager(manager);
}

}