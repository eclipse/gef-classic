package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.ui.palette.editparts.*;

/**
 * @author Pratik Shah
 */
public class PinCategoryAction extends Action {
	
private DrawerEditPart category;

/**
 * Constructor for PinCategoryAction.
 * @param fig
 */
public PinCategoryAction (DrawerEditPart category) {
	this.category = category;
	setChecked(category.isPinnedOpen());
	setEnabled(category.isExpanded());
	setText(PaletteMessages.CATEGORY_PIN);
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	category.setPinnedOpen(!category.isPinnedOpen());
}

}
