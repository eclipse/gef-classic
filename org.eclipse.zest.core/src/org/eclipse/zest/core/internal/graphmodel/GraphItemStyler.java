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

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.ZestException;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.IConnectionStyleBezierExtension;
import org.eclipse.mylar.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.mylar.zest.core.viewers.IEntityConnectionStyleBezierExtension;
import org.eclipse.mylar.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.mylar.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.services.IDisposable;

/**
 * Helper class used to style graph elements based on graph element stylers.
 * @author Del Myers
 *
 */
//@tag bug(151327-Styles) : created to help resolve this bug
public class GraphItemStyler {
	public static void styleItem(IGraphItem item, final IBaseLabelProvider labelProvider) {
		//provided for label providers that must be disposed.
		if (labelProvider instanceof IDisposable && item instanceof Widget) {
			((Widget)item).addDisposeListener(new DisposeListener(){
				public void widgetDisposed(DisposeEvent e) {
					((IDisposable)labelProvider).dispose();						
				}
			});
		}
		
		if (item instanceof IGraphModelNode) {
			IGraphModelNode node = (IGraphModelNode)item;
			//set defaults.
			if (node.getGraphModel().getNodeStyle() != ZestStyles.NONE) {
				node.setNodeStyle(node.getGraphModel().getNodeStyle());
			} else {
				node.setNodeStyle(IZestGraphDefaults.NODE_STYLE);
			}
			Object entity= node.getExternalNode();
			if (labelProvider instanceof IEntityStyleProvider) {
				styleNode(node, (IEntityStyleProvider)labelProvider);
			}
			if (labelProvider instanceof IColorProvider) {
				IColorProvider colorProvider = (IColorProvider) labelProvider;
				node.setForegroundColor(colorProvider.getForeground(entity));
				node.setBackgroundColor(colorProvider.getBackground(entity));
			}
			if (labelProvider instanceof IFontProvider) {
				IFontProvider fontProvider = (IFontProvider) labelProvider;
				node.setFont(fontProvider.getFont(entity));
			}
			if (labelProvider instanceof ILabelProvider) {
				String text = ((ILabelProvider)labelProvider).getText(node.getExternalNode());
				node.setText((text != null) ? text : "");
				node.setImage(((ILabelProvider)labelProvider).getImage(node.getExternalNode()));
			}
		} else if (item instanceof IGraphModelConnection) {
			IGraphModelConnection conn = (IGraphModelConnection) item;
		
			//set defaults
			if (conn.getGraphModel().getConnectionStyle() != ZestStyles.NONE) {
				int s = conn.getGraphModel().getConnectionStyle();
				conn.setConnectionStyle(s);
			} else {
				conn.setConnectionStyle(IZestGraphDefaults.CONNECTION_STYLE);
			}
			if (labelProvider instanceof ILabelProvider) {
				String text = ((ILabelProvider)labelProvider).getText(conn.getExternalConnection());
				conn.setText((text != null) ? text : "");
				conn.setImage(((ILabelProvider)labelProvider).getImage(conn.getExternalConnection()));
			}
			if (labelProvider instanceof IEntityConnectionStyleProvider) {
				styleEntityConnection(conn, (IEntityConnectionStyleProvider)labelProvider);
			} else if (labelProvider instanceof IConnectionStyleProvider) {
				styleConnection(conn, (IConnectionStyleProvider)labelProvider);
			}
			int swt = getLineStyleForZestStyle(conn.getConnectionStyle());
			conn.setLineStyle(swt);
			
		}
	}
	
	/**
	 * @param conn
	 * @param provider
	 */
	private static void styleConnection(IGraphModelConnection conn, IConnectionStyleProvider provider) {
		Object rel = conn.getExternalConnection();
		Color c;
		int style = provider.getConnectionStyle(rel);
		if (!ZestStyles.validateConnectionStyle(style)) ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		if (style != ZestStyles.NONE) {
			conn.setConnectionStyle(style);
		}
//		@tag bug(152530-Bezier(fix))
		if (ZestStyles.checkStyle(conn.getConnectionStyle(), ZestStyles.CONNECTIONS_BEZIER) &&
			provider instanceof IConnectionStyleBezierExtension) {
			IConnectionStyleBezierExtension bezier = (IConnectionStyleBezierExtension)provider;
			double d;
			if (!Double.isNaN((d = bezier.getStartAngle(rel)))) conn.setStartAngle(d);
			if (!Double.isNaN((d = bezier.getEndAngle(rel)))) conn.setEndAngle(d);
			if (!Double.isNaN((d = bezier.getStartDistance(rel)))) conn.setStartLength(d);
			if (!Double.isNaN((d = bezier.getEndDistance(rel)))) conn.setEndLength(d);
		}
		if ((c = provider.getHighlightColor(rel)) != null) conn.setHighlightColor(c);
		if ((c = provider.getColor(rel)) != null) conn.setLineColor(c);
		int w = -1;
		if ((w = provider.getLineWidth(rel)) >= 0) conn.setLineWidth(w);
	}

	/**
	 * @param conn
	 * @param provider
	 */
	private static void styleEntityConnection(IGraphModelConnection conn, IEntityConnectionStyleProvider provider) {
		Object src = conn.getSource().getExternalNode();
		Object dest = conn.getDestination().getExternalNode();
		Color c;
		int style = provider.getConnectionStyle(src, dest);
		if (!ZestStyles.validateConnectionStyle(style)) ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		if (style != ZestStyles.NONE) {
			conn.setConnectionStyle(style);
		}
		//@tag bug(152530-Bezier(fix))
		if (ZestStyles.checkStyle(conn.getConnectionStyle(), ZestStyles.CONNECTIONS_BEZIER) &&
				provider instanceof IEntityConnectionStyleBezierExtension) {
				IEntityConnectionStyleBezierExtension bezier = 
					(IEntityConnectionStyleBezierExtension)provider;
				double d;
				if (!Double.isNaN((d = bezier.getStartAngle(src, dest)))) conn.setStartAngle(d);
				if (!Double.isNaN((d = bezier.getEndAngle(src, dest)))) conn.setEndAngle(d);
				if (!Double.isNaN((d = bezier.getStartDistance(src, dest)))) conn.setStartLength(d);
				if (!Double.isNaN((d = bezier.getEndDistance(src, dest)))) conn.setEndLength(d);
			}
		if ((c = provider.getColor(src, dest))!=null) conn.setLineColor(c);
		if ((c = provider.getHighlightColor(src, dest)) != null) conn.setHighlightColor(c);
		int w = -1;
		if ((w = provider.getLineWidth(src, dest)) >= 0) conn.setLineWidth(w);
	}

	/**
	 * Styles the given node according to the properties in the style provider.
	 * @param node the graph element to style.
	 * @param data the element that is being styled.
	 * @param provider the style provier.
	 */
	//@tag bug(151327-Styles) : resolution
	private static void styleNode(IGraphModelNode node, IEntityStyleProvider provider) {
		Object entity = node.getExternalNode();
		node.setHighlightAdjacentNodes(provider.highlightAdjacentEntities(entity));
		if (provider.highlightAdjacentEntities(entity)) {
			Color c = provider.getAdjacentEntityHighlightColor(entity);
			if (c != null) node.setHighlightAdjacentColor(c);
		}
		Color c;
		int width = -1;
		if ((c = provider.getBorderColor(entity)) != null) node.setBorderColor(c);
		if ((c = provider.getBorderHighlightColor(entity)) != null) node.setBorderHighlightColor(c);
		if ((c = provider.getHighlightColor(entity)) != null) node.setHighlightColor(c);
		if ((width = provider.getBorderWidth(entity)) >= 0) node.setBorderWidth(width);
		
	}
	/**
	 * Returns the SWT line style for the given zest connection style.
	 *
	 */
	public static int getLineStyleForZestStyle(int style){
		int lineStyles = 
			ZestStyles.CONNECTIONS_DASH_DOT |
			ZestStyles.CONNECTIONS_DASH |
			ZestStyles.CONNECTIONS_DOT |
			ZestStyles.CONNECTIONS_SOLID;
		style = style & lineStyles;
		if (style == 0) {
			style = ZestStyles.CONNECTIONS_SOLID;
		}
		switch (style) {
			case ZestStyles.CONNECTIONS_DASH_DOT:
				return SWT.LINE_DASHDOT;
			case ZestStyles.CONNECTIONS_DASH:
				return SWT.LINE_DASH;
			case ZestStyles.CONNECTIONS_DOT:
				return SWT.LINE_DOT;
		}
		return SWT.LINE_SOLID;
	}
}
