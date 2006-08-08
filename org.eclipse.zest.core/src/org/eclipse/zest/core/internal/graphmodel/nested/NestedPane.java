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
import java.util.Collections;
import java.util.List;


/**
 * @author Ian bull
 */
//@tag bug(152613-Client-Supplier(fix))
public class NestedPane {

	public static final int SUPPLIER_PANE = 0;
	public static final int MAIN_PANE = 1;
	public static final int CLIENT_PANE = 2;
	
	NestedGraphModel nestedGraphModel = null;
	private int paneType = 0;
	List children = null;
	public NestedPane( int paneType ) {
		this.paneType = paneType;
		children = new ArrayList();
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
	
	public void addNode(NestedGraphModelNode node) {
		children.add(node);
	}
	
	public List getChildren() {
		switch (getPaneType()) {
		case MAIN_PANE:
			return nestedGraphModel.getNodes();
		case CLIENT_PANE:
			return nestedGraphModel.getCurrentNode().getNodesConnectedTo();
		case SUPPLIER_PANE:
			return nestedGraphModel.getCurrentNode().getNodesConnectedFrom();
		}
		return Collections.EMPTY_LIST;
	}

}
