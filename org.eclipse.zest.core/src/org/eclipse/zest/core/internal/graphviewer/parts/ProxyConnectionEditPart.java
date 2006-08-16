/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.graphviewer.parts;

import org.eclipse.mylar.zest.core.internal.graphmodel.ProxyConnection;

/**
 * An edit part for creating proxy connections.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix))
public final class ProxyConnectionEditPart extends GraphConnectionEditPart {

	/**
	 * 
	 */
	public ProxyConnectionEditPart() {
		super();
	}
		
		
	public ProxyConnection getProxyModel() {
		return (ProxyConnection)getModel();
	}

}
