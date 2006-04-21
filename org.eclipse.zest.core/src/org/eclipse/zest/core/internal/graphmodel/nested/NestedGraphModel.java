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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.swt.widgets.Canvas;



/**
 * Extends {@link GraphModel} to provider support for nested graphs.
 * Instead of holding a list of all the nodes, the model now only
 * holds the root node, the current node and the previous node.  All other
 * nodes can be retrieved by traversing the children/parent hierarchy.
 * 
 * @author Chris Callendar
 */
public class NestedGraphModel extends GraphModel {

	private final int STACK_SIZE = 10;
	
	private NestedGraphModelNode rootNode;
	private NestedGraphModelNode currentNode;
	private NestedGraphModelNode previousNode;
	
	private LinkedList backStack;
	private LinkedList forwardStack;
	
	private Rectangle mainArea;

	/**
	 * Initializes the model with the given canvas.
	 * @param canvas
	 */
	public NestedGraphModel(Canvas canvas) {
		super(canvas);
		this.backStack = new LinkedList();
		this.forwardStack = new LinkedList();
		this.mainArea = new Rectangle();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#toString()
	 */
	public String toString() {
		return "NestedGraphModel {" + 
			(rootNode != null ? "Root: " + rootNode.getText() + ", ": "") +
			(currentNode != null ? "Current: " + currentNode.getText() + ", " : "") + 
			connections.size() + " connections}";
	}
	

	
	/* (non-Javadoc)
	 * @see ca.uvic.cs.zest.internal.graphmodel.GraphModel#removeNodeFromList(ca.uvic.cs.zest.internal.graphmodel.GraphModelNode)
	 */
	protected boolean removeNodeFromList(GraphModelNode node) {
		return true;
	}
	
	/**
	 * Returns the children nodes under the current node.
	 * @return List of NestedGraphModelNode objects which are the children of the current node
	 */
	public List getNodes() {
		if ( currentNode != null ) {
			ArrayList al = new ArrayList(  );
			al.add( currentNode );
			return al;
		}
		else { 
			ArrayList al = new ArrayList();
			al.add( rootNode );
			return al;
		}
		
	}
	
	/**
	 * Gets the root node for this model.  If this model is of a flat graph
	 * then the root node will be null.  Otherwise the root node will be returned.
	 * @return NestedGraphModelNode
	 */
	public NestedGraphModelNode getRootNode() {
		return rootNode;
	}	
	
	/**
	 * Sets the root node for this model.
	 * @param rootNode
	 */
	public void setRootNode(NestedGraphModelNode rootNode) {
		this.rootNode = rootNode;
		if (currentNode == null) {
			setCurrentNode(rootNode);
		}
	}

	/**
	 * Returns the current node.
	 * @return NestedGraphModelNode
	 */
	public NestedGraphModelNode getCurrentNode() {
		return currentNode;		
	}
	
	/**
	 * Sets the current node.  The previous node is added to the back stack.
	 * @param newNode the new current node (can't be null)
	 */
	public void setCurrentNode(NestedGraphModelNode newNode) {
		if (newNode == null)
			return;
		
		addToBackStack(currentNode);
		setCurrent(newNode);
	}	
	
	/**
	 * Sets the current node.  The back and forward stacks are not touched.
	 * @param node
	 */
	private void setCurrent(NestedGraphModelNode node) {
		if (currentNode != null) {
			previousNode = currentNode;
			previousNode.setCurrent(false);
		}
		currentNode = node;
		currentNode.setCurrent(true);
		currentNode.setChildrenVisible(true);
	}

	/**
	 * Gets the previous root node for this graph model
	 * @return NestedGraphModelNode
	 */
	public	NestedGraphModelNode getPreviousRootNode() {
		return previousNode;
	}	
	
	// BACK/FORWARD/UP methods
	
	/**
	 * Returns true if there are entries in the Back button stack.
	 * @return boolean
	 */
	public boolean hasBackNode() {
		return (backStack.size() > 0);
	}
	
	/**
	 * Returns true if there are entries in the Forward button stack.
	 * @return boolean
	 */
	public boolean hasForwardNode() {
		return (forwardStack.size() > 0);
	}
	
	/**
	 * Returns true if the current node has a parent (which isn't the root).
	 * @return boolean
	 */
	public boolean hasParentNode() {
		boolean hasParent = (currentNode != null) && 
							(currentNode != rootNode) && 
							(currentNode.getParent() != null); 
		return hasParent;
	}
	
	/**
	 * Goes back to the previous node.
	 */
	public void goBack() {
		if (hasBackNode()) {
			addToForwardStack(currentNode);
			NestedGraphModelNode node = popBackStack();
			setCurrent(node);
		}
	}
	
	public void goForward() {
		if (hasForwardNode()) {
			addToBackStack(currentNode);
			NestedGraphModelNode node = popForwardStack();
			setCurrent(node);
		}
	}

	/**
	 * Sets the parent of the current node to the be the 
	 * new current node.
	 */
	public void goUp() {
		if (hasParentNode()) {
			addToBackStack(currentNode);
			setCurrent(currentNode.getCastedParent());
		}
	}
	
	/**
	 * Adds the given node to the front of the list (top of the stack).
	 * @param node
	 */
	private void addToBackStack(NestedGraphModelNode node) {
		if (node == null)
			return;

		//  check to make sure that the top isn't the same as the new one
		if (backStack.size() > 0) {
			NestedGraphModelNode top = (NestedGraphModelNode)backStack.getFirst();
			if (top == node)
				return;
		}
		// if the stack is too big, remove the last node
		if (backStack.size() >= STACK_SIZE) {
			backStack.removeLast();
		}
		backStack.addFirst(node);
	}
	
	/**
	 * Adds the given node to the front of the forward stack.
	 * @param node
	 */
	private void addToForwardStack(NestedGraphModelNode node) {
		if (node == null)
			return;
		
		//  check to make sure that the top isn't the same as the new one
		if (forwardStack.size() > 0) {
			NestedGraphModelNode top = (NestedGraphModelNode)forwardStack.getFirst();
			if (top == node)
				return;
		}
		// if the stack is too big, remove the bottom (last) node
		if (forwardStack.size() >= STACK_SIZE) {
			forwardStack.removeLast();
		}
		forwardStack.addFirst(node);
	}
	
	/**
	 * Removes the top node from the back stack.
	 * @return NestedGraphModelNode
	 */
	private NestedGraphModelNode popBackStack() {
		if (backStack.size() > 0) {
			return (NestedGraphModelNode)backStack.removeFirst();
		}
		return null;
	}
	
	/**
	 * Removes the top node from the forward stack.
	 * @return NestedGraphModelNode
	 */
	private NestedGraphModelNode popForwardStack() {
		if (forwardStack.size() > 0) {
			return (NestedGraphModelNode)forwardStack.removeFirst();
		}
		return null;
	}

	/**
	 * Sets the main (scrollable) area.
	 * @param mainArea
	 */
	public void setMainArea(Rectangle area) {
		this.mainArea.setBounds(area); 
	}
	
	/**
	 * Returns a copy of the main (scrollable) area.
	 * @return Rectangle
	 */
	public Rectangle getMainArea() {
		return mainArea.getCopy();
	}
	
	
}
