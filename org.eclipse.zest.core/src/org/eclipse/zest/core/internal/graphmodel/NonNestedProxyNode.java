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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * A model object that indirectly references a node. It is used at times when a
 * graph model node needs to be virtually duplicated on the display, but not in
 * the actual model.
 * 
 * Note: almost all calls are sent directly to the proxy. This means, color changes,
 * font changes, etc. Will be forwarded to the proxy. Calls that are not forwarded
 * are noted in the javadocs.
 * @author Del Myers
 *
 */
//@tag bug(153466-NoNestedClientSupply(fix)) : don't nest nodes in the client/supplier panes.
//@tag bug(154259-Abstraction(fix)) : implement IGraphModelNode. 
public class NonNestedProxyNode extends GraphItem implements IGraphModelNode {
	private IGraphModelNode proxy;
	private ArrayList sourceConnections;
	private ArrayList targetConnections;
	private ProxyListener proxyListener;
	private Point currentLocation;
	private Dimension currentSize;
	private boolean preferredLocation;
	private boolean selected;
	private boolean highlighted;
	private Object internalNode;
	private Dimension labelSize;
	
	private class ProxyListener implements PropertyChangeListener {
		/* (non-Javadoc)
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			String property = evt.getPropertyName();
			if (
				LOCATION_PROP.equals(property) || 
				SIZE_PROP.equals(property) || 
				SOURCE_CONNECTIONS_PROP.equals(property) ||
				TARGET_CONNECTIONS_PROP.equals(property) ||
				HIGHLIGHT_PROP.equals(property)
			) return;
			firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());			
		}
	}

	/**
	 * Creates a new node that references the given proxy. Initial display information
	 * will be taken from the proxy.
	 */
	protected NonNestedProxyNode(IGraphModelNode proxy) {
		super(proxy.getGraphModel());
		this.proxy = proxy;
		setData(proxy.getExternalNode());
		proxyListener = new ProxyListener();
		sourceConnections = new ArrayList();
		targetConnections = new ArrayList();
		currentLocation = proxy.getLocation().getCopy();
		currentSize = proxy.getSize().getCopy();
		this.preferredLocation = proxy.hasPreferredLocation();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#getData()
	 */
	public Object getData() {
		return getExternalNode();
	}
	
	/**
	 * Connects this node to its proxy to start listening for property changes on
	 * the proxy. Any overriders must call super on this methd.
	 *
	 */
	protected void activate() {
		proxy.addPropertyChangeListener(proxyListener);
	}
	
	/**
	 * Disconnects this node from its proxy and stops listening for changes.
	 * Any overriders must call super on this methd.
	 *
	 */
	protected void deactivate() {
		proxy.removePropertyChangeListener(proxyListener);
	}
	

	/**
	 * Adds the given connection to the list of connections. This is not forwarded
	 * to the proxy. All connections must be added manually to this node
	 * because it is impossible to infer from the model which connections should
	 * be copied from the proxy.
	 * @param connection
	 * @param source true if the given connection should be added as a source.
	 */
	public void addConnection(IGraphModelConnection connection, boolean source) {
		if (connection != null) {
			if (source) {
				if (connection.getSource() == this) {
					sourceConnections.add(connection);
					firePropertyChange(SOURCE_CONNECTIONS_PROP, null, connection);
				}
			} else {
				if (connection.getDestination() == this) {
					targetConnections.add(connection);
					firePropertyChange(TARGET_CONNECTIONS_PROP, null, connection);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#changeBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void changeBackgroundColor(Color c) {
		proxy.changeBackgroundColor(c);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getBackgroundColor()
	 */
	public Color getBackgroundColor() {
		return proxy.getBackgroundColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getBorderColor()
	 */
	public Color getBorderColor() {
		return proxy.getBorderColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getBorderWidth()
	 */
	public int getBorderWidth() {
		return proxy.getBorderWidth();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getExternalNode()
	 */
	public Object getExternalNode() {
		return proxy.getExternalNode();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getFont()
	 */
	public Font getFont() {
		return proxy.getFont();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getForegroundColor()
	 */
	public Color getForegroundColor() {
		return proxy.getForegroundColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getGraphModel()
	 */
	public GraphModel getGraphModel() {
		return proxy.getGraphModel();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getHighlightAdjacentColor()
	 */
	public Color getHighlightAdjacentColor() {
		return proxy.getHighlightAdjacentColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getHighlightColor()
	 */
	public Color getHighlightColor() {
		return proxy.getHighlightColor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getImage()
	 */
	public Image getImage() {
		return proxy.getImage();
	}

	/**
	 * Not forwarded to the proxy.
	 */
	public Point getLocation() {
		return currentLocation.getCopy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getNodeStyle()
	 */
	public int getNodeStyle() {
		return proxy.getNodeStyle();
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getSize()
	 */
	public Dimension getSize() {
		return currentSize.getCopy();
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getSourceConnections()
	 */
	public List getSourceConnections() {
		return sourceConnections;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getTargetConnections()
	 */
	public List getTargetConnections() {
		return targetConnections;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getText()
	 */
	public String getText() {
		return proxy.getText();
	}

	/**
	 * Highlights the node changing the background color and border color.
	 * The source and destination connections are also highlighted,
	 * and the adjacent nodes are highlighted too in a different color.
	 */
	public void highlight() {
		if (isHighlighted()) return;
		changeBackgroundColor(getHighlightColor());
		// highlight the adjacent nodes
		for (Iterator iter = sourceConnections.iterator(); iter.hasNext();) {
			IGraphModelConnection conn = (IGraphModelConnection)iter.next();
			conn.highlight();
			conn.getDestination().highlightAdjacent();
		}
		for (Iterator iter = targetConnections.iterator(); iter.hasNext();) {
			IGraphModelConnection conn = (IGraphModelConnection)iter.next();
			conn.highlight();
			conn.getSource().highlightAdjacent();
		}
		highlighted = true;
		firePropertyChange(HIGHLIGHT_PROP, Boolean.FALSE, Boolean.TRUE);

	}
	
	public boolean isHighlighted() {
		return highlighted;
	}
	
	/**
	 * Restores the nodes original background color and border width.
	 */
	public void unhighlight() {
		if (!isHighlighted()) return;
		changeBackgroundColor(getUnhiglightColor());
		// unhighlight the adjacent edges
		for (Iterator iter = sourceConnections.iterator(); iter.hasNext();) {
			IGraphModelConnection conn = (IGraphModelConnection)iter.next();
			conn.unhighlight();
			conn.getDestination().unhighlight();
		}
		for (Iterator iter = targetConnections.iterator(); iter.hasNext();) {
			IGraphModelConnection conn = (IGraphModelConnection)iter.next();
			conn.unhighlight();
			conn.getSource().unhighlight();
		}
		highlighted = false;
		firePropertyChange(HIGHLIGHT_PROP, Boolean.TRUE, Boolean.FALSE);
	
	}
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#highlightAdjacent()
	 */
	public void highlightAdjacent() {
		proxy.highlightAdjacent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#isHighlightAdjacentNodes()
	 */
	public boolean isHighlightAdjacentNodes() {
		return proxy.isHighlightAdjacentNodes();
	}

	/**
	 * Not forwarded to the proxy
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#removeConnection(org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelConnection)
	 */
	public boolean removeConnection(IGraphModelConnection connection) {
		boolean removed = false;
		if (connection != null) {
			if (connection.getSource() == this) {
				removed = sourceConnections.remove(connection);
				if (removed) {
					firePropertyChange(SOURCE_CONNECTIONS_PROP, null, connection);
				}
			}
			else if (connection.getDestination() == this) {
				removed = targetConnections.remove(connection);
				if (removed) {
					firePropertyChange(TARGET_CONNECTIONS_PROP, null, connection);
				}
			}
		}
		return removed;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color c) {
		proxy.setBackgroundColor(c);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setBorderColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBorderColor(Color c) {
		proxy.setBorderColor(c);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setBorderHighlightColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBorderHighlightColor(Color c) {
		proxy.setBorderHighlightColor(c);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setBorderWidth(int)
	 */
	public void setBorderWidth(int width) {
		proxy.setBorderWidth(width);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		proxy.setFont(f);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setForegroundColor(Color c) {
		proxy.setForegroundColor(c);
	}

	/**
	 * Not forarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setHasPreferredLocation(boolean)
	 */
	public void setHasPreferredLocation(boolean hasPreferredLocation) {
		this.preferredLocation = hasPreferredLocation;
	}
	
	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#hasPreferredLocation()
	 */
	public boolean hasPreferredLocation() {
		return preferredLocation;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setHighlightAdjacentColor(org.eclipse.swt.graphics.Color)
	 */
	public void setHighlightAdjacentColor(Color c) {
		proxy.setHighlightAdjacentColor(c);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setHighlightAdjacentNodes(boolean)
	 */
	public void setHighlightAdjacentNodes(boolean highlightAdjacent) {
		proxy.setHighlightAdjacentNodes(highlightAdjacent);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setHighlightColor(org.eclipse.swt.graphics.Color)
	 */
	public void setHighlightColor(Color c) {
		proxy.setHighlightColor(c);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setImage(org.eclipse.swt.graphics.Image)
	 */
	public void setImage(Image image) {
		proxy.setImage(image);
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setLocation(double, double)
	 */
	public void setLocation( double x, double y ) {
		Point oldPoint = getLocation();
		currentLocation.setLocation((int)x, (int)y);
		firePropertyChange(LOCATION_PROP, oldPoint, currentLocation);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setNodeStyle(int)
	 */
	public void setNodeStyle(int style) {
		proxy.setNodeStyle(style);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setPreferredLocation(double, double)
	 */
	public void setPreferredLocation(double x, double y) {
		setLocation(x,y);
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setSelected(boolean)
	 */
	public void setSelected( boolean selected ) {
		if (selected = isSelected()) return;
		if (selected) {
			highlight();
		} else {
			unhighlight();
		}
		this.selected = selected;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setText(java.lang.String)
	 */
	public void setText(String text) {
		proxy.setText(text);
	}

	
	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#getHeightInLayout()
	 */
	public double getHeightInLayout() {
		return getSize().height;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#getLayoutInformation()
	 */
	public Object getLayoutInformation() {
		return internalNode;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#getWidthInLayout()
	 */
	public double getWidthInLayout() {
		return getSize().width;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#getXInLayout()
	 */
	public double getXInLayout() {
		return getLocation().x;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#getYInLayout()
	 */
	public double getYInLayout() {
		return getLocation().y;
	}

	/**
	 * Does nothing.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#populateLayoutConstraint(org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint)
	 */
	public void populateLayoutConstraint(LayoutConstraint constraint) {
	
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#setLayoutInformation(java.lang.Object)
	 */
	public void setLayoutInformation(Object internalEntity) {
		this.internalNode = internalEntity;
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#setLocationInLayout(double, double)
	 */
	public void setLocationInLayout(double x, double y) {
		setLocation(x, y);
	}

	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.layouts.LayoutEntity#setSizeInLayout(double, double)
	 */
	public void setSizeInLayout(double width, double height) {
		setSize(width, height);
	}
	
	/**
	 * Not forwarded to the proxy.
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setSize(double, double)
	 */
	public void setSize(double width, double height) {
		Object old = getSize();
		currentSize.setSize(new Dimension((int)width, (int)height));
		firePropertyChange(SIZE_PROP, old, currentSize);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object otherNode) {
		int rv = 0;
		if (otherNode instanceof IGraphModelNode) {
			IGraphModelNode node = (IGraphModelNode)otherNode;
			if (this.getText() != null) {
				rv = this.getText().compareTo(node.getText());
			}
		}
		return rv;
	}
	
	
	public IGraphModelNode getProxy() {
		return proxy;
	}
	
	/**
	 * Returns the extent of the text and the image with some padding.
	 * @return Dimension the minimum size needed to display the text and the image
	 */
	public Dimension calculateMinimumLabelSize() {
		if (labelSize == null) {
			Dimension text = calculateTextExtents();
			Dimension icon = calculateImageExtents();
			labelSize = new Dimension(text.width + icon.width, Math.max(text.height, icon.height));
			labelSize.expand(12, 6);
		}
		return labelSize;
	}
	
	private Dimension calculateTextExtents() {
		Dimension dim = new Dimension(0, 0);
		String text = getText();
		if (text != null) {
			Font font = getFont();
			if (font == null) {
				font = JFaceResources.getDefaultFont();
			}
			dim.setSize(FigureUtilities.getTextExtents(text + "  ", font));
		}
		return dim;
	}
	
	private Dimension calculateImageExtents() {
		Dimension dim = new Dimension(0, 0);
		Image image = getImage();
		if (image != null) {
			dim.setSize(new Dimension(image.getBounds().width + 4, image.getBounds().height));
		}
		return dim;
	}
	
	/**
	 * Gets the minimum size for this node.  This is the minimum size of the label (text & icon)
	 * @return Dimension
	 */
	public Dimension calculateMinimumSize() {
		return calculateMinimumLabelSize();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getUnhiglightColor()
	 */
	public Color getUnhiglightColor() {
		return proxy.getUnhiglightColor();
	}

	
}