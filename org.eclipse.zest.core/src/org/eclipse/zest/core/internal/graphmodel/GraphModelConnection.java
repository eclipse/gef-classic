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
package org.eclipse.mylar.zest.core.internal.graphmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.eclipse.draw2d.Graphics;
import org.eclipse.mylar.zest.core.IZestColorConstants;
import org.eclipse.mylar.zest.core.ZestPlugin;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.layouts.LayoutBendPoint;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;


/**
 * This is the graph connection model which stores the source and destination nodes and the properties
 * of this connection (color, line width etc).
 * 
 * @author Chris Callendar
 */
public class GraphModelConnection extends GraphItem implements IGraphModelConnection, LayoutRelationship {

	private Font font;
	private IGraphModelNode sourceNode;
	private IGraphModelNode destinationNode;

	private  double weight;
	private Color color;
	private Color highlightColor;
	private Color foreground;
	private int lineWidth;
	private int lineStyle;
	private HashMap attributes;
	private boolean isConnected;
	private GraphModel graphModel;
	
	private Object internalConnection;
	private int connectionStyle;
	private int curveDepth;
	

	
	/**
	 * For bezier curves: angle between the start point, and the line.
	 * This may be a hint only. Future implementations of graph viewers may
	 * adjust the actual visual representation based on the look of the graph.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	private double startAngle;
	/**
	 * For bezier curves: angle between the end point and the line. This may
	 * be a hint only. Future implementations of graph viewers may adjust the
	 * actual visual representation based on the look of the graph.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	private double endAngle;
	
	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the start point, and the control point/the length of the connection.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	private double startLength;
	
	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the end point, and the control point/the length of the connection.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	private double endLength;
	
	
	/**
	 * Visibility based on the internal visibility of the connection, and
	 * of the nodes.
	 */
	private boolean trueVisibility;
	
	/**
	 * The state of visibility set by the user.
	 */
	private boolean visible;
	/**
	 * Listens for when the source and target nodes change visibility. If 
	 * a connection's node is invisible, so should the connection.
	 * @author Del Myers
	 *
	 */
	//@tag zest.bug.156528-Filters.follows : we need this support for filters.
	private class NodeVisibilityListener implements PropertyChangeListener {
		/* (non-Javadoc)
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if (VISIBLE_PROP.equals(evt.getPropertyName())) {
				resetVisibility();
			}
		}
	}
	
	private NodeVisibilityListener nodeListener;
	
	/**
	 * LayoutConnection constructor, initializes the nodes and the connection properties.
	 * Defaults to bidirectional and a weighting of 0.
	 * @param graphModel	The graph model.
	 * @param data			The data object for this connection.
	 * @param source		The source node.
	 * @param destination 	The destination node.
	 */
	public GraphModelConnection(GraphModel graphModel, Object data, IGraphModelNode source, IGraphModelNode destination) {
		this(graphModel, data, source, destination, true, 0);
	}
	
	/**
	 * LayoutConnection constructor, initializes the nodes and the connection properties.
	 * @param graphModel	The graph model.
	 * @param data			The data object for this connection.
	 * @param source		The source node.
	 * @param destination 	The destination node.
	 * @param bidirection	If the connection is bidirectional.
	 * @param weight		The connection weight.
	 */
	public GraphModelConnection(GraphModel graphModel, Object data, IGraphModelNode source, IGraphModelNode destination, boolean bidirection, double weight) {
		super(graphModel);
		this.trueVisibility = super.isVisible();
		this.visible = this.trueVisibility;
		ZestPlugin plugin = ZestPlugin.getDefault();
		this.setData(data);
		this.connectionStyle = IZestGraphDefaults.CONNECTION_STYLE;
		this.color = plugin.getColor(IZestColorConstants.EDGE_DEFAULT);
		this.foreground = plugin.getColor(IZestColorConstants.EDGE_DEFAULT);
		this.highlightColor = plugin.getColor(IZestColorConstants.EDGE_HIGHLIGHT);
		this.lineWidth = 1;
		this.lineStyle = Graphics.LINE_SOLID;
		setWeightInLayout(weight);
		this.attributes = new HashMap();
		this.isConnected = false;
		this.graphModel = graphModel;
		this.sourceNode = source;
		this.destinationNode = destination;
		//@tag removed : can cause the edit parts to be created before the model is finished. Wait until the model is fully constructed to reconnect.
		//reconnect(source, destination);
		this.font = Display.getDefault().getSystemFont();
		nodeListener = new NodeVisibilityListener();
	}
	
	/**
	 * Gets the external connection object.
	 * @return Object
	 */
	public Object getExternalConnection() {
		return this.getData();
	}
	
	/**
	 * Returns a string like 'source -> destination'
	 * @return String
	 */
	public String toString() {
		String arrow = (isBidirectionalInLayout() ? " <--> " : " --> ");
		String src = (sourceNode != null ? sourceNode.getText() : "null");
		String dest = (destinationNode != null ? destinationNode.getText() : "null");
		String weight = "  (weight=" + getWeightInLayout() + ")";
		return ("GraphModelConnection: " + src + arrow + dest + weight);
	}

	/** 
	 * Disconnect this connection from the shapes it is attached to.
	 */
	public void disconnect() {
		if (isConnected) {
			sourceNode.removePropertyChangeListener(nodeListener);
			destinationNode.removePropertyChangeListener(nodeListener);
			sourceNode.removeConnection(this);
			destinationNode.removeConnection(this);
			isConnected = false;
		}
	}
	
	/** 
	 * Reconnect this connection. 
	 * The connection will reconnect with the node it was previously attached to.
	 */  
	public void reconnect() {
		if (!isConnected) {
			sourceNode.addConnection(this, true);
			destinationNode.addConnection(this, false);
			sourceNode.addPropertyChangeListener(nodeListener);
			destinationNode.addPropertyChangeListener(nodeListener);
			isConnected = true;
		}
	}

	/**
	 * Reconnect to a different source and/or destination node.
	 * The connection will disconnect from its current attachments and reconnect to 
	 * the new source and destination. 
	 * @param newSource 		a new source endpoint for this connection (non null)
	 * @param newDestination	a new destination endpoint for this connection (non null)
	 * @throws IllegalArgumentException if any of the paramers are null
	 */
	public void reconnect(IGraphModelNode newSource, IGraphModelNode newDestination) {
		if (newSource == null || newDestination == null ) {
			throw new IllegalArgumentException("Invalid source and/or destination nodes");
		}
		disconnect();
		this.sourceNode = newSource;
		this.destinationNode = newDestination;
		reconnect();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getSourceInLayout()
	 */
	public LayoutEntity getSourceInLayout() {
		return sourceNode;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getDestinationInLayout()
	 */
	public LayoutEntity getDestinationInLayout() {
		return destinationNode;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#isBidirectionalInLayout()
	 */
	public boolean isBidirectionalInLayout() {
		return !ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_DIRECTED);
	}

	/**
	 * Returns the style of this connection. Valid styles are those that begin with
	 * CONNECTION in ZestStyles.
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}
	/**
	 * Returns the style of this connection. Valid styles are those that begin with
	 * CONNECTION in ZestStyles.
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int style) {
		this.connectionStyle = style;
	}
	
	/**
	 * Gets the weight of this connection. The weight must be in {-1, [0-1]}.
	 * A weight of -1 means that there is no force/tension between the nodes.
	 * A weight of 0 results in the maximum spring length being used (farthest apart).
	 * A weight of 1 results in the minimum spring length being used (closest together).
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getWeightInLayout()
	 * @return the weight: {-1, [0 - 1]}.
	 */
	public double getWeightInLayout() {
		return weight;
	}

	/**
	 * Gets the font for the label on this connection
	 * @return
	 */
	public Font getFont() {
		return this.font;
	}
	/**
	 * Sets the font for the label on this connection.
	 *
	 */
	public void setFont(Font f) {
		this.font = f;
	}
	
	/**
	 * Sets the weight for this connection. The weight must be in {-1, [0-1]}.
	 * A weight of -1 means that there is no force/tension between the nodes.
	 * A weight of 0 results in the maximum spring length being used (farthest apart).
	 * A weight of 1 results in the minimum spring length being used (closest together).
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#setWeightInLayout(double)
	 */
	public void setWeightInLayout(double weight) {
		if (weight < 0) {
			this.weight = -1;
		} else if (weight > 1) {
			this.weight = 1;
		} else {
			this.weight = weight;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getAttributeInLayout(java.lang.String)
	 */
	public Object getAttributeInLayout(String attribute) {
		return attributes.get(attribute);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#setAttributeInLayout(java.lang.String, java.lang.Object)
	 */
	public void setAttributeInLayout(String attribute, Object value) {
		attributes.put(attribute, value);
	}

	/**
	 * Returns the color of this connection.
	 * @return Color
	 */
	public Color getLineColor() {
		return color;
	}

	/**
	 * Sets the highlight color.
	 * @param color the color to use for highlighting.
	 */
	public void setHighlightColor(Color color) {
		this.highlightColor = color;
	}
	
	/**
	 * @return the highlight color
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}
	/**
	 * Perminently sets the color of this line to the given color. This will become the
	 * color of the line when it is not highlighted. If you would like to temporarily change
	 * the color of the line, use changeLineColor.
	 * @param color the color to be set.
	 * @see changeLineColor(Color color)
	 */
	public void setLineColor(Color color) {
		this.foreground = color;
		changeLineColor(foreground);
	}
	
	/**
	 * Sets the connection color.
	 * @param color
	 */
	public void changeLineColor(Color color) {
		Color old = this.color;
		if (this.color != color) {
			this.color = color;
			firePropertyChange(LINECOLOR_PROP, old, color);
		}
	}

	/**
	 * Returns the connection line width.
	 * @return int
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Sets the connection line width.
	 * @param lineWidth 
	 */
	public void setLineWidth(int lineWidth) {
		int old = this.lineWidth;
		if (this.lineWidth != lineWidth) {
			this.lineWidth = lineWidth;
			firePropertyChange(LINEWIDTH_PROP, new Integer(old), new Integer(lineWidth) );
		}
	}
	

	/**
	 * Returns the connection line style.
	 * @return int
	 */
	public int getLineStyle() {
		return lineStyle;
	}

	/**
	 * Sets the connection line style.
	 * @param lineStyle
	 */
	public void setLineStyle(int lineStyle) {
		int old = this.lineStyle;
		if (this.lineStyle != lineStyle) {
			this.lineStyle = lineStyle;
			firePropertyChange(LINESTYLE_PROP, new Integer(old), new Integer(lineStyle));
		}
	}
	
	/**
	 * Gets the source node for this relationship
	 * @return GraphModelNode
	 */
	public IGraphModelNode getSource() {
		return this.sourceNode;
	}
	
	/**
	 * Gets the target node for this relationship
	 * @return GraphModelNode
	 */
	public IGraphModelNode getDestination() {
		return this.destinationNode;
	}
	
	/**
	 * Gets the internal relationship object.
	 * @return Object
	 */
	public Object getLayoutInformation() {
		return internalConnection;
	}
	
	/**
	 * Sets the internal relationship object.
	 * @param layoutInformation
	 */
	public void setLayoutInformation(Object layoutInformation) {
		this.internalConnection = layoutInformation;
	}
	
	/**
	 * Highlights this node.  Uses the default highlight color.
	 */
	public void highlight() {
		changeLineColor(highlightColor);
		firePropertyChange(HIGHLIGHT_PROP, null, null);
	}
	
	/**
	 * Unhighlights this node.  Uses the default color.
	 */
	public void unhighlight() {
		changeLineColor(foreground);
		firePropertyChange(UNHIGHLIGHT_PROP, null, null);
	}
	
	/**
	 * Gets the graph model that this connection is in
	 * @return The graph model that this connection is contained in
	 */
	public GraphModel getGraphModel() {
		return this.graphModel;
	}


	public void setBendPoints(LayoutBendPoint[] bendPoints) {
		
	}

	public void clearBendPoints() {
		
	}

	/**
	 * Returns the curve depth for this connection. The return value is only meaningful
	 * if the connection style has the ZestStyles.CONNECTIONS_CURVED style set.
	 * @return the curve depth.
	 */
	public int getCurveDepth() {
		return curveDepth;
	}
	
	public void setCurveDepth(int curveDepth) {
		Integer oldDepth = new Integer(this.curveDepth);
		this.curveDepth = curveDepth;
		firePropertyChange(CURVE_PROP, oldDepth, new Integer(curveDepth));
	}


	public void populateLayoutConstraint(LayoutConstraint constraint) {
	
	}


	/**
	 * Gets the end angle for bezier arcs.
	 * 
	 * For bezier curves: angle between the start point, and the line.
	 * This may be a hint only. Future implementations of graph viewers may
	 * adjust the actual visual representation based on the look of the graph.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public double getEndAngle() {
		return endAngle;
	}
	
	/**
	 * Sets the end angle for bezier arcs.
	 * 
	 * For bezier curves: angle between the start point, and the line.
	 * This may be a hint only. Future implementations of graph viewers may
	 * adjust the actual visual representation based on the look of the graph.
	 * 
	 * @param endAngle the angle to set.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public void setEndAngle(double endAngle) {
		Double oldAngle = new Double(this.endAngle);
		this.endAngle = endAngle;
		firePropertyChange(CURVE_PROP, oldAngle, new Double(endAngle));
	}

	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the end point, and the control point/the length of the connection.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public double getEndLength() {
		return endLength;
	}

	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the end point, and the control point/the length of the connection.
	 * 
	 * @param endLength the length to set.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public void setEndLength(double endLength) {
		Double oldLength = new Double(this.endLength);
		this.endLength = endLength;
		firePropertyChange(CURVE_PROP, oldLength, new Double(endLength));
		
	}

	/**
	 * Gets the start angle for bezier arcs.
	 * 
	 * For bezier curves: angle between the end point and the line. This may
	 * be a hint only. Future implementations of graph viewers may adjust the
	 * actual visual representation based on the look of the graph.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public double getStartAngle() {
		return startAngle;
	}

	/**
	 * Sets the start angle for bezier arcs.
	 * 
	 * For bezier curves: angle between the end point and the line. This may
	 * be a hint only. Future implementations of graph viewers may adjust the
	 * actual visual representation based on the look of the graph.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public void setStartAngle(double startAngle) {
		Double oldAngle = new Double(this.startAngle);
		this.startAngle = startAngle;
		firePropertyChange(CURVE_PROP, oldAngle, new Double(startAngle));
		
	}

	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the start point, and the control point/the length of the connection.
	 */
//	@tag zest(bug(152530-Bezier(fix)))
	public double getStartLength() {
		return startLength;
	}

	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of the
	 * line between the start point, and the control point/the length of the connection.
	 * @param startLength the length to set.
	 */
	//@tag zest(bug(152530-Bezier(fix)))
	public void setStartLength(double startLength) {
		Double oldLength = new Double(this.startLength);
		this.startLength = startLength;
		firePropertyChange(CURVE_PROP, oldLength, new Double(startLength));
	}
	
	private boolean checkVisibilityByNodes() {
		if (!isConnected) return false;
		boolean visible = true;
		if (sourceNode != null) {
			visible &= sourceNode.isVisible();
		} else {
			return false;
		}
		if (visible && destinationNode != null) {
			visible &= destinationNode.isVisible();
		} else {
			return false;
		}
		return visible;
	}
	
	private void resetVisibility() {
		boolean parent = this.visible;
		boolean old = isVisible();
		boolean nodes = checkVisibilityByNodes();
		boolean visible = parent && nodes;
		if (visible != old) {
			trueVisibility = visible;
			firePropertyChange(VISIBLE_PROP, new Boolean(old), new Boolean(nodes));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		boolean nodeVisibility = checkVisibilityByNodes();
		boolean old = isVisible();
		trueVisibility = visible && nodeVisibility;
		this.visible = visible;
		if (old != trueVisibility) {
			firePropertyChange(VISIBLE_PROP, new Boolean(old), new Boolean(trueVisibility));
		}
	}
	
	public boolean isVisible() {
		return trueVisibility;
	}
}
