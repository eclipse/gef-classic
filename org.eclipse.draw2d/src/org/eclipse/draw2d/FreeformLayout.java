/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;

/**
 * A layout for {@link org.eclipse.draw2d.FreeformFigure FreeformFigures}.
 */
public class FreeformLayout
	extends XYLayout
{

/**
 * Returns the point (0,0) as the origin.
 * @see XYLayout#getOrigin(IFigure)
 */
public Point getOrigin(IFigure figure) {
	return new Point();
}

}
