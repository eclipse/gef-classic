/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.graph;

import java.util.*;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;

/**
 * A Path representation for the ShortestPathRouting. A Path has a start and end point 
 * and may have bendpoints. The output of a path is accessed via the method <code>getPoints()</code>.
 * 
 * This class is for internal use only.
 * @author Whitney Sorenson
 * @since 3.0
 */
public class Path {

private static final double OVAL_CONSTANT = 1.13;
private static final double EPSILON = .08;

PointList points, bendpoints;
List grownSegments, segments, excludedObstacles;
/**
 * this field is for internal use only.  It is true whenever a property has been changed
 * which requires the solver to reroute this path.
 */
public boolean isDirty = true;
boolean isInverted = false;
boolean isMarked = false;
Vertex start, end;
double threshold;

private double distance;
private double prevCost;
private SegmentStack stack;
private Path subPath;
private Set visibleObstacles;
private Set visibleVertices;
public Object data;


/**
 * Constructs a new path.
 * @since 3.0
 */
public Path() {
	segments = new ArrayList();
	grownSegments = new ArrayList();
	points = new PointList();
	visibleVertices = new HashSet();
	stack = new SegmentStack();
	visibleObstacles = new HashSet();
	excludedObstacles = new ArrayList();
}

/**
 * Constructs a new path with the given data.
 * @since 3.0
 * @param data an arbitrary data field
 */
public Path(Object data) {
	this();
	this.data = data;
}

/**
 * Constructs a new path with the given data, start and end point.
 * 
 * @param start the start point for this path
 * @param end the end point for this path
 */
public Path(Point start, Point end) {
	this(new Vertex(start, null), new Vertex(end, null));
}

/**
 * Creates a path between the given vertices.
 * 
 * @param start start vertex
 * @param end end vertex
 */
Path(Vertex start, Vertex end) {
	this();
	this.start = start;
	this.end = end;
}

/**
 * Attempts to add all segments between the given obstacles to the visibility graph.
 * @param source the source obstacle
 * @param target the target obstacle
 */
private void addAllSegmentsBetween(Obstacle source, Obstacle target) {
	addConnectingSegment(new Segment(source.bottomLeft, target.bottomLeft), source, target, false);
	addConnectingSegment(new Segment(source.bottomLeft, target.bottomRight), source, target, false);
	addConnectingSegment(new Segment(source.bottomRight, target.bottomLeft), source, target, true);
	addConnectingSegment(new Segment(source.bottomRight, target.bottomRight), source, target, true);
	addConnectingSegment(new Segment(source.topLeft, target.topRight),  source, target, true);
	addConnectingSegment(new Segment(source.topLeft, target.topLeft), source, target, true);
	addConnectingSegment(new Segment(source.topRight, target.topRight), source, target, false);
	addConnectingSegment(new Segment(source.topRight, target.topLeft), source, target, false);
}

/**
 * Attempts to add a segment between the given obstacles to the visibility graph. This 
 * method is specifically written for the case where the two obstacles intersect and contains
 * a boolean as to whether to check the diagnol that includes the top right point of the other obstacle.
 * 
 * @param segment the segment to check
 * @param o1 the first obstacle
 * @param o2 the second obstacle
 * @param checkTopRight whether or not to check the diagnol containing top right point
 */
private void addConnectingSegment(Segment segment, Obstacle o1, Obstacle o2, boolean checkTopRight) {
	if (threshold != 0
			&& (segment.end.getDistance(end) + segment.end.getDistance(start) > threshold
				|| segment.start.getDistance(end) + segment.start.getDistance(start) > threshold))
		return;
	if (o2.contains(segment.start) || o1.contains(segment.end)) 
		return;
	
	if (checkTopRight && (segment.intersects(o1.x, o1.bottom() - 1, o1.right() - 1, o1.y) 
			|| segment.intersects(o2.x, o2.bottom() - 1, o2.right() - 1, o2.y)))
		return;
	if (!checkTopRight && (segment.intersects(o1.x, o1.y, o1.right() - 1, o1.bottom() - 1)
			|| segment.intersects(o2.x, o2.y, o2.right() - 1, o2.bottom() - 1)))
		return;
	
	stack.push(o1);
	stack.push(o2);
	stack.push(segment);
}

/**
 * Adds an obstacle to the visibility graph and generates new segments
 * @param newObs the new obstacle, should not be in the graph already
 */
private void addObstacle(Obstacle newObs) {
	visibleObstacles.add(newObs);
	Iterator oItr = new HashSet(visibleObstacles).iterator();
	while (oItr.hasNext()) {
		Obstacle currObs = (Obstacle)oItr.next();
		if (newObs != currObs)
			addSegmentsFor(newObs, currObs);
	}
	addPerimiterSegments(newObs);
	addSegmentsFor(start, newObs);
	addSegmentsFor(end, newObs);
}

/**
 * Adds the segments along the perimiter of an obstacle to the visiblity graph queue.
 * @param obs the obstacle
 */
private void addPerimiterSegments(Obstacle obs) {
	Segment seg = new Segment(obs.topLeft, obs.topRight);
	stack.push(obs);
	stack.push(null);
	stack.push(seg);
	seg = new Segment(obs.topRight, obs.bottomRight);
	stack.push(obs);
	stack.push(null);
	stack.push(seg);
	seg = new Segment(obs.bottomRight, obs.bottomLeft);
	stack.push(obs);
	stack.push(null);
	stack.push(seg);
	seg = new Segment(obs.bottomLeft, obs.topLeft);
	stack.push(obs);
	stack.push(null);
	stack.push(seg);
}

/**
 * Attempts to add a segment to the visibility graph. 
 * First checks to see if the segment is outside the threshold oval. Then it compares the segment
 * against all obstacles. If it is clean, the segment is finally added to the graph.
 * 
 * @param segment the segment
 * @param exclude1 an obstacle to exclude from the search
 * @param exclude2 another obstacle to exclude from the search
 * @param allObstacles the list of all obstacles
 */
private void addSegment(Segment segment, Obstacle exclude1, Obstacle exclude2, List allObstacles) {
	if (threshold != 0
			&& (segment.end.getDistance(end) + segment.end.getDistance(start) > threshold
				|| segment.start.getDistance(end) + segment.start.getDistance(start) > threshold))
		return;
	
	for (int i = 0; i < allObstacles.size(); i++) {
		Obstacle obs = (Obstacle)allObstacles.get(i);
		
		if (obs == exclude1 || obs == exclude2 || obs.exclude)
			continue;		

		if (segment.intersects(obs.x, obs.y, obs.right() - 1, obs.bottom() - 1)
				|| segment.intersects(obs.x, obs.bottom() - 1, obs.right() - 1, obs.y)
				|| obs.contains(segment.start) 
				|| obs.contains(segment.end)) {
			if (!visibleObstacles.contains(obs))
				addObstacle(obs);
			return;
		}
	}
	
	linkVertices(segment);
}

/**
 * Adds the segments between the given obstacles.
 * @param source source obstacle
 * @param target target obstacle
 */
private void addSegmentsFor(Obstacle source, Obstacle target) {
	if (source.intersects(target))
		addAllSegmentsBetween(source, target);
	else if (target.bottom() - 1 < source.y)
		targetAboveSource(source, target);
	else if (source.bottom() - 1 < target.y)
		targetAboveSource(target, source);
	else if (target.right() - 1 < source.x)
		targetBesideSource(source, target);
	else
		targetBesideSource(target, source);
}

/**
 * Adds the segments between the given obstacles.
 * @param source source obstacle
 * @param target target obstacle
 */
private void addSegmentsFor(Vertex vertex, Obstacle obs) {
	Segment seg = null;
	Segment seg2 = null;
	
	switch (obs.getPosition(vertex)) {			
		case PositionConstants.SOUTH_WEST :
		case PositionConstants.NORTH_EAST :
			seg = new Segment(vertex, obs.topLeft);
			seg2 = new Segment(vertex, obs.bottomRight);
			break;
		case PositionConstants.SOUTH_EAST :
		case PositionConstants.NORTH_WEST :
			seg = new Segment(vertex, obs.topRight);
			seg2 = new Segment(vertex, obs.bottomLeft);
			break;
		case PositionConstants.NORTH :
			seg = new Segment(vertex, obs.topLeft);
			seg2 = new Segment(vertex, obs.topRight);
			break;
		case PositionConstants.EAST :
			seg = new Segment(vertex, obs.bottomRight);
			seg2 = new Segment(vertex, obs.topRight);
			break;
		case PositionConstants.SOUTH :
			seg = new Segment(vertex, obs.bottomRight);
			seg2 = new Segment(vertex, obs.bottomLeft);
			break;
		case PositionConstants.WEST :
			seg = new Segment(vertex, obs.topLeft);
			seg2 = new Segment(vertex, obs.bottomLeft);
			break;
	}
	
	stack.push(obs);
	stack.push(null);
	stack.push(seg);
	stack.push(obs);
	stack.push(null);
	stack.push(seg2);
}

/**
 * Begins the creation of the visibility graph with the first segment
 * @param allObstacles list of all obstacles
 */
private void createVisibilityGraph(List allObstacles) {
	stack.push(null);
	stack.push(null);
	stack.push(new Segment(start, end));
	
	while (!stack.isEmpty())
		addSegment(stack.pop(), stack.popObstacle(), stack.popObstacle(), allObstacles);
}

/**
 * Once the visibility graph is constructed, this is called to label the graph and 
 * determine the shortest path. Returns false if no path can be found.
 * 
 * @return true if a path can be found.
 */
private boolean determineShortestPath() {
	if (!labelGraph())
		return false;
	Vertex vertex = end;
	prevCost = end.cost;
	
	Vertex nextVertex;
	while (!vertex.equals(start)) {
		nextVertex = vertex.label;
		if (nextVertex == null)
			return false;
		Segment s = new Segment(nextVertex, vertex);
		segments.add(s);
		vertex = nextVertex;
	}
	
	Collections.reverse(segments);
	return true;
}

/**
 * Resets all necessary fields for a solve.
 */
void fullReset() {
	visibleVertices.clear();
	segments.clear();
	if (prevCost == 0) {
		distance = start.getDistance(end);
		threshold = distance * OVAL_CONSTANT;
	} else 
		threshold = distance + prevCost * EPSILON;
	visibleObstacles.clear();
	reset();
}

/**
 * Creates the visibility graph and returns whether or not a shortest path could be
 * determined.
 * 
 * @param allObstacles the list of all obstacles
 * @return true if a shortest path was found
 */
boolean generateShortestPath(List allObstacles) {
	createVisibilityGraph(allObstacles);
	
	if (visibleVertices.size() == 0)
		return false;
	
	return determineShortestPath();
}

/**
 * Returns the list of bend points assigned to this path.
 * @return list of bend points.
 */
public PointList getBendPoints() {
	return bendpoints;
}

/**
 * Returns the end point for this path
 * @return end point for this path
 */
public Point getEndPoint() {
	return end;
}

/**
 * Returns the start point for this path
 * @return start point for this path
 */
public Point getStartPoint() {
	return start;
}

/**
 * Returns a pointlist of the bendpoints in this path.
 * 
 * @return the points for this path.
 */
public PointList getPoints() {
	return points;
}

/**
 * Returns a subpath for this path at the given segment
 * 
 * @param currentSegment the segment at which the subpath should be created
 * @return the new path
 */
Path getSubPath(Segment currentSegment) {
	// ready new path
	Path newPath = new Path(currentSegment.start, end);
	newPath.grownSegments = new ArrayList(grownSegments.subList(grownSegments.indexOf(currentSegment), grownSegments.size()));
	
	// fix old path
	grownSegments = new ArrayList(grownSegments.subList(0, grownSegments.indexOf(currentSegment) + 1));		
	end = currentSegment.end;
	
	subPath = newPath;
	return newPath;
}

/**
 * Returns true if this obstacle is in the visibility graph
 * @param obs the obstacle 
 * @return true if obstacle is in the visibility graph
 */
boolean isObstacleVisible(Obstacle obs) {
	return visibleObstacles.contains(obs);
}

/**
 * Labels the visibility graph to assist in finding the shortest path
 * @return false if there was a gap in the visibility graph
 */
private boolean labelGraph() {
	int numPermanentNodes = 1;
	Vertex vertex = start;
	Vertex neighborVertex = null;
	vertex.isPermanent = true;
	double newCost;
	while (numPermanentNodes != visibleVertices.size()) {
		List neighbors = vertex.neighbors;
		if (neighbors == null) 
			return false;
		// label neighbors if they have a new shortest path
		for (int i = 0; i < neighbors.size(); i++) {
			neighborVertex = (Vertex)neighbors.get(i);
			if (!neighborVertex.isPermanent) {
				newCost = vertex.cost + vertex.getDistance(neighborVertex);
				if (neighborVertex.label == null) {
					neighborVertex.label = vertex;
					neighborVertex.cost = newCost;
				} else if (neighborVertex.cost > newCost) {
					neighborVertex.label = vertex;
					neighborVertex.cost = newCost;
				}
			}
		}
		// find the next none-permanent, labeled vertex with smallest cost
		double smallestCost = 0;
		Vertex tempVertex = null;
		Iterator v = visibleVertices.iterator();
		while (v.hasNext()) {
			tempVertex = (Vertex)v.next();
			if (!tempVertex.isPermanent && tempVertex.label != null
					&& (tempVertex.cost < smallestCost || smallestCost == 0)) {
				smallestCost = tempVertex.cost;
				vertex = tempVertex;
			}
		}
		// set the new vertex to permanent.
		vertex.isPermanent = true;
		numPermanentNodes++;
	}
	return true;
}

/**
 * Links two vertices together in the visibility graph
 * @param segment the segment to add
 */
private void linkVertices(Segment segment) {
	if (segment.start.neighbors == null)
		segment.start.neighbors = new ArrayList();
	if (segment.end.neighbors == null)
		segment.end.neighbors = new ArrayList();
	
	if (!segment.start.neighbors.contains(segment.end)) {
		segment.start.neighbors.add(segment.end);
		segment.end.neighbors.add(segment.start);
	}
	
	visibleVertices.add(segment.start);
	visibleVertices.add(segment.end);
}

/**
 * Called to reconnect a subpath back onto this path. Does a depth-first search to
 * reconnect all paths. Should be called after sorting.
 */
void reconnectSubPaths() {
	if (subPath != null) {
		subPath.reconnectSubPaths();
		
		Segment changedSegment = (Segment)subPath.grownSegments.remove(0);
		Segment oldSegment = (Segment)grownSegments.get(grownSegments.size() - 1);
		
		oldSegment.end = changedSegment.end;
		grownSegments.addAll(subPath.grownSegments);
		
		subPath.points.removePoint(0);
		points.removePoint(points.size() - 1);
		points.addAll(subPath.points);
		
		end = subPath.end;
		subPath = null;
	}
}

/**
 * Refreshes the exclude field on the obstacles in the list. Excludes all obstacles
 * that contain the start or end point for this path.
 * @param allObstacles list of all obstacles
 */
void refreshExcludedObstacles(List allObstacles) {
	excludedObstacles.clear();
	
	for (int i = 0; i < allObstacles.size(); i++) {
		Obstacle o = (Obstacle)allObstacles.get(i);
		o.exclude = o.contains(start) || o.contains(end);

		if (o.exclude && !excludedObstacles.contains(o))
			excludedObstacles.add(o);
	}
}

/**
 * Resets the fields for everything in the solve after the visibility graph steps.
 */
void reset() {
	isMarked = false;
	isInverted = false;
	subPath = null;
	isDirty = false;
	grownSegments.clear();
	points.removeAllPoints();
}


/**
 * Resets the vertices that this path has traveled prior to this segment. This is called
 * when the path has become inverted and needs to rectify any labeling mistakes it made
 * before it knew it was inverted.
 * @param currentSegment the segment at which the path found it was inverted
 */
void resetVertices(Segment currentSegment) {
	for (int i = 0; i < grownSegments.indexOf(currentSegment); i++) {
		Vertex vertex = ((Segment)grownSegments.get(i)).end;
		if (vertex.type == Vertex.INNIE)
			vertex.type = Vertex.OUTIE;
		else
			vertex.type = Vertex.INNIE;
	}
}

/**
 * Sets the list of bend points to the given list and dirties the path.
 * @param bendPoints the list of bend points
 */
public void setBendPoints(PointList bendPoints) {
	this.bendpoints = bendPoints;
	isDirty = true;
}

/**
 * Sets the end point for this path to the given point.
 * @param end the new end point for this path
 */
public void setEndPoint(Point end) {
	if (end.equals(this.end))
		return;
	this.end = new Vertex(end, null);
	isDirty = true;
}

/**
 * Sets the start point for this path to the given point.
 * @param start the new start point for this path
 */
public void setStartPoint(Point start) {
	if (start.equals(this.start))
		return;
	this.start = new Vertex(start, null);
	isDirty = true;
}

private void targetAboveSource(Obstacle source, Obstacle target) {
	//target located above source
	Segment seg = null;
	Segment seg2 = null;
	if (target.x > source.x) {
		seg = new Segment(source.topLeft, target.topLeft);
		if (target.x < source.right() - 1)
			seg2 = new Segment(source.topRight, target.bottomLeft);
		else
			seg2 = new Segment(source.bottomRight, target.topLeft);		
	} else if (source.x == target.x) {
		seg = new Segment(source.topLeft, target.bottomLeft);
		seg2 = new Segment(source.topRight, target.bottomLeft);
	} else {
		seg = new Segment(source.bottomLeft, target.bottomLeft);
		seg2 = new Segment(source.topRight, target.bottomLeft);
	}
	
	stack.push(source);
	stack.push(target);
	stack.push(seg);
	stack.push(source);
	stack.push(target);
	stack.push(seg2);
	seg = null;
	seg2 = null;
	
	if (target.right() < source.right()) {
		seg = new Segment(source.topRight, target.topRight);
		if (target.right() - 1 > source.x)
			seg2 = new Segment(source.topLeft, target.bottomRight);
		else
			seg2 = new Segment(source.bottomLeft, target.topRight);
	} else if (source.right() == target.right()) {
		seg = new Segment(source.topRight, target.bottomRight);
		seg2 = new Segment(source.topLeft, target.bottomRight);
	} else {
		seg = new Segment(source.bottomRight, target.bottomRight);
		seg2 = new Segment(source.topLeft, target.bottomRight);
	}	

	stack.push(source);
	stack.push(target);
	stack.push(seg);
	stack.push(source);
	stack.push(target);
	stack.push(seg2);
}

private void targetBesideSource(Obstacle source, Obstacle target) {
	//target located above source
	Segment seg = null;
	Segment seg2 = null;
	if (target.y > source.y) {
		seg = new Segment(source.topLeft, target.topLeft);
		if (target.y < source.bottom() - 1)
			seg2 = new Segment(source.bottomLeft, target.topRight);
		else
			seg2 = new Segment(source.bottomRight, target.topLeft);
	} else if (source.y == target.y) {
		//degenerate case
		seg = new Segment(source.topLeft, target.topRight);
		seg2 = new Segment(source.bottomLeft, target.topRight);
	} else {
		seg = new Segment(source.topRight, target.topRight);
		seg2 = new Segment(source.bottomLeft, target.topRight);	
	}
	stack.push(source);
	stack.push(target);
	stack.push(seg);
	stack.push(source);
	stack.push(target);
	stack.push(seg2);
	seg = null;
	seg2 = null;
	
	if (target.bottom() < source.bottom()) {
		seg = new Segment(source.bottomLeft, target.bottomLeft);
		if (target.bottom() - 1 > source.y)
			seg2 = new Segment(source.topLeft, target.bottomRight);
		else
			seg2 = new Segment(source.topRight, target.bottomLeft);
	} else if (source.bottom() == target.bottom()) {
		seg = new Segment(source.bottomLeft, target.bottomRight);
		seg2 = new Segment(source.topLeft, target.bottomRight);
	} else {
		seg = new Segment(source.bottomRight, target.bottomRight);
		seg2 = new Segment(source.topLeft, target.bottomRight);
	}
	stack.push(source);
	stack.push(target);
	stack.push(seg);
	stack.push(source);
	stack.push(target);
	stack.push(seg2);
}

/**
 * A Stack of segments.
 */
static private class SegmentStack extends ArrayList {
	
Segment pop() {
	return (Segment)remove(size() - 1);
}	

Obstacle popObstacle() {
	return (Obstacle)remove(size() - 1);
}

void push(Object obj) {
	add(obj);
}

}

}