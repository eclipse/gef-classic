package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.*;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.tools.*;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.*;

/**
 * A default implementation of the EditDomain interface.
 * This implementation will create a CommandStack by default.
 * A SelectionTool will be used if no Palette or Tool is set.
 * Users of this class must provide one or more EditPartViewers.
 */
public class DefaultEditDomain
	implements EditDomain
{

private Tool activeTool;
private List viewers = new ArrayList();
private Tool defaultTool;
private PaletteViewer paletteViewer;
private PaletteRoot paletteRoot;
private CommandStack commandStack = createCommandStack();
private IEditorPart editorPart;

/**
 * Listens to the palette for changes in selections, and sets
 * the Editor's tool accordingly.
 */
private PaletteListener paletteListener = new PaletteListener() {
	public void entrySelected(PaletteEvent event) {
		Object paletteEntry = event.getEntry();
		if (paletteEntry instanceof PaletteToolEntry)
			setTool(((PaletteToolEntry) paletteEntry).getTool());
		else if (paletteEntry == null)
			setTool(null);
	};
	public void newDefaultEntry(PaletteEvent event) {
		Object paletteEntry = event.getEntry();
		if (paletteEntry instanceof PaletteToolEntry)
			setDefaultTool(((PaletteToolEntry) paletteEntry).getTool());
		else
			setDefaultTool(null);
	};
};

/**
 * Creates a DefaultEditDomain for the given IEditorPart
 */	
public DefaultEditDomain(IEditorPart editorPart) {
	setEditorPart(editorPart);
	init();
}

/**
 * Adds a new viewer to the editor.
 * @param viewer  The viewer to add
 */
public void addViewer(EditPartViewer viewer) {
	viewer.setEditDomain(this);
	if (!viewers.contains(viewer))
		viewers.add(viewer);
}

/**
 * Creates the default command Stack
 */
protected CommandStack createCommandStack(){
	return new org.eclipse.gef.commands.DefaultCommandStack();
}

/**
 * @see EditDomain#focusGained(FocusEvent, EditPartViewer)
 */
public void focusGained(FocusEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.focusGained(event, viewer);
}

/**
 * @see EditDomain#focusLost(FocusEvent, EditPartViewer)
 */
public void focusLost(FocusEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.focusLost(event, viewer);
}

/**
 * Return the current tool for the editor.  The current
 * tool is the tool that will be routed all the input
 * events for the viewers that the editor manages.
 *
 * @return The current tool
 */
public Tool getActiveTool() {
	return activeTool;
}

/**
 * Returns the command stack of this Editor.
 *
 * @return  Command stack for this Editor.
 */
public CommandStack getCommandStack() {
	return commandStack;
}

/**
 * Gets the default tool for this Editor.
 *
 * @return  The default Tool of this Editor.
 */
public Tool getDefaultTool(){
	if (defaultTool == null)
		defaultTool = new SelectionTool();
	return defaultTool;
}

public IEditorPart getEditorPart(){
	return editorPart;
}

protected void init(){
	loadDefaultTool();
}

/**
 * Called when a key is pressed within a viewer that
 * the editor controls. Passes on the event to the 
 * current active tool.
 *
 * @param keyEvent  Event representing the key press.
 * @param viewer  Viewer where the key was pressed.
 */
public void keyDown(KeyEvent keyEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.keyDown(keyEvent, viewer);
}

/**
 * Called when a key is pressed within a viewer that
 * the editor controls. Passes on the event to the 
 * current active tool.
 *
 * @param keyEvent  Event representing the key press.
 * @param viewer  Viewer where the key was pressed.
 */
public void keyUp(KeyEvent keyEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.keyUp(keyEvent, viewer);
}

/**
 * Sets the current tool to the default tool.
 */
public void loadDefaultTool(){
	setTool(null);
	
	if(paletteViewer != null){
		paletteViewer.setSelection((PaletteEntry)null);
		if (getActiveTool() == null)
			setTool(getDefaultTool());
	} else {
		setTool(getDefaultTool());
	}
}

/**
 * Called when the mouse button has been double clicked over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse double-click.
 * @param viewer  Viewer where the mouse was double-clicked.
 */
public void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDoubleClick(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been pressed over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse press down.
 * @param viewer  Viewer where the mouse was pressed.
 */
public void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDown(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been dragged over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse drag.
 * @param viewer  Viewer where the mouse was dragged.
 */
public void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDrag(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been dragged over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse drag.
 * @param viewer  Viewer where the mouse was dragged.
 */
public void mouseHover(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseHover(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been moved over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse move.
 * @param viewer  Viewer where the mouse was moved.
 */
public void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseMove(mouseEvent, viewer);
}

/**
 * Called when the mouse button has been released over
 * a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse press release.
 * @param viewer  Viewer where the mouse was released.
 */
public void mouseUp(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)	
		tool.mouseUp(mouseEvent, viewer);	
}

/**
 * @see org.eclipse.gef.EditDomain#nativeDragStarted(DragSourceEvent, EditPartViewer)
 */
public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.nativeDragStarted(event, viewer);
}

/**
 * Removes an already added viewer from the editor. 
 * A viewer that is removed from the editor will
 * no longer be manipulatable by the editor.
 *
 * @param viewer  The viewer to remove
 */
public void removeViewer(EditPartViewer viewer) {
	if (viewers.remove(viewer))
		viewer.setEditDomain(null);
}

/**
 * Sets the command stack of this Editor with the 
 * input stack given.
 *
 * @param stack  The new command stack for this Editor.
 */
public void setCommandStack(CommandStack stack) {
	commandStack = stack;
}

/**
 * Sets the default tool, which is used if the PaletteRoot has no default entry.
 *
 * @param tool  Default tool for this Editor.
 */
public void setDefaultTool(Tool tool) {
	defaultTool = tool;
}

protected void setEditorPart(IEditorPart editorPart){
	this.editorPart = editorPart;
}

/**
 * Called to set the palette model for this Editor.
 * It loads the default tool from the given model, and
 * sets the model to a palette viewer if present.
 *
 * @param root  Model of the new palette.
 */
public void setPaletteRoot(PaletteRoot root){
	paletteRoot = root;
	if (paletteViewer != null)
		paletteViewer.setPaletteRoot(paletteRoot);
	loadDefaultTool();
}

/**
 * Sets the palette viewer to be used by the editor.
 *
 * @param palette  Viewer of the palette for this Editor.
 */
public void setPaletteViewer(PaletteViewer palette) {
	palette.addPaletteListener(paletteListener);
	paletteViewer = palette;
	if (paletteRoot != null){
		paletteViewer.setPaletteRoot(paletteRoot);
		loadDefaultTool();
	}
}

/**
 * Set the current tool for the editor.  The current
 * tool is the tool that will be routed all the input
 * events for the viewers that the editor manages.
 *
 * @param newTool  The new tool for this Editor.
 */
public void setTool(Tool newTool) {
	if (activeTool != null)
		activeTool.deactivate();
	activeTool = newTool;
	if (activeTool != null){
		activeTool.setEditDomain(this);
		activeTool.activate();
	}
}

/**
 * Called when the mouse has entered a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse press down.
 * @param viewer  Viewer that the mouse entered.
 */
public void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.viewerEntered(mouseEvent, viewer);
}

/**
 * Called when the mouse has exited a viewer that the editor controls.
 *
 * @param mouseEvent  Event representing the mouse press down.
 * @param viewer  Viewer that the mouse exited.
 */
public void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.viewerExited(mouseEvent, viewer);
}

}
