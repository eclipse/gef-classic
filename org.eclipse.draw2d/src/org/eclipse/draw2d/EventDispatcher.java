package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Control;

abstract public class EventDispatcher {

static public abstract class AccessibilityDispatcher
	implements AccessibleControlListener, AccessibleListener
{
	public void getChild(AccessibleControlEvent e) {}
}

abstract public void dispatchFocusGained(FocusEvent e);

abstract public void dispatchFocusLost(FocusEvent e);

abstract public void dispatchKeyPressed(KeyEvent e);

abstract public void dispatchKeyReleased(KeyEvent e);

abstract public void dispatchKeyTraversed(TraverseEvent e);

abstract public void dispatchMouseDoubleClicked(MouseEvent me);

abstract public void dispatchMouseEntered(MouseEvent e);

abstract public void dispatchMouseExited(MouseEvent e);

abstract public void dispatchMouseHover(MouseEvent me);

abstract public void dispatchMouseMoved(MouseEvent me);

abstract public void dispatchMousePressed(MouseEvent me);

abstract public void dispatchMouseReleased(MouseEvent me);

abstract protected AccessibilityDispatcher getAccessibilityDispatcher();

abstract /*package*/ IFigure getCursorTarget();

abstract /*package*/ IFigure getFocusOwner();

abstract /*package*/ IFigure getMouseTarget();

abstract /*package*/ void releaseCapture();

abstract public void requestFocus(IFigure fig);

abstract public void requestRemoveFocus(IFigure fig);

abstract /*package*/ void setCapture(IFigure figure);

abstract public void setControl(Control control);

abstract public void setRoot(IFigure figure);

abstract /*package*/ void updateCursor();

}