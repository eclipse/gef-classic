/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.figures.FigureFactory;
import org.eclipse.gef.examples.logicdesigner.figures.LEDFigure;
import org.eclipse.gef.examples.logicdesigner.model.LED;

/**
 * Holds the EditPart signifying an LED.
 */
public class LEDEditPart
	extends LogicEditPart
{

private static Image LED_SEL_PRIM_BG;
private static Image LED_SEL_SECD_BG;

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){

		public void getName(AccessibleEvent e) {
			e.result = LogicMessages.LogicPlugin_Tool_CreationTool_LED_Label;
		}
		
		public void getValue(AccessibleControlEvent e) {
			e.result = Integer.toString(getLEDModel().getValue());
		}

	};
}

protected void createEditPolicies(){
	super.createEditPolicies();
	installEditPolicy(EditPolicy.COMPONENT_ROLE, new LEDEditPolicy());
}

/**
 * Returns a newly created Figure to represent this.
 *
 * @return  Figure of this.
 */
protected IFigure createFigure() {
	return FigureFactory.createNewLED();
}

public Object getAdapter(Class key) {
	if (key == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider() { 
			public List getSourceAnchorLocations() {
				List list = new ArrayList();
				Vector sourceAnchors = getNodeFigure().getSourceConnectionAnchors();
				for (int i=0; i<sourceAnchors.size(); i++) {
					ConnectionAnchor anchor = (ConnectionAnchor)sourceAnchors.get(i);
					list.add(anchor.getReferencePoint().getTranslated(0, -3));
				}
				return list;
			}
			public List getTargetAnchorLocations() {
				List list = new ArrayList();
				Vector targetAnchors = getNodeFigure().getTargetConnectionAnchors();
				for (int i=0; i<targetAnchors.size(); i++) {
					ConnectionAnchor anchor = (ConnectionAnchor)targetAnchors.get(i);
					list.add(anchor.getReferencePoint().getTranslated(0, 3));
				}
				return list;
			}
		};
	return super.getAdapter(key);
}

protected Image getBackgroundImage(int state){
	if (state == SELECTED_PRIMARY){
		if(LED_SEL_PRIM_BG==null)
			LED_SEL_PRIM_BG = new Image(null, LEDFigure.class.getResourceAsStream("icons/ledbgprim.gif"));  //$NON-NLS-1$
		return LED_SEL_PRIM_BG;
	}
	if (state == SELECTED){
		if(LED_SEL_SECD_BG==null)
			LED_SEL_SECD_BG = new Image(null, LEDFigure.class.getResourceAsStream("icons/ledbgsel.gif"));  //$NON-NLS-1$
		return LED_SEL_SECD_BG;
	}
	return null;
}

/**
 * Returns the Figure of this as a LEDFigure.
 *
 * @return  LEDFigure of this.
 */
public LEDFigure getLEDFigure() {
	return (LEDFigure)getFigure();
}

/**
 * Returns the model of this as a LED.
 *
 * @return  Model of this as an LED.
 */
protected LED getLEDModel() {
	return (LED)getModel();
}

public void propertyChange(java.beans.PropertyChangeEvent change){
	if (change.getPropertyName().equals(LED.P_VALUE))
		refreshVisuals();
	else
		super.propertyChange(change);
}

/**
 * Apart from the usual visual update, it also
 * updates the numeric contents of the LED.
 */
public void refreshVisuals() {
	getLEDFigure().setValue(getLEDModel().getValue());
	super.refreshVisuals();
}

public void setSelected(int i){
	super.setSelected(i);
	refreshVisuals();
}

}
