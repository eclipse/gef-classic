package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.core.runtime.IPluginDescriptor;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.*;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.tools.*;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.logicdesigner.model.*;

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
	DefaultPaletteCategory category = new DefaultPaletteCategory(
		LogicMessages.LogicPlugin_Category_ComplexParts_Label,
		new Image(null,Circuit.class.getResourceAsStream("icons/can.gif")));//$NON-NLS-1$
	List groups = new ArrayList();
	DefaultPaletteGroup group = new DefaultPaletteGroup(
		LogicMessages.LogicPlugin_Category_ComplexParts_Label);
	groups.add(group);
	category.setChildren(groups);

	List entries = new ArrayList();
	entries.add(
		new DefaultPaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getHalfAdderFactory()),
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/halfadder16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/halfadder.gif")))//$NON-NLS-1$
		);
	entries.add(
		new DefaultPaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getFullAdderFactory()),
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/fulladder16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/fulladder.gif")))//$NON-NLS-1$
		);
	group.setChildren(entries);

	return category;
}

static private PaletteContainer createComponentsCategory(){

	DefaultPaletteCategory category = new DefaultPaletteCategory(
		LogicMessages.LogicPlugin_Category_Components_Label,
		new Image(null,Circuit.class.getResourceAsStream("icons/comp.gif")));//$NON-NLS-1$
	List groups = new ArrayList();
	DefaultPaletteGroup group = new DefaultPaletteGroup("Components Group");//$NON-NLS-1$
	groups.add(group);
	category.setChildren(groups);

	List entries = new ArrayList();
	
	PaletteToolEntry tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicLabel.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel,
			LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel,
			new Image(null,Circuit.class.getResourceAsStream("icons/label.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/label.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicFlowContainer.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/logicflow16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/logicflow.gif"))//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LED.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LED_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/ledicon16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/ledicon.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(Circuit.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/circuit16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/circuit.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(OrGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/or16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/or.gif"))//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(XORGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/xor16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/xor.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(AndGate.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/and16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/and.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LiveOutput.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/live.gif")),//$NON-NLS-1$
			null
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(GroundOutput.class)),
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/ground.gif")),//$NON-NLS-1$
			null
		);
	entries.add(tool);
	
	group.setChildren(entries);
	return category;
}

static private PaletteContainer createControlGroup(){
	DefaultPaletteGroup controlGroup = new DefaultPaletteGroup(
		LogicMessages.LogicPlugin_Category_ControlGroup_Label);
	controlGroup.setType(PaletteContainer.PALETTE_TYPE_GROUP);

	List entries = new ArrayList();

	PaletteToolEntry tool =
		new DefaultPaletteToolEntry(
			new SelectionTool(),
			LogicMessages.LogicPlugin_Tool_SelectionTool_SelectionTool_Label,
			LogicMessages.LogicPlugin_Tool_SelectionTool_SelectionTool_Description,
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_ARROW_16),
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_ARROW_32)
		);
	((DefaultPaletteEntry)tool).setDefault(true);
//	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new MarqueeSelectionTool(),
			LogicMessages.LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Label,
			LogicMessages.LogicPlugin_Tool_MarqueeSelectionTool_MarqueeSelectionTool_Description,
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_MARQUEE_16),
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_MARQUEE_16)
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new ConnectionCreationTool(),
			LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Label,
			LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Description,
			new Image(null,Circuit.class.getResourceAsStream("icons/connection16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/connection32.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	controlGroup.setChildren(entries);
	return controlGroup;
}

static PaletteRoot createPalette() {
	DefaultPaletteRoot logicPalette = new DefaultPaletteRoot(createCategories());
	return logicPalette;
}

}