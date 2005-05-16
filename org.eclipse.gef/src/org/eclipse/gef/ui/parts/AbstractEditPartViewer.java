/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.RootEditPart;

/**
 * The base implementation for EditPartViewer.
 * @author hudsonr
 */
public abstract class AbstractEditPartViewer
	implements EditPartViewer
{

private DisposeListener lDispose;
	
/**
 * The raw list of selected editparts.
 */
protected final List selection = new ArrayList();
/**
 * The unmodifiable list of selected editparts.
 */
protected final List constantSelection = Collections.unmodifiableList(selection);

/**
 * The list of selection listeners.
 */
protected List selectionListeners = new ArrayList(1);

private EditPartFactory factory;
private Map mapIDToEditPart = new HashMap();
private Map mapVisualToEditPart = new HashMap();
private Map properties;
private Control control;
private EditDomain domain;
private RootEditPart rootEditPart;
 /**
  * The editpart specifically set to have focus.  Note that if this value is
  * <code>null</code>, the focus editpart is still implied to be the part with primary
  * selection.  Subclasses should call the accessor: {@link #getFocusEditPart()} whenever
  * possible.
  */
protected EditPart focusPart;
private MenuManager contextMenu;
private org.eclipse.gef.dnd.DelegatingDragAdapter dragAdapter = 
	new org.eclipse.gef.dnd.DelegatingDragAdapter();
private DragSource dragSource;
private org.eclipse.gef.dnd.DelegatingDropAdapter dropAdapter = 
	new org.eclipse.gef.dnd.DelegatingDropAdapter();
private DropTarget dropTarget;
private KeyHandler keyHandler;
private PropertyChangeSupport changeSupport;

/**
 * Constructs the viewer and calls {@link #init()}.
 */
public AbstractEditPartViewer() {
	init();
}

/**
 * @see
 * EditPartViewer#addDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener)
 */
public void addDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener listener) {
	addDragSourceListener((TransferDragSourceListener)listener);
}

/**
 * @see EditPartViewer#addDragSourceListener(TransferDragSourceListener)
 */
public void addDragSourceListener(TransferDragSourceListener listener) {
	getDelegatingDragAdapter().addDragSourceListener(listener);
	refreshDragSourceAdapter();
}

/**
 * @see
 * EditPartViewer#addDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener)
 */
public void addDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener listener) {
	addDropTargetListener((TransferDropTargetListener)listener);
}

/**
 * @see EditPartViewer#addDropTargetListener(TransferDropTargetListener)
 */
public void addDropTargetListener(TransferDropTargetListener listener) {
	getDelegatingDropAdapter().addDropTargetListener(listener);
	refreshDropTargetAdapter();
}

/**
 * @see org.eclipse.gef.EditPartViewer#addPropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	if (changeSupport == null)
		changeSupport = new PropertyChangeSupport(this);
	changeSupport.addPropertyChangeListener(listener);
}

/**
 * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
 */
public void addSelectionChangedListener(ISelectionChangedListener listener) {
	selectionListeners.add(listener);
}

/**
 * @see EditPartViewer#appendSelection(org.eclipse.gef.EditPart)
 */
public void appendSelection(EditPart editpart) {
	if (editpart != focusPart)
		setFocus(null);
	List list = primGetSelectedEditParts();
	if (!list.isEmpty()) {
		EditPart primary = (EditPart)list.get(list.size() - 1);
		primary.setSelected(EditPart.SELECTED);
	}
	// if the editpart is already in the list, re-order it to be the last one
	list.remove(editpart);
	list.add(editpart);
	editpart.setSelected(EditPart.SELECTED_PRIMARY);
	
	fireSelectionChanged();
}

/**
 * @see EditPartViewer#createControl(org.eclipse.swt.widgets.Composite)
 */
public abstract Control createControl(Composite parent);

/**
 * @see EditPartViewer#deselect(org.eclipse.gef.EditPart)
 */
public void deselect(EditPart editpart) {
	editpart.setSelected(EditPart.SELECTED_NONE);

	List selection = primGetSelectedEditParts();
	selection.remove(editpart);
	if (!selection.isEmpty()) {
		EditPart primary = (EditPart)selection.get(selection.size() - 1);
		primary.setSelected(EditPart.SELECTED_PRIMARY);
	}
	fireSelectionChanged();
}

/**
 * @see EditPartViewer#deselectAll()
 */
public void deselectAll() {
	EditPart part;
	List list = primGetSelectedEditParts();
	setFocus(null);
	for (int i = 0; i < list.size(); i++) {
		part = (EditPart)list.get(i);
		part.setSelected(EditPart.SELECTED_NONE);
	}
	list.clear();
	fireSelectionChanged();
}

/**
 * Called if and when the <code>Control</code> is disposed. Subclasses may extend this
 * method to perform additional cleanup.
 * @param e the disposeevent
 */
protected void handleDispose(DisposeEvent e) {
	setControl(null);
}

/**
 * @see EditPartViewer#findObjectAt(Point)
 */
public final EditPart findObjectAt(Point pt) {
	return findObjectAtExcluding(pt, Collections.EMPTY_SET);
}

/**
 * @see EditPartViewer#findObjectAtExcluding(Point, Collection)
 */
public final EditPart findObjectAtExcluding(Point pt, Collection exclude) {
	return findObjectAtExcluding(pt, exclude, null);
}

/**
 * Fires selection changed to the registered listeners at the time called.
 */
protected void fireSelectionChanged() {
	Object listeners[] = selectionListeners.toArray();
	SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
	for (int i = 0; i < selectionListeners.size(); i++)
		((ISelectionChangedListener)listeners[i])
			.selectionChanged(event);
}

/**
 * @see EditPartViewer#flush()
 */
public void flush() { }

/**
 * @see EditPartViewer#getContextMenu()
 */
public MenuManager getContextMenu() {
	return contextMenu;
}

/**
 * @see EditPartViewer#getContents()
 */
public EditPart getContents() {
	return getRootEditPart().getContents();
}

/**
 * @see EditPartViewer#getControl()
 */
public Control getControl() {
	return control;
}

/**
 * Returns <code>null</code> or the DelegatingDragAdapater. The adapter is created
 * automatically when {@link #addDragSourceListener(TransferDragSourceListener)} is
 * called.
 * 
 * @return <code>null</code> or the adapter
 */
protected org.eclipse.gef.dnd.DelegatingDragAdapter getDelegatingDragAdapter() {
	return dragAdapter;
}

/**
 * Returns <code>null</code> or the DelegatingDropAdapater. The adapter is created
 * automatically when {@link #addDropTargetListener(TransferDropTargetListener)} is
 * called.
 * @return <code>null</code> or the adapter
 */
protected org.eclipse.gef.dnd.DelegatingDropAdapter getDelegatingDropAdapter() {
	return dropAdapter;
}

/**
 * Returns <code>null</code> or the DragSource. The drag source is created automatically
 * when {@link #addDragSourceListener(TransferDragSourceListener)} is called.
 * @return <code>null</code> or the drag source
 */
protected DragSource getDragSource() {
	return dragSource;
}

/**
 * Returns <code>null</code> or the DropTarget. The drop target is created automatically
 * when {@link #addDropTargetListener(TransferDropTargetListener)} is called.
 * @return <code>null</code> or the drop target
 */
protected DropTarget getDropTarget() {
	return dropTarget;
}

/**
 * @see EditPartViewer#getEditDomain()
 */
public EditDomain getEditDomain() {
	return domain;
}

/**
 * @see EditPartViewer#getEditPartFactory()
 */
public EditPartFactory getEditPartFactory() {
	return factory;
}

/**
 * @see EditPartViewer#getEditPartRegistry()
 */
public Map getEditPartRegistry() {
	return mapIDToEditPart;
}

/**
 * @see EditPartViewer#getFocusEditPart()
 */
public EditPart getFocusEditPart() {
	if (focusPart != null)
		return focusPart;
	if (getSelectedEditParts().isEmpty()) {
		if (getContents() != null)
			return getContents();
		else
			return getRootEditPart();
	}
	List selection = getSelectedEditParts();
	return (EditPart)selection.get(selection.size() - 1);
}

/**
 * @see EditPartViewer#getKeyHandler()
 */
public KeyHandler getKeyHandler() {
	return keyHandler;
}

/**
 * @see org.eclipse.gef.EditPartViewer#getProperty(java.lang.String)
 */
public Object getProperty(String key) {
	if (properties != null)
		return properties.get(key);
	return null;
}

/**
 * @see EditPartViewer#getRootEditPart()
 */
public RootEditPart getRootEditPart() {
	return rootEditPart;
}

/**
 * @see EditPartViewer#getSelectedEditParts()
 */
public List getSelectedEditParts() {
	return constantSelection;
}

/**
 * Returns an ISelection containing a list of one or more EditPart. Whenever {@link
 * #getSelectedEditParts()} returns an empty list, the <i>contents</i> editpart ({@link
 * #getContents()}) is returned as the current selection.
 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
 */
public ISelection getSelection() {
	if (getSelectedEditParts().isEmpty() && getContents() != null)
		return new StructuredSelection(getContents());
	return new StructuredSelection(getSelectedEditParts());
}

/**
 * @see EditPartViewer#getVisualPartMap()
 */
public Map getVisualPartMap() {
	return mapVisualToEditPart;
}

/**
 * Called once the control has been set.
 * @see #unhookControl()
 */
protected void hookControl() {
	Assert.isTrue(getControl() != null);
	getControl().addDisposeListener(lDispose = new DisposeListener() {
		public void widgetDisposed(DisposeEvent e) {
			handleDispose(e);
		}
	});
	if (getRootEditPart() != null)
		getRootEditPart().activate();
	refreshDragSourceAdapter();
	refreshDropTargetAdapter();
	if (contextMenu != null)
		getControl().setMenu(contextMenu.createContextMenu(getControl()));
}

/**
 * Called whenever the {@link #getDragSource() drag source} is automatically created.
 */
protected void hookDragSource() {
	dragSource.addDragListener(getDelegatingDragAdapter());
}

/**
 * Called whenever the {@link #getDropTarget() drop target} is automatically created.
 */
protected void hookDropTarget() {
	getDropTarget().addDropListener(getDelegatingDropAdapter());
}

/**
 * Called from the constructor.  Subclasses may extend this method.
 */
protected void init() { }

private void primDeselectAll() {
	EditPart part;
	List list = primGetSelectedEditParts();
	for (int i = 0; i < list.size(); i++) {
		part = (EditPart)list.get(i);
		part.setSelected(EditPart.SELECTED_NONE);
	}
	list.clear();
}

/**
 * Returns the modifiable List of selected EditParts.
 * @return the internal list of selected editparts
 */
protected List primGetSelectedEditParts() {
	return selection;
}

/**
 * Called whenever it may be appropriate to automatically create or dispose the drag
 * source.
 */
protected void refreshDragSourceAdapter() {
	if (getControl() == null)
		return;
	if (getDelegatingDragAdapter().isEmpty())
		setDragSource(null);
	else {
		if (getDragSource() == null)
			setDragSource(
				new DragSource(
					getControl(),
					DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK));
		getDragSource().setTransfer(getDelegatingDragAdapter().getTransfers());
	}
}

/**
 * Called whenever it may be appropriate to automatically create or dispose the drop
 * target.
 *
 */
protected void refreshDropTargetAdapter() {
	if (getControl() == null)
		return;
	if (getDelegatingDropAdapter().isEmpty())
		setDropTarget(null);
	else {
		if (getDropTarget() == null)
			setDropTarget(
				new DropTarget(
					getControl(),
					DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK));
		getDropTarget().setTransfer(getDelegatingDropAdapter().getTransfers());
	}
}

/**
 * @see EditPartViewer#registerAccessibleEditPart(AccessibleEditPart)
 */
public void registerAccessibleEditPart(AccessibleEditPart acc) { }

/**
 * @see
 * EditPartViewer#removeDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener)
 * @deprecated
 */
public void removeDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener listener) {
	removeDragSourceListener((TransferDragSourceListener)listener);
}

/**
 * @see EditPartViewer#removeDragSourceListener(TransferDragSourceListener)
 */
public void removeDragSourceListener(TransferDragSourceListener listener) {
	getDelegatingDragAdapter().removeDragSourceListener(listener);
	if (getDelegatingDragAdapter().isEmpty())
		refreshDragSourceAdapter();
}

/**
 * @see
 * EditPartViewer#removeDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener)
 * @deprecated
 */
public void removeDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener listener) {
	removeDropTargetListener((TransferDropTargetListener)listener);
}

/**
 * @see EditPartViewer#removeDropTargetListener(TransferDropTargetListener)
 */
public void removeDropTargetListener(TransferDropTargetListener listener) {
	getDelegatingDropAdapter().removeDropTargetListener(listener);
	if (getDelegatingDropAdapter().isEmpty())
		refreshDropTargetAdapter();
}

/**
 * @see org.eclipse.gef.EditPartViewer#removePropertyChangeListener(java.beans.PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	if (changeSupport != null) {
		changeSupport.removePropertyChangeListener(listener);
		if (changeSupport.getPropertyChangeListeners().length == 0)
			changeSupport = null;
	}
}

/**
 * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
 */
public void removeSelectionChangedListener(ISelectionChangedListener l) {
	selectionListeners.remove(l);
}

/**
 * @see EditPartViewer#reveal(EditPart)
 */
public void reveal(EditPart part) { }

/**
 * @see EditPartViewer#select(org.eclipse.gef.EditPart)
 */
public void select(EditPart editpart) {
	// If selection isn't changing, do nothing.
	if ((getSelectedEditParts().size() == 1)
	  && (getSelectedEditParts().get(0) == editpart))
		return;
	primDeselectAll();
	appendSelection(editpart);  // fireSelectionChanged() is called here
}

/**
 * @see EditPartViewer#setContextMenu(org.eclipse.jface.action.MenuManager)
 */
public void setContextMenu(MenuManager manager) {
	if (contextMenu != null)
		contextMenu.dispose();
	contextMenu = manager;
	if (getControl() != null && !getControl().isDisposed())
		getControl().setMenu(
			contextMenu.createContextMenu(getControl()));
}

/**
 * @see EditPartViewer#setContents(org.eclipse.gef.EditPart)
 */
public void setContents(EditPart editpart) {
	getRootEditPart().setContents(editpart);
}

/**
 * @see EditPartViewer#setContents(java.lang.Object)
 */
public void setContents(Object contents) {
	Assert.isTrue(getEditPartFactory() != null,
		"An EditPartFactory is required to call setContents(Object)");//$NON-NLS-1$
	setContents(getEditPartFactory().
			createEditPart(null, contents));
}

/**
 * @see EditPartViewer#setControl(org.eclipse.swt.widgets.Control)
 */
public void setControl(Control control) {
	if (this.control != null)
		unhookControl();
	this.control = control;
	if (control != null)
		hookControl();
}

/**
 * @see EditPartViewer#setCursor(org.eclipse.swt.graphics.Cursor)
 */
public void setCursor(Cursor cursor) {
	if (getControl() == null || getControl().isDisposed())
		return;
	getControl().setCursor(cursor);
}

/**
 * Sets the drag source. Called from {@link #refreshDragSourceAdapter()}.
 * @param source <code>null</code> or a drag source
 */
protected void setDragSource(DragSource source) {
	if (dragSource != null)
		dragSource.dispose();
	dragSource = source;
	if (dragSource != null)
		hookDragSource();
}

/**
 * Sets the drop target. Called from {@link #refreshDropTargetAdapter()}.
 * @param target dropTarget <code>null</code> or a drop target
 */
protected void setDropTarget(DropTarget target) {
	if (dropTarget != null)
		dropTarget.dispose();
	dropTarget = target;
	if (dropTarget != null)
		hookDropTarget();
}

/**
 * @see EditPartViewer#setEditDomain(org.eclipse.gef.EditDomain)
 */
public void setEditDomain(EditDomain editdomain) {
	this.domain = editdomain;
}

/**
 * @see EditPartViewer#setEditPartFactory(org.eclipse.gef.EditPartFactory)
 */
public void setEditPartFactory(EditPartFactory factory) {
	this.factory = factory;
}

/**
 * @see EditPartViewer#setFocus(org.eclipse.gef.EditPart)
 */
public void setFocus(EditPart part) {
	if (focusPart == part)
		return;
	if (focusPart != null && getControl() != null && getControl().isFocusControl())
		focusPart.setFocus(false);
	focusPart = part;
	if (focusPart != null && getControl() != null && getControl().isFocusControl())
		focusPart.setFocus(true);
}

/**
 * @see EditPartViewer#setKeyHandler(org.eclipse.gef.KeyHandler)
 */
public void setKeyHandler(KeyHandler handler) {
	keyHandler = handler;
}

/**
 * @see org.eclipse.gef.EditPartViewer#setProperty(java.lang.String, java.lang.Object)
 */
public void setProperty(String key, Object value) {
	if (properties == null)
		properties = new HashMap();
	Object old;
	if (value == null)
		old = properties.remove(key);
	else
		old = properties.put(key, value);
		
	if (changeSupport != null)
		changeSupport.firePropertyChange(key, old, value);
}

/**
 * @see EditPartViewer#setRootEditPart(org.eclipse.gef.RootEditPart)
 */
public void setRootEditPart(RootEditPart editpart) {
	if (rootEditPart != null) {
		if (rootEditPart.isActive())
			rootEditPart.deactivate();
		rootEditPart.setViewer(null);
	}
	rootEditPart = editpart;
	rootEditPart.setViewer(this);
	if (getControl() != null)
		rootEditPart.activate();
}

/**
 * @see EditPartViewer#setRouteEventsToEditDomain(boolean)
 */
public void setRouteEventsToEditDomain(boolean value) { }

/**
 * Sets the selection to the given selection and fires selection changed. The ISelection
 * should be an {@link IStructuredSelection} or it will be ignored.
 * @see ISelectionProvider#setSelection(ISelection)
 */
public void setSelection(ISelection newSelection) {
	if (!(newSelection instanceof IStructuredSelection))
		return;
	
	List orderedSelection = ((IStructuredSelection)newSelection).toList();
	// Convert to HashSet to optimize performance.
	Collection editparts = new HashSet(orderedSelection);
	List selection = primGetSelectedEditParts();

	setFocus(null);
	for (int i = 0; i < selection.size(); i++) {
		EditPart part = (EditPart)selection.get(i);
		if (!editparts.contains(part))
			part.setSelected(EditPart.SELECTED_NONE);
	}
	selection.clear();
	
	if (!orderedSelection.isEmpty()) {
		Iterator itr = orderedSelection.iterator();
		while (true) {
			EditPart part = (EditPart)itr.next();
			selection.add(part);
			if (!itr.hasNext()) {
				part.setSelected(EditPart.SELECTED_PRIMARY);
				break;
			}
			part.setSelected(EditPart.SELECTED);
		}
	}

	fireSelectionChanged();
}

/**
 * Called when the control is being set to <code>null</code>, but before it is null.
 */
protected void unhookControl() {
	Assert.isTrue(getControl() != null);
	
	if (lDispose != null) {
		getControl().removeDisposeListener(lDispose);
		lDispose = null;
	}
	if (getContextMenu() != null)
		getContextMenu().dispose();
	if (getRootEditPart() != null)
		getRootEditPart().deactivate();
}

/**
 * Does nothing by default. Subclasses needing to add accessibility support should
 * override this method.
 * @see EditPartViewer#unregisterAccessibleEditPart(org.eclipse.gef.AccessibleEditPart)
 */
public void unregisterAccessibleEditPart(AccessibleEditPart acc) { }

}
