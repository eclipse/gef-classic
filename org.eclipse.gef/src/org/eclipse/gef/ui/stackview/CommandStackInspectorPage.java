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