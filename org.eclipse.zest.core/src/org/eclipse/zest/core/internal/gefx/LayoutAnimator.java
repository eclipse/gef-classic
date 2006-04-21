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

import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Point;

/**
 * This runs animated layouts.  I am deprecating this because GEF provides
 * its own animated layout.  That should be used.
 * @author Ian Bull
 * @deprecated
 */
public class LayoutAnimator {
	
	public static final int NUMBER_OF_STEPS = 10;
	public void animateNodes( AnimateableNode[] nodes ) {
		UpdateManager updateManager = null;
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].startAnimation();
			updateManager = nodes[i].getFigure().getUpdateManager();
		}
		
		for ( int step = 0; step < NUMBER_OF_STEPS; step++ ) {
			for (int i = 0; i < nodes.length; i++) {
				AnimateableNode node = nodes[i];
				Point start = node.getStartLocation();
				Point end = node.getEndLocation();
				int newX = ((end.x - start.x)/NUMBER_OF_STEPS)*step + start.x;
				int newY = ((end.y - start.y)/NUMBER_OF_STEPS)*step + start.y;
				node.updateLocation(new Point(newX, newY));

				node.getFigure().revalidate();
				
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateManager.performUpdate();
		}
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].endAnimation();
		}
	}

}
