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
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.*;

/**
 * An implementation of <code>ExposeHelper</code> for use with editparts using a
 * <code>Viewport</code>.
 * @author hudsonr
 * @since 2.0 */
public class ViewportExposeHelper
	extends ViewportHelper
	implements ExposeHelper
{

private Insets exposeMargin;
private int minimumFrameCount = 3;
private int maximumFrameCount = 8;

/**
 * Constructs a new ViewportExposeHelper on the specified GraphicalEditPart. The
 * GraphicalEditPart must have a <code>Viewport</code> somewhere between its
 * <i>contentsPane</i> and its <i>figure</i> inclusively.
 * @param owner the GraphicalEditPart that owns the Viewport */
public ViewportExposeHelper(GraphicalEditPart owner) {
	super(owner);
}

/**
 * Exposes the descendant EditPart by smoothly scrolling the <code>Viewport</code>. The
 * smoothness is determined by the minimum and maximum frame count, and the overall
 * amount being scrolled.
 * @see org.eclipse.gef.ExposeHelper#exposeDescendant(EditPart) */
public void exposeDescendant(EditPart part) {
	Viewport port = findViewport(owner);
	if (port == null)
		return;
	IFigure target = ((GraphicalEditPart)part).getFigure();

/* All calculations are done relative to the contents of the viewport. The expose margin
 * is added in absolute coordinates, and then taken back to relative viewport coordinates.
 */
	Rectangle exposeRegion = target.getBounds().getCopy();
	target.translateToAbsolute(exposeRegion);
	if (exposeMargin != null)
		exposeRegion.expand(exposeMargin);
	port.getContents().translateToRelative(exposeRegion);

	Point offset = port.getContents().getBounds().getLocation();
//By substracting the offset, the region is now the difference from the contents origin.
	exposeRegion.translate(offset.negate());
	exposeRegion.translate(
		port.getHorizontalRangeModel().getMinimum(),
		port.getVerticalRangeModel().getMinimum());

	Dimension viewportSize = port.getClientArea().getSize();
	Point topLeft = exposeRegion.getTopLeft();
	Point bottomRight = exposeRegion.
		getBottomRight().
		translate(viewportSize.getNegated());

	Point finalLocation = Point.min(topLeft,
		Point.max(bottomRight, port.getViewLocation()));
	Point startLocation = port.getViewLocation();

	int dx = finalLocation.x - startLocation.x;
	int dy = finalLocation.y - startLocation.y;

	int FRAMES = (Math.abs(dx) + Math.abs(dy)) / 15;
	FRAMES = Math.max(FRAMES, getMinimumFrameCount());
	FRAMES = Math.min(FRAMES, getMaximumFrameCount());

	int stepX = Math.min((dx / FRAMES), (viewportSize.width / 3));
	int stepY = Math.min((dy / FRAMES), (viewportSize.height / 3));

	for (int i = 1; i < FRAMES; i++) {
		port.setViewLocation(startLocation.x + stepX * i, startLocation.y + stepY * i);
		port.getUpdateManager().performUpdate();
	}
	port.setViewLocation(finalLocation);
}

/**
 * Returns the maximumFrameCount.
 * @return int
 */
public int getMaximumFrameCount() {
	return maximumFrameCount;
}

/**
 * Returns the minimumFrameCount.
 * @return int
 */
public int getMinimumFrameCount() {
	return minimumFrameCount;
}

/**
 * Sets the amount of margin to be left around the descendant being exposed.  There is no
 * margin by default.
 * @param margin the margin in pixels
 */
public void setMargin(Insets margin) {
	exposeMargin = margin;
}

/**
 * Sets the maximumFrameCount.
 * @param maximumFrameCount The maximumFrameCount to set
 */
public void setMaximumFrameCount(int maximumFrameCount) {
	this.maximumFrameCount = maximumFrameCount;
}

/**
 * Sets the minimumFrameCount.
 * @param minimumFrameCount The minimumFrameCount to set
 */
public void setMinimumFrameCount(int minimumFrameCount) {
	this.minimumFrameCount = minimumFrameCount;
	if (getMaximumFrameCount() < minimumFrameCount)
		setMaximumFrameCount(minimumFrameCount);
}

}
