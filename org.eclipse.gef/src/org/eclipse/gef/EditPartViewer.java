package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.gef.dnd.*;

import org.eclipse.gef.ui.parts.ContextMenuProvider;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.util.*;

/**
 * Displays EditParts, manages selection, and routes Input events to
 * an EditDomain.  The EditDomain typically sends these events to the
 * Tool that is currently active.
 *
 * The output of an EditPartViewer is the set of selected EditParts.
 * The last member of the selection is said to be "primary" selected.
 * The Viewer tells EditParts of their membership to the viewer's selection
 * by calling setSelection(int)
 */
public interface EditPartViewer
	extends org.eclipse.jface.viewers.ISelectionProvider
{

/**
 * Adds the TransferDragSourceListener to the viewer to handle
 * DragEvents.
 */
void addDragSourceListener(TransferDragSourceListener listener);

/**
 * Adds the TransferDropTargetListener to the viewer to handle
 * DropEvents.
 */
void addDropTargetListener(TransferDropTargetListener listener);

/**
 * appends the EditPart to the Viewer's selection set. The EditPart becomes the
 * new Primary selection.
 * Fires selection changed to ISelectionListeners.
 */
void appendSelection(EditPart editpart);

/**
 * if called, will create the default {@link org.eclipse.swt.widgets.Control
 * Control} with the default style bits.  The control can also be
 * created externally and then set into the Viewer.
 * @see #setControl(Control)
 */
Control createControl(Composite composite);

/**
 * Removes the EditPart from the selection, and sets the last member of the
 * selection to be the "primary" selection.
 * Fires selection changed to ISelectionListeners.
 */
void deselect(EditPart editpart);

/**
 * deselects all EditParts.
 * Fires selection changed to ISelectionListeners.
 */
void deselectAll();

/**
 * Disposes the viewer.  Dispose should unhook all Listeners, and free any
 * allocated resources.  Dispose should also deactivate the root EditPart, which
 * will deactivate all EditParts.
 * @see EditPart#deactivate()
 */
void dispose();

/**
 * Returns <code>null</code> or the EditPart at the given mouse location.
 * @param point The mouse location
 */
EditPart findObjectAt(Point p);

/**
 * Returns <code>null</code> or the EditPart at the given mouse location,
 *  excluding all EditParts in the Collection
 * <code>c</code>.  If the editpart at the location is part of the exclusion set, the Viewer
 * should try to search through other EditParts in z-order that also occupy that point.
 * @param point The mouse location
 * @param c The collection of editpart that should be excluded from the search
 */
EditPart findObjectAtExcluding(Point p, Collection c);

void flush();

/**
 * Returns the contents of this EditPartViewer.
 */
EditPart getContents();

/**
 * Returns the SWT Control for this viewer.
 */
Control getControl();

/**
 * Returns the {@link EditDomain EditDomain} to which the viewer belongs
 */
EditDomain getEditDomain();

EditPartFactory getEditPartFactory();

/**
 * Returns the map for registering EditParts by Keys.  EditParts may register
 * themselves using any method, and may register themselved multiple times.
 *  This registry is used to help EditParts find
 * other EditParts.
 */
Map getEditPartRegistry();

EditPart getFocusEditPart();

KeyHandler getKeyHandler();

/**
 * Returns the RootEditPart.
 * The root is typically not replaced, although it may be.
 * Also, the root is generally a placeholder and has no corresponding view,
 * not does it support editing of any kind.
 * A RootEditPart has no parent, and knows its Viewer.
 * @see #setContents(EditPart)
 * @see #setRootEditPart(RootEditPart)
 */
RootEditPart getRootEditPart();

/**
 * Returns a list containing the currently selected editparts.
 */
List getSelectedEditParts();

/**
 * Returns the map for registering visual parts to the EditParts which
 * created them.  This map is used externally by EditParts to register
 * one or more visual parts.  It is used internally by the viewer during
 * hit-testing to map a visual to its EditPart.
 */
Map getVisualPartMap();

void registerAccessibleEditPart(AccessibleEditPart acc);

/**
 * Replaces the current selection with the EditPart.
 * Fires selectionChanged event to ISelectionListeners.
 */
void select(EditPart editpart);

/**
 * Sets the Contents for this Viewer.
 * The Contents EditPart is installed in the RootEditPart for this Viewer.
 * @see #getRootEditPart()
 * @see #setRootEditPart(RootEditPart)
 */
void setContents(EditPart editpart);

/**
 * Creates an EditPart for the specified contents using this viewers Factory.
 * @see #setEditPartFactory(EditPartFactory)
 */
void setContents(Object contents);

/**
 * sets the ContextMenuProvider that will be used when the user
 * invokes the context menu on the Viewer's control.
 * @see #getControl()
 */
void setContextMenuProvider(ContextMenuProvider menuProvider);

/**
 * Optionally sets the control that the viewer should use.
 * @see #createControl(Composite)
 */
void setControl(Control control);

/**
 * Sets the cursor for the viewer's control.
 * @see #getControl()
 */
void setCursor(Cursor c);

/**
 * Sets the EditDomain for this viewer.
 * When this editor is set, it is the
 * viewer's resposibility to route all of the mouse
 * and keyboard events to the editor.
 * @param domain The EditDomain
 */
void setEditDomain(EditDomain domain);

void setEditPartFactory(EditPartFactory factory);

void setFocus(EditPart part);

void setKeyHandler(KeyHandler keyHandler);

/**
 * Sets the root of this viewer.
 * It is uncommon to replace the default root.
 * Instead, applications will typically call <code>
 * {@link #setContents(EditPart) setContents(EditPart)}</code>
 */
void setRootEditPart(RootEditPart root);

/**
 * Turns on/off the routing of events directly to the Editor.
 * If supported by the viewer, all events should be routed
 * to the EditDomain rather than handled in the default way.
 */
void setRouteEventsToEditor(boolean value);

void unregisterAccessibleEditPart(AccessibleEditPart acc);

}
