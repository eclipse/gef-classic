package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.properties.PropertySheetPage;

import org.eclipse.draw2d.ColorConstants;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.*;

public abstract class GraphicalEditor
	extends EditorPart
{

private DefaultEditDomain editDomain;
private GraphicalViewer graphicalViewer;
private ActionRegistry actionRegistry;
private SelectionSynchronizer synchronizer;

public GraphicalEditor(){}

protected void configureGraphicalViewer(){
	getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);
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
 * initializes the ActionRegistry.  Subclasses may extend this method to register additional
 * Actions.  This registry may be used by {@link ActionBarContributor ActionBarContributors}
 * and/or {@link ContextMenuProvider ContextMenuProviders}.
 * <P>This method may be called on Editor creation, or lazily
 * the first time {@link #getActionRegistry()} is called.
 */
protected void initializeActionRegistry() {
	getActionRegistry().registerAction(new UndoAction(this));
	getActionRegistry().registerAction(new RedoAction(this));
	getActionRegistry().registerAction(new DeleteAction(this));
	getActionRegistry().registerAction(new SaveAction(this));
}

/**
 * Override to set the contents of the GraphicalViewer after it has been created.
 * @see #createGraphicalViewer(Composite)
 */
abstract protected void initializeGraphicalViewer();

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

protected void setSite(org.eclipse.ui.IWorkbenchPartSite site){
	super.setSite(site);
	initializeActionRegistry();
}

}
