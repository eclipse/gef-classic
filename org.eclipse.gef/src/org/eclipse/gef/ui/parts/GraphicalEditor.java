package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.*;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.PropertySheetPage;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.*;

public abstract class GraphicalEditor
	extends EditorPart
	implements CommandStackListener, ISelectionListener
{

private DefaultEditDomain editDomain;
private GraphicalViewer graphicalViewer;
private ActionRegistry actionRegistry;
private SelectionSynchronizer synchronizer;
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

/**
 * Creates actions for this editor.  Subclasses should override this method to create
 * and register actions with the {@link ActionRegistry}.
 */
protected void createActions() {
	ActionRegistry registry = getActionRegistry();
	IAction action;
	
	action = new UndoAction(this);
	registry.registerAction(action);
	markAsStackDependentAction(action.getId(), true);
	
	action = new RedoAction(this);
	registry.registerAction(action);
	markAsStackDependentAction(action.getId(), true);
	
	action = new DeleteAction(this);
	registry.registerAction(action);
	markAsSelectionDependentAction(action.getId(), true);
	
	action = new SaveAction(this);
	registry.registerAction(action);
	markAsPropertyDependentAction(action.getId(), true);
	
	action = new PrintAction(this);
	registry.registerAction(action);
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
	getActionRegistry().dispose();
	getGraphicalViewer().dispose();
	super.dispose();
}

/**
 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
 */
protected void firePropertyChange(int property) {
	super.firePropertyChange(property);
	updatePropertyDependentActions();
}

protected ActionRegistry getActionRegistry() {
	if (actionRegistry == null)
		actionRegistry = new ActionRegistry();
	return actionRegistry;
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
	if (type == ActionRegistry.class)
		return getActionRegistry();
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

/**
 * Sets the site and input for this editor then creates and initializes the actions.
 * Subclasses may override this method, but must call <code>super.init(site, input)
 * </code>.
 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
 */
public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	setSite(site);
	setInput(input);
	getCommandStack().addCommandStackListener(this);
	getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	initializeActionRegistry();
}

/**
 * Initializes the ActionRegistry.  This registry may be used by {@link
 * ActionBarContributor ActionBarContributors} and/or {@link ContextMenuProvider
 * ContextMenuProviders}.
 * <P>This method may be called on Editor creation, or lazily the first time {@link
 * #getActionRegistry()} is called.
 */
protected void initializeActionRegistry() {
	createActions();
	updatePropertyDependentActions();
	updateStackDependentActions();
} 

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
protected void markAsPropertyDependentAction(String id, boolean mark) {
	if (mark) {
		if (!propertyActions.contains(id))
			propertyActions.add(id);
	}
	else
		propertyActions.remove(id);
}

/**
 * Marks or unmarks the given action to be updated on selection changes.
 *
 * @param id the action id
 * @param mark <code>true</code> if the action is selection dependent
 */
protected void markAsSelectionDependentAction(String id, boolean mark) {
	if (mark) {
		if (!selectionActions.contains(id))
			selectionActions.add(id);
	}
	else
		selectionActions.remove(id);
}

/**
 * Marks or unmarks the given action to be updated on stack changes.
 *
 * @param id the action id
 * @param mark <code>true</code> if the action is stack dependent
 */
protected void markAsStackDependentAction(String id, boolean mark) {
	if (mark) {
		if (!stackActions.contains(id))
			stackActions.add(id);
	}
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
 * Sets the ActionRegistry for this EditorPart.
 */
protected void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
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

private void updateAction(String id) {
	if (id == null)
		return;
	ActionRegistry registry = getActionRegistry();
	IAction action = registry.getAction(id);
	if (action instanceof Updatable)
		((Updatable)action).update();
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
