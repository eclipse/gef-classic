/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.draw2d.examples.cg;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @since 3.0
 */
public class ShortestPathRouting {

static class Segment {
	public Point u;
	public Point v;

	Segment() {}

	Segment(Point p1, Point p2) {
		this.u = p1;
		this.v = p2;
	}
	
	private long cross (int x1, int y1, int x2, int y2) {
		return x1 * y2 - x2 * y1;
	}
	
	boolean intersects(int sx, int sy, int tx, int ty) {
		/*
		 * Given the segments:
		 * u-------v.
		 * s-------t.
		 * If s->t is inside the triangle uvs, then check whether the line uv splits the
		 * line st.
		 */
		int su_x = u.x - sx;
		int su_y = u.y - sy;
		int sv_x = v.x - sx;
		int sv_y = v.y - sy;
		int st_x = sx - tx;
		int st_y = sy - ty;

		long product = cross(sv_x, sv_y, st_x, st_y)
			* cross(st_x, st_y, su_x, su_y);
		if (product > 0) {
			int uvx = v.x - u.x;
			int uvy = v.y - u.y;
			int tux = u.x - tx;
			int tuy = u.y - ty;
			product = cross(-su_x, -su_y, uvx, uvy)
				* cross(uvx, uvy, tux, tuy);
			boolean intersects = product < 0;
			return intersects;
		}

		return false;
	}
	
	boolean intersects(Segment other) {
		return intersects(other.u.x, other.u.y, other.v.x, other.v.y);
	}
}
Rectangle[] boxes;
List segments;

PointList vertices;
List visibility;


/**
 * @since 3.0
 * @param start
 * @param end
 */
private void addSegment(Point start, Point end) {
	Segment s = new Segment(start, end);
	for (int i = 0; i < boxes.length; i++) {
		Rectangle r = boxes[i];
		if (s.intersects(r.x, r.y, r.right(), r.bottom())
				|| s.intersects(r.x, r.bottom(), r.right(), r.y))
			return;
	}
	segments.add(s);
}

public void setGoals(Point source, Point target) {
	addSegment(source, target);
	for (int i = 0; i < boxes.length; i++) {
		Rectangle rect = boxes[i];
		addSegments(rect, source);
		addSegments(rect, target);
	}
}

private void addSegments(Rectangle rect, Point point) {
	switch (rect.getPosition(point)) {
		case PositionConstants.SOUTH_WEST:
		case PositionConstants.NORTH_EAST:
			addSegment(point, rect.getTopLeft());
			addSegment(point, rect.getBottomRight());
			break;
		case PositionConstants.SOUTH_EAST:
		case PositionConstants.NORTH_WEST:
			addSegment(point, rect.getTopRight());
			addSegment(point, rect.getBottomLeft());
			break;
		case PositionConstants.NORTH:
			addSegment(point, rect.getTopLeft());
			addSegment(point, rect.getTopRight());
			break;
		case PositionConstants.EAST:
			addSegment(point, rect.getBottomRight());
			addSegment(point, rect.getTopRight());
			break;
		case PositionConstants.SOUTH:
			addSegment(point, rect.getBottomRight());
			addSegment(point, rect.getBottomLeft());
			break;
		case PositionConstants.WEST:
			addSegment(point, rect.getTopLeft());
			addSegment(point, rect.getBottomLeft());
			break;
	}
}

public void setObstacles(Rectangle[] boxes) {
	this.boxes = boxes;
	vertices = new PointList();
	segments = new ArrayList();
	for (int i = 0; i < boxes.length; i++) {
		Rectangle source = boxes[i];
		for (int j = i + 1; j < boxes.length; j++) {
			Rectangle target = boxes[j];
			if (source.intersects(target))
				continue;
			if (target.bottom() < source.y) {
				targetAboveSource(source, target);
			} else if (source.bottom() < target.y) {
				targetAboveSource(target, source);
			} else if (target.right() < source.x) {
				targetBesideSource(source, target);
			} else
				targetBesideSource(target, source);
		}
	}
	
	visibility = new ArrayList();
	Segment s = new Segment();
	for (int i = 0; i < vertices.size() - 1; i++) {
		for (int j = i + 1; j < vertices.size(); j++) {
			vertices.getPoint(s.u, i);
			vertices.getPoint(s.v, j);
			boolean isClean = true;
			for (int k = 0; isClean && k < segments.size(); k++)
				isClean &= !s.intersects((Segment)segments.get(k));
			if (isClean) {
				visibility.add(s);
				s = new Segment();
			}
		}
	}
}


private void targetAboveSource(Rectangle source, Rectangle target) {
	//target located above source
	if (target.x > source.x) {
		addSegment(source.getTopLeft(), target.getTopLeft());
		if (target.x < source.right())
			addSegment(source.getTopRight(), target.getBottomLeft());
		else
			addSegment(source.getBottomRight(), target.getTopLeft());
	}
	else if (source.x == target.x) {
		addSegment(source.getTopLeft(), target.getBottomLeft());
		addSegment(source.getTopRight(), target.getBottomLeft());
	} else {
		addSegment(source.getBottomLeft(), target.getBottomLeft());
		addSegment(source.getTopRight(), target.getBottomLeft());
	}

	if (target.right() < source.right()) {
		addSegment(source.getTopRight(), target.getTopRight());
		if (target.right() > source.x)
			addSegment(source.getTopLeft(), target.getBottomRight());
		else
			addSegment(source.getBottomLeft(), target.getTopRight());
	} else if (source.right() == target.right()) {
		addSegment(source.getTopRight(), target.getBottomRight());
		addSegment(source.getTopLeft(), target.getBottomRight());		
	} else {
		addSegment(source.getBottomRight(), target.getBottomRight());
		addSegment(source.getTopLeft(), target.getBottomRight());
	}
}


private void targetBesideSource(Rectangle source, Rectangle target) {
	//target located above source
	if (target.y > source.y) {
		addSegment(source.getTopLeft(), target.getTopLeft());
		if (target.y < source.bottom())
			addSegment(source.getBottomLeft(), target.getTopRight());
		else
			addSegment(source.getBottomRight(), target.getTopLeft());
	}
	else if (source.y == target.y) {
		//degenerate case
		addSegment(source.getTopLeft(), target.getTopRight());
		addSegment(source.getBottomLeft(), target.getTopRight());
	} else {
		addSegment(source.getTopRight(), target.getTopRight());
		addSegment(source.getBottomLeft(), target.getTopRight());
	}

	if (target.bottom() < source.bottom()) {
		addSegment(source.getBottomLeft(), target.getBottomLeft());
		if (target.bottom() > source.y)
			addSegment(source.getTopLeft(), target.getBottomRight());
		else
			addSegment(source.getTopRight(), target.getBottomLeft());
	} else if (source.bottom() == target.bottom()) {
		addSegment(source.getBottomLeft(), target.getBottomRight());
		addSegment(source.getTopLeft(), target.getBottomRight());
	} else {
		addSegment(source.getBottomRight(), target.getBottomRight());
		addSegment(source.getTopLeft(), target.getBottomRight());
	}
}

}
