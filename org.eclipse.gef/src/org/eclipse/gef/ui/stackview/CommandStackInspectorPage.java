package org.eclipse.gef.ui.stackview;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.*;

public class CommandStackInspectorPage
	extends org.eclipse.ui.part.Page
{

CommandStack input;
TreeViewer treeViewer;

public CommandStackInspectorPage(CommandStack input){
	this.input = input;
}

public void createControl(Composite composite){
	treeViewer = new TreeViewer(composite);
	treeViewer.setContentProvider(new TreeContentProvider(input));
	treeViewer.setLabelProvider(new TreeLabelProvider(input));
	treeViewer.setInput(input);
}

public Control getControl(){
	return treeViewer.getControl();
}

public void makeContributions(
	IMenuManager menuManager, 
	IToolBarManager toolBarManager, 
	IStatusLineManager statusLineManager) {
	super.makeContributions(menuManager, toolBarManager, statusLineManager);
	toolBarManager.add(new CommandStackViewerAction(treeViewer));
}


public void setFocus(){
	getControl().setFocus();
}

}