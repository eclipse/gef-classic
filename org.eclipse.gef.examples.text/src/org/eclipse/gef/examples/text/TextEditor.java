/*****************************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Common Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************/

package org.eclipse.gef.examples.text;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
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
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.TreeViewer;

import org.eclipse.gef.examples.text.edit.BlockTextualPart;
import org.eclipse.gef.examples.text.edit.CompoundTextualPart;
import org.eclipse.gef.examples.text.edit.ContainerTreePart;
import org.eclipse.gef.examples.text.edit.DocumentPart;
import org.eclipse.gef.examples.text.edit.ImportsPart;
import org.eclipse.gef.examples.text.edit.InlineTextualPart;
import org.eclipse.gef.examples.text.edit.TextFlowPart;
import org.eclipse.gef.examples.text.edit.TextLayoutPart;
import org.eclipse.gef.examples.text.edit.TextRunTreePart;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.TextTool;

/**
 * @since 3.1
 */
public class TextEditor extends GraphicalEditor {

private Container doc;

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

/**
 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
 */
protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
	((FigureCanvas)getGraphicalViewer().getControl()).getViewport()
			.setContentsTracksWidth(true);
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

	doc = new Container(Container.TYPE_ROOT);

	Container block = new Container(Container.TYPE_COMMENT);
	
	block.add(new TextRun("Copyright (c) 2004 IBM Corporation and others. All rights reserved. This program and " +
			"the accompanying materials are made available under the terms of the Common Public " +
			"License v1.0 which accompanies this distribution, and is available at " +
			"http://www.eclipse.org/legal/cpl-v10.html\r\n" + 
			"Contributors: IBM Corporation - initial API and implementation"));
	
	Container inline = new Container(Container.TYPE_INLINE);
	inline.getStyle().setBold(true);
	inline.add(new TextRun("ABC def GHI jkl MNO pqr STU vwxyz"));
	block.add(inline);
	
	doc.add(block);
	doc.add(new TextRun("Paragraph 1"));

	getGraphicalViewer().setContents(doc);
}

/**
 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
 */
public void doSave(IProgressMonitor monitor) {}

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
				if (event.getDetail() == CommandStack.POST_REDO)
					textViewer.setSelectionRange(command
							.getRedoSelectionRange(textViewer));
				else if (event.getDetail() == CommandStack.POST_UNDO)
						textViewer.setSelectionRange(command
								.getUndoSelectionRange(textViewer));
			}
		}
	});
	getEditDomain().setDefaultTool(new TextTool());
	getEditDomain().loadDefaultTool();
	super.init(site, input);
	site.getActionBars().setGlobalActionHandler(ActionFactory.UNDO.getId(),
			getActionRegistry().getAction(ActionFactory.UNDO.getId()));
	site.getActionBars().setGlobalActionHandler(ActionFactory.REDO.getId(),
			getActionRegistry().getAction(ActionFactory.REDO.getId()));
}

/**
 * @see org.eclipse.ui.ISaveablePart#isDirty()
 */
public boolean isDirty() {
	return false;
}

/**
 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
 */
public boolean isSaveAsAllowed() {
	return false;
}

}