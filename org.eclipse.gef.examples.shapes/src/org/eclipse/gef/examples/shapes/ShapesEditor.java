/*******************************************************************************
 * Copyright (c) 2004 Elias Volanakis.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;

import org.eclipse.gef.examples.shapes.model.ShapesDiagram;
import org.eclipse.gef.examples.shapes.parts.ShapesEditPartFactory;
import org.eclipse.gef.examples.shapes.parts.ShapesTreeEditPartFactory;

/**
 * A graphical editor with flyout palette that can edit .shapes files.
 * The binding between the .shapes file extension and this editor is done in plugin.xml
 * @author Elias Volanakis
 */
public class ShapesEditor extends GraphicalEditorWithFlyoutPalette {

/** This is the root of the editor's model. */
private ShapesDiagram diagram = new ShapesDiagram();
/** OutlinePage instance for this editor. */
private IContentOutlinePage outlinePage;
/** Palette component, holding the tools and shapes. */
private PaletteRoot palette;
/** Cache save-request status. */
private boolean saveAlreadyRequested;
/** KeyHandler with common bindings for both the Outline View and the Editor. */
private KeyHandler sharedKeyHandler;

/** Create a new ShapesEditor instance. This is called by the Workspace. */
public ShapesEditor() {
	setEditDomain(new DefaultEditDomain(this));
}

/**
 * Configure the graphical viewer before it receives contents.
 * <p>This is the place to choose an appropriate RootEditPart and EditPartFactory
 * for your editor. The RootEditPart determines the behavior of the editor's "work-area".
 * For example, GEF includes zoomable and scrollable root edit parts. The EditPartFactory
 * maps model elements to edit parts (controllers).</p>
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
 */
protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	
	GraphicalViewer viewer = getGraphicalViewer();
	viewer.setRootEditPart(new ScalableRootEditPart());
	viewer.setEditPartFactory(new ShapesEditPartFactory());
	viewer.setKeyHandler(
			new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
	
	// configure the context menu provider
	ContextMenuProvider cmProvider =
		new ShapesEditorContextMenuProvider(viewer, getActionRegistry());
	viewer.setContextMenu(cmProvider);
	getSite().registerContextMenu(cmProvider, viewer);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util.EventObject)
 */
public void commandStackChanged(EventObject event) {
	super.commandStackChanged(event);
	if (isDirty() && !saveAlreadyRequested) {
		saveAlreadyRequested = true;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	else {
		saveAlreadyRequested = false;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
}

private void createOutputStream(OutputStream os) 
throws IOException {
	ObjectOutputStream oos = new ObjectOutputStream(os);
	oos.writeObject(getModel());
	oos.close();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#createPaletteViewerProvider()
 */
protected PaletteViewerProvider createPaletteViewerProvider() {
	return new PaletteViewerProvider(getEditDomain()) {
		protected void configurePaletteViewer(PaletteViewer viewer) {
			super.configurePaletteViewer(viewer);
			// create a drag source listener for this palette viewer
			// together with an appropriate transfer drop target listener, this will enable
			// model element creation by dragging a CombinatedTemplateCreationEntries 
			// from the palette into the editor
			// @see ShapesEditor#createTransferDropTargetListener()
			viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
		}
	};
}

/**
 * Create a transfer drop target listener. When using a CombinedTemplateCreationEntry
 * tool in the palette, this will enable model element creation by dragging from the palette.
 * @see #createPaletteViewerProvider()
 */
private TransferDropTargetListener createTransferDropTargetListener() {
	return new TemplateTransferDropTargetListener(getGraphicalViewer()) {
		protected CreationFactory getFactory(Object template) {
			return new SimpleFactory((Class) template);
		}
	};
}

/**
 * Forget the outline page for this editor.
 * Note that this is called from ShapesEditorOutlinePage#dispose, so the outline page
 * is already disposed.
 * @see ShapesEditorOutlinePage#dispose()
 */
private void disposeOutlinePage() {
	outlinePage = null;
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
 */
public void doSave(IProgressMonitor monitor) {
	
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		createOutputStream(out);
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		file.setContents(
			new ByteArrayInputStream(out.toByteArray()), 
			true,  // keep saving, even if IFile is out of sync with the Workspace
			false, // dont keep history
			monitor); // progress monitor
		getCommandStack().markSaveLocation();
	}
	catch (CoreException ce) { 
		ce.printStackTrace();
	}
	catch (IOException ioe) {
		ioe.printStackTrace();
	}	
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
 */
public void doSaveAs() {
	// Show a SaveAs dialog
	Shell shell = getSite().getWorkbenchWindow().getShell();
	SaveAsDialog dialog = new SaveAsDialog(shell);
	dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
	dialog.open();
	
	IPath path = dialog.getResult();	
	if (path != null) {
		// try to save the editor's contents under a different file name
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		try {
			new ProgressMonitorDialog(shell).run(
					false, // don't fork
					false, // not cancelable
					new WorkspaceModifyOperation() { // run this operation
						public void execute(final IProgressMonitor monitor) {
							try {
								ByteArrayOutputStream out = new ByteArrayOutputStream();
								createOutputStream(out);
								file.create(
									new ByteArrayInputStream(out.toByteArray()), // contents
									true, // keep saving, even if IFile is out of sync with the Workspace
									monitor); // progress monitor
							}
							catch (CoreException ce) { ce.printStackTrace(); }
							catch (IOException ioe) { ioe.printStackTrace(); } 
						}
					});
			// set input to the new file
			setInput(new FileEditorInput(file));
			getCommandStack().markSaveLocation();
		}
		catch (InterruptedException ie) {
  			// should not happen, since the monitor dialog is not cancelable
			ie.printStackTrace(); 
		}
		catch (InvocationTargetException ite) { 
			ite.printStackTrace(); 
		}
	} // if
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(java.lang.Class)
 */
public Object getAdapter(Class type) {
	// returns the content outline page for this editor
	if (type == IContentOutlinePage.class) {
		if (outlinePage == null) {
			outlinePage = new ShapesEditorOutlinePage(this, new TreeViewer()); 
		}
		return outlinePage;
	}
	return super.getAdapter(type);
}

/**
 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
 * For example, delete is a common action.
 */
KeyHandler getCommonKeyHandler() {
	if (sharedKeyHandler == null) {
		sharedKeyHandler = new KeyHandler();

		// Add key and action pairs to sharedKeyHandler
		sharedKeyHandler.put(
				KeyStroke.getPressed(SWT.DEL, 127, 0),
				getActionRegistry().getAction(ActionFactory.DELETE.getId()));
	}
	return sharedKeyHandler;
}

private ShapesDiagram getModel() {
	return diagram;
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPalettePreferences()
 */
protected FlyoutPreferences getPalettePreferences() {
	return ShapesEditorPaletteFactory.createPalettePreferences();
}

/* (non-Javadoc)
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
 */
protected PaletteRoot getPaletteRoot() {
	if (palette == null) {
		palette = ShapesEditorPaletteFactory.createPalette();
	}
	return palette;
}

private void handleLoadException(Exception e) {
	System.err.println("** Load failed. Using default model. **");
	e.printStackTrace();
	diagram = new ShapesDiagram();
}

/**
 * Set up the editor's inital content (after creation).
 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
 */
protected void initializeGraphicalViewer() {
	super.initializeGraphicalViewer();
	GraphicalViewer graphicalViewer = getGraphicalViewer();
	graphicalViewer.setContents(getModel()); // set the contents of this editor
	// listen for dropped parts
	graphicalViewer.addDropTargetListener(createTransferDropTargetListener());
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#isDirty()
 */
public boolean isDirty() {
	return getCommandStack().isDirty();
}

/* (non-Javadoc)
 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
 */
public boolean isSaveAsAllowed() {
	return true;
}


/* (non-Javadoc)
 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
 */
protected void setInput(IEditorInput input) {
	super.setInput(input);
	try {
		IFile file = ((IFileEditorInput) input).getFile();
		ObjectInputStream in = new ObjectInputStream(file.getContents());
		diagram = (ShapesDiagram) in.readObject();
		in.close();
		setPartName(file.getName());
	}
	catch (IOException e) { 
		handleLoadException(e); 
	}
	catch (CoreException e) { 
		handleLoadException(e); 
	}
	catch (ClassNotFoundException e) { 
		handleLoadException(e); 
	}
}

/**
 * Creates an outline pagebook for this editor.
 */
public class ShapesEditorOutlinePage extends ContentOutlinePage {
	/** 
	 * Pointer to the editor instance associated with this outline page.
	 * Used for calling back on editor methods.
	 */
	private final ShapesEditor editor;
	/** A pagebook can containt multiple pages (just one in our case). */
	private PageBook pagebook;
	
	/**
	 * Create a new outline page for the shapes editor.
	 * @param editor the editor associated with this outline page (non-null)
	 * @param viewer a viewer (TreeViewer instance) used for this outline page
	 * @throws IllegalArgumentException if editor is null
	 */
	public ShapesEditorOutlinePage(ShapesEditor editor, EditPartViewer viewer) {
		super(viewer);
		if (editor == null) {
			throw new IllegalArgumentException();
		}
		this.editor = editor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		pagebook = new PageBook(parent, SWT.NONE);

		// create outline viewer page
		Control outline = getViewer().createControl(pagebook);
		// configure outline viewer
		getViewer().setEditDomain(editor.getEditDomain());
		getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
		getViewer().setKeyHandler(editor.getCommonKeyHandler());
		// configure & add context menu to viewer
		ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(
				getViewer(), 
				editor.getActionRegistry()); 
		getViewer().setContextMenu(cmProvider);
		getSite().registerContextMenu(
				"org.eclipse.gef.examples.shapes.outline.contextmenu",
				cmProvider,
				getSite().getSelectionProvider());		
		// hook outline viewer
		editor.getSelectionSynchronizer().addViewer(getViewer());
		// initialize outline viewer with model
		getViewer().setContents(editor.getModel());
		// show outline viewer
		pagebook.showPage(outline);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose() {
		// unhook outline viewer
		editor.getSelectionSynchronizer().removeViewer(getViewer());
		// dispose
		super.dispose();
		editor.disposeOutlinePage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() {
		return pagebook;
	}
} // inner class 
}