package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.*;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane 
	extends FreeformLayeredPane 
{

private double zoom = 1.0;

/**
 * @see org.eclipse.draw2d.Figure#getClientArea()
 */
public Rectangle getClientArea(Rectangle rect) {
	super.getClientArea(rect);
	rect.width /= zoom;
	rect.height /= zoom;
	rect.x /= zoom;
	rect.y /= zoom;
	return rect;
}

/**
 * @see org.eclipse.draw2d.FreeformLayeredPane#getFreeformExtent()
 */
public Rectangle getFreeformExtent() {
	return super.getFreeformExtent();
}

/**
 *  * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics) */
protected void paintClientArea(Graphics graphics) {
	if (getChildren().isEmpty())
		return;
	if (zoom == 1.0) {
		super.paintClientArea(graphics);
	} else {
		ScaledGraphics g = new ScaledGraphics(graphics);
		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
		if (!optimizeClip)
			g.clipRect(getBounds().getCropped(getInsets()));
		g.scale(zoom);
		g.pushState();
		paintChildren(g);
		g.dispose();
		graphics.restoreState();
	}
}

/**
 * @see org.eclipse.draw2d.FreeformLayeredPane#setFreeformBounds(Rectangle)
 */
public void setFreeformBounds(Rectangle bounds) {
	super.setFreeformBounds(bounds);
}

/**
 * Sets the zoom level
 * @param newZoom The new zoom level */
public void setZoom(double newZoom) {
	zoom = newZoom;
	superFireMoved();
	getFreeformHelper().invalidate();
}

/**
 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
 */
public void translateToParent(Translatable t) {
	t.performScale(zoom);
}

/**
 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
 */
public void translateFromParent(Translatable t) {
	t.performScale(1 / zoom);
}

/**
 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
 */
protected final boolean useLocalCoordinates() {
	return false;
}

}