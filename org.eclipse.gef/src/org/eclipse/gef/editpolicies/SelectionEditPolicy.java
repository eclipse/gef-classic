package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.draw2d.*;
import org.eclipse.gef.*;

abstract public class SelectionEditPolicy
	extends org.eclipse.gef.editpolicies.GraphicalEditPolicy
{

private EditPartListener selectionListener;
private int state = -1;
boolean focus;

public SelectionEditPolicy(){}

public void activate(){
	super.activate();
	addSelectionListener();
	setSelectedState(getHost().getSelected());
	setFocus(getHost().hasFocus());
}

protected void addSelectionListener(){
	selectionListener = new EditPartListener.Stub(){
		public void selectedStateChanged(EditPart part){
			setSelectedState(part.getSelected());
			setFocus(part.hasFocus());
		}
	};
	getHost().addEditPartListener(selectionListener);
}

public void deactivate(){
	removeSelectionListener();
	setSelectedState(EditPart.SELECTED_NONE);
	setFocus(false);
	super.deactivate();
}

public EditPart getTargetEditPart(Request request){
	if (RequestConstants.REQ_SELECTION.equals(request.getType()))
		return getHost();
	return null;
}

protected void hideFocus(){}

protected abstract void hideSelection();

protected void removeSelectionListener(){
	getHost().removeEditPartListener(selectionListener);
}

protected void setFocus(boolean value){
	if (focus == value)
		return;
	focus = value;
	if (focus)
		showFocus();
	else
		hideFocus();
}

protected void setSelectedState(int type){
	if (state == type)
		return;
	state = type;
	if (type == EditPart.SELECTED_PRIMARY)
		showPrimarySelection();
	else if (type == EditPart.SELECTED)
		showSelection();
	else
		hideSelection();
}

protected void showFocus(){}

protected void showPrimarySelection(){
	showSelection();
}

abstract protected void showSelection();
}


