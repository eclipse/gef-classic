/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.gefx;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author Del Myers
 */
class ArcLocator extends ConnectionLocator {
	
	
	public ArcLocator(Connection connection, int location) {
		super(connection, location);
	}
	
	
	public void relocate(IFigure target) {
		if (!(target instanceof RotatableDecoration)) return;
		if (!(getConnection() instanceof ArcConnection)) return;
		Point alignmentPoint = getAlignementPoint();
		((RotatableDecoration)target).setLocation(alignmentPoint);
		rotate((RotatableDecoration)target);
	}
	
	protected int getQuadrant(Point p) {
		ArcConnection conn = (ArcConnection) getConnection();
		int centerx = conn.getArcBounds().x + conn.getArcBounds().width/2;
		int centery = conn.getArcBounds().y + conn.getArcBounds().height/2;
		if (p.y > centery) {
			if (p.x > centerx) {
				return 4;
			} else {
				return 3;
			}
		} else {
			if (p.x < centerx) {
				return 2;
			} else {
				return 1;
			}
		}
	}
	
	protected Point getAlignementPoint() {
		Point point = null;
		ArcConnection connection = (ArcConnection)getConnection();
		if (getAlignment() == SOURCE) {
			point = connection.getPoints().getFirstPoint().getCopy();
		} else if (getAlignment() == TARGET) {
			point = connection.getPoints().getLastPoint().getCopy();
		} else {
			point = connection.getPoints().getMidpoint().getCopy();
		}
		return point;
	}
	protected void rotate(RotatableDecoration target) {
		ArcConnection connection = (ArcConnection)getConnection();
		Point point = getAlignementPoint();
		Rectangle arcBounds = connection.getArcBounds();
		//normalize the coordinates.
		double bx = arcBounds.x;
		double by = arcBounds.y;
		double bw = arcBounds.width;
		double bh = arcBounds.height;
		
		int q = getQuadrant(point);
		Rectangle tbounds = target.getBounds();
		//the new location of the arrow will depend on what quadrant it is in
		switch (q) {
			case 1: 
				 point.x = point.x+tbounds.width;
				 point.y = point.y+tbounds.height;
				break;
			case 2:
				point.x = point.x+tbounds.width;
				point.y = point.y-tbounds.height;
				break;
			case 3:
				point.x = point.x-tbounds.width;
				point.y = point.y-tbounds.height;
				break;
			case 4:
				point.x = point.x-tbounds.width;
				point.y = point.y+tbounds.height;
				break;
		}
		
		
		double normx = (point.x - (bx + bw/2));
		double normy = (point.y - (by+ bh/2));
		double th = Math.atan(normy/normx);
		//adjust theta according to quadrant
		switch (q) {
			case 4:
				th = th + Math.PI;
			case 2: 
			case 3: 
				th = th + Math.PI;
				break;
		}

		//translate back from polar coordinates.
		double nx = (bw/2)*Math.cos(th) + bx + bw/2;
		double ny = (bh/2)*Math.sin(th) + by + bh/2;
		target.setReferencePoint(new Point(Math.round(nx), Math.round(ny)));
	}
	
	
}
