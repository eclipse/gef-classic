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
package org.eclipse.gef.examples.ediagram.outline;

import org.eclipse.swt.graphics.Image;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.gef.examples.ediagram.EDiagramImages;


public class ReferenceTreeEditPart extends BaseTreeEditPart {
	public ReferenceTreeEditPart(EReference model) {
		super(model);
	}
	public void activate() {
		super.activate();
		toggleListener(getERef().getEReferenceType(), true);
	}
	public void deactivate() {
		toggleListener(getERef().getEReferenceType(), false);
		super.deactivate();
	}
	private EReference getERef() {
		return (EReference)getModel();
	}
	protected Image getImage() {
		return EDiagramImages.getImage(EDiagramImages.REFERENCE);
	}
	protected String getText() {
		String result = getERef().getName() + " : ";
		String suffix = "null";
		if (getERef().getEReferenceType() != null)
			suffix = getERef().getEReferenceType().getName();
		return result + suffix;
	}
	protected void handlePropertyChanged(Notification msg) {
		switch (msg.getFeatureID(EReference.class)) {
			case EcorePackage.ETYPED_ELEMENT__ETYPE:
				toggleListener((EClass)msg.getOldValue(), false);
				toggleListener(getERef().getEReferenceType(), true);
			// EREFERENCE__NAME == ENAMED_ELEMENT__NAME
			// this will catch the reference's name changing or the reference
		    // type's name changing
			case EcorePackage.EREFERENCE__NAME:
				refreshVisuals();
				return;
		}
	}
	private void toggleListener(EClass eClass, boolean add) {
		if (eClass != null) {
			if (add)
				eClass.eAdapters().add(modelListener);
			else
				eClass.eAdapters().remove(modelListener);
		}
	}
}
