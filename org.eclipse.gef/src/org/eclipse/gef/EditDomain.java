package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.*;


/**
 * The collective state of a GEF "application", loosely defined by a CommandStack,
 * one or more EditPartViewers, and the active Tool. An EditDomain is usually tied with an
 * Eclipse {@link org.eclipse.ui.IEditorPart IEditorPart}).  However, the distinction
 * between EditorPart and EditDomain was made to allow for much flexible use of the
 * Graphical Editing Framework.
 */
public interface EditDomain {

/**
 * Adds an EditPartViewer into the EditDomain.  A viewer is most likely placed in a {@link
 * org.eclipse.ui.IWorkbenchPart WorkbenchPart} of some form, such as the IEditorPart or
 * an IViewPart.
 * @param viewer The EditPartViewer
 */
void addViewer(EditPartViewer viewer);

/**
 * Called when one of the EditDomain's Viewers receives keyboard focus.
 * @param event The SWT focus event 
 * @param viewer the Viewer that received the event.
 */
void focusGained(FocusEvent event, EditPartViewer viewer);

/**
 * Called when one of the EditDomain's Viewers is losing keyboard focus.
 * @param event The SWT focus event 
 * @param viewer the Viewer that received the event.
 */
void focusLost(FocusEvent event, EditPartViewer viewer);

/**
 * Returns the active Tool
 * @return the active Tool
 */
Tool getActiveTool();

/**
 * Returns the CommandStack.
 * Command stacks could potentially be shared across domains depending on the application.
 * @return The command stack
 */
CommandStack getCommandStack();

/**
 * Called when a key is <B>pressed</B> on a Viewer.
 * @param keyEvent The SWT keyboard event 
 * @param viewer The source of the event.
 */
void keyDown(KeyEvent keyEvent, EditPartViewer viewer);

/**
 * Called when a key is <B>released</b> on a Viewer.
 * @param keyEvent The SWT keyboard event 
 * @param viewer the source of the event.
 */
void keyUp(KeyEvent keyEvent, EditPartViewer viewer);

/**
 * Unloads the current tool and loads the default tool for this Domain.
 */
void loadDefaultTool();

/**
 * Called when the mouse button has been double-clicked on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse button has been pressed on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDown(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse has been dragged within a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseDrag(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse has hovered on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseHover(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse has been moved on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The viewer that the mouse event is over.
 */
void mouseMove(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse button has been released on a Viewer.
 * @param mouseEvent The SWT mouse event 
 * @param viewer The source of the event.
 */
void mouseUp(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when a native drag has started on a Viewer.
 * @param event The DragSourceEvent
 * @param viewer The viewer where the drag started
 */
void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer);

/**
 * Removes a previously added viewer from the EditDomain. A Viewer that is removed from
 * the EditDomain will no longer forward input to the domain and its active Tool.
 * @param viewer the Viewer being removed
 */
void removeViewer(EditPartViewer viewer);

/**
 * Sets the active Tool for this EditDomain.
 * @param tool the Tool
 */
void setTool(Tool tool);

/**
 * Called when the mouse enters a Viewer.
 * @param mouseEvent the SWT mouse event
 * @param viewer the Viewer being entered
 */
void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer);

/**
 * Called when the mouse exits a Viewer.
 * @param mouseEvent the SWT mouse event
 * @param viewer the Viewer being exited
 */
void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer);

}
