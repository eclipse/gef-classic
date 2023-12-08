/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.handles;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Identifies figures which use an alternative rectangle to place their handles.
 * Normally, handles will appear around a GraphicalEditPart's figure's bounds.
 * However, if that figure has an irregular shape, it may implement this
 * interface to indicate that some rectangle other than its bounding rectangle
 * should be used to place handles.
 *
 * @author hudsonr
 */
public interface HandleBounds extends org.eclipse.draw2d.IFigure {

	/**
	 * Returns the Rectangle around which handles are to be placed. The Rectangle
	 * should be in the same coordinate system as the figure itself.
	 *
	 * @return The rectangle used for handles
	 */
	Rectangle getHandleBounds();

}
