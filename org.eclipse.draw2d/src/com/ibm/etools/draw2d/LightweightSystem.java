package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.ibm.etools.draw2d.geometry.*;

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

private Canvas            canvas;
IFigure           contents;
private IFigure           root;
private EventDispatcher   dispatcher;
private UpdateManager     manager;
private Rectangle         oldControlSize = new Rectangle();

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
	canvas.addMouseListener(handler);
	canvas.addMouseMoveListener(handler);
	canvas.addMouseTrackListener(handler);
	canvas.addKeyListener(handler);
	canvas.addTraverseListener(handler);
	canvas.addFocusListener(handler);
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
	setUpdateManager(new DeferredUpdateManager());
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

public void setRootPaneFigure(RootFigure root){
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
	if (root != null)
		root.setUpdateManager(um);
//	if (manager != null)
//		manager.performUpdate();
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
		if (canvas != null)
			return canvas.getForeground();
		return null;
	}

	public EventDispatcher internalGetEventDispatcher(){
		return dispatcher;
	}
	
}

protected class EventHandler 
	implements MouseMoveListener, MouseListener, KeyListener, MouseTrackListener,
				TraverseListener, FocusListener
{
	EventHandler(){}
	
	public void keyPressed(KeyEvent e){
		getEventDispatcher().dispatchKeyPressed(e);
	}
	
	public void keyReleased(KeyEvent e){
		getEventDispatcher().dispatchKeyReleased(e);
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
	
	public void mouseDoubleClick(MouseEvent e){
		getEventDispatcher().dispatchMouseDoubleClicked(e);
	}
		
	public void mouseDown(MouseEvent e){
		getEventDispatcher().dispatchMousePressed(e);
	}
	
	public void mouseUp(MouseEvent e){
		getEventDispatcher().dispatchMouseReleased(e);
	}
	
	public void keyTraversed(TraverseEvent e){
		// Only dispatch the tab next and previous events for now
		if(e.detail == SWT.TRAVERSE_TAB_NEXT || 
		   e.detail == SWT.TRAVERSE_TAB_PREVIOUS){
		   	e.doit = true; //SWT : For some reason, this is false by default on a Canvas for TAB_NEXT.
			getEventDispatcher().dispatchKeyTraversed(e);
		}
	}
	
	public void focusGained(FocusEvent e){
		getEventDispatcher().dispatchFocusGained(e);
	}
	
	public void focusLost(FocusEvent e){
		getEventDispatcher().dispatchFocusLost(e);
	}
}

}