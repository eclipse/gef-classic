package com.ibm.etools.gef.examples.logicdesigner.edit;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.viewers.TextCellEditor;

import com.ibm.etools.draw2d.*;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.tools.DirectEditManager;
import com.ibm.etools.gef.examples.logicdesigner.model.LogicLabel;

public class LogicLabelEditPart
	extends LogicEditPart
{

private DirectEditManager manager;

private static Border BORDER = new CompoundBorder(
	new LineBorder(),
	new MarginBorder(2)
);

protected void createEditPolicies(){
	super.createEditPolicies();
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);		
	installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
	installEditPolicy(EditPolicy.COMPONENT_ROLE,new LogicLabelEditPolicy()); 
}

protected IFigure createFigure() {
	Label label = new Label();
	label.setBorder(BORDER);
	return label;
}

private void performDirectEdit(){
	if(manager == null)
		manager = new LogicLabelEditManager(this, 
			TextCellEditor.class, new LabelCellEditorLocator((Label)getFigure()));
	manager.show();
}

public void performRequest(Request request){
	if (request.getType() == RequestConstants.REQ_DIRECT_EDIT)
		performDirectEdit();
}

public void propertyChange(PropertyChangeEvent evt){
	if (evt.getPropertyName().equalsIgnoreCase("filename"))//$NON-NLS-1$
		refreshVisuals();
	else
		super.propertyChange(evt);
}

protected void refreshVisuals() {
	LogicLabel model = (LogicLabel)getModel();
	((Label)getFigure()).setText(model.getFilename());
	super.refreshVisuals();
}

}