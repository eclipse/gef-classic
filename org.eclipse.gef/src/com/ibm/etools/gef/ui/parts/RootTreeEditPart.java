package com.ibm.etools.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.ArrayList;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.Tree;

import com.ibm.etools.common.command.*;

import com.ibm.etools.gef.*;

public class RootTreeEditPart
	extends com.ibm.etools.gef.editparts.AbstractEditPart
	implements RootEditPart, TreeEditPart
{

private EditPartViewer viewer;
private Widget widget;
private TreeEditPart contents;

/**
 * This is where the child gets added. No TreeItem is needed here because
 * the contents is actually represented by the Tree iteself.
 *
 * @param childEditPart  EditPart of child to be added.
 * @param index  Position where it is to be added.
 */
protected void addChildVisual(EditPart childEditPart, int index){}

protected void createEditPolicies(){}

public Command getCommand(Request request) {
	return UnexecutableCommand.INSTANCE;
}

public EditPart getContents(){
	return contents;
}

public DragTracker getDragTracker(Request request){
	return null;
}

/**
 * Returns itself
 */
public RootEditPart getRoot(){
	return this;
}

/**
 * Return the viewer that this root view object lives in.
 * @param viewer com.ibm.etools.gef.IGEFViewer  The viewer.
 */
public EditPartViewer getViewer(){
	return viewer;
}

public Widget getWidget(){
	return widget;
}

/**
 * This is where the child gets removed.  This method is overridden
 * here so that the AbstractTreeEditPart does not dispose the widget,
 * which is the Tree in this case.  The tree is owned by the viewer, not the child.
 *
 * @param childEditPart  EditPart of child to be removed.
 */
protected void removeChildVisual(EditPart childEditPart){}

public void setContents(EditPart editpart){
	if(contents != null){
		if(getWidget() != null)
			((Tree)getWidget()).removeAll();
		removeChild(contents);
		contents.setWidget(null);
	}
	contents = (TreeEditPart)editpart;
	if(contents != null){
		addChild(contents, -1);
		contents.setWidget(getWidget());
	}
}

/**
 * Set the viewer that this root view object lives in.
 * @param viewer com.ibm.etools.gef.IGEFViewer  The viewer.
 */
public void setViewer(EditPartViewer epviewer){
	viewer = epviewer;
//	setWidget(viewer.getControl());
}

public void setWidget(Widget w){
	widget = w;
	if (contents != null)
		contents.setWidget(w);
}

}