package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Point;


/**
 * Specializes {@link EditPartViewer} adding the ability to hit-test {@link Handle
 * Handles}.
 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl
 */
public interface GraphicalViewer
	extends EditPartViewer
{

/**
 * Returns the <code>Handle</code> at the specified Point. Returns <code>null</code> if no
 * handle exists at the given Point. The specified point should be relative to the
 * {@link org.eclipse.swt.widgets.Scrollable#getClientArea() client area} for this
 * Viewer's <code>Control</code>.
 * @param p the location relative to the Control's client area
 * @return Handle <code>null</code> or a Handle
 */
Handle findHandleAt(Point p);

}
