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


/**
 * @author Ian bull
 */
public class NestedPane {

	NestedGraphModel nestedGraphModel = null;
	private int paneType = 0;
	public NestedPane( int paneType ) {
		this.paneType = paneType;
	}
	
	public void setModel( NestedGraphModel model ) {
		this.nestedGraphModel = model;
	}
	
	public NestedGraphModel getModel() {
		return this.nestedGraphModel;
	}
	
	public int getPaneType() {
		return this.paneType;
	}
	
}
