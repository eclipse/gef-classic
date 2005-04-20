/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import org.eclipse.swt.SWT;
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
import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
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

import org.eclipse.gef.examples.text.actions.BooleanStyleAction;
import org.eclipse.gef.examples.text.actions.MultiStyleAction;
import org.eclipse.gef.examples.text.actions.StyleService;
import org.eclipse.gef.examples.text.actions.TextActionConstants;
import org.eclipse.gef.examples.text.edit.BlockTextualPart;
import org.eclipse.gef.examples.text.edit.ContainerTreePart;
import org.eclipse.gef.examples.text.edit.DocumentPart;
import org.eclipse.gef.examples.text.edit.ImportPart;
import org.eclipse.gef.examples.text.edit.ImportsPart;
import org.eclipse.gef.examples.text.edit.InlineTextualPart;
import org.eclipse.gef.examples.text.edit.TextFlowPart;
import org.eclipse.gef.examples.text.edit.TextRunTreePart;
import org.eclipse.gef.examples.text.model.Block;
import org.eclipse.gef.examples.text.model.CanvasStyle;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Style;
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
	doc.getStyle().setParentStyle(new CanvasStyle(getGraphicalViewer().getControl()));

	//getGraphicalViewer().getControl().setFont(new Font(Display.getCurrent(), "Tahoma", 11, 0));
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
	
	action = new BooleanStyleAction(styleService, TextActionConstants.STYLE_BOLD, 
			Style.PROPERTY_BOLD);
	registry.registerAction(action);
	service.registerAction(action);

	action = new BooleanStyleAction(styleService, TextActionConstants.STYLE_ITALIC, 
			Style.PROPERTY_ITALIC);
	registry.registerAction(action);
	service.registerAction(action);

	action = new BooleanStyleAction(styleService, TextActionConstants.STYLE_UNDERLINE, 
			Style.PROPERTY_UNDERLINE);
	registry.registerAction(action);
	service.registerAction(action);
	
	action = new MultiStyleAction(styleService, TextActionConstants.BLOCK_ALIGN_LEFT, 
			Style.PROPERTY_ALIGNMENT, new Integer(PositionConstants.LEFT));
	registry.registerAction(action);
	
	action = new MultiStyleAction(styleService, TextActionConstants.BLOCK_ALIGN_CENTER,
			Style.PROPERTY_ALIGNMENT, new Integer(PositionConstants.CENTER));
	registry.registerAction(action);
	
	action = new MultiStyleAction(styleService, TextActionConstants.BLOCK_ALIGN_RIGHT,
			Style.PROPERTY_ALIGNMENT, new Integer(PositionConstants.RIGHT));
	registry.registerAction(action);
	
	action = new MultiStyleAction(styleService, TextActionConstants.BLOCK_LTR,
			Style.PROPERTY_ORIENTATION, new Integer(SWT.LEFT_TO_RIGHT));
	registry.registerAction(action);
	
	action = new MultiStyleAction(styleService, TextActionConstants.BLOCK_RTL,
			Style.PROPERTY_ORIENTATION, new Integer(SWT.RIGHT_TO_LEFT));
	registry.registerAction(action);
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
				switch (((TextRun)model).getType()) {
					case TextRun.TYPE_IMPORT:
						return new ImportPart(model);
					default:
						return new TextFlowPart(model);
				}
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
		Container preface = new Block(Container.TYPE_PARAGRAPH);
		preface.add(new TextRun("package foo.bar;"));
		doc.add(preface);
		Container imports = new Block(Container.TYPE_IMPORT_DECLARATIONS);
		doc.add(imports);
		imports.add(new TextRun("org.eclipse.gef", TextRun.TYPE_IMPORT));
		imports.add(new TextRun("org.eclipse.draw2d", TextRun.TYPE_IMPORT));
		//for (int i = 0; i < 400; i++) {
			Container block = new Block(Container.TYPE_COMMENT);
			block.add(new TextRun("Copyright (c) 2005 IBM Corporation and others. All rights reserved. This program and " +
					"the accompanying materials are made available under the terms of the Eclipse Public " +
					"License v1.0 which accompanies this distribution, and is available at " +
					"http://www.eclipse.org/legal/epl-v10.html\r\n" + 
					"Contributors: IBM Corporation - initial API and implementation"));
			doc.add(block);
			
			Container code = new Block(Container.TYPE_PARAGRAPH);
			code.getStyle().setFontFamily("Courier New");
			doc.add(code);
			code.add(new TextRun("public void countToANumber(int limit) {\n" +
					"    for (int i = 0; i < limit; i++)\n" +
					"        System.out.println(\"Counting: \" + i); //$NON-NLS-1$\n\n" +
					"}", TextRun.TYPE_CODE));
		//}
	}
	
	setPartName(file.getName());
}

}