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
package org.eclipse.mylar.zest.layouts.constraints;

/**
 * A simple text layout constraint
 * 
 * @author Ian Bull
 * 
 * This is not yet final, please don't use!
 *
 */
public class TextLayoutConstraint implements LayoutConstraint {
	
	public static final String ID = "TextLayoutConstraint";
	
	private String text;
	private int ptSize;
	
	public int getPtSize() {
		return ptSize;
	}
	public void setPtSize(int ptSize) {
		this.ptSize = ptSize;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	// Unique constraint identifier
	public String getConstraintID() {
		return ID;
	}

}
