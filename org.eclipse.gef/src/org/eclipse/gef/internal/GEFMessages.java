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
package org.eclipse.gef.internal;

import java.util.ResourceBundle;

import org.eclipse.core.runtime.Platform;

import org.osgi.framework.Bundle;

/**
 * Internal Messages
 * @author hudsonr
 * @since 2.0
 */
public class GEFMessages {

public static final String AlignBottomAction_Label;
public static final String AlignBottomAction_Tooltip;
public static final String AlignCenterAction_Label;
public static final String AlignCenterAction_Tooltip;
public static final String AlignLeftAction_Label;
public static final String AlignLeftAction_Tooltip;
public static final String AlignMiddleAction_Label;
public static final String AlignMiddleAction_Tooltip;
public static final String AlignRightAction_Label;
public static final String AlignRightAction_Tooltip;
public static final String AlignTopAction_Label;
public static final String AlignTopAction_Tooltip;

public static final String CopyAction_ActionDeleteCommandName;
public static final String CopyAction_Label;
public static final String CopyAction_Tooltip;

public static final String DeleteAction_ActionDeleteCommandName;
public static final String DeleteAction_Label;
public static final String DeleteAction_Tooltip;

public static final String GraphicalEditor_CLOSE_BUTTON_UI;
public static final String GraphicalEditor_FILE_DELETED_TITLE_UI;
public static final String GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO;
public static final String GraphicalEditor_SAVE_BUTTON_UI;

public static final String PasteAction_Label;
public static final String PasteAction_Tooltip;

public static final String PrintAction_ActionDeleteCommandName;
public static final String PrintAction_Label;
public static final String PrintAction_Tooltip;

public static final String RedoAction_Label;
public static final String RedoAction_Tooltip;
public static final String RenameAction_Label;
public static final String RenameAction_Tooltip;
public static final String SaveAction_Label;
public static final String SaveAction_Tooltip;
public static final String SetPropertyValueCommand_Label;

public static final String SelectAllAction_Label;
public static final String SelectAllAction_Tooltip;

public static final String MatchWidthAction_Label;
public static final String MatchWidthAction_Tooltip;

public static final String MatchHeightAction_Label;
public static final String MatchHeightAction_Tooltip;

public static final String SelectionTool_Label;
public static final String MarqueeTool_Label;
public static final String UndoAction_Label;
public static final String UndoAction_Tooltip;

public static final String ZoomIn_Label;
public static final String ZoomIn_Tooltip;

public static final String ZoomOut_Label;
public static final String ZoomOut_Tooltip;

// View menu actions
public static final String ToggleRulerVisibility_Label;
public static final String ToggleRulerVisibility_Tooltip;
public static final String ToggleSnapToGeometry_Label;
public static final String ToggleSnapToGeometry_Tooltip;
public static final String ToggleGrid_Label;
public static final String ToggleGrid_Tooltip;

// Palette view Strings
public static final String Palette_Label;
public static final String Palette_Not_Available;

// Rulers and guides
public static final String Ruler_Horizontal_Label;
public static final String Ruler_Vertical_Label;
public static final String Ruler_Desc;
public static final String Guide_Label;
public static final String Guide_Desc;
public static final String Create_Guide_Label;
public static final String Create_Guide_Tooltip;

static {
	Bundle bundle = Platform.getBundle("org.eclipse.gef");//$NON-NLS-1$
	ResourceBundle rb = ResourceBundle.getBundle("org.eclipse.gef.internal.messages"); //$NON-NLS-1$
	
	AlignBottomAction_Label = rb.getString("AlignBottomAction.Label");//$NON-NLS-1$
	AlignBottomAction_Tooltip = rb.getString("AlignBottomAction.Tooltip");//$NON-NLS-1$
	AlignCenterAction_Label = rb.getString("AlignCenterAction.Label");//$NON-NLS-1$
	AlignCenterAction_Tooltip = rb.getString("AlignCenterAction.Tooltip");//$NON-NLS-1$
	AlignLeftAction_Label = rb.getString("AlignLeftAction.Label");//$NON-NLS-1$
	AlignLeftAction_Tooltip = rb.getString("AlignLeftAction.Tooltip");//$NON-NLS-1$
	AlignMiddleAction_Label = rb.getString("AlignMiddleAction.Label");//$NON-NLS-1$
	AlignMiddleAction_Tooltip = rb.getString("AlignMiddleAction.Tooltip");//$NON-NLS-1$
	AlignRightAction_Label = rb.getString("AlignRightAction.Label");//$NON-NLS-1$
	AlignRightAction_Tooltip = rb.getString("AlignRightAction.Tooltip");//$NON-NLS-1$
	AlignTopAction_Label = rb.getString("AlignTopAction.Label");//$NON-NLS-1$
	AlignTopAction_Tooltip = rb.getString("AlignTopAction.Tooltip");//$NON-NLS-1$

	CopyAction_ActionDeleteCommandName = rb.getString("CopyAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
	CopyAction_Label = rb.getString("CopyAction.Label");//$NON-NLS-1$
	CopyAction_Tooltip = rb.getString("CopyAction.Tooltip");//$NON-NLS-1$

	DeleteAction_ActionDeleteCommandName = rb.getString("DeleteAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
	DeleteAction_Label = rb.getString("DeleteAction.Label");//$NON-NLS-1$
	DeleteAction_Tooltip = rb.getString("DeleteAction.Tooltip");//$NON-NLS-1$

	GraphicalEditor_CLOSE_BUTTON_UI = rb.getString("GraphicalEditor.CLOSE_BUTTON_UI_");//$NON-NLS-1$
	GraphicalEditor_FILE_DELETED_TITLE_UI = rb.getString("GraphicalEditor.FILE_DELETED_TITLE_UI_");//$NON-NLS-1$
	GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO = rb.getString("GraphicalEditor.FILE_DELETED_WITHOUT_SAVE_INFO_");//$NON-NLS-1$
	GraphicalEditor_SAVE_BUTTON_UI = rb.getString("GraphicalEditor.SAVE_BUTTON_UI_");//$NON-NLS-1$

	PasteAction_Label = rb.getString("PasteAction.Label");//$NON-NLS-1$
	PasteAction_Tooltip = rb.getString("PasteAction.Tooltip");//$NON-NLS-1$

	PrintAction_ActionDeleteCommandName = rb.getString("PrintAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
	PrintAction_Label = rb.getString("PrintAction.Label");//$NON-NLS-1$
	PrintAction_Tooltip = rb.getString("PrintAction.Tooltip");//$NON-NLS-1$

	RedoAction_Label = rb.getString("RedoAction.Label");//$NON-NLS-1$
	RedoAction_Tooltip = rb.getString("RedoAction.Tooltip");//$NON-NLS-1$
	RenameAction_Label = rb.getString("RenameAction.Label");//$NON-NLS-1$
	RenameAction_Tooltip = rb.getString("RenameAction.Tooltip");//$NON-NLS-1$
	SaveAction_Label = rb.getString("SaveAction.Label");//$NON-NLS-1$
	SaveAction_Tooltip = rb.getString("SaveAction.Tooltip");//$NON-NLS-1$
	SetPropertyValueCommand_Label = rb.getString("SetPropertyValueCommand.Label_UI_");//$NON-NLS-1$

	SelectAllAction_Label = rb.getString("SelectAllAction.Label");//$NON-NLS-1$
	SelectAllAction_Tooltip = rb.getString("SelectAllAction.Tooltip");//$NON-NLS-1$

	MatchWidthAction_Label = rb.getString("MatchWidthAction.Label");//$NON-NLS-1$
	MatchWidthAction_Tooltip = rb.getString("MatchWidthAction.Tooltip");//$NON-NLS-1$

	MatchHeightAction_Label = rb.getString("MatchHeightAction.Label");//$NON-NLS-1$
	MatchHeightAction_Tooltip = rb.getString("MatchHeightAction.Tooltip");//$NON-NLS-1$

	SelectionTool_Label = rb.getString("SelectionTool.Label");//$NON-NLS-1$
	MarqueeTool_Label = rb.getString("MarqueeTool.Label");//$NON-NLS-1$
	UndoAction_Label = rb.getString("UndoAction.Label");//$NON-NLS-1$
	UndoAction_Tooltip = rb.getString("UndoAction.Tooltip");//$NON-NLS-1$

	ZoomIn_Label = Platform.getResourceString(bundle, "ZoomIn.Label");//$NON-NLS-1$
	ZoomIn_Tooltip = Platform.getResourceString(bundle, "ZoomIn.Tooltip");//$NON-NLS-1$

	ZoomOut_Label = Platform.getResourceString(bundle, "ZoomOut.Label");//$NON-NLS-1$
	ZoomOut_Tooltip = Platform.getResourceString(bundle, "ZoomOut.Tooltip");//$NON-NLS-1$

//	 View menu actions
	ToggleRulerVisibility_Label = rb.getString("ToggleRuler.Label"); //$NON-NLS-1$
	ToggleRulerVisibility_Tooltip = rb.getString("ToggleRuler.Tooltip"); //$NON-NLS-1$
	ToggleSnapToGeometry_Label = rb.getString("ToggleSnap.Label"); //$NON-NLS-1$
	ToggleSnapToGeometry_Tooltip = rb.getString("ToggleSnap.Tooltip"); //$NON-NLS-1$
	ToggleGrid_Label = rb.getString("ToggleGrid.Label"); //$NON-NLS-1$
	ToggleGrid_Tooltip = rb.getString("ToggleGrid.Tooltip"); //$NON-NLS-1$

//	 Palette view Strings
	Palette_Label = Platform.getResourceString(bundle, "Palette.Label"); //$NON-NLS-1$
	Palette_Not_Available = rb.getString("Palette_Not_Available"); //$NON-NLS-1$

//	 Rulers and guides
	Ruler_Horizontal_Label = rb.getString("Ruler.Horizontal"); //$NON-NLS-1$
	Ruler_Vertical_Label = rb.getString("Ruler.Vertical"); //$NON-NLS-1$
	Ruler_Desc = rb.getString("Ruler.Desc"); //$NON-NLS-1$
	Guide_Label = rb.getString("Guide.Label"); //$NON-NLS-1$
	Guide_Desc = rb.getString("Guide.Desc"); //$NON-NLS-1$
	Create_Guide_Label = rb.getString("Guide.CreateAction.Label"); //$NON-NLS-1$
	Create_Guide_Tooltip = rb.getString("Guide.CreateAction.Tooltip"); //$NON-NLS-1$
}

}