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

import org.eclipse.gef.examples.logicdesigner.model.*;
import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;

import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.SimpleFactory;

import org.eclipse.jface.resource.ImageDescriptor;

public class LogicPlugin
	extends org.eclipse.ui.plugin.AbstractUIPlugin
{

public LogicPlugin(IPluginDescriptor desc){
	super(desc);
}

static private List createCategories(PaletteRoot root){
	List categories = new ArrayList();
	
	categories.add(createControlGroup(root));
	categories.add(createComponentsCategory());
	categories.add(createComplexPartsCategory());

	return categories;
}

static private PaletteContainer createComplexPartsCategory(){
	PaletteDrawer category = new PaletteDrawer(LogicMessages.LogicPlugin_Category_ComplexParts_Label, ImageDescriptor.createFromFile(Circuit.class, "icons/can.gif")); //$NON-NLS-1$

	List entries = new ArrayList();

	CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Description,
		TemplateConstants.TEMPLATE_HALF_ADDER,
		LogicDiagramFactory.getHalfAdderFactory(),
		ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder24.gif") //$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Description,
		TemplateConstants.TEMPLATE_FULL_ADDER,
		LogicDiagramFactory.getFullAdderFactory(),
		ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder24.gif") //$NON-NLS-1$
	);
	entries.add(combined);

	category.addAll(entries);
	return category;
}

static private PaletteContainer createComponentsCategory(){

	PaletteDrawer category = new PaletteDrawer(
		LogicMessages.LogicPlugin_Category_Components_Label,
		ImageDescriptor.createFromFile(Circuit.class, "icons/comp.gif"));//$NON-NLS-1$

	List entries = new ArrayList();
	
	CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Description,
		TemplateConstants.TEMPLATE_LOGIC_LABEL,
		new SimpleFactory(LogicLabel.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/label16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
		TemplateConstants.TEMPLATE_FLOW_CONTAINER,
		new SimpleFactory(LogicFlowContainer.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	entries.add(new PaletteSeparator());

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_LED_Description,
		TemplateConstants.TEMPLATE_LED,
		new SimpleFactory(LED.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Description,
		TemplateConstants.TEMPLATE_CIRCUIT,
		new SimpleFactory(Circuit.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/circuit16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/circuit24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Description,
		TemplateConstants.TEMPLATE_OR_GATE,
		new SimpleFactory(OrGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/or16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/or24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Description,
		TemplateConstants.TEMPLATE_XOR_GATE,
		new SimpleFactory(OrGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
		TemplateConstants.TEMPLATE_AND_GATE,
		new SimpleFactory(OrGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/and16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/and24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
		TemplateConstants.TEMPLATE_LIVE_OUTPUT,
		new SimpleFactory(OrGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/live16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/live24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
		TemplateConstants.TEMPLATE_GROUND,
		new SimpleFactory(OrGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	category.addAll(entries);
	return category;
}

static private PaletteContainer createControlGroup(PaletteRoot root){
	PaletteGroup controlGroup = new PaletteGroup(
		LogicMessages.LogicPlugin_Category_ControlGroup_Label);
	controlGroup.setType(PaletteContainer.PALETTE_TYPE_GROUP);

	List entries = new ArrayList();

	ToolEntry tool = new SelectionToolEntry();
	entries.add(tool);
	root.setDefaultEntry(tool);

	tool = new MarqueeToolEntry();
	entries.add(tool);
	
//	entries.add( new PaletteSeparator("org.eclipse.gef.examples.logicdesigner.logicplugin.sep2") ); //$NON-NLS-1$

	tool = new ConnectionCreationToolEntry(
		LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Label,
		LogicMessages.LogicPlugin_Tool_ConnectionCreationTool_ConnectionCreationTool_Description,
		null,
		ImageDescriptor.createFromFile(Circuit.class, "icons/connection16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/connection24.gif")//$NON-NLS-1$
	);
	entries.add(tool);
	controlGroup.addAll(entries);
	return controlGroup;
}

static PaletteRoot createPalette() {
	PaletteRoot logicPalette = new PaletteRoot();
	logicPalette.addAll(createCategories(logicPalette));
	return logicPalette;
}

}