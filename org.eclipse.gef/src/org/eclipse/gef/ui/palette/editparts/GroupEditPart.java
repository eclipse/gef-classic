package org.eclipse.gef.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;

import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

public class GroupEditPart 
	extends PaletteEditPart 
{

private int cachedLayout = -1;

public GroupEditPart(PaletteContainer group) {
	super(group);
}

public IFigure createFigure() {
	return new GroupFigure();
}

/**
 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
 */
protected void refreshVisuals() {
	int layout = getPreferenceSource().getLayoutSetting();
	if (cachedLayout == layout)
		return;
	cachedLayout = layout;
	LayoutManager manager;
	if (layout == PaletteViewerPreferences.LAYOUT_FOLDER)
		manager = new FolderLayout();
	else if (layout == PaletteViewerPreferences.LAYOUT_ICONS)
		manager = new FlowLayout();
	else
		manager = new ToolbarLayout();
	getContentPane().setLayoutManager(manager);
}

}
