/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import java.util.*;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.*;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.PropertySheetPage;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.ui.actions.*;

/**
 * This class serves as a quick starting point for clients who are new to GEF. It will
 * create an Editor containing a single GraphicalViewer as its control.
 * <P>
 * <EM>IMPORTANT</EM>This class should only be used as a reference for creating your own
 * EditorPart implementation. This class will not suit everyone's needs, and may change in
 * the future. Clients may copy the implementation.
 * @author hudsonr
 */
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

/**
 * Constructs the editor part
 */
public GraphicalEditor() { }

/**
 * When the command stack changes, the actions interested in the command stack are
 * updated.
 * @param event the change event
 */
public void commandStackChanged(EventObject event) {
	updateActions(stackActions);
}

/**
 * Called to configure the graphical viewer before it receives its contents.  This is
 * where the root editpart should be configured. Subclasses should extend or override this
 * method as needed.
 */
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
	getStackActions().add(action.getId());
	
	action = new RedoAction(this);
	registry.registerAction(action);
	getStackActions().add(action.getId());
	
	action = new SelectAllAction(this);
	registry.registerAction(action);
	
	action = new DeleteAction((IWorkbenchPart)this);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	
	action = new SaveAction(this);
	registry.registerAction(action);
	getPropertyActions().add(action.getId());
	
	action = new PrintAction(this);
	registry.registerAction(action);
}

/**
 * Creates the GraphicalViewer on the specified <code>Composite</code>.
 * @param parent the parent composite
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
 * <P>WARNING: This method may or may not be called by the workbench prior to {@link
 * #dispose()}.
 * @param parent the parent composite
 */
public void createPartControl(Composite parent) {
	createGraphicalViewer(parent);
}

/**
 * @see org.eclipse.ui.IWorkbenchPart#dispose()
 */
public void dispose() {
	getCommandStack().removeCommandStackListener(this);
	getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
	getEditDomain().setActiveTool(null);
	getActionRegistry().dispose();
	super.dispose();
}

/**
 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
 */
protected void firePropertyChange(int property) {
	super.firePropertyChange(property);
	updateActions(propertyActions);
}

/**
 * Lazily creates and returns the action registry.
 * @return the action registry
 */
protected ActionRegistry getActionRegistry() {
	if (actionRegistry == null)
		actionRegistry = new ActionRegistry();
	return actionRegistry;
}

/** 
 * Returns the adapter for the specified key.
 * 
 * <P><EM>IMPORTANT</EM> certain requests, such as the property sheet, may be made before
 * or after {@link #createPartControl(Composite)} is called. The order is unspecified by
 * the Workbench.
 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class type) {
	if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
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
	if (type == EditPart.class && getGraphicalViewer() != null)
		return getGraphicalViewer().getRootEditPart();
	if (type == IFigure.class && getGraphicalViewer() != null)
		return ((GraphicalEditPart)getGraphicalViewer().getRootEditPart()).getFigure();
	return super.getAdapter(type);
}

/**
 * Returns the command stack.
 * @return the command stack
 */
protected CommandStack getCommandStack() {
	return getEditDomain().getCommandStack();
}

/**
 * Returns the edit domain.
 * @return the edit domain
 */
protected DefaultEditDomain getEditDomain() {
	return editDomain;
}

/**
 * Returns the graphical viewer.
 * @return the graphical viewer
 */
protected GraphicalViewer getGraphicalViewer() {
	return graphicalViewer;
}

/**
 * Returns the list of {@link IAction IActions} dependant on property changes in the
 * Editor.  These actions should implement the {@link UpdateAction} interface so that they
 * can be updated in response to property changes.  An example is the "Save" action.
 * @return the list of property-dependant actions
 */
protected List getPropertyActions() {
	return propertyActions;
}

/**
 * Returns the list of {@link IAction IActions} dependant on changes in the workbench's
 * {@link ISelectionService}. These actions should implement the {@link UpdateAction}
 * interface so that they can be updated in response to selection changes.  An example is
 * the Delete action.
 * @return the list of selection-dependant actions
 */
protected List getSelectionActions() {
	return selectionActions;
}

/**
 * Returns the selection syncronizer object. The synchronizer can be used to sync the
 * selection of 2 or more EditPartViewers.
 * @return the syncrhonizer
 */
protected SelectionSynchronizer getSelectionSynchronizer() {
	if (synchronizer == null)
		synchronizer = new SelectionSynchronizer();
	return synchronizer;
}

/**
 * Returns the list of {@link IAction IActions} dependant on the CommmandStack's state. 
 * These actions should implement the {@link UpdateAction} interface so that they can be
 * updated in response to command stack changes.  An example is the "undo" action.
 * @return the list of stack-dependant actions
 */
protected List getStackActions() {
	return stackActions;
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
 * Subclasses may extend this method, but should always call <code>super.init(site, input)
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
	updateActions(propertyActions);
	updateActions(stackActions);
} 

/** 
 * Override to set the contents of the GraphicalViewer after it has been created.
 * @see #createGraphicalViewer(Composite)
 */
protected abstract void initializeGraphicalViewer();

/**
 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
 */
public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	// If not the active editor, ignore selection changed.
	if (this.equals(getSite().getPage().getActiveEditor()))
		updateActions(selectionActions);
}

/**
 * Sets the ActionRegistry for this EditorPart.
 * @param registry the registry
 */
protected void setActionRegistry(ActionRegistry registry) {
	actionRegistry = registry;
} 

/**
 * Sets the EditDomain for this EditorPart.
 * @param ed the domain
 */
protected void setEditDomain(DefaultEditDomain ed) {
	this.editDomain = ed;
}

/**
 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
 */
public void setFocus() {
	getGraphicalViewer().getControl().setFocus();
}

/**
 * Sets the graphicalViewer for this EditorPart.
 * @param viewer the graphical viewer
 */
protected void setGraphicalViewer(GraphicalViewer viewer) {
	getEditDomain().addViewer(viewer);
	this.graphicalViewer = viewer;
}

/**
 * A convenience method for updating a set of actions defined by the given List of action
 * IDs. The actions are found by looking up the ID in the {@link #getActionRegistry()
 * action registry}. If the corresponding action is an {@link UpdateAction}, it will have
 * its <code>update()</code> method called.
 * @param actionIds the list of IDs to update
 */
protected void updateActions(List actionIds) {
	ActionRegistry registry = getActionRegistry();
	Iterator iter = actionIds.iterator();
	while (iter.hasNext()) {
		IAction action = registry.getAction(iter.next());
		if (action instanceof UpdateAction)
			((UpdateAction)action).update();
	}
}

}
