package org.eclipse.draw2d.graph;

import org.eclipse.draw2d.geometry.Insets;

/**
 * 
 * @author hudsonr
 * Created on Jul 18, 2003
 */
public class SubgraphBoundary extends Node {

/**
 * constant indicating TOP.
 */
public static final int TOP = 0;

/**
 * constant indicating LEFT.
 */
public static final int LEFT = 1;

/**
 * constant indicating BOTTOM.
 */
public static final int BOTTOM = 2;

/**
 * constant indicating RIGHT.
 */
public static final int RIGHT = 3;

/**
 * Constructs a new boundary.
 * @param s the subgraph
 * @param p the padding
 * @param side which side
 */
public SubgraphBoundary(Subgraph s, Insets p, int side) {
	super(null, s);
	// TODO Auto-generated constructor stub
	this.width = s.width;
	//$TODO width of head/tail should be 0
	this.height = s.height;
	this.padding = new Insets(p);
	switch (side) {
		case LEFT :
			width = s.insets.left;
			y = s.y;
			padding.right = s.innerPadding.left;
			setParent(s.getParent());
			data = "left(" + s + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case RIGHT :
			width = s.insets.right;
			y = s.y;
			padding.left = s.innerPadding.right;
			setParent(s.getParent());
			data = "right(" + s + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case TOP :
			height = s.insets.top;
			width = 20;
			padding.bottom = s.innerPadding.top;
			data = "top(" + s + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			break;
		case BOTTOM :
			height = s.insets.bottom;
			width = 20;
			padding.top = s.innerPadding.bottom;
			data = "bottom(" + s + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			break;
	}
}

}
