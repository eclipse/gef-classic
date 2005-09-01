/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 	   Keith Pilson - initial API and implementation
 *     Ian Bull     - updated and modified
 *******************************************************************************/
package org.eclipse.mylar.zest.layouts.algorithms;

/**
 * @version 2.0
 * @author Casey Best and Rob Lintern
 */
public class HorizontalLayoutAlgorithm extends GridLayoutAlgorithm {

	
	public HorizontalLayoutAlgorithm(int styles) {
		super(styles);
	}

	/**
	 * Calculates and returns an array containing the number of columns, followed by the number of rows
	 */
	protected int[] calculateNumberOfRowsAndCols (int numChildren, double boundX, double boundY, double boundWidth, double boundHeight) {
		int rows = 1;
		int cols = numChildren;
		int[] result = {cols, rows};
		return result;
	}
	
	boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
		if ( asynchronous && continueous ) return false;
		else if ( asynchronous && !continueous ) return true;
		else if ( !asynchronous && continueous ) return false;
		else if ( !asynchronous && !continueous ) return true;
		
		return false;
	}
}
