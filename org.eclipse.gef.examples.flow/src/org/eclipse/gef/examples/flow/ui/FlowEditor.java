package org.eclipse.gef.examples.flow.ui;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.*;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.examples.flow.FlowContextMenuProvider;
import org.eclipse.gef.examples.flow.FlowPlugin;
import org.eclipse.gef.examples.flow.dnd.FlowTemplateTransferDropTargetListener;
import org.eclipse.gef.examples.flow.model.*;
import org.eclipse.gef.examples.flow.parts.ActivityPartFactory;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;

/**
 * 
 * @author hudsonr
 * Created on Jun 27, 2003
 */
public class FlowEditor extends GraphicalEditorWithPalette {

ActivityDiagram diagram;
private PaletteRoot root;
private KeyHandler sharedKeyHandler;

public FlowEditor() {
	DefaultEditDomain defaultEditDomain = new DefaultEditDomain(this);
	defaultEditDomain.setActiveTool(new ConnectionCreationTool());
	setEditDomain(defaultEditDomain);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
 */
protected void createActions() {
	super.createActions();
	ActionRegistry registry = getActionRegistry();
	IAction action;
	
	action = new DirectEditAction((IWorkbenchPart)this);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
}


/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
 */
protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
	getGraphicalViewer().setEditPartFactory(new ActivityPartFactory());
	getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer())
		.setParent(getCommonKeyHandler()));
	
	ContextMenuProvider provider =
		new FlowContextMenuProvider(getGraphicalViewer(), getActionRegistry());
	getGraphicalViewer().setContextMenu(provider);
	getSite().registerContextMenu(
		"org.eclipse.gef.examples.flow.editor.contextmenu",
		provider,
		getGraphicalViewer());
	
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
 */
protected void initializeGraphicalViewer() {
	getGraphicalViewer().setContents(diagram);
	getGraphicalViewer().addDropTargetListener(
		new FlowTemplateTransferDropTargetListener(getGraphicalViewer()));

}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#initializePaletteViewer()
 */
protected void initializePaletteViewer() {
	super.initializePaletteViewer();
	getPaletteViewer().addDragSourceListener(
		new TemplateTransferDragSourceListener(getPaletteViewer()));
}

public void doSave(IProgressMonitor monitor) {
	//$TODO
}

public void doSaveAs() {
	//$TODO
}

protected KeyHandler getCommonKeyHandler(){
	if (sharedKeyHandler == null){
		sharedKeyHandler = new KeyHandler();
		sharedKeyHandler.put(
			KeyStroke.getPressed(SWT.DEL, 127, 0),
			getActionRegistry().getAction(GEFActionConstants.DELETE));
		sharedKeyHandler.put(
			KeyStroke.getPressed(SWT.F2, 0),
			getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
	}
	return sharedKeyHandler;
}

protected PaletteRoot getPaletteRoot() {
	if( root == null ){
		root = FlowPlugin.createPalette();
	}
	return root;
}

public void gotoMarker(IMarker marker) {
	//$TODO
}

public boolean isDirty() {
	//$TODO
	return false;
}

public boolean isSaveAsAllowed() {
	//$TODO
	return false;
}

/**
 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
 */
protected void setInput(IEditorInput input) {
	super.setInput(input);
	diagram = new ActivityDiagram();
	Activity a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;

	a1 = new Activity();
	a1.setName("a1");
	a2 = new Activity();
	a2.setName("a2");
	a3 = new Activity();
	a3.setName("a3");
	a4 = new Activity();
	a4.setName("a4");
	a5 = new Activity();
	a5.setName("a5");
	a6 = new Activity();
	a6.setName("a6");
	a7 = new Activity();
	a7.setName("a7");
	a8 = new Activity();
	a8.setName("a8");
	a9 = new Activity();
	a9.setName("a9");
	a10 = new Activity();
	a10.setName("a10");
	
	diagram.addChild(a1);
	diagram.addChild(a2);
	diagram.addChild(a3);
	diagram.addChild(a4);
	diagram.addChild(a5);
	diagram.addChild(a6);
	diagram.addChild(a7);
	diagram.addChild(a8);
	
	new Transition(a1, a2);
	new Transition(a1, a3);
	new Transition(a2, a4);
	new Transition(a2, a5);
	new Transition(a3, a5);	
	new Transition(a2, a6);
	new Transition(a3, a7);
	new Transition(a3, a8);

}

}
