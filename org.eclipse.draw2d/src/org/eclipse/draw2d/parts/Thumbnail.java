package org.eclipse.draw2d.parts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Thumbnail is a Figure that displays an image of its source Figure at a 
 * smaller size. The Thumbnail will maintain the aspect ratio of the source 
 * Figure.
 * 
 * @author Eric Bordeau */
public class Thumbnail 
	extends Figure 
	implements UpdateListener
{

private IFigure sourceFigure;
private boolean isDirty;
private float scaleX;
private float scaleY;
private Dimension targetSize = new Dimension(0, 0);

/**
 * Creates a new Thumbnail.  The source Figure must be set separately if you
 * use this constructor. */
public Thumbnail() {
	super();	
}

/**
 * Creates a new Thumbnail with the given IFigure as its source figure.
 *  * @param fig The source figure */
public Thumbnail(IFigure fig) {
	this();
	setSource(fig);
}

private Dimension adjustToAspectRatio(Dimension size, boolean adjustToMaxDimension) {
	Dimension sourceSize = sourceFigure.getSize();
	Dimension borderSize = new Dimension(getInsets().getWidth(), getInsets().getHeight());
	size.expand(borderSize.getNegated());
	int width, height;
	if (adjustToMaxDimension) {
		width  = Math.max(size.width, (int)(size.height * sourceSize.width / (float)sourceSize.height + 0.5));
		height = Math.max(size.height, (int)(size.width * sourceSize.height / (float)sourceSize.width + 0.5));
	} else {
		width  = Math.min(size.width,  (int)(size.height * sourceSize.width / (float)sourceSize.height + 0.5));
		height = Math.min(size.height, (int)(size.width * sourceSize.height / (float)sourceSize.width + 0.5));
	}
	size.width  = width;
	size.height = height;
	return size.expand(borderSize);
}

/**
 * Deactivates this Thumbnail. */
public void deactivate() {
	sourceFigure.getUpdateManager().removeUpdateListener(this);
}

/**
 * Returns the preferred size of this Thumbnail.  The preferred size will be 
 * calculated in a way that maintains the source Figure's aspect ratio.
 * 
 * @param wHint The width hint
 * @param hHint The height hint
 * @return The preferred size */
public Dimension getPreferredSize(int wHint, int hHint) {
	if (prefSize == null)
		return adjustToAspectRatio(getBounds().getSize(), false);
		
	Dimension preferredSize = adjustToAspectRatio(prefSize.getCopy(), true);
	
	if (maxSize == null)
		return preferredSize;
	
	Dimension maximumSize = adjustToAspectRatio(maxSize.getCopy(), true);
	if (preferredSize.contains(maximumSize))
		return maximumSize;
	else
		return preferredSize;
}

/**
 * Returns the scale factor on the X-axis. * @return X scale */
protected float getScaleX() {
	return scaleX;
}

/**
 * Returns the scale factor on the Y-axis.
 * @return Y scale
 */
protected float getScaleY() {
	return scaleY;
}

/**
 * Returns <code>true</code> if the source figure has changed.
 *  * @return <code>true</code> if the source figure has changed */
protected boolean isDirty() {
	return isDirty;
}

/**
 * @see org.eclipse.draw2d.UpdateListener#notifyPainting(Rectangle)
 */
public void notifyPainting(Rectangle damage) {
	setDirty(true);
	repaint();
}

/**
 * @see org.eclipse.draw2d.UpdateListener#notifyValidating()
 */
public void notifyValidating() {
	setDirty(true);
	revalidate();
}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics) */
protected void paintFigure(Graphics graphics) {
	targetSize = getPreferredSize();
	setScales(targetSize.width / (float)sourceFigure.getSize().width,
		     targetSize.height / (float)sourceFigure.getSize().height);
	ScaledGraphics scaledGraphics = new ScaledGraphics(graphics);
	scaledGraphics.scale(getScaleX());
	sourceFigure.paint(scaledGraphics);
}

/**
 * Sets the dirty flag.
 *  * @param value The dirty value */
public void setDirty(boolean value) {
	isDirty = value;
}

/**
 * Sets the X and Y scales for the Thumbnail.  These scales represent the ratio
 * between the source figure and the Thumbnail.   * @param x The X scale * @param y The Y scale */
protected void setScales(float x, float y) {
	scaleX = x;
	scaleY = y;
}

/**
 * Sets the source Figure.  Also sets the scales and creates the necessary
 * update manager.
 *  * @param fig The source figure */
public void setSource(IFigure fig) {
	if (sourceFigure == fig)
		return;
	if (sourceFigure != null)
		sourceFigure.getUpdateManager().removeUpdateListener(this);
	sourceFigure = fig;
	if (sourceFigure != null) {
		setScales((float)getSize().width / (float)sourceFigure.getSize().width,
				(float)getSize().height / (float)sourceFigure.getSize().height);
		sourceFigure.getUpdateManager().addUpdateListener(this);
		repaint();
	}
}

}
