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
package org.eclipse.mylar.zest.core.internal.graphmodel;



/**
 * A model class that references a connection in the model in order to conseptually
 * duplicate them, without actually duplicating them in the model. Useful for 
 * visual elements. Uses visual information from the proxy.
 * 
 * Unlike proxy nodes, this connection does not forward to the proxy. It simply
 * initializes its state based on the proxy node.
 * @author Del Myers
 *
 */

//@tag zest(bug(153466-NoNestedClientSupply(fix))) : will be used to create multiple visual connections.
//@tag zest(bug(154259-Abstraction(fix)))
public class ProxyConnection extends GraphModelConnection {
	private IGraphModelConnection proxy;

	/**
	 * Creates a new proxy connection with its visuals based on the given connection.
	 */
	protected ProxyConnection(IGraphModelNode source, IGraphModelNode dest, IGraphModelConnection proxy) {
		super(proxy.getGraphModel(), proxy.getExternalConnection(), source, dest);
		this.proxy = proxy;
		init();
	}

	private final void init() {
		setConnectionStyle(proxy.getConnectionStyle());
		setCurveDepth(proxy.getCurveDepth());
		setEndAngle(proxy.getEndAngle());
		setEndLength(proxy.getEndLength());
		setFont(proxy.getFont());
		setHighlightColor(proxy.getHighlightColor());
		setImage(proxy.getImage());
		setLineColor(proxy.getLineColor());
		setLineStyle(proxy.getLineStyle());
		setLineWidth(proxy.getLineWidth());
		setStartAngle(proxy.getStartAngle());
		setStartLength(proxy.getStartLength());
		setText(proxy.getText());
	}
	
	public IGraphModelConnection getProxy() {
		return proxy;
	}

}
