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

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.mylar.zest.core.ZestException;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.mylar.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.services.IDisposable;

/**
 * Helper class used to style graph elements based on graph element stylers.
 * @author Del Myers
 *
 */
//@tag bug(151327-Styles) : created to help resolve this bug
class GraphItemStyler {
	public static void styleItem(GraphItem item, final ILabelProvider labelProvider) {
		//provided for label providers that must be disposed.
		if (labelProvider instanceof IDisposable) {
			item.addDisposeListener(new DisposeListener(){
				public void widgetDisposed(DisposeEvent e) {
					((IDisposable)labelProvider).dispose();						
				}
			});
		}
	
		if (item instanceof GraphModelNode) {
			GraphModelNode node = (GraphModelNode)item;
			Object entity= node.getData();
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
		} else if (item instanceof GraphModelConnection) {
			GraphModelConnection conn = (GraphModelConnection) item;
			if (labelProvider instanceof IEntityConnectionStyleProvider) {
				styleEntityConnection(conn, (IEntityConnectionStyleProvider)labelProvider);
			}
		}
	}
	
	/**
	 * @param conn
	 * @param provider
	 */
	private static void styleEntityConnection(GraphModelConnection conn, IEntityConnectionStyleProvider provider) {
		Object src = conn.getSource().getData();
		Object dest = conn.getDestination().getData();
		Color c;
		int style = provider.getConnectionStyle(src, dest);
		if (!ZestStyles.validateConnectionStyle(style)) ZestPlugin.error(ZestException.ERROR_INVALID_STYLE);
		if (style > ZestStyles.NONE) {
			conn.setConnectionStyle(style);
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
	private static void styleNode(GraphModelNode node, IEntityStyleProvider provider) {
		Object entity = node.getData();
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
}
