/**
 * 
 */
package org.eclipse.gef.examples.ediagram.model.properties;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.LabelProvider;

public class ENamedElementLabelProvider extends LabelProvider {

	public static String getPath(EObject eObject) {
		EObject eContainer = eObject.eContainer();
		if (eContainer instanceof EPackage)
			return getPath(eContainer) + ((EPackage)eContainer).getName() + ".";
		else
			return "";
	} 

	public String getText(Object object) {
		if (object == null)
			return "";
		ENamedElement element = (ENamedElement)object;
		return getPath(element) + element.getName();
	}
}