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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.Label;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.ediagram.EDiagramImages;
import org.eclipse.gef.examples.ediagram.model.commands.ChangeNameCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class AttributeEditPart 
	extends BaseEditPart
{
	
public AttributeEditPart(EAttribute model) {
	super(model);
}

protected DirectEditPolicy createDirectEditPolicy() {
	return new DirectEditPolicy() {
		protected Command getDirectEditCommand(DirectEditRequest request) {
			return new ChangeNameCommand(
					getAttribute(), (String)request.getCellEditor().getValue());
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
			((Label)getFigure()).setText((String)request.getCellEditor().getValue());
			getFigure().getUpdateManager().performUpdate();
		}
	};
}

private EAttribute getAttribute() {
	return (EAttribute)getModel();
}

String getDirectEditText() {
	return getAttribute().getName();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EAttribute.class)) {
		case EcorePackage.EATTRIBUTE__NAME:
		case EcorePackage.EATTRIBUTE__ETYPE:
			refreshVisuals();
	}
}

protected void refreshVisuals() {
	Label fig = (Label)getFigure();
	String displayText = "" + getAttribute().getName(); //$NON-NLS-1$
	if (getAttribute().getEAttributeType() != null) {
		displayText += " : " + getAttribute().getEAttributeType().getName();
	}
	fig.setText(displayText);
	fig.setIcon(EDiagramImages.getImage(EDiagramImages.FIELD_PUBLIC));
}

}
