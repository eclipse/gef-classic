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
package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.internal.graphmodel.nested.NestedPane;
import org.eclipse.mylar.zest.core.messages.ZestUIMessages;

/**
 * 
 * Basic figure for creating panes in the nested graph viewer. Can be one of
 * three types: NestedPane.SUPPLIER_PANE, NestedPane.CLIENT_PANE,
 * or NestedPain.MAIN_PANE. The main pane contains the nested graph element that
 * the user is currently focussed on. The supplier pane contains all nodes that
 * have arcs running "from" that node "to" the focus node, or any of its children.
 * The client pane contains all nodes that have arcs running "to" that node "from"
 * the focus node or any of its children.
 * @author Del Myers
 */

//@tag bug(152613-Client-Supplier(fix)) : the basic panes that will contain the nested graphs.
class PaneFigure extends Panel {
	private Label label;
	private Panel clientArea;
	private int type;
	private boolean closed;
	public PaneFigure(int type) {
		super();
		this.type = type;
		clientArea = new Panel();
		if (type != NestedPane.MAIN_PANE) {
			label = new Label();
			label.setBackgroundColor(ColorConstants.darkBlue);
			label.setText(getTextForType(type));
			label.setOpaque(true);
			label.setForegroundColor(ColorConstants.lightGray);
			super.add(label, null, -1);
		}
		clientArea.setBackgroundColor(ColorConstants.white);
		clientArea.setLayoutManager(new FigureGridLayout());
		clientArea.setOpaque(false);
		super.add(clientArea, null, -1);
		setLayoutManager(new FreeformLayout(){
			/* (non-Javadoc)
			 * @see org.eclipse.draw2d.XYLayout#layout(org.eclipse.draw2d.IFigure)
			 */
			public void layout(IFigure parent) {
				Rectangle textbounds = (label != null) ? label.getTextBounds() : new Rectangle(0,0,0,0);
				Rectangle parentBounds = parent.getBounds();
				if (label != null) label.setSize(parent.getSize().width, textbounds.height);
				clientArea.setBounds(new Rectangle(parentBounds.x, parentBounds.y+textbounds.height, parent.getSize().width, parent.getSize().height-textbounds.height));
			}
		});
	}
	/**
	 * @param paneType2
	 * @return
	 */
	private String getTextForType(int paneType) {
		switch(paneType) {
		case NestedPane.CLIENT_PANE: return ZestUIMessages.VIEW_NESTED_CLIENT_PANE;
		case NestedPane.SUPPLIER_PANE: return ZestUIMessages.VIEW_NESTED_SUPPLIER_PANE;
		}
		return "";
	}
	
	public Panel getClientPanel() {
		return clientArea;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#add(org.eclipse.draw2d.IFigure, java.lang.Object, int)
	 */
	public void add(IFigure figure, Object constraint, int index) {
		//add to the client area so that the title is visible.
		clientArea.add(figure, constraint, index);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#remove(org.eclipse.draw2d.IFigure)
	 */
	public void remove(IFigure figure) {
		clientArea.remove(figure);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#removeAll()
	 */
	public void removeAll() {
		clientArea.removeAll();
	}
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Allows the pane to be open or to be closed.
	 * @param closed whether or not the content pane should be closed.
	 */
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	/**
	 * 
	 * @return whether or not the content pane is closed.
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		//returns the size of the label, if one exists, plus the size of the
		//client pane if it is not closed.
		int labelHeight = 0;
		int labelWidth = wHint;
		int clientHeight = 0;
		if (label != null) {
			labelHeight = label.getTextBounds().height;
			labelWidth = label.getTextBounds().width;
		}
		if (!isClosed()) {
			if (hHint >= labelHeight) {
				clientHeight = hHint-labelHeight;
			}
		}
		return super.getPreferredSize(labelWidth, labelHeight+clientHeight);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getClientArea(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public Rectangle getClientArea(Rectangle rect) {
		Rectangle client = getClientPanel().getClientArea();
		rect.x = client.x;
		rect.y = client.y;
		rect.width = client.width;
		rect.height = client.height;
		return rect;
	}
}