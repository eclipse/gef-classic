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
package org.eclipse.gef.examples.logicdesigner;

import java.util.*;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Platform;

public interface LogicMessages {

static class Helper {
	public static String getString(String key) {
		IPluginDescriptor desc = Platform.getPluginRegistry().getPluginDescriptor("org.eclipse.gef.examples.logic");  //$NON-NLS-1$
		try {
			return desc.getResourceString(key);
		}
		catch (MissingResourceException e) {
			return key;
		}
	}
}

public String PasteAction_ActionToolTipText=Helper.getString("%PasteAction.ActionToolTipText_UI_");	//$NON-NLS-1$
public String PasteAction_ActionLabelText=Helper.getString("%PasteAction.ActionLabelText_UI_");		//$NON-NLS-1$
public String PasteAction_ActionDeleteCommandName=Helper.getString("%PasteAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$

public String CopyAction_ActionToolTipText=Helper.getString("%CopyAction.ActionToolTipText_UI_");	//$NON-NLS-1$
public String CopyAction_ActionLabelText=Helper.getString("%CopyAction.ActionLabelText_UI_");		//$NON-NLS-1$
public String CopyAction_ActionDeleteCommandName=Helper.getString("%CopyAction.ActionDeleteCommandName_UI_");//$NON-NLS-1$

public String ZoomAction_ZoomIn_ActionToolTipText = Helper.getString("%ZoomAction.ZoomIn.ActionToolTipText_UI_");//$NON-NLS-1$
public String ZoomAction_ZoomIn_ActionLabelText=Helper.getString("%ZoomAction.ZoomIn.ActionLabelText_UI_");			//$NON-NLS-1$

public String ZoomAction_ZoomOut_ActionToolTipText = Helper.getString("%ZoomAction.ZoomOut.ActionToolTipText_UI_");//$NON-NLS-1$
public String ZoomAction_ZoomOut_ActionLabelText=Helper.getString("%ZoomAction.ZoomOut.ActionLabelText_UI_");			//$NON-NLS-1$

public String CreateLogicPage1_Title = Helper.getString("%CreateLogicPage1.Title");  //$NON-NLS-1$
public String CreateLogicPage1_Description = Helper.getString("%CreateLogicPage1.Description");  //$NON-NLS-1$
public String CreateLogicPage1_ModelNames_GroupName = Helper.getString("%CreateLogicPage1.ModelNames.GroupName");  //$NON-NLS-1$
public String CreateLogicPage1_ModelNames_EmptyModelName = Helper.getString("%CreateLogicPage1.ModelNames.EmptyModelName");  //$NON-NLS-1$
public String CreateLogicPage1_ModelNames_FourBitAdderModelName = Helper.getString("%CreateLogicPage1.ModelNames.FourBitAdderModelName");  //$NON-NLS-1$

public String IncrementDecrementAction_Increment_ActionLabelText = Helper.getString("%IncrementDecrementAction.Increment.ActionLabelText");  //$NON-NLS-1$
public String IncrementDecrementAction_Increment_ActionToolTipText = Helper.getString("%IncrementDecrementAction.Increment.ActionToolTipText");  //$NON-NLS-1$
public String IncrementDecrementAction_Decrement_ActionLabelText = Helper.getString("%IncrementDecrementAction.Decrement.ActionLabelText");  //$NON-NLS-1$
public String IncrementDecrementAction_Decrement_ActionToolTipText = Helper.getString("%IncrementDecrementAction.Decrement.ActionToolTipText");  //$NON-NLS-1$

public String AlignmentAction_AlignSubmenu_ActionLabelText = Helper.getString("%AlignmentAction.AlignSubmenu.ActionLabelText");  //$NON-NLS-1$

public String LogicPlugin_Category_ComplexParts_Label = Helper.getString("%LogicPlugin.Category.ComplexParts.Label");  //$NON-NLS-1$
public String LogicPlugin_Category_Components_Label = Helper.getString("%LogicPlugin.Category.Components.Label");  //$NON-NLS-1$
public String LogicPlugin_Category_ControlGroup_Label = Helper.getString("%LogicPlugin.Category.ControlGroup.Label");  //$NON-NLS-1$

public String LogicPlugin_Tool_CreationTool_HalfAdder_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.HalfAdder.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_HalfAdder_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.HalfAdder.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_FullAdder_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.FullAdder.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_FullAdder_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.FullAdder.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_LogicLabel = Helper.getString("%LogicPlugin.Tool.CreationTool.LogicLabel");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Label_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.Label.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Label_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.Label.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_FlowContainer_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.FlowContainer.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_FlowContainer_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.FlowContainer.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_LED_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.LED.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_LED_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.LED.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Circuit_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.Circuit.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Circuit_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.Circuit.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_ORGate_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.ORGate.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_ORGate_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.ORGate.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_XORGate_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.XORGate.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_XORGate_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.XORGate.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_ANDGate_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.ANDGate.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_ANDGate_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.ANDGate.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_LiveOutput_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.LiveOutput.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_LiveOutput_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.LiveOutput.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Ground_Label = Helper.getString("%LogicPlugin.Tool.CreationTool.Ground.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_CreationTool_Ground_Description = Helper.getString("%LogicPlugin.Tool.CreationTool.Ground.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_SelectionTool_SelectionTool_Label = Helper.getString("%LogicPlugin.Tool.SelectionTool.SelectionTool.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_SelectionTool_SelectionTool_Description = Helper.getString("%LogicPlugin.Tool.SelectionTool.SelectionTool.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Label = Helper.getString("%LogicPlugin.Tool.MarqueeSelectionTool.MarqueeSelectionTool.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Description = Helper.getString("%LogicPlugin.Tool.MarqueeSelectionTool.MarqueeSelectionTool.Description");  //$NON-NLS-1$
public String LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Label = Helper.getString("%LogicPlugin.Tool.ConnectionCreationTool.ConnectionCreationTool.Label");  //$NON-NLS-1$
public String LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Description = Helper.getString("%LogicPlugin.Tool.ConnectionCreationTool.ConnectionCreationTool.Description");  //$NON-NLS-1$

public String IncrementDecrementCommand_LabelText = Helper.getString("%IncrementDecrementCommand.LabelText");  //$NON-NLS-1$
public String LogicContainerEditPolicy_OrphanCommandLabelText = Helper.getString("%LogicContainerEditPolicy.OrphanCommandLabelText");  //$NON-NLS-1$
public String LogicElementEditPolicy_OrphanCommandLabelText = Helper.getString("%LogicElementEditPolicy.OrphanCommandLabelText");  //$NON-NLS-1$
public String LogicXYLayoutEditPolicy_AddCommandLabelText = Helper.getString("%LogicXYLayoutEditPolicy.AddCommandLabelText");  //$NON-NLS-1$
public String LogicXYLayoutEditPolicy_CreateCommandLabelText = Helper.getString("%LogicXYLayoutEditPolicy.CreateCommandLabelText");  //$NON-NLS-1$
public String AddCommand_Label = Helper.getString("%AddCommand.Label");  //$NON-NLS-1$
public String AddCommand_Description = Helper.getString("%AddCommand.Description");  //$NON-NLS-1$
public String AndGate_LabelText = Helper.getString("%AndGate.LabelText");  //$NON-NLS-1$
public String Circuit_LabelText = Helper.getString("%Circuit.LabelText");  //$NON-NLS-1$
public String ConnectionCommand_Label = Helper.getString("%ConnectionCommand.Label");  //$NON-NLS-1$
public String ConnectionCommand_Description = Helper.getString("%ConnectionCommand.Description");  //$NON-NLS-1$

public String GraphicalEditor_FILE_DELETED_TITLE_UI=Helper.getString("%GraphicalEditor.FILE_DELETED_TITLE_UI_"); //$NON-NLS-1$
public String GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO=Helper.getString("%GraphicalEditor.FILE_DELETED_WITHOUT_SAVE_INFO_");//$NON-NLS-1$
public String GraphicalEditor_SAVE_BUTTON_UI=Helper.getString("%GraphicalEditor.SAVE_BUTTON_UI_");	//$NON-NLS-1$
public String GraphicalEditor_CLOSE_BUTTON_UI=Helper.getString("%GraphicalEditor.CLOSE_BUTTON_UI_");	//$NON-NLS-1$

public String CreateCommand_Label = Helper.getString("%CreateCommand.Label");  //$NON-NLS-1$
public String CreateCommand_Description = Helper.getString("%CreateCommand.Description");  //$NON-NLS-1$
public String DeleteCommand_Label = Helper.getString("%DeleteCommand.Label");  //$NON-NLS-1$
public String DeleteCommand_Description = Helper.getString("%DeleteCommand.Description");  //$NON-NLS-1$
public String DimensionPropertySource_Property_Width_Label = Helper.getString("%DimensionPropertySource.Property.Width.Label");  //$NON-NLS-1$
public String DimensionPropertySource_Property_Height_Label = Helper.getString("%DimensionPropertySource.Property.Height.Label");  //$NON-NLS-1$
public String GroundOutput_LabelText = Helper.getString("%GroundOutput.LabelText");  //$NON-NLS-1$
public String PropertyDescriptor_LED_Value = Helper.getString("%PropertyDescriptor.LED.Value");  //$NON-NLS-1$
public String LED_LabelText = Helper.getString("%LED.LabelText");  //$NON-NLS-1$
public String LiveOutput_LabelText = Helper.getString("%LiveOutput.LabelText");  //$NON-NLS-1$
public String LocationPropertySource_Property_X_Label = Helper.getString("%LocationPropertySource.Property.X.Label");  //$NON-NLS-1$
public String LocationPropertySource_Property_Y_Label = Helper.getString("%LocationPropertySource.Property.Y.Label");  //$NON-NLS-1$
public String PropertyDescriptor_LogicDiagram_ConnectionRouter = Helper.getString("%PropertyDescriptor.LogicDiagram.ConnectionRouter");  //$NON-NLS-1$
public String PropertyDescriptor_LogicDiagram_Manual = Helper.getString("%PropertyDescriptor.LogicDiagram.Manual");  //$NON-NLS-1$
public String PropertyDescriptor_LogicDiagram_Manhattan = Helper.getString("%PropertyDescriptor.LogicDiagram.Manhattan");  //$NON-NLS-1$
public String LogicDiagram_LabelText = Helper.getString("%LogicDiagram.LabelText");  //$NON-NLS-1$
public String PropertyDescriptor_Label_Text = Helper.getString("%PropertyDescriptor.Label.Text");  //$NON-NLS-1$
public String PropertyDescriptor_LogicSubPart_Size = Helper.getString("%PropertyDescriptor.LogicSubPart.Size");  //$NON-NLS-1$
public String PropertyDescriptor_LogicSubPart_Location = Helper.getString("%PropertyDescriptor.LogicSubPart.Location");  //$NON-NLS-1$
public String OrGate_LabelText = Helper.getString("%OrGate.LabelText");  //$NON-NLS-1$
public String OrphanChildCommand_Label = Helper.getString("%OrphanChildCommand.Label");  //$NON-NLS-1$
public String ReorderPartCommand_Label = Helper.getString("%ReorderPartCommand.Label");  //$NON-NLS-1$
public String ReorderPartCommand_Description = Helper.getString("%ReorderPartCommand.Description");  //$NON-NLS-1$
public String SetLocationCommand_Description = Helper.getString("%SetLocationCommand.Description");  //$NON-NLS-1$
public String SetLocationCommand_Label_Location = Helper.getString("%SetLocationCommand.Label.Location");  //$NON-NLS-1$
public String SetLocationCommand_Label_Resize = Helper.getString("%SetLocationCommand.Label.Resize");  //$NON-NLS-1$
public String CreateGuideCommand_Label = Helper.getString("%CreateGuideCommand_Label"); //$NON-NLS-1$
public String DeleteGuideCommand_Label = Helper.getString("%DeleteGuideCommand_Label"); //$NON-NLS-1$
public String MoveGuideCommand_Label = Helper.getString("%MoveGuideCommand_Label"); //$NON-NLS-1$
public String CloneCommand_Label = Helper.getString("%CloneCommand.Label"); //$NON-NLS-1$

public String ViewMenu_LabelText = Helper.getString("%ViewMenu.LabelText_UI_"); //$NON-NLS-1$

public String PaletteCustomizer_InvalidCharMessage = Helper.getString("%PaletteCustomizer.InvalidCharMessage"); //$NON-NLS-1$

public String XORGate_LabelText = Helper.getString("%XORGate.LabelText");  //$NON-NLS-1$
public String Wire_LabelText = Helper.getString("%Wire.LabelText");  //$NON-NLS-1$
}
