/*******************************************************************************
 * Copyright 2005-2007, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets;

/**
 * Style constants used in Zest.
 * 
 * @author Chris Callendar
 */
public final class ZestStyles {

	/**
	 * A constant known to be zero (0), used in operations which take bit flags
	 * to indicate that "no bits are set".
	 */
	// @tag style(graph)
	public static final int NONE = 0;

	/**
	 * Style constant indicating if marquee selection (multiple selection) is
	 * allowed.
	 */
	// @tag style(graph)
	public static final int MARQUEE_SELECTION = 1 << 0;

	/**
	 * Style constant indicating if panning (moving root edit part canvas) is
	 * allowed. You cannot have marquee selection and panning enabled at the
	 * same time.
	 */
	// @tag style(graph)
	public static final int PANNING = 1 << 1;

	/**
	 * Style constant for nested graphs which indicates that real zooming will
	 * be done when navigating in or out of nodes.
	 */
	// @tag style(graph(later))
	public static final int ZOOM_REAL = 1 << 5;

	/**
	 * Style constant for nested graphs which indicates that expand and collapse
	 * zooming will be done when navigating in or out of nodes. This is not real
	 * zooming, instead the nodes will expand to fill the area giving the
	 * impression of zooming.
	 */
	// @tag style(graph(later))
	public static final int ZOOM_EXPAND = 1 << 6;

	/**
	 * Style constant for nested graphs which indicates that fake zooming will
	 * be done when navigating in or out of nodes. This consists of a dotted
	 * rectangle expanding to fill the area when zooming in, or a rectangle
	 * shrinking down to the size of the node.
	 */
	// @tag style(graph(later))
	public static final int ZOOM_FAKE = 1 << 7;

	/**
	 * A style constant which indicates that nodes aren't allowed to overlap.
	 * Note: this is a hint.
	 */
	// @tag style(graph(hint))
	//public static final int NO_OVERLAPPING_NODES = 1 << 10;
	/**
	 * Style constant indicating whether figures can be moved outside the
	 * bounding rectangle. If enforce bounds is not set then scrollbars will
	 * probably be used.
	 */
	// @tag style(graph)
	//public static final int ENFORCE_BOUNDS = 1 << 11;
	/**
	 * Style constant indicating that invisible nodes should be ignored for
	 * layouts.
	 */
	// @tag style.graph
	public static final int IGNORE_INVISIBLE_LAYOUT = 1 << 12;

	/**
	 * Style constant indicating if the selected node's neighbors should be
	 * highlighted. Note: this is a node-level style. It should not be applied
	 * to graph views during construction.
	 * 
	 * @see
	 */
	// @tag style(node)
	public static final int NODES_HIGHLIGHT_ADJACENT = 1 << 1;

	/**
	 * Style constant indicating that node labels should be cached. This is
	 * important under GTK+ because font drawing is slower than Windows.
	 */
	public static final int NODES_CACHE_LABEL = 1 << 2;

	/**
	 * Style constant indiciating that nodes should not be resized on layout.
	 */
	public static final int NODES_NO_LAYOUT_RESIZE = 1 << 3;

	/**
	 * Style indicating that connections should show their direction by default.
	 */
	// @tag style(edge) : use a new name... this may be useful later for
	// algorithms.
	public static final int CONNECTIONS_DIRECTED = 1 << 5;

	/**
	 * Style indicating that the arrow indicating the direction of the
	 * connection should be open.
	 */
	// @tag style(edge)
	public static final int CONNECTIONS_OPEN = 1 << 4;

	/**
	 * Style indicating that lables should be placed in the middle of
	 * connections. This is the default.
	 */
	// @tag style.arcs
	// @tag zest.bug.160368-ConnectionAlign.fix
	public static final int CONNECTIONS_VALIGN_MIDDLE = 1 << 11;

	/**
	 * Style indicating that labels should be placed above connections.
	 */
	// @tag style.arcs
	// @tag zest.bug.160368-ConnectionAlign.fix
	public static final int CONNECTIONS_VALIGN_TOP = 1 << 12;

	/**
	 * Style indicating that labels should be placed below connections.
	 */
	// @tag style.arcs
	// @tag zest.bug.160368-ConnectionAlign.fix
	public static final int CONNECTIONS_VALIGN_BOTTOM = 1 << 13;

	/**
	 * "Horizontal" alignment constant. Labels should be placed at the beginning
	 * of the line. Figures will be anchored so that they have one end at the
	 * beginning of the connection, not so that they are centered at the start
	 * point. Which end of the figure is placed at that point will depend on the
	 * direction of the first two points.
	 */
	public static final int CONNECTIONS_HALIGN_START = 1 << 14;

	/**
	 * "Horizontal" alignment constant. Labels should be placed at the end of
	 * the line. Figures will be anchored so that they have one end at the
	 * beginning of the connection, not so that they are centered at the end
	 * point. Which end of the figure is placed at that point will depend on the
	 * direction of the last two points.
	 */
	public static final int CONNECTIONS_HALIGN_END = 1 << 15;

	/**
	 * "Horizontal" alignment constant. Figures should be placed in the center
	 * of the points on the line.
	 */
	public static final int CONNECTIONS_HALIGN_CENTER = 1 << 16;

	/**
	 * "Horizontal" alignment constant. Labels should be centered between the
	 * first two points on the connection. For connections with only two points,
	 * this will behave the same as CENTER.
	 */
	public static final int CONNECTIONS_HALIGN_CENTER_START = 1 << 17;
	/**
	 * "Horizontal" alignment constant. Labels should be centered between the
	 * last two points on the connection. For connections with only two points,
	 * this will behave the same as CENTER.
	 */
	public static final int CONNECTIONS_HALIGN_CENTER_END = 1 << 18;
	/**
	 * Style constant to indicate that connections between nodes should be
	 * curved by default. A connection cannot by styled with any of
	 * CONNECTIONS_CURVED, CONNECTIONS_STRAIGHT, or CONNECTIONS_BEZIER at the
	 * same time.
	 */
	// @tag style(arcs) TODO curves bezier : add back the curve stuff
	// public static final int CONNECTIONS_CURVED = 1 << 1;
	/**
	 * Style constant to indicate that connections between nodes should be
	 * straight by default. A connection cannot by styled with any of
	 * CONNECTIONS_CURVED, CONNECTIONS_STRAIGHT, or CONNECTIONS_BEZIER at the
	 * same time.
	 */
	// @tag style(arcs)
	public static final int CONNECTIONS_STRAIGHT = 1 << 0;

	/**
	 * Style constant to indicate that connections should be drawn with solid
	 * lines (this is the default).
	 */
	// @tag style(arcs)
	public static final int CONNECTIONS_SOLID = 1 << 6;

	/**
	 * Style constant to indicate that connections should be drawn with dashed
	 * lines.
	 */
	// @tag style(arcs)
	public static final int CONNECTIONS_DASH = 1 << 7;

	/**
	 * Style constant to indicate that connections should be drawn with dotted
	 * lines.
	 */
	// @tag style(arcs)
	public static final int CONNECTIONS_DOT = 1 << 8;

	/**
	 * Style constant to indicate that connections should be drawn with
	 * dash-dotted lines.
	 */
	// @tag style(arcs)
	public static final int CONNECTIONS_DASH_DOT = 1 << 9;

	/**
	 * Style constant to indicate that connections between nodes should be
	 * bezier-S shaped. A connection cannot by styled with any of
	 * CONNECTIONS_CURVED, CONNECTIONS_STRAIGHT, or CONNECTIONS_BEZIER at the
	 * same time.
	 * 
	 * Bezier curves are defined by a set of four points: two point in the
	 * layout (start and end), and two related control points (also start and
	 * end). The control points are defined relative to their corresponding
	 * layout point. This definition includes an angle between the layout point
	 * and the line between the two layout points, as well as a ratio distance
	 * from the corresponding layout point. The ratio distance is defined as a
	 * fraction between 0 and 1 of the distance between the two layout points.
	 * Using this definition allows bezier curves to have a consistant look
	 * regardless of the actual positions of the nodes in the layouts.
	 * 
	 * @see IConnectionStyleBezierExtension
	 * @see IConnectionEntityStyleBezierExtension
	 */

	// @tag style(arcs) TODO curves bezier : add back the curve stuff 
	// public static final int CONNECTIONS_BEZIER = 1 << 2;
	/**
	 * Style constant to indicate that the Tree Graph Viewer should be
	 * compressed.
	 * 
	 * @tag style(tree_graph)
	 */
	public static final int TREE_GRAPH_COMPRESS = 1 << 0;

	/**
	 * Style constant to indicate that the Tree Graph Viewer should use a
	 * default "hanging" layout instead of the bushy "Normal" tree layout.
	 * 
	 * @tag style(tree_graph)
	 */
	public static final int TREE_GRAPH_HANGING_LAYOUT = 1 << 1;

	/**
	 * Bitwise ANDs the styleToCheck integer with the given style.
	 * 
	 * @param style
	 * @param styleToCheck
	 * @return boolean if styleToCheck is part of the style
	 */
	public static boolean checkStyle(int style, int styleToCheck) {
		return ((style & styleToCheck) == styleToCheck);
	}

	/**
	 * Validates the given style for connections to see if it is legal. Returns
	 * false if not.
	 * 
	 * @param style
	 *            the style to check.
	 * @return true iff the given style is legal.
	 */
	public static boolean validateConnectionStyle(int styleToValidate) {
		int style = styleToValidate;
		// int illegal = CONNECTIONS_CURVED | CONNECTIONS_STRAIGHT |
		// CONNECTIONS_BEZIER;
		int illegal = CONNECTIONS_STRAIGHT;
		style &= illegal;
		int rightBit = style & (-style);
		boolean okay = (style == rightBit);
		if (!okay) {
			return okay;
		}

		illegal = CONNECTIONS_DASH_DOT | CONNECTIONS_DASH | CONNECTIONS_DOT | CONNECTIONS_SOLID;
		style = styleToValidate;
		style &= illegal;
		rightBit = style & (-style);
		okay = (style == rightBit);
		if (!okay) {
			return okay;
		}

		// @tag zest.bug.160368-ConnectionAlign.fix : must check the connections
		// to make sure that there isnt' an illegal combination of alignments.
		illegal = CONNECTIONS_VALIGN_BOTTOM | CONNECTIONS_VALIGN_MIDDLE | CONNECTIONS_VALIGN_TOP;
		style = styleToValidate;
		style &= illegal;
		rightBit = style & (-style);
		return (style == rightBit);
	}
}
