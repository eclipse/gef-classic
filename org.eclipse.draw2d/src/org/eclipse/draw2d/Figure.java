package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import java.beans.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.draw2d.internal.Draw2dMessages;

/**
 * The base implementation for graphical figures.
 */
public class Figure
	implements IFigure
{

private static Rectangle PRIVATE_RECT = new Rectangle();
private static final int
	FLAG_VALID = 1,
	FLAG_OPAQUE = 1 << 1,
	FLAG_VISIBLE = 1 << 2,
	FLAG_FOCUSABLE = 1 << 3,
	FLAG_ENABLED = 1 << 4,
	FLAG_FOCUS_TRAVERSABLE = 1 << 5;
	
protected static int
	MAX_FLAG = FLAG_FOCUS_TRAVERSABLE;

protected Rectangle bounds = new Rectangle(0,0,64,36);

private LayoutManager layoutManager;

protected int flags = FLAG_VISIBLE | FLAG_ENABLED;

private IFigure parent;
private Cursor cursor;
private UpdateManager updateManager;

private PropertyChangeSupport propertyListeners;
private EventListenerList eventListeners = new EventListenerList();

/*package*/ List children = Collections.EMPTY_LIST;

protected Dimension
	prefSize,
	minSize,
	maxSize;

protected Font font;
protected Color	bgColor;
protected Color fgColor;

protected Border border;

private AncestorHelper ancestorHelper;

protected IFigure toolTip;

/**
 * Adds the given figure as a child of this figure, with 
 * the given constraint.
 * 
 * @since 2.0
 */
final public void add(IFigure figure, Object constraint){add(figure, constraint, -1);}

/**
 * Adds the given figure as a child of this figure at the
 * given index, with the given constraint.
 * 
 * @since 2.0
 */
public void add(IFigure figure, Object constraint, int index){
	if (children == Collections.EMPTY_LIST)
		children = new ArrayList(2);
	if (index < -1 || index > children.size())
		throw new IndexOutOfBoundsException(Draw2dMessages.ERR_Figure_Add_Exception_OutOfBounds);

//Check for Cycle in heirarchy
	for (IFigure f = this; f != null; f = f.getParent())
		if (figure == f)
			throw new IllegalArgumentException(Draw2dMessages.ERR_Figure_Add_Exception_IllegalArgument);

//Detach the child from previous parent
	if (figure.getParent() != null)
		figure.getParent().remove(figure);

	if (index == -1)
		children.add(figure);
	else
		children.add(index,figure);

//Add to layout manager
	if (getLayoutManager() != null)
		getLayoutManager().setConstraint(figure, constraint);
	// The updates in the UpdateManager *have* to be
	// done asynchronously, else will result in 
	// incorrect dirty region corrections.
	revalidate();
	repaint(figure.getBounds());

//Parent the figure
	figure.setParent(this);
	figure.addNotify();
	figure.repaint();
}

/**
 * Adds the given Figure as a child of this Figure.
 * 
 * @since 2.0
 */
final public void add(IFigure figure){add(figure,null,-1);}

/**
 * Adds the given Figure as a child of this Figure at the
 * given index.
 * 
 * @since 2.0
 */
final public void add(IFigure figure, int index){add(figure, null, index);}

public void addAncestorListener(AncestorListener ancestorListener){
	if (ancestorHelper==null)
		ancestorHelper=new AncestorHelper(this);
	ancestorHelper.addAncestorListener(ancestorListener);
}

public void addFigureListener(FigureListener listener){
	eventListeners.addListener(FigureListener.class, listener);
}

public void addFocusListener(FocusListener listener){
	eventListeners.addListener(FocusListener.class, listener);
}

public void addKeyListener(KeyListener listener){
	eventListeners.addListener(KeyListener.class,listener);
}	

protected void addListener(Class clazz, Object listener){
	if (eventListeners == null)
		eventListeners = new EventListenerList();
	eventListeners.addListener(clazz, listener);
}

public void addMouseListener(MouseListener l){
	eventListeners.addListener(MouseListener.class, l);
}

public void addMouseMotionListener(MouseMotionListener l){
	eventListeners.addListener(MouseMotionListener.class, l);
}

/**
 * Called after the receiver's parent has been set and it 
 * has been added to its parent.
 * 
 * @since 2.0
 */
public void addNotify(){
	for (int i=0; i<children.size(); i++)
		((IFigure)children.get(i)).addNotify();
}

public void addPropertyChangeListener(String property, PropertyChangeListener listener){
	if (propertyListeners == null)
		propertyListeners = new PropertyChangeSupport(this);
	propertyListeners.addPropertyChangeListener(property, listener);
}

public void addPropertyChangeListener(PropertyChangeListener l){
	if (propertyListeners == null)
		propertyListeners = new PropertyChangeSupport(this);
	propertyListeners.addPropertyChangeListener(l);
}

/**
 * This method is final.  Override {@link #containsPoint(int, int)}
 * 
 * @since 2.0
 */
public final boolean containsPoint(Point p){return containsPoint(p.x, p.y);}

public boolean containsPoint(int x, int y){return getBounds().contains(x, y);}

public void erase(){
	if (getParent() == null || !isVisible())
		return;
	Rectangle r = getBounds();
	getParent().repaint(r.x, r.y, r.width, r.height);
}

/**
 * Returns a descendant of this Figure such 
 * that the Figure returned contains the point &lt;x, y&gt;, and 
 * is not a member of the Collection <i>c</i>.
 * Returns null if none found.
 */
protected IFigure findDescendantAtExcluding(int x, int y, Collection c){
	if(useLocalCoordinates()){
		x -= (getBounds().x + getInsets().left);
		y -= (getBounds().y + getInsets().top);
	}
	if(!getClientArea(Rectangle.SINGLETON).contains(x,y))
		return null;

	FigureIterator iter = new FigureIterator(this);
	IFigure fig;
	while (iter.hasNext()){
		fig = iter.nextFigure();
		if (fig.isVisible()){
			fig = fig.findFigureAtExcluding(x, y, c);
			if (fig != null) return fig;
		}
	}
	//No descendants were found
	return null;
}

final public IFigure findFigureAt(Point pt){
	return findFigureAtExcluding(pt.x,pt.y,Collections.EMPTY_LIST);
}

final public IFigure findFigureAt(int x, int y){
	return findFigureAtExcluding(x,y,Collections.EMPTY_LIST);
}

public IFigure findFigureAtExcluding(int x, int y, Collection c){
	if (!containsPoint(x,y))
		return null;
	if (c.contains(this))
		return null;
	IFigure child = findDescendantAtExcluding(x, y, c);
	if (child != null)
		return child;

	//No children were found, but this figure containsPoint(x,y);
	return this;
}

/*
 * Returns null or the deepest descendant for which isMouseEventTarget()
 * returns true. The Parameters <i>x</i> and <i>y</i> are absolute locations.
 * Any Graphics transformations applied by this Figure to its children
 * during paintChildren (Thus causing the children to appear transformed to
 * the user) should be applied inversely to the points <i>x</i> and <i>y</i>
 * when called on the children.
 */
public IFigure findMouseEventTargetAt(int x, int y){
	if (!containsPoint(x,y))
		return null;
	IFigure f = findMouseEventTargetInDescendantsAt(x,y);
	if (f != null)
		return f;
	if (isMouseEventTarget())
		return this;
	return null;
}

IFigure findMouseEventTargetInDescendantsAt(int x, int y){
	if(useLocalCoordinates()){
		x -= (getBounds().x + getInsets().left);
		y -= (getBounds().y + getInsets().top);
	}
	FigureIterator iter = new FigureIterator(this);
	IFigure fig;
	while (iter.hasNext()){
		fig = iter.nextFigure();
		if (fig.isVisible() && fig.isEnabled()){
			if (fig.containsPoint(x, y)){
				fig = fig.findMouseEventTargetAt(x, y);
				return fig;
			}
		}
	}
	return null;
}

/**
 * Notifies any {@link FigureListener}s listening to this Figure 
 * that it has moved.
 * 
 * @since 2.0
 */
protected void fireMoved(){
	if (!eventListeners.containsListener(FigureListener.class))
		return;
	Iterator figureListeners = eventListeners.getListeners(FigureListener.class);
	while (figureListeners.hasNext())
		((FigureListener)figureListeners.next()).
			figureMoved(this);
}

/**
 * Notifies any {@link PropertyChangeListener}s listening to this
 * figure that the boolean property with id <code>property</code> 
 * has changed.
 * 
 * @since 2.0
 */
protected void firePropertyChange(String property, boolean old, boolean current){
	if (propertyListeners == null) return;
	propertyListeners.firePropertyChange(property, old, current);
}

/**
 * Notifies any {@link PropertyChangeListener}s listening to this
 * figure that the Object property with id <code>property</code> 
 * has changed.
 * 
 * @since 2.0
 */
protected void firePropertyChange(String property, Object old, Object current){
	if (propertyListeners == null) return;
	propertyListeners.firePropertyChange(property, old, current);
}

/**
 * Notifies any {@link PropertyChangeListener}s listening to this
 * figure that the integer property with id <code>property</code> 
 * has changed.
 * 
 * @since 2.0
 */
protected void firePropertyChange(String property, int old, int current){
	if (propertyListeners == null) return;
	propertyListeners.firePropertyChange(property, old, current);
}

public Color getBackgroundColor(){
	if (bgColor == null && getParent() != null)
		return getParent().getBackgroundColor();
	return bgColor;
}

public Border getBorder(){
	return border;
}

/*
 * Returns the smallest rectangle completely enclosing the figure.
 * Implementations may return the Rectangle by reference.
 * For this reasons, callers of this method must not modify the
 * returned Rectangle.
 */
public Rectangle getBounds(){
	return new Rectangle(bounds);
}

public List getChildren(){
	return children;
}

/**
 * Modifies the given Rectangle so that it represents this
 * Figure's client area, then returns it.
 * 
 * @since 2.0
 */
public Rectangle getClientArea(Rectangle rect){
	rect.setBounds(getBounds());
	rect.crop(getInsets());
	if(useLocalCoordinates())
		rect.setLocation(0,0);
	return rect;
}

public Rectangle getClientArea(){
	return getClientArea(new Rectangle());
}

public Cursor getCursor(){
	if(cursor == null && getParent() != null)
		return getParent().getCursor();
	return cursor;
}

protected boolean getFlag(int flag){
	return (flags & flag) != 0;
}

public Font getFont(){
	if (font != null)
		return font;
	if (getParent() != null)
		return getParent().getFont();
	return null;
}

public Color getForegroundColor(){
	if (fgColor == null && getParent() != null)
		return getParent().getForegroundColor();
	return fgColor;
}

/*
 * Returns the border's Insets if the border is set.
 * Otherwise returns NO_INSETS, an instance of Insets
 * with all 0s.
 * Returns Insets by reference.  DO NOT Modify returned value.
 * Cannot return null.
 */
public Insets getInsets(){
	if (getBorder() != null)
		return getBorder().getInsets(this);
	return NO_INSETS;
}

public LayoutManager getLayoutManager(){
	return layoutManager;
}

/**
 * Returns an Iterator containing the listeners of type
 * <code>clazz</code> that are listening to this Figure.
 * If there are no listeners of type <code>clazz</code>, 
 * an Iterator of an empty list is returned.
 * 
 * @since 2.0
 */
protected Iterator getListeners(Class clazz){
	if (eventListeners == null)
		return Collections.EMPTY_LIST.iterator();
	return eventListeners.getListeners(clazz);
}

/**
 * Returns the top-left corner of this Figure's bounds.
 * 
 * @since 2.0
 */
final public Point getLocation(){
	return getBounds().getLocation();
}

public Dimension getMaximumSize() {
	if(maxSize!=null)
		return maxSize;
	return MAX_DIMENSION;
}

public Dimension getMinimumSize(){
	if (minSize != null)
		return minSize;
	if (getLayoutManager() != null){
		Dimension d = getLayoutManager().getMinimumSize(this);
		if (d != null) return d;
	}
	return getPreferredSize(-1, -1);
}

/*
 * Returns the parent of this figure.  If there is no parent,
 * null is returned.
 */
public IFigure getParent(){
	return parent;
}

public Dimension getPreferredSize(){
	if (prefSize != null)
		return prefSize;
	if (getLayoutManager() != null){
		Dimension d = getLayoutManager().getPreferredSize(this);
		if (d != null) return d;
	}
	return getSize();
}

/**
 * Returns the preferred size this Figure should be, using
 * width and height hints.
 * 
 * @since 2.0
 */
public Dimension getPreferredSize(int wHint, int hHint){
	return getPreferredSize();
}

/**
 * Returns the current size of this figure.
 * 
 * @since 2.0
 */
final public Dimension getSize(){
	return getBounds().getSize();
}

public IFigure getToolTip(){
	return toolTip;
}

public UpdateManager getUpdateManager(){
	if (updateManager != null) return updateManager;
	if (getParent() != null) return getParent().getUpdateManager();
	// Only happens when the figure has not been realized
	return NO_MANAGER;
}

/**
 * Notifies FocusListeners that this figure has gained focus.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a FocusListener with this figure.
 * 
 * @since 2.0
 */
public void handleFocusGained(FocusEvent fe){
	Iterator iter = eventListeners.getListeners(FocusListener.class);
	while(iter.hasNext())
		((FocusListener)iter.next()).
			focusGained(fe);
}

/**
 * Notifies FocusListeners that this figure has lost focus.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a FocusListener with this figure.
 * 
 * @since 2.0
 */
public void handleFocusLost(FocusEvent fe){
	Iterator iter = eventListeners.getListeners(FocusListener.class);
	while(iter.hasNext())
		((FocusListener)iter.next()).
			focusLost(fe);
}

/**
 * Notifies KeyListeners that this figure has lost focus.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a KeyListener with this figure.
 * 
 * @since 2.0
 */
public void handleKeyPressed(KeyEvent e){
	Iterator iter = eventListeners.getListeners(KeyListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((KeyListener)iter.next()).
			keyPressed(e);
}

/**
 * Notifies KeyListeners that this figure has lost focus.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a KeyListener with this figure.
 * 
 * @since 2.0
 */
public void handleKeyReleased(KeyEvent e){
	Iterator iter = eventListeners.getListeners(KeyListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((KeyListener)iter.next()).
			keyReleased(e);
}

/**
 * Notifies MouseListeners that the mouse has been double-clicked
 * on this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseDoubleClicked(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mouseDoubleClicked(e);
}

/**
 * Notifies MouseMotionListeners that the mouse has been dragged
 * over this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseMotionListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseDragged(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseDragged(e);
}

/**
 * Notifies MouseMotionListeners that the mouse has entered this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseMotionListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseEntered(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseEntered(e);
}

/**
 * Notifies MouseMotionListeners that the mouse has exited this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseMotionListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseExited(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseExited(e);
}

/**
 * Notifies MouseMotionListeners that the mouse has hovered over this
 * figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseMotionListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseHover(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseHover(e);
}

/**
 * Notifies MouseMotionListeners that the mouse has moved over this
 * figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseMotionListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseMoved(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseMoved(e);
}

/**
 * Notifies MouseListeners that a mouse button has been pressed
 * while over this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseListener with this figure.
 * 
 * @since 2.0
 */
public void handleMousePressed(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mousePressed(e);
}

/**
 * Notifies MouseListeners that a mouse button has been released
 * while over this figure.
 * NOTE: You should not override this method.  If you are interested
 *       in receiving notification of this type of event, you should
 *       register a MouseListener with this figure.
 * 
 * @since 2.0
 */
public void handleMouseReleased(MouseEvent e){
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!e.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mouseReleased(e);
}

public boolean hasFocus(){
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher == null)
		return false;
	return dispatcher.getFocusOwner() == this;
}

public EventDispatcher internalGetEventDispatcher(){
	if (getParent() != null)
		return getParent().internalGetEventDispatcher();
	return null;
}

public boolean intersects(Rectangle r){
	return getBounds().intersects(r);
}

/*
 * Invalidates this figure through its {@link LayoutManager}.
 */
public void invalidate(){
	if (!isValid()) return;
	setValid(false);
}

public boolean isEnabled(){
	return (flags & FLAG_ENABLED) != 0;
}

public boolean isFocusTraversable(){
	return (flags & FLAG_FOCUS_TRAVERSABLE) != 0;
}

/**
 * Returns <code>true</code> if this figure can receive
 * {@link MouseEvent}s.
 * 
 * @since 2.0
 */
protected boolean isMouseEventTarget(){
	return (eventListeners.containsListener(MouseListener.class) ||
	    eventListeners.containsListener(MouseMotionListener.class));
}

public boolean isOpaque(){
	return (flags & FLAG_OPAQUE) != 0;
}

public boolean isRequestFocusEnabled(){
	return (flags & FLAG_FOCUSABLE) != 0;
}

/**
 * Returns <code>true</code> if this figure is valid.
 * 
 * @since 2.0
 */
protected boolean isValid(){
	return (flags & FLAG_VALID) != 0;
}

/**
 * Returns <code>true</code> if revalidating this figure does
 * not require revalidating its parent.
 * 
 * @since 2.0
 */
protected boolean isValidationRoot(){
	return false;
}

public boolean isVisible(){
	return (flags & FLAG_VISIBLE) != 0;
}

/**
 * Lays out this figure using its {@link LayoutManager}.
 * 
 * @since 2.0
 */
protected void layout(){
	if (getLayoutManager() != null)
		getLayoutManager().layout(this);
}

/**
 * Paints this figure, including its border and children.
 *
 * @see #paintFigure(Graphics)
 * @see #paintClientArea(Graphics)
 * @see #paintBorder(Graphics)
 */
public void paint(Graphics graphics){
	if (bgColor != null)
		graphics.setBackgroundColor(bgColor);
	if (fgColor != null)
		graphics.setForegroundColor(fgColor);
	if (font != null)
		graphics.setFont(font);
	paintFigure(graphics);
	paintClientArea(graphics);
	paintBorder(graphics);
}

/**
 * Paints the border associated with this figure, if one exists.
 *
 * @see Border#paint(IFigure, Graphics, Insets)
 * @since 2.0
 */
protected void paintBorder(Graphics graphics){
	if (getBorder() != null)
		getBorder().paint(this, graphics, NO_INSETS);
}

/**
 * Paint this Figure's children.
 * 
 * @since 2.0
 */
protected void paintChildren(Graphics g){
	IFigure f;

	g.pushState();
	Rectangle clip = Rectangle.SINGLETON;
	for (int i=0; i<children.size(); i++){
		f = (IFigure)children.get(i);
		if (f.isVisible() && f.intersects(g.getClip(clip))){
			g.clipRect(f.getBounds());
			f.paint(g);
			g.restoreState();
		}
	}
	g.popState();
}

/**
 * Paints this Figure's client area.
 * The client ares is typically defined as the anything inside the figures border or
 * <i>insets</i>, and by default includes the children of this figure.
 * This method levaes the graphics context in its initial state.
 * @since 2.0
 */
protected void paintClientArea(Graphics g){
	if (children.isEmpty())
		return;

	boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

	if(useLocalCoordinates()){
		g.pushState();
		g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
		if (!optimizeClip)
			g.clipRect(getClientArea(PRIVATE_RECT));
		paintChildren(g);
		g.popState();
	} else {
		if (optimizeClip)
			paintChildren(g);
		else {
			g.pushState();
			g.clipRect(getClientArea(PRIVATE_RECT));
			paintChildren(g);
			g.popState();
		}
	}
}

/**
 * Paints this Figure's primary representation, or background.
 * The client area is painted next. Changes made to the graphics context
 * here affect the other paint methods.
 * @since 2.0
 */
protected void paintFigure(Graphics graphics){
	if (isOpaque())
		graphics.fillRectangle(getBounds());
}

/**
 * Translates this figure's bounds, without firing a move.
 *
 * @see #translate(int, int)
 * @since 2.0
 */
protected void primTranslate(int dx, int dy){
	bounds.x += dx;
	bounds.y += dy;
	if (useLocalCoordinates())
		return;
	for (int i=0; i < children.size(); i++)
		((IFigure)children.get(i)).translate(dx,dy);
}

/*
 * Removes the given child figure from this figure's hierarchy and
 * revalidates this figure. The child figure's {@link #removeNotify()} 
 * method is also called.
 */
public void remove(IFigure figure){
	if ((figure.getParent() != this) || !children.contains(figure))
		throw new IllegalArgumentException(Draw2dMessages.ERR_Figure_Remove_Exception_IllegalArgument);
	figure.removeNotify();
	if(layoutManager != null)
		layoutManager.remove(figure);
	// The updates in the UpdateManager *have* to be
	// done asynchronously, else will result in 
	// incorrect dirty region corrections.
	figure.erase();
	figure.setParent(null);
	children.remove(figure);
	revalidate();
}

/**
 * Removes all children from this Figure.
 *
 * @see #remove(IFigure)
 * @since 2.0
 */
public void removeAll() {
	List list = new ArrayList(getChildren());
	for (int i=0; i< list.size(); i++) {
		remove((IFigure) list.get(i));
	}
}

public void removeAncestorListener(AncestorListener listener){
	if(ancestorHelper!=null){
		ancestorHelper.removeAncestorListener(listener);
		if(ancestorHelper.getNumberOfListeners()==0){
			ancestorHelper.dispose();
			ancestorHelper=null;
		}
	}
}

public void removeFigureListener(FigureListener listener){
	eventListeners.removeListener(FigureListener.class, listener);
}

public void removeFocusListener(FocusListener listener){
	eventListeners.removeListener(FocusListener.class, listener);
}

/**
 * Removes <i>listener</i> from <i>clazz</i>.
 * 
 * @since 2.0
 */
protected void removeListener(Class clazz, Object listener){
	if (eventListeners == null)
		return;
	eventListeners.removeListener(clazz, listener);
}

public void removeMouseListener(MouseListener listener){
	eventListeners.removeListener(MouseListener.class, listener);
}

public void removeMouseMotionListener(MouseMotionListener listener){
	eventListeners.removeListener(MouseMotionListener.class, listener);
}

/*
 * Called prior to this figure's removal from its parent
 */
public void removeNotify() {
	for (int i=0; i<children.size(); i++)
		((IFigure)children.get(i)).removeNotify();
	if (internalGetEventDispatcher() != null)
		internalGetEventDispatcher().requestRemoveFocus(this);
}

public void removePropertyChangeListener(PropertyChangeListener listener){
	if (propertyListeners == null) return;
	propertyListeners.removePropertyChangeListener(listener);
}

public void removePropertyChangeListener(String property, PropertyChangeListener listener){
	if (propertyListeners == null) return;
	propertyListeners.removePropertyChangeListener(property, listener);
}

/**
 * Repaints the area of this figure represented by the given
 * Rectangle.
 */
final public void repaint(Rectangle r){
	repaint(r.x, r.y, r.width, r.height);
}

public void repaint(int x, int y, int w, int h){
	if (isVisible())
		getUpdateManager().addDirtyRegion(this, x, y, w, h);
}

public void repaint(){
	repaint(getBounds());
}

final public void requestFocus(){
	if (!isRequestFocusEnabled() || hasFocus())
		return;
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher == null)
		return;
	dispatcher.requestFocus(this);
}

public void revalidate(){
	invalidate();
	if (getLayoutManager() != null)
		getLayoutManager().invalidate();
	if (getParent() == null || isValidationRoot())
		getUpdateManager().addInvalidFigure(this);
	else
		getParent().revalidate();
}

public void setBackgroundColor(Color bg){
	bgColor = bg;
	repaint();
}

public void setBorder(Border border){
	this.border = border;
	revalidate();
}

/**
 * Sets the bounds of this Figure to the Rectangle r.  Note that <b>r</b>
 * is compared to the figure's current bounds to determine what needs to be
 * repainted and/or exposed and if validation is required.  Since getBounds()
 * may return the current bounds by reference, it is not safe to modify that
 * Rectangle and then call setBounds() after making modifications.  The figure
 * would assume that the bounds are unchanged, and no layout or paint would occur.
 * For proper behavior, always use a copy.
 * @see	#getBounds()
 * @since 2.0
 */
public void setBounds(Rectangle r){
	int x = bounds.x,
	    y = bounds.y;

	boolean resize = (r.width != bounds.width) || (r.height != bounds.height),
		  translate = (r.x != x) || (r.y != y);

	if (isVisible() && (resize || translate))
		erase();
	if (translate){
		int dx = r.x - x;
		int dy = r.y - y;
		primTranslate(dx,dy);
	}
	bounds.width = r.width;
	bounds.height= r.height;
	if (resize)
		invalidate();
	if (resize || translate){
		fireMoved();
		repaint();
	}
}

/**
 * Sets the direction of any {@link Orientable} children.  Allowable
 * values for <code>dir</code> are found in {@link PositionConstants}.
 *
 * @see Orientable#setDirection(int)
 * @since 2.0
 */
protected void setChildrenDirection(int dir){
	FigureIterator iterator = new FigureIterator(this);
	IFigure child;
	while (iterator.hasNext()){
		child = iterator.nextFigure();
		if (child instanceof Orientable)
			((Orientable)child).setDirection(dir);
	}
}

/**
 * Sets all childrens' enabled property to <code>value</code>.
 *
 * @see #setEnabled(boolean)
 * @since 2.0
 */
protected void setChildrenEnabled(boolean value){
	FigureIterator iterator = new FigureIterator(this);
	while (iterator.hasNext())
		iterator.nextFigure().setEnabled(value);
}

/**
 * Sets the orientation of any {@link Orientable} children.  Allowable
 * values for <code>orientation</code> are found in {@link PositionConstants}.
 *
 * @see Orientable#setOrientation(int)
 * @since 2.0
 */
protected void setChildrenOrientation(int orientation){
	FigureIterator iterator = new FigureIterator(this);
	IFigure child;
	while (iterator.hasNext()){
		child = iterator.nextFigure();
		if (child instanceof Orientable)
			((Orientable)child).setOrientation(orientation);
	}
}

/**
 * @deprecated
 * Sets the constraint of a previously added child.
 */
public void setConstraint(IFigure child, Object constraint) {
	if (!getChildren().contains(child))
		throw new IllegalArgumentException(Draw2dMessages.ERR_Figure_SetConstraint_Exception_IllegalArgument);
	
	if (layoutManager != null)
		layoutManager.setConstraint(child, constraint);
	revalidate();
}

public void setCursor(Cursor cursor){
	if (this.cursor == cursor)
		return;
	this.cursor = cursor;
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher != null)
		dispatcher.updateCursor();
}

public void setEnabled(boolean value){
	if (isEnabled() == value) return;
	setFlag(FLAG_ENABLED, value);
}

/**
 * Sets the given flag to the given value.
 * 
 * @since 2.0
 */
final protected void setFlag(int flag, boolean value){
	if (value) flags |= flag;
	else flags &= ~flag;
}

public void setFocusTraversable(boolean focusTraversable){
	if (isFocusTraversable() == focusTraversable)
		return;
	setFlag(FLAG_FOCUS_TRAVERSABLE, focusTraversable);	
}

public void setFont(Font f){
	if (font != f){
		font = f;
		revalidate();
	}
}

public void setForegroundColor(Color fg){
	if (fgColor != null && fgColor.equals(fg)) return;
	fgColor = fg;
	repaint();
}

public void setLayoutManager(LayoutManager manager){
	layoutManager = manager;
	revalidate();
}

public void setLocation(Point p){
	if (getLocation().equals(p)) return;
	Rectangle r = new Rectangle(getBounds());
	r.setLocation(p);
	setBounds(r);
}

public void setMaximumSize(Dimension d){
	if (maxSize != null && maxSize.equals(d)) return;
	maxSize = d;
	revalidate();
}

public void setMinimumSize(Dimension d){
	if (minSize != null && minSize.equals(d)) return;
	minSize = d;
	revalidate();
}

public void setOpaque(boolean opaque){
	if (isOpaque() == opaque)
		return;
	setFlag(FLAG_OPAQUE, opaque);
	repaint();
}

public void setParent(IFigure p){
	IFigure oldParent = parent;
	parent = p;
	firePropertyChange("parent", oldParent, p);//$NON-NLS-1$
}

public void setPreferredSize(Dimension size){
	if (prefSize != null && prefSize.equals(size))
		return;
	prefSize = size;
	revalidate();
}

/**
 * Sets the preferred size of this figure.
 *
 * @see #setPreferredSize(Dimension)
 * @since 2.0
 */
public final void setPreferredSize(int w, int h){
	setPreferredSize(new Dimension(w,h));
}

public void setRequestFocusEnabled(boolean requestFocusEnabled){
	if (isRequestFocusEnabled() == requestFocusEnabled)
		return;
	setFlag(FLAG_FOCUSABLE, requestFocusEnabled);	
}

final public void setSize(Dimension d){
	setSize(d.width, d.height);
}

/**
 * Sets the current size of this figure.
 *
 * @since 2.0
 */
public void setSize(int w, int h){
	Rectangle bounds = getBounds();
	if (bounds.width == w && bounds.height == h)
		return;
	Rectangle r = new Rectangle(getBounds());
	r.setSize(w, h);
	setBounds(r);
}

/**
 * The passed Figure <i>f</i> will appear on a
 * mouse hover over this Figure.
 * 
 * @since 2.0
 */
public void setToolTip(IFigure f){
	if(toolTip == f)
		return;
	toolTip = f;
}

final public void setUpdateManager(UpdateManager updateManager){
	this.updateManager = updateManager;
	revalidate();
	repaint();
}

/**
 * Sets this figure to be valid if <code>valid</code> is <code>true</code>,
 * and invalid otherwise.
 * 
 * @since 2.0
 */
public void setValid(boolean valid){
	setFlag(FLAG_VALID, valid);
}

public void setVisible(boolean visible){
	boolean currentVisibility = isVisible();
	if (visible == currentVisibility) return;
	if (currentVisibility) erase();
	setFlag(FLAG_VISIBLE, visible);
	if (visible) repaint();
}

final public void translate(int x, int y){
	primTranslate(x, y);
	fireMoved();
}

public void translateFromParent(Translatable t){
	if(useLocalCoordinates())
		t.performTranslate(-getBounds().x - getInsets().left, -getBounds().y - getInsets().top);
}

public final void translateToAbsolute(Translatable t){
	if (getParent() != null){
		getParent().translateToParent(t);
		getParent().translateToAbsolute(t);
	}
}

public void translateToParent(Translatable t){
	if(useLocalCoordinates())
		t.performTranslate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
}

final public void translateToRelative(Translatable t){
	if (getParent() != null){
		getParent().translateFromParent(t);
		getParent().translateToRelative(t);
	}
}

/**
 * Returns <code>true</code> if this figure uses local coordinates.  This
 * means its children are placed relative to this figure's top-left corner.
 * 
 * @since 2.0
 */
protected boolean useLocalCoordinates(){
	return false;
}

public void validate(){
	if (isValid())
		return;
	setValid(true);
	layout();
	for (int i=0; i<children.size(); i++)
		((IFigure)children.get(i)).validate();
}

static public class FigureIterator {
	protected List list;
	protected int index;
	public FigureIterator(IFigure f){
		list=f.getChildren();
		index=list.size();
	}
	public IFigure nextFigure(){
		return (IFigure)list.get(--index);
	}
	public boolean hasNext(){
		return index > 0;
	}
};

protected static final UpdateManager NO_MANAGER = new UpdateManager(){
	public void addDirtyRegion (IFigure figure, int x, int y, int w, int h){}
	public void addInvalidFigure(IFigure f){}
	public void performUpdate(){}
	public void performUpdate(Rectangle region){}
	public void setRoot(IFigure root){}
	public void setGraphicsSource(GraphicsSource gs){}
};

}