package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.TreeEditPart;

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

private EventDispatcher dispatcher;

/**
 * The constructor.  It sets up the default root edit part.
 * The setEditor() and either one of createControl() or setControl()
 * methods should be called to completely set up the GEFTreeViewer.
 */
public TreeViewer() {
	dispatcher = new EventDispatcher();
	RootTreeEditPart rep = new RootTreeEditPart();
	setRootEditPart(rep);
	addDragSourceListener(new TreeViewerTransferDragListener(this));
	addDropTargetListener(new TreeViewerTransferDropListener(this));
}

/**
 * Creates the default tree and sets it as the control.
 *
 * @param	parent	The parent Composite for the Control (tree).
 */
public Control createControl(Composite parent) {
	Tree tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
	setControl(tree);
	return tree;
}

/**
 * Returns the <code>Data</code> of the TreeItem at the given point. Returns null if the
 * Point is not on the Control (Tree).  Returns the data of the Tree if there is no
 * TreeItem at the given point. Sub-classes can override this method to respect the
 * request to exclude the Objects in the  given Collection. 
 *
 * @param	pt		The location at which to look for a TreeItem
 * @param	exclude	The collection of EditParts to be excluded.
 */ 
public EditPart findObjectAtExcluding(Point pt, Collection exclude, Conditional condition) {
	if (getControl() == null)
		return null;

	Tree tree = (Tree)getControl();
	Rectangle area = tree.getClientArea();
	if (pt.x < 0 || pt.y < 0 || pt.x >= area.width || pt.y >= area.height)
		return null;
	
	EditPart result = null;
	TreeItem tie = tree.getItem(new org.eclipse.swt.graphics.Point(pt.x, pt.y));

	if (tie != null) {
		result = (EditPart)tie.getData();
	} else {
		result = (EditPart)tree.getData();
	}
	while (result != null) {
		if (!exclude.contains(result)
			&& (condition == null
				|| condition.evaluate(result)))
			return result;
		result = result.getParent();
	}
	return null;
}

protected void fireSelectionChanged() {
	super.fireSelectionChanged();
	showSelectionInTree();
}

/**
 * "Hooks up" a Control, i.e. sets it as the control for the RootTreeEditPart,
 * adds necessary listener for proper operation, etc.
 */
protected void hookControl() {
	if (getControl() == null)
		return;
	
	final Tree tree = (Tree)getControl();
	tree.addFocusListener(dispatcher);
	tree.addMouseListener(dispatcher);
	tree.addMouseMoveListener(dispatcher);
	tree.addKeyListener(dispatcher);
	tree.addMouseTrackListener(dispatcher);
	tree.addSelectionListener(new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			TreeItem[] ties = tree.getSelection();
			Object newSelection[] = new Object[ties.length];
			for (int i = 0; i < ties.length; i++)
				newSelection[i] = ties[i].getData();
			ignore = true;
			setSelection(new StructuredSelection(newSelection));
			ignore = false;
		}
		public void widgetDefaultSelected(SelectionEvent e) {
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
	for (int i = 0; i < selection.size(); i++) {
		TreeEditPart part = (TreeEditPart)selection.get(i);
		if (part.getWidget() instanceof TreeItem)
			treeParts.add(part);
	}
	TreeItem[] treeItems = new TreeItem[treeParts.size()];
	for (int i = 0; i < treeParts.size(); i++) {
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
protected void unhookControl() {
	if (getControl() == null)
		return;
	super.unhookControl();
	// Ideally, you would want to remove the listeners here
	TreeEditPart tep = (TreeEditPart)getRootEditPart();
	tep.setWidget(null);
}

}