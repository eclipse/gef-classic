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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * A model object that indirectly references a nested node. It is used to represent
 * a node in times when it should not be nested. For example, when a node is placed
 * in the Connected To/Connected From panes. Connections must be added manually
 * to the proxy node, since it isn't known which of the actual model connections
 * should be represented in the proxy.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix)) : don't nest nodes in the client/supplier panes.
public class NonNestedProxyNode extends GraphModelNode {
	private GraphModelNode proxy;
	
	/**
	 * Proxy nodes are added to the graph model upon creation.
	 * @param proxy
	 */
	NonNestedProxyNode(GraphModelNode proxy) {
		super(proxy.getGraphModel(), proxy.getText(), proxy.getImage(), proxy.getExternalNode());
		this.proxy = proxy;
	}
	
	/**
	 * @return the proxy
	 */
	public GraphModelNode getProxy() {
		return proxy;
	}
	

	
	/**
	 * Returns all proxy connections.
	 * @return all proxy connections.
	 */
	public List getProxyConnections() {
		LinkedList all = new LinkedList();
		all.addAll(getSourceConnections());
		all.addAll(getTargetConnections());
		return all;
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getBackgroundColor()
	 */
	public Color getBackgroundColor() {
		return proxy.getBackgroundColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color c) {
		proxy.setBackgroundColor(c);
		super.setBackgroundColor(c);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#changeBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	protected void changeBackgroundColor(Color c) {
		proxy.changeBackgroundColor(c);
		super.changeBackgroundColor(c);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getBorderWidth()
	 */
	public int getBorderWidth() {
		return proxy.getBorderWidth();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getNodeStyle()
	 */
	public int getNodeStyle() {
		return proxy.getNodeStyle();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getExternalNode()
	 */
	public Object getExternalNode() {
		return proxy.getExternalNode();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#getData()
	 */
	public Object getData() {
		return proxy.getData();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getForegroundColor()
	 */
	public Color getForegroundColor() {
		return proxy.getForegroundColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#setForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setForegroundColor(Color c) {
		proxy.setForegroundColor(c);
		super.setForegroundColor(c);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getHighlightColor()
	 */
	public Color getHighlightColor() {
		return proxy.getHighlightColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#getHighlightAdjacentColor()
	 */
	public Color getHighlightAdjacentColor() {
		return proxy.getHighlightAdjacentColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Item#getImage()
	 */
//	@tag bug(154256-ClientSupplySelect(fix))
	public Image getImage() {
		return proxy.getImage();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Item#getText()
	 */
//	@tag bug(154256-ClientSupplySelect(fix))
	public String getText() {
		return proxy.getText();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#highlight()
	 */
//	@tag bug(154256-ClientSupplySelect(fix))
	public void highlight() {
		proxy.highlight();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#unhighlight()
	 */
	//@tag bug(154256-ClientSupplySelect(fix))
	public void unhighlight() {
		proxy.unhighlight();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode#highlightAdjacent()
	 */
//	@tag bug(154256-ClientSupplySelect(fix))
	public void highlightAdjacent() {
		proxy.highlightAdjacent();
	}
	
	
	
	
}