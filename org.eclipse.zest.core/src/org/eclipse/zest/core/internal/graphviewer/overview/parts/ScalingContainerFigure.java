/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.overview.parts;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.draw2d.text.CaretInfo;

/**
 * A figure that scales all of its children to always be within its bounds.
 * @author Del Myers
 *
 */
public class ScalingContainerFigure extends Figure {
	private static final int DEFAULT_SIZE = 1000; //the "default" size used for scaling.
	private Rectangle logicalBounds;
	
	/**
	 * 
	 */
	public ScalingContainerFigure() {
		setLogicalBounds(new Rectangle(0,0,DEFAULT_SIZE, DEFAULT_SIZE));
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#isCoordinateSystem()
	 */
	public boolean isCoordinateSystem() {
		return true;
	}

	public double getWidthScale() {
		Dimension size = getSize();
		Dimension logicalSize = getLogicalSize();
		double widthScale = size.width/(double)logicalSize.width;
		return widthScale;
	}
	
	public double getHeightScale() {
		Dimension size = getSize();
		Dimension logicalSize = getLogicalSize();
		double heightScale = size.height/(double)logicalSize.height;
		return heightScale;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#translateFromParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		Dimension size = getSize();
		Dimension logicalSize = getLogicalSize();
		double widthScale = size.width/(double)logicalSize.width;
		double heightScale = size.height/(double)logicalSize.height;
		
		
		if ( t instanceof PrecisionRectangle ) {
			PrecisionRectangle r = (PrecisionRectangle)t;
			r.preciseX *= 1/widthScale;
			r.preciseY *= 1/heightScale;
			r.preciseWidth *= 1/widthScale;
			r.preciseHeight *= 1/heightScale;
			r.updateInts();
		}
		else if ( t instanceof Rectangle ) {
			Rectangle r = (Rectangle)t;
			r.scale(1/widthScale, 1/heightScale);
		}
		else if ( t instanceof CaretInfo ) {
			CaretInfo c = (CaretInfo)t;
			c.performScale(1/heightScale);
		}
		else if ( t instanceof PrecisionDimension ) {
			PrecisionDimension d = (PrecisionDimension)t;
			d.preciseWidth *= 1/widthScale;
			d.preciseHeight *= 1/heightScale;
			d.updateInts();
		}
		else if ( t instanceof Dimension ) {
			Dimension d = (Dimension) t;
			d.scale(1/widthScale, 1/heightScale);
		}
		else if ( t instanceof PrecisionPoint ) {
			PrecisionPoint p = (PrecisionPoint) t;
			p.preciseX *= 1/widthScale;
			p.preciseY *= 1/heightScale;
			p.updateInts();
		}
		else if ( t instanceof Point ) {
			Point p = (Point)t;
			p.scale(1/widthScale, 1/heightScale);
		}
		else if ( t instanceof PointList ) {
			throw new RuntimeException("PointList not supported in AspectRatioScale");
		}
		else {
			throw new RuntimeException( t.toString() + " not supported in AspectRatioScale");
		}
		t.performTranslate((int)(getLogicalReference().x), (int)(getLogicalReference().y));
		//t.performScale(1/widthScale);		
	}
	
	/**
	 * Sets the logical size to the given width and heigth. This will be used
	 * to determine how children should be scaled.
	 * @param w the logical width.
	 * @param h the logical height.
	 */
	public final void setLogicalSize(int w, int h) {
		setLogicalSize(new Dimension(w, h));
	}
	
	/**
	 * Sets the logical size to the given dimension. This will be used to determine
	 * how the children should be scaled.
	 * @param size the logical size.
	 */
	public void setLogicalSize(Dimension size) {
		setLogicalBounds(new Rectangle(new Point(0,0), size));
		invalidate();
	}
	
	/**
	 * Sets the logical size to the dimensions of the bounds, and allows for translation
	 * based on the position of the given rectangle. The top-left corner of the rectangle
	 * will be translated to 0,0 in this scaling container.
	 * @param bounds
	 */
	public void setLogicalBounds(Rectangle bounds) {
		this.logicalBounds = bounds.getCopy();
		invalidate();
	}
	
	/**
	 * Returns the top-left reference point for the bounds of the figures contained within
	 * this container.
	 * @return the logical reference.
	 */
	public Point getLogicalReference() {
		return logicalBounds.getTopLeft();
	}
	
	/**
	 * @return the logicalSize
	 */
	public final Dimension getLogicalSize() {
		return logicalBounds.getSize();
	}
	
	/**
	 * @return the logicalBounds
	 */
	public Rectangle getLogicalBounds() {
		return logicalBounds;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	protected void paintClientArea(Graphics graphics) {
		List children = getChildren();
		if (children.isEmpty())
			return;

		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

		if (useLocalCoordinates()) {
			graphics.translate(
				getBounds().x + getInsets().left,
				getBounds().y + getInsets().top);
			if (isCoordinateSystem()) {
				graphics.scale((float)getWidthScale(), (float)getHeightScale());
				graphics.translate(getLogicalReference().getNegated());
			}
			if (!optimizeClip)
				graphics.clipRect(getClientArea(new Rectangle()));
			graphics.pushState();
			paintChildren(graphics);
			graphics.popState();
			graphics.restoreState();
		} else {
			if (optimizeClip)
				paintChildren(graphics);
			else {
				graphics.clipRect(getClientArea(new Rectangle()));
				graphics.pushState();
				paintChildren(graphics);
				graphics.popState();
				graphics.restoreState();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#translateToParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateToParent(Translatable t) {
		Dimension size = getBounds().getSize();
		Dimension logicalSize = getLogicalSize();
		double widthScale = size.width/(double)logicalSize.width;
		double heightScale = size.height/(double)logicalSize.height;
		t.performTranslate(-getLogicalReference().x, -getLogicalReference().y);
		
		if ( t instanceof PrecisionRectangle ) {
			PrecisionRectangle r = (PrecisionRectangle)t;
			r.preciseX *= widthScale;
			r.preciseY *= heightScale;
			r.preciseWidth *= widthScale;
			r.preciseHeight *= heightScale;
			r.updateInts();
		}
		else if ( t instanceof Rectangle ) {
			Rectangle r = (Rectangle)t;
			//r.performScale(widthScale);
			r.scale(widthScale, heightScale);
		}
		else if ( t instanceof CaretInfo ) {
			CaretInfo c = (CaretInfo)t;
			c.performScale(heightScale);
		}
		else if ( t instanceof PrecisionDimension ) {
			PrecisionDimension d = (PrecisionDimension)t;
			d.preciseWidth *= widthScale;
			d.preciseHeight *= heightScale;
			d.updateInts();
		}
		else if ( t instanceof Dimension ) {
			Dimension d = (Dimension) t;
			d.scale(widthScale, heightScale);
		}
		else if ( t instanceof PrecisionPoint ) {
			PrecisionPoint p = (PrecisionPoint) t;
			p.preciseX *= widthScale;
			p.preciseY *= heightScale;
			p.updateInts();
		}
		else if ( t instanceof Point ) {
			Point p = (Point)t;
			p.scale(widthScale, heightScale);
		}
		else if ( t instanceof PointList ) {
			throw new RuntimeException("PointList not supported in AspectRatioScale");
		}
		else {
			throw new RuntimeException( t.toString() + " not supported in AspectRatioScale");
		}
		
		
		super.translateToParent(t);
		
	}
}
