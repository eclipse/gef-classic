/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette;

import java.util.ResourceBundle;

/**
 * @author Pratik Shah
 */
public class PaletteMessages {

/**
 * The String "Customize Palette"
 */
public static final String CUSTOMIZE_DIALOG_TITLE;
/**
 * The String "Drawer options:  "
 */
public static final String COLLAPSE_OPTIONS_TITLE;
/**
 * The String "Layout options"
 */
public static final String SETTINGS_LAYOUT_OPTIONS_TITLE;
/**
 * The String "&Apply"
 */
public static final String APPLY_LABEL;
/**
 * The String "Move &Up"
 */
public static final String MOVE_UP_LABEL;
/**
 * The String "Move Do&wn"
 */
public static final String MOVE_DOWN_LABEL;
/**
 * The String "&Delete"
 */
public static final String DELETE_LABEL;
/**
 * The String "&New"
 */
public static final String NEW_LABEL;
/**
 * The String "&Columns"
 */
public static final String SETTINGS_COLUMNS_VIEW_LABEL;
/**
 * The String "&List"
 */
public static final String SETTINGS_LIST_VIEW_LABEL;
/**
 * The String "&Use large icons"
 */
public static final String SETTINGS_USE_LARGE_ICONS_LABEL;
/**
 * The String "&Use Large Icons"
 */
public static final String SETTINGS_USE_LARGE_ICONS_LABEL_CAPS;
/**
 * The String "&Icons only"
 */
public static final String SETTINGS_ICONS_VIEW_LABEL;
/**
 * The String "&Icons Only"
 */
public static final String SETTINGS_ICONS_VIEW_LABEL_CAPS;
/**
 * The String "&Never close"
 */
public static final String COLLAPSE_NEVER_LABEL;
/**
 * The String "&Always close when opening another drawer"
 */
public static final String COLLAPSE_ALWAYS_LABEL;
/**
 * The String "Close automatically &when there is not enough room"
 */
public static final String COLLAPSE_AS_NEEDED_LABEL;
/**
 * The String "Nothing Selected"
 */
public static final String NO_SELECTION_TITLE;
/**
 * The String "Na&me:  "
 */
public static final String NAME_LABEL;
/**
 * The String "Des&cription:  "
 */
public static final String DESCRIPTION_LABEL;
/**
 * The String "&Hide"
 */
public static final String HIDDEN_LABEL;
/**
 * The String "No description is available."
 */
public static final String NO_DESCRIPTION_AVAILABLE;
/**
 * The String "Select a node from the tree on the left."
 */
public static final String NO_SELECTION_MADE;
/**
 * The String "Cus&tomize..."
 */
public static final String MENU_OPEN_CUSTOMIZE_DIALOG;
/**
 * The String "Drawer"
 */
public static final String MODEL_TYPE_DRAWER;
/**
 * The String "Separator"
 */
public static final String MODEL_TYPE_SEPARATOR;
/**
 * The String "Stack"
 */
public static final String MODEL_TYPE_STACK;
/**
 * The String "Separates entries in a palette container"
 */
public static final String NEW_SEPARATOR_DESC;
/**
 * The String "New Drawer"
 */
public static final String NEW_DRAWER_LABEL;
/**
 * The String "[Separator]"
 */
public static final String NEW_SEPARATOR_LABEL;
/**
 * The String "New Stack"
 */
public static final String NEW_STACK_LABEL;
/**
 * The String "Group"
 */
public static final String MODEL_TYPE_GROUP;
/**
 * The String "New Group"
 */
public static final String NEW_GROUP_LABEL;
/**
 * The String "&Layout"
 */
public static final String LAYOUT_MENU_LABEL;
/**
 * The String "Could Not Accept Changes"
 */
public static final String ERROR;
/**
 * The String "The currently displayed page contains invalid values."
 */
public static final String ABORT_PAGE_FLIPPING_MESSAGE;
/**
 * The String "&Settings..."
 */
public static final String MENU_OPEN_SETTINGS_DIALOG;
/**
 * The String "Palette Settings"
 */
public static final String SETTINGS_DIALOG_TITLE;
/**
 * The String "&Details"
 */
public static final String SETTINGS_DETAILS_VIEW_LABEL;
/**
 * The String "Font"
 */
public static final String SETTINGS_FONT_TITLE;
/**
 * The String "C&hange..."
 */
public static final String SETTINGS_FONT_CHANGE;
/**
 * The String "Font:  "
 */
public static final String SETTINGS_FONT_CURRENT;
/**
 * The String "Layout:  "
 */
public static final String SETTINGS_LAYOUT_TITLE;
/**
 * The String "Drawer Options"
 */
public static final String SETTINGS_DRAWER_OPTIONS_TITLE;
/**
 * The String "&Open drawer at start-up"
 */
public static final String EXPAND_DRAWER_AT_STARTUP_LABEL;
/**
 * The String "Columns layout options"
 */
public static final String SETTINGS_OPTIONS_COLUMNS;
/**
 * The String "List layout options"
 */
public static final String SETTINGS_OPTIONS_LIST;
/**
 * The String " layout options"
 */
public static final String SETTINGS_OPTIONS_ICONS_ONLY;
/**
 * The String "Details layout options"
 */
public static final String SETTINGS_OPTIONS_DETAILS;
/**
 * The String "&Override default column width settings"
 */
public static final String SETTINGS_LAYOUT_COLUMNS_OVERRIDE_WIDTH;
/**
 * The String "Colu&mn width (in pixels):  "
 */
public static final String SETTINGS_LAYOUT_COLUMNS_WIDTH;
/**
 * The String "&Pin drawer open at start-up"
 */
public static final String DRAWER_PIN_AT_STARTUP;
/**
 * The String "&Restore Default"
 */
public static final String SETTINGS_DEFAULT_FONT;
/**
 * The String "<Using Workbench Dialog Font>"
 */
public static final String SETTINGS_WORKBENCH_FONT_LABEL;
/**
 * The String "-"
 */
public static final String NAME_DESCRIPTION_SEPARATOR;
/**
 * The String "&Pinned"
 */
public static final String PINNED;
/**
 * The String "Pin Open"
 */
public static final String TOOLTIP_PIN_FIGURE;

/**
 * The String "&Dock On"
 */
public static final String DOCK_LABEL;

/**
 * The String "&Left"
 */
public static final String LEFT_LABEL;

/**
 * The String "&Right"
 */
public static final String RIGHT_LABEL;

/**
 * The String "&Resize"
 */
public static final String RESIZE_LABEL;

/**
 * Show Palette
 */
public static final String PALETTE_SHOW;

/**
 * Hide Palette
 */
public static final String PALETTE_HIDE;

/**
 * This button is used to show or hide the palette.
 */
public static final String ACC_DESC_PALETTE_BUTTON;

/**
 * The palette can be moved or resized through the context menu for this control.
 */
public static final String ACC_DESC_PALETTE_TITLE;

static {
	ResourceBundle bundle =
		ResourceBundle.getBundle("org.eclipse.gef.ui.palette.messages"); //$NON-NLS-1$

	CUSTOMIZE_DIALOG_TITLE = bundle.getString("CUSTOMIZE_DIALOG_TITLE"); //$NON-NLS-1$
	COLLAPSE_OPTIONS_TITLE = bundle.getString("COLLAPSE_OPTIONS_TITLE"); //$NON-NLS-1$
	SETTINGS_LAYOUT_OPTIONS_TITLE 
					= bundle.getString("SETTINGS_LAYOUT_OPTIONS_TITLE"); //$NON-NLS-1$
	MOVE_UP_LABEL = bundle.getString("MOVE_UP_LABEL"); //$NON-NLS-1$
	MOVE_DOWN_LABEL = bundle.getString("MOVE_DOWN_LABEL"); //$NON-NLS-1$
	DELETE_LABEL = bundle.getString("DELETE_LABEL"); //$NON-NLS-1$
	NEW_LABEL = bundle.getString("NEW_LABEL"); //$NON-NLS-1$
	SETTINGS_COLUMNS_VIEW_LABEL 
					= bundle.getString("SETTINGS_COLUMNS_VIEW_LABEL"); //$NON-NLS-1$
	SETTINGS_LIST_VIEW_LABEL = bundle.getString("SETTINGS_LIST_VIEW_LABEL"); //$NON-NLS-1$
	SETTINGS_USE_LARGE_ICONS_LABEL 
					= bundle.getString("SETTINGS_USE_LARGE_ICONS_LABEL"); //$NON-NLS-1$
	SETTINGS_ICONS_VIEW_LABEL 
					= bundle.getString("SETTINGS_ICONS_VIEW_LABEL"); //$NON-NLS-1$
	COLLAPSE_NEVER_LABEL = bundle.getString("COLLAPSE_NEVER_LABEL"); //$NON-NLS-1$
	COLLAPSE_ALWAYS_LABEL = bundle.getString("COLLAPSE_ALWAYS_LABEL"); //$NON-NLS-1$
	COLLAPSE_AS_NEEDED_LABEL = bundle.getString("COLLAPSE_AS_NEEDED_LABEL"); //$NON-NLS-1$
	NO_SELECTION_TITLE = bundle.getString("NO_SELECTION_TITLE"); //$NON-NLS-1$
	NAME_LABEL = bundle.getString("NAME_LABEL"); //$NON-NLS-1$
	DESCRIPTION_LABEL = bundle.getString("DESCRIPTION_LABEL"); //$NON-NLS-1$
	HIDDEN_LABEL = bundle.getString("HIDDEN_LABEL"); //$NON-NLS-1$
	NO_DESCRIPTION_AVAILABLE = bundle.getString("NO_DESCRIPTION_AVAILABLE"); //$NON-NLS-1$
	NO_SELECTION_MADE = bundle.getString("NO_SELECTION_MADE"); //$NON-NLS-1$
	MENU_OPEN_CUSTOMIZE_DIALOG 
					= bundle.getString("MENU_OPEN_CUSTOMIZE_DIALOG"); //$NON-NLS-1$
	MODEL_TYPE_DRAWER = bundle.getString("MODEL_TYPE_DRAWER"); //$NON-NLS-1$
	MODEL_TYPE_SEPARATOR = bundle.getString("MODEL_TYPE_SEPARATOR"); //$NON-NLS-1$
	MODEL_TYPE_STACK = bundle.getString("MODEL_TYPE_STACK"); //$NON-NLS-1$
	NEW_SEPARATOR_DESC = bundle.getString("NEW_SEPARATOR_DESC"); //$NON-NLS-1$
	NEW_DRAWER_LABEL = bundle.getString("NEW_DRAWER_LABEL"); //$NON-NLS-1$
	NEW_SEPARATOR_LABEL = bundle.getString("NEW_SEPARATOR_LABEL"); //$NON-NLS-1$
	NEW_STACK_LABEL = bundle.getString("NEW_STACK_LABEL");  //$NON-NLS-1$
	APPLY_LABEL = bundle.getString("APPLY_LABEL"); //$NON-NLS-1$
	MODEL_TYPE_GROUP = bundle.getString("MODEL_TYPE_GROUP"); //$NON-NLS-1$
	NEW_GROUP_LABEL = bundle.getString("NEW_GROUP_LABEL"); //$NON-NLS-1$
	LAYOUT_MENU_LABEL = bundle.getString("LAYOUT_MENU_LABEL"); //$NON-NLS-1$
	ERROR = bundle.getString("ERROR"); //$NON-NLS-1$
	ABORT_PAGE_FLIPPING_MESSAGE 
					= bundle.getString("ABORT_PAGE_FLIPPING_MESSAGE"); //$NON-NLS-1$
	MENU_OPEN_SETTINGS_DIALOG 
					= bundle.getString("MENU_OPEN_SETTINGS_DIALOG"); //$NON-NLS-1$
	SETTINGS_DIALOG_TITLE = bundle.getString("SETTINGS_DIALOG_TITLE"); //$NON-NLS-1$
	SETTINGS_DETAILS_VIEW_LABEL 
					= bundle.getString("SETTINGS_DETAILS_VIEW_LABEL"); //$NON-NLS-1$
	SETTINGS_FONT_TITLE = bundle.getString("SETTINGS_FONT_TITLE"); //$NON-NLS-1$	
	SETTINGS_FONT_CHANGE = bundle.getString("SETTINGS_FONT_CHANGE"); //$NON-NLS-1$	
	SETTINGS_FONT_CURRENT = bundle.getString("SETTINGS_FONT_CURRENT"); //$NON-NLS-1$	
	SETTINGS_LAYOUT_TITLE = bundle.getString("SETTINGS_LAYOUT_TITLE"); //$NON-NLS-1$	
	SETTINGS_DRAWER_OPTIONS_TITLE 
						= bundle.getString("SETTINGS_DRAWER_OPTIONS_TITLE"); //$NON-NLS-1$
	EXPAND_DRAWER_AT_STARTUP_LABEL 
					= bundle.getString("EXPAND_DRAWER_AT_STARTUP_LABEL"); //$NON-NLS-1$	
	SETTINGS_OPTIONS_COLUMNS = bundle.getString("SETTINGS_OPTIONS_COLUMNS"); //$NON-NLS-1$
	SETTINGS_OPTIONS_LIST = bundle.getString("SETTINGS_OPTIONS_LIST"); //$NON-NLS-1$	
	SETTINGS_OPTIONS_ICONS_ONLY 
						= bundle.getString("SETTINGS_OPTIONS_ICONS_ONLY"); //$NON-NLS-1$
	SETTINGS_OPTIONS_DETAILS = bundle.getString("SETTINGS_OPTIONS_DETAILS"); //$NON-NLS-1$
	SETTINGS_LAYOUT_COLUMNS_OVERRIDE_WIDTH 
			= bundle.getString("SETTINGS_LAYOUT_COLUMNS_OVERRIDE_WIDTH"); //$NON-NLS-1$	
	SETTINGS_LAYOUT_COLUMNS_WIDTH 
						= bundle.getString("SETTINGS_LAYOUT_COLUMNS_WIDTH"); //$NON-NLS-1$
	DRAWER_PIN_AT_STARTUP = bundle.getString("DRAWER_PIN_AT_STARTUP"); //$NON-NLS-1$	
	SETTINGS_DEFAULT_FONT = bundle.getString("SETTINGS_DEFAULT_FONT"); //$NON-NLS-1$	
	SETTINGS_WORKBENCH_FONT_LABEL 
					= bundle.getString("SETTINGS_WORKBENCH_FONT_LABEL"); //$NON-NLS-1$	
	NAME_DESCRIPTION_SEPARATOR 
					= bundle.getString("NAME_DESCRIPTION_SEPARATOR"); //$NON-NLS-1$	
	SETTINGS_ICONS_VIEW_LABEL_CAPS 
					= bundle.getString("SETTINGS_ICONS_VIEW_LABEL_CAPS"); //$NON-NLS-1$	
	SETTINGS_USE_LARGE_ICONS_LABEL_CAPS 
				= bundle.getString("SETTINGS_USE_LARGE_ICONS_LABEL_CAPS"); //$NON-NLS-1$
	PINNED = bundle.getString("PINNED"); //$NON-NLS-1$	
	TOOLTIP_PIN_FIGURE = bundle.getString("TOOLTIP_PIN_FIGURE"); //$NON-NLS-1$
	DOCK_LABEL = bundle.getString("DOCK_LABEL"); //$NON-NLS-1$
	LEFT_LABEL = bundle.getString("LEFT_LABEL"); //$NON-NLS-1$
	RIGHT_LABEL = bundle.getString("RIGHT_LABEL"); //$NON-NLS-1$
	RESIZE_LABEL = bundle.getString("RESIZE_LABEL"); //$NON-NLS-1$
	PALETTE_SHOW = bundle.getString("PALETTE_SHOW"); //$NON-NLS-1$
	PALETTE_HIDE = bundle.getString("PALETTE_HIDE"); //$NON-NLS-1$
	ACC_DESC_PALETTE_BUTTON = bundle.getString("ACC_DESC_PALETTE_BUTTON"); //$NON-NLS-1$
	ACC_DESC_PALETTE_TITLE = bundle.getString("ACC_DESC_PALETTE_TITLE"); //$NON-NLS-1$
}

}