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
package org.eclipse.gef.examples.ediagram.edit.parts;

import org.eclipse.jface.viewers.TextCellEditor;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.text.TextFlow;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.tools.DirectEditManager;

import org.eclipse.gef.examples.ediagram.edit.policies.LabelSelectionEditPolicy;
import org.eclipse.gef.examples.ediagram.figures.SelectableLabel;
import org.eclipse.gef.examples.ediagram.model.commands.DeleteCommand;

/**
 * Provides infrastructure for deletion, direct-editing, property source and 
 * model-listening.
 * @author Pratik Shah
 * @since 3.1
 */
public abstract class BaseEditPart 
	extends AbstractGraphicalEditPart
{

protected DirectEditManager manager;
protected Adapter modelListener = new AdapterImpl() {
	public void notifyChanged(Notification msg) {
		handlePropertyChanged(msg);
	}
};

public BaseEditPart(EObject obj) {
	super();
	setModel(obj);
}

public void activate() {
	super.activate();
	((EObject)getModel()).eAdapters().add(modelListener);
}

protected abstract DirectEditPolicy createDirectEditPolicy();

protected void createEditPolicies() {
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {
		protected Command createDeleteCommand(GroupRequest deleteRequest) {
			Boolean bool = (Boolean)deleteRequest.getExtendedData()
					.get(DeleteCommand.KEY_PERM_DELETE);
			boolean permDelete = bool == null ? false : bool.booleanValue();
			DeleteCommand cmd = new DeleteCommand(permDelete);
			cmd.setPartToBeDeleted(getHost().getModel());
			return cmd;
		}
	});
	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, createDirectEditPolicy());
	installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new LabelSelectionEditPolicy());
}

protected IFigure createFigure() {
	return new SelectableLabel();
}

public void deactivate() {
	((EObject)getModel()).eAdapters().remove(modelListener);
	super.deactivate();
}

IFigure getDirectEditFigure() {
	return getFigure();
}

String getDirectEditText() {
	IFigure fig = getDirectEditFigure();
	if (fig instanceof Label) {
		return ((Label)fig).getText();
	} else if (fig instanceof TextFlow) {
		return ((TextFlow)fig).getText();
	}
	return ""; //$NON-NLS-1$
}

protected abstract void handlePropertyChanged(Notification msg);

protected void performDirectEdit() {
	if(manager == null)
		manager = new LabelDirectEditManager(this, TextCellEditor.class, 
				new LabelCellEditorLocator(getDirectEditFigure()));
	manager.show();
}

public void performRequest(Request request){
	if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		performDirectEdit();
	else
		super.performRequest(request);
}

}
