package org.eclipse.gef.ui.stackview;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;

public class CommandStackViewerAction 
	extends Action {

protected TreeViewer viewer;

public CommandStackViewerAction(TreeViewer viewer){
	super("Toggle Debug Labels", //$NON-NLS-1$
		ImageDescriptor.createFromFile(CommandStackInspector.class,"icons/stackDebug.gif"));//$NON-NLS-1$

	this.viewer=viewer;
	setChecked(((TreeLabelProvider)viewer.getLabelProvider()).getLabelStyle()==TreeLabelProvider.DEBUG_LABEL_STYLE);
}

public void run(){
	if(viewer==null)
		return;
	TreeLabelProvider labelProvider = (TreeLabelProvider)viewer.getLabelProvider();
	if(!isChecked()){
		labelProvider.setLabelStyle(TreeLabelProvider.NORMAL_LABEL_STYLE);
	}else{
		labelProvider.setLabelStyle(TreeLabelProvider.DEBUG_LABEL_STYLE);
	}
	viewer.refresh();
}

}