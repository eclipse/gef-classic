package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A specialization of Tool that is used by the {@link
 * org.eclipse.gef.tools.SelectionTool} during a Mouse Drag.  The
 * <code>SelectionTool</code> obtains a <code>DragTracker</code> on mouse down, and
 * forwards all input to that tracker until after the mouse is released. The SelectionTool
 * also obtains DragTrackers in keyboard accessible ways.
 */
public interface DragTracker
	extends Tool
{

/**
 * The <code>SelectionTool</code> supports keyboard accessible drags.  In such scenarios
 * it is up to the SelectionTool to interpret <i>commit</i> and <i>abort</i> keystrokes.
 * Since the DragTracker cannot do this, this method is used to indicate that the User
 * has committed the drag using the keyboard.  Abort is not handled specially, and the
 * DragTracker should peform the usual cleanup in its {@link Tool#deactivate()} method.
 */
void commitDrag();

}
