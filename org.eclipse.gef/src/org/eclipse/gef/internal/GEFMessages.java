package org.eclipse.gef.internal;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public interface GEFMessages {

static class Helper{
	public static String getString(String key){
		org.eclipse.core.runtime.IPluginDescriptor desc = org.eclipse.core.runtime.Platform.getPluginRegistry().getPluginDescriptor("org.eclipse.gef");//$NON-NLS-1$
		try{
			return desc.getResourceString(key);
		}catch(MissingResourceException e){
			return key;
		}
	}
	
	public static String[] getStrings(String[] keys){
		Assert.isNotNull(keys);
		int length = keys.length;
		String[] result = new String[length];
		for(int i=0;i<length;i++)
			result[i] = getString(keys[i]);
		return result;
	}
}

public String AlignLeftAction_ActionToolTipText=Helper.getString("%AlignLeftAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignLeftAction_ActionLabelText=Helper.getString("%AlignLeftAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String AlignRightAction_ActionToolTipText=Helper.getString("%AlignRightAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignRightAction_ActionLabelText=Helper.getString("%AlignRightAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String AlignTopAction_ActionToolTipText=Helper.getString("%AlignTopAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignTopAction_ActionLabelText=Helper.getString("%AlignTopAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String AlignBottomAction_ActionToolTipText=Helper.getString("%AlignBottomAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignBottomAction_ActionLabelText=Helper.getString("%AlignBottomAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String AlignCenterAction_ActionToolTipText=Helper.getString("%AlignCenterAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignCenterAction_ActionLabelText=Helper.getString("%AlignCenterAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String AlignMiddleAction_ActionToolTipText=Helper.getString("%AlignMiddleAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String AlignMiddleAction_ActionLabelText=Helper.getString("%AlignMiddleAction.ActionLabelText_UI_");			//$NON-NLS-1$

public String UndoAction_ActionLabelText=Helper.getString("%UndoAction.ActionLabelText_UI_");			//$NON-NLS-1$
public String UndoAction_ActionShortcutText=Helper.getString("%UndoAction.ActionShortcutText_UI_");		//$NON-NLS-1$
public String UndoAction_ToolTipText=Helper.getString("%UndoAction.ActionToolTipText_UI_");				//$NON-NLS-1$

public String DeleteAction_ActionToolTipText=Helper.getString("%DeleteAction.ActionToolTipText_UI_");	//$NON-NLS-1$
public String DeleteAction_ActionLabelText=Helper.getString("%DeleteAction.ActionLabelText_UI_");		//$NON-NLS-1$
public String DeleteAction_ActionDeleteCommandName=Helper.getString("%DeleteAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$

public String RedoAction_ActionToolTipText=Helper.getString("%RedoAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String RedoAction_ActionLabelText=Helper.getString("%RedoAction.ActionLabelText_UI_");			//$NON-NLS-1$
public String RedoAction_ActionShortcutText=Helper.getString("%RedoAction.ActionShortcutText_UI_");		//$NON-NLS-1$

public String SaveAction_ActionToolTipText=Helper.getString("%SaveAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String SaveAction_ActionLabelText=Helper.getString("%SaveAction.ActionLabelText_UI_");			//$NON-NLS-1$
public String SaveAction_ActionShortcutText=Helper.getString("%SaveAction.ActionShortcutText_UI_");		//$NON-NLS-1$

public String RenameAction_ActionToolTipText=Helper.getString("%RenameAction.ActionToolTipText_UI_");		//$NON-NLS-1$
public String RenameAction_ActionLabelText=Helper.getString("%RenameAction.ActionLabelText_UI_");			//$NON-NLS-1$
public String RenameAction_ActionShortcutText=Helper.getString("%RenameAction.ActionShortcutText_UI_");		//$NON-NLS-1$

public String ERR_Assert_IsNotNull_Exception_AssertionFailed=Helper.getString("%Assert.IsNotNull.Exception.AssertionFailed_EXC_");	//$NON-NLS-1$
public String ERR_Assert_IsTrue_Exception_AssertionFailed=Helper.getString("%Assert.IsTrue.Exception.AssertionFailed_EXC_");	//$NON-NLS-1$

public String GraphicalEditor_FILE_DELETED_TITLE_UI=Helper.getString("%GraphicalEditor.FILE_DELETED_TITLE_UI_"); //$NON-NLS-1$
public String GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO=Helper.getString("%GraphicalEditor.FILE_DELETED_WITHOUT_SAVE_INFO_");//$NON-NLS-1$
public String GraphicalEditor_SAVE_BUTTON_UI=Helper.getString("%GraphicalEditor.SAVE_BUTTON_UI_");	//$NON-NLS-1$
public String GraphicalEditor_CLOSE_BUTTON_UI=Helper.getString("%GraphicalEditor.CLOSE_BUTTON_UI_");	//$NON-NLS-1$

public String GraphicalRootEditPart_Overview_Title=Helper.getString("%GraphicalRootEditPart.Overview.Title_UI_"); //$NON-NLS-1$

public String SetPropertyValueCommand_Label=Helper.getString("%SetPropertyValueCommand.Label_UI_");		//$NON-NLS-1$


}