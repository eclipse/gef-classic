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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.gef.examples.ediagram.EDiagramImages;


public class PackageTreeEditPart extends BaseTreeEditPart {
	public PackageTreeEditPart(EPackage model) {
		super(model);
	}
	protected Image getImage() {
		return EDiagramImages.getImage(EDiagramImages.PACKAGE);
	}
	protected List getModelChildren() {
		List result = new ArrayList();
		for (Iterator iter = getPackage().getESubpackages().iterator(); iter.hasNext();) {
			result.add(iter.next());
		}
		for (Iterator iter = getPackage().getEClassifiers().iterator(); iter.hasNext();) {
			Object next = iter.next();
			result.add(next);
		}
		return result;
	}
	private EPackage getPackage() {
		return (EPackage)getModel();
	}
	protected String getText() {
		return getPackage().getName();
	}
	protected void handlePropertyChanged(Notification msg) {
		switch (msg.getFeatureID(EPackage.class)) {
			case EcorePackage.EPACKAGE__NAME:
				refreshVisuals();
				return;
			case EcorePackage.EPACKAGE__ESUBPACKAGES:
			case EcorePackage.EPACKAGE__ECLASSIFIERS:
				refreshChildren();
		}
	}
}
