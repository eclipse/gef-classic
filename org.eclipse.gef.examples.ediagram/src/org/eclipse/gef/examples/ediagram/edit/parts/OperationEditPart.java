/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.ediagram.EDiagramImages;
import org.eclipse.gef.examples.ediagram.figures.SelectableLabel;
import org.eclipse.gef.examples.ediagram.model.commands.ChangeNameCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class OperationEditPart 
	extends BaseEditPart
{
	
public OperationEditPart(EOperation model) {
	super(model);
}

protected DirectEditPolicy createDirectEditPolicy() {
	return new DirectEditPolicy() {
		protected Command getDirectEditCommand(DirectEditRequest request) {
			return new ChangeNameCommand(
					getOperation(), (String)request.getCellEditor().getValue());
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
			((Label)getFigure()).setText((String)request.getCellEditor().getValue());
			getFigure().getUpdateManager().performUpdate();
		}
	};
}

protected IFigure createFigure() {
	SelectableLabel fig = new SelectableLabel();
	fig.setIcon(EDiagramImages.getImage(EDiagramImages.METHOD_PUBLIC));
	return fig;
}

String getDirectEditText() {
	return getOperation().getName();
}

private EOperation getOperation() {
	return (EOperation)getModel();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EOperation.class)) {
		case EcorePackage.EOPERATION__EPARAMETERS:
		case EcorePackage.EOPERATION__ETYPE:
		case EcorePackage.EOPERATION__NAME:
//		case EcorePackage.EOPERATION__EEXCEPTIONS:
			refreshVisuals();
	}
}

protected void refreshVisuals() {
	Label fig = (Label)getFigure();
	String params = ""; //$NON-NLS-1$
	for (Iterator iter = getOperation().getEParameters().iterator(); iter.hasNext();) {
		EParameter param = (EParameter)iter.next();
		if (params.length() > 0)
			params += ", "; //$NON-NLS-1$
		params += param.getEType().getName();
	}
	String suffix = " : void";
	if (getOperation().getEType() != null) {
		suffix = " : " + getOperation().getEType().getName();
	}
	/*
	 * @TODO:Pratik do you need to show the exceptions that a method throws?
	 */
	fig.setText(getOperation().getName() + "(" + params + ")" + suffix);
}

}
