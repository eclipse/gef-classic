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

import java.util.HashMap;

import org.eclipse.mylar.zest.layouts.LayoutEntity;

/**
 * @author irbull
 */
public class InternalNode implements Comparable {
	
	private LayoutEntity entity = null;
	private HashMap attributeMap = new HashMap();
	
	public InternalNode( LayoutEntity entity ) {
		this.entity = entity;
		this.entity.setLayoutInformation(this);
	}
	
	public LayoutEntity getLayoutEntity() {
		return this.entity;
	}
	
	public double getPreferredX() {
		return entity.getXInLayout();
	}
	
	public double getPreferredY() {
		return entity.getYInLayout();
	}
	
	public boolean hasPreferredLocation() {
		return entity.hasPreferredLocation();
	}
	
	double dx, dy;
	public void setDx( double x ) {
		this.dx = x;
	}
	public void setDy( double y ) {
		this.dy = y;
	}
	public double getDx() {
		return this.dx;
	}
	public double getDy() {
		return this.dy;
	}	
	
	public double getX() {
		return entity.getXInLayout();
	}
	public double getY() {
		return entity.getYInLayout();
	}
		
	public void setLocation( double x, double y ) {
		entity.setLocationInLayout( x, y );
	}
	public void setSize( double width, double height ) {
		entity.setSizeInLayout( width, height );
	}
	
	double normalizedX      = 0.0;
	double normalizedY      = 0.0;
	double normalizedWidth  = 0.0;
	double normalizedHeight = 0.0;
	
	public void setInternalLocation( double x, double y ) {
		//entity.setLocationInLayout(x,y);
		
		normalizedX = x;
		normalizedY = y;
		
	}
	
	public DisplayIndependentPoint getInternalLocation() {
		return new DisplayIndependentPoint(getInternalX(),getInternalY());
	}
	
	public void setInternalSize( double width, double height ) {
		normalizedWidth = width;
		normalizedHeight = height;
	}

	public double getInternalX() {
		//return entity.getXInLayout();
		return normalizedX;
	}
	public double getInternalY() {
		//return entity.getYInLayout();
		return normalizedY;
	}
	public double getInternalWidth() {
		return normalizedWidth;
	}
	public double getInternalHeight() {
		return normalizedHeight;
	}

	
	/**
	 * An algorithm may require a place to store information.  Use this structure for that purpose.
	 */
	public void setAttributeInLayout (Object attribute, Object value) {
		attributeMap.put(attribute, value);
	}
	
	/**
	 * An algorithm may require a place to store information.  Use this structure for that purpose.
	 */
	public Object getAttributeInLayout (Object attribute) {
		return attributeMap.get( attribute );
	}

	
	//TODO: Fix all these preferred stuff!!!!! NOW!
	
	public boolean hasPreferredWidth () {
		return false;
	    //return enity.getAttributeInLayout(LayoutEntity.ATTR_PREFERRED_WIDTH) != null;
	}
	
	public double getPreferredWidth () {
		return 0.0;
//	    if (hasPreferredWidth()) {
//	        return ((Double)entity.getAttributeInLayout(LayoutEntity.ATTR_PREFERRED_WIDTH)).doubleValue();
//	    } else {
//	        return 10.0;
//	    }
	}
	
	public boolean hasPreferredHeight () {
		return false;
	//    return entity.getAttributeInLayout(LayoutEntity.ATTR_PREFERRED_HEIGHT) != null;
	}
	
	public double getPreferredHeight () {
		return 0.0;
//	    if (hasPreferredHeight()) {
//	        return ((Double)entity.getAttributeInLayout(LayoutEntity.ATTR_PREFERRED_HEIGHT)).doubleValue();
//	    } else {
//	        return 10.0;
//	    }
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (entity != null ? entity.toString() : "");
	}
	
}
