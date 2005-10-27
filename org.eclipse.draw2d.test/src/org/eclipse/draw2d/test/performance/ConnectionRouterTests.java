/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test.performance;

import java.util.Random;

import org.eclipse.test.performance.Dimension;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Path;
import org.eclipse.draw2d.graph.ShortestPathRouter;

public class ConnectionRouterTests 
	extends BasePerformanceTestCase
{
	
private Random generator = new Random(1);

private void buildDiagram(ShortestPathRouter router) {
	Rectangle bounds = new Rectangle(0, 0, 75, 50);
	for (int i = 0; i < 500; i++) {
		bounds.setLocation(getPoint());
		router.addObstacle(bounds);
	}
	for (int i = 0; i < 200; i++) {
		Path path = new Path(getPoint(), getPoint());
		if (i % 10 == 0) {
			PointList list = new PointList();
			list.addPoint(getPoint());
			list.addPoint(getPoint());
			path.setBendPoints(list);
		}
		router.addPath(path);
	}
}

private Point getPoint() {
	return new Point(generator.nextInt(3000), generator.nextInt(3000));
}

public void testShortestPathRouter() {
	tagAsGlobalSummary("Shortest Path Routing Algorithm", Dimension.CPU_TIME);
	ShortestPathRouter router = new ShortestPathRouter();
	buildDiagram(router);

	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		System.gc();
		if (i > warmupRuns)
			startMeasuring();
		router.solve();
		if (i > warmupRuns)
			stopMeasuring();
	}
	commitMeasurements();
	assertPerformanceInRelativeBand(Dimension.CPU_TIME, -100, 10);
}

}