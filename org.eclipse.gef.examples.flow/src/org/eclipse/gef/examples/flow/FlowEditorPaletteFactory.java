package org.eclipse.gef.examples.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.SimpleFactory;

import org.eclipse.gef.examples.flow.model.*;

/**
 * Handles the creation of the palette for the Flow Editor.
 * @author Daniel Lee
 */
public class FlowEditorPaletteFactory {

private static List createCategories(PaletteRoot root) {
	List categories = new ArrayList();
	categories.add(createControlGroup(root));
	categories.add(createComponentsDrawer());
	return categories;
}

private static PaletteContainer createComponentsDrawer() {

	PaletteDrawer drawer = new PaletteDrawer(
		"Components",
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif"));

	List entries = new ArrayList();

	CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
		"Activity",
		"Create a new Activity Node",
		Activity.class,
		new SimpleFactory(Activity.class),
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif"), 
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif")
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		"Sequential Activity",
		"Create a Sequential Activity",
		SequentialActivity.class,
		new SimpleFactory(SequentialActivity.class),
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif"), 
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif")
	);
	entries.add(combined);

	combined = new CombinedTemplateCreationEntry(
		"Parallel Activity",
		"Create a  Parallel Activity",
		ParallelActivity.class,
		new SimpleFactory(ParallelActivity.class),
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif"), 
		ImageDescriptor.createFromFile(Activity.class, "icons/comp.gif")
	);
	entries.add(combined);

	drawer.addAll(entries);
	return drawer;
}

private static PaletteContainer createControlGroup(PaletteRoot root) {
	PaletteGroup controlGroup = new PaletteGroup("Control Group");

	List entries = new ArrayList();

	ToolEntry tool = new SelectionToolEntry();
	entries.add(tool);
	root.setDefaultEntry(tool);

	tool = new MarqueeToolEntry();
	entries.add(tool);

	PaletteSeparator sep = new PaletteSeparator(
			"org.eclipse.gef.examples.flow.flowplugin.sep2");
	sep.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
	entries.add(sep);

	tool = new ConnectionCreationToolEntry(
		"Connection Creation",
		"Creating connections",
		null,
		ImageDescriptor.createFromFile(Activity.class, "icons/connection16.gif"),
		ImageDescriptor.createFromFile(Activity.class, "icons/connection24.gif")
	);
	entries.add(tool);
	controlGroup.addAll(entries);
	return controlGroup;
}

/**
 * Creates the PaletteRoot and adds all Palette elements.
 * @return the root
 */
public static PaletteRoot createPalette() {
	PaletteRoot flowPalette = new PaletteRoot();
	flowPalette.addAll(createCategories(flowPalette));
	return flowPalette;
}

}
