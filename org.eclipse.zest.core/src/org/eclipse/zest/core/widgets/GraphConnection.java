/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylyn.zest.core.widgets;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.Shape;
import org.eclipse.mylyn.zest.core.widgets.internal.AligningBendpointLocator;
import org.eclipse.mylyn.zest.core.widgets.internal.LoopAnchor;
import org.eclipse.mylyn.zest.core.widgets.internal.PolylineArcConnection;
import org.eclipse.mylyn.zest.core.widgets.internal.RoundedChopboxAnchor;
import org.eclipse.mylyn.zest.core.widgets.internal.ZestRootLayer;
import org.eclipse.mylyn.zest.layouts.LayoutBendPoint;
import org.eclipse.mylyn.zest.layouts.LayoutEntity;
import org.eclipse.mylyn.zest.layouts.LayoutRelationship;
import org.eclipse.mylyn.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * This is the graph connection model which stores the source and destination
 * nodes and the properties of this connection (color, line width etc).
 * 
 * @author Chris Callendar
 * @author Ian Bull
 */
public class GraphConnection extends GraphItem {

	private Font font;
	private GraphNode sourceNode;
	private GraphNode destinationNode;

	private double weight;
	private Color color;
	private Color highlightColor;
	private Color foreground;
	private int lineWidth;
	private int lineStyle;
	private final Graph graphModel;

	private int connectionStyle;
	private int curveDepth;
	private boolean isDisposed = false;

	private Connection connectionFigure = null;
	private Connection sourceContainerConnectionFigure = null;
	private Connection targetContainerConnectionFigure = null;

	/**
	 * For bezier curves: angle between the start point, and the line. This may
	 * be a hint only. Future implementations of graph viewers may adjust the
	 * actual visual representation based on the look of the graph.
	 */
	// @tag zest(bug(152530-Bezier(fix)))
	// private double startAngle;
	/**
	 * For bezier curves: angle between the end point and the line. This may be
	 * a hint only. Future implementations of graph viewers may adjust the
	 * actual visual representation based on the look of the graph.
	 */
	// @tag zest(bug(152530-Bezier(fix)))
	// private double endAngle;
	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of
	 * the line between the start point, and the control point/the length of the
	 * connection.
	 */
	// @tag zest(bug(152530-Bezier(fix)))
	// private double startLength;
	/**
	 * For bezier curves: this is a value from 0-1 as a ratio of the length of
	 * the line between the end point, and the control point/the length of the
	 * connection.
	 */
	// @tag zest(bug(152530-Bezier(fix)))
	// private double endLength;
	/**
	 * The state of visibility set by the user.
	 */
	private boolean visible;

	private IFigure tooltip;

	private boolean highlighted;
	private GraphLayoutConnection layoutConnection = null;

	public GraphConnection(Graph graphModel, int style, GraphNode source, GraphNode destination) {
		super(graphModel, style);

		this.connectionStyle |= graphModel.getConnectionStyle();
		this.connectionStyle |= style;
		this.sourceNode = source;
		this.destinationNode = destination;
		this.visible = true;
		this.color = ColorConstants.lightGray;
		this.foreground = ColorConstants.lightGray;
		this.highlightColor = graphModel.DARK_BLUE;
		this.lineWidth = 1;
		this.lineStyle = Graphics.LINE_SOLID;
		setWeight(weight);
		this.graphModel = graphModel;
		this.curveDepth = 20;
		this.layoutConnection = new GraphLayoutConnection();
		this.font = Display.getDefault().getSystemFont();
		(source).addSourceConnection(this);
		(destination).addTargetConnection(this);
		connectionFigure = createFigure();

		if (source.getParent().getItemType() == GraphItem.CONTAINER && destination.getParent().getItemType() == GraphItem.CONTAINER && (source.getParent() == destination.getParent())) {
			// If the source and the destination are in the same container (not the root graph) then 
			// don't add the connection to the edge layer.  This way we don't get artifacts on the screen
			// when the nodes are scrolled off the screen
			//
			// 196189: Edges should not draw on the edge layer if both the src and dest are in the same container
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=196189
			graphModel.addConnection(this, ZestRootLayer.EDGES_ON_TOP);
		} else {
			graphModel.addConnection(this, true);
		}
		graphModel.getGraph().registerItem(this);

		if ((source.getParent()).getItemType() == GraphItem.CONTAINER) {
			// If the container of the source is a container, we need to draw another
			// arc on that arc layer
			ChopboxAnchor srcAnchor = new ChopboxAnchor(source.getFigure());
			ChopboxAnchor destAnchor = new ChopboxAnchor(destination.getFigure());
			PolylineConnection polylineConnection = new PolylineConnection();
			polylineConnection.setSourceAnchor(srcAnchor);
			polylineConnection.setTargetAnchor(destAnchor);
			polylineConnection.setForegroundColor(this.color);
			((GraphContainer) source.getParent()).addConnectionFigure(polylineConnection);
			sourceContainerConnectionFigure = polylineConnection;
			this.setVisible(false);
		}

		if ((destination.getParent()).getItemType() == GraphItem.CONTAINER) { //&& src_destSameContainer == false) {
			// If the container of the source is a container, we need to draw another
			// arc on that arc layer
			ChopboxAnchor srcAnchor = new ChopboxAnchor(source.getFigure());
			ChopboxAnchor destAnchor = new ChopboxAnchor(destination.getFigure());
			PolylineConnection polylineConnection = new PolylineConnection();
			polylineConnection.setBackgroundColor(this.color);
			polylineConnection.setForegroundColor(this.color);
			polylineConnection.setSourceAnchor(srcAnchor);
			polylineConnection.setTargetAnchor(destAnchor);
			((GraphContainer) destination.getParent()).addConnectionFigure(polylineConnection);
			targetContainerConnectionFigure = polylineConnection;
			this.setVisible(false);
		}
	}

	public void dispose() {
		super.dispose();
		this.isDisposed = true;
		(getSource()).removeSourceConnection(this);
		(getDestination()).removeTargetConnection(this);
		graphModel.removeConnection(this);
	}

	public boolean isDisposed() {
		return isDisposed;
	}

	public Connection getConnectionFigure() {
		return connectionFigure;
	}

	/**
	 * Gets a proxy to this connection that can be used with the Zest layout
	 * engine
	 * 
	 * @return
	 */
	public LayoutRelationship getLayoutRelationship() {
		return this.layoutConnection;
	}

	/**
	 * Gets the external connection object.
	 * 
	 * @return Object
	 */
	public Object getExternalConnection() {
		return this.getData();
	}

	/**
	 * Returns a string like 'source -> destination'
	 * 
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
	 * Returns the style of this connection. Valid styles are those that begin
	 * with CONNECTION in ZestStyles.
	 * 
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public int getConnectionStyle() {
		return connectionStyle;
	}

	/**
	 * Returns the style of this connection. Valid styles are those that begin
	 * with CONNECTION in ZestStyles.
	 * 
	 * @return the style of this connection.
	 * @see #ZestStyles
	 */
	public void setConnectionStyle(int style) {
		this.connectionStyle = style;
	}

	/**
	 * Gets the weight of this connection. The weight must be in {-1, [0-1]}. A
	 * weight of -1 means that there is no force/tension between the nodes. A
	 * weight of 0 results in the maximum spring length being used (farthest
	 * apart). A weight of 1 results in the minimum spring length being used
	 * (closest together).
	 * 
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#getWeightInLayout()
	 * @return the weight: {-1, [0 - 1]}.
	 */
	public double getWeightInLayout() {
		return weight;
	}

	/**
	 * Gets the font for the label on this connection
	 * 
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
	 * Sets the weight for this connection. The weight must be in {-1, [0-1]}. A
	 * weight of -1 means that there is no force/tension between the nodes. A
	 * weight of 0 results in the maximum spring length being used (farthest
	 * apart). A weight of 1 results in the minimum spring length being used
	 * (closest together).
	 * 
	 */
	public void setWeight(double weight) {
		if (weight < 0) {
			this.weight = -1;
		} else if (weight > 1) {
			this.weight = 1;
		} else {
			this.weight = weight;
		}
	}

	/**
	 * Returns the color of this connection.
	 * 
	 * @return Color
	 */
	public Color getLineColor() {
		return color;
	}

	/**
	 * Sets the highlight color.
	 * 
	 * @param color
	 *            the color to use for highlighting.
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
	 * Perminently sets the color of this line to the given color. This will
	 * become the color of the line when it is not highlighted. If you would
	 * like to temporarily change the color of the line, use changeLineColor.
	 * 
	 * @param color
	 *            the color to be set.
	 * @see changeLineColor(Color color)
	 */
	public void setLineColor(Color color) {
		this.foreground = color;
		changeLineColor(foreground);
	}

	/**
	 * Sets the connection color.
	 * 
	 * @param color
	 */
	public void changeLineColor(Color color) {
		this.color = color;
		updateFigure(connectionFigure);
	}

	/**
	 * Sets the tooltip on this node. This tooltip will display if the mouse
	 * hovers over the node. Setting the tooltip has no effect if a custom
	 * figure has been set.
	 */
	public void setTooltip(IFigure tooltip) {
		this.tooltip = tooltip;
		updateFigure(connectionFigure);
	}

	/**
	 * Gets the current tooltip for this node. The tooltip returned is
	 * meaningless if a custom figure has been set.
	 */
	public IFigure getTooltip() {
		return this.tooltip;
	}

	/**
	 * Returns the connection line width.
	 * 
	 * @return int
	 */
	public int getLineWidth() {
		return lineWidth;
	}

	/**
	 * Sets the connection line width.
	 * 
	 * @param lineWidth
	 */
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		updateFigure(connectionFigure);
	}

	/**
	 * Returns the connection line style.
	 * 
	 * @return int
	 */
	public int getLineStyle() {
		return lineStyle;
	}

	/**
	 * Sets the connection line style.
	 * 
	 * @param lineStyle
	 */
	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
		updateFigure(connectionFigure);
	}

	/**
	 * Gets the source node for this relationship
	 * 
	 * @return GraphModelNode
	 */
	public GraphNode getSource() {
		return this.sourceNode;
	}

	/**
	 * Gets the target node for this relationship
	 * 
	 * @return GraphModelNode
	 */
	public GraphNode getDestination() {
		return this.destinationNode;
	}

	/**
	 * Highlights this node. Uses the default highlight color.
	 */
	public void highlight() {
		if (highlighted) {
			return;
		}
		highlighted = true;
		updateFigure(connectionFigure);
		graphModel.highlightEdge(this);
	}

	/**
	 * Unhighlights this node. Uses the default color.
	 */
	public void unhighlight() {
		if (!highlighted) {
			return;
		}
		highlighted = false;
		updateFigure(connectionFigure);
		graphModel.unhighlightEdge(this);
	}

	/**
	 * Returns true if this connection is highlighted, false otherwise
	 * 
	 * @return
	 */
	public boolean isHighlighted() {
		return highlighted;
	}

	/**
	 * Gets the graph model that this connection is in
	 * 
	 * @return The graph model that this connection is contained in
	 */
	public Graph getGraphModel() {
		return this.graphModel;
	}

	// /**
	// * Returns the curve depth for this connection. The return value is only
	// * meaningful if the connection style has the
	// ZestStyles.CONNECTIONS_CURVED
	// * style set.
	// *
	// * @return the curve depth.
	// */
	// public int getCurveDepth() {
	// return curveDepth;
	// }

	// public void setCurveDepth(int curveDepth) {
	// this.curveDepth = curveDepth;
	// updateFigure(connectionFigure);
	// }

	// void invokeLayoutListeners(LayoutConstraint constraint) {
	// // @tag TODO: Add layout listeners
	// }

	// /**
	// * Gets the end angle for bezier arcs.
	// *
	// * For bezier curves: angle between the start point, and the line. This
	// may
	// * be a hint only. Future implementations of graph viewers may adjust the
	// * actual visual representation based on the look of the graph.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public double getEndAngle() {
	// return endAngle;
	// }

	// /**
	// * Sets the end angle for bezier arcs.
	// *
	// * For bezier curves: angle between the start point, and the line. This
	// may
	// * be a hint only. Future implementations of graph viewers may adjust the
	// * actual visual representation based on the look of the graph.
	// *
	// * @param endAngle
	// * the angle to set.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public void setEndAngle(double endAngle) {
	// this.endAngle = endAngle;
	// updateFigure(connectionFigure);
	// }

	// /**
	// * For bezier curves: this is a value from 0-1 as a ratio of the length of
	// * the line between the end point, and the control point/the length of the
	// * connection.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public double getEndLength() {
	// return endLength;
	// }

	// /**
	// * For bezier curves: this is a value from 0-1 as a ratio of the length of
	// * the line between the end point, and the control point/the length of the
	// * connection.
	// *
	// * @param endLength
	// * the length to set.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public void setEndLength(double endLength) {
	// this.endLength = endLength;
	// updateFigure(connectionFigure);
	// }

	// /**
	// * Gets the start angle for bezier arcs.
	// *
	// * For bezier curves: angle between the end point and the line. This may
	// be
	// * a hint only. Future implementations of graph viewers may adjust the
	// * actual visual representation based on the look of the graph.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public double getStartAngle() {
	// return startAngle;
	// }

	// /**
	// * Sets the start angle for bezier arcs.
	// *
	// * For bezier curves: angle between the end point and the line. This may
	// be
	// * a hint only. Future implementations of graph viewers may adjust the
	// * actual visual representation based on the look of the graph.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public void setStartAngle(double startAngle) {
	// this.startAngle = startAngle;
	// updateFigure(connectionFigure);
	//
	// }

	// /**
	// * For bezier curves: this is a value from 0-1 as a ratio of the length of
	// * the line between the start point, and the control point/the length of
	// the
	// * connection.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public double getStartLength() {
	// return startLength;
	// }

	// /**
	// * For bezier curves: this is a value from 0-1 as a ratio of the length of
	// * the line between the start point, and the control point/the length of
	// the
	// * connection.
	// *
	// * @param startLength
	// * the length to set.
	// */
	// // @tag zest(bug(152530-Bezier(fix)))
	// public void setStartLength(double startLength) {
	// this.startLength = startLength;
	// updateFigure(connectionFigure);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#getItemType()
	 */
	public int getItemType() {
		return CONNECTION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.GraphItem#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		//graphModel.addRemoveFigure(this, visible);
		if (getSource().isVisible() && getDestination().isVisible() && visible) {
			this.getFigure().setVisible(visible);
			if (sourceContainerConnectionFigure != null) {
				sourceContainerConnectionFigure.setVisible(visible);
			}
			if (targetContainerConnectionFigure != null) {
				targetContainerConnectionFigure.setVisible(visible);
			}
			this.visible = visible;
		} else {
			this.getFigure().setVisible(false);
			if (sourceContainerConnectionFigure != null) {
				sourceContainerConnectionFigure.setVisible(false);
			}
			if (targetContainerConnectionFigure != null) {
				targetContainerConnectionFigure.setVisible(false);
			}
			this.visible = false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#isVisible()
	 */
	public boolean isVisible() {
		return visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Item#setText(java.lang.String)
	 */
	public void setText(String string) {
		super.setText(string);

		if (this.connectionFigure != null) {
			updateFigure(this.connectionFigure);
		}
	}

	private Connection updateFigure(Connection connection) {
		Shape connectionShape = (Shape) connection;

		connectionShape.setLineStyle(getLineStyle());

		AligningBendpointLocator labelLocator = new AligningBendpointLocator(connection);
		if (this.getText() != null || this.getImage() != null) {
			Label l = new Label(this.getText(), this.getImage());
			l.setFont(this.getFont());
			connection.add(l, labelLocator);
		}

		if (highlighted) {
			connectionShape.setForegroundColor(getHighlightColor());
			connectionShape.setLineWidth(getLineWidth() * 2);
		} else {
			connectionShape.setForegroundColor(getLineColor());
			connectionShape.setLineWidth(getLineWidth());
		}

		if (connection instanceof PolylineArcConnection) {
			PolylineArcConnection arcConnection = (PolylineArcConnection) connection;
			arcConnection.setDepth(curveDepth);
		}
		if ((connectionStyle & ZestStyles.CONNECTIONS_DIRECTED) > 0) {
			PolygonDecoration decoration = new PolygonDecoration();
			if (getLineWidth() < 3) {
				decoration.setScale(9, 3);
			} else {
				double logLineWith = getLineWidth() / 2.0;
				decoration.setScale(7 * logLineWith, 3 * logLineWith);
			}
			((PolylineConnection) connection).setTargetDecoration(decoration);
		}

		IFigure toolTip;
		if (this.getTooltip() == null && getText() != null && getText().length() > 0) {
			toolTip = new Label();
			((Label) toolTip).setText(getText());
		} else {
			toolTip = this.getTooltip();
		}
		connection.setToolTip(toolTip);

		return connection;
	}

	private Connection createFigure() {
		Connection connectionFigure = null;
		ChopboxAnchor sourceAnchor = null;
		ChopboxAnchor targetAnchor = null;
		if (getSource() == getDestination()) {
			connectionFigure = new PolylineArcConnection();
			sourceAnchor = new LoopAnchor(getSource().getNodeFigure());
			targetAnchor = new LoopAnchor(getDestination().getNodeFigure());
		} else {
			connectionFigure = new PolylineConnection();
			sourceAnchor = new RoundedChopboxAnchor(getSource().getNodeFigure(), 8);
			targetAnchor = new RoundedChopboxAnchor(getDestination().getNodeFigure(), 8);
		}

		connectionFigure.setSourceAnchor(sourceAnchor);
		connectionFigure.setTargetAnchor(targetAnchor);

		return updateFigure(connectionFigure);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.layouts.LayoutRelationship#isBidirectionalInLayout()
	 */
	private boolean isBidirectionalInLayout() {
		return !ZestStyles.checkStyle(connectionStyle, ZestStyles.CONNECTIONS_DIRECTED);
	}

	class GraphLayoutConnection implements LayoutRelationship {

		Object layoutInformation = null;

		public void clearBendPoints() {
			// @tag TODO : add bendpoints
		}

		public LayoutEntity getDestinationInLayout() {
			return getDestination().getLayoutEntity();
		}

		public Object getLayoutInformation() {
			return layoutInformation;
		}

		public LayoutEntity getSourceInLayout() {
			return getSource().getLayoutEntity();
		}

		public void populateLayoutConstraint(LayoutConstraint constraint) {
			graphModel.invokeConstraintAdapters(GraphConnection.this, constraint);
		}

		public void setBendPoints(LayoutBendPoint[] bendPoints) {
			// @tag TODO : add bendpoints
		}

		public void setLayoutInformation(Object layoutInformation) {
			this.layoutInformation = layoutInformation;
		}

	}

	IFigure getFigure() {
		return this.getConnectionFigure();
	}
}
