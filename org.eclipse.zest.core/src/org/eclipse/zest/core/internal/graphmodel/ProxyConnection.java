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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;


/**
 * A model class that references a connection in the model in order to conseptually
 * duplicate them, without actually duplicating them in the model. Useful for 
 * visual elements. Uses visual information from the proxy.
 * @author Del Myers
 *
 */

//@tag bug(153466-NoNestedClientSupply(fix)) : will be used to create multiple visual connections.
public class ProxyConnection extends GraphModelConnection {
	private GraphModelConnection proxy;

	/**
	 * Proxies will be added to the graph model upon creation.
	 * @param source
	 * @param dest
	 * @param proxy
	 */
	ProxyConnection(GraphModelNode source, GraphModelNode dest, GraphModelConnection proxy) {
		super(proxy.getGraphModel(), proxy.getData(), source, dest, proxy.isBidirectionalInLayout(), proxy.getWeightInLayout());
		this.proxy = proxy;
	}
	
	/**
	 * @return the proxy
	 */
	public GraphModelConnection getProxy() {
		return proxy;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getConnectionStyle()
	 */
	public int getConnectionStyle() {
		if (getProxy()!=null) return getProxy().getConnectionStyle();
		return super.getConnectionStyle();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getCurveDepth()
	 */
	public int getCurveDepth() {
		if (getProxy()!=null) return getProxy().getCurveDepth();
		return super.getCurveDepth();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getColorFromWeight()
	 */
	protected Color getColorFromWeight() {
		if (getProxy()!=null) return getProxy().getColorFromWeight();
		return super.getColorFromWeight();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getFont()
	 */
	public Font getFont() {
		if (getProxy()!=null) return getProxy().getFont();
		return super.getFont();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getExternalConnection()
	 */
	public Object getExternalConnection() {
		if (getProxy()!=null) return getProxy().getExternalConnection();
		return super.getExternalConnection();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getGraphModel()
	 */
	public GraphModel getGraphModel() {
		if (getProxy()!=null) return getProxy().getGraphModel();
		return super.getGraphModel();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getHighlightColor()
	 */
	public Color getHighlightColor() {
		if (getProxy()!=null) return getProxy().getHighlightColor();
		return super.getHighlightColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getLineColor()
	 */
	public Color getLineColor() {
		if (getProxy()!=null) return getProxy().getLineColor();
		return super.getLineColor();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getLineStyle()
	 */
	public int getLineStyle() {
		if (getProxy()!=null) return getProxy().getLineStyle();
		return super.getLineStyle();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getLineWidth()
	 */
	public int getLineWidth() {
		if (getProxy()!=null) return getProxy().getLineWidth();
		return super.getLineWidth();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#getLineWidthFromWeight()
	 */
	protected int getLineWidthFromWeight() {
		if (getProxy()!=null) return getProxy().getLineWidthFromWeight();
		return super.getLineWidthFromWeight();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Item#getImage()
	 */
	public Image getImage() {
		if (getProxy()!=null) return getProxy().getImage();
		return super.getImage();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#getStyle()
	 */
	public int getStyle() {
		if (getProxy()!=null) return getProxy().getStyle();
		return super.getStyle();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Item#getText()
	 */
	public String getText() {
		if (getProxy()!=null) return getProxy().getText();
		return super.getText();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelConnection#isBidirectionalInLayout()
	 */
	public boolean isBidirectionalInLayout() {
		if (getProxy()!=null) return getProxy().isBidirectionalInLayout();
		return super.isBidirectionalInLayout();
	}
		
}
