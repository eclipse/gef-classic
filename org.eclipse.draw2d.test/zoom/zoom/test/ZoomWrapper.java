package zoom.test;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class ZoomWrapper
	extends Figure
{

{
	setLayoutManager(new StackLayout());
}

private float zoom;

/**
 * @see org.eclipse.draw2d.Figure#getClientArea()
 */
public Rectangle getClientArea(Rectangle rect) {
	super.getClientArea(rect);
	rect.width /= zoom;
	rect.height /= zoom;
	return rect;
}

public Dimension getPreferredSize(int wHint, int hHint) {
	Dimension d = super.getPreferredSize(wHint, hHint);
	int w = getInsets().getWidth();
	int h = getInsets().getHeight();
	return d.getExpanded(-w, -h)
		.scale(zoom)
		.expand(w,h);
}

/**
 * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
 */
protected void paintClientArea(Graphics graphics) {
	if (getChildren().isEmpty())
		return;

	boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

	ScaledGraphics g = new ScaledGraphics(graphics);

	graphics.pushState();
	if (!optimizeClip)
		g.clipRect(getBounds().getCropped(getInsets()));
	g.translate(getBounds().x + getInsets().left, getBounds().y + getInsets().top);
	g.scale(zoom);
	paintChildren(g);
	//g.dispose();
	graphics.popState();
}

public void setZoom(float zoom) {
	this.zoom = zoom;
	revalidate();
	repaint();
}

/**
 * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
 */
public void translateToParent(Translatable t) {
	t.performScale(zoom);
	super.translateToParent(t);
}

/**
 * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
 */
public void translateFromParent(Translatable t) {
	super.translateFromParent(t);
	t.performScale(1/zoom);
}

/**
 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
 */
protected boolean useLocalCoordinates() {
	return true;
}

}
