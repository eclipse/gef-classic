/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Stephane Lizeray slizeray@ilog.fr - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.examples.path;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class PathFigure extends Polyline {

	{
		setFill(true);
	}

	private boolean isClosed;

	private float degrees;

	private float cx, cy;

	protected void outlineShape(Graphics g) {
		drawShape(g, false);

	}

	protected void fillShape(Graphics g) {
		drawShape(g, true);

	}

	private void drawShape(Graphics g, boolean fill) {
		Path path = createPath();
		g.pushState();
		g.setAntialias(SWT.ON);
		if (fill)
			g.fillPath(path);
		else {
			g.drawPath(path);

		}
		g.popState();
		path.dispose();
		

	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setRotation(float cx, float cy, float degrees) {
		this.degrees = degrees;
		this.cx = cx;
		this.cy = cy;
		bounds = null;
		repaint();
	}

	public float getRotationAngle() {
		return degrees;

	}

	public Point getRotationCenter() {
		return new PrecisionPoint(cx, cy);
	}

	private Path createPath() {
		PointList points = getPoints();

		Path path = new Path(Display.getCurrent());

		double angle = degreesToRadians(degrees);
		double cos = (double) Math.cos(angle), sin = (double) Math.sin(angle);

		PrecisionPoint p1 = getRotatedPoint(points.getFirstPoint().x, points
				.getFirstPoint().y, cos, sin);
		path.moveTo((float) p1.preciseX, (float) p1.preciseY);
		for (int i = 1; i < points.size(); i++) {
			PrecisionPoint p = getRotatedPoint(points.getPoint(i).x, points
					.getPoint(i).y, cos, sin);
			path.lineTo((float) p.preciseX, (float) p.preciseY);
		}
		if (isClosed())
			path.close();

		return path;

	}

	public PrecisionPoint getRotatedPoint(float x, float y, double cos,
			double sin) {

		PrecisionPoint p = new PrecisionPoint(x, y);
		if (degrees == 0)
			return p;

		p.preciseX = p.preciseX - cx;
		p.preciseY = p.preciseY - cy;

		float x1 = (float) (p.preciseX * cos - p.preciseY * sin);
		float y1 = (float) (p.preciseY * cos + p.preciseX * sin);

		p.preciseX = x1 + cx;
		p.preciseY = y1 + cy;
		p.updateInts();
		return p;
	}

	public Rectangle getBounds() {
		if (bounds == null) {
			bounds = new Rectangle();
			if (getPoints().size() > 0) {
				PointList points = getPoints();
				double angle = degreesToRadians(degrees);
				double cos = (double) Math.cos(angle), sin = (double) Math
						.sin(angle);
				PrecisionPoint p1 = getRotatedPoint(points.getFirstPoint().x,
						points.getFirstPoint().y, cos, sin);
				bounds.setLocation(p1);
				for (int i = 1; i < points.size(); i++) {
					PrecisionPoint p = getRotatedPoint(points.getPoint(i).x,
							points.getPoint(i).y, cos, sin);
					bounds.union(p);
				}
			}
		}
		return bounds.getExpanded(lineWidth / 2, lineWidth / 2);
	}

	static private double degreesToRadians(double angle) {
		return angle * Math.PI / 180;
	}

}
