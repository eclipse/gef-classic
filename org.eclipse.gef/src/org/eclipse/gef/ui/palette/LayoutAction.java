package org.eclipse.gef.ui.palette;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.*;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Pratik Shah
 */
public class LayoutAction 
	extends Action 
	implements IMenuCreator
{

private PaletteViewerPreferences prefs;
private List actions;

/**
 * Constructor
 */
public LayoutAction(PaletteViewerPreferences prefs) {
	super(PaletteMessages.LAYOUT_MENU_LABEL);
	this.prefs = prefs;
	actions = createActions();
	setMenuCreator(this);
}

protected void addActionToMenu(Menu parent, IAction action) {
	ActionContributionItem item = new ActionContributionItem(action);
	item.fill(parent, -1);
}

/**
 * Method createActions.
 * @return List
 */
protected List createActions() {
	ArrayList list = new ArrayList();
	
	Action action = new LayoutChangeAction(PaletteViewerPreferences.LAYOUT_FOLDER);
	action.setText(PaletteMessages.SETTINGS_FOLDER_VIEW_LABEL);
	list.add(action);
	
	action = new LayoutChangeAction(PaletteViewerPreferences.LAYOUT_LIST);
	action.setText(PaletteMessages.SETTINGS_LIST_VIEW_LABEL);
	list.add(action);

	action = new LayoutChangeAction(PaletteViewerPreferences.LAYOUT_ICONS);
	action.setText(PaletteMessages.SETTINGS_ICONS_VIEW_LABEL);
	list.add(action);
	
	return list;
}

/**
 * Empty method
 * 
 * @see org.eclipse.jface.action.IMenuCreator#dispose()
 */
public void dispose() {
}

/**
 * Empty method
 * 
 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Control)
 */
public Menu getMenu(Control parent) {
	return null;
}

/**
 * @see org.eclipse.jface.action.IMenuCreator#getMenu(Menu)
 */
public Menu getMenu(Menu parent) {
	Menu menu = new Menu(parent);

	for (Iterator iter = actions.iterator(); iter.hasNext();) {
		LayoutChangeAction action = (LayoutChangeAction) iter.next();
		action.setChecked(prefs.getLayoutSetting() == action.getLayoutSetting());
		addActionToMenu(menu, action);
	}	
	
	setEnabled(!actions.isEmpty());
	return menu;
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	super.run();
}

private class LayoutChangeAction extends Action {
	private int value;
	public LayoutChangeAction(int layoutSetting) {
		value = layoutSetting;
	}
	public int getLayoutSetting() {
		return value;
	}
	public void run() {
		prefs.setLayoutSetting(value);
	}
}

}
