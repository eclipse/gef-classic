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
package org.eclipse.mylar.zest.layouts.algorithms;


import org.eclipse.mylar.zest.layouts.dataStructures.InternalNode;
import org.eclipse.mylar.zest.layouts.dataStructures.InternalRelationship;

/**
 * 
 * @author irbull
 * 
 * Used to represent algorithms that can continuously run.  
 *
 */
public abstract class ContinuousLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	public ContinuousLayoutAlgorithm(int styles) {
		super(styles);
	}

	/**
	 * The logic to determine if a layout should continue running or not
	 */
	protected abstract boolean performAnotherNonContinuousIteration();
	
	/**
	 * Computes a single iteration of the layout algorithm
	 * @return
	 */
	protected abstract void computeOneIteration(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y, double width, double height);
	
	

	
	private boolean continueRunning() {
		if ( layoutStopped ) return false;
		else if ( runContinuously && !layoutStopped ) return true;
		else if ( performAnotherNonContinuousIteration() ) return true;
		else return false;
	}
	
	/**
	 * Calculates and applies the positions of the given entities based on a
	 * spring layout using the given relationships.
	 */
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, 
			double x, double y, double width, double height) {
	
		while (continueRunning() ) {
			// check for entities and relationships to add or remove 
			entitiesToLayout = updateEntities(entitiesToLayout);
			relationshipsToConsider = updateRelationships(relationshipsToConsider);
			
			computeOneIteration(entitiesToLayout, relationshipsToConsider, x, y, width, height);
			updateLayoutLocations(entitiesToLayout);
			
			if ( runContinuously )
				fireProgressEvent(1,1);
			else
				fireProgressEvent(getCurrentLayoutStep(), getTotalNumberOfLayoutSteps() );
				
		}
	}
	
}
