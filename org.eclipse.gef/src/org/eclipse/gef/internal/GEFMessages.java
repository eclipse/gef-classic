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
package org.eclipse.gef.internal;

import java.util.MissingResourceException;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;

/**
 * Internal Messages
 * @author hudsonr
 * @since 2.0
 */
public class GEFMessages {

/** */
static class Helper {
	private static IPluginDescriptor desc = Platform.getPluginRegistry()
			.getPluginDescriptor("org.eclipse.gef");//$NON-NLS-1$


	public static String getString(String key) {
		try {
			return desc.getResourceString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
}
public static String AlignBottomAction_Label = Helper.getString("%AlignBottomAction.Label");//$NON-NLS-1$
public static String AlignBottomAction_Tooltip = Helper.getString("%AlignBottomAction.Tooltip");//$NON-NLS-1$
public static String AlignCenterAction_Label = Helper.getString("%AlignCenterAction.Label");//$NON-NLS-1$
public static String AlignCenterAction_Tooltip = Helper.getString("%AlignCenterAction.Tooltip");//$NON-NLS-1$
public static String AlignLeftAction_Label = Helper.getString("%AlignLeftAction.Label");//$NON-NLS-1$
public static String AlignLeftAction_Tooltip = Helper.getString("%AlignLeftAction.Tooltip");//$NON-NLS-1$
public static String AlignMiddleAction_Label = Helper.getString("%AlignMiddleAction.Label");//$NON-NLS-1$
public static String AlignMiddleAction_Tooltip = Helper.getString("%AlignMiddleAction.Tooltip");//$NON-NLS-1$
public static String AlignRightAction_Label = Helper.getString("%AlignRightAction.Label");//$NON-NLS-1$
public static String AlignRightAction_Tooltip = Helper.getString("%AlignRightAction.Tooltip");//$NON-NLS-1$
public static String AlignTopAction_Label = Helper.getString("%AlignTopAction.Label");//$NON-NLS-1$
public static String AlignTopAction_Tooltip = Helper.getString("%AlignTopAction.Tooltip");//$NON-NLS-1$


public static String CopyAction_ActionDeleteCommandName = Helper.getString("%CopyAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
public static String CopyAction_Label = Helper.getString("%CopyAction.Label");//$NON-NLS-1$
public static String CopyAction_Tooltip = Helper.getString("%CopyAction.Tooltip");//$NON-NLS-1$

public static String FitPageAction_Label = Helper.getString("%FitPageAction.Label");//$NON-NLS-1$
public static String FitWidthAction_Label = Helper.getString("%FitWidthAction.Label");//$NON-NLS-1$
public static String FitHeightAction_Label = Helper.getString("%FitHeightAction.Label");//$NON-NLS-1$

public static String DeleteAction_ActionDeleteCommandName = Helper.getString("%DeleteAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
public static String DeleteAction_Label = Helper.getString("%DeleteAction.Label");//$NON-NLS-1$
public static String DeleteAction_Tooltip = Helper.getString("%DeleteAction.Tooltip");//$NON-NLS-1$

public static String ERR_Assert_IsNotNull_Exception_AssertionFailed = Helper.getString("%Assert.IsNotNull.Exception.AssertionFailed_EXC_");//$NON-NLS-1$
public static String ERR_Assert_IsTrue_Exception_AssertionFailed = Helper.getString("%Assert.IsTrue.Exception.AssertionFailed_EXC_");//$NON-NLS-1$

public static String GraphicalEditor_CLOSE_BUTTON_UI = Helper.getString("%GraphicalEditor.CLOSE_BUTTON_UI_");//$NON-NLS-1$
public static String GraphicalEditor_FILE_DELETED_TITLE_UI = Helper.getString("%GraphicalEditor.FILE_DELETED_TITLE_UI_");//$NON-NLS-1$
public static String GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO = Helper.getString("%GraphicalEditor.FILE_DELETED_WITHOUT_SAVE_INFO_");//$NON-NLS-1$
public static String GraphicalEditor_SAVE_BUTTON_UI = Helper.getString("%GraphicalEditor.SAVE_BUTTON_UI_");//$NON-NLS-1$
public static String GraphicalRootEditPart_Overview_Title = Helper.getString("%GraphicalRootEditPart.Overview.Title_UI_");//$NON-NLS-1$

//public static String PasteAction_ActionDeleteCommandName = Helper.getString("%PasteAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
public static String PasteAction_Label = Helper.getString("%PasteAction.Label");//$NON-NLS-1$
public static String PasteAction_Tooltip = Helper.getString("%PasteAction.Tooltip");//$NON-NLS-1$

public static String PrintAction_ActionDeleteCommandName = Helper.getString("%PrintAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$
public static String PrintAction_Label = Helper.getString("%PrintAction.Label");//$NON-NLS-1$
public static String PrintAction_Tooltip = Helper.getString("%PrintAction.Tooltip");//$NON-NLS-1$

public static String RedoAction_Label = Helper.getString("%RedoAction.Label");//$NON-NLS-1$
public static String RedoAction_Tooltip = Helper.getString("%RedoAction.Tooltip");//$NON-NLS-1$
public static String RenameAction_Label = Helper.getString("%RenameAction.Label");//$NON-NLS-1$
public static String RenameAction_Tooltip = Helper.getString("%RenameAction.Tooltip");//$NON-NLS-1$
public static String SaveAction_Label = Helper.getString("%SaveAction.Label");//$NON-NLS-1$
public static String SaveAction_Tooltip = Helper.getString("%SaveAction.Tooltip");//$NON-NLS-1$
public static String SetPropertyValueCommand_Label = Helper.getString("%SetPropertyValueCommand.Label_UI_");//$NON-NLS-1$

public static String SelectionTool_Label = Helper.getString("%SelectionTool.Label");//$NON-NLS-1$
public static String MarqueeTool_Label = Helper.getString("%MarqueeTool.Label");//$NON-NLS-1$
public static String UndoAction_Label = Helper.getString("%UndoAction.Label");//$NON-NLS-1$
public static String UndoAction_Tooltip = Helper.getString("%UndoAction.Tooltip");//$NON-NLS-1$

public static String ZoomIn_Label = Helper.getString("%ZoomIn.Label");//$NON-NLS-1$
public static String ZoomIn_Tooltip = Helper.getString("%ZoomIn.Tooltip");//$NON-NLS-1$

public static String ZoomOut_Label = Helper.getString("%ZoomOut.Label");//$NON-NLS-1$
public static String ZoomOut_Tooltip = Helper.getString("%ZoomOut.Tooltip");//$NON-NLS-1$

}