package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.accessibility.*;

import org.eclipse.draw2d.EventDispatcher.AccessibilityDispatcher;
import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.internal.Draw2dMessages;

/**
 * The SWTEventDispatcher provides draw2d with the ability to 
 * dispatch SWT Events. The {@link org.eclipse.draw2d.LightweightSystem}
 * adds SWT event listeners on its Canvas. When the Canvas receives
 * an SWT event, it calls the appropriate dispatcher method in 
 * SWTEventDispatcher. 
 */
public class SWTEventDispatcher
	extends EventDispatcher
{

private final static boolean DEBUG = false;

protected static final int ANY_BUTTON = SWT.BUTTON1 | SWT.BUTTON2 | SWT.BUTTON3;

private boolean figureTraverse = true;

private boolean captured;
private IFigure root;
private IFigure mouseTarget;
private IFigure cursorTarget;
private IFigure focusOwner;
private IFigure hoverSource;

private MouseEvent currentEvent;
private Cursor cursor;
protected org.eclipse.swt.widgets.Control control;

private ToolTipHelper toolTipHelper;
private FocusTraverseManager focusManager = new FocusTraverseManager();

protected class FigureAccessibilityDispatcher
	extends AccessibilityDispatcher
{
	public void getChildAtPoint(AccessibleControlEvent e) {}
	public void getChildCount(AccessibleControlEvent e) {}
	public void getChildren(AccessibleControlEvent e) {}
	public void getDefaultAction(AccessibleControlEvent e) {}
	public void getDescription(AccessibleEvent e) {}
	public void getFocus(AccessibleControlEvent e) {}
	public void getHelp(AccessibleEvent e) {}
	public void getKeyboardShortcut(AccessibleEvent e) {}
	public void getLocation(AccessibleControlEvent e) {}
	public void getName(AccessibleEvent e) {}
	public void getRole(AccessibleControlEvent e) {}
	public void getSelection(AccessibleControlEvent e) {}
	public void getState(AccessibleControlEvent e) {}
	public void getValue(AccessibleControlEvent e) {}
}

public void dispatchFocusGained(org.eclipse.swt.events.FocusEvent e){
	IFigure currentFocusOwner = getFocusTraverseManager().getCurrentFocusOwner();

	/*
	 * Upon focus gained, if there is no current focus owner,
	 * set focus on first focusable child. 
	 */
	if(currentFocusOwner == null)
		currentFocusOwner = getFocusTraverseManager().getNextFocusableFigure(root,focusOwner);
	setFocus(currentFocusOwner);
}

public void dispatchFocusLost(org.eclipse.swt.events.FocusEvent e){
	setFocus(null);
}

public void dispatchKeyPressed(org.eclipse.swt.events.KeyEvent e){
	if (focusOwner != null){
		KeyEvent event = new KeyEvent(this, focusOwner, e);
		focusOwner.handleKeyPressed(event);
	}
}

public void dispatchKeyReleased(org.eclipse.swt.events.KeyEvent e){
	if (focusOwner != null){
		KeyEvent event = new KeyEvent(this, focusOwner, e);
		focusOwner.handleKeyReleased(event);
	}
}

public void dispatchKeyTraversed(TraverseEvent e){
	if (!figureTraverse)
		return;
	IFigure nextFigure = null;
	
	if(e.detail == SWT.TRAVERSE_TAB_NEXT)
		nextFigure = getFocusTraverseManager().getNextFocusableFigure(root,focusOwner);
	else if(e.detail == SWT.TRAVERSE_TAB_PREVIOUS)
		nextFigure = getFocusTraverseManager().getPreviousFocusableFigure(root,focusOwner);

	if(nextFigure == null)
		e.doit = true;	
	else{
		e.doit = false;	
		setFocus(nextFigure);
	}
}

public void dispatchMouseHover(org.eclipse.swt.events.MouseEvent me){
	receive(me);
	if(mouseTarget != null)
		mouseTarget.handleMouseHover(currentEvent);		
	/*
	 * Check Tooltip source.
	 * Get Tooltip source's Figure.
	 * Set that tooltip as the lws contents on the helper.
	 */
	if(hoverSource != null){
		toolTipHelper = getToolTipHelper();
		IFigure tip = hoverSource.getToolTip();
		Control control = (Control)me.getSource();
		org.eclipse.swt.graphics.Point absolute;
		absolute = control.toDisplay(new org.eclipse.swt.graphics.Point(me.x, me.y));
		toolTipHelper.displayToolTipNear(hoverSource, tip, absolute.x, absolute.y);			
	}
}

public void dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent me){
	receive(me);
	if (mouseTarget != null)
		mouseTarget.handleMouseDoubleClicked(currentEvent);
}

public void dispatchMouseEntered(org.eclipse.swt.events.MouseEvent me) {
	receive(me);
}

public void dispatchMouseExited(org.eclipse.swt.events.MouseEvent me) {
	setHoverSource(null, me);
	if (mouseTarget != null){
		currentEvent = new MouseEvent(me.x, me.y, this, mouseTarget, me.button, me.stateMask);
		mouseTarget.handleMouseExited(currentEvent);
	}
	mouseTarget = null;
}

public void dispatchMousePressed(org.eclipse.swt.events.MouseEvent me){
	receive(me);
	if (mouseTarget != null){
		if (DEBUG)
			System.out.println("Pressed:\n\t" + currentEvent);//$NON-NLS-1$
		mouseTarget.handleMousePressed(currentEvent);
		if (currentEvent.isConsumed())
			setCapture(mouseTarget);
	}
}

public void dispatchMouseMoved(org.eclipse.swt.events.MouseEvent me){
	receive(me);
	if (mouseTarget != null)
		if ((me.stateMask & ANY_BUTTON)!= 0){
			if (DEBUG)
				System.out.println("Drag:\n\t" + currentEvent);//$NON-NLS-1$
			mouseTarget.handleMouseDragged(currentEvent);
		}
		else {
			if (DEBUG)
				System.out.println("Move:\n\t" + currentEvent);//$NON-NLS-1$
			mouseTarget.handleMouseMoved(currentEvent);
		}
}

public void dispatchMouseReleased(org.eclipse.swt.events.MouseEvent me){
	receive(me);
	if (mouseTarget != null){
		if (DEBUG)
			System.out.println("Released:\n\t" + currentEvent);//$NON-NLS-1$
		mouseTarget.handleMouseReleased(currentEvent);
	}
	releaseCapture();
	receive(me);
}

protected AccessibilityDispatcher getAccessibilityDispatcher() {
	return null;
}

protected MouseEvent getCurrentEvent(){return currentEvent;}

private IFigure getCurrentToolTip(){
	if(hoverSource != null)
		return hoverSource.getToolTip();
	else
		return null;	
}

protected IFigure getCursorTarget(){return cursorTarget;}

protected ToolTipHelper getToolTipHelper(){
	if(toolTipHelper == null)
		toolTipHelper = new ToolTipHelper(control);
	return toolTipHelper;
}

final protected FocusTraverseManager getFocusTraverseManager(){
	if(focusManager == null){
		focusManager = new FocusTraverseManager();
	}	
	return focusManager;
}				

/*package*/ IFigure getFocusOwner(){return focusOwner;}
protected IFigure getMouseTarget(){return mouseTarget;}
protected IFigure getRoot(){return root;}

public boolean isCaptured(){return captured;}

private void receive(org.eclipse.swt.events.MouseEvent me){
	updateFigureUnderCursor(me);
	int state = me.stateMask;
	if (captured){
		if (mouseTarget != null)
			currentEvent = new MouseEvent(me.x, me.y, this, mouseTarget, me.button, state);
	} else {
		IFigure f = root.findMouseEventTargetAt(me.x, me.y);
		if (f == mouseTarget){
			if (mouseTarget != null)
				currentEvent = new MouseEvent(me.x, me.y, this, mouseTarget, me.button, state);
			return;
		}
		if (mouseTarget != null){
			currentEvent = new MouseEvent(me.x, me.y, this, mouseTarget, me.button, state);
			if (DEBUG)
				System.out.println("Exit:\n\t" + currentEvent);//$NON-NLS-1$
			mouseTarget.handleMouseExited(currentEvent);
		}
		setMouseTarget(f);
		if (mouseTarget != null){
			currentEvent = new MouseEvent(me.x, me.y, this, mouseTarget, me.button, state);
			if (DEBUG)
				System.out.println("Enter:\n\t" + currentEvent);//$NON-NLS-1$
			mouseTarget.handleMouseEntered(currentEvent);
		}
	}
}

protected void releaseCapture(){
	captured = false;
}

public void requestFocus(IFigure fig){
	setFocus(fig);
}

public void requestRemoveFocus(IFigure fig){
	if (getFocusOwner() == fig){
		setFocus(null);
	}
	if (mouseTarget == fig)
		mouseTarget = null;
	if (cursorTarget == fig)
		cursorTarget = null;
	if (hoverSource == fig)
		hoverSource = null;
	getFocusTraverseManager().setCurrentFocusOwner(null);
}

protected void setCapture(IFigure figure){
	captured = true;
	mouseTarget = figure;
}

public void setControl(Control c){
	if (control != null)
		throw new RuntimeException(Draw2dMessages.ERR_SWTEventDispatcher_SetControl_Exception_Runtime);
	if (c != null)
		c.addDisposeListener(new org.eclipse.swt.events.DisposeListener(){
			public void widgetDisposed(DisposeEvent e){
				if (toolTipHelper != null)
					toolTipHelper.dispose();
			}
		});
	control = c;
}

protected void setCursor(Cursor c){
	if(c == null && cursor == null){
		return;
	} else if((c != cursor) || (!c.equals(cursor))){
		cursor = c;
		if (control != null && !control.isDisposed())
			control.setCursor(c);
	}
}

public void setEnableKeyTraversal(boolean traverse){
	figureTraverse = traverse;
}

protected void setFigureUnderCursor(IFigure f){
	if (cursorTarget == f)
		return;
	cursorTarget = f;
	updateCursor();
}

protected void setFocus(IFigure fig){
	FocusEvent fe = new FocusEvent(focusOwner, fig);
	if(focusOwner != null)
		focusOwner.handleFocusLost(fe);
	focusOwner = fig;
	if(focusOwner != null)
		focusOwner.handleFocusGained(fe);
	if(fig != null)
		getFocusTraverseManager().setCurrentFocusOwner(fig);
}

protected void setHoverSource(Figure figure,org.eclipse.swt.events.MouseEvent me){
	hoverSource = figure;
	if(figure != null){
		Control control = (Control)me.getSource();
		org.eclipse.swt.graphics.Point absolute;
		absolute = control.toDisplay(new org.eclipse.swt.graphics.Point(me.x, me.y));
		toolTipHelper = getToolTipHelper();
		toolTipHelper.updateToolTip(hoverSource, getCurrentToolTip(), absolute.x, absolute.y);	
	} else if (toolTipHelper != null) {
		// Update with null to clear hoverSource in ToolTipHelper
		toolTipHelper.updateToolTip(hoverSource, getCurrentToolTip(), me.x, me.y);
	}
}	

protected void setMouseTarget(IFigure figure){
	mouseTarget = figure;
}

public void setRoot(IFigure figure){
	root = figure;
}

protected void updateCursor(){
	Cursor newCursor = null;
	if(cursorTarget != null)
		newCursor = cursorTarget.getCursor();
	setCursor(newCursor);
}

protected void updateFigureUnderCursor(org.eclipse.swt.events.MouseEvent me){
	if(!captured){
		IFigure f = root.findFigureAt(me.x, me.y);
		setFigureUnderCursor(f);
		if((Figure)cursorTarget != hoverSource)
			updateHoverSource(me);
	}
}

protected void updateHoverSource(org.eclipse.swt.events.MouseEvent me){
	/* 
	 * Derive source from figure under cursor.
	 * Set the source in setHoverSource();
	 * If figure.getToolTip() is null, get parents toolTip
	 * Continue parent traversal until a toolTip is found or root is reached.
	 */
	if(cursorTarget != null){
		boolean sourceFound = false;
		Figure source = (Figure)cursorTarget;
		while( sourceFound == false && source.getParent() != null ){
			if(source.getToolTip() != null)
				sourceFound = true;
			else
				source = (Figure)source.getParent();		
		}
		setHoverSource(source,me);							
	} else {
		setHoverSource(null,me);
	}	
}

}