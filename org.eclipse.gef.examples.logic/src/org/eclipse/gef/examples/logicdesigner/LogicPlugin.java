package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.gef.SharedImages;

import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;

public class LogicPlugin
	extends org.eclipse.ui.plugin.AbstractUIPlugin
{

public LogicPlugin(IPluginDescriptor desc){
	super(desc);
}

static private List createCategories(){
	List categories = new ArrayList();
	
	categories.add(createControlGroup());
	categories.add(createComponentsCategory());
	categories.add(createComplexPartsCategory());

	return categories;
}

static private PaletteContainer createComplexPartsCategory(){
	PaletteCategory category = new PaletteCategory(
		LogicMessages.LogicPlugin_Category_ComplexParts_Label,
		ImageDescriptor.createFromFile(Circuit.class, "icons/can.gif"));//$NON-NLS-1$

	List entries = new ArrayList();
	
	PaletteTemplateEntry template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_HALF_ADDER,
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_FULL_ADDER,
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder24.gif")//$NON-NLS-1$
		);
	entries.add(template);

/*
	entries.add(
		new PaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getHalfAdderFactory()),
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder16.gif")), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder24.gif")))//$NON-NLS-1$
		);
	entries.add(
		new PaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getFullAdderFactory()),
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder24.gif"))//$NON-NLS-1$
		);
*/
	category.addAll(entries);

	return category;
}

static private PaletteContainer createComponentsCategory(){

	PaletteCategory category = new PaletteCategory(
		LogicMessages.LogicPlugin_Category_Components_Label,
		ImageDescriptor.createFromFile(Circuit.class, "icons/comp.gif"));//$NON-NLS-1$

	List entries = new ArrayList();
	
	PaletteTemplateEntry template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_LOGIC_LABEL,
			LogicMessages.LogicPlugin_Tool_CreationTool_Label_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Label_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/label16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_FLOW_CONTAINER,
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow24.gif")//$NON-NLS-1$
		);
	entries.add(template);

//	entries.add( new PaletteSeparator(PaletteSeparator.NOT_A_MARKER) );
	
	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_LED,
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_CIRCUIT,
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/circuit16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/circuit24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_OR_GATE,
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/or16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/or24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_XOR_GATE,
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/xor16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/xor24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_AND_GATE,
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/and16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/and24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_LIVE_OUTPUT,
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/live16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/live24.gif")//$NON-NLS-1$
		);
	entries.add(template);

	template = new PaletteTemplateEntry(
			TemplateConstants.TEMPLATE_GROUND,
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/ground16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/ground24.gif")//$NON-NLS-1$
		);
	entries.add(template);

/*
	PaletteToolEntry tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicLabel.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel,
			LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel,
			ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicFlowContainer.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow24.gif")//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LED.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon16.gif"), //$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(Circuit.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/circuit16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/circuit24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(OrGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/or16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/or24.gif")//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(XORGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/xor16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/xor24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(AndGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/and16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/and24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LiveOutput.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/live24.gif"),//$NON-NLS-1$
			null
		);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(GroundOutput.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
			ImageDescriptor.createFromFile(Circuit.class, "icons/ground24.gif"),//$NON-NLS-1$
			null
		);
	entries.add(tool);
*/
	category.addAll(entries);
	return category;
}

static private PaletteContainer createControlGroup(){
	PaletteGroup controlGroup = new PaletteGroup(
		LogicMessages.LogicPlugin_Category_ControlGroup_Label);
	controlGroup.setType(PaletteContainer.PALETTE_TYPE_GROUP);

	List entries = new ArrayList();

	PaletteToolEntry tool =
		new PaletteToolEntry(
			new SelectionTool(),
			LogicMessages.LogicPlugin_Tool_SelectionTool_SelectionTool_Label,
			LogicMessages.LogicPlugin_Tool_SelectionTool_SelectionTool_Description,
			SharedImages.DESC_SELECTION_TOOL_16,
			SharedImages.DESC_SELECTION_TOOL_24
		);
	((PaletteEntry)tool).setDefault(true);
	entries.add(tool);

	tool = new PaletteToolEntry(
			new MarqueeSelectionTool(),
			LogicMessages.LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Label,
			LogicMessages.LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Description,
			SharedImages.DESC_MARQUEE_TOOL_16,
			SharedImages.DESC_MARQUEE_TOOL_24
		);
	entries.add(tool);
	
//	entries.add( new PaletteSeparator("org.eclipse.gef.examples.logicdesigner.logicplugin.sep2") ); //$NON-NLS-1$

	tool = new PaletteToolEntry(
			new ConnectionCreationTool(),
			LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Label,
			LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Description,
			//@RESOURCE_LEAK The Image below is never disposed
			ImageDescriptor.createFromFile(Circuit.class, "icons/connection16.gif"),//$NON-NLS-1$
			ImageDescriptor.createFromFile(Circuit.class, "icons/connection24.gif")//$NON-NLS-1$
		);
	entries.add(tool);

	controlGroup.addAll(entries);
	return controlGroup;
}

static PaletteRoot createPalette() {
	PaletteRoot logicPalette = new PaletteRoot(createCategories());
	return logicPalette;
}

}