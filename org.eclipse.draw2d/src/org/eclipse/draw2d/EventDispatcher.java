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

abstract /*package*/ IFigure getFocusOwner();

abstract public boolean isCaptured();

abstract protected void releaseCapture();

abstract public void requestFocus(IFigure fig);

abstract public void requestRemoveFocus(IFigure fig);

abstract protected void setCapture(IFigure figure);

abstract public void setControl(Control control);

abstract public void setRoot(IFigure figure);

abstract protected void updateCursor();

}