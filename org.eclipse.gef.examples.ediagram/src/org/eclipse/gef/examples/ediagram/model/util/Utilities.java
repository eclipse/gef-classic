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
package org.eclipse.gef.examples.ediagram.model.util;

import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.InheritanceView;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.outline.InheritanceModel;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class Utilities
{
	
// Returns the corresponding view model for the given business model
// Opp. references are found only if the refView's isOppositeShown() returns true
public static Object findViewFor(Object model, Object searchOrigin) {
	if (searchOrigin instanceof Diagram) {
		List children = ((Diagram)searchOrigin).getContents();
		for (int i = 0; i < children.size(); i++) {
			Object result = findViewFor(model, children.get(i));
			if (result != null)
				return result;
		}
	} else if (searchOrigin instanceof NamedElementView) {
		NamedElementView view = (NamedElementView)searchOrigin;
		if (model instanceof EReference || model instanceof InheritanceModel) {
			List children = view.getOutgoingConnections();
			for (int i = 0; i < children.size(); i++) {
				Object result = findViewFor(model, children.get(i));
				if (result != null)
					return result;
			}
		} else if (model instanceof ENamedElement) {
			if (view.getENamedElement() == model)
				return view;
		}
	} else if (searchOrigin instanceof ReferenceView && model instanceof EReference) {
		ReferenceView refView = (ReferenceView)searchOrigin;
		EReference ref = refView.getEReference();
		if (model == ref || (refView.isOppositeShown() && model == ref.getEOpposite()))
			return refView;
	} else if (searchOrigin instanceof InheritanceView 
			&& model instanceof InheritanceModel) {
		InheritanceView iView = (InheritanceView)searchOrigin;
		InheritanceModel iModel = (InheritanceModel)model;
		if (iModel.getSubType() 
				== ((NamedElementView)iView.getSource()).getENamedElement()
				&& iModel.getSuperType() 
				== ((NamedElementView)iView.getTarget()).getENamedElement())
			return iView;
	}
	return null;
}
	
public static boolean importsPackage(EPackage pckg, Diagram diagram) {
	if (pckg == null)
		return false;
	do {
		if (diagram.getImports().contains(pckg))
			return true;
		pckg = pckg.getESuperPackage();
	} while (pckg != null);
	return false;
}

}
