package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

import org.eclipse.swt.graphics.Image;

import com.ibm.etools.draw2d.geometry.Dimension;

public class LogicLabel
	extends LogicSubpart
{

private String filename = 
	LogicResources.getString("LogicPlugin.Tool.CreationTool.LogicLabel");//$NON-NLS-1$

private static Image LOGIC_LABEL_ICON = new Image(null,
	LED.class.getResourceAsStream("icons/label.gif"));  //$NON-NLS-1$

private static int count;

public LogicLabel() {
	super();
}

public String getFilename(){
	return filename;
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

public void setFilename(String s){
	filename = s;
	firePropertyChange("filename", "", filename); //$NON-NLS-2$//$NON-NLS-1$
}

public String toString() {
	return LogicResources.getString("LogicPlugin.Tool.CreationTool.LogicLabel") //$NON-NLS-1$
					                + " #" + getID() + " " //$NON-NLS-1$ //$NON-NLS-2$
					                + LogicResources.getString("PropertyDescriptor.Label.Text") //$NON-NLS-1$ 
					                + "=" + getFilename(); //$NON-NLS-1$ 
}

}