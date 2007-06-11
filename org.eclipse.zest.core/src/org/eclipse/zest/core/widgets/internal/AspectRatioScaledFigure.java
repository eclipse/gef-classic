/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets.internal;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionDimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

//import org.eclipse.draw2d.text.CaretInfo;

/**
 * 
 * @author irbull
 * 
 */
public class AspectRatioScaledFigure extends Figure {// extends ScaledFigure {

	private double widthScale = 1.0;
	private double heigthScale = 1.0;

	public AspectRatioScaledFigure(String debugLabel) {
		widthScale = 1D;
		heigthScale = 1D;
		setLayoutManager(new FreeformLayout());
		setOpaque(true);
	}

	public void setSize(int w, int h) {
		super.setSize(w, h);

	}

	public void setScale(double wScale, double hScale) {
		this.widthScale = wScale;
		this.heigthScale = hScale;
	}

	public double getWidthScale() {
		return this.widthScale;
	}

	public double getHeightScale() {
		return this.heigthScale;
	}

	public boolean isCoordinateSystem() {
		return true;
	}

	public double getScale() {
		throw new RuntimeException("Operation not supported");
	}

	public void setScale(double scale) {
		//super.setScale( scale );
		this.widthScale = scale;
		this.heigthScale = scale;
		revalidate();
		repaint();
		//System.out.println("Operation not supported");
		//throw new RuntimeException("Operation not supported");
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getClientArea()
	 */

	public Rectangle getClientArea(Rectangle rect) {
		super.getClientArea(rect);

		rect.width /= widthScale;
		rect.height /= heigthScale;
		return rect;
	}

	public Dimension getPreferredSize(int wHint, int hHint) {
		Dimension d = super.getPreferredSize(wHint, hHint);
		int w = getInsets().getWidth();
		int h = getInsets().getHeight();
		return d.getExpanded(-w, -h).scale(widthScale, heigthScale).expand(w, h);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#translateFromParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateFromParent(Translatable t) {
		super.translateFromParent(t);
		//t.performScale(1/widthScale);

		if (t instanceof PrecisionRectangle) {
			PrecisionRectangle r = (PrecisionRectangle) t;
			r.preciseX *= 1 / widthScale;
			r.preciseY *= 1 / heigthScale;
			r.preciseWidth *= 1 / widthScale;
			r.preciseHeight *= 1 / heigthScale;
			r.updateInts();
		} else if (t instanceof Rectangle) {
			Rectangle r = (Rectangle) t;
			r.scale(1 / widthScale, 1 / heigthScale);
		}
		//		else if ( t instanceof CaretInfo ) {
		//			CaretInfo c = (CaretInfo)t;
		//			c.performScale(1/heigthScale);
		//		}
		else if (t instanceof PrecisionDimension) {
			PrecisionDimension d = (PrecisionDimension) t;
			d.preciseWidth *= 1 / widthScale;
			d.preciseHeight *= 1 / heigthScale;
			d.updateInts();
		} else if (t instanceof Dimension) {
			Dimension d = (Dimension) t;
			d.scale(1 / widthScale, 1 / heigthScale);
		} else if (t instanceof PrecisionPoint) {
			PrecisionPoint p = (PrecisionPoint) t;
			p.preciseX *= 1 / widthScale;
			p.preciseY *= 1 / heigthScale;
			p.updateInts();
		} else if (t instanceof Point) {
			Point p = (Point) t;
			p.scale(1 / widthScale, 1 / heigthScale);
		} else if (t instanceof PointList) {
			throw new RuntimeException("PointList not supported in AspectRatioScale");
		} else {
			throw new RuntimeException(t.toString() + " not supported in AspectRatioScale");
		}

		//t.performScale(1/widthScale);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#findFigureAt(int, int,
	 *      org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		IFigure f = super.findFigureAt(x, y, search);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#translateToParent(org.eclipse.draw2d.geometry.Translatable)
	 */
	public void translateToParent(Translatable t) {

		//t.performScale(widthScale);

		if (t instanceof PrecisionRectangle) {
			PrecisionRectangle r = (PrecisionRectangle) t;
			r.preciseX *= widthScale;
			r.preciseY *= heigthScale;
			r.preciseWidth *= widthScale;
			r.preciseHeight *= heigthScale;
			r.updateInts();
		} else if (t instanceof Rectangle) {
			Rectangle r = (Rectangle) t;
			//r.performScale(widthScale);
			r.scale(widthScale, heigthScale);
		}
		//		else if ( t instanceof CaretInfo ) {
		//			CaretInfo c = (CaretInfo)t;
		//			c.performScale(heigthScale);
		//		}
		else if (t instanceof PrecisionDimension) {
			PrecisionDimension d = (PrecisionDimension) t;
			d.preciseWidth *= widthScale;
			d.preciseHeight *= heigthScale;
			d.updateInts();
		} else if (t instanceof Dimension) {
			Dimension d = (Dimension) t;
			d.scale(widthScale, heigthScale);
		} else if (t instanceof PrecisionPoint) {
			PrecisionPoint p = (PrecisionPoint) t;
			p.preciseX *= widthScale;
			p.preciseY *= heigthScale;
			p.updateInts();
		} else if (t instanceof Point) {
			Point p = (Point) t;
			p.scale(widthScale, heigthScale);
		} else if (t instanceof PointList) {
			throw new RuntimeException("PointList not supported in AspectRatioScale");
		} else {
			throw new RuntimeException(t.toString() + " not supported in AspectRatioScale");
		}

		super.translateToParent(t);

	}

	protected boolean useLocalCoordinates() {
		return true;
	}

	protected void paintClientArea(Graphics graphics) {
		if (getChildren().isEmpty()) {
			return;
		}

		XYScaledGraphics g = null;
		boolean disposeGraphics = false;
		if (graphics instanceof XYScaledGraphics) {
			g = (XYScaledGraphics) graphics;
		} else {
			g = new XYScaledGraphics(graphics);
			disposeGraphics = true;
		}

		boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
		if (!optimizeClip) {
			g.clipRect(getBounds().getCropped(getInsets()));
		}

		g.translate((getBounds().x + getInsets().left), (getBounds().y + getInsets().top));

		g.scale(widthScale, heigthScale);
		//g.scale(widthScale);

		//g.scale(widthScale);
		g.pushState();
		paintChildren(g);
		g.popState();
		if (disposeGraphics) {
			g.dispose();
			graphics.restoreState();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintChildren(org.eclipse.draw2d.Graphics)
	 */
	protected void paintChildren(Graphics graphics) {
		IFigure child;
		List children = getChildren();
		Rectangle clip = Rectangle.SINGLETON;
		for (int i = 0; i < children.size(); i++) {
			child = (IFigure) children.get(i);
			if (child.isVisible() && child.intersects(graphics.getClip(clip))) {
				graphics.clipRect(child.getBounds());
				child.paint(graphics);
				graphics.restoreState();
			}
		}
	}

}
