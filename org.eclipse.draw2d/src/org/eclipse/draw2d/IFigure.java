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
 * An interface for graphical figures.
 */
public interface IFigure
{

class NoInsets
	extends Insets
{
	NoInsets(){
		super(0,0,0,0);
	}
	public boolean isEmpty(){return true;}
}

Insets NO_INSETS = new NoInsets();

public static final Dimension
	MAX_DIMENSION = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE),
	MIN_DIMENSION = new Dimension(5,5);

/**
 * Adds the given IFigure as a child of this IFigure.
 */
void add(IFigure figure);

/**
 * Adds the given IFigure as a child of this IFigure at the
 * given index.
 */
void add(IFigure figure, int index);

/**
 * Adds the given IFigure as a child of this IFigure with
 * the given constraint.
 */
void add(IFigure figure, Object constraint);

/**
 * Adds the given IFigure as a child of this IFigure at the 
 * given index with the given constraint.
 */
void add(IFigure figure, Object constraint, int index);

/**
 * Registers the given listener as an AncestorListener of this
 * IFigure.
 */
void addAncestorListener(AncestorListener ancestorListener);

/**
 * Registers the given listener as a FigureListener of this
 * IFigure.
 */
void addFigureListener(FigureListener fListener);

/**
 * Registers the given listener as a FocusListener of this
 * IFigure.
 */
void addFocusListener(FocusListener fl);

/**
 * Registers the given listener as a KeyListner of this
 * IFigure.
 */
void addKeyListener(KeyListener keyListner);

/**
 * Registers the given listener as a MouseListener of this
 * IFigure.
 */
void addMouseListener(MouseListener mListener);

/**
 * Registers the given listener as a MouseMotionListener of 
 * this IFigure.
 */
void addMouseMotionListener(MouseMotionListener mListener);

/**
 * Called after this figure is added to its parent.
 */
void addNotify();

/**
 * Registers the given listener as a PropertyChangeListener of 
 * this IFigure.
 */
void addPropertyChangeListener(PropertyChangeListener pListener);

/**
 * Registers the given listener as a PropertyChangeListener of 
 * this IFigure, interested only in the given property.
 */
void addPropertyChangeListener(String property, PropertyChangeListener listener);

/**
 * Returns true if the point <code>(x, y)</code> is contained
 * within this IFigures bounds.
 */
boolean containsPoint(int x, int y);

/**
 * Returns true if the Point p is contained within this IFigures 
 * bounds.
 */
boolean containsPoint(Point p);

/**
 * Erases this IFigure.  
 */
void erase();

/**
 * Returns the figure at the specified location. May return 
 * <b>this</b> or <b>null</b>.
 */
IFigure findFigureAt(int x, int y);

/**
 * Returns the figure at the specified location. May return 
 * <b>this</b> or <b>null</b>.
 */
IFigure findFigureAt(Point pt);

/**
 * Returns the figure at the specified location, excluding any
 * figures in <code>collection</code>. May return <b>this</b> 
 * or <b>null</b>.
 */
IFigure findFigureAtExcluding(int x, int y, Collection collection);

/**
 * Returns the figure located at the given location which will
 * accept mouse events.
 */
IFigure findMouseEventTargetAt(int x, int y);

/**
 * Returns the background color.
 */
Color getBackgroundColor();

/**
 * Returns the current border by reference.
 */
Border getBorder();

/**
 * Returns the smallest rectangle completely enclosing the figure.
 * Implementation may return the Rectangle by reference.
 * For this reason, callers of this method must not modify the
 * returned Rectangle.
 */
Rectangle getBounds();

/**
 * Returns the rectangular area within this Figure's bounds
 * in which children will be placed (via {@link LayoutManager}s) and
 * the painting of children will be clipped.
 */
Rectangle getClientArea();

/**
 * Copies the client area into the specificied Recangle, and returns that rectangle for convenience
 * @param rect the destination rectangle for the client area
 * @return The same instance that was passed is returned for convenience
 */
Rectangle getClientArea(Rectangle rect);

/**
 * Returns an unmodifiable collection of children by reference.
 */
List getChildren();

/**
 * Returns the cursor used when the mouse is over this figure.
 */
Cursor getCursor();

/**
 * Returns the current font by reference.
 */
Font getFont();

/**
 * Returns the foreground color.
 */
Color getForegroundColor();

/**
 * Returns the current Insets.  May be returned by reference.
 */
Insets getInsets();

/**
 * Returns the current LayoutManager by reference.
 */
LayoutManager getLayoutManager();

/**
 * Returns a hint indicating the largest desireable size for the figure.
 * Returned Dimension is by value.
 */
Dimension getMaximumSize();

/**
 * Returns a hint indicating the smallest desireable size for the figure.
 * Returned Dimension is by value.
 */
Dimension getMinimumSize();

/**
 * Returns the current Parent.
 */
IFigure getParent();

/**
 * Returns the desireable size for this figure. The returned value 
 * should not be modified.
 */
Dimension getPreferredSize();

/**
 * Returns the desireable size for this figure using the provided 
 * width and height hints. Returned Dimension is by value.
 */ 
Dimension getPreferredSize(int wHint, int hHint);

/**
 * Returns the current size. Returned Dimension is by value.
 */
Dimension getSize();

/**
 * Returns an IFigure that is the tooltip for this Figure.
 */
IFigure getToolTip();

/**
 * Returns the UpdateManager for this figure by reference.
 */
UpdateManager getUpdateManager();

/**
 * Called when this figure has gained focus.
 */
void handleFocusGained(FocusEvent fe);

/**
 * Called when this figure has lost focus.
 */
void handleFocusLost(FocusEvent fe);

void handleKeyPressed(KeyEvent ke);

void handleKeyReleased(KeyEvent ke);

/**
 * Called when a mouse button has been double-clicked while
 * within this figure's bounds.
 */
void handleMouseDoubleClicked(MouseEvent me);

/**
 * Called when the mouse has been dragged within this
 * figure's bounds.
 */
void handleMouseDragged(MouseEvent me);

/**
 * Called when the mouse has entered this figure's bounds.
 */
void handleMouseEntered(MouseEvent me);

/**
 * Called when the mouse has exited this figure's bounds.
 */
void handleMouseExited(MouseEvent me);

/**
 * Called when called when the mouse has hovered over this
 * figure.
 */
void handleMouseHover(MouseEvent me);

/**
 * Called when the mouse has moved within this figure's
 * bounds.
 */
void handleMouseMoved(MouseEvent me);

/**
 * Called when a mouse button has been pressed while within
 * this figure's bounds.
 */
void handleMousePressed(MouseEvent me);

/**
 * Called when a mouse button has been released while
 * within this figure's bounds.
 */
void handleMouseReleased(MouseEvent me);

/**
 * Returns true if this figure has focus.
 */
boolean hasFocus();

/**
 * This method is for internal purposes only and should not
 * be called.
 */
EventDispatcher internalGetEventDispatcher();

/**
 * Returns true if this figure needs to paint anywhere inside r.
 * Figure is asked so that non-rectangular figures can reduce 
 * the frequency of paints.
 */
boolean intersects(Rectangle r);

/**
 * Invalidates this figure.
 */
void invalidate();

/**
 * Returns true if this figure is enabled.
 */
boolean isEnabled();

/**
 * Returns true if this figure can gain
 * focus on a TraverseEvent.
 */
boolean isFocusTraversable();

/**
 * Returns true if this figure is opaque.
 */
boolean isOpaque();

/**
 * Returns <code>true</code> if this figure can receive focus
 * on a call to requestFocus().
 */
boolean isRequestFocusEnabled();

/**
 * Returns true if this figure is visible.
 */
boolean isVisible();

/**
 * Paints this figure and its children.
 */
void paint(Graphics graphics);

/**
 * Removes the given figure from this figure's list of 
 * children.
 */
void remove(IFigure figure);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of ancestor events.
 */
void removeAncestorListener(AncestorListener ancestorListener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of figure events.
 */
void removeFigureListener(FigureListener listener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of focus events.
 */
void removeFocusListener(FocusListener listener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of mouse events.
 */
void removeMouseListener(MouseListener listener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of mouse motion events.
 */
void removeMouseMotionListener(MouseMotionListener listener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of any property changes.
 */
void removePropertyChangeListener(PropertyChangeListener listener);

/**
 * Unregisters the given listener, so that it will no longer
 * receive notification of changes in the given property.
 */
void removePropertyChangeListener(String property, PropertyChangeListener listener);

/**
 * Called before this figure is removed from its parent.
 */
void removeNotify();

/**
 * Repaints this figure.
 */
void repaint();

/**
 * Repaints the rectangular area within this figure whose upper-left 
 * corner is located at the point <code>(x,y)</code> and whose width 
 * and height are <code>w</code> and <code>h</code>, respectively.
 */
void repaint(int x, int y, int w, int h);

/**
 * Repaints the rectangular area within this figure represented by
 * <code>rect</code>.
 */
void repaint(Rectangle rect);

/**
 * Requests focus from the {@link EventDispatcher}.
 */
void requestFocus();

/**
 * Revalidates this figure.
 */
void revalidate();

/**
 * Sets the background color.
 */
void setBackgroundColor(Color c);

/**
 * Sets the border.
 */
void setBorder(Border b);

/**
 * Sets the bounds to the given rectangle.
 */
void setBounds(Rectangle r);

/*
 * Sets the constraint of a previously added child.
 */
void setConstraint(IFigure child, Object constraint);

/**
 * Sets the cursor.
 */
void setCursor(Cursor cursor);

/**
 * Sets this figure to be enabled.
 */
void setEnabled(boolean value);

/**
 * Sets the ability for this figure to gain
 * focus on a TraverseEvent.
 */
void setFocusTraversable(boolean value);

/**
 * Sets the font.
 */
void setFont(Font f);

/**
 * Sets the foreground color.
 */
void setForegroundColor(Color c);

/**
 * Sets the LayoutManager.
 */
void setLayoutManager(LayoutManager lm);

/**
 * Sets the location of this figure.
 */
void setLocation(Point p);

/**
 * Sets the maximum size this figure can be.
 */
void setMaximumSize(Dimension size);

/**
 * Sets the minimum size this figure can be.
 */
void setMinimumSize(Dimension size);

/**
 * Sets this figure to be opaque if <code>isOpaque</code> is
 * true and transparent if <code>isOpaque</code> is false.
 */
void setOpaque(boolean isOpaque);

/**
 * Sets this figure's parent.
 */
void setParent(IFigure parent);

/**
 * Sets this figure's preferred size.
 */
void setPreferredSize(Dimension size);

/**
 * Sets the ability for this Figure to gain focus on 
 * a call to requestFocus().
 */
void setRequestFocusEnabled(boolean requestFocusEnabled);

/**
 * Sets this figure's size.
 */
void setSize(Dimension d);

/**
 * Sets this figure's size.
 */
void setSize(int w, int h);

/**
 * Sets a tooltip that is displayed when the mouse hovers over this figure.
 */
void setToolTip(IFigure figure);

/**
 * Sets the UpdateManager.
 */
void setUpdateManager(UpdateManager updateManager);

/**
 * Sets this figure's visibility.
 */
void setVisible(boolean visible);

/**
 * Moves this figure <code>x</code> pixels horizontally and
 * <code>y</code> pixels vertically.
 */
void translate(int x, int y);

/**
 * Translates a Translatable from this figure's parent's
 * coordinates to this figure's local coordinates.
 */
void translateFromParent(Translatable t);

/**
 * Translates a Translatable from this figure's coordinates
 * to the top-most parent's coordinates.
 */
void translateToAbsolute(Translatable t);

/**
 * Translates a Translatable from this figure's coordinates to
 * its parent's coordinates.
 */
void translateToParent(Translatable t);

/**
 * Translates a Translatable from the top-most parent's coordinates 
 * to local coordinates.
 */
void translateToRelative(Translatable t);

/**
 * Causes this figure to layout itself, as well as its children.
 */
void validate();

}