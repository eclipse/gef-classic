package com.ibm.etools.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;

import org.eclipse.jface.viewers.StructuredSelection;

import com.ibm.etools.gef.*;
import com.ibm.etools.gef.commands.*;

/**
 * Provides support for EditParts which belong to the
 * Tree.
 */
abstract public class AbstractTreeEditPart
	extends AbstractEditPart
	implements TreeEditPart
{

protected Widget widget;
private boolean expanded;

/**
 * Constructor, which sets the model of this.
 *
 * @param model  Model which this Tree EditPart should
 *               represent.
 */
public AbstractTreeEditPart(Object model) {
	setModel(model);
}

public AbstractTreeEditPart(){}

public void activate(){
	super.activate();
}

/**
 * This is where the child gets added, and its widget
 * created and set.
 *
 * @param childEditPart  EditPart of child to be added.
 * @param index  Position where it is to be added.
 */
protected void addChildVisual(EditPart childEditPart, int index){
	Widget widget = getWidget();
	TreeItem item;
	if (widget instanceof Tree)
		item = new TreeItem((Tree)widget, 0, index);
	else
		item = new TreeItem((TreeItem)widget, 0, index);
	((TreeEditPart)childEditPart).setWidget(item);
}

protected boolean checkTreeItem(){
	return !(widget == null || widget.isDisposed() || widget instanceof Tree);
}

protected void createEditPolicies(){}

/**
 * Returns a drag tracker suitable for dragging the receiver.
 *
 * @return  Returns a DragTracker
 */
public DragTracker getDragTracker(Request req){
	return new com.ibm.etools.gef.tools.DragTreeItemsTracker(this);
}

protected Image getImage(){
	return null;
}

protected String getText(){
	return getClass().getName();
}

/**
 * Returns the Widget which represents this.
 *
 * @return  Widget
 */
public Widget getWidget() {
	return widget;
}

protected void refreshVisuals() {
	setWidgetImage(getImage());
	setWidgetText(getText());
}

/**
 * The child is finally removed through this method.
 * Its Widget is first removed and disposed, after
 * which it removes the EditPart.
 *
 * @param childEditPart  EditPart of the child to be removed.
 */
protected void removeChildVisual(EditPart childEditPart){
	TreeEditPart treeEditPart = (TreeEditPart)childEditPart;
	treeEditPart.getWidget().dispose();
	treeEditPart.setWidget(null);
}

/**
 * Sets the Widget of this, after which the entire EditPart
 * is updated.
 *
 * @param newWidget  The new Widget of this.
 */
public void setWidget(Widget widget) {
	List children = getChildren();
	if (widget != null){
		widget.setData(this);
		if (widget instanceof TreeItem){
			final TreeItem item = (TreeItem) widget;
			item.addDisposeListener(new DisposeListener(){
				public void widgetDisposed(DisposeEvent e){
					expanded = item.getExpanded();
				}
			});
		}
		for (int i=0; i<children.size(); i++){
			TreeEditPart tep = (TreeEditPart)children.get(i);
			if (widget instanceof TreeItem)
				tep.setWidget(new TreeItem((TreeItem)widget, 0));
			else
				tep.setWidget(new TreeItem((Tree)widget,0));
		}
		if (widget instanceof TreeItem)
			((TreeItem)widget).setExpanded(expanded);
	} else {
		setFlag(FLAG_INITIALIZED, false);
		Iterator iter = getChildren().iterator();
		while (iter.hasNext())
			((TreeEditPart)iter.next()).setWidget(null);
	}
	this.widget = widget;
	initialize();
}

/**
 * Sets the image of a TreeItem (Widget) of this
 * with the one given as input.
 *
 * @param image  Image to be used.
 */
protected void setWidgetImage(Image image){
	if (checkTreeItem())
		((TreeItem)getWidget()).setImage(image);
}

/**
 * Sets the test of a TreeItem (Widget) of this
 * with the text given as input.
 *
 * @param text  Text to be set.
 */
protected void setWidgetText(String text){
	if (checkTreeItem())
		((TreeItem)getWidget()).setText(text);
}

/**
 * Determines if the Editpart has the necessary information to initialize
 */
protected boolean shouldInitialize(){
	return super.shouldInitialize() &&
		getWidget() != null &&
		!getWidget().isDisposed();
}

}
