package org.eclipse.gef.ui.stackview;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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