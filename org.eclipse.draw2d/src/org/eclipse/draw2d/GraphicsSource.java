package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides a {@link org.eclipse.draw2d.Graphics} object for painting.
 */
public interface GraphicsSource {

/**
 * Returns a Graphics for the rectangular region requested. May return <code>null</code>.
 * @param region The rectangular region
 * @return A new Graphics object for the given region
 */
Graphics getGraphics(Rectangle region);

/**
 * Tells the GraphicsSource that you have finished using that region.
 * @param region The rectangular region that that no longer needs the Graphics
 */
void flushGraphics(Rectangle region);

}