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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LogicLabel
	extends LogicSubpart
{

private String text = 
	LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel; 

private static Image LOGIC_LABEL_ICON = new Image(null,
	LED.class.getResourceAsStream("icons/label16.gif"));  //$NON-NLS-1$

private static int count;

public LogicLabel() {
	super();
}

public String getLabelContents(){
	return text;
}

public Image getIconImage() {
	return LOGIC_LABEL_ICON;
}

protected String getNewID() {
	return Integer.toString(count++);
}

public Dimension getSize(){
	return new Dimension(-1, -1);
}

public void setLabelContents(String s){
	text = s;
	firePropertyChange("labelContents", "", text); //$NON-NLS-2$//$NON-NLS-1$
}

public String toString() {
	return LogicMessages.LogicPlugin_Tool_CreationTool_LogicLabel
					                + " #" + getID() + " " //$NON-NLS-1$ //$NON-NLS-2$
					                + LogicMessages.PropertyDescriptor_Label_Text  
					                + "=" + getLabelContents(); //$NON-NLS-1$ 
}

}