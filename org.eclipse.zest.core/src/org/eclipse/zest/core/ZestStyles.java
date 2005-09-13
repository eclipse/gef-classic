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
package org.eclipse.mylar.zest.core;

/**
 * Style constants used in Zest.
 * 
 * @author Chris Callendar
 */
public final class ZestStyles {

	/**
	 * A constant known to be zero (0), used in operations which
	 * take bit flags to indicate that "no bits are set".
	 */
	public static final int NONE = 0;

	/** Style constant indicating if marquee selection (multiple selection) is allowed. */
	public static final int MARQUEE_SELECTION = 1;
	
	/** 
	 * Style constant indicating if panning (moving root edit part canvas) is allowed.  You cannot have 
	 * marquee selection and panning enabled at the same time.
	 */ 
	public static final int PANNING = 1 << 1;
	
	/** Style constant indicating if the selected node's neighbors should be highlighted. */
	public static final int HIGHLIGHT_ADJACENT_NODES = 1 << 2;

	/** A style constant which indicates that nodes aren't allowed to overlap.	 */
	public static final int NO_OVERLAPPING_NODES = 1 << 3;
	
	/** 
	 * A style constant which indicates if nodes should stabilize when their change
	 * in positions become small
	 */
	public static final int STABILIZE = 1 << 4;
	
	/** 
	 * Style constant for nested graphs which indicates that real zooming 
	 * will be done when navigating in or out of nodes.
	 */
	public static final int ZOOM_REAL = 1 << 5;
	
	/** 
	 * Style constant for nested graphs which indicates that expand and collapse zooming 
	 * will be done when navigating in or out of nodes.  This is not real zooming, instead
	 * the nodes will expand to fill the area giving the impression of zooming. 
	 */
	public static final int ZOOM_EXPAND = 1 << 6;
	
	/** 
	 * Style constant for nested graphs which indicates that fake zooming 
	 * will be done when navigating in or out of nodes.  This consists of a 
	 * dotted rectangle expanding to fill the area when zooming in, or a rectangle shrinking
	 * down to the size of the node.
	 */
	public static final int ZOOM_FAKE = 1 << 7;

	/** 
	 * Style constant indicating whether figures can be moved outside the bounding
	 * rectangle.  If enforce bounds is not set then scrollbars will probably be used.
	 */ 
	public static final int ENFORCE_BOUNDS = 1 << 8;
	
	/**
	 * Style constant indicating that the graph is a directed graph and 
	 * arrows should be used
	 */
	public static final int DIRECTED_GRAPH = 1 << 9;
	
	
	/**
	 * Bitwise ANDs the styleToCheck integer with the given style.  
	 * @param style
	 * @param styleToCheck
	 * @return boolean if styleToCheck is part of the style
	 */
	public static boolean checkStyle(int style, int styleToCheck) {
		return ((style & styleToCheck) == styleToCheck);
	}
}
