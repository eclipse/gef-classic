package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import com.ibm.etools.gef.*;

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