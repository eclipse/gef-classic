/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.tools;

import java.util.*;

import org.eclipse.gef.*;

public class ToolUtilities {

public static List getSelectionWithoutDependants(EditPartViewer viewer){
	List selectedParts = viewer.getSelectedEditParts();
	List result = new ArrayList();
	for (int i=0; i<selectedParts.size(); i++) {
		GraphicalEditPart editpart = (GraphicalEditPart)selectedParts.get(i);;
		if (!isAncestorContainedIn(selectedParts, editpart))
			result.add(editpart);
	}
	return result;
}

public static List getSelectionWithoutDependants(List selectedParts){
	List result = new ArrayList();
	for (int i=0; i<selectedParts.size(); i++) {
		GraphicalEditPart editpart = (GraphicalEditPart)selectedParts.get(i);;
		if (!isAncestorContainedIn(selectedParts, editpart))
			result.add(editpart);
	}
	return result;
}

public static void filterEditPartsUnderstanding(List list, Request request){
	Iterator iter = list.iterator();
	while (iter.hasNext()){
		EditPart ep = (EditPart)iter.next();
		if (!ep.understandsRequest(request))
			iter.remove();
	}
}

private static boolean isAncestorContainedIn(Collection c, EditPart ep){
	ep = ep.getParent();
	while (ep != null){
		if (c.contains(ep))
			return true;
		ep = ep.getParent();
	}
	return false;
}

}