package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.events.*;


/**
 * The collective state of a GEF "application", defined as its Tools,
 * CommandStack, EditPartViewers, and their contents.
 * An EditDomain is similar to an Eclipse Editor
 * ({@link org.eclipse.ui.IEditorPart IEditorPart}).  However, rather
 * than specializing IEditorPart, EditDomain is a standalone Object. An Editor
 * corresponds to one SWT {@link org.eclipse.swt.widgets.Control Control} and
 * one IEditorInput.<P>
 * An EditDomain can encompass other {@link org.eclipse.ui.IWorkbenchPart WorkbenchPart}s,
 * such as Views, in addition to the Editor.
 * <P>Although rare, a single Editor may have multiple EditDomains, or vice-versa.
 */

public interface EditDomain {

/**
 * Adds an EditPartViewer into the EditDomain. A viewer is
 * a UI component that Tools can work with.  A viewer is most likely
 * contained in a {@link org.eclipse.ui.part.WorkbenchPart WorkbenchPart}
 * of some form, such as the EditorPart or a ViewPart.
 * @param viewer The EditPartViewer
 */
void addViewer(EditPartViewer viewer);

/**
 * Called when a viewer that the editor controls has gained focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The source of the event.
 */
void focusGained(FocusEvent event, EditPartViewer viewer);

/**
 * Called when a viewer that the editor controls has lost focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The source of the event.
 */
void focusLost(FocusEvent event, EditPartViewer viewer);

/**
 * Returns the active Tool
 */
Tool getActiveTool();

/**
 * Returns the CommandStack.
 * Command stacks could potentially be shared across domains if this makes sense.
 * @return The command stack for this edit domain.
 */
CommandStack getCommandStack();

/**
 * Called when a key is <B>pressed</B> within a viewer of this Domain.
 * @param keyEvent The SWT keyboard event 
 * @param viewer The source of the event.
 */
void keyDown(KeyEvent keyEvent, EditPartViewer viewer);

/**
 * Called when a key is <B>released</b> within a viewer of this Domain.
 * @param keyEvent The SWT keyboard event 
 * @param viewer the source of the event.
 */
void keyUp(KeyEvent keyEvent, EditPartViewer view);

/**
 * Unloads the current tool and loads the default tool for this Domain.
 */
void loadDefaultTool();

/**
 * Called when the mouse button has been double-clicked
 * on a viewer from this domain.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Called when the mouse button has been pressed over
 * a viewer that the editor controls.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDown(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Called when the mouse has been dragged within
 * a viewer that the editor controls.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDrag(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Called when the mouse has hovered within
 * a viewer that the editor controls.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseHover(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Called when the mouse has been moved within
 * a viewer that the editor controls.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The viewer that the mouse event is over.
 */
void mouseMove(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Called when the mouse button has been released over
 * a viewer that the editor controls.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseUp(MouseEvent mouseEvent, EditPartViewer view);

/**
 * Adds an already added viewer from the editor. 
 * A viewer that is removed from the editor will
 * no longer be manipulatable by the editor.
 */
void removeViewer(EditPartViewer view);

/**
 * Sets the active Tool for this EditDomain.
 */
public void setTool(Tool tool);

/**
 * Called when the mouse Enters a viewer from this domain.
 */
void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse Exits a viewer from this domain.
 */
void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer);

}
