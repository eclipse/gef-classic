/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.text.FlowFigure;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.TreeViewer;

import org.eclipse.gef.examples.text.actions.StyleAction;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.edit.BlockTextualPart;
import org.eclipse.gef.examples.text.edit.ContainerTreePart;
import org.eclipse.gef.examples.text.edit.DocumentPart;
import org.eclipse.gef.examples.text.edit.ImportsPart;
import org.eclipse.gef.examples.text.edit.InlineTextualPart;
import org.eclipse.gef.examples.text.edit.TextFlowPart;
import org.eclipse.gef.examples.text.edit.TextLayoutPart;
import org.eclipse.gef.examples.text.edit.TextRunTreePart;
import org.eclipse.gef.examples.text.model.Block;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.InlineContainer;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.TextTool;

/**
 * @since 3.1
 */
public class TextEditor extends GraphicalEditor {

private Container doc;
private StyleService styleService = new StyleService();

class TextOutlinePage extends ContentOutlinePage {

	public TextOutlinePage(EditPartViewer viewer) {
		super(new TreeViewer());
		getEditDomain().addViewer(getViewer());
		getViewer().setEditPartFactory(new EditPartFactory() {
			public EditPart createEditPart(EditPart context, Object model) {
				if (model instanceof Container)
					return new ContainerTreePart(model);
				return new TextRunTreePart(model);
			}
		});
	}
	
	public void createControl(Composite parent) {
		super.createControl(parent);
		getViewer().setContents(doc);
	}
}

public void commandStackChanged(EventObject event) {
	firePropertyChange(PROP_DIRTY);
	super.commandStackChanged(event);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
 */
protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	getEditDomain().setDefaultTool(
			new TextTool((GraphicalTextViewer)getGraphicalViewer(), styleService));
	getEditDomain().loadDefaultTool();

	getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
	((FigureCanvas)getGraphicalViewer().getControl()).getViewport()
			.setContentsTracksWidth(true);
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
 */
protected void createActions() {
	super.createActions();
	IKeyBindingService service = getSite().getKeyBindingService();
	ActionRegistry registry = getActionRegistry();
	IAction action;
	
	action = new StyleAction(this, GEFActionConstants.STYLE_BOLD);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	service.registerAction(action);

	action = new StyleAction(this, GEFActionConstants.STYLE_ITALIC);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	service.registerAction(action);

	action = new StyleAction(this, GEFActionConstants.STYLE_UNDERLINE);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	service.registerAction(action);
	
	action = new StyleAction(this, GEFActionConstants.BLOCK_ALIGN_LEFT);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	
	action = new StyleAction(this, GEFActionConstants.BLOCK_ALIGN_CENTER);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
	
	action = new StyleAction(this, GEFActionConstants.BLOCK_ALIGN_RIGHT);
	registry.registerAction(action);
	getSelectionActions().add(action.getId());
}

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createGraphicalViewer(org.eclipse.swt.widgets.Composite)
 */
protected void createGraphicalViewer(Composite parent) {
	GraphicalViewer viewer = new GraphicalTextViewer();
	viewer.createControl(parent);
	setGraphicalViewer(viewer);
	configureGraphicalViewer();
	hookGraphicalViewer();
	initializeGraphicalViewer();
}

public Object getAdapter(Class type) {
	if (type == IContentOutlinePage.class)
		return createOutlinePage();
	if (type == StyleService.class)
		return styleService;
	return super.getAdapter(type);
}

/**
 * @since 3.1
 * @return
 */
private IContentOutlinePage createOutlinePage() {
	return new TextOutlinePage(null);
}

/**
 * @see GraphicalEditor#initializeGraphicalViewer()
 */
protected void initializeGraphicalViewer() {
	getGraphicalViewer().setEditPartFactory(new EditPartFactory() {
		public EditPart createEditPart(EditPart context, Object model) {
			if (model instanceof Container) {
				switch (((Container)model).getType()) {
					case Container.TYPE_ROOT:
						return new DocumentPart(model);
					case Container.TYPE_IMPORT_DECLARATIONS:
						return new ImportsPart(model);
					
					case Container.TYPE_COMMENT:
					case Container.TYPE_PARAGRAPH:
						return new BlockTextualPart(model);
					case Container.TYPE_INLINE:
						return new InlineTextualPart(model);
					default:
						throw new RuntimeException("unknown model type");
				}
			} else if (model instanceof TextRun) {
				if (((GraphicalEditPart)context).getFigure() instanceof FlowFigure)
					return new TextFlowPart(model);
				return new TextLayoutPart(model);
			}
			throw new RuntimeException("unexpected model object");
		}
	});

	getGraphicalViewer().setContents(doc);
}

/**
 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
 */
public void doSave(IProgressMonitor monitor) {
	try {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(outputStream);
		objStream.writeObject(doc);
		objStream.close();	
		IFile file = ((IFileEditorInput)getEditorInput()).getFile();
		file.setContents(new ByteArrayInputStream(outputStream.toByteArray()), 
						true, false, monitor);
		outputStream.close();
		getCommandStack().markSaveLocation();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

/**
 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
 */
public void doSaveAs() {}

/**
 * @see GraphicalEditor#init(IEditorSite, IEditorInput)
 */
public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	setEditDomain(new DefaultEditDomain(this));
	getCommandStack().addCommandStackEventListener(new CommandStackEventListener() {
		public void stackChanged(CommandStackEvent event) {
			TextCommand command = (TextCommand)event.getCommand();
			if (command != null) {
				GraphicalTextViewer textViewer = (GraphicalTextViewer)getGraphicalViewer();
				if (event.getDetail() == CommandStack.POST_EXECUTE)
					textViewer.setSelectionRange(command
							.getExecuteSelectionRange(textViewer));
				else if (event.getDetail() == CommandStack.POST_REDO)
					textViewer.setSelectionRange(command
							.getRedoSelectionRange(textViewer));
				else if (event.getDetail() == CommandStack.POST_UNDO)
						textViewer.setSelectionRange(command
								.getUndoSelectionRange(textViewer));
			}
		}
	}); 
	
	super.init(site, input);

	site.getKeyBindingService().setScopes(
			new String[] {GEFActionConstants.CONTEXT_TEXT});
	site.getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(),
			getActionRegistry().getAction(ActionFactory.UNDO.getId()));
	site.getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(),
			getActionRegistry().getAction(ActionFactory.REDO.getId()));
}

/**
 * @see org.eclipse.ui.ISaveablePart#isDirty()
 */
public boolean isDirty() {
	return getCommandStack().isDirty();
}

/**
 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
 */
public boolean isSaveAsAllowed() {
	return false;
}

protected void setInput(IEditorInput input) {
	super.setInput(input);
	
	IFile file = ((IFileEditorInput)input).getFile();
	try {
		InputStream is = file.getContents(false);
		ObjectInputStream ois = new ObjectInputStream(is);
		doc = (Container)ois.readObject();
		ois.close();
	} catch (EOFException eofe) {
		// file was empty (as in the case of a new file); do nothing
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	if (doc == null) {
		doc = new Block(Container.TYPE_ROOT);
		Container block = new Block(Container.TYPE_COMMENT);
		block.add(new TextRun("Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and " +
				"the accompanying materials are made available under the terms of the Common Public " +
				"License v1.0 which accompanies this distribution, and is available at " +
				"http://www.eclipse.org/legal/cpl-v10.html\r\n" + 
				"Contributors: IBM Corporation - initial API and implementation"));
		Container inline = new InlineContainer(Container.TYPE_INLINE);
		inline.getStyle().setBold(true);
		inline.add(new TextRun("ABC def GHI jkl MNO pqr STU vwxyz"));
		block.add(inline);
		doc.add(block);
		doc.add(new TextRun("Paragraph 1"));
	}
	
	setPartName(file.getName());
}

}