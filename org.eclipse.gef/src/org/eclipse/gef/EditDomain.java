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
package org.eclipse.gef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.palette.PaletteViewer;


/**
 * The collective state of a GEF "application", loosely defined by a CommandStack,
 * one or more EditPartViewers, and the active Tool. An EditDomain is usually tied with an
 * Eclipse {@link org.eclipse.ui.IEditorPart IEditorPart}).  However, the distinction
 * between EditorPart and EditDomain was made to allow for much flexible use of the
 * Graphical Editing Framework.
 */
public class EditDomain {

private Tool defaultTool;
private PaletteViewer paletteViewer;
private PaletteRoot paletteRoot;
private Tool activeTool;
private List viewers = new ArrayList();
private CommandStack commandStack = new CommandStack();

/**
 * Listens to the PaletteViewer for changes in selection, and sets the Domain's Tool
 * accordingly.
 */
private PaletteListener paletteListener = new PaletteListener() {
	public void activeToolChanged(PaletteViewer viewer, ToolEntry tool) {
		handlePaletteToolChanged();
	}
};

public EditDomain() {
	loadDefaultTool();
}

/**
 * Adds an EditPartViewer into the EditDomain.  A viewer is most likely placed in a {@link
 * org.eclipse.ui.IWorkbenchPart WorkbenchPart} of some form, such as the IEditorPart or
 * an IViewPart.
 * @param viewer The EditPartViewer
 */
public void addViewer(EditPartViewer viewer) {
	viewer.setEditDomain(this);
	if (!viewers.contains(viewer))
		viewers.add(viewer);
}

/**
 * Called when one of the EditDomain's Viewers receives keyboard focus.
 * @param event The SWT focus event 
 * @param viewer the Viewer that received the event.
 */
public void focusGained(FocusEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.focusGained(event, viewer);
}

/**
 * Called when one of the EditDomain's Viewers is losing keyboard focus.
 * @param event The SWT focus event 
 * @param viewer the Viewer that received the event.
 */
public void focusLost(FocusEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.focusLost(event, viewer);
}

/**
 * Returns the active Tool
 * @return the active Tool
 */
public Tool getActiveTool() {
	return activeTool;
}

/**
 * Returns the CommandStack.
 * Command stacks could potentially be shared across domains depending on the application.
 * @return The command stack
 */
public CommandStack getCommandStack() {
	return commandStack;
}

/**
 * Returns the default tool for this edit domain. This will be a {@link
 * org.eclipse.gef.tools.SelectionTool} unless specifically replaced using {@link
 * #setDefaultTool(Tool)}.
 *
 * @return The default Tool for this domain
 */
public Tool getDefaultTool() {
	if (defaultTool == null)
		defaultTool = new SelectionTool();
	return defaultTool;
}

private void handlePaletteToolChanged() {
	ToolEntry entry = paletteViewer.getActiveTool();
	if (entry != null)
		setActiveTool(entry.createTool());
	else
		setActiveTool(getDefaultTool());
}

/**
 * Called when a key is <B>pressed</B> on a Viewer.
 * @param keyEvent The SWT keyboard event 
 * @param viewer The source of the event.
 */
public void keyDown(KeyEvent keyEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.keyDown(keyEvent, viewer);
}

/**
 * Called when a key is <B>released</b> on a Viewer.
 * @param keyEvent The SWT keyboard event 
 * @param viewer the source of the event.
 */
public void keyUp(KeyEvent keyEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.keyUp(keyEvent, viewer);
}

/**
 * Loads the default Tool. If a palette has been provided and that palette has a default,
 * then that tool is loaded. If not, the EditDomain's default tool is loaded. By default,
 * this is the {@link org.eclipse.gef.tools.SelectionTool}.
 */
public void loadDefaultTool() {
	setActiveTool(null);
	if (paletteRoot != null) {
		if (paletteRoot.getDefaultEntry() != null) {
			paletteViewer.setActiveTool(paletteRoot.getDefaultEntry());
			return;
		} else
			paletteViewer.setActiveTool(null);
	}
	setActiveTool(getDefaultTool());
}

/**
 * Called when the mouse button has been double-clicked on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
public void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDoubleClick(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been pressed on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
public void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDown(mouseEvent, viewer);
}

/**
 * Called when the mouse has been dragged within a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
public void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDrag(mouseEvent, viewer);
}

/**
 * Called when the mouse has hovered on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
public void mouseHover(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseHover(mouseEvent, viewer);
}

/**
 * Called when the mouse has been moved on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The viewer that the mouse event is over.
 */
public void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseMove(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been released on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
public void mouseUp(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.mouseUp(mouseEvent, viewer);	
}

/**
 * Called when a native drag has finished on a Viewer.
 * @param event The DragSourceEvent
 * @param viewer The viewer where the drag finished
 */
public void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.nativeDragFinished(event, viewer);
}

/**
 * Called when a native drag has started on a Viewer.
 * @param event The DragSourceEvent
 * @param viewer The viewer where the drag started
 */
public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.nativeDragStarted(event, viewer);
}

/**
 * Removes a previously added viewer from the EditDomain. A Viewer that is removed from
 * the EditDomain will no longer forward input to the domain and its active Tool.
 * @param viewer the Viewer being removed
 */
public void removeViewer(EditPartViewer viewer) {
	if (viewers.remove(viewer))
		viewer.setEditDomain(null);
}

/**
 * Sets the <code>CommandStack</code>.
 * @param stack the CommandStack
 */
public void setCommandStack(CommandStack stack) {
	commandStack = stack;
}

/**
 * Sets the default Tool, which is used if the Palette does not provide a default
 * @param tool <code>null</code> or a Tool
 */
public void setDefaultTool(Tool tool) {
	defaultTool = tool;
}

/**
 * Sets the PalatteRoot for this EditDomain. If the EditDomain already knows about a
 * PaletteViewer, this root will be set into the palette viewer also. Loads the default
 * Tool after the root has been set.
 * @param root the palette's root
 */
public void setPaletteRoot(PaletteRoot root) {
	paletteRoot = root;
	if (paletteViewer != null)
		paletteViewer.setPaletteRoot(paletteRoot);
	loadDefaultTool();
}

/**
 * Sets the <code>PaletteViewer</code> for this EditDomain
 * @param palette the PaletteViewer
 */
public void setPaletteViewer(PaletteViewer palette) {
	palette.addPaletteListener(paletteListener);
	paletteViewer = palette;
	if (paletteRoot != null) {
		paletteViewer.setPaletteRoot(paletteRoot);
		loadDefaultTool();
	}
}

/**
 * Sets the active Tool for this EditDomain. If a current Tool is active, it is
 * deactivated. The new Tool is told its EditDomain, and is activated.
 * @param tool the Tool
 */
public void setActiveTool(Tool tool) {
	if (activeTool != null)
		activeTool.deactivate();
	activeTool = tool;
	if (activeTool != null) {
		activeTool.setEditDomain(this);
		activeTool.activate();
	}
}

/**
 * Called when the mouse enters a Viewer.
 * @param mouseEvent the SWT mouse event
 * @param viewer the Viewer being entered
 */
public void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.viewerEntered(mouseEvent, viewer);
}

/**
 * Called when the mouse exits a Viewer.
 * @param mouseEvent the SWT mouse event
 * @param viewer the Viewer being exited
 */
public void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.viewerExited(mouseEvent, viewer);
}

}
