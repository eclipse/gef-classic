package org.eclipse.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.beans.*;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * A scaled image representation of a Figure.  If the source Figure is not completely 
 * visible, a SelectorFigure is placed over the thumbnail representing the viewable area 
 * and can be dragged around to scroll the source figure.
 */
public final class ScrollableThumbnail 
	extends Thumbnail
{

private SelectorFigure selector;
private Viewport viewport;
private ScrollBar hBar, vBar;
private ScrollSynchronizer syncher;

/**
 * Creates a new ScrollableThumbnail. */
public ScrollableThumbnail() {
	super();
	initialize();
}

/**
 * Creates a new ScrollableThumbnail that synchs with the given Viewport.
 *  * @param port The Viewport */
public ScrollableThumbnail(Viewport port) {
	super();
	setViewport(port);
	initialize();
}

private void initialize() {
	selector = new SelectorFigure();
	selector.addMouseListener(syncher = new ScrollSynchronizer());
	selector.addMouseMotionListener(syncher);
	add(selector);
	ClickScrollerAndDragTransferrer transferrer = 
				new ClickScrollerAndDragTransferrer();
	addMouseListener(transferrer);
	addMouseMotionListener(transferrer);
}

private void reconfigureSelectorBounds() {
	Rectangle rect = new Rectangle();
	rect.setLocation(viewport.getViewLocation());
	rect.setSize(viewport.getClientArea().getSize());
	rect.scale(getViewportScaleX(), getViewportScaleY());
	rect.translate(getClientArea().getLocation());
	selector.setBounds(rect);
}	

private double getViewportScaleX() {
	return (double)targetSize.width / viewport.getContents().getBounds().width;
}

private double getViewportScaleY() {
	return (double)targetSize.height / viewport.getContents().getBounds().height;
}

/**
 * Reconfigures the SelectorFigure's bounds if the scales have changed.
 * 
 * @param scaleX The X scale * @param scaleY The Y scale
 * @see org.eclipse.draw2d.parts.Thumbnail#setScales(float, float) */
protected void setScales(float scaleX, float scaleY) {
	if (scaleX == getScaleX() && scaleY == getScaleY())
		return;
		
	super.setScales(scaleX, scaleY);
	
	reconfigureSelectorBounds();
}

/**
 * Sets the Viewport that this ScrollableThumbnail will synch with.
 *  * @param port The Viewport */
public void setViewport(Viewport port) {
	port.addPropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION, new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent event) {
			reconfigureSelectorBounds();
		}
	});
	port.addFigureListener(new FigureListener() {
		public void figureMoved(IFigure fig) {
			reconfigureSelectorBounds();
		}
	});
	viewport = port;
}

private class ScrollSynchronizer
	extends MouseMotionListener.Stub
	implements MouseListener
{
	private Point startLocation;
	private Point viewLocation;

	public void mouseDragged(MouseEvent me) {
		Dimension d = me.getLocation().getDifference(startLocation);
		d.scale(1.0f / getViewportScaleX(), 1.0f / getViewportScaleY());
		viewport.setViewLocation(viewLocation.getTranslated(d));
		me.consume();
	}

	public void mousePressed(MouseEvent me) {
		startLocation = me.getLocation();
		viewLocation = viewport.getViewLocation();
		me.consume();
	}

	public void mouseReleased(MouseEvent me) { }
	
	public void mouseDoubleClicked(MouseEvent me) { }
}

private class ClickScrollerAndDragTransferrer
	extends MouseMotionListener.Stub
	implements MouseListener
{
	private boolean dragTransfer;
	public void mouseDragged(MouseEvent me) {
		if (dragTransfer)
			syncher.mouseDragged(me);
	}
	public void mousePressed(MouseEvent me) {
		if (!(ScrollableThumbnail.this.getClientArea().contains(me.getLocation())))
			return;
		Dimension selectorCenter = selector.getBounds().getSize().scale(0.5f);
		Point scrollPoint = me.getLocation()
							.getTranslated(getLocation().getNegated())
							.translate(selectorCenter.negate())
							.scale(1.0f / getViewportScaleX(), 1.0f / getViewportScaleY());
		viewport.setViewLocation(scrollPoint);
		syncher.mousePressed(me);
		dragTransfer = true;
	}
	public void mouseReleased(MouseEvent me) {
		syncher.mouseReleased(me);
		dragTransfer = false;
	}
	public void mouseDoubleClicked(MouseEvent me) { }
}

private class SelectorFigure
	extends Figure
{

	private Image image;
	private Rectangle iBounds = new Rectangle(0, 0, 1, 1);
	{
		Display display = Display.getCurrent();
		PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
		RGB rgb = new RGB(255, 157, 20);
		int fillColor = pData.getPixel(rgb);
		ImageData iData = new ImageData(1, 1, 24, pData);
		iData.setPixel(0, 0, fillColor);
		iData.setAlpha(0, 0, 40);
		image = new Image(display, iData);
	}
	
	protected void finalize() {
		image.dispose();
	}

	public void paintFigure(Graphics g) {
		Rectangle bounds = getBounds();		

		// Avoid drawing images that are 0 in dimension
		if (bounds.width < 5 || bounds.height < 5)
			return;
			
		// Don't paint the selector figure if the entire source is visible.
		Dimension thumbnailSize = ScrollableThumbnail.this.getClientArea().getSize();
		// expand to compensate for rounding errors in calculating bounds
		Dimension size = getSize().getExpanded(1, 1); 
		if (size.contains(thumbnailSize))
			return;

		bounds.height--;
		bounds.width--;
		g.drawImage(image, iBounds, bounds);
		bounds.height++;
		bounds.width++;

		g.drawImage(image, iBounds.x, iBounds.y, iBounds.width, iBounds.height, 
							bounds.x, bounds.y, bounds.width - 2, 1);
		g.drawImage(image, iBounds.x, iBounds.y, iBounds.width, iBounds.height, 
							bounds.x, bounds.y + 1, 1, bounds.height - 2);
		g.setForegroundColor(ColorConstants.gray);
		g.drawLine(bounds.x + 2, bounds.bottom() - 1, bounds.right() - 1, bounds.bottom() - 1);
		g.drawLine(bounds.right() - 1, bounds.y + 2, bounds.right() - 1, bounds.bottom() - 1);
	}

}

}