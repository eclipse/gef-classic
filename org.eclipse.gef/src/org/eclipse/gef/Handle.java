package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * An interface used by the {@link org.eclipse.gef.tools.SelectionTool} to obtain a
 * DragTracker. A GraphicalViewer will return a Handle at a given location. The
 * <code>SelectionTool</code> looks for <code>Handles</code> first whenever the User
 * presses the mouse button. If a Handle is found, it usually offers a DragTracker,
 * although <code>null</code> can also be returned.
 * <P>
 * For keyboard accessibility purposes, a Handle can provide a Point at which the
 * SelectionTool should programmatically place the mouse.
 */
public interface Handle {

/**
 * Returns the DragTracker for dragging this Handle.
 * @return <code>null</code> or a <code>DragTracker</code>
 */
DragTracker getDragTracker();

/**
 * Returns an optional accessibility Point.  This Point is relative to the Control's
 * client area
 * @return <code>null</code> or  */
Point getAccessibleLocation();

}
