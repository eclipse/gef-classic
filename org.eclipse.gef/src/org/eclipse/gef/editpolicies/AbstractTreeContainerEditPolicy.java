package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

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

/**
 * An EditPolicy for handling ADDS, MOVES, and CREATES on a {@link TreeEditPart}.
 * <P>
 * This EditPolicy is responsible for displaying the insertion feedback in the Tree during
 * the appropriate interactions.
 * <P>
 * This EditPolicy factors the {@link #getCommand(Request)} into three different abstract
 * methods which subclasses must implement.
 * @since 2.0 */
public abstract class AbstractTreeContainerEditPolicy 
	extends AbstractEditPolicy 
{

private Timer autoExpandTimer = new Timer();

/**
 * Returns a Command for adding the children to the container.
 * @param request the Request to add. * @return Command <code>null</code> or a Command to perform the add */
protected abstract Command getAddCommand(ChangeBoundsRequest request);

/**
 * Returns a Command for creating the object inside the container.
 * @param request the CreateRequest * @return Command <code>null</code> or a Command to perform the create */
protected abstract Command getCreateCommand(CreateRequest request);

/**
 * Returns a Command for moving the children within the container.
 * @param request the Request to move * @return Command <code>null</code> or a Command to perform the move */
protected abstract Command getMoveChildrenCommand(ChangeBoundsRequest request);

private void eraseAddFeedback(Request req) {
	getTree().setInsertMark(null, true);
}

private void eraseCreateFeedback(Request req) {
	eraseAddFeedback(req);
	autoExpandTimer.cancel();
}

private void eraseMoveFeedback(Request req) {
	eraseAddFeedback(req);
}

/** * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(Request) */
public void eraseTargetFeedback(Request req) {
	if (req.getType().equals(REQ_MOVE))
		eraseMoveFeedback(req);
	if (req.getType().equals(REQ_ADD))
		eraseAddFeedback(req);
	if (req.getType().equals(REQ_CREATE))
		eraseCreateFeedback(req);	    			
}

/**
 * Calculates the index of the TreeItem ata  agiven point.
 * @param pt the Point in the Viewer * @return the index of the TreeItem */
protected final int findIndexOfTreeItemAt(org.eclipse.draw2d.geometry.Point pt) {
	int index = -1;
	TreeItem item = findTreeItemAt(pt);	
	if (item != null) {
		index = getHost().getChildren().indexOf(item.getData());	
		if (index >= 0 && !isInUpperHalf(item.getBounds(), pt))
			index++;
	}
	return index;
}

/**
 * Calculates the <code>TreeItem</code> at a specified {@link
 * org.eclipse.draw2d.geometry.Point}.
 * @param pt the draw2d Point * @return <code>null</code> or the TreeItem */
protected final TreeItem findTreeItemAt(org.eclipse.draw2d.geometry.Point pt) {
	return getTree().getItem(new Point(pt.x, pt.y));			
}

/** * @see org.eclipse.gef.EditPolicy#getCommand(Request) */
public Command getCommand(Request req) {
	if (req.getType().equals(REQ_MOVE_CHILDREN))
		return getMoveChildrenCommand((ChangeBoundsRequest)req);
	if (req.getType().equals(REQ_ADD))
		return getAddCommand((ChangeBoundsRequest)req);
	if (req.getType().equals(REQ_CREATE))
		return getCreateCommand((CreateRequest)req);
		
	return null;
}

/**
 * Returns the host EditPart when appropriate. Targeting is done by checking if the mouse
 * is clearly over the host's TreeItem.
 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(Request) */
public EditPart getTargetEditPart(Request req) {
	if (req.getType().equals(REQ_ADD)
	  || req.getType().equals(REQ_MOVE)
	  || req.getType().equals(REQ_CREATE)) {
		DropRequest drop = (DropRequest) req;
		Point where = new Point(drop.getLocation().x, drop.getLocation().y);
		Widget widget = ((TreeEditPart) getHost()).getWidget();
		if (widget instanceof Tree)
			return getHost();
		TreeItem treeitem = (TreeItem) widget;
		Rectangle bounds = treeitem.getBounds();
		int fudge = bounds.height / 5;
		Rectangle inner =
			new Rectangle(
				bounds.x,
				bounds.y + fudge,
				bounds.width,
				bounds.height - fudge * 2);
		//Point is either outside the Treeitem, or inside the inner Rect.
		if (!bounds.contains(where) || inner.contains(where))
			return getHost();
	}
	
	return null;
}

private Tree getTree() {
	Widget widget = ((TreeEditPart)getHost()).getWidget();
	if (widget instanceof Tree)
		return (Tree)widget;
	else
		return ((TreeItem)widget).getParent();
}

private void insertMarkAfterLastChild(TreeItem[] children) {
	if (children != null && children.length > 0) {
		TreeItem item = children[ children.length - 1 ];
		getTree().setInsertMark(item, false);
	}
}

private boolean isInUpperHalf(Rectangle rect, 
					   org.eclipse.draw2d.geometry.Point pt) {
	Rectangle tempRect = new Rectangle(rect.x, rect.y, 
							rect.width, rect.height / 2);
	return tempRect.contains(new Point(pt.x, pt.y));
}

private void showAddFeedback(Request req) {
	if (!(req instanceof ChangeBoundsRequest))
		return;
		
	ChangeBoundsRequest request = (ChangeBoundsRequest)req;
	Tree tree = getTree();
	org.eclipse.draw2d.geometry.Point pt = request.getLocation();
	TreeItem item = findTreeItemAt(pt);
	if (item == null) {
		Widget widget = ((TreeEditPart)getHost()).getWidget();
		if (widget == tree) {
			insertMarkAfterLastChild(tree.getItems());
		}
	} else if (item == ((TreeEditPart)getHost()).getWidget()) {
		insertMarkAfterLastChild(item.getItems());
	} else {
		boolean before = isInUpperHalf(item.getBounds(), pt);
		tree.setInsertMark(item, before);
	}
}

private void showCreateFeedback(Request req) {
	if (!(req instanceof CreateRequest))
		return;
	
	CreateRequest request = (CreateRequest)req;
	org.eclipse.draw2d.geometry.Point pt = request.getLocation();
	final Tree tree = getTree();
	TreeItem item = findTreeItemAt(pt);
	if (item == null) {
		Widget widget = ((TreeEditPart)getHost()).getWidget();
		if (widget == tree) {
			insertMarkAfterLastChild(tree.getItems());
		}
	} else if (item == ((TreeEditPart)getHost()).getWidget()) {
		insertMarkAfterLastChild(item.getItems());
	} else {
		boolean before = isInUpperHalf(item.getBounds(), pt);
		tree.setInsertMark(item, before);
	}
	
	if (!(autoExpandTimer.hasStopped())) {
		return;
	}
	
	autoExpandTimer = new Timer();
	autoExpandTimer.scheduleRepeatedly(new Runnable() {
		public void run() {
			Display display = tree.getDisplay();
			display.syncExec(new Runnable() {
				public void run() {
					Widget widget = ((TreeEditPart)getHost()).getWidget();
					if (widget instanceof TreeItem)
						((TreeItem)widget).setExpanded(true);
					autoExpandTimer.cancel();
				}
			});
		}
	}, 500, 10000);
}

private void showMoveFeedback(Request req) {
	showAddFeedback(req);
}

/** * @see org.eclipse.gef.EditPolicy#showTargetFeedback(Request) */
public void showTargetFeedback(Request req) {
	if (req.getType().equals(REQ_MOVE))
		showMoveFeedback(req);
	if (req.getType().equals(REQ_ADD))
		showAddFeedback(req);
	if (req.getType().equals(REQ_CREATE))
		showCreateFeedback(req);   			
}

}