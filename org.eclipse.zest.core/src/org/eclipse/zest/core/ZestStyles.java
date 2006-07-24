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
	//@tag style(graph)
	public static final int NONE = 0;

	/** 
	 * Style constant indicating if marquee selection (multiple selection) 
	 * is allowed. 
	 */
	//@tag style(graph)
	public static final int MARQUEE_SELECTION = 1 << 0;
	
	/** 
	 * Style constant indicating if panning (moving root edit part canvas) is allowed.  
	 * You cannot have marquee selection and panning enabled at the same time.
	 */
	//@tag style(graph)
	public static final int PANNING = 1 << 1;
	
	/** 
	 * Style constant for nested graphs which indicates that real zooming 
	 * will be done when navigating in or out of nodes.
	 */
	//@tag style(graph(later))
	public static final int ZOOM_REAL = 1 << 5;
	
	/** 
	 * Style constant for nested graphs which indicates that expand and collapse zooming 
	 * will be done when navigating in or out of nodes.  This is not real zooming, instead
	 * the nodes will expand to fill the area giving the impression of zooming. 
	 */
	//@tag style(graph(later))
	public static final int ZOOM_EXPAND = 1 << 6;
	
	/** 
	 * Style constant for nested graphs which indicates that fake zooming 
	 * will be done when navigating in or out of nodes.  This consists of a 
	 * dotted rectangle expanding to fill the area when zooming in, or a rectangle shrinking
	 * down to the size of the node.
	 */
	//@tag style(graph(later))
	public static final int ZOOM_FAKE = 1 << 7;
	
	
	/** 
	 * A style constant which indicates that nodes aren't 
	 * allowed to overlap. Note: this is a hint.
	 */
	//@tag style(graph(hint))
	public static final int NO_OVERLAPPING_NODES = 1 << 10;
	
	/** 
	 * Style constant indicating whether figures can be moved outside the bounding
	 * rectangle.  If enforce bounds is not set then scrollbars will probably be used.
	 */ 
	//@tag style(graph)
	public static final int ENFORCE_BOUNDS = 1 << 11;
	

	
	/** 
	 * Style constant indicating if the selected node's neighbors 
	 * should be highlighted. Note: this is a node-level style. It should not
	 * be applied to graph views during construction.
	 * @see  
	 */
	//@tag style(node)
	public static final int NODES_HIGHLIGHT_ADJACENT = 1 << 1;

	
	
	/**
	 * Style indicating that connections should show their direction by default.
	 */
	//@tag style(edge) : use a new name... this may be useful later for algorithms.
	public static final int CONNECTIONS_DIRECTED = 1 << 5;
	
		
	/**
	 * Style constant to indicate that connections between nodes should be curved
	 * by default. A connection cannot by styled with both CONNECTIONS_CURVED and
	 * CONNECTIONS_STRAIGHT.
	 */
	//@tag style(arcs)
	public static final int CONNECTIONS_CURVED = 1 << 1;
	
	/**
	 * Style constant to indicate that connections between nodes should be straight by
	 * default.  A connection cannot by styled with both CONNECTIONS_CURVED and
	 * CONNECTIONS_STRAIGHT.
	 */
	//@tag style(arcs)
	public static final int CONNECTIONS_STRAIGHT = 1 << 0;
	/**
	 * Bitwise ANDs the styleToCheck integer with the given style.  
	 * @param style
	 * @param styleToCheck
	 * @return boolean if styleToCheck is part of the style
	 */
	public static boolean checkStyle(int style, int styleToCheck) {
		return ((style & styleToCheck) == styleToCheck);
	}
	
	public static boolean validateConnectionStyle(int style) {
		int illegal = CONNECTIONS_CURVED | CONNECTIONS_STRAIGHT;
		style &= illegal;
		int rightBit = style & (-style);
		return (style == rightBit);
	}
}
