package org.eclipse.gef.examples.flow.parts;

import java.util.*;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

/**
 * @author hudsonr
 * Created on Apr 28, 2003
 */
public class GraphAnimation {

static final long DURATION = 230;

static long current;
static double progress;
static long start = -1;
static long finish;
static Viewport viewport;
//static IFigure trackMe;
//static IFigure showMe;
//static Point trackLocation;

static boolean PLAYBACK;
static boolean RECORDING;

static Map initialStates;
static Map finalStates;

static void end() {
	Iterator iter = initialStates.keySet().iterator();
	while (iter.hasNext()) {
		IFigure f = ((IFigure)iter.next());
		f.revalidate();
		f.setVisible(true);
	}
	//$TODO instead of performing a final normal layout, what about setting progress=1.0?
	initialStates = null;
	finalStates = null;
	PLAYBACK = false;
//	trackMe = null;
//	showMe = null;
	viewport = null;
}

static boolean captureLayout(IFigure root) {

	RECORDING = true;

	while (!(root instanceof Viewport))
		root = root.getParent();
	viewport = (Viewport)root;
	while (root.getParent()!= null)
		root = root.getParent();

	initialStates = new HashMap();
	finalStates = new HashMap();

	//This part records all layout results.
	root.validate();
	Iterator iter = initialStates.keySet().iterator();
	if (!iter.hasNext()) {
		//Nothing layed out, so abort the animation
		RECORDING = false;
		return false;
	}
	while (iter.hasNext())
		recordFinalState((IFigure)iter.next());

	start = System.currentTimeMillis();
	finish = start + DURATION;
	current = start + 20;

	RECORDING = false;
	PLAYBACK = true;
	return true;
}

static boolean playbackState(Connection conn) {
	if (!PLAYBACK)
		return false;

	PointList list1 = (PointList)initialStates.get(conn);
	PointList list2 = (PointList)finalStates.get(conn);
	if (list1 == null) {
		conn.setVisible(false);
		return true;
	}
	if (list1.size() == list2.size()) {
		Point pt1 = new Point(), pt2 = new Point();
		PointList points = conn.getPoints();
		for (int i = 0; i < points.size(); i++) {
			list1.getPoint(pt2, i);
			list2.getPoint(pt1, i);
			pt1.x = (int)Math.round(pt1.x * progress + (1-progress) * pt2.x);
			pt1.y = (int)Math.round(pt1.y * progress + (1-progress) * pt2.y);
			points.setPoint(pt1, i);
		}
		conn.setPoints(points);
	}
	return true;
}

static boolean playbackState(IFigure container) {
	if (!PLAYBACK)
		return false;

	List children = container.getChildren();
	Rectangle rect1, rect2;
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		rect1 = (Rectangle)initialStates.get(child);
		rect2 = (Rectangle)finalStates.get(child);
		if (rect2 == null)
			continue;
		child.setBounds(new Rectangle(
			(int)Math.round(progress * rect2.x + (1-progress) * rect1.x),
			(int)Math.round(progress * rect2.y + (1-progress) * rect1.y),
			(int)Math.round(progress * rect2.width + (1-progress) * rect1.width),
			(int)Math.round(progress * rect2.height + (1-progress) * rect1.height)
		));
//		child.invalidate();
	}
	return true;
}

static void recordFinalState(Connection conn) {
	//$TODO
	PointList points = conn.getPoints().getCopy();
	finalStates.put(conn, points);
}

static void recordFinalState(IFigure child) {
	if (child instanceof Connection) {
		recordFinalState((Connection)child);
		return;
	}
	Rectangle rect2 = child.getBounds().getCopy();
	Rectangle rect1 = (Rectangle)initialStates.get(child);
	if (rect1.isEmpty())
			rect1.setLocation(rect2.getLocation());
	finalStates.put(child, rect2);
}

static void recordInitialState(Connection connection) {
	if (!RECORDING)
		return;
	PointList points = connection.getPoints().getCopy();
	if (points.size() == 2
	  && points.getPoint(0).equals(Point.SINGLETON.setLocation(0,0))
	  && points.getPoint(1).equals(Point.SINGLETON.setLocation(100,100)))
		initialStates.put(connection, null);
	else
		initialStates.put(connection, points);
}

static void recordInitialState(IFigure container) {
	if (!RECORDING)
		return;

	List children = container.getChildren();
	IFigure child;
	for (int i=0; i<children.size();i++) {
		child = (IFigure)children.get(i);
		initialStates.put(child, child.getBounds().getCopy());
	}
}

static void swap() {
	Map temp = finalStates;
	finalStates = initialStates;
	initialStates = temp;
}

static boolean step() {
	current = System.currentTimeMillis() + 30;
	progress = (double)(current - start)/(finish - start);
	progress = Math.min(progress, 0.999);
	Iterator iter = initialStates.keySet().iterator();
	
	while (iter.hasNext())
		((IFigure)iter.next()).revalidate();
	viewport.validate();
	
//	Point loc = viewport.getViewLocation();
//	loc.translate(trackMe.getBounds().getLocation().getDifference(trackLocation));
//	viewport.setViewLocation(loc);
//	trackLocation = trackMe.getBounds().getLocation();
	
	return current < finish;
}

}
