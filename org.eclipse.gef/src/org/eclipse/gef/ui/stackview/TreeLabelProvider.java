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
package org.eclipse.gef.ui.stackview;

import org.eclipse.swt.graphics.Image;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;

public class TreeLabelProvider
	implements org.eclipse.jface.viewers.ILabelProvider
{

protected CommandStack stack = null;
protected static Image 
	yesIcon = ImageDescriptor.createFromFile(TreeLabelProvider.class, "icons/YESGRN.gif").createImage(),//$NON-NLS-1$
	noIcon  = ImageDescriptor.createFromFile(TreeLabelProvider.class, "icons/NORED.gif").createImage();//$NON-NLS-1$
public static byte NORMAL_LABEL_STYLE = 2,
			  	   DEBUG_LABEL_STYLE = 3;
protected byte labelStyle=NORMAL_LABEL_STYLE;
	
public TreeLabelProvider(CommandStack stack){
	this.stack = stack;	
}

public void addListener(ILabelProviderListener l){}
public void dispose(){}

public Image getImage(Object o){
	if(o instanceof Command){
		Command command = (Command)o;
//		if(((DefaultCommandStack)stack).canUndoCommand(command)) 
//			return yesIcon;
//		if(((DefaultCommandStack)stack).canRedoCommand(command)) 
//			return noIcon;
		if(command.canUndo())
			return yesIcon;
		else
			return noIcon;
	}
	return null;
}

public byte getLabelStyle(){
	return labelStyle;
}
 
public String getText(Object o){
	if (o instanceof CommandStack)
		return "Command Stack";//$NON-NLS-1$
	if (o instanceof Command){
		if(getLabelStyle()==NORMAL_LABEL_STYLE)
			if(((Command)o).getLabel()==null)
				return "";//$NON-NLS-1$
			else
				return ((Command)o).getLabel();
		if(getLabelStyle()==DEBUG_LABEL_STYLE)
			if(((Command)o).getDebugLabel()==null)
				return "";//$NON-NLS-1$
			else
				return ((Command)o).getDebugLabel();
	}
	if (o instanceof Command)
			return ((Command)o).getLabel();
	return "???";//$NON-NLS-1$
}

public boolean isLabelProperty(Object element, String property){return false;}
public void removeListener(ILabelProviderListener l){}

public void setLabelStyle(byte labelStyle){
	this.labelStyle=labelStyle;
}

}