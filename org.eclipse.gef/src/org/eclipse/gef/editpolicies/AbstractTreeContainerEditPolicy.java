package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;


import org.eclipse.gef.*;
import org.eclipse.gef.requests.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.Timer;

abstract public class AbstractTreeContainerEditPolicy 
	extends AbstractEditPolicy 
{

private Timer autoExpandTimer = new Timer();

{autoExpandTimer.cancel();}

abstract protected Command getAddCommand(ChangeBoundsRequest req);
abstract protected Command getCreateCommand(CreateRequest req);
abstract protected Command getMoveChildrenCommand(ChangeBoundsRequest req);

private void eraseAddFeedback(Request req){
	getTree().setInsertMark(null,true);
}

private void eraseCreateFeedback(Request req){
	eraseAddFeedback(req);
	autoExpandTimer.cancel();
}

private void eraseMoveFeedback(Request req){
	eraseAddFeedback(req);
}

public void eraseTargetFeedback(Request req){
	if(req.getType().equals(REQ_MOVE))
		eraseMoveFeedback(req);
	if(req.getType().equals(REQ_ADD))
		eraseAddFeedback(req);
	if(req.getType().equals(REQ_CREATE))
		eraseCreateFeedback(req);	    			
}

protected int findIndexOfTreeItemAt(org.eclipse.draw2d.geometry.Point pt){
	int index = -1;
	List children = getHost().getChildren();
	TreeItem item = findTreeItemAt(pt);	
	if(item != null){
		index = children.indexOf(item.getData());	
		if(index >= 0 && !isInUpperHalf(item.getBounds(), pt))
			index++;
	}
	return index;
}

private TreeItem findTreeItemAt(org.eclipse.draw2d.geometry.Point pt){
	return getTree().getItem(new Point(pt.x, pt.y));			
}

public Command getCommand(Request req){
	if(req.getType().equals(REQ_MOVE_CHILDREN))
		return getMoveChildrenCommand((ChangeBoundsRequest)req);
	if(req.getType().equals(REQ_ADD))
		return getAddCommand((ChangeBoundsRequest)req);
	if(req.getType().equals(REQ_CREATE))
		return getCreateCommand((CreateRequest)req);
		
	return null;
}

public EditPart getTargetEditPart(Request req){
	if(req.getType().equals(REQ_ADD) ||
	    req.getType().equals(REQ_MOVE) ||
	    req.getType().equals(REQ_CREATE))
	{
		DropRequest drop = (DropRequest)req;
		Point where = new Point(drop.getLocation().x, drop.getLocation().y);
		Widget widget = ((TreeEditPart)getHost()).getWidget();
		if (widget instanceof Tree)
			return getHost();
		TreeItem treeitem = (TreeItem)widget;
		Rectangle bounds = treeitem.getBounds();
		int fudge = bounds.height/5;
		Rectangle inner = new Rectangle(bounds.x, bounds.y + fudge, bounds.width, bounds.height - fudge*2);
		if (!bounds.contains(where) ||
			inner.contains(where))
			return getHost();
	}
	
	return null;
}

private Tree getTree(){
	Widget widget = ((TreeEditPart)getHost()).getWidget();
	if(widget instanceof Tree)
		return (Tree)widget;
	else
		return ((TreeItem)widget).getParent();
}

private void insertMarkAfterLastChild(TreeItem[] children){
	if(children != null && children.length > 0){
		TreeItem item = children[ children.length - 1 ];
		getTree().setInsertMark(item,false);
	}
}

private boolean isInUpperHalf(Rectangle rect, 
					   org.eclipse.draw2d.geometry.Point pt){
	Rectangle tempRect = new Rectangle(rect.x, rect.y, 
							rect.width, rect.height/2);
	return tempRect.contains(new Point(pt.x, pt.y));
}

private void showAddFeedback(Request req){
	if(!(req instanceof ChangeBoundsRequest))
		return;
		
	ChangeBoundsRequest request = (ChangeBoundsRequest)req;
	Tree tree = getTree();
	org.eclipse.draw2d.geometry.Point pt = request.getLocation();
	TreeItem item = findTreeItemAt(pt);
	if(item == null){
		Widget widget = ((TreeEditPart)getHost()).getWidget();
		if(widget == tree){
			insertMarkAfterLastChild(tree.getItems());
		}
	} else if(item == ((TreeEditPart)getHost()).getWidget()){
		insertMarkAfterLastChild(item.getItems());
	} else {
		boolean before = isInUpperHalf(item.getBounds(), pt);
		tree.setInsertMark(item, before);
	}
}

private void showCreateFeedback(Request req){
	if(!(req instanceof CreateRequest))
		return;
		
	CreateRequest request = (CreateRequest)req;
	org.eclipse.draw2d.geometry.Point pt = request.getLocation();
	final Tree tree = getTree();
	TreeItem item = findTreeItemAt(pt);
	if(item == null){
		Widget widget = ((TreeEditPart)getHost()).getWidget();
		if(widget == tree){
			insertMarkAfterLastChild(tree.getItems());
		}
	} else if(item == ((TreeEditPart)getHost()).getWidget()){
		insertMarkAfterLastChild(item.getItems());
	} else {
		boolean before = isInUpperHalf(item.getBounds(), pt);
		tree.setInsertMark(item, before);
	}
	
	if(!(autoExpandTimer.hasStopped())){
		return;
	}
	
	autoExpandTimer = new Timer();
	autoExpandTimer.scheduleRepeatedly(new Runnable(){
		public void run(){
			Display display = tree.getDisplay();
			display.syncExec(new Runnable(){
				public void run(){
					Widget widget = ((TreeEditPart)getHost()).getWidget();
					if(widget instanceof TreeItem)
						((TreeItem)widget).setExpanded(true);
					autoExpandTimer.cancel();
				}
			});
		}
	}, 500, 10000);
}

private void showMoveFeedback(Request req){
	showAddFeedback(req);
}

public void showTargetFeedback(Request req){
	if(req.getType().equals(REQ_MOVE))
		showMoveFeedback(req);
	if(req.getType().equals(REQ_ADD))
		showAddFeedback(req);
	if(req.getType().equals(REQ_CREATE))
		showCreateFeedback(req);   			
}

}