package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.*;

/**
 * Generates {@link Request Requests} or other high-level interpretations of
 * raw event data.  Raw event data is sent to Tools from EditPartViewers.
 * Tools may interpret this data in any way appropriate.
 * <P>Typically, tools will evoke some change on the application's model by
 * executing a {@link org.eclipse.gef.commands.Command}.  This command
 * should be obtained by sending Requests to the EditParts in the viewer.
 * <P>Tools may also just change some state of the Viewer, such as selection.
 * <P>Tools may show feedback during the operation and ask the involved
 * EditParts to also show feedback.
 * <table><tr><td><img src="doc-files/important.gif"/><td>
 *    All feedback should be erased
 * and temporary changes reverted prior to executing any command.</tr></table>
 */
public interface Tool {

/**
 * Initialize the tool and setup any resources needed.
 * @see #deactivate()
 */
public void activate();

/**
 * Called when anothre Tool becomes the active tool for the Editor.
 * implement this method to clean-up any tool state or resources.
 */
public void deactivate();

/**
 * Called when a viewer that the editor controls gains focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The viewer that the focus event is over.
 */
void focusGained(FocusEvent event, EditPartViewer viewer);

/**
 * Called when a viewer that the editor controls loses focus.
 * 
 * @param event The SWT focus event 
 * @param viewer The viewer that the focus event is over.
 */
void focusLost(FocusEvent event, EditPartViewer viewer);

/*
 Returns the editor
 */
//EditDomain getEditDomain();

/**
 * Called when a key is pressed within a viewer that
 * the editor controls.
 */
void keyDown(KeyEvent keyEvent, EditPartViewer view);
/**
 * Called when a key is released within a viewer that
 * the editor controls.
 */
void keyUp(KeyEvent keyEvent, EditPartViewer view);
/**
 * Called when the mouse button has been double clicked over
 * a viewer that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseDoubleClick(MouseEvent mouseEvent, EditPartViewer view);
/**
 * Called when the mouse button has been pressed over
 * a viewer that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseDown(MouseEvent mouseEvent, EditPartViewer view);
/**
 * Called when the mouse has been dragged within
 * a viewer that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseDrag(MouseEvent mouseEvent, EditPartViewer view);
/**
 * Called when the mouse has hovered within
 * a viewer that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseHover(MouseEvent mouseEvent, EditPartViewer view);
/**
 * Called when the mouse has been moved within
 * a based that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseMove(MouseEvent mouseEvent, EditPartViewer view);
/**
 * Called when the mouse button has been released over
 * a viewer that the editor controls.
 * @param mouseEvent org.eclipse.swt.events.MouseEvent  The SWT mouse event 
 * @param viewer org.eclipse.gef.IViewer The viewer that the mouse event is over.
 */
void mouseUp(MouseEvent mouseEvent, EditPartViewer view);

void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer);

void setEditDomain(EditDomain domain);

void setViewer(EditPartViewer view);

void viewerEntered(MouseEvent mouseEvent, EditPartViewer viewer);

void viewerExited(MouseEvent mouseEvent, EditPartViewer viewer);

}
