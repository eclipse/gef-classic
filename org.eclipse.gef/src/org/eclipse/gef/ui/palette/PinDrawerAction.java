package org.eclipse.gef.ui.palette;

import org.eclipse.jface.action.Action;

import org.eclipse.gef.internal.ui.palette.editparts.*;

/**
 * @author Pratik Shah
 */
public class PinDrawerAction extends Action {
	
private DrawerEditPart drawer;

/**
 * Constructor for PinDrawerAction
 * 
 * @param	drawer	The EditPart for the drawer that this action pins/unpins
 */
public PinDrawerAction (DrawerEditPart drawer) {
	this.drawer = drawer;
	setChecked(drawer.isPinnedOpen());
	setEnabled(drawer.isExpanded());
	setText(PaletteMessages.DRAWER_PIN);
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	drawer.setPinnedOpen(!drawer.isPinnedOpen());
}

}
