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
package org.eclipse.gef.examples.logicdesigner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;

import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagramFactory;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

public class LogicPlugin
	extends org.eclipse.ui.plugin.AbstractUIPlugin
{

private static LogicPlugin singleton;

public LogicPlugin(IPluginDescriptor desc){
	super(desc);
	if( singleton == null ){
		singleton = this;
	}
}

public static LogicPlugin getDefault(){
	return singleton;
}

static private List createCategories(PaletteRoot root){
	List categories = new ArrayList();
	
	categories.add(createControlGroup(root));
	categories.add(createComponentsDrawer());
	categories.add(createComplexPartsDrawer());
//	categories.add(createTemplateComponentsDrawer());
//	categories.add(createComplexTemplatePartsDrawer());

	return categories;
}

static private PaletteContainer createComplexPartsDrawer(){
	PaletteDrawer drawer = new PaletteDrawer(LogicMessages.LogicPlugin_Category_ComplexParts_Label, ImageDescriptor.createFromFile(Circuit.class, "icons/can.gif")); //$NON-NLS-1$

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

	drawer.addAll(entries);
	return drawer;
}

static private PaletteContainer createComplexTemplatePartsDrawer(){
	PaletteDrawer drawer = new PaletteDrawer("Canned Templates", ImageDescriptor.createFromFile(Circuit.class, "icons/can.gif")); //$NON-NLS-2$
	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

	List entries = new ArrayList();

	PaletteTemplateEntry combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_HalfAdder_Description,
		TemplateConstants.TEMPLATE_HALF_ADDER,
		ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/halfadder24.gif") //$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_FullAdder_Description,
		TemplateConstants.TEMPLATE_FULL_ADDER,
		ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/fulladder24.gif") //$NON-NLS-1$
	);
	entries.add(combined);

	drawer.addAll(entries);
	return drawer;
}

static private PaletteContainer createComponentsDrawer(){

	PaletteDrawer drawer = new PaletteDrawer(
		LogicMessages.LogicPlugin_Category_Components_Label,
		ImageDescriptor.createFromFile(Circuit.class, "icons/comp.gif"));//$NON-NLS-1$

	List entries = new ArrayList();
	
	CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
		TemplateConstants.TEMPLATE_FLOW_CONTAINER,
		new SimpleFactory(LogicFlowContainer.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow24.gif")//$NON-NLS-1$
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
	
	entries.add(new PaletteSeparator());

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Description,
		TemplateConstants.TEMPLATE_LOGIC_LABEL,
		new SimpleFactory(LogicLabel.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/label16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

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
		new SimpleFactory(XORGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
		TemplateConstants.TEMPLATE_AND_GATE,
		new SimpleFactory(AndGate.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/and16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/and24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	PaletteStack liveGroundStack = new PaletteStack(
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveGroundStack_Label,
			LogicMessages.LogicPlugin_Tool_CreationTool_LiveGroundStack_Description, null);
	
	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
		TemplateConstants.TEMPLATE_LIVE_OUTPUT,
		new SimpleFactory(LiveOutput.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/live16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/live24.gif")//$NON-NLS-1$
	);
	liveGroundStack.add(combined);

	combined = new CombinedTemplateCreationEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
		TemplateConstants.TEMPLATE_GROUND,
		new SimpleFactory(GroundOutput.class),
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground24.gif")//$NON-NLS-1$
	);
	liveGroundStack.add(combined);

	entries.add(liveGroundStack);
	
	drawer.addAll(entries);
	return drawer;
}

static private PaletteContainer createTemplateComponentsDrawer(){

	PaletteDrawer drawer = new PaletteDrawer("Template Components",
		ImageDescriptor.createFromFile(Circuit.class, "icons/comp.gif"));//$NON-NLS-1$
	drawer.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

	List entries = new ArrayList();
	
	PaletteTemplateEntry combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_FlowContainer_Description,
		TemplateConstants.TEMPLATE_FLOW_CONTAINER,
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/logicflow24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Circuit_Description,
		TemplateConstants.TEMPLATE_CIRCUIT,
		ImageDescriptor.createFromFile(Circuit.class, "icons/circuit16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/circuit24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	entries.add(new PaletteSeparator());

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Label_Description,
		TemplateConstants.TEMPLATE_LOGIC_LABEL,
		ImageDescriptor.createFromFile(Circuit.class, "icons/label16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/label24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_LED_Description,
		TemplateConstants.TEMPLATE_LED,
		ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon16.gif"), //$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/ledicon24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_ORGate_Description,
		TemplateConstants.TEMPLATE_OR_GATE,
		ImageDescriptor.createFromFile(Circuit.class, "icons/or16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/or24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_XORGate_Description,
		TemplateConstants.TEMPLATE_XOR_GATE,
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/xor24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_ANDGate_Description,
		TemplateConstants.TEMPLATE_AND_GATE,
		ImageDescriptor.createFromFile(Circuit.class, "icons/and16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/and24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_LiveOutput_Description,
		TemplateConstants.TEMPLATE_LIVE_OUTPUT,
		ImageDescriptor.createFromFile(Circuit.class, "icons/live16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/live24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	combined = new PaletteTemplateEntry(
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Label,
		LogicMessages.LogicPlugin_Tool_CreationTool_Ground_Description,
		TemplateConstants.TEMPLATE_GROUND,
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground16.gif"),//$NON-NLS-1$
		ImageDescriptor.createFromFile(Circuit.class, "icons/ground24.gif")//$NON-NLS-1$
	);
	entries.add(combined);

	drawer.addAll(entries);
	return drawer;
}

static private PaletteContainer createControlGroup(PaletteRoot root){
	PaletteGroup controlGroup = new PaletteGroup(
		LogicMessages.LogicPlugin_Category_ControlGroup_Label);

	List entries = new ArrayList();

	ToolEntry tool = new PanningSelectionToolEntry();
	entries.add(tool);
	root.setDefaultEntry(tool);

	tool = new MarqueeToolEntry();
	entries.add(tool);
	
	PaletteSeparator sep = new PaletteSeparator(
			"org.eclipse.gef.examples.logicdesigner.logicplugin.sep2"); //$NON-NLS-1$
	sep.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
	entries.add(sep); //$NON-NLS-1$

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