package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.dnd.TransferDragSourceListener;
import org.eclipse.gef.dnd.TransferDropTargetListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * An adapter on an SWT {@link org.eclipse.swt.widgets.Control} that manages EditParts,
 * the conceptual objects with which the User interacts. The Viewer is responsible
 * for:
 * <UL>
 *   <LI>Managing the EditParts in it
 *   <LI>Supporting some form of <i>visuals</i>, such as Figures or TreeItems
 *   <LI>Managing the <i>selection</i>
 *   <LI>Managing a <i>focus</i> EditPart
 *   <LI>Routing mouse input and keystrokes to the {@link EditDomain}
 * </UL>
 * 
 * <P>When the Viewer is realized, meaning it has an SWT <code>Control</code>, it should
 * activate its <code>RootEditPart</code>.  When that Control is being disposed, it should
 * {@link EditPart#deactivate()} the root.
 * 
 * <P><I>Visuals</I> is the term used to describe how EditParts are manifested to the
 * User.  {@link org.eclipse.draw2d.IFigure Figures} and {@link
 * org.eclipse.swt.widgets.TreeItem TreeItems} are examples of visuals support by the
 * EditPartViewers included with GEF.  The Viewer creates the necessary infrastructure for
 * hosting its EditParts' visuals.
 * 
 * <P>The Viewer maintains a list of selected EditParts.  The last EditPart in this list
 * is said to have <i>primary</i> selection.  <I>Focus</I> is also maintained by the
 * viewer.  Focus can be changed without affecting the current selection.  This is used
 * for keyboard accessibility.  The Viewer is responsible for reflecting the current
 * focus and selection on its EditParts by calling {@link EditPart#setFocus(boolean)} and
 * {@link EditPart#setSelected(int)}.
 */
public interface EditPartViewer
	extends org.eclipse.jface.viewers.ISelectionProvider
{

interface Conditional {
	boolean evaluate(EditPart editpart);
}

/**
 * Adds a <code>TransferDragSourceListener</code> to this viewer. This has the side-effect
 * of ensuring that a {@link org.eclipse.swt.dnd.DragSource} exists for the viewer's
 * Control.
 * @param listener the listener
 */
void addDragSourceListener(TransferDragSourceListener listener);

/**
 * Adds a <code>TransferDropTargetListener</code> to this viewer. This has the side-effect
 * of ensuring that a {@link org.eclipse.swt.dnd.DropTarget} exists for the viewer's
 * Control.
 * @param listener the listener
 */
void addDropTargetListener(TransferDropTargetListener listener);

/**
 * Appends the specified <code>EditPart</code> to the Viewer's <i>selection</i>. The
 * EditPart becomes the new primary selection. Fires selection changed to all{@link
 * org.eclipse.jface.viewers.ISelectionChangedListener}s.
 * @param editpart the EditPart to append
 */
void appendSelection(EditPart editpart);

/**
 * Optionally creates the default {@link org.eclipse.swt.widgets.Control Control} using
 * the default style.  The Control can also be created externally and then set into the
 * Viewer.
 * @param composite the parent in which create the SWT <code>Control</code>
 * @see #setControl(Control)
 * @return the created Control for convenience
 */
Control createControl(Composite composite);

/**
 * Removes the specified <code>EditPart</code> from the current selection.  If the
 * selection becomes empty, the viewer's {@link #getContents() contents} becomes the
 * current selected part.  The last EditPart in the new selection is made {@link
 * EditPart#SELECTED_PRIMARY primary}.
 * <P>Fires selection changed to
 * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
 * @param editpart the <code>EditPart</code> to deselect
 */
void deselect(EditPart editpart);

/**
 * Deselects all EditParts. The viewer's {@link #getContents() contents} becomes the
 * current selection.  Fires selection changed to
 * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
 */
void deselectAll();

/**
 * Disposes the Viewer. The Viewer will release appropriate resources and remove any
 * listeners. The Viewer will {@link EditPart#deactivate() deactivate} the
 * {@link #getRootEditPart() RootEditPart}, which will deactivate all EditParts in the
 * viewer.
 * @see EditPart#deactivate()
 */
void dispose();

/**
 * Returns <code>null</code> or the <code>EditPart</code> associated with the specified
 * location. The location is relative to the client area of the Viewer's
 * <code>Control</code>.  An EditPart is not directly visible.  It is targeted using its
 * <i>visual part</i> which it registered using the {@link #getVisualPartMap() visual part
 * map}.  What constitutes a <i>visual part</i> is viewer-specific.  Examples include
 * Figures and TreeItems.
 * @param location The location
 * @return <code>null</code> or an EditPart
 */
EditPart findObjectAt(Point location);

/**
 * Returns <code>null</code> or the <code>EditPart</code> at the specified location,
 * excluding the specified set.  This method behaves similarly to {@link
 * #findObjectAt(Point)}.
 * @param location The mouse location
 * @param exclusionSet The set of EditParts to be excluded
 * @return <code>null</code> or an EditPart
 */
EditPart findObjectAtExcluding(Point location, Collection exclusionSet);


/**
 * Returns <code>null</code> or the <code>EditPart</code> at the specified location,
 * using the given exclusion set and conditional. This method behaves similarly to {@link
 * #findObjectAt(Point)}.
 * @param location The mouse location
 * @param exclusionSet The set of EditParts to be excluded
 * @param conditional the Conditional used to evaluate a potential hit
 * @return <code>null</code> or an EditPart
 */
EditPart findObjectAtExcluding(Point location, Collection exclusionSet, Conditional conditional);

/**
 * Flushes all pending updates to the Viewer.
 */
void flush();

/**
 * Returns the <i>contents</i> of this Viewer.  The contents is the EditPart associated
 * with the top-level model object. It is considered to be "The Diagram".  If the user has
 * nothing selected, the <i>contents</i> is implicitly the selected object.
 * <P>The <i>Root</i> of the Viewer is different.  By constrast, the root is never
 * selected or targeted, and does not correspond to something in the model.
 * @see #getRootEditPart()
 * @return the <i>contents</i> <code>EditPart</code>
 */
EditPart getContents();

/**
 * Returns the SWT <code>Control</code> for this viewer.
 * @return the SWT <code>Control</code>
 */
Control getControl();

/**
 * Returns the {@link EditDomain EditDomain} to which this viewer belongs
 * @return the EditDomain
 */
EditDomain getEditDomain();

/**
 * Returns the <code>EditPartFactory</code> for this viewer.  The EditPartFactory is used
 * to create the <i>contents</i> EditPart when {@link #setContents(Object)} is called. It
 * is then used by all EditParts that need to create additional EditParts, such as
 * children.
 * @return EditPartFactory */
EditPartFactory getEditPartFactory();

/**
 * Returns the {@link Map} for registering <code>EditParts</code> by <i>Keys</i>. 
 * EditParts may register themselves using any method, and may register themselved
 * multiple times. The purpose of such registration is to allow an EditPart to be found by
 * other EditParts.
 * @return the registry map
 */
Map getEditPartRegistry();

/**
 * Returns the <i>focus</i> <code>EditPart</code>. Focus refers to keyboard focus.  This
 * is the same concept as focus in a native Tree or Table.  The User can change focus
 * using the keyboard without affecting the currently selected objects.
 * @return the <i>focus</i> <code>EditPart</code> */
EditPart getFocusEditPart();

/**
 * Returns the <code>KeyHandler</code> for this viewer.  The KeyHandler is forwarded keys
 * by the active <code>Tool</code>.  This is important, because only the current tool
 * knows if it is in a state in which keys should be ignored, such as during a drag.  By
 * default, only the {@link org.eclipse.gef.tools.SelectionTool} forwards keysrokes.  It
 * does not do so during a drag.
 * @return KeyHandler */
KeyHandler getKeyHandler();

/**
 * Returns the <code>RootEditPart</code>.  The RootEditPart is a special EditPart
 * that bridges the connection to the viewer.  The <i>root</i> is never selected. 
 * The root does not correspond to anything in the model.  The User does not interact with
 * the root.
 * <P>
 * The RootEditPart has a single child: the {@link #getContents() contents}.
 * <P>
 * By defining the concept of "root", GEF allows the application's "real" EditParts to be
 * more homogeneous.  For example, all non-root EditParts have a parent.
 * @see #getContents()
 * @see #setRootEditPart(RootEditPart)
 * @return the RootEditPart
 */
RootEditPart getRootEditPart();

/**
 * Returns an unmodifiable <code>List</code> containing the selected editparts.
 * @return the selected parts
 */
List getSelectedEditParts();

/**
 * Returns the {@link Map} for associating <i>visual parts</i> with their
 * <code>EditParts</code>. This map is used for hit-testing.  Hit testing is performed by
 * first determining which visual part is hit, and then mapping that part to an
 * <code>EditPart</code>.  What consistutes a <i>visual part</i> is viewer-specific. 
 * Examples include Figures and TreeItems.
 * @return the visual part <code>Map</code>
 */
Map getVisualPartMap();

/**
 * Used for accessibility purposes.
 * @param acc the AccessibleEditPart */
void registerAccessibleEditPart(AccessibleEditPart acc);

/**
 * Replaces the current selection with the specified <code>EditPart</code>. That part
 * becomes the primary selection.
 * <P>Fires selection changed to
 * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
 * @param editpart the new selection
 */
void select(EditPart editpart);

/**
 * Sets the  for this Viewer.
 * @see #getRootEditPart()
 */
void setContents(EditPart editpart);

/**
 * Creates an <code>EditPart</code> for the specified contents using this viewer's
 * <code>EditPartFactory</code>.  That EditPart is then added to the {@link
 * #getRootEditPart() RootEditPart}.
 * @param contents the Object representing the viewer's contents
 * @see #setEditPartFactory(EditPartFactory)
 */
void setContents(Object contents);

/**
 * Sets the <code>ContextMenuProvider</code> for this viewer.  The first time this is
 * called, a {@link org.eclipse.jface.action.MenuManager MenuManager} is created, and a
 * {@link org.eclipse.swt.widgets.Menu Menu} is created and made the context menu for this
 * viewer's <code>Control</code>.
 * @see #getControl()
 * @param contextMenu the <code>ContextMenuProvider</code>
 */
void setContextMenuProvider(ContextMenuProvider contextMenu);

/**
 * Optionally sets the <code>Control</code> for this viewer.  The viewer's control is also
 * set automatically if {@link #createControl(Composite)} is called.
 * @param control the Control
 */
void setControl(Control control);

/**
 * Sets the cursor for the viewer's <code>Control</code>.  This method should only be
 * called by {@link Tool Tools}.  <code>null</code> can be used to indicate that the
 * default cursor should be restored.
 * @param cursor <code>null</code> or a Cursor
 * @see #getControl()
 */
void setCursor(Cursor cursor);

/**
 * Sets the <code>EditDomain</code> for this viewer. The Viewer will route all mouse and
 * keyboard events to the EditDomain.
 * @param domain The EditDomain
 */
void setEditDomain(EditDomain domain);

/**
 * Sets the EditPartFactory.
 * @param factory the factory
 * @see #getEditPartFactory()
 */
void setEditPartFactory(EditPartFactory factory);

/**
 * Sets the <i>focus</i> EditPart.
 * @see #getFocusEditPart()
 * @param focus the FocusPart. */
void setFocus(EditPart focus);

/**
 * Sets the <code>KeyHandler</code>.
 * @param keyHandler the KeyHandler
 * @see #getKeyHandler()
 */
void setKeyHandler(KeyHandler keyHandler);

/**
 * Sets the <i>root</i> of this viewer.  The root should not be confused with the
 * <i>contents</i>.
 * @param root the RootEditPart
 * @see #getRootEditPart()
 * @see #getContents()
 */
void setRootEditPart(RootEditPart root);

/**
 * Turns on/off the routing of events directly to the Editor. If supported by the viewer
 * implementation, all Events should be routed to the <code>EditDomain</code> rather than
 * handled in the default way.
 * @param value true if the viewer should route events to the EditDomain
 */
void setRouteEventsToEditDomain(boolean value);

/**
 * Used for accessibility purposes
 * @param acc the accessible part */
void unregisterAccessibleEditPart(AccessibleEditPart acc);

}
