package org.eclipse.gef.ui.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */
 
import java.util.*;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;

public class DomainEventDispatcher
	extends SWTEventDispatcher
{

protected EditDomain domain;
protected EditPartViewer viewer;
private IFigure translationFigure;
private Point currentEventPoint; 
private boolean editorCaptured = false;
private Cursor overrideCursor;
private Map accessibles = new HashMap();
private EditPartAccessibilityDispatcher accessibilityDispatcher;

protected class EditPartAccessibilityDispatcher
	extends AccessibilityDispatcher
{
	private AccessibleEditPart get(int childID){
		if (childID == ACC.CHILDID_SELF
			|| childID == ACC.CHILDID_NONE)
			return (AccessibleEditPart)getViewer().getContents().getAdapter(AccessibleEditPart.class);
		return (AccessibleEditPart)accessibles.get(new Integer(childID));
	}
	
	public void getChildAtPoint(AccessibleControlEvent e) {
		org.eclipse.swt.graphics.Point p = new org.eclipse.swt.graphics.Point(e.x, e.y);
		p = getViewer().getControl().toControl(p);
		EditPart part = getViewer().findObjectAt(new Point(p.x, p.y));
		if (part == null)
			return;
		AccessibleEditPart acc = (AccessibleEditPart)
			part.getAdapter(AccessibleEditPart.class);
		if (acc != null)
			e.childID = acc.getAccessibleID();
	}

	public void getChildCount(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getChildCount(e);
	}
	
	public void getChildren(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getChildren(e);
	}
	
	public void getDefaultAction(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getDefaultAction(e);
	}
	
	public void getDescription(AccessibleEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getDescription(e);
	}
	
	public void getFocus(AccessibleControlEvent e) {
		AccessibleEditPart acc = (AccessibleEditPart)
			getViewer().getFocusEditPart().getAdapter(AccessibleEditPart.class);
		if (acc != null)
			e.childID = acc.getAccessibleID();
	}
	
	public void getHelp(AccessibleEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getHelp(e);
	}
	
	public void getKeyboardShortcut(AccessibleEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getKeyboardShortcut(e);
	}
	
	public void getLocation(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getLocation(e);
	}
	
	public void getName(AccessibleEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getName(e);
	}
	
	public void getRole(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getRole(e);
	}
	
	public void getSelection(AccessibleControlEvent e) {}
	
	public void getState(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getState(e);
	}
	
	public void getValue(AccessibleControlEvent e) {
		AccessibleEditPart acc = get(e.childID);
		if (acc != null)
			acc.getValue(e);
	}
}

public DomainEventDispatcher(EditDomain d, EditPartViewer v) {
	domain = d;
	viewer = v;
	setEnableKeyTraversal(false);
}

public void dispatchFocusGained(FocusEvent event) {
	super.dispatchFocusGained(event);
	domain.focusGained(event, viewer);
}

public void dispatchFocusLost(FocusEvent event) {
	super.dispatchFocusLost(event);
	domain.focusLost(event, viewer);
}

public void dispatchKeyPressed(org.eclipse.swt.events.KeyEvent e) { 
	if (!editorCaptured){
		super.dispatchKeyPressed(e);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch())
		domain.keyDown(e, viewer);
}

public void dispatchKeyReleased(org.eclipse.swt.events.KeyEvent e) { 
	if (!editorCaptured){
		super.dispatchKeyReleased(e);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) 
		domain.keyUp(e, viewer);
}

public void dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseDoubleClicked(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch())
		domain.mouseDoubleClick(me, viewer);
}

public void dispatchMouseEntered(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseEntered(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) {
		domain.viewerEntered(me, viewer);
	}
}

public void dispatchMouseExited(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseExited(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) {
		domain.viewerExited(me, viewer);
	}
}

public void dispatchMouseHover(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseHover(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch())
		domain.mouseHover(me, viewer);
}

public void dispatchMousePressed(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMousePressed(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) {
		setFocus(null);
		control.forceFocus();
		setRouteEventsToEditor(true);
		domain.mouseDown(me, viewer);
	}
}

public void dispatchMouseMoved(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseMoved(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) {
		if ((me.stateMask & InputEvent.ANY_BUTTON) != 0)
			domain.mouseDrag(me, viewer);
		else
			domain.mouseMove(me, viewer);
	}
}

public void dispatchMouseReleased(org.eclipse.swt.events.MouseEvent me){
	if (!editorCaptured){
		super.dispatchMouseReleased(me);
		if (draw2dBusy())
			return;
	}
	if (okToDispatch()) {
		setRouteEventsToEditor(false);
		domain.mouseUp(me, viewer);
	}
}

private boolean draw2dBusy(){
	if (getCurrentEvent() != null)
		if (getCurrentEvent().isConsumed())
			return true;
	if (isCaptured())
		return true;
	return false;
}

protected AccessibilityDispatcher getAccessibilityDispatcher() {
	if (accessibilityDispatcher == null)
		accessibilityDispatcher = new EditPartAccessibilityDispatcher();
	return accessibilityDispatcher;
}

protected final EditPartViewer getViewer(){
	return viewer;
}

public void nativeDragStarted(DragSourceEvent event, AbstractEditPartViewer viewer) {
	releaseCapture();
	setRouteEventsToEditor(false);
}

private boolean okToDispatch() {
	return domain != null;
}

void putAccessible(AccessibleEditPart acc){
	accessibles.put(new Integer(acc.getAccessibleID()), acc);
}

void removeAccessible(AccessibleEditPart acc){
	accessibles.remove(new Integer(acc.getAccessibleID()));
}

protected void setCapture(IFigure figure){
	super.setCapture(figure);
	if (figure == null){
		releaseCapture();
		setRouteEventsToEditor(true);
	}
}

protected void setCursor( Cursor newCursor ){
	if( overrideCursor == null )
		super.setCursor( newCursor );
	else
		super.setCursor( overrideCursor );
}

public void setRouteEventsToEditor(boolean value) {
	editorCaptured = value;
}

public void setOverrideCursor( Cursor newCursor ){
	if (overrideCursor == newCursor)
		return;
	overrideCursor = newCursor;
	if( overrideCursor == null )
		updateCursor();
	else
		setCursor( overrideCursor );
}

}