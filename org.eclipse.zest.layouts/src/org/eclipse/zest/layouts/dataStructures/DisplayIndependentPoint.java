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

/**
 * This is a point that isn't dependent on awt, swt, or any other library,
 * except layout.
 * 
 * @author Casey Best
 */
public class DisplayIndependentPoint {
	public double x, y;
	
	
	public boolean equals( Object o ) {
		DisplayIndependentPoint that = (DisplayIndependentPoint) o;
		if ( this.x == that.x && this.y == that.y ) return true;
		else return false;
	}
	
	public DisplayIndependentPoint (double x, double y) {
		this.x = x;
		this.y = y;
	}

	public DisplayIndependentPoint (DisplayIndependentPoint point) {
		this.x = point.x;
		this.y = point.y;
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
