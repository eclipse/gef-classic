/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation Chisel Group,
 * University of Victoria - Adapted for XY Scaled Graphics
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets.internal;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;

/**
 * The root figure for Zest.  The figure is broken up into four segments, 
 * 1. The Connections
 * 2. The Nodes
 * 3. The Highlighted Connections
 * 4. The Highlighted Nodes
 * 
 * @author Ian Bull
 * 
 */
public class ZestRootLayer extends FreeformLayer {

	private int numberOfNodes = 0;
	private int numberOfConnections = 0;
	private int numberOfHighlightedNodes = 0;
	private int numberOfHighlightedConnections = 0;

	/**
	 * Adds a node to the ZestRootLayer
	 * @param nodeFigure The figure representing the node
	 */
	public void addNode(IFigure nodeFigure) {
		int nodePosition = getNodePosition();
		numberOfNodes++;
		add(nodeFigure, nodePosition);
	}

	/**
	 * Removes a node from the layer
	 * @param nodeFigure
	 */
	public void removeNode(IFigure nodeFigure) {
		if (!this.getChildren().contains(nodeFigure)) {
			throw new RuntimeException("Node not contained on the ZestRootLayer");
		}
		int nodePosition = this.getChildren().indexOf(nodeFigure);
		if (nodePosition > getHighlightNodePosition()) {
			// The node is in the highlight node area
			numberOfHighlightedNodes--;
		} else {
			// The node is in the node area
			numberOfNodes--;
		}
		this.remove(nodeFigure);
	}

	public void removeConnection(IFigure connectionFigure) {
		int connectionPosition = this.getChildren().indexOf(connectionFigure);
		if (connectionPosition > getHighlightConnectionPosition()) {
			// The connection is in the highlight connection area
			numberOfHighlightedConnections--;
		} else {
			// The connection is in the connection area
			numberOfConnections--;
		}
		this.remove(connectionFigure);
	}

	public void addConnection(IFigure connectionFigure) {
		int connectionPosition = getConnectionPosition();
		numberOfConnections++;
		add(connectionFigure, connectionPosition);
	}

	public void highlightNode(IFigure nodeFigure) {
		this.numberOfNodes--;
		int highlightNodePosition = getHighlightNodePosition();
		this.numberOfHighlightedNodes++;
		this.getChildren().remove(nodeFigure);
		this.getChildren().add(highlightNodePosition, nodeFigure);
		this.invalidate();
		this.repaint();
	}

	public void highlightConnection(IFigure connectionFigure) {
		this.numberOfConnections--;
		int highlightConnectionPosition = getHighlightConnectionPosition();
		this.numberOfHighlightedConnections++;
		this.getChildren().remove(connectionFigure);
		this.getChildren().add(highlightConnectionPosition, connectionFigure);
		this.invalidate();
		this.repaint();
	}

	public void unHighlightNode(IFigure nodeFigure) {
		int nodePosition = this.getChildren().indexOf(nodeFigure);
		if (nodePosition > getHighlightNodePosition() + this.numberOfHighlightedNodes) {
			//throw new RuntimeException("Node: " + nodeFigure + " not currently Highlighted");
			return;
		}
		this.numberOfHighlightedNodes--;
		nodePosition = getNodePosition();
		this.numberOfNodes++;
		this.getChildren().remove(nodeFigure);
		this.getChildren().add(nodePosition, nodeFigure);
		this.invalidate();
		this.repaint();
	}

	public void unHighlightConnection(IFigure connectionFigure) {
		int connectionPosition = this.getChildren().indexOf(connectionFigure);
		if (connectionPosition > getHighlightConnectionPosition() + this.numberOfHighlightedConnections) {
			//throw new RuntimeException("Connection: " + connectionFigure + " not currently Highlighted");
			return;
		}
		this.numberOfHighlightedConnections--;
		this.numberOfConnections++;
		connectionPosition = getConnectionPosition();
		this.getChildren().remove(connectionFigure);
		this.getChildren().add(connectionPosition, connectionFigure);
		this.invalidate();
		this.repaint();
	}

	private int getNodePosition() {
		return numberOfConnections;
	}

	private int getConnectionPosition() {
		return 0;
	}

	private int getHighlightNodePosition() {
		return numberOfConnections + numberOfHighlightedConnections + numberOfNodes;
	}

	private int getHighlightConnectionPosition() {
		return numberOfConnections + numberOfNodes;
	}

}
