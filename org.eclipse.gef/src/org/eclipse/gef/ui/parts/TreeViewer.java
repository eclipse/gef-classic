package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.internal.Timer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class TreeViewer
	extends AbstractEditPartViewer
{

private boolean ignore = false;

class EventDispatcher
	implements MouseListener, MouseMoveListener, KeyListener, MouseTrackListener, FocusListener
{
	protected static final int ANY_BUTTON = SWT.BUTTON1 | SWT.BUTTON2 | SWT.BUTTON3;

	private boolean dragInProgress = false;
	public void keyPressed(KeyEvent kee){
		getEditDomain().keyDown(kee, TreeViewer.this);
	}
	public void keyReleased(KeyEvent kee){
		getEditDomain().keyUp(kee, TreeViewer.this);
	}
	public void mouseDoubleClick(MouseEvent me){
		getEditDomain().mouseDoubleClick(me, TreeViewer.this);
	}
	public void mouseDown(MouseEvent me){
		dragInProgress = true;				 
		getEditDomain().mouseDown(me, TreeViewer.this);
	}
	public void mouseEnter(MouseEvent me) {
		getEditDomain().viewerEntered(me, TreeViewer.this);
	}
	public void mouseExit(MouseEvent me) {
		//me.x = -1;
		getEditDomain().viewerExited(me, TreeViewer.this);
		mouseMove(me);
	}
	public void mouseHover(MouseEvent me) {
		getEditDomain().mouseHover(me, TreeViewer.this);
	}
	public void mouseMove(MouseEvent me){
		if ((me.stateMask & ANY_BUTTON) != 0)
			getEditDomain().mouseDrag(me, TreeViewer.this);
		else
			getEditDomain().mouseMove(me, TreeViewer.this);
	}
	public void mouseUp(MouseEvent me){
		dragInProgress = false;
		getEditDomain().mouseUp(me, TreeViewer.this);
	}
	public void focusGained(FocusEvent event) {
		getEditDomain().focusGained(event, TreeViewer.this);
	}
	public void focusLost(FocusEvent event) {
		getEditDomain().focusLost(event, TreeViewer.this);
	}
}

class BehaviourCustomizer
	extends MouseTrackAdapter
	implements MouseListener, MouseMoveListener
{
	int autoScrollPixelLength = 6;
	boolean top, bottom, dragInProgress, expansionInProgress;
	final private Tree tree = (Tree)getControl();
	final private ScrollBar hBar = tree.getHorizontalBar();
	final private ScrollBar vBar = tree.getVerticalBar();
	final private Display display = tree.getDisplay();
	private Timer topTimer = new Timer();
	private Timer bottomTimer = new Timer();
	private Timer autoExpandTimer = new Timer();
	private TreeItem itemToBeExpanded;
	
	private TreeItem getNextItem(TreeItem item, 
						boolean includeChildren){
		if (item == null) {
			return null;
		}
		if (includeChildren && item.getExpanded()) {
			TreeItem[] children = item.getItems();
			if (children != null && children.length > 0) {
				return children[0];
			}
		}
		TreeItem parent = item.getParentItem();
		TreeItem[] siblings;
		if (parent == null) {
			siblings = tree.getItems();
		} else {
			siblings = parent.getItems();
		}
		if (siblings != null && siblings.length <= 1) {
			return getNextItem(parent, false);
		}
		for (int i = 0; i < siblings.length; i++) {
			if (siblings[i] == item && i < (siblings.length - 1)) {
				return siblings[i+1];
			}
		}
		return getNextItem(parent, false);
	}
	private TreeItem getPreviousItem(TreeItem item) {
		TreeItem parent = item.getParentItem();
		TreeItem[] siblings;
		if (parent == null) {
			siblings = tree.getItems();
		} else {
			siblings = parent.getItems();
		}
		if (siblings.length == 0 || siblings[0] == item) {
			return parent;
		}
		TreeItem previous = siblings[0];
		for (int i = 1; i < siblings.length; i++) {
			if (siblings[i] == item) {
				return rightMostVisibleDescendent(previous);
			}
			previous = siblings[i];
		}
		return null;
	}	
	private void launchAutoExpandTimer(){
		if(itemToBeExpanded == null)
			return;
		expansionInProgress = true;
		autoExpandTimer = new Timer();
		autoExpandTimer.scheduleRepeatedly(new Runnable(){
			public void run(){
				display.syncExec(new Runnable(){
					public void run(){
						if(itemToBeExpanded != null)
							itemToBeExpanded.setExpanded(true);
						stopAutoExpandTimer();
					}
				});
			}
		}, 500, 100000);
	}
	private void launchBottomTimer(){
		if(vBar == null || !vBar.isEnabled() || bottom)
			return;
		bottomTimer = new Timer();
		bottomTimer.scheduleRepeatedly(new Runnable(){
			public void run(){
				display.syncExec(new Runnable(){ 
					public void run(){
						scrollDown();
					} 
				});
			}
		}, 400, 300);
		bottom = true;
	}
	private void launchTopTimer(){
		if(vBar == null || !vBar.isEnabled() || top)
			return;
		topTimer = new Timer();
		topTimer.scheduleRepeatedly(new Runnable(){
			public void run(){
				display.syncExec(new Runnable(){ 
					public void run(){
						scrollUp();
					} 
				});
			}
		}, 400, 300);
		top = true;
	}		
	public void mouseDoubleClick(MouseEvent me){
	}
	public void mouseDown(MouseEvent me){
		getControl().setFocus();
		dragInProgress = true;
	}
	public void mouseEnter(MouseEvent me) {
		if ((me.stateMask & (SWT.BUTTON1 | SWT.BUTTON2 | SWT.BUTTON3)) != 0) {
			getControl().setFocus();
			dragInProgress = true;
		}
	}
	public void mouseExit(MouseEvent me){
		stopBottomTimer();
		stopTopTimer();
		stopAutoExpandTimer();
		mouseUp(me);
	}
	public void mouseMove(MouseEvent me){
		Rectangle area = ((Tree)getControl()).getClientArea();
		if (area.width < (2 * autoScrollPixelLength) || 
		    area.height < (2 * autoScrollPixelLength))
			return;
		org.eclipse.swt.graphics.Point point = 
				new org.eclipse.swt.graphics.Point(me.x, me.y);
		Rectangle topEdge = new Rectangle(area.x, area.y, 
				area.width, autoScrollPixelLength);
		Rectangle bottomEdge = new Rectangle(area.x, 
				area.y + area.height - autoScrollPixelLength, 
				area.width, autoScrollPixelLength);
		if (dragInProgress) {
			if (topEdge.contains(point))
				launchTopTimer();
			else 
				stopTopTimer();
			if (bottomEdge.contains(point))
				launchBottomTimer();
			else
				stopBottomTimer();
			if (expansionInProgress){
				if (itemToBeExpanded != tree.getItem(point)){
					stopAutoExpandTimer();
					itemToBeExpanded = tree.getItem(point);
					launchAutoExpandTimer();
				}
			} else {
				itemToBeExpanded = tree.getItem(point);
				launchAutoExpandTimer();
			}
		} else {
			stopAutoExpandTimer();
			stopTopTimer();
			stopBottomTimer();
		}
	}
	public void mouseUp(MouseEvent me){
		dragInProgress = false;
		stopAutoExpandTimer();
		stopTopTimer();
		stopBottomTimer();
	}
	private TreeItem rightMostVisibleDescendent(TreeItem item) {
		TreeItem[] children = item.getItems();
		if (item.getExpanded() && children != null && children.length > 0) {
			return rightMostVisibleDescendent(children[children.length-1]);
		} else {
			return item;
		}
	}
	private void scrollDown(){
		int xInterval = 10;
		int yPoint = tree.getClientArea().height - 1;
		int width  = tree.getClientArea().width;
		int height = tree.getClientArea().height;
		org.eclipse.swt.graphics.Point point =
			new org.eclipse.swt.graphics.Point(xInterval, yPoint);
		TreeItem item = tree.getItem(point);
		while(item == null && point.y < height){
			while(item == null && point.x < width){
				point.x += xInterval;
				item = tree.getItem(point);
			}
			point.y -= 1;
			point.x = xInterval;
		}
		item = getNextItem(item, true);
		if(item != null && bottom)
			tree.showItem(item);		
	}
	private void scrollUp(){
		int xInterval = 10;
		int yPoint = 1;
		int width  = tree.getClientArea().width;
		int height = tree.getClientArea().height;
		org.eclipse.swt.graphics.Point point =
			new org.eclipse.swt.graphics.Point(xInterval, yPoint);
		TreeItem item = tree.getItem(point);
		while(item == null && point.y < height){
			while(item == null && point.x < width){
				point.x += xInterval;
				item = tree.getItem(point);
			}
			point.y += 1;
			point.x = xInterval;
		}
		item = getPreviousItem(item);
		if(item != null && top)
			tree.showItem(item);
	}
	private void stopAutoExpandTimer(){
		autoExpandTimer.cancel();
		expansionInProgress = false;
		itemToBeExpanded = null;
	}
	private void stopBottomTimer(){
		bottomTimer.cancel();
		bottom = false;
	}
	private void stopTopTimer(){
		topTimer.cancel();
		top = false;
	}
}

private EventDispatcher dispatcher;

/**
 * The constructor.  It sets up the default root edit part.
 * The setEditor() and either one of createControl() or setControl()
 * methods should be called to completely set up the GEFTreeViewer.
 */
public TreeViewer(){
	dispatcher = new EventDispatcher();
	RootTreeEditPart rep = new RootTreeEditPart();
	setRootEditPart(rep);
}

/**
 * Creates the default tree and sets it as the control.
 *
 * @param	parent	The parent Composite for the Control (tree).
 */
public Control createControl(Composite parent){
	Tree tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | 
                                    SWT.V_SCROLL);
	setControl(tree);
	return tree;
}

/**
 * Returns the <code>Data</code> of the TreeItem at the given point.
 * Returns null if the Point is not on the Control (Tree).  Returns
 * the data of the Tree if there is no TreeItem at the given point.
 *
 * @param	pt	The location at which to look for a TreeItem
 */ 
public EditPart findObjectAt(Point pt){
	return findObjectAtExcluding(pt, Collections.EMPTY_SET);
}

/**
 * Returns the <code>Data</code> of the TreeItem at the given point.
 * Returns null if the Point is not on the Control (Tree).  Returns
 * the data of the Tree if there is no TreeItem at the given point.
 * At this point, the collection is ignored.  Sub-classes can override
 * this method to respect the request to exclude the Objects in the 
 * given Collection. 
 *
 * @param	pt		The location at which to look for a TreeItem
 * @param	exclude	The collection of EditParts to be excluded.
 *				NOTE: The current implementation ignores this Collection.
 */ 
public EditPart findObjectAtExcluding(Point pt, Collection exclude){
	if(getControl() == null)
		return null;

	Tree tree = (Tree)getControl();
	Rectangle area = tree.getClientArea();
	if(pt.x < 0 || pt.y < 0 || 
	    pt.x >= area.width || pt.y >= area.height){
		return null;
	}
	
	EditPart result = null;
	TreeItem tie = tree.getItem(new org.eclipse.swt.graphics.Point(pt.x, pt.y));

	if (tie != null){
		result = (EditPart)tie.getData();
	} else {
		result = (EditPart)tree.getData();
	}
	return exclude.contains(result) ? null : result;
}

protected void fireSelectionChanged(){
	super.fireSelectionChanged();
	showSelectionInTree();
}

/**
 * "Hooks up" a Control, i.e. sets it as the control for the RootTreeEditPart,
 * adds necessary listener for proper operation, etc.
 */
protected void hookControl(){
	if (getControl() == null)
		return;
		  
	BehaviourCustomizer customizer = new BehaviourCustomizer();
	final Tree tree = (Tree)getControl();
	tree.addFocusListener(dispatcher);
	tree.addMouseListener(customizer);
	tree.addMouseMoveListener(customizer);
	tree.addMouseTrackListener(customizer);
	tree.addMouseListener(dispatcher);
	tree.addMouseMoveListener(dispatcher);
	tree.addKeyListener(dispatcher);
	tree.addMouseTrackListener(dispatcher);
	tree.addSelectionListener(new SelectionListener(){
		public void widgetSelected(SelectionEvent e){
			TreeItem[] ties = tree.getSelection();
			Object newSelection[] = new Object[ties.length];
			for(int i = 0; i < ties.length; i++)
				newSelection[i] = ties[i].getData();
			ignore = true;
			setSelection(new StructuredSelection(newSelection));
			ignore = false;
		}
		public void widgetDefaultSelected(SelectionEvent e){
			widgetSelected(e);
		}	
	}); 
	TreeEditPart tep = (TreeEditPart)getRootEditPart();
	tep.setWidget(tree);
	super.hookControl();
}

private void showSelectionInTree() {
	if (ignore || getControl()==null || getControl().isDisposed())
		return;
	List selection = getSelectedEditParts();
	Tree tree = (Tree)getControl();
	List treeParts = new ArrayList();
	for (int i=0; i<selection.size(); i++) {
		TreeEditPart part = (TreeEditPart)selection.get(i);
		if (part.getWidget() instanceof TreeItem)
			treeParts.add(part);
	}
	TreeItem[] treeItems = new TreeItem[treeParts.size()];
	for (int i=0; i<treeParts.size(); i++) {
		TreeEditPart part = (TreeEditPart)treeParts.get(i);
		treeItems[i] = (TreeItem)part.getWidget();
	}
	tree.setSelection(treeItems);
}

/**
 * Unhooks a control so that it can be reset.  This method deactivates
 * the contents, removes the Control as being the Control of the 
 * RootTreeEditPart, etc.  It does not remove the listeners because
 * it is causing errors, although that would be a desirable outcome. 
 */
protected void unhookControl(){
	if (getControl() == null)
		return;
	super.unhookControl();
	// Ideally, you would want to remove the listeners here
	TreeEditPart tep = (TreeEditPart)getRootEditPart();
	tep.setWidget(null);
}

}