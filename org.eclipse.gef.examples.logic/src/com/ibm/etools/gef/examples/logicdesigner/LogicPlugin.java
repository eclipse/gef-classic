package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.core.runtime.IPluginDescriptor;

import org.eclipse.swt.graphics.Image;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.palette.*;
import com.ibm.etools.gef.tools.*;
import com.ibm.etools.gef.requests.CreateRequest;

import com.ibm.etools.gef.examples.logicdesigner.model.*;

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
		LogicResources.getString("LogicPlugin.Category.ComplexParts.Label"),//$NON-NLS-1$
		new Image(null,Circuit.class.getResourceAsStream("icons/can.gif")));//$NON-NLS-1$
	List groups = new ArrayList();
	DefaultPaletteGroup group = new DefaultPaletteGroup("Complex Parts Group");//$NON-NLS-1$
	groups.add(group);
	category.setChildren(groups);

	List entries = new ArrayList();
	entries.add(
		new DefaultPaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getHalfAdderFactory()),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.HalfAdder.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.HalfAdder.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/halfadder16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/halfadder.gif")))//$NON-NLS-1$
		);
	entries.add(
		new DefaultPaletteToolEntry(
			new CreationTool(LogicDiagramFactory.getFullAdderFactory()),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.FullAdder.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.FullAdder.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/fulladder16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/fulladder.gif")))//$NON-NLS-1$
		);
	group.setChildren(entries);

	return category;
}

static private PaletteContainer createComponentsCategory(){

	DefaultPaletteCategory category = new DefaultPaletteCategory(
		LogicResources.getString("LogicPlugin.Category.Components.Label"),//$NON-NLS-1$
		new Image(null,Circuit.class.getResourceAsStream("icons/comp.gif")));//$NON-NLS-1$
	List groups = new ArrayList();
	DefaultPaletteGroup group = new DefaultPaletteGroup("Components Group");//$NON-NLS-1$
	groups.add(group);
	category.setChildren(groups);

	List entries = new ArrayList();
	
	PaletteToolEntry tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicLabel.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LogicLabel"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LogicLabel"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/label.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/label.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LogicFlowContainer.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.FlowContainer.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.FlowContainer.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/logicflow16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/logicflow.gif"))//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LED.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LED.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LED.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/ledicon16.gif")), //$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/ledicon.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(Circuit.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.Circuit.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.Circuit.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/circuit16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/circuit.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(OrGate.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.ORGate.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.ORGate.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/or16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/or.gif"))//$NON-NLS-1$
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(XORGate.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.XORGate.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.XORGate.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/xor16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/xor.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(AndGate.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.ANDGate.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.ANDGate.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/and16.gif")),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/and.gif"))//$NON-NLS-1$
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(LiveOutput.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LiveOutput.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.LiveOutput.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/live.gif")),//$NON-NLS-1$
			null
		);
	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new CreationTool(new CreateRequest.SimpleFactory(GroundOutput.class)),
			LogicResources.getString("LogicPlugin.Tool.CreationTool.Ground.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.CreationTool.Ground.Description"),//$NON-NLS-1$
			new Image(null,Circuit.class.getResourceAsStream("icons/ground.gif")),//$NON-NLS-1$
			null
		);
	entries.add(tool);
	
	group.setChildren(entries);
	return category;
}

static private PaletteContainer createControlGroup(){
	DefaultPaletteGroup controlGroup = new DefaultPaletteGroup(
		LogicResources.getString("Control Group"));//$NON-NLS-1$
	controlGroup.setType(PaletteContainer.PALETTE_TYPE_GROUP);

	List entries = new ArrayList();

	PaletteToolEntry tool =
		new DefaultPaletteToolEntry(
			new SelectionTool(),
			LogicResources.getString("LogicPlugin.Tool.SelectionTool.SelectionTool.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.SelectionTool.SelectionTool.Description"),//$NON-NLS-1$
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_ARROW_16),
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_ARROW_32)
		);
	((DefaultPaletteEntry)tool).setDefault(true);
//	entries.add(tool);

	tool = new DefaultPaletteToolEntry(
			new MarqueeSelectionTool(),
			LogicResources.getString("LogicPlugin.Tool.MarqueeSelectionTool.MarqueeSelectionTool.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.MarqueeSelectionTool.MarqueeSelectionTool.Description"),//$NON-NLS-1$
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_MARQUEE_16),
			GEFPlugin.getImage(SharedImageConstants.ICON_TOOL_MARQUEE_16)
		);
	entries.add(tool);
	
	tool = new DefaultPaletteToolEntry(
			new ConnectionCreationTool(),
			LogicResources.getString("LogicPlugin.Tool.ConnectionCreationTool.ConnectionCreationTool.Label"),//$NON-NLS-1$
			LogicResources.getString("LogicPlugin.Tool.ConnectionCreationTool.ConnectionCreationTool.Description"),//$NON-NLS-1$
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