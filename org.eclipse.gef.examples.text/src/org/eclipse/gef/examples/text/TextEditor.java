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

import org.eclipse.draw2d.FigureCanvas;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;

import org.eclipse.gef.examples.text.edit.CompoundTextualEditPart;
import org.eclipse.gef.examples.text.edit.DocumentPart;
import org.eclipse.gef.examples.text.edit.ImportStatementPart;
import org.eclipse.gef.examples.text.edit.ImportsPart;
import org.eclipse.gef.examples.text.edit.TextFlowPart;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.model.Document;
import org.eclipse.gef.examples.text.model.ImportStatement;
import org.eclipse.gef.examples.text.model.Imports;
import org.eclipse.gef.examples.text.model.TextRun;
import org.eclipse.gef.examples.text.tools.TextTool;

/**
 * @since 3.1
 */
public class TextEditor extends GraphicalEditor {

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

/**
 * @see GraphicalEditor#initializeGraphicalViewer()
 */
protected void initializeGraphicalViewer() {
	getGraphicalViewer().setEditPartFactory(new EditPartFactory() {
		public EditPart createEditPart(EditPart context, Object model) {
			if (model instanceof Document)
				return new DocumentPart(model);
			else if (model instanceof Imports)
				return new ImportsPart(model);
			else if (model instanceof ImportStatement)
				return new ImportStatementPart(model);
			else if (model instanceof TextRun)
				return new TextFlowPart(model);
			else if (model instanceof Container)
					return new CompoundTextualEditPart(model);
			throw new RuntimeException("unexpected model object");
		}
	});

	Document doc = new Document();
	doc.add(new TextRun("document"));

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
				if (event.getDetail() == CommandStack.STATE_POST_REDO)
					textViewer.setSelectionRange(command
							.getRedoSelectionRange(textViewer));
				else if (event.getDetail() == CommandStack.STATE_POST_UNDO)
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
	site.getKeyBindingService().registerAction(
			getActionRegistry().getAction(ActionFactory.UNDO.getId()));
	site.getKeyBindingService().registerAction(
			getActionRegistry().getAction(ActionFactory.REDO.getId()));

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