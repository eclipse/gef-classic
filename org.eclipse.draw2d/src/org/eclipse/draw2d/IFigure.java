package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import java.util.Collection;
import java.beans.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.draw2d.geometry.*;

/**
 * A lightweight graphical representation.  Figures are rendered to a {@link Graphics} 
 * object. Figures can be composed to create complex renderings.
 */
public interface IFigure {

/**
 * Insets that are all 0.  Always returns <code>true<code> for {@link #isEmpty()}.
 */
class NoInsets
	extends Insets
{
	NoInsets() {
		super(0, 0, 0, 0);
	}
	public boolean isEmpty() {
		return true;
	}
}

/**
 * Empty Insets.
 */
Insets NO_INSETS = new NoInsets();

/**
 * The maximum allowable dimension. ({@link Integer#MAX_VALUE},{@link Integer#MAX_VALUE})
 */
public static final Dimension MAX_DIMENSION = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
/**
 * The minimum allowable dimension. (5,5)
 */
public static final Dimension MIN_DIMENSION = new Dimension(5, 5);

/**
 * Adds the given IFigure as a child of this IFigure.
 * @param figure The IFigure to add
 */
void add(IFigure figure);

/**
 * Adds the given IFigure as a child of this IFigure at the given index.
 * @param figure The IFigure to add
 * @param index The index where the IFigure should be added
 */
void add(IFigure figure, int index);

/**
 * Adds the given IFigure as a child of this IFigure with the given constraint.
 * @param figure The IFigure to add
 * @param constraint The newly added IFigure's constraint
 */
void add(IFigure figure, Object constraint);

/**
 * Adds the given IFigure as a child of this IFigure at the given index with the given
 * constraint.
 * @param figure The IFigure to add
 * @param constraint The newly added IFigure's constraint
 * @param index The index where the IFigure should be added
 */
void add(IFigure figure, Object constraint, int index);

/**
 * Registers the given listener as an AncestorListener of this IFigure.
 * @param listener The listener to add
 */
void addAncestorListener(AncestorListener listener);

/**
 * Registers the given listener as a FigureListener of this IFigure.
 * @param listener The listener to add
 */
void addFigureListener(FigureListener listener);

/**
 * Registers the given listener as a FocusListener of this IFigure.
 * @param listener The listener to add
 */
void addFocusListener(FocusListener listener);

/**
 * Registers the given listener as a KeyListner of this IFigure.
 * @param listener The listener to add
 */
void addKeyListener(KeyListener listener);

/**
 * Registers the given listener as a MouseListener of this IFigure.
 * @param listener The listener to add
 */
void addMouseListener(MouseListener listener);

/**
 * Registers the given listener as a MouseMotionListener of this IFigure.
 * @param listener The listener to add
 */
void addMouseMotionListener(MouseMotionListener listener);

/**
 * Called after this IFigure is added to its parent.
 */
void addNotify();

/**
 * Registers the given listener as a PropertyChangeListener of this IFigure.
 * @param listener The listener to add
 */
void addPropertyChangeListener(PropertyChangeListener listener);

/**
 * Registers the given listener as a PropertyChangeListener of this IFigure, interested
 * only in the given property.
 * @param property The property the listener is interested in
 * @param listener The listener to add
 */
void addPropertyChangeListener(String property, PropertyChangeListener listener);

/**
 * Returns <code>true</code> if the point <code>(x, y)</code> is contained within this
 * IFigure's bounds.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @return <code>true</code> if the point (x,y) is contained in this IFigure's bounds
 */
boolean containsPoint(int x, int y);

/**
 * Returns <code>true</code> if the Point p is contained within this IFigure's bounds.
 * @param p The point
 * @return <code>true</code> if the Point p is contained within this IFigure's bounds
 */
boolean containsPoint(Point p);

/**
 * Erases this IFigure.  
 */
void erase();

/**
 * Returns the IFigure at the specified location. May return <code>this</code> or
 * <code>null</code>.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @return The IFigure at the specified location
 */
IFigure findFigureAt(int x, int y);

/**
 * Returns the IFigure at the specified location. May return <code>this</code> or
 * <code>null</code>.
 * @param p The point
 * @return The IFigure at the specified location
 */
IFigure findFigureAt(Point p);

/**
 * Returns the IFigure at the specified location, excluding any IFigures in
 * <code>collection</code>. May return <code>this</code> or <code>null</code>.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @param collection A collection of IFigures to be excluded
 * @return The IFigure at the specified location, excluding any IFigures in collection
 */
IFigure findFigureAtExcluding(int x, int y, Collection collection);

/**
 * Returns the IFigure located at the given location which will accept mouse events.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @return The IFigure located at the given location which will accept mouse events
 */
IFigure findMouseEventTargetAt(int x, int y);

/**
 * Returns the background color.
 * @return The background color
 */
Color getBackgroundColor();

/**
 * Returns the current border by reference.
 * @return The current border
 */
Border getBorder();

/**
 * Returns the smallest rectangle completely enclosing the IFigure. Implementation may
 * return the Rectangle by reference. For this reason, callers of this method must not
 * modify the returned Rectangle.
 * @return This IFigure's bounds
 */
Rectangle getBounds();

/**
 * Returns the rectangular area within this Figure's bounds in which children will be
 * placed (via {@link LayoutManager LayoutManagers}) and the painting of children will be
 * clipped.
 * @return The client area
 */
Rectangle getClientArea();

/**
 * Copies the client area into the specificied Recangle, and returns that rectangle for 
 * convenience.
 * @param rect The destination rectangle for the client area
 * @return The same instance that was passed in, modified to contain the client area
 */
Rectangle getClientArea(Rectangle rect);

/**
 * Returns an unmodifiable list of children by reference.
 * @return An unmodifiable list of children by reference
 */
List getChildren();

/**
 * Returns the Cursor used when the mouse is over this IFigure.
 * @return The Cursor used when the mouse is over this IFigure
 */
Cursor getCursor();

/**
 * Returns the current Font by reference.
 * @return The current Font
 */
Font getFont();

/**
 * Returns the foreground color.
 * @return The foreground color
 */
Color getForegroundColor();

/**
 * Returns the current Insets.  May be returned by reference.
 * @return The current Insets.
 */
Insets getInsets();

/**
 * Returns the current LayoutManager by reference.
 * @return The current LayoutManager by reference
 */
LayoutManager getLayoutManager();

/**
 * Returns a hint indicating the largest desireable size for the IFigure. Returned
 * Dimension is by value.
 * @return The maximum size
 */
Dimension getMaximumSize();

/**
 * Returns a hint indicating the smallest desireable size for the IFigure. Returned
 * Dimension is by value.
 * @return The minimum size
 */
Dimension getMinimumSize();

/**
 * Returns the IFigure that is the current parent of this IFigure or <code>null</code> if
 * there is no parent.
 * @return The current parent of this IFigure
 */
IFigure getParent();

/**
 * Returns the desireable size for this IFigure. The returned value should not be modified.
 * @return The preferred size
 */
Dimension getPreferredSize();

/**
 * Returns the desireable size for this IFigure using the provided width and height hints.
 * Returned Dimension is by value.  If a hint is less than or equal to 0 (usually, it's
 * set to -1), it means that hint should be ignored.
 * 
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size
 */ 
Dimension getPreferredSize(int wHint, int hHint);

/**
 * Returns the current size. Returned Dimension is by value.
 * @return The current size
 */
Dimension getSize();

/**
 * Returns a IFigure that is the tooltip for this IFigure.
 * @return This IFigure's tooltip
 */
IFigure getToolTip();

/**
 * Returns the UpdateManager for this IFigure by reference.
 * @return The update manager
 */
UpdateManager getUpdateManager();

/**
 * Called when this IFigure has gained focus.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link FocusListener} with
 * this IFigure.
 * @param event The focus event
 */
void handleFocusGained(FocusEvent event);

/**
 * Called when this IFigure has lost focus.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link FocusListener} with
 * this IFigure.
 * @param event The focus event
 */
void handleFocusLost(FocusEvent event);

/**
 * Called when a key is pressed while this IFigure has focus.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link KeyListener} with
 * this IFigure.
 * @param event The key event */
void handleKeyPressed(KeyEvent event);

/**
 * Called when a key is released while this IFigure has focus.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link KeyListener} with
 * this IFigure.
 * @param event The key event
 */
void handleKeyReleased(KeyEvent event);

/**
 * Called when a mouse button has been double-clicked while within this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseListener} with
 * this IFigure.
 * @param event The mouse event
 */
void handleMouseDoubleClicked(MouseEvent event);

/**
 * Called when the mouse has been dragged within this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseMotionListener}
 * with this IFigure.
 * @param event The mouse event
 */
void handleMouseDragged(MouseEvent event);

/**
 * Called when the mouse has entered this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseMotionListener}
 * with this IFigure.
 * @param event The mouse event
 */
void handleMouseEntered(MouseEvent event);

/**
 * Called when the mouse has exited this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseMotionListener}
 * with this IFigure.
 * @param event The mouse event
 */
void handleMouseExited(MouseEvent event);

/**
 * Called when the mouse has hovered over this IFigure.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseMotionListener}
 * with this IFigure.
 * @param event The mouse event
 */
void handleMouseHover(MouseEvent event);

/**
 * Called when the mouse has moved within this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseMotionListener}
 * with this IFigure.
 * @param event The mouse event
 */
void handleMouseMoved(MouseEvent event);

/**
 * Called when a mouse button has been pressed while within this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseListener} with
 * this IFigure.
 * @param event The mouse event
 */
void handleMousePressed(MouseEvent event);

/**
 * Called when a mouse button has been released while within this IFigure's bounds.
 * <p><b>NOTE</b>: You should not override this method. If you are interested in receiving
 * notification of this type of event, you should register a {@link MouseListener} with
 * this IFigure.
 * @param event The mouse event
 */
void handleMouseReleased(MouseEvent event);

/**
 * Returns <code>true</code> if this IFigure has focus.
 * @return <code>true</code> if this IFigure has focus
 */
boolean hasFocus();

/**
 * This method is <b>for internal purposes only</b> and should not be called.
 * @return The event dispatcher
 */
EventDispatcher internalGetEventDispatcher();

/**
 * Returns <code>true</code> if this IFigure's bounds intersect with the given Rectangle.
 * Figure is asked so that non-rectangular IFigures can reduce the frequency of paints.
 * @param rect The rectangle to check for intersection
 * @return <code>true</code> if this IFigure's bounds intersect with the given Rectangle
 */
boolean intersects(Rectangle rect);

/**
 * Invalidates this IFigure.
 */
void invalidate();

/**
 * Returns <code>true</code> if this IFigure is enabled.
 * @return <code>true</code> if this IFigure is enabled
 */
boolean isEnabled();

/**
 * Returns <code>true</code> if this IFigure can gain focus on a 
 * {@link org.eclipse.swt.events.TraverseEvent}.
 * @return <code>true</code> if this IFigure can gain focus on a TraverseEvent
 */
boolean isFocusTraversable();

/**
 * Returns <code>true</code> if this IFigure is opaque.
 * @return <code>true</code> if this IFigure is opaque
 */
boolean isOpaque();

/**
 * Returns <code>true</code> if this IFigure can receive focus on a call to
 * {@link #requestFocus()}.
 * @return <code>true</code> if this IFigure can receive focus on a call to requestFocus()
 */
boolean isRequestFocusEnabled();

/**
 * Returns <code>true</code> if this IFigure is visible.
 * @return <code>true</code> if this IFigure is visible
 */
boolean isVisible();

/**
 * Paints this IFigure and its children.
 * @param graphics The Graphics object used for painting
 */
void paint(Graphics graphics);

/**
 * Removes the given IFigure from this IFigure's list of children.
 * @param figure The IFigure to remove
 */
void remove(IFigure figure);

/**
 * Unregisters the given listener, so that it will no longer receive notification of
 * ancestor events.
 * @param listener The listener to remove
 */
void removeAncestorListener(AncestorListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of
 * IFigure events.
 * @param listener The listener to remove
 */
void removeFigureListener(FigureListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of focus
 * events.
 * @param listener The listener to remove
 */
void removeFocusListener(FocusListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of mouse
 * events.
 * @param listener The listener to remove
 */
void removeMouseListener(MouseListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of mouse
 * motion events.
 * @param listener The listener to remove
 */
void removeMouseMotionListener(MouseMotionListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of any
 * property changes.
 * @param listener The listener to remove
 */
void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Unregisters the given listener, so that it will no longer receive notification of
 * changes in the given property. This will only unregister the listener for the given
 * property. If the listener is registered to listen to other properties, this will not
 * affect the notification of the listener regarding those properties.
 * @param property The property that the listener is no longer interested in
 * @param listener The listener no longer interested in the property
 */
void removePropertyChangeListener(String property, PropertyChangeListener listener);

/**
 * Called before this IFigure is removed from its parent.
 */
void removeNotify();

/**
 * Repaints this IFigure.
 */
void repaint();

/**
 * Repaints the rectangular area within this IFigure whose upper-left corner is located at
 * the point <code>(x,y)</code> and whose width and height are <code>w</code> and
 * <code>h</code>, respectively.
 * @param x The X coordinate of the area to repaint
 * @param y The Y coordinate of the area to repaint
 * @param w The width of the area to repaint
 * @param h The height of the area to repaint
 */
void repaint(int x, int y, int w, int h);

/**
 * Repaints the rectangular area within this IFigure represented by <code>rect</code>.
 * @param rect The rectangular area to be repainted
 */
void repaint(Rectangle rect);

/**
 * Requests focus from the {@link EventDispatcher}.
 */
void requestFocus();

/**
 * Revalidates this IFigure.
 */
void revalidate();

/**
 * Sets the background color.
 * @param c The new background color
 */
void setBackgroundColor(Color c);

/**
 * Sets the border.
 * @param b The new border
 */
void setBorder(Border b);

/**
 * Sets the bounds to the given rectangle.
 * @param rect The new bounds
 */
void setBounds(Rectangle rect);

/**
 * @deprecated Set the constraint while adding the Figure.
 * Sets the constraint of a previously added child.
 * @param child The IFigure whose constraint is being set
 * @param constraint The constraint
 */
void setConstraint(IFigure child, Object constraint);

/**
 * Sets the cursor.
 * @param cursor The new cursor
 */
void setCursor(Cursor cursor);

/**
 * Sets this IFigure to be enabled.
 * @param value <code>true</code> if this IFigure should be enabled
 */
void setEnabled(boolean value);

/**
 * Sets the ability for this IFigure to gain focus on a 
 * {@link org.eclipse.swt.events.TraverseEvent}.
 * @param value <code>true</code> if this IFigure should gain focus on a TraverseEvent
 */
void setFocusTraversable(boolean value);

/**
 * Sets the font.
 * @param f The new font
 */
void setFont(Font f);

/**
 * Sets the foreground color.
 * @param c The new foreground color
 */
void setForegroundColor(Color c);

/**
 * Sets the LayoutManager.
 * @param lm The new layout manager
 */
void setLayoutManager(LayoutManager lm);

/**
 * Sets the location of this IFigure.
 * @param p The new location
 */
void setLocation(Point p);

/**
 * Sets the maximum size this IFigure can be.
 * @param size The new maximum size
 */
void setMaximumSize(Dimension size);

/**
 * Sets the minimum size this IFigure can be.
 * @param size The new minimum size
 */
void setMinimumSize(Dimension size);

/**
 * Sets this IFigure to be opaque if <i>isOpaque</i> is <code>true</code> and transparent
 * if <i>isOpaque</i> is <code>false</code>.
 * @param isOpaque <code>true</code> is this IFigure should be opaque
 */
void setOpaque(boolean isOpaque);

/**
 * Sets this IFigure's parent.
 * @param parent The new parent IFigure
 */
void setParent(IFigure parent);

/**
 * Sets this IFigure's preferred size.
 * @param size The new preferred size
 */
void setPreferredSize(Dimension size);

/**
 * Sets the ability for this Figure to gain focus on a call to {@link #requestFocus()}.
 * @param requestFocusEnabled <code>true</code> if this IFigure should gain focus on a call
 * to requestFocus()
 */
void setRequestFocusEnabled(boolean requestFocusEnabled);

/**
 * Sets this IFigure's size.
 * @param d The new size
 */
void setSize(Dimension d);

/**
 * Sets this IFigure's size.
 * @param w The new width
 * @param h The new height
 */
void setSize(int w, int h);

/**
 * Sets a tooltip that is displayed when the mouse hovers over this IFigure.
 * @param figure The tooltip IFigure
 */
void setToolTip(IFigure figure);

/**
 * Sets the UpdateManager.
 * @param updateManager The new update manager
 */
void setUpdateManager(UpdateManager updateManager);

/**
 * Sets this IFigure's visibility.
 * @param visible <code>true</code> if this IFigure should be visible
 */
void setVisible(boolean visible);

/**
 * Moves this IFigure <code>x</code> pixels horizontally and <code>y</code> pixels
 * vertically.
 * @param x The amount to move this IFigure horizontally
 * @param y The amount to move this IFigure vertically
 */
void translate(int x, int y);

/**
 * Translates a Translatable from this IFigure's parent's coordinates to this IFigure's
 * local coordinates.
 * @param t The object to translate
 */
void translateFromParent(Translatable t);

/**
 * Translates a Translatable from this IFigure's coordinates to the top-most parent's
 * coordinates.
 * @param t The object to translate
 */
void translateToAbsolute(Translatable t);

/**
 * Translates a Translatable from this IFigure's coordinates to its parent's coordinates.
 * @param t The object to translate
 */
void translateToParent(Translatable t);

/**
 * Translates a Translatable from the top-most parent's coordinates to local coordinates.
 * @param t The object to translate
 */
void translateToRelative(Translatable t);

/**
 * Causes this IFigure to layout itself, as well as its children.
 */
void validate();

}