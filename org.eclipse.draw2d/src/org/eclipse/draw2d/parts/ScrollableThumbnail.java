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
package org.eclipse.draw2d.parts;

import java.beans.*;

import org.eclipse.swt.SWT;
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
private FigureListener figureListener = new FigureListener() {
	public void figureMoved(IFigure source) {
		reconfigureSelectorBounds();
	}
};
private PropertyChangeListener propListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		reconfigureSelectorBounds();
	}
};
private KeyListener keyListener = new KeyListener.Stub() {
	public void keyPressed(KeyEvent ke) {
		int moveX = (int)(viewport.getClientArea().width * .25); 
		int moveY = (int)(viewport.getClientArea().height * .25);
		if (ke.keycode == SWT.ARROW_LEFT || ke.keycode == SWT.HOME)
			viewport.setViewLocation(viewport.getViewLocation().translate(-moveX, 0));
		else if (ke.keycode == SWT.ARROW_RIGHT || ke.keycode == SWT.END)
			viewport.setViewLocation(viewport.getViewLocation().translate(moveX, 0));
		else if (ke.keycode == SWT.ARROW_UP || ke.keycode == SWT.PAGE_UP)
			viewport.setViewLocation(viewport.getViewLocation().translate(0, -moveY));
		else if (ke.keycode == SWT.ARROW_DOWN  || ke.keycode == SWT.PAGE_DOWN)
			viewport.setViewLocation(viewport.getViewLocation().translate(0, moveY));
		}
	};

/**
 * Creates a new ScrollableThumbnail. */
public ScrollableThumbnail() {
	super();
	initialize();
}

/**
 * Creates a new ScrollableThumbnail that synchs with the given Viewport.
 * @param port The Viewport */
public ScrollableThumbnail(Viewport port) {
	super();
	setViewport(port);
	initialize();
}

/**
 * @see Thumbnail#deactivate()
 */
public void deactivate() {
	viewport.removePropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION, propListener);
	viewport.removeFigureListener(figureListener);
	super.deactivate();
}

private void initialize() {
	selector = new SelectorFigure();
	selector.addMouseListener(syncher = new ScrollSynchronizer());
	selector.addMouseMotionListener(syncher);
	selector.setFocusTraversable(true);
	selector.addKeyListener(keyListener);
	add(selector);
	ClickScrollerAndDragTransferrer transferrer = 
				new ClickScrollerAndDragTransferrer();
	addMouseListener(transferrer);
	addMouseMotionListener(transferrer);
}

private void reconfigureSelectorBounds() {
	Rectangle rect = new Rectangle();
	Point offset = viewport.getViewLocation();
	offset.x -= viewport.getHorizontalRangeModel().getMinimum();
	offset.y -= viewport.getVerticalRangeModel().getMinimum();
	rect.setLocation(offset);
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
 * @param scaleX The X scale
 * @param scaleY The Y scale
 * @see org.eclipse.draw2d.parts.Thumbnail#setScales(float, float) */
protected void setScales(float scaleX, float scaleY) {
	if (scaleX == getScaleX() && scaleY == getScaleY())
		return;
		
	super.setScales(scaleX, scaleY);
	
	reconfigureSelectorBounds();
}

/**
 * Sets the Viewport that this ScrollableThumbnail will synch with.
 * @param port The Viewport */
public void setViewport(Viewport port) {
	port.addPropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION, propListener);
	port.addFigureListener(figureListener);
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
		RGB rgb = ColorConstants.menuBackgroundSelected.getRGB();
		int fillColor = pData.getPixel(rgb);
		ImageData iData = new ImageData(1, 1, 24, pData);
		iData.setPixel(0, 0, fillColor);
		iData.setAlpha(0, 0, 55);
		image = new Image(display, iData);
	}
	
	protected void finalize() {
		image.dispose();
	}

	public void paintFigure(Graphics g) {
		Rectangle bounds = getBounds().getCopy();		

		// Avoid drawing images that are 0 in dimension
		if (bounds.width < 5 || bounds.height < 5)
			return;
			
		// Don't paint the selector figure if the entire source is visible.
		Dimension thumbnailSize = new Dimension(getThumbnailImage());
		// expand to compensate for rounding errors in calculating bounds
		Dimension size = getSize().getExpanded(1, 1); 
		if (size.contains(thumbnailSize))
			return;

		bounds.height--;
		bounds.width--;
		g.drawImage(image, iBounds, bounds);
		
		g.setForegroundColor(ColorConstants.menuBackgroundSelected);
		g.drawRectangle(bounds);
	}

}

}