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

import java.util.EventObject;

import org.eclipse.jface.viewers.Viewer;

import org.eclipse.gef.commands.*;

public class TreeContentProvider
	implements org.eclipse.jface.viewers.ITreeContentProvider,
		CommandStackListener
{

Viewer viewer;

public TreeContentProvider(CommandStack stack){
	stack.addCommandStackListener(this);

}

public void dispose(){
}

public void commandStackChanged(EventObject event){
	viewer.refresh();
}

public Object[] getChildren(Object o){
	if (o instanceof CompoundCommand){
		return ((CompoundCommand)o).getChildren();
	}
	return null;
}

public Object[] getElements(Object o){
	if (o instanceof CommandStack){
		return ((CommandStack)o).getCommands();
	}
	if (o instanceof CompoundCommand){
	}
	return null;
}

public Object getParent(Object child){return null;}

public boolean hasChildren(Object o){
	return o instanceof CompoundCommand;
}

public boolean isDeleted(Object o){return false;}

public void inputChanged(Viewer v, Object o, Object n){
	viewer = v;
}

}