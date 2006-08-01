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
package org.eclipse.mylar.zest.core.internal.gefx;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Simple Bezier curve shape.
 * @author Del Myers
 *
 */

//@tag bug(152530-Bezier(fix)) : a basic shape for drawing bezier curves.
public class Bezier extends Polyline {
	//the control attached to the start point.
	private Point startControl;
	//the control attached to the end point.
	private Point endControl;
	private boolean isValid;
	
	
	private static Object lock = new Object();
	
	
	//	the number of line segments between the start and end points.
	public static int SEGMENTS;
	//@tag performance(optimization) : pre-compute the weights for faster updates of bezier curves. Greatly reduces the number of multiplications.
	private static double[] WEIGHTS1;
	private static double[] WEIGHTS2;
	
	//allow users to change the number of segments
	static {
		synchronized (lock) {
			SEGMENTS = 24;
			WEIGHTS1 = COMPUTE_WEIGHTS_1();
			WEIGHTS2 = COMPUTE_WEIGHTS_2();
		}
	}
	
	public Bezier(Point start, Point end, Point startControl, Point endControl) {
		super();
		setStart(start);
		setEnd(end);
		this.startControl = startControl;
		this.endControl = endControl;
		this.isValid = false;

	}
	
	
	//set the number of segments to be used in drawing.
	public static void setSegments(int numSegments) {
		synchronized (lock) {
			SEGMENTS = numSegments;
			WEIGHTS1 = COMPUTE_WEIGHTS_1();
			WEIGHTS2 = COMPUTE_WEIGHTS_2();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 */
	protected void fillShape(Graphics graphics) {
		reCompute();
		super.fillShape(graphics);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 */
	protected void outlineShape(Graphics graphics) {
		reCompute();
		graphics.drawRectangle(getStartControl().x-2, getStartControl().y-2, 4, 4);
		graphics.drawRectangle(getEndControl().x-2, getEndControl().y-2, 4, 4);
		graphics.setClip(new Rectangle(0,0,3000,3000));
		graphics.drawPolyline(getPoints().toIntArray());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Polyline#setStart(org.eclipse.draw2d.geometry.Point)
	 */
	public void setStart(Point start) {
		super.setStart(start);
		isValid = false;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Polyline#setEnd(org.eclipse.draw2d.geometry.Point)
	 */
	public void setEnd(Point end) {
		super.setEnd(end);
		isValid = false;
	}
	
	/**
	 * @param endControl the endControl to set
	 */
	public void setEndControl(Point endControl) {
		this.endControl = endControl;
		isValid = false;
	}
	
	/**
	 * @param startControl the startControl to set
	 */
	public void setStartControl(Point startControl) {
		this.startControl = startControl;
		isValid = false;
	}
	
	/**
	 * @return the startControl
	 */
	public Point getStartControl() {
		return startControl;
	}
	
	/**
	 * @return the endControl
	 */
	public Point getEndControl() {
		return endControl;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#revalidate()
	 */
	public void revalidate() {
		reCompute();
		super.revalidate();
	}
	
	public final void reCompute() {
		if (isValid) return;
		Point start = getStart();
		Point end = getEnd();
		PointList points = getPoints();
		points.removeAllPoints();
		points.addPoint(start);
		synchronized(lock) {
			for (int i = 1; i < SEGMENTS; i++) {
				Point p = new Point(
						Math.round(t0(i)*start.x + t1(i)*startControl.x + t2(i)*endControl.x+t3(i)*end.x),
						Math.round(t0(i)*start.y + t1(i)*startControl.y + t2(i)*endControl.y+t3(i)*end.y)
				);
				points.addPoint(p);
			}
		}
		points.addPoint(end);
		super.setPoints(points);
		isValid = true;
	}
	
	private static final double[] COMPUTE_WEIGHTS_1() {
		double[] weights = new double[SEGMENTS+1];
		double t = 0.0;
		weights[0] = 1;
		weights[SEGMENTS] = 0;
		for (int i = 1; i < SEGMENTS; i++) {
			t = ((double)i)/((double)SEGMENTS);
			double oneMinusT = (1-t);
			weights[i] = oneMinusT*oneMinusT*oneMinusT;
		}
		return weights;
	}
	
	private static final double[] COMPUTE_WEIGHTS_2() {
		double[] weights = new double[SEGMENTS+1];
		double t = 0.0;
		weights[0] = 1;
		weights[SEGMENTS] = 0;
		for (int i = 1; i < SEGMENTS; i++) {
			t = ((double)i)/((double)SEGMENTS);
			double oneMinusT = (1-t);
			weights[i] = t*oneMinusT*oneMinusT*3;
		}
		return weights;
	}
	
	private static final double t0(int s) {
		return WEIGHTS1[s];
	}
	
	private static final double t1(int s) {
		return WEIGHTS2[s];
	}
	
	private static final double t2(int s) {
		return WEIGHTS2[SEGMENTS-s];
	}
	
	private static final double t3(int s) {
		return WEIGHTS1[SEGMENTS-s];
	}

}