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
package org.eclipse.gef.examples.logicdesigner.edit;

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;

public class LogicLabelEditPart
	extends LogicEditPart
{
	
protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getValue(AccessibleControlEvent e) {
			e.result = getLogicLabel().getLabelContents();
		}

		public void getName(AccessibleEvent e) {
			e.result = LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel;
		}
	};
}

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

private LogicLabel getLogicLabel(){
	return (LogicLabel)getModel();
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
	if (evt.getPropertyName().equalsIgnoreCase("labelContents"))//$NON-NLS-1$
		refreshVisuals();
	else
		super.propertyChange(evt);
}

protected void refreshVisuals() {
	((Label)getFigure()).setText(getLogicLabel().getLabelContents());
	super.refreshVisuals();
}

}