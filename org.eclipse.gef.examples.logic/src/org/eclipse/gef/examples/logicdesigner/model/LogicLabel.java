package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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