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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * This allows a node to be animated
 * @author Ian Bull
 *
 */
public interface AnimateableNode {
	
	public Point getStartLocation();
	
	public Point getEndLocation();
	
	public Point getCurrentLocation();
	
	public void updateLocation( Point p );
	
	public void startAnimation();
	
	/**
	 * This should be implemented by nodes that
	 * wish to be animated.  This should
	 * end the animation, that is, remove an temp locations
	 */
	public void endAnimation();
	
	public IFigure getFigure();

}
