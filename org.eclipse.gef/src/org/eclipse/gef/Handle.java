package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;

/**
 * A simple lightweight object that combines a small hit-test area with
 * a sinlge DragTracker.
 * Handles can be used for resizing, rotating, etc.
 * Handles are hit-tested before anything else.  Handles make it
 * easy to support many types of drag operations.
 */
public interface Handle
{

/**
 * Returns the drag tracker to use when dragging this handle.
 */
DragTracker getDragTracker();

Point getAccessibleLocation();

}
