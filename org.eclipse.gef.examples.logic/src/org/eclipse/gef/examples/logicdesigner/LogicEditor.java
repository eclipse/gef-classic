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
package org.eclipse.gef.examples.logicdesigner;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.*;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;

import org.eclipse.gef.*;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.rulers.ToggleRulerVisibilityAction;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.*;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.*;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;

import org.eclipse.gef.examples.logicdesigner.actions.IncrementDecrementAction;
import org.eclipse.gef.examples.logicdesigner.actions.LogicPasteTemplateAction;
import org.eclipse.gef.examples.logicdesigner.dnd.LogicTemplateTransferDropTargetListener;
import org.eclipse.gef.examples.logicdesigner.dnd.TextTransferDropTargetListener;
import org.eclipse.gef.examples.logicdesigner.edit.GraphicalPartFactory;
import org.eclipse.gef.examples.logicdesigner.edit.TreePartFactory;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicRuler;
import org.eclipse.gef.examples.logicdesigner.palette.LogicPaletteCustomizer;
import org.eclipse.gef.examples.logicdesigner.rulers.LogicRulerProvider;

public class LogicEditor 
	extends GraphicalEditorWithPalette 
{

class OutlinePage
	extends ContentOutlinePage
	implements IAdaptable
{
	
	private PageBook pageBook;
	private Control outline;
	private Canvas overview;
	private IAction showOutlineAction, showOverviewAction;
	static final int ID_OUTLINE  = 0;
	static final int ID_OVERVIEW = 1;
	private Thumbnail thumbnail;
	
	public OutlinePage(EditPartViewer viewer){
		super(viewer);
	}
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		ActionRegistry registry = getActionRegistry();
		IActionBars bars = pageSite.getActionBars();
		String id = IWorkbenchActionConstants.UNDO;
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = IWorkbenchActionConstants.REDO;
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = IWorkbenchActionConstants.DELETE;
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = IncrementDecrementAction.INCREMENT;
		bars.setGlobalActionHandler(id, registry.getAction(id));
		id = IncrementDecrementAction.DECREMENT;
		bars.setGlobalActionHandler(id, registry.getAction(id));
		bars.updateActionBars();
	}

	protected void configureOutlineViewer(){
		getViewer().setEditDomain(getEditDomain());
		getViewer().setEditPartFactory(new TreePartFactory());
		ContextMenuProvider provider = new LogicContextMenuProvider(getViewer(), getActionRegistry());
		getViewer().setContextMenu(provider);
		getSite().registerContextMenu(
			"org.eclipse.gef.examples.logic.outline.contextmenu", //$NON-NLS-1$
			provider, getSite().getSelectionProvider());
		getViewer().setKeyHandler(getCommonKeyHandler());
		getViewer().addDropTargetListener(
			new LogicTemplateTransferDropTargetListener(getViewer()));
		IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
		showOutlineAction = new Action() {
			public void run() {
				showPage(ID_OUTLINE);
			}
		};
		showOutlineAction.setImageDescriptor(ImageDescriptor.createFromFile(
								LogicPlugin.class,"icons/outline.gif")); //$NON-NLS-1$
		tbm.add(showOutlineAction);
		showOverviewAction = new Action() {
			public void run() {
				showPage(ID_OVERVIEW);
			}
		};
		showOverviewAction.setImageDescriptor(ImageDescriptor.createFromFile(
								LogicPlugin.class,"icons/overview.gif")); //$NON-NLS-1$
		tbm.add(showOverviewAction);
		showPage(ID_OUTLINE);
	}

	public void createControl(Composite parent){
		pageBook = new PageBook(parent, SWT.NONE);
		outline = getViewer().createControl(pageBook);
		overview = new Canvas(pageBook, SWT.NONE);
		pageBook.showPage(outline);
		configureOutlineViewer();
		hookOutlineViewer();
		initializeOutlineViewer();
	}
	
	public void dispose(){
		unhookOutlineViewer();
		if (thumbnail != null)
			thumbnail.deactivate();
		super.dispose();
		LogicEditor.this.outlinePage = null;
	}
	
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		return null;
	}

	public Control getControl() {
		return pageBook;
	}

	protected void hookOutlineViewer(){
		getSelectionSynchronizer().addViewer(getViewer());
	}

	protected void initializeOutlineViewer(){
		setContents(getLogicDiagram());
	}
	
	protected void initializeOverview() {
		LightweightSystem lws = new LightweightSystem(overview);
		RootEditPart rep = getGraphicalViewer().getRootEditPart();
		if (rep instanceof ScalableFreeformRootEditPart) {
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart)rep;
			thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
			thumbnail.setBorder(new MarginBorder(3));
			thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);
		}
	}
	
	public void setContents(Object contents) {
		getViewer().setContents(contents);
	}
	
	protected void showPage(int id) {
		if (id == ID_OUTLINE) {
			showOutlineAction.setChecked(true);
			showOverviewAction.setChecked(false);
			pageBook.showPage(outline);
			if (thumbnail != null)
				thumbnail.setVisible(false);
		} else if (id == ID_OVERVIEW) {
			if (thumbnail == null)
				initializeOverview();
			showOutlineAction.setChecked(false);
			showOverviewAction.setChecked(true);
			pageBook.showPage(overview);
			thumbnail.setVisible(true);
		}
	}
	
	protected void unhookOutlineViewer(){
		getSelectionSynchronizer().removeViewer(getViewer());
	}
}

private KeyHandler sharedKeyHandler;
private PaletteRoot root;
private OutlinePage outlinePage;
private boolean editorSaving = false;

// This class listens to changes to the file system in the workspace, and 
// makes changes accordingly.
// 1) An open, saved file gets deleted -> close the editor
// 2) An open file gets renamed or moved -> change the editor's input accordingly	
class ResourceTracker
	implements IResourceChangeListener, IResourceDeltaVisitor
{
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		try {
			if (delta != null)
				delta.accept(this);
		} 
		catch (CoreException exception) {
			// What should be done here?
		}
	}	
	public boolean visit(IResourceDelta delta) { 
		if (delta == null || !delta.getResource().equals(((FileEditorInput)getEditorInput()).getFile()))
			return true;
			
		if (delta.getKind() == IResourceDelta.REMOVED) {
			Display display = getSite().getShell().getDisplay();
			if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) { // if the file was deleted
				// NOTE: The case where an open, unsaved file is deleted is being handled by the 
				// PartListener added to the Workbench in the initialize() method.
				display.asyncExec(new Runnable() {
					public void run() {
						if (!isDirty()) 
							closeEditor(false); 
					}
				});
			} else { // else if it was moved or renamed
				final IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getMovedToPath());
				display.asyncExec(new Runnable() {
					public void run() {
						superSetInput(new FileEditorInput(newFile));
					}
				});
			}
		} else if (delta.getKind() == IResourceDelta.CHANGED) {
			if (!editorSaving) {
				// the file was overwritten somehow (could have been replaced by another 
				// version in the respository)
				final IFile newFile = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(delta.getFullPath());
				Display display = getSite().getShell().getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						setInput(new FileEditorInput(newFile));
						getCommandStack().flush();
					}
				});
			}
		}
		return false; 
	}
}

private LogicDiagram logicDiagram = new LogicDiagram();
private boolean savePreviouslyNeeded = false;
private ResourceTracker resourceListener = new ResourceTracker();
private RulerComposite rulerComp;

private IPartListener partListener = new IPartListener() {
	// If an open, unsaved file was deleted, query the user to either do a "Save As"
	// or close the editor.
	public void partActivated(IWorkbenchPart part) {
		if (part != LogicEditor.this)
			return;
		if (!((FileEditorInput)getEditorInput()).getFile().exists()) {
			Shell shell = getSite().getShell();
			String title = LogicMessages.GraphicalEditor_FILE_DELETED_TITLE_UI;
			String message = LogicMessages.GraphicalEditor_FILE_DELETED_WITHOUT_SAVE_INFO;
			String[] buttons = { 	LogicMessages.GraphicalEditor_SAVE_BUTTON_UI, 
						   			LogicMessages.GraphicalEditor_CLOSE_BUTTON_UI };
			MessageDialog dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, buttons, 0);			
			if (dialog.open() == 0) {
				if (!performSaveAs())
					partActivated(part);
			} 
			else {
				closeEditor(false);
			}
		}
	}
	public void partBroughtToTop(IWorkbenchPart part) {}
	public void partClosed(IWorkbenchPart part) {}
	public void partDeactivated(IWorkbenchPart part) {}
	public void partOpened(IWorkbenchPart part) {}
};

protected static final String PALETTE_SIZE = "Palette Size"; //$NON-NLS-1$
protected static final int DEFAULT_PALETTE_SIZE = 130;
	
public LogicEditor() {
	setEditDomain(new DefaultEditDomain(this));
}

protected void closeEditor(boolean save) {
	getSite().getPage().closeEditor(LogicEditor.this, save);
}

public void commandStackChanged(EventObject event) {
	if (isDirty()){
		if (!savePreviouslyNeeded()) {
			setSavePreviouslyNeeded(true);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}
	else {
		setSavePreviouslyNeeded(false);
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	super.commandStackChanged(event);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#configurePaletteViewer()
 */
protected void configurePaletteViewer() {
	super.configurePaletteViewer();
	PaletteViewer viewer = getPaletteViewer();
	ContextMenuProvider provider = new PaletteContextMenuProvider(viewer);
	getPaletteViewer().setContextMenu(provider);
	viewer.setCustomizer(new LogicPaletteCustomizer());
}


protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer)getGraphicalViewer();

	ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();

	List zoomLevels = new ArrayList(3);
	zoomLevels.add(ZoomManager.FIT_ALL);
	zoomLevels.add(ZoomManager.FIT_WIDTH);
	zoomLevels.add(ZoomManager.FIT_HEIGHT);
	root.getZoomManager().setZoomLevelContributions(zoomLevels);

	IAction zoomIn = new ZoomInAction(root.getZoomManager());
	IAction zoomOut = new ZoomOutAction(root.getZoomManager());
	getActionRegistry().registerAction(zoomIn);
	getActionRegistry().registerAction(zoomOut);
	getSite().getKeyBindingService().registerAction(zoomIn);
	getSite().getKeyBindingService().registerAction(zoomOut);

	viewer.setRootEditPart(root);

	viewer.setEditPartFactory(new GraphicalPartFactory());
	ContextMenuProvider provider = new LogicContextMenuProvider(viewer, getActionRegistry());
	viewer.setContextMenu(provider);
	getSite().registerContextMenu("org.eclipse.gef.examples.logic.editor.contextmenu", //$NON-NLS-1$
		provider, viewer);
	viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
		.setParent(getCommonKeyHandler()));

	loadRulers();
	IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
	getActionRegistry().registerAction(showRulers);
	getSite().getKeyBindingService().registerAction(showRulers);
}

protected void createOutputStream(OutputStream os)throws IOException {
	ObjectOutputStream out = new ObjectOutputStream(os);
	out.writeObject(getLogicDiagram());
	out.close();	
}

protected void loadRulers(){
	LogicRuler ruler = getLogicDiagram().getRuler(PositionConstants.WEST);
	RulerProvider provider = null;
	if (ruler != null) {
		provider = new LogicRulerProvider(ruler);
	}
	getGraphicalViewer().setProperty(RulerProvider.VERTICAL, provider);
	ruler = getLogicDiagram().getRuler(PositionConstants.NORTH);
	provider = null;
	if (ruler != null) {
		provider = new LogicRulerProvider(ruler);
	}
	getGraphicalViewer().setProperty(RulerProvider.HORIZONTAL, provider);
	getGraphicalViewer().setProperty(RulerProvider.RULER_VISIBILITY, 
			new Boolean(getLogicDiagram().getRulerVisibility()));
}

public void dispose() {
	CopyTemplateAction copy = (CopyTemplateAction)getActionRegistry().getAction(GEFActionConstants.COPY);
	getPaletteViewer().removeSelectionChangedListener(copy);
	getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
	partListener = null;
	((FileEditorInput)getEditorInput()).getFile().getWorkspace().removeResourceChangeListener(resourceListener);
	super.dispose();
}

public void doSave(IProgressMonitor progressMonitor) {
	try {
		editorSaving = true;
		getLogicDiagram().setRulerVisibility(((Boolean)getGraphicalViewer()
				.getProperty(RulerProvider.RULER_VISIBILITY)).booleanValue());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		createOutputStream(out);
		IFile file = ((IFileEditorInput)getEditorInput()).getFile();
		file.setContents(new ByteArrayInputStream(out.toByteArray()), 
						true, false, progressMonitor);
		out.close();
		getCommandStack().markSaveLocation();
	} 
	catch (Exception e) {
		e.printStackTrace();
	} finally {
		editorSaving = false;
	}
}

public void doSaveAs() {
	performSaveAs();
}

public Object getAdapter(Class type){
	if (type == CommandStackInspectorPage.class)
		return new CommandStackInspectorPage(getCommandStack());
	if (type == IContentOutlinePage.class) {
		outlinePage = new OutlinePage(new TreeViewer());
		return outlinePage;
	}
//	if (type == PaletteViewPage.class) {
//		return new PaletteViewPage() {
//			public void createControl(Composite parent) {
//				createPaletteViewer(parent);
//			}
//			public Object getAdapter(Class type) {
//				if (type == ZoomManager.class)
//					return getGraphicalViewer().getProperty(ZoomManager.class.toString());
//				return null;
//			}
//			public Control getControl() {
//				return getPaletteViewer().getControl();
//			}
//			public void setFocus() {
//				getControl().setFocus();
//			}
//		};
//	}
	if (type == ZoomManager.class)
		return getGraphicalViewer().getProperty(ZoomManager.class.toString());

	return super.getAdapter(type);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getInitialPaletteSize()
 */
protected int getInitialPaletteSize() {
	return LogicPlugin.getDefault().getPreferenceStore().getInt(PALETTE_SIZE);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#handlePaletteResized(int)
 */
protected void handlePaletteResized(int newSize) {
	LogicPlugin.getDefault().getPreferenceStore().setValue(PALETTE_SIZE, newSize);
}

/**
 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
 * For example, delete is a common action.
 */
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

protected LogicDiagram getLogicDiagram() {
	return logicDiagram;
}

protected PaletteRoot getPaletteRoot() {
	if( root == null ){
		root = LogicPlugin.createPalette();
	}
	return root;
}

public void gotoMarker(IMarker marker) {}

protected void hookPaletteViewer() {
	super.hookPaletteViewer();
	final CopyTemplateAction copy = 
			(CopyTemplateAction)getActionRegistry().getAction(GEFActionConstants.COPY);
	getPaletteViewer().addSelectionChangedListener(copy);
	getPaletteViewer().getContextMenu().addMenuListener(new IMenuListener() {
		public void menuAboutToShow(IMenuManager manager) {
			manager.appendToGroup(GEFActionConstants.GROUP_COPY, copy);
		}
	});
}

protected void initializeGraphicalViewer() {
	getGraphicalViewer().setContents(getLogicDiagram());
	
	getGraphicalViewer().addDropTargetListener(
		new LogicTemplateTransferDropTargetListener(getGraphicalViewer()));
	getGraphicalViewer().addDropTargetListener(
		new TextTransferDropTargetListener(getGraphicalViewer(), TextTransfer.getInstance()));
}

protected void initializePaletteViewer() {
	super.initializePaletteViewer();
	getPaletteViewer().addDragSourceListener(
		new TemplateTransferDragSourceListener(getPaletteViewer()));
	LogicPlugin.getDefault().getPreferenceStore().setDefault(
				PALETTE_SIZE, DEFAULT_PALETTE_SIZE);
}

protected void createActions() {
	super.createActions();
	ActionRegistry registry = getActionRegistry();
	IAction action;
	
	action = new CopyTemplateAction(this);
	registry.registerAction(action);

	action = new LogicPasteTemplateAction(this);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new IncrementDecrementAction(this, true);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new IncrementDecrementAction(this, false);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new DirectEditAction((IWorkbenchPart)this);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());

	action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
}

public void createPartControl(Composite parent) {
	createGraphicalViewer(parent);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createGraphicalViewer(org.eclipse.swt.widgets.Composite)
 */
protected void createGraphicalViewer(Composite parent) {
	rulerComp = new RulerComposite(parent, SWT.NONE);
	super.createGraphicalViewer(rulerComp);
	rulerComp.setGraphicalViewer(getGraphicalViewer());
}

protected FigureCanvas getEditor(){
	return (FigureCanvas)getGraphicalViewer().getControl();
}

public boolean isDirty() {
	return isSaveOnCloseNeeded();
}

public boolean isSaveAsAllowed() {
	return true;
}

public boolean isSaveOnCloseNeeded() {
	return getCommandStack().isDirty();
}

protected boolean performSaveAs() {
	SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
	dialog.setOriginalFile(((IFileEditorInput)getEditorInput()).getFile());
	dialog.open();
	IPath path= dialog.getResult();
	
	if (path == null)
		return false;
	
	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	final IFile file= workspace.getRoot().getFile(path);
	
	if (!file.exists()) {
		WorkspaceModifyOperation op= new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor) {
				try {
					getLogicDiagram().setRulerVisibility(((Boolean)getGraphicalViewer()
							.getProperty(RulerProvider.RULER_VISIBILITY)).booleanValue());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					createOutputStream(out);
					file.create(new ByteArrayInputStream(out.toByteArray()), true, monitor);
					out.close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		try {
			new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell()).run(false, true, op);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	try {
		superSetInput(new FileEditorInput(file));
		getCommandStack().markSaveLocation();
	} 
	catch (Exception e) {
		e.printStackTrace();
	} 
	return true;
}

private boolean savePreviouslyNeeded() {
	return savePreviouslyNeeded;
}

public void setInput(IEditorInput input) {
	superSetInput(input);

	IFile file = ((IFileEditorInput)input).getFile();
	try {
		InputStream is = file.getContents(false);
		ObjectInputStream ois = new ObjectInputStream(is);
		setLogicDiagram((LogicDiagram)ois.readObject());
		ois.close();
	}
	catch (Exception e) {
		//This is just an example.  All exceptions caught here.
		e.printStackTrace();
	}
	
	if (!editorSaving) {
		if (getGraphicalViewer() != null) {
			getGraphicalViewer().setContents(getLogicDiagram());
			loadRulers();
		}
		if (outlinePage != null) {
			outlinePage.setContents(getLogicDiagram());
		}
	}
}

public void setLogicDiagram(LogicDiagram diagram) {
	logicDiagram = diagram;
}

private void setSavePreviouslyNeeded(boolean value) {
	savePreviouslyNeeded = value;
}

protected void superSetInput(IEditorInput input) {
	// The workspace never changes for an editor.  So, removing and re-adding the 
	// resourceListener is not necessary.  But it is being done here for the sake
	// of proper implementation.  Plus, the resourceListener needs to be added 
	// to the workspace the first time around.
	if(getEditorInput() != null) {
		IFile file = ((FileEditorInput)getEditorInput()).getFile();
		file.getWorkspace().removeResourceChangeListener(resourceListener);
	}
	
	super.setInput(input);
	
	if(getEditorInput() != null) {
		IFile file = ((FileEditorInput)getEditorInput()).getFile();
		file.getWorkspace().addResourceChangeListener(resourceListener);
		setTitle(file.getName());
	}
}

protected void setSite(IWorkbenchPartSite site){
	super.setSite(site);
	getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
}

}