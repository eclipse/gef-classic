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

private static final Rectangle PRIVATE_RECT = new Rectangle();
private static final Point PRIVATE_POINT = new Point();
private static final int
	FLAG_VALID = 1,
	FLAG_OPAQUE = 1 << 1,
	FLAG_VISIBLE = 1 << 2,
	FLAG_FOCUSABLE = 1 << 3,
	FLAG_ENABLED = 1 << 4,
	FLAG_FOCUS_TRAVERSABLE = 1 << 5;

/**
 * The largest flag defined in this class.  If subclasses define flags, they should
 * declare them as larger than this value and redefine MAX_FLAG to be their largest flag
 * value.
 */
protected static int MAX_FLAG = FLAG_FOCUS_TRAVERSABLE;

/**
 * The rectangular area that this Figure occupies.
 */
protected Rectangle bounds = new Rectangle(0, 0, 64, 36);

private LayoutManager layoutManager;

/**
 * The flags for this Figure.
 */
protected int flags = FLAG_VISIBLE | FLAG_ENABLED;

private IFigure parent;
private Cursor cursor;

private PropertyChangeSupport propertyListeners;
private EventListenerList eventListeners = new EventListenerList();

private List children = Collections.EMPTY_LIST;

/** This Figure's preferred size. */
protected Dimension prefSize;
/** This Figure's minimum size. */
protected Dimension minSize;
/** This Figure's maximum size. */
protected Dimension maxSize;
/** This Figure's font. */
protected Font font;
/** This Figure's background color. */
protected Color bgColor;
/** This Figure's foreground color. */
protected Color fgColor;
/** This Figure's border. */
protected Border border;
/** This Figure's tooltip. */
protected IFigure toolTip;

private AncestorHelper ancestorHelper;

/**
 * Calls {@link #add(IFigure, Object, int)} with -1 as the index.
 * @see org.eclipse.draw2d.IFigure#add(IFigure, Object)
 */
public final void add(IFigure figure, Object constraint) {
	add(figure, constraint, -1);
}

/**
 * @see org.eclipse.draw2d.IFigure#add(IFigure, Object, int)
 */
public void add(IFigure figure, Object constraint, int index) {
	if (children == Collections.EMPTY_LIST)
		children = new ArrayList(2);
	if (index < -1 || index > children.size())
		throw new IndexOutOfBoundsException(Draw2dMessages.ERR_Figure_Add_Exception_OutOfBounds);

	//Check for Cycle in heirarchy
	for (IFigure f = this; f != null; f = f.getParent())
		if (figure == f)
			throw new IllegalArgumentException(
						Draw2dMessages.ERR_Figure_Add_Exception_IllegalArgument);

	//Detach the child from previous parent
	if (figure.getParent() != null)
		figure.getParent().remove(figure);

	if (index == -1)
		children.add(figure);
	else
		children.add(index, figure);

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
 * Calls {@link #add(IFigure, Object, int)} with <code>null</code> as the constraint and 
 * -1 as the index.
 * @see org.eclipse.draw2d.IFigure#add(IFigure)
 */
public final void add(IFigure figure) {
	add(figure, null, -1);
}

/**
 * Calls {@link #add(IFigure, Object, int)} with <code>null</code> as the constraint.
 * @see org.eclipse.draw2d.IFigure#add(IFigure, int)
 */
public final void add(IFigure figure, int index) {
	add(figure, null, index);
}
/** * @see org.eclipse.draw2d.IFigure#addAncestorListener(AncestorListener) */
public void addAncestorListener(AncestorListener ancestorListener) {
	if (ancestorHelper == null)
		ancestorHelper = new AncestorHelper(this);
	ancestorHelper.addAncestorListener(ancestorListener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addFigureListener(FigureListener)
 */
public void addFigureListener(FigureListener listener) {
	eventListeners.addListener(FigureListener.class, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addFocusListener(FocusListener)
 */
public void addFocusListener(FocusListener listener) {
	eventListeners.addListener(FocusListener.class, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addKeyListener(KeyListener)
 */
public void addKeyListener(KeyListener listener) {
	eventListeners.addListener(KeyListener.class, listener);
}	

/**
 * Adds a listener of type <i>clazz</i> to this Figure's list of event listeners.
 * @param clazz The listener type * @param listener The listener */
protected void addListener(Class clazz, Object listener) {
	eventListeners.addListener(clazz, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addMouseListener(MouseListener)
 */
public void addMouseListener(MouseListener listener) {
	eventListeners.addListener(MouseListener.class, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addMouseMotionListener(MouseMotionListener)
 */
public void addMouseMotionListener(MouseMotionListener listener) {
	eventListeners.addListener(MouseMotionListener.class, listener);
}

/**
 * Called after the receiver's parent has been set and it has been added to its parent.
 * 
 * @since 2.0
 */
public void addNotify() {
	for (int i = 0; i < children.size(); i++)
		((IFigure)children.get(i)).addNotify();
}

/**
 * @see org.eclipse.draw2d.IFigure#addPropertyChangeListener(String,
 * PropertyChangeListener)
 */
public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
	if (propertyListeners == null)
		propertyListeners = new PropertyChangeSupport(this);
	propertyListeners.addPropertyChangeListener(property, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#addPropertyChangeListener(PropertyChangeListener)
 */
public void addPropertyChangeListener(PropertyChangeListener listener) {
	if (propertyListeners == null)
		propertyListeners = new PropertyChangeSupport(this);
	propertyListeners.addPropertyChangeListener(listener);
}

/**
 * This method is final.  Override {@link #containsPoint(int, int)} if needed.
 * @see org.eclipse.draw2d.IFigure#containsPoint(Point)
 * @since 2.0
 */
public final boolean containsPoint(Point p) {
	return containsPoint(p.x, p.y);
}

/**
 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
 */
public boolean containsPoint(int x, int y) {
	return getBounds().contains(x, y);
}

/**
 * @see org.eclipse.draw2d.IFigure#erase()
 */
public void erase() {
	if (getParent() == null || !isVisible())
		return;
	Rectangle r = getBounds();
	getParent().repaint(r.x, r.y, r.width, r.height);
}

/**
 * Returns a descendant of this Figure such that the Figure returned contains the point
 * (x, y), and is not a member of the Collection <i>c</i>. Returns <code>null</code> if
 * none found.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @param c A Collection of IFigures to exclude from the search
 * @return The descendant Figure at (x,y)
 */
protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
	PRIVATE_POINT.setLocation(x, y);
	translateFromParent(PRIVATE_POINT);
	if (!getClientArea(Rectangle.SINGLETON).contains(PRIVATE_POINT))
		return null;

	FigureIterator iter = new FigureIterator(this);
	IFigure fig;
	while (iter.hasNext()) {
		fig = iter.nextFigure();
		if (fig.isVisible()) {
			fig = fig.findFigureAt(PRIVATE_POINT.x, PRIVATE_POINT.y, search);
			if (fig != null)
				return fig;
		}
	}
	//No descendants were found
	return null;
}

/**
 * @see org.eclipse.draw2d.IFigure#findFigureAt(Point)
 */
public final IFigure findFigureAt(Point pt) {
	return findFigureAtExcluding(pt.x, pt.y, Collections.EMPTY_LIST);
}

/**
 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int)
 */
public final IFigure findFigureAt(int x, int y) {
	return findFigureAt(x, y, IdentitySearch.INSTANCE);
}

public IFigure findFigureAt(int x, int y, TreeSearch search) {
	if (!containsPoint(x, y))
		return null;
	if (search.prune(this))
		return null;
	IFigure child = findDescendantAtExcluding(x, y, search);
	if (child != null)
		return child;
	if (search.accept(this))
		return this;
	return null;
}

/**
 * @see org.eclipse.draw2d.IFigure#findFigureAtExcluding(int, int, Collection)
 */
public final IFigure findFigureAtExcluding(int x, int y, Collection c) {
	return findFigureAt(x, y, new ExclusionSearch(c));
}

/**
 * Returns the deepest descendant for which {@link #isMouseEventTarget()} returns
 * <code>true</code> or <code>null</code> if none found. The Parameters <i>x</i> and
 * <i>y</i> are absolute locations. Any Graphics transformations applied by this Figure to
 * its children during {@link #paintChildren(Graphics)} (thus causing the children to
 * appear transformed to the user) should be applied inversely to the points <i>x</i> and
 * <i>y</i> when called on the children.
 * 
 * @param x The X coordinate
 * @param y The Y coordinate
 * @return The deepest descendant for which isMouseEventTarget() returns true
 */
public IFigure findMouseEventTargetAt(int x, int y) {
	if (!containsPoint(x, y))
		return null;
	IFigure f = findMouseEventTargetInDescendantsAt(x, y);
	if (f != null)
		return f;
	if (isMouseEventTarget())
		return this;
	return null;
}

/**
 * Searches this Figure's children for the deepest descendant for which 
 * {@link #isMouseEventTarget()} returns <code>true</code> and returns that descendant or
 * <code>null</code> if none found.
 * @see {@link #findMouseEventTargetAt(int, int)}
 * @param x The X coordinate * @param y The Y coordinate * @return The deepest descendant for which isMouseEventTarget() returns true */
protected IFigure findMouseEventTargetInDescendantsAt(int x, int y) {
	PRIVATE_POINT.setLocation(x, y);
	translateFromParent(PRIVATE_POINT);

	if (!getClientArea(Rectangle.SINGLETON).contains(PRIVATE_POINT))
		return null;

	FigureIterator iter = new FigureIterator(this);
	IFigure fig;
	while (iter.hasNext()) {
		fig = iter.nextFigure();
		if (fig.isVisible() && fig.isEnabled()) {
			if (fig.containsPoint(PRIVATE_POINT.x, PRIVATE_POINT.y)) {
				fig = fig.findMouseEventTargetAt(PRIVATE_POINT.x, PRIVATE_POINT.y);
				return fig;
			}
		}
	}
	return null;
}

/**
 * Notifies any {@link FigureListener FigureListeners} listening to this Figure that it
 * has moved.
 * 
 * @since 2.0
 */
protected void fireMoved() {
	if (!eventListeners.containsListener(FigureListener.class))
		return;
	Iterator figureListeners = eventListeners.getListeners(FigureListener.class);
	while (figureListeners.hasNext())
		((FigureListener)figureListeners.next()).
			figureMoved(this);
}

/**
 * Notifies any {@link PropertyChangeListener PropertyChangeListeners} listening to this
 * Figure that the boolean property with id <i>property</i> has changed.
 * @param property The id of the property that changed
 * @param old The old value of the changed property
 * @param current The current value of the changed property
 * @since 2.0
 */
protected void firePropertyChange(String property, boolean old, boolean current) {
	if (propertyListeners == null) 
		return;
	propertyListeners.firePropertyChange(property, old, current);
}

/**
 * Notifies any {@link PropertyChangeListener PropertyChangeListeners} listening to this
 * figure that the Object property with id <i>property</i> has changed.
 * @param property The id of the property that changed
 * @param old The old value of the changed property
 * @param current The current value of the changed property
 * @since 2.0
 */
protected void firePropertyChange(String property, Object old, Object current) {
	if (propertyListeners == null) 
		return;
	propertyListeners.firePropertyChange(property, old, current);
}

/**
 * Notifies any {@link PropertyChangeListener PropertyChangeListeners} listening to this
 * figure that the integer property with id <code>property</code> has changed.
 * @param property The id of the property that changed
 * @param old The old value of the changed property
 * @param current The current value of the changed property
 * @since 2.0
 */
protected void firePropertyChange(String property, int old, int current) {
	if (propertyListeners == null) 
		return;
	propertyListeners.firePropertyChange(property, old, current);
}

/**
 * Returns this Figure's background color.  If this Figure's background color is
 * <code>null</code> and its parent is not <code>null</code>, the background color is
 * inherited from the parent.
 * @see org.eclipse.draw2d.IFigure#getBackgroundColor()
 */
public Color getBackgroundColor() {
	if (bgColor == null && getParent() != null)
		return getParent().getBackgroundColor();
	return bgColor;
}

/**
 * @see org.eclipse.draw2d.IFigure#getBorder()
 */
public Border getBorder() {
	return border;
}

/**
 * Returns the smallest rectangle completely enclosing the figure. Implementors may return
 * the Rectangle by reference. For this reason, callers of this method must not modify the
 * returned Rectangle.
 * @return The bounds of this Figure
 */
public Rectangle getBounds() {
	return bounds;
}

/**
 * @see org.eclipse.draw2d.IFigure#getChildren()
 */
public List getChildren() {
	return children;
}

/**
 * @see org.eclipse.draw2d.IFigure#getClientArea(Rectangle)
 */
public Rectangle getClientArea(Rectangle rect) {
	rect.setBounds(getBounds());
	rect.crop(getInsets());
	if (useLocalCoordinates())
		rect.setLocation(0, 0);
	return rect;
}

/**
 * @see org.eclipse.draw2d.IFigure#getClientArea()
 */
public final Rectangle getClientArea() {
	return getClientArea(new Rectangle());
}

/**
 * @see org.eclipse.draw2d.IFigure#getCursor()
 */
public Cursor getCursor() {
	if (cursor == null && getParent() != null)
		return getParent().getCursor();
	return cursor;
}

/**
 * Returns the value of the given flag.
 * @param flag The flag to get * @return The value of the given flag */
protected boolean getFlag(int flag) {
	return (flags & flag) != 0;
}

/**
 * @see org.eclipse.draw2d.IFigure#getFont()
 */
public Font getFont() {
	if (font != null)
		return font;
	if (getParent() != null)
		return getParent().getFont();
	return null;
}

/**
 * @see org.eclipse.draw2d.IFigure#getForegroundColor()
 */
public Color getForegroundColor() {
	if (fgColor == null && getParent() != null)
		return getParent().getForegroundColor();
	return fgColor;
}

/**
 * Returns the border's Insets if the border is set. Otherwise returns NO_INSETS, an
 * instance of Insets with all 0s. Returns Insets by reference.  DO NOT Modify returned
 * value. Cannot return null.
 * @return This Figure's Insets
 */
public Insets getInsets() {
	if (getBorder() != null)
		return getBorder().getInsets(this);
	return NO_INSETS;
}

/**
 * @see org.eclipse.draw2d.IFigure#getLayoutManager()
 */
public LayoutManager getLayoutManager() {
	return layoutManager;
}

/**
 * Returns an Iterator containing the listeners of type <i>clazz</i> that are listening to
 * this Figure. If there are no listeners of type <i>clazz</i>, an Iterator of an empty
 * list is returned.
 * @param clazz The type of listeners to get
 * @return An Iterator containing the listeners of type <i>clazz</i>
 * @since 2.0
 */
protected Iterator getListeners(Class clazz) {
	if (eventListeners == null)
		return Collections.EMPTY_LIST.iterator();
	return eventListeners.getListeners(clazz);
}

/**
 * Returns the local background Color of this Figure. Does not inherit this Color from the
 * parent, may return null.
 * @return bgColor The local background Color
 */
public Color getLocalBackgroundColor() {
	return bgColor;
}	

/**
 * Returns the local foreground Color of this Figure. Does not inherit this Color from the
 * parent, may return null.
 * @return fgColor The local foreground Color
 */
public Color getLocalForegroundColor() {
	return fgColor;
}	

/**
 * Returns the top-left corner of this Figure's bounds.
 * @return The top-left corner of this Figure's bounds
 * @since 2.0
 */
public final Point getLocation() {
	return getBounds().getLocation();
}

/**
 * @see org.eclipse.draw2d.IFigure#getMaximumSize()
 */
public Dimension getMaximumSize() {
	if (maxSize != null)
		return maxSize;
	return MAX_DIMENSION;
}

/**
 * @see org.eclipse.draw2d.IFigure#getMinimumSize()
 */
public final Dimension getMinimumSize() {
	return getMinimumSize(-1, -1);
}

public Dimension getMinimumSize(int wHint, int hHint) {
	if (minSize != null)
		return minSize;
	if (getLayoutManager() != null) {
		Dimension d = getLayoutManager().getMinimumSize(this, wHint, hHint);
		if (d != null)
			return d;
	}
	return getPreferredSize(wHint, hHint);
}

/**
 * @see org.eclipse.draw2d.IFigure#getParent()
 */
public IFigure getParent() {
	return parent;
}

/**
 * @see org.eclipse.draw2d.IFigure#getPreferredSize()
 */
public final Dimension getPreferredSize() {
	return getPreferredSize(-1, -1);
}

/**
 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	if (prefSize != null)
		return prefSize;
	if (getLayoutManager() != null) {
		Dimension d = getLayoutManager().getPreferredSize(this, wHint, hHint);
		if (d != null)
			return d;
	}
	return getSize();
}

/**
 * @see org.eclipse.draw2d.IFigure#getSize()
 */
public final Dimension getSize() {
	return getBounds().getSize();
}

/**
 * @see org.eclipse.draw2d.IFigure#getToolTip()
 */
public IFigure getToolTip() {
	return toolTip;
}

/**
 * @see org.eclipse.draw2d.IFigure#getUpdateManager()
 */
public UpdateManager getUpdateManager() {
	if (getParent() != null) 
		return getParent().getUpdateManager();
	// Only happens when the figure has not been realized
	return NO_MANAGER;
}

/**
 * @see org.eclipse.draw2d.IFigure#handleFocusGained(FocusEvent)
 */
public void handleFocusGained(FocusEvent event) {
	Iterator iter = eventListeners.getListeners(FocusListener.class);
	while (iter.hasNext())
		((FocusListener)iter.next()).
			focusGained(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleFocusLost(FocusEvent)
 */
public void handleFocusLost(FocusEvent event) {
	Iterator iter = eventListeners.getListeners(FocusListener.class);
	while (iter.hasNext())
		((FocusListener)iter.next()).
			focusLost(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleKeyPressed(KeyEvent)
 */
public void handleKeyPressed(KeyEvent event) {
	Iterator iter = eventListeners.getListeners(KeyListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((KeyListener)iter.next()).
			keyPressed(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleKeyReleased(KeyEvent)
 */
public void handleKeyReleased(KeyEvent event) {
	Iterator iter = eventListeners.getListeners(KeyListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((KeyListener)iter.next()).
			keyReleased(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseDoubleClicked(MouseEvent)
 */
public void handleMouseDoubleClicked(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mouseDoubleClicked(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseDragged(MouseEvent)
 */
public void handleMouseDragged(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseDragged(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseEntered(MouseEvent)
 */
public void handleMouseEntered(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseEntered(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseExited(MouseEvent)
 */
public void handleMouseExited(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseExited(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseHover(MouseEvent)
 */
public void handleMouseHover(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseHover(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseMoved(MouseEvent)
 */
public void handleMouseMoved(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseMotionListener)iter.next()).
			mouseMoved(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMousePressed(MouseEvent)
 */
public void handleMousePressed(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mousePressed(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#handleMouseReleased(MouseEvent)
 */
public void handleMouseReleased(MouseEvent event) {
	Iterator iter = eventListeners.getListeners(MouseListener.class);
	while (!event.isConsumed() && iter.hasNext()) 
		((MouseListener)iter.next()).
			mouseReleased(event);
}

/**
 * @see org.eclipse.draw2d.IFigure#hasFocus()
 */
public boolean hasFocus() {
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher == null)
		return false;
	return dispatcher.getFocusOwner() == this;
}

/**
 * @see org.eclipse.draw2d.IFigure#internalGetEventDispatcher()
 */
public EventDispatcher internalGetEventDispatcher() {
	if (getParent() != null)
		return getParent().internalGetEventDispatcher();
	return null;
}

/**
 * @see org.eclipse.draw2d.IFigure#intersects(Rectangle)
 */
public boolean intersects(Rectangle rect) {
	return getBounds().intersects(rect);
}

/**
 * @see org.eclipse.draw2d.IFigure#invalidate()
 */
public void invalidate() {
	if (!isValid()) 
		return;
	setValid(false);
}

/**
 * @see org.eclipse.draw2d.IFigure#isEnabled()
 */
public boolean isEnabled() {
	return (flags & FLAG_ENABLED) != 0;
}

/**
 * @see org.eclipse.draw2d.IFigure#isFocusTraversable()
 */
public boolean isFocusTraversable() {
	return (flags & FLAG_FOCUS_TRAVERSABLE) != 0;
}

/**
 * Returns <code>true</code> if this Figure can receive {@link MouseEvent MouseEvents}.
 * @return <code>true</code> if this Figure can receive {@link MouseEvent MouseEvents}
 * @since 2.0
 */
protected boolean isMouseEventTarget() {
	return (eventListeners.containsListener(MouseListener.class)
		|| eventListeners.containsListener(MouseMotionListener.class));
}

/**
 * @see org.eclipse.draw2d.IFigure#isOpaque()
 */
public boolean isOpaque() {
	return (flags & FLAG_OPAQUE) != 0;
}

/**
 * @see org.eclipse.draw2d.IFigure#isRequestFocusEnabled()
 */
public boolean isRequestFocusEnabled() {
	return (flags & FLAG_FOCUSABLE) != 0;
}

/**
 * Returns <code>true</code> if this Figure is valid.
 * @return <code>true</code> if this Figure is valid
 * @since 2.0
 */
protected boolean isValid() {
	return (flags & FLAG_VALID) != 0;
}

/**
 * Returns <code>true</code> if revalidating this Figure does not require revalidating its
 * parent.
 * @return <code>true</code> if revalidating this Figure doesn't require revalidating its
 * parent.
 * @since 2.0
 */
protected boolean isValidationRoot() {
	return false;
}

/**
 * @see org.eclipse.draw2d.IFigure#isVisible()
 */
public boolean isVisible() {
	return (flags & FLAG_VISIBLE) != 0;
}

/**
 * Lays out this Figure using its {@link LayoutManager}.
 * 
 * @since 2.0
 */
protected void layout() {
	if (getLayoutManager() != null)
		getLayoutManager().layout(this);
}

/**
 * Paints this Figure and its children.
 * @param graphics The Graphics object used for painting
 * @see #paintFigure(Graphics)
 * @see #paintClientArea(Graphics)
 * @see #paintBorder(Graphics)
 */
public void paint(Graphics graphics) {
	if (bgColor != null)
		graphics.setBackgroundColor(bgColor);
	if (fgColor != null)
		graphics.setForegroundColor(fgColor);
	if (font != null)
		graphics.setFont(font);

	graphics.pushState();
	try {
		paintFigure(graphics);
		graphics.restoreState();
		paintClientArea(graphics);
		paintBorder(graphics);
	} finally {
		graphics.popState();
	}
}

/**
 * Paints the border associated with this Figure, if one exists.
 * @param graphics The Graphics used to paint
 * @see Border#paint(IFigure, Graphics, Insets)
 * @since 2.0
 */
protected void paintBorder(Graphics graphics) {
	if (getBorder() != null)
		getBorder().paint(this, graphics, NO_INSETS);
}

/**
 * Paints this Figure's children. The caller must save the state of the graphics prior to
 * calling this method, such that <code>graphics.restoreState()</code> may be called
 * safely, and doing so will return the graphics to its original state when the method was
 * entered.
 * <P>
 * This method must leave the Graphics in its original state upon return.
 * @param graphics the graphics used to paint
 * @since 2.0
 */
protected void paintChildren(Graphics graphics) {
	IFigure child;

	Rectangle clip = Rectangle.SINGLETON;
	for (int i = 0; i < children.size(); i++) {
		child = (IFigure)children.get(i);
		if (child.isVisible() && child.intersects(graphics.getClip(clip))) {
			graphics.clipRect(child.getBounds());
			child.paint(graphics);
			graphics.restoreState();
		}
	}
}

/**
 * Paints this Figure's client area. The client area is typically defined as the anything
 * inside the Figure's {@link Border} or {@link Insets}, and by default includes the
 * children of this Figure. On return, this method must leave the given Graphics in its
 * initial state.
 * @param graphics The Graphics used to paint
 * @since 2.0
 */
protected void paintClientArea(Graphics graphics) {
	if (children.isEmpty())
		return;

	boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

	if (useLocalCoordinates()) {
		graphics.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
		if (!optimizeClip)
			graphics.clipRect(getClientArea(PRIVATE_RECT));
		graphics.pushState();
		paintChildren(graphics);
		graphics.popState();
		graphics.restoreState();
	} else {
		if (optimizeClip)
			paintChildren(graphics);
		else {
			graphics.clipRect(getClientArea(PRIVATE_RECT));
			graphics.pushState();
			paintChildren(graphics);
			graphics.popState();
			graphics.restoreState();
		}
	}
}

/**
 * Paints this Figure's primary representation, or background. Changes made to the
 * graphics to the graphics current state will not affect the subsequent calls to {@link
 * #paintClientArea(Graphics)} and {@link #paintBorder(Graphics)}. Furthermore, it is safe
 * to call <code>graphics.restoreState()</code> within this method, and doing so will
 * restore the graphics to its original state upon entry.
 * @param graphics The Graphics used to paint
 * @since 2.0
 */
protected void paintFigure(Graphics graphics) {
	if (isOpaque())
		graphics.fillRectangle(getBounds());
}

/**
 * Translates this Figure's bounds, without firing a move.
 * @param dx The amount to translate horizontally
 * @param dy The amount to translate vertically
 * @see #translate(int, int)
 * @since 2.0
 */
protected void primTranslate(int dx, int dy) {
	bounds.x += dx;
	bounds.y += dy;
	if (useLocalCoordinates())
		return;
	for (int i = 0; i < children.size(); i++)
		((IFigure)children.get(i)).translate(dx, dy);
}

/**
 * Removes the given child Figure from this Figure's hierarchy and revalidates this
 * Figure. The child Figure's {@link #removeNotify()} method is also called.
 * @param figure The Figure to remove
 */
public void remove(IFigure figure) {
	if ((figure.getParent() != this) || !children.contains(figure))
		throw new IllegalArgumentException(
						Draw2dMessages.ERR_Figure_Remove_Exception_IllegalArgument);
	figure.removeNotify();
	if (layoutManager != null)
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
	for (int i = 0; i < list.size(); i++) {
		remove((IFigure)list.get(i));
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#removeAncestorListener(AncestorListener)
 */
public void removeAncestorListener(AncestorListener listener) {
	if (ancestorHelper != null) {
		ancestorHelper.removeAncestorListener(listener);
		if (ancestorHelper.getNumberOfListeners() == 0) {
			ancestorHelper.dispose();
			ancestorHelper = null;
		}
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#removeFigureListener(FigureListener)
 */
public void removeFigureListener(FigureListener listener) {
	eventListeners.removeListener(FigureListener.class, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#removeFocusListener(FocusListener)
 */
public void removeFocusListener(FocusListener listener) {
	eventListeners.removeListener(FocusListener.class, listener);
}

/**
 * Removes <i>listener</i> of type <i>clazz</i> from this Figure's list of listeners.
 * @param clazz The type of listener
 * @param listener The listener to remove
 * @since 2.0
 */
protected void removeListener(Class clazz, Object listener) {
	if (eventListeners == null)
		return;
	eventListeners.removeListener(clazz, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#removeMouseListener(MouseListener)
 */
public void removeMouseListener(MouseListener listener) {
	eventListeners.removeListener(MouseListener.class, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#removeMouseMotionListener(MouseMotionListener)
 */
public void removeMouseMotionListener(MouseMotionListener listener) {
	eventListeners.removeListener(MouseMotionListener.class, listener);
}

/**
 * Called prior to this figure's removal from its parent
 */
public void removeNotify() {
	for (int i = 0; i < children.size(); i++)
		((IFigure)children.get(i)).removeNotify();
	if (internalGetEventDispatcher() != null)
		internalGetEventDispatcher().requestRemoveFocus(this);
}

/**
 * @see org.eclipse.draw2d.IFigure#removePropertyChangeListener(PropertyChangeListener)
 */
public void removePropertyChangeListener(PropertyChangeListener listener) {
	if (propertyListeners == null) return;
	propertyListeners.removePropertyChangeListener(listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#removePropertyChangeListener(String, PropertyChangeListener)
 */
public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
	if (propertyListeners == null) return;
	propertyListeners.removePropertyChangeListener(property, listener);
}

/**
 * @see org.eclipse.draw2d.IFigure#repaint(Rectangle)
 */
public final void repaint(Rectangle rect) {
	repaint(rect.x, rect.y, rect.width, rect.height);
}

/**
 * @see org.eclipse.draw2d.IFigure#repaint(int, int, int, int)
 */
public void repaint(int x, int y, int w, int h) {
	if (isVisible())
		getUpdateManager().addDirtyRegion(this, x, y, w, h);
}

/**
 * @see org.eclipse.draw2d.IFigure#repaint()
 */
public void repaint() {
	repaint(getBounds());
}

/**
 * @see org.eclipse.draw2d.IFigure#requestFocus()
 */
public final void requestFocus() {
	if (!isRequestFocusEnabled() || hasFocus())
		return;
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher == null)
		return;
	dispatcher.requestFocus(this);
}

/**
 * @see org.eclipse.draw2d.IFigure#revalidate()
 */
public void revalidate() {
	invalidate();
	if (getLayoutManager() != null)
		getLayoutManager().invalidate();
	if (getParent() == null || isValidationRoot())
		getUpdateManager().addInvalidFigure(this);
	else
		getParent().revalidate();
}

/**
 * @see org.eclipse.draw2d.IFigure#setBackgroundColor(Color)
 */
public void setBackgroundColor(Color bg) {
	bgColor = bg;
	repaint();
}

/**
 * @see org.eclipse.draw2d.IFigure#setBorder(Border)
 */
public void setBorder(Border border) {
	this.border = border;
	revalidate();
}

/**
 * Sets the bounds of this Figure to the Rectangle <i>rect</i>. Note that <i>rect</i> is
 * compared to the Figure's current bounds to determine what needs to be repainted and/or
 * exposed and if validation is required. Since {@link #getBounds()} may return the
 * current bounds by reference, it is not safe to modify that Rectangle and then call
 * setBounds() after making modifications. The figure would assume that the bounds are
 * unchanged, and no layout or paint would occur. For proper behavior, always use a copy.
 * @param rect The new bounds
 * @since 2.0
 */
public void setBounds(Rectangle rect) {
	int x = bounds.x,
	    y = bounds.y;

	boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height),
		  translate = (rect.x != x) || (rect.y != y);

	if (isVisible() && (resize || translate))
		erase();
	if (translate) {
		int dx = rect.x - x;
		int dy = rect.y - y;
		primTranslate(dx, dy);
	}
	bounds.width = rect.width;
	bounds.height = rect.height;
	if (resize)
		invalidate();
	if (resize || translate) {
		fireMoved();
		repaint();
	}
}

/**
 * Sets the direction of any {@link Orientable} children.  Allowable values for
 * <code>dir</code> are found in {@link PositionConstants}.
 * @param direction The direction
 * @see Orientable#setDirection(int)
 * @since 2.0
 */
protected void setChildrenDirection(int direction) {
	FigureIterator iterator = new FigureIterator(this);
	IFigure child;
	while (iterator.hasNext()) {
		child = iterator.nextFigure();
		if (child instanceof Orientable)
			((Orientable)child).setDirection(direction);
	}
}

/**
 * Sets all childrens' enabled property to <i>value</i>.
 * @param value The enable value
 * @see #setEnabled(boolean)
 * @since 2.0
 */
protected void setChildrenEnabled(boolean value) {
	FigureIterator iterator = new FigureIterator(this);
	while (iterator.hasNext())
		iterator.nextFigure().setEnabled(value);
}

/**
 * Sets the orientation of any {@link Orientable} children. Allowable values for
 * <i>orientation</i> are found in {@link PositionConstants}.
 * @param orientation The Orientation
 * @see Orientable#setOrientation(int)
 * @since 2.0
 */
protected void setChildrenOrientation(int orientation) {
	FigureIterator iterator = new FigureIterator(this);
	IFigure child;
	while (iterator.hasNext()) {
		child = iterator.nextFigure();
		if (child instanceof Orientable)
			((Orientable)child).setOrientation(orientation);
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#setConstraint(IFigure, Object)
 */
public void setConstraint(IFigure child, Object constraint) {
	if (!getChildren().contains(child))
		throw new IllegalArgumentException(
			Draw2dMessages.ERR_Figure_SetConstraint_Exception_IllegalArgument);
	
	if (layoutManager != null)
		layoutManager.setConstraint(child, constraint);
	revalidate();
}

/**
 * @see org.eclipse.draw2d.IFigure#setCursor(Cursor)
 */
public void setCursor(Cursor cursor) {
	if (this.cursor == cursor)
		return;
	this.cursor = cursor;
	EventDispatcher dispatcher = internalGetEventDispatcher();
	if (dispatcher != null)
		dispatcher.updateCursor();
}

/**
 * @see org.eclipse.draw2d.IFigure#setEnabled(boolean)
 */
public void setEnabled(boolean value) {
	if (isEnabled() == value) 
		return;
	setFlag(FLAG_ENABLED, value);
}

/**
 * Sets the given flag to the given value.
 * @param flag The flag to set
 * @param value The value
 * @since 2.0
 */
protected final void setFlag(int flag, boolean value) {
	if (value) 
		flags |= flag;
	else 
		flags &= ~flag;
}

/**
 * @see org.eclipse.draw2d.IFigure#setFocusTraversable(boolean)
 */
public void setFocusTraversable(boolean focusTraversable) {
	if (isFocusTraversable() == focusTraversable)
		return;
	setFlag(FLAG_FOCUS_TRAVERSABLE, focusTraversable);	
}

/**
 * @see org.eclipse.draw2d.IFigure#setFont(Font)
 */
public void setFont(Font f) {
	if (font != f) {
		font = f;
		revalidate();
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#setForegroundColor(Color)
 */
public void setForegroundColor(Color fg) {
	if (fgColor != null && fgColor.equals(fg)) 
		return;
	fgColor = fg;
	repaint();
}

/**
 * @see org.eclipse.draw2d.IFigure#setLayoutManager(LayoutManager)
 */
public void setLayoutManager(LayoutManager manager) {
	layoutManager = manager;
	revalidate();
}

/**
 * @see org.eclipse.draw2d.IFigure#setLocation(Point)
 */
public void setLocation(Point p) {
	if (getLocation().equals(p)) 
		return;
	Rectangle r = new Rectangle(getBounds());
	r.setLocation(p);
	setBounds(r);
}

/**
 * @see org.eclipse.draw2d.IFigure#setMaximumSize(Dimension)
 */
public void setMaximumSize(Dimension d) {
	if (maxSize != null && maxSize.equals(d)) 
		return;
	maxSize = d;
	revalidate();
}

/**
 * @see org.eclipse.draw2d.IFigure#setMinimumSize(Dimension)
 */
public void setMinimumSize(Dimension d) {
	if (minSize != null && minSize.equals(d)) 
		return;
	minSize = d;
	revalidate();
}

/**
 * @see org.eclipse.draw2d.IFigure#setOpaque(boolean)
 */
public void setOpaque(boolean opaque) {
	if (isOpaque() == opaque)
		return;
	setFlag(FLAG_OPAQUE, opaque);
	repaint();
}

/**
 * @see org.eclipse.draw2d.IFigure#setParent(IFigure)
 */
public void setParent(IFigure p) {
	IFigure oldParent = parent;
	parent = p;
	firePropertyChange("parent", oldParent, p);//$NON-NLS-1$
}

/**
 * @see org.eclipse.draw2d.IFigure#setPreferredSize(Dimension)
 */
public void setPreferredSize(Dimension size) {
	if (prefSize != null && prefSize.equals(size))
		return;
	prefSize = size;
	revalidate();
}

/**
 * Sets the preferred size of this figure.
 * @param w The new preferred width
 * @param h The new preferred height
 * @see #setPreferredSize(Dimension)
 * @since 2.0
 */
public final void setPreferredSize(int w, int h) {
	setPreferredSize(new Dimension(w, h));
}

/**
 * @see org.eclipse.draw2d.IFigure#setRequestFocusEnabled(boolean)
 */
public void setRequestFocusEnabled(boolean requestFocusEnabled) {
	if (isRequestFocusEnabled() == requestFocusEnabled)
		return;
	setFlag(FLAG_FOCUSABLE, requestFocusEnabled);	
}

/**
 * @see org.eclipse.draw2d.IFigure#setSize(Dimension)
 */
public final void setSize(Dimension d) {
	setSize(d.width, d.height);
}

/**
 * @see org.eclipse.draw2d.IFigure#setSize(int, int)
 */
public void setSize(int w, int h) {
	Rectangle bounds = getBounds();
	if (bounds.width == w && bounds.height == h)
		return;
	Rectangle r = new Rectangle(getBounds());
	r.setSize(w, h);
	setBounds(r);
}

/**
 * @see org.eclipse.draw2d.IFigure#setToolTip(IFigure)
 */
public void setToolTip(IFigure f) {
	if (toolTip == f)
		return;
	toolTip = f;
}

/**
 * Sets this figure to be valid if <i>value</i> is <code>true</code> and invalid
 * otherwise.
 * @param value The valid value 
 * @since 2.0
 */
public void setValid(boolean value) {
	setFlag(FLAG_VALID, value);
}

/**
 * @see org.eclipse.draw2d.IFigure#setVisible(boolean)
 */
public void setVisible(boolean visible) {
	boolean currentVisibility = isVisible();
	if (visible == currentVisibility) 
		return;
	if (currentVisibility) 
		erase();
	setFlag(FLAG_VISIBLE, visible);
	if (visible) 
		repaint();
}

/**
 * @see org.eclipse.draw2d.IFigure#translate(int, int)
 */
public final void translate(int x, int y) {
	primTranslate(x, y);
	fireMoved();
}

/**
 * @see org.eclipse.draw2d.IFigure#translateFromParent(Translatable)
 */
public void translateFromParent(Translatable t) {
	if (useLocalCoordinates())
		t.performTranslate(-getBounds().x - getInsets().left, -getBounds().y - getInsets().top);
}

/**
 * @see org.eclipse.draw2d.IFigure#translateToAbsolute(Translatable)
 */
public final void translateToAbsolute(Translatable t) {
	if (getParent() != null) {
		getParent().translateToParent(t);
		getParent().translateToAbsolute(t);
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#translateToParent(Translatable)
 */
public void translateToParent(Translatable t) {
	if (useLocalCoordinates())
		t.performTranslate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
}

/**
 * @see org.eclipse.draw2d.IFigure#translateToRelative(Translatable)
 */
public final void translateToRelative(Translatable t) {
	if (getParent() != null) {
		getParent().translateToRelative(t);
		getParent().translateFromParent(t);
	}
}

/**
 * Returns <code>true</code> if this Figure uses local coordinates. This means its
 * children are placed relative to this Figure's top-left corner.
 * @return <code>true</code> if this Figure uses local coordinates
 * @since 2.0
 */
protected boolean useLocalCoordinates() {
	return false;
}

/**
 * @see org.eclipse.draw2d.IFigure#validate()
 */
public void validate() {
	if (isValid())
		return;
	setValid(true);
	layout();
	for (int i = 0; i < children.size(); i++)
		((IFigure)children.get(i)).validate();
}

static class IdentitySearch implements TreeSearch {
	static final TreeSearch INSTANCE = new IdentitySearch();
	public boolean accept(IFigure f) {
		return true;
	}
	public boolean prune(IFigure f) {
		return false;
	}
}

/**
 * Iterates over a Figure's children.
 */
public static class FigureIterator {
	private List list;
	private int index;
	/**
	 * Constructs a new FigureIterator for the given Figure.
	 * @param figure The Figure whose children to iterate over	 */
	public FigureIterator(IFigure figure) {
		list = figure.getChildren();
		index = list.size();
	}
	/**
	 * Returns the next Figure.
	 * @return The next Figure
	 */
	public IFigure nextFigure() {
		return (IFigure)list.get(--index);
	}
	/**
	 * Returns <code>true</code> if there's another Figure to iterate over.
	 * @return <code>true</code> if there's another Figure to iterate over	 */
	public boolean hasNext() {
		return index > 0;
	}
};

/**
 * An UpdateManager that does nothing.
 */
protected static final UpdateManager NO_MANAGER = new UpdateManager() {
	public void addDirtyRegion (IFigure figure, int x, int y, int w, int h) { }
	public void addInvalidFigure(IFigure f) { }
	public void performUpdate() { }
	public void performUpdate(Rectangle region) { }
	public void setRoot(IFigure root) { }
	public void setGraphicsSource(GraphicsSource gs) { }
};

}