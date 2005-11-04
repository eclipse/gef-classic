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
package org.eclipse.mylar.zest.layouts;


/**
 * This represents a single relationship, providing the layout algorithms with 
 * a common interface to run on.
 * 
 * @author Casey Best
 * @author Chris Callendar
 */
public interface LayoutRelationship {
	/**
	 * Gets the sourceEntity of this SimpleRelation whether the relation is
	 * exchangeable or not.
	 * @return The sourceEntity.
	 */
	public LayoutEntity getSourceInLayout();

	/**
	 * Gets the destinationEntity of this SimpleRelation whether the relation is
	 * exchangeable or not.
	 * @return The destinationEntity of this SimpleRelation.
	 */
	public LayoutEntity getDestinationInLayout();

	/**
	 * If bidirectional, the direction of the relationship doesn't matter.  Switching the source and destination should make no difference.
	 * If not bidirectional, layout algorithms need to take into account the direction of the relationship.  The direction is based on the
	 * source and destination entities.
	 */
	public boolean isBidirectionalInLayout();

	/**
	 * Some layout algorithms place entities based on the 'weight' of their relationships.
	 * The weight can represent anything.  It could be based on the size of the source and destination
	 * entities.  It could also be based on the type of relationship.
	 */
	public void setWeightInLayout(double weight);

	/**
	 * Returns the weight of the relationship
	 */
	public double getWeightInLayout();

	/**
	 * Sets the internal relationship object.
	 * @param layoutInformation
	 */
	public void setLayoutInformation(Object layoutInformation);
	
	/**
	 * Returns the internal relationship object.
	 * @return Object
	 */
	public Object getLayoutInformation();
	
}
