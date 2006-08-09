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
package org.eclipse.mylar.zest.layouts.dataStructures;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.constraints.BasicEdgeConstraints;

/**
 * @author Ian Bull
 */
public class InternalRelationship {
	
	private LayoutRelationship externalRelationship;
	private InternalNode source;
	private InternalNode destination;
	private List bendPoints = new LinkedList();
	BasicEdgeConstraints basicEdgeConstraints = new BasicEdgeConstraints();
	
	public InternalRelationship( LayoutRelationship externalRelationship, InternalNode source, InternalNode destination) {
		this.externalRelationship = externalRelationship;
		this.externalRelationship.setLayoutInformation(this);
		this.source = source;
		this.destination = destination;
		this.externalRelationship.populateLayoutConstraint(basicEdgeConstraints);
	}
	
	public LayoutRelationship getLayoutRelationship() {
		return externalRelationship;
	}
	
	public InternalNode getSource() {
		if ( this.source == null ) throw new RuntimeException("Source is null");
		return this.source;
	}
	
	public InternalNode getDestination() {
		if ( this.destination == null ) throw new RuntimeException("Dest is null");
		return this.destination;
	}
	
	public double getWeight() {
		return this.basicEdgeConstraints.weight;
	}
	
	public boolean isBidirectional() {
		return this.basicEdgeConstraints.isBiDirectional;
	}
	
	/**
	 * Ensure this is called in order of source to target node position.
	 * @param x
	 * @param y
	 */
	public void addBendPoint(double x, double y) {
		bendPoints.add(new BendPoint(x, y));
	}
	
	public List getBendPoints() {
		return this.bendPoints;
	}

}
