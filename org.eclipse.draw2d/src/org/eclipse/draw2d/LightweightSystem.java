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
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The LightweightSystem is the link between SWT and draw2d.
 * It is the component that provides the ability for {@link Figure Figures} to
 * be hosted on an SWT Canvas.
 * 
 * Normal procedure for using a LightweightSystem:
 * 1) Create an SWT Canvas.
 * 2) Create a LightweightSystem passing it that Canvas. 
 * 3) Create a draw2d Figure and call setContents(IFigure).
 *    This Figure will be the top-level Figure of the draw2d
 *    application.
 */
public class LightweightSystem
{

private Canvas canvas;
IFigure contents;
private IFigure root;
private EventDispatcher dispatcher;
private UpdateManager manager = new DeferredUpdateManager();
private Rectangle oldControlSize = new Rectangle();

/**
 * Constructs a LightweightSystem on Canvas <i>c</i>.
 * 
 * @since 2.0
 */
public LightweightSystem (Canvas c){
	this();
	setControl(c);
}

public LightweightSystem(){
	init();
}

/**
 * Adds SWT listeners to the LightWeightSystem's Canvas.
 * This allows for SWT events to be dispatched and handled
 * by its {@link EventDispatcher}. 
 * 
 * @since 2.0
 */
protected void addListeners(){
	EventHandler handler = createEventHandler();
	canvas.getAccessible().addAccessibleListener(handler);
	canvas.getAccessible().addAccessibleControlListener(handler);
	canvas.addMouseListener(handler);
	canvas.addMouseMoveListener(handler);
	canvas.addMouseTrackListener(handler);
	canvas.addKeyListener(handler);
	canvas.addTraverseListener(handler);
	canvas.addFocusListener(handler);
	
	if (SWT.getPlatform().equals("gtk")) { //$NON-NLS-1$
		canvas.addControlListener(new ControlAdapter(){
			public void controlResized(ControlEvent e) {
				LightweightSystem.this.controlResized();
				((Canvas)e.widget).redraw();
			}
		});

		canvas.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event e) {
				Canvas c = (Canvas)e.widget;
				Rectangle client = new Rectangle(c.getClientArea());
				Rectangle clip = new Rectangle(e.gc.getClipping());
				if (clip.equals(client))
					LightweightSystem.this.paint(e.gc);
				else
					c.redraw();
			}
		});
	} else {
		canvas.addControlListener(new ControlAdapter(){
			public void controlResized(ControlEvent e) {
				LightweightSystem.this.controlResized();
			}
		});
		canvas.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event e) {
				LightweightSystem.this.paint(e.gc);
			}
		});
	}

	root.setBounds(oldControlSize);
	getUpdateManager().performUpdate();
	setEventDispatcher(getEventDispatcher());
}

protected void controlResized(){
	Rectangle r = new Rectangle(canvas.getClientArea());
	r.setLocation(0,0);
	root.setBounds(r);
//	manager.addDirtyRegion(root,r);
	root.revalidate();
	manager.performUpdate();
	oldControlSize = r;
}

/**
 * Returns this LightwightSystem's EventDispatcher.
 * 
 * @since 2.0
 */
protected EventDispatcher getEventDispatcher(){
	if (dispatcher == null)
		dispatcher = new SWTEventDispatcher();
	return dispatcher;
}

/**
 * Returns this LightweightSystem's root Figure.
 * 
 * @since 2.0
 */
public IFigure getRootFigure(){
	return root;
}

/**
 * Returns a new instance of this LightweightSystem's.
 * EventHandler.
 * 
 * @since 2.0
 */
final protected EventHandler createEventHandler(){
	return internalCreateEventHandler();
}

protected RootFigure createRootFigure(){
	RootFigure f = new RootFigure();
	f.setOpaque(true);
	f.setLayoutManager(new StackLayout());
	return f;
}

/**
 * Returns this LightweightSystem's UpdateManager.
 * 
 * @since 2.0
 */
public UpdateManager getUpdateManager(){return manager;}

protected void init(){
	setRootPaneFigure(createRootFigure());
}

EventHandler internalCreateEventHandler(){
	return new EventHandler();
}

/**
 * Invokes this LightweightSystem's {@link UpdateManager}
 * to paint this LightweightSystem's Canvas and contents.
 * 
 * @since 2.0
 */
public void paint(GC gc){
	manager.performUpdate(new Rectangle(gc.getClipping()));
}

/**
 * Sets the contents of the LightweightSystem to the passed
 * Figure. This Figure should be the top-level Figure in 
 * a draw2d application.
 * 
 * @since 2.0
 */
public void setContents(IFigure figure){
	if (contents != null)
		root.remove(contents);
	contents = figure;
	root.add(contents);
}

/**
 * Sets the LightweightSystem's control to the
 * passed Canvas.
 * 
 * @since 2.0
 */
public void setControl(Canvas c){
	if (canvas == c)
		return;
	canvas = c;
	getUpdateManager().setGraphicsSource(new BufferedGraphicsSource(canvas));
	controlResized();
	addListeners();
}

/**
 * Sets this LightweightSystem's EventDispatcher to <i>dispatcher</i>.
 * 
 * @since 2.0
 */
public void setEventDispatcher(EventDispatcher dispatcher){
	this.dispatcher = dispatcher;
	dispatcher.setRoot(root);
	dispatcher.setControl(canvas);
}

protected void setRootPaneFigure(RootFigure root){
	getUpdateManager().setRoot(root);
	this.root = root;
}

/**
 * Sets this LightweightSystem's UpdateManager to <i>um</i>.
 * 
 * @since 2.0
 */
public void setUpdateManager(UpdateManager um){
	manager = um;
	manager.setRoot(root);
}

protected class RootFigure
	extends Figure
{

	public Color getBackgroundColor(){
		if (bgColor != null)
			return bgColor;
		if (canvas != null)
			return canvas.getBackground();
		return null;
	}

	public Font getFont(){
		if (font != null)
			return font;
		if (canvas != null)
			return canvas.getFont();
		return null;
	}

	public Color getForegroundColor(){
		if (fgColor != null)
			return fgColor;
		if (canvas != null)
			return canvas.getForeground();
		return null;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#getUpdateManager()
	 */
	public UpdateManager getUpdateManager() {
		return LightweightSystem.this.getUpdateManager();
	}

	public EventDispatcher internalGetEventDispatcher(){
		return dispatcher;
	}
	
	/**
	 * @see org.eclipse.draw2d.Figure#isVisible()
	 */
	public boolean isShowing() {
		return true;
	}
}

protected class EventHandler 
	implements MouseMoveListener, MouseListener, KeyListener, MouseTrackListener,
				TraverseListener, FocusListener, AccessibleListener, AccessibleControlListener
{
	public void focusGained(FocusEvent e){
		getEventDispatcher().dispatchFocusGained(e);
	}
	
	public void focusLost(FocusEvent e){
		getEventDispatcher().dispatchFocusLost(e);
	}

	public void getChild(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getChild(e);
	}

	public void getChildAtPoint(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getChildAtPoint(e);
	}

	public void getChildCount(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getChildCount(e);
	}

	public void getChildren(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getChildren(e);
	}

	public void getDefaultAction(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getDefaultAction(e);
	}

	public void getDescription(AccessibleEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getDescription(e);
	}

	public void getFocus(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getFocus(e);
	}

	public void getHelp(AccessibleEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getHelp(e);
	}

	public void getKeyboardShortcut(AccessibleEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getKeyboardShortcut(e);
	}

	public void getLocation(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getLocation(e);
	}

	public void getName(AccessibleEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getName(e);
	}

	public void getRole(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getRole(e);
	}

	public void getSelection(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getSelection(e);
	}

	public void getState(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getState(e);
	}

	public void getValue(AccessibleControlEvent e) {
		EventDispatcher.AccessibilityDispatcher ad;
		ad = getEventDispatcher().getAccessibilityDispatcher();
		if (ad != null)
			ad.getValue(e);
	}

	public void keyPressed(KeyEvent e){
		getEventDispatcher().dispatchKeyPressed(e);
	}
	
	public void keyReleased(KeyEvent e){
		getEventDispatcher().dispatchKeyReleased(e);
	}

	public void keyTraversed(TraverseEvent e){
		// Only dispatch the tab next and previous events for now
		if(e.detail == SWT.TRAVERSE_TAB_NEXT || 
		   e.detail == SWT.TRAVERSE_TAB_PREVIOUS){
		   	e.doit = true; //SWT : For some reason, this is false by default on a Canvas for TAB_NEXT.
			getEventDispatcher().dispatchKeyTraversed(e);
		}
	}
	
	public void mouseDoubleClick(MouseEvent e){
		getEventDispatcher().dispatchMouseDoubleClicked(e);
	}
		
	public void mouseDown(MouseEvent e){
		getEventDispatcher().dispatchMousePressed(e);
	}
	
	public void mouseEnter(MouseEvent e){
		getEventDispatcher().dispatchMouseEntered(e);
	}

	public void mouseExit(MouseEvent e){
		getEventDispatcher().dispatchMouseExited(e);
	}

	public void mouseHover(MouseEvent e){
		getEventDispatcher().dispatchMouseHover(e);
	}

	public void mouseMove(MouseEvent e){
		getEventDispatcher().dispatchMouseMoved(e);
	}
	
	public void mouseUp(MouseEvent e){
		getEventDispatcher().dispatchMouseReleased(e);
	}
}

}