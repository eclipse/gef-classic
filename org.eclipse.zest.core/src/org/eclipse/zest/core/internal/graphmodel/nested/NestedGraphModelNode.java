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
package org.eclipse.mylar.zest.core.internal.graphmodel.nested;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.layouts.NestedLayoutEntity;
import org.eclipse.swt.graphics.Image;



/**
 * Extends GraphModelNode to add methods that deal with nested graphs.
 * 
 * @author Chris Callendar
 * @author Ian Bull
 */
public class NestedGraphModelNode extends GraphModelNode implements NestedLayoutEntity {

	/*
	 * Tree Constants
	 */
	public static final int SAME_NODE = 0;
	public static final int DESCENDANT = 1;
	public static final int ANCESTOR = 2;
	public static final int NO_RELATION = 3;

	public static final int PLUS_SIZE = 16;
	
	private NestedGraphModelNode parent;
	private List children;
	private int depth;		// the depth of this node.  Root nodes (null parent) are at a depth 0
	private double widthScale;
	private double heightScale;
	private boolean isCurrent;
	private boolean childrenVisible;
	private Rectangle childrenBounds;
	private Dimension minimizedSize;
	private Dimension fullSize;
	
	public NestedGraphModelNode(NestedGraphModel graphModel, Object externalNode) {
		super(graphModel, externalNode);
	}

	public NestedGraphModelNode(NestedGraphModel graphModel, String label, Object externalNode) {
		super(graphModel, label, externalNode);
	}

	public NestedGraphModelNode(NestedGraphModel graphModel, Image img, Object externalNode) {
		super(graphModel, img, externalNode);
	}

	public NestedGraphModelNode(NestedGraphModel graphModel, String label, Image img, Object externalNode) {
		super(graphModel, label, img, externalNode);
	}

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.GraphModelNode#initModel(java.lang.Object)
	 */
	protected void initModel(GraphModel graphModel, Object externalNode) {
		super.initModel(graphModel, externalNode);
		this.parent = null;
		this.children = new ArrayList();
		this.depth = 0;
		this.widthScale = 1.0;
		this.heightScale = 1.0;
		this.isCurrent = false;
		this.childrenVisible = false;
	}
	
	public String toString() {
		//" {Parent: " + getParent().getText() + "}"
		return "NestedGraphModelNode: " + getText() + 
			("  {" + getChildren().size() + " children}");
	}
	
	/**
	 * Gets the relationship between two given nodes 
	 *  0 - Same
	 *  1 - node is a child of this
	 *  2 - node is a parent of this
	 *  3 - node is not directly related
	 * @param that	the node to compare with this
	 * @return int
	 * @see #SAME_NODE
	 * @see #ANCESTOR
	 * @see #DESCENDANT
	 * @see #NO_RELATION
	 */
	public int getRelationshipBetweenNodes( NestedGraphModelNode that ) {
		if ( that == this ) 
			return SAME_NODE;
		if ( this.isAncestorOf( that )) 
			return ANCESTOR;
		else if ( this.isDescendantOf( that )) 
			return DESCENDANT;
		else 
			return NO_RELATION;
	}
	
	private boolean isAncestorOf( NestedGraphModelNode that ) {
		NestedLayoutEntity parent = that.getParent();
		while ( parent != null && parent != this ) {
			parent = parent.getParent();
		}
		if ( parent == this ) 
			return true;
		else 
			return false;
	}
	
	private boolean isDescendantOf( NestedGraphModelNode that ) {
		return that.isAncestorOf( this );
	}	
	
	
	/**
	 * Returns the parent (or null if it is a root node).
	 * @return NestedGraphModelNode
	 */
	public NestedLayoutEntity getParent() {
		return parent;
	}
	
	/**
	 * Gets the parent casted to a NestedGraphModelNode.
	 * @return NestedGraphModelNode
	 */
	public NestedGraphModelNode getCastedParent() {
		if (parent instanceof NestedGraphModelNode) {
			return (NestedGraphModelNode) parent;
		}
		return null;
	}
	
	/**
	 * Sets the parent for this node.  This also sets the depth of nesting 
	 * of the node by traversing up the parent hierarchy counting
	 * the levels.
	 * @param parent the parent to set
	 */
	public void setParent(NestedGraphModelNode parent) {
		this.parent = parent;
		this.depth = 0;	
		for (NestedLayoutEntity node = parent; node != null; node = node.getParent()) {
			this.depth++;
		}
	}
	
	public boolean isCurrent() {
		return isCurrent;
	}
	
	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}
	
	/**
	 * Returns the nested height of this node.  Root nodes whose parent
	 * is null will be at a depth of 0.  
	 * @return int the nested height
	 */
	public int getNestedDepth() {
		return depth;
	}
		
	/**
	 * Returns the list of the NestedGraphModelNode children nodes.
	 * @return List of NestedGraphModelNode objects
	 */
	public List getChildren() {
		return children;
	}
	
	public boolean hasChildren() {
		return (children.size() > 0);
	}
		
	/**
	 * Adds the child node if it isn't in the parent hierarchy 
	 * of this node and if it doesn't already exist in the list.
	 * @param child
	 */
	public void addChild(NestedGraphModelNode child) {
		if (child != null) {
			boolean add = true;
			for (NestedLayoutEntity node = this; node != null; node = node.getParent()) {
				if (node.equals(child)) {
					add = false;
					break;
				}
			}
			if (add && !children.contains(child)) {
				children.add(child);
			}
			childrenBounds = null; 	// reset the size - will be calculated again
		}
	}

	
	/**
	 * Removes the given node from the list of children.
	 * @param nodeToRemove
	 * @return boolean if it was removed
	 */
	public boolean removeChild(GraphModelNode nodeToRemove) {
		boolean removed = false;
		if (nodeToRemove != null) {
			removed = children.remove(nodeToRemove);
		}
		if (removed) {
			childrenBounds = null;	// reset the size - will be calculated again
		}
		return removed;
	}
	
	/**
	 * Returns true if the children are visible.
	 * @return boolean
	 */
	public boolean getChildrenVisible() {
		return childrenVisible;
	}
	
	/**
	 * Sets if the children are visible and adjusts the size appropriately.
	 * @param visible
	 */
	public void setChildrenVisible(boolean visible) {
		if (visible) {
			showChildren();
		} else {
			hideChildren();
		}
	}

	/**
	 * Hides the children and collapses the node to just the label.
	 */
	public void hideChildren() {
		childrenVisible = false;
		Dimension size = getFullSize();
		super.setSizeInLayout(size.width, size.height);
		this.firePropertyChange(FORCE_REDRAW, null, null);
	}

	/**
	 * Displays the children and expands the node.
	 */
	public void showChildren() {
		childrenVisible = true;
		Dimension size = getFullSize();
		super.setSizeInLayout(size.width, size.height);
		this.firePropertyChange(FORCE_REDRAW, null, null);
	}
	
	/**
	 * Gets the full size of the node without scaling and with children shown.
	 * @return Dimension
	 */
	public Dimension getFullSize() {
		if (fullSize == null) {
			fullSize = calculateMinimumSize();
		}
		return fullSize.getCopy();
	}
	
	/**
	 * Gets the size of just the plus/minus and icon and text.
	 * @return Dimension
	 */
	public Dimension getMinimizedSize() {
		if (minimizedSize == null) {
			minimizedSize = calculateMinimumLabelSize();
			minimizedSize.width = Math.max(minimizedSize.width, getFullSize().width);
		}
		return minimizedSize.getCopy();
	}
	
	/**
	 * Sets the size of the node. 
	 * Do not use this method if you want to minize the node (label only).
	 * Call hideChildren() instead.  You can also call showChildren() to show the full size.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#setSizeInLayout(double, double)
	 */
	public void setSizeInLayout(double width, double height) {
		fullSize = new Dimension((int)width, (int)height);
		super.setSizeInLayout(width, height);
	}

	
	/**
	 * Currently this always returns 1
	 * @return
	 */
	//public double getScale() {
	//	return (isCurrent() ? 1 : scale);
	//}
	
	
	public double getWidthScale() {
		return widthScale;
	}
	public double getHeightScale() {
		return heightScale;
	}
	
	public void setScale( double w, double h ) {
		this.widthScale = w;
		this.heightScale = h;
		
	}
	
	/**
	 * The scale from (0-1].  The scale defaults to 1.
	 * @param scale
	 */
	//public void setScale(double scale) {
	//	if ((scale > 0) && (scale <= 1)) {
	//		this.scale = scale;
	//	} else {
	//		this.scale = 1 ;
	//	}
	//}

	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.GraphModelNode#calculateMinimumLabelSize()
	 */
	public Dimension calculateMinimumLabelSize() {
		if (labelSize == null) {
			labelSize = super.calculateMinimumLabelSize();
			if (hasChildren()) {
				labelSize.expand(PLUS_SIZE + 4, Math.max(PLUS_SIZE - labelSize.height, 0));
			}
		}
		return labelSize;
	}
	
	/**
	 * Calculates the minimum size of this node without taking scaling
	 * into account.  This is the full size of the label and the children.
	 * @see ca.uvic.cs.zest.internal.graphmodel.GraphModelNode#calculateMinimumSize()
	 */
	public Dimension calculateMinimumSize() {
		Dimension labelSize = calculateMinimumLabelSize();
		Rectangle childSize = calculateMinimumChildrenBounds();
		int width = childSize.width;
		int height = childSize.height;
		width = Math.max(width, labelSize.width);
		height += labelSize.height;
		return new Dimension(width, height);
	}

	/**
	 * Gets the minimum size for the children.  
	 * @return Dimension
	 */
	public Rectangle calculateMinimumChildrenBounds() {
		if (childrenBounds == null) {
			childrenBounds = new Rectangle();
			for (Iterator iter = getChildren().iterator(); iter.hasNext(); ) {
				GraphModelNode node = (GraphModelNode)iter.next();
				double x = node.getXInLayout();
				double y = node.getYInLayout();
				Dimension labelSize = node.calculateMinimumLabelSize();
				double width = x + Math.max(node.getWidthInLayout(), labelSize.width);
				double height = y + Math.max(node.getHeightInLayout(), labelSize.height);
				childrenBounds.x = (int)Math.min(childrenBounds.x, x);
				childrenBounds.y = (int)Math.min(childrenBounds.y, y);
				childrenBounds.width = (int)Math.max(childrenBounds.width, width);
				childrenBounds.height = (int)Math.max(childrenBounds.height, height);			
			}
		}
		return childrenBounds;
	}
	
	public NestedGraphModel getNestedGraphModel() {
		return (NestedGraphModel) this.getGraphModel();
	}
	
}
