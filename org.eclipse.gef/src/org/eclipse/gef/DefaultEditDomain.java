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
import org.eclipse.gef.commands.DefaultCommandStack;
import org.eclipse.gef.palette.*;

/**
 * A default implementation of {@link EditDomain}. A {@link
 * org.eclipse.gef.commands.DefaultCommandStack} is used by default. An {@link
 * IEditorPart} is required in the constructor, but it can be <code>null</code>.
 * <P>
 * A {@link org.eclipse.gef.tools.SelectionTool} will be the active Tool until:
 * <UL>
 *   <LI>{@link #setTool(Tool)} is called with some other Tool
 *   <LI>A {@link org.eclipse.gef.palette.PaletteRoot} is provided which contains a
 *   default entry which is a {@link org.eclipse.gef.palette.PaletteToolEntry}. In which
 *   case that entry's tool is made the active Tool.
 * </UL>
 * <P>
 * DefaultEditDomain can be configured with a {@link PaletteViewer}. When provided, the
 * DefaultEditDomain will listen for {@link org.eclipse.gef.palette.PaletteEvent
 * PaletteEvents}, and will switch the active Tool automatically in response.
 */
public class DefaultEditDomain
	implements EditDomain
{

private Tool activeTool;
private List viewers = new ArrayList();
private Tool defaultTool;
private PaletteViewer paletteViewer;
private PaletteRoot paletteRoot;
private CommandStack commandStack = new DefaultCommandStack();
private IEditorPart editorPart;

/**
 * Listens to the PaletteViewer for changes in selection, and sets the Domain's Tool
 * accordingly.
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
 * Constructs a DefaultEditDomain with the specified IEditorPart
 * @param editorPart <code>null</code> or an IEditorPart
 */	
public DefaultEditDomain(IEditorPart editorPart) {
	setEditorPart(editorPart);
	loadDefaultTool();
}

/** * @see org.eclipse.gef.EditDomain#addViewer(EditPartViewer) */
public void addViewer(EditPartViewer viewer) {
	viewer.setEditDomain(this);
	if (!viewers.contains(viewer))
		viewers.add(viewer);
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

/** * @see org.eclipse.gef.EditDomain#getActiveTool() */
public Tool getActiveTool() {
	return activeTool;
}

/** * @see org.eclipse.gef.EditDomain#getCommandStack() */
public CommandStack getCommandStack() {
	return commandStack;
}

/**
 * Gets the default tool for this Editor.
 *
 * @return  The default Tool of this Editor.
 */
public Tool getDefaultTool() {
	if (defaultTool == null)
		defaultTool = new SelectionTool();
	return defaultTool;
}

/** * @return the IEditorPart for this EditDomain */
public IEditorPart getEditorPart() {
	return editorPart;
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
 * Sets the current Tool to the default tool. The default tool is determined by first
 * seeing if the PaletteViewer provides a default Tool.  If not, the EditDomain's default
 * Tool property is used.
 */
public void loadDefaultTool() {
	setTool(null);
	
	if (paletteViewer != null) {
		paletteViewer.setSelection((PaletteEntry)null);
		if (getActiveTool() == null)
			setTool(getDefaultTool());
	} else {
		setTool(getDefaultTool());
	}
}

/** * @see org.eclipse.gef.EditDomain#mouseDoubleClick(MouseEvent, EditPartViewer) */
public void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDoubleClick(mouseEvent, viewer);
}

/** * @see org.eclipse.gef.EditDomain#mouseDown(MouseEvent, EditPartViewer) */
public void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDown(mouseEvent, viewer);
}

/** * @see org.eclipse.gef.EditDomain#mouseDrag(MouseEvent, EditPartViewer) */
public void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseDrag(mouseEvent, viewer);
}

/** * @see org.eclipse.gef.EditDomain#mouseHover(MouseEvent, EditPartViewer) */
public void mouseHover(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseHover(mouseEvent, viewer);
}

/** * @see org.eclipse.gef.EditDomain#mouseMove(MouseEvent, EditPartViewer) */
public void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer) {
	Tool tool = getActiveTool();
	if (tool != null)
		tool.mouseMove(mouseEvent, viewer);
}

/** * @see org.eclipse.gef.EditDomain#mouseUp(MouseEvent, EditPartViewer) */
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

/** * @see org.eclipse.gef.EditDomain#removeViewer(EditPartViewer) */
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
 * Sets the IEditorPart for this EditDomain.
 * @param editorPart the editor */
protected void setEditorPart(IEditorPart editorPart) {
	this.editorPart = editorPart;
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

/** * @see org.eclipse.gef.EditDomain#setTool(Tool) */
public void setTool(Tool newTool) {
	if (activeTool != null)
		activeTool.deactivate();
	activeTool = newTool;
	if (activeTool != null) {
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
