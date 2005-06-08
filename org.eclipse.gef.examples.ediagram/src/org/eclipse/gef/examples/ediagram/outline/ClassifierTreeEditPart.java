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
package org.eclipse.gef.examples.ediagram.outline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.gef.examples.ediagram.EDiagramImages;


public class ClassifierTreeEditPart extends BaseTreeEditPart {
	private List superTypes = Collections.EMPTY_LIST;
	public ClassifierTreeEditPart(EClassifier model) {
		super(model);
	}
	public void deactivate() {
		updateSuperTypes(Collections.EMPTY_LIST);
		super.deactivate();
	}
	private EClassifier getEClassifier() {
		return (EClassifier)getModel();
	}
	protected Image getImage() {
		if (getEClassifier() instanceof EClass && ((EClass)getEClassifier()).isInterface())
			return EDiagramImages.getImage(EDiagramImages.INTERFACE);
		return EDiagramImages.getImage(EDiagramImages.CLASS);
	}
	protected String getText() {
		return getEClassifier().getName();
	}
	protected List getModelChildren() {
		if (getModel() instanceof EClass) {
			EClass subType = (EClass)getModel();
			List children = new ArrayList();
			updateSuperTypes(subType.getESuperTypes());
			for (Iterator iter = superTypes.iterator(); iter.hasNext();) {
				children.add(new InheritanceModel((EClass)iter.next(), subType));
			}
			children.addAll(subType.getEReferences());
			return children;
		}
		return super.getModelChildren();
	}
	protected void handlePropertyChanged(Notification msg) {
		switch (msg.getFeatureID(EClassifier.class)) {
			case EcorePackage.ECLASSIFIER__NAME:
				if (msg.getNotifier() == getModel())
					refreshVisuals();
				else
					// a supertype's name has changed
					refreshChildren();
		}
		switch (msg.getFeatureID(EClass.class)) {
			case EcorePackage.ECLASS__ESTRUCTURAL_FEATURES:
			case EcorePackage.ECLASS__ESUPER_TYPES:
				refreshChildren();
		}
	}
	protected void updateSuperTypes(List newTypes) {
		for (Iterator iter = superTypes.iterator(); iter.hasNext();) {
			EClass superClass = (EClass) iter.next();
			superClass.eAdapters().remove(modelListener);
		}
		superTypes = newTypes;
		for (Iterator iter = superTypes.iterator(); iter.hasNext();) {
			EClass superClass = (EClass) iter.next();
			superClass.eAdapters().add(modelListener);
		}
	}
}
