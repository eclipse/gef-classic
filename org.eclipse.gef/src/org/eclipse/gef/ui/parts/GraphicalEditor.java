package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.*;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GEFPlugin;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.PropertySheetPage;

public abstract class GraphicalEditor
	extends EditorPart
	implements CommandStackListener, ISelectionListener
{

private DefaultEditDomain editDomain;
private GraphicalViewer graphicalViewer;
private SelectionSynchronizer synchronizer;
private Map actions = new HashMap();
private List selectionActions = new ArrayList();
private List stackActions = new ArrayList();
private List propertyActions = new ArrayList();

public GraphicalEditor() {}

public void commandStackChanged(EventObject event) {
	updateStackDependentActions();
}

protected void configureGraphicalViewer() {
	getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);
}

protected void createActions() {
	setAction(IWorkbenchActionConstants.UNDO, new UndoAction(this));
	markAsStackDependentAction(IWorkbenchActionConstants.UNDO, true);
	setAction(IWorkbenchActionConstants.REDO, new RedoAction(this));
	markAsStackDependentAction(IWorkbenchActionConstants.REDO, true);
	setAction(IWorkbenchActionConstants.DELETE, new DeleteAction(this));
	markAsSelectionDependentAction(IWorkbenchActionConstants.DELETE, true);
	setAction(IWorkbenchActionConstants.SAVE, new SaveAction(this));
	markAsPropertyDependentAction(IWorkbenchActionConstants.SAVE, true);
	setAction(IWorkbenchActionConstants.PRINT, new PrintAction(this));
}

/**
 * Creates the GraphicalViewer on the specified <code>Composite</code>.
 */
protected void createGraphicalViewer(Composite parent) {
	GraphicalViewer viewer = new ScrollingGraphicalViewer();
	viewer.createControl(parent);
	setGraphicalViewer(viewer);
	configureGraphicalViewer();
	hookGraphicalViewer();
	initializeGraphicalViewer();
}

/**
 * Realizes the Editor by creating it's Control.
 * <P>WARNING: This method may or may not be called by Eclipse prior to {@link #dispose()}.
 */
public void createPartControl(Composite parent) {
	createGraphicalViewer(parent);
}

public void dispose() {
	getEditDomain().setTool(null);
	disposeActions();
	getGraphicalViewer().dispose();
	super.dispose();
}

/**
 * Iterates through the registered actions and calls {@link 
 */
protected void disposeActions() {
	Iterator iter = actions.values().iterator();
	while (iter.hasNext()) {
		Object action = iter.next();
		if (action instanceof Disposable)
			((Disposable)action).dispose();
	}
}

/**
 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
 */
protected void firePropertyChange(int property) {
	super.firePropertyChange(property);
	updatePropertyDependentActions();
}

/**
 * Returns the action with the given id.  <i>id</i> must not be <code>null</code>.
 * @param id The id of the requested action. * @return IAction The action corresponding to id. */
public IAction getAction(String id) {
	return (IAction)actions.get(id);
}

/**
 * Returns the adapter for the specified key.
 * 
 * <P>getAdapter may be called before {@link #createPartControl(Composite)}.
 * The order is unspecified in JFace.
 */
public Object getAdapter(Class type) {
	if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class){
		PropertySheetPage page = new PropertySheetPage();
		page.setRootEntry(GEFPlugin.createUndoablePropertySheetEntry(getCommandStack()));
		return page;
	}
	if (type == GraphicalViewer.class)
		return getGraphicalViewer();
	if (type == CommandStack.class)
		return getCommandStack();
	return super.getAdapter(type);
}

protected CommandStack getCommandStack() {
	return getEditDomain().getCommandStack();
}

protected DefaultEditDomain getEditDomain() {
	return editDomain;
}

protected GraphicalViewer getGraphicalViewer() {
	return graphicalViewer;
}

protected SelectionSynchronizer getSelectionSynchronizer() {
	if (synchronizer == null)
		synchronizer = new SelectionSynchronizer();
	return synchronizer;
}

/**
 * Hooks the GraphicalViewer to the rest of the Editor.  By default, the viewer
 * is added to the SelectionSynchronizer, which can be used to keep 2 or more
 * EditPartViewers in sync.  The viewer is also registered as the ISelectionProvider
 * for the Editor's PartSite.
 */
protected void hookGraphicalViewer() {
	getSelectionSynchronizer().addViewer(getGraphicalViewer());
	getSite().setSelectionProvider(getGraphicalViewer());
}

public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	setSite(site);
	setInput(input);
	getCommandStack().addCommandStackListener(this);
	getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);}

/**
 * Override to set the contents of the GraphicalViewer after it has been created.
 * @see #createGraphicalViewer(Composite)
 */
abstract protected void initializeGraphicalViewer();

/**
 * Marks or unmarks the given action to be updated on property changes.
 *
 * @param id the action id
 * @param mark <code>true</code> if the action is property dependent
 */
public void markAsPropertyDependentAction(String id, boolean mark) {
	if (mark)
		if (!propertyActions.contains(id))
			propertyActions.add(id);
	else
		propertyActions.remove(id);
}

/**
 * Marks or unmarks the given action to be updated on selection changes.
 *
 * @param id the action id
 * @param mark <code>true</code> if the action is selection dependent
 */
public void markAsSelectionDependentAction(String id, boolean mark) {
	if (mark)
		if (!selectionActions.contains(id))
			selectionActions.add(id);
	else
		selectionActions.remove(id);
}

/**
 * Marks or unmarks the given action to be updated on stack changes.
 *
 * @param id the action id
 * @param mark <code>true</code> if the action is stack dependent
 */
public void markAsStackDependentAction(String id, boolean mark) {
	if (mark)
		if (!stackActions.contains(id))
			stackActions.add(id);
	else
		stackActions.remove(id);
}

/**
 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
 */
public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	updateSelectionDependentActions();
}

/**
 * Registers the given action and associates it with the given id. <i>id</i> must not be
 * <code>null</code>. If <i>action</i> is <code>null</code>, the action previously
 * associated with <i>id</i> will be removed.
 * @param id The id of the action being set. * @param action The action being set, or null. */
public void setAction(String id, IAction action) {
	if (action == null)
		actions.remove(id);
	else
		actions.put(id, action);
}

/**
 * Sets the EditDomain for this EditorPart.
 */
protected void setEditDomain(DefaultEditDomain ed){
	this.editDomain = ed;
}

public void setFocus(){
	getGraphicalViewer().getControl().setFocus();
}

/**
 * Sets the graphicalViewer for this EditorPart.
 */
protected void setGraphicalViewer(GraphicalViewer viewer) {
	getEditDomain().addViewer(viewer);
	this.graphicalViewer = viewer;
}

protected void setSite(org.eclipse.ui.IWorkbenchPartSite site){
	super.setSite(site);
	createActions();
	updatePropertyDependentActions();
	updateStackDependentActions();
}

private void updateAction(String id) {
	if (id == null)
		return;
	if (actions != null) {
		IAction action = (IAction)actions.get(id);
		if (action instanceof Updatable)
			((Updatable)action).update();
	}
}

protected void updatePropertyDependentActions() {
	Iterator iter = propertyActions.iterator();
	while (iter.hasNext())
		updateAction((String)iter.next());
}

protected void updateSelectionDependentActions() {
	Iterator iter = selectionActions.iterator();
	while (iter.hasNext())
		updateAction((String)iter.next());
}

protected void updateStackDependentActions() {
	Iterator iter = stackActions.iterator();
	while (iter.hasNext())
		updateAction((String)iter.next());
}

}
