/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.parts;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.draw2d.Label;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.ediagram.model.commands.ChangeNameCommand;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class EnumLiteralEditPart 
	extends BaseEditPart
{
	
public EnumLiteralEditPart(EEnumLiteral model) {
	super(model);
}

protected DirectEditPolicy createDirectEditPolicy() {
	return new DirectEditPolicy() {
		protected Command getDirectEditCommand(DirectEditRequest request) {
			return new ChangeNameCommand(
					getEnumLiteral(), (String)request.getCellEditor().getValue());
		}
		protected void showCurrentEditValue(DirectEditRequest request) {
			((Label)getFigure()).setText((String)request.getCellEditor().getValue());
			getFigure().getUpdateManager().performUpdate();
		}
	};
}

protected EEnumLiteral getEnumLiteral() {
	return (EEnumLiteral)getModel();
}

protected void handlePropertyChanged(Notification msg) {
	switch (msg.getFeatureID(EEnumLiteral.class)) {
		case EcorePackage.EENUM_LITERAL__NAME:
			refreshVisuals();
	}
}

protected void refreshVisuals() {
	((Label)getFigure()).setText(getEnumLiteral().getName());
}

}