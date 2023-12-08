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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides a {@link org.eclipse.draw2d.Graphics} object for painting.
 */
public interface GraphicsSource {

	/**
	 * Returns a Graphics for the rectangular region requested. May return
	 * <code>null</code>.
	 *
	 * @param region The rectangular region
	 * @return A new Graphics object for the given region
	 */
	Graphics getGraphics(Rectangle region);

	/**
	 * Tells the GraphicsSource that you have finished using that region.
	 *
	 * @param region The rectangular region that that no longer needs the Graphics
	 */
	void flushGraphics(Rectangle region);

}
