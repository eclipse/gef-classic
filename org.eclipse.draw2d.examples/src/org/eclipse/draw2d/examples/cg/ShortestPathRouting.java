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

import org.eclipse.draw2d.geometry.*;

/**
 * @since 3.0
 */
public class ShortestPathRouting {

static class Segment {
	int x1;
	int x2;
	int y1;
	int y2;

	Segment() {}

	Segment(int[] ints, int offset) {
		this.x1 = ints[offset];
		this.y1 = ints[offset + 1];
		this.x2 = ints[offset + 2];
		this.y2 = ints[offset + 3];
	}
	Segment(Point p1, Point p2) {
		x1 = p1.x;
		y1 = p1.y;
		x2 = p2.x;
		y2 = p2.y;
	}
	
	private long cross (int x1, int y1, int x2, int y2) {
		return x1 * y2 - x2 * y1;
	}
	
	boolean intersects(int x3, int y3, int x4, int y4) {
		/*
		 * Given the segments:
		 * u-------v.
		 * s-------t.
		 * If s->t is inside the triangle uvs, then check whether the line uv splits the
		 * line st.
		 */
		int su_x = this.x1 - x3;
		int su_y = this.y1 - y3;
		int sv_x = this.x2 - x3;
		int sv_y = this.y2 - y3;
		int st_x = x3 - x4;
		int st_y = y3 - y4;

		long product = cross(sv_x, sv_y, st_x, st_y)
			* cross(st_x, st_y, su_x, su_y);
		if (product > 0) {
			int uvx = this.x2 - this.x1;
			int uvy = this.y2 - this.y1;
			int tux = this.x1 - x4;
			int tuy = this.y1 - y4;
			product = cross(-su_x, -su_y, uvx, uvy)
				* cross(uvx, uvy, tux, tuy);
			boolean intersects = product < 0;
			return intersects;
		}

		return false;
	}
	
	boolean intersects(Segment other) {
		return intersects(other.x1, other.y1, other.x2, other.y2);
	}
}
Rectangle[] boxes;
List segments;

PointList v;
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

public void setObstacles(Rectangle[] boxes) {
	this.boxes = boxes;
	v = new PointList();
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
	Point p1 = new Point(), p2 = new Point();
	Segment s = new Segment();
	for (int i = 0; i < v.size() - 1; i++) {
		for (int j = i + 1; j < v.size(); j++) {
			v.getPoint(p1, i);
			v.getPoint(p2, j);
			s.x1 = p1.x;
			s.y1 = p1.y;
			s.x2 = p2.x;
			s.y2 = p2.y;
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
