package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.*;

public class SelectionSynchronizer
	implements ISelectionChangedListener
{

private List viewers = new ArrayList();
private boolean isDispatching = false;

public void addViewer(EditPartViewer viewer){
	viewer.addSelectionChangedListener(this);
	viewers.add(viewer);
}

/**
 * This method converts the given edit part into an edit part that 
 * is accepted by the given viewer.  It returns null if there is
 * nothing in the viewer that corresponds to the given edit part.
 * This method can be overridden by sub-classes to provide
 * custom conversions, i.e. to provide a different implementation
 * for converting edit parts.
 */
protected EditPart convert(EditPartViewer viewer, EditPart part){
	Object temp = viewer.getEditPartRegistry().get(part.getModel());
	EditPart newPart = null;
	if(temp != null){
		newPart = (EditPart)temp;
	}
	return newPart;
}

public void removeViewer(EditPartViewer viewer){
	viewer.removeSelectionChangedListener(this);
	viewers.remove(viewer);
}

public void selectionChanged(SelectionChangedEvent event){
	if (isDispatching)
		return;
	isDispatching = true;
	EditPartViewer source = (EditPartViewer)event.getSelectionProvider();
	ISelection selection = event.getSelection();
	for(int i = 0; i < viewers.size(); i++){
		if(viewers.get(i) != source){
			EditPartViewer viewer = (EditPartViewer)viewers.get(i);
			setViewerSelection(viewer, selection);
		}
	}
	isDispatching = false;
}

private void setViewerSelection(EditPartViewer viewer, ISelection selection){
	ArrayList result = new ArrayList();
	Iterator iter = ((IStructuredSelection)selection).iterator();
	while(iter.hasNext()){
		EditPart part = convert(viewer, (EditPart)iter.next());
		if(part != null){
			result.add(part);
		}
	}
	viewer.setSelection(new StructuredSelection(result));
}

}