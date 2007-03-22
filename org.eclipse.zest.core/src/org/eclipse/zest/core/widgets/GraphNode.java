/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.widgets.internal.GraphLabel;
import org.eclipse.mylar.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Simple node class which has the following properties: color, size, location,
 * and a label. It also has a list of connections and anchors.
 * 
 * @author Chris Callendar
 * @author Del Myers
 * @author Ian Bull
 */
public class GraphNode extends GraphItem implements IGraphNode {
	public static final int HIGHLIGHT_NONE = 0;
	public static final int HIGHLIGHT_ON = 1;
	public static final int HIGHLIGHT_ADJACENT = 2;

	private int nodeStyle;

	private List /* IGraphModelConnection */sourceConnections;
	private List /* IGraphModelConnection */targetConnections;

	private boolean preferredLocation;
	private Color foreColor;
	private Color backColor;
	private Color highlightColor;
	private Color highlightAdjacentColor;
	private Color borderColor;
	private Color borderHighlightColor;
	private Color borderUnhighlightColor;
	private int borderWidth;
	private Point currentLocation;
	private Dimension size;
	private Font font;
	private boolean cacheLabel;
	private boolean visible = true;

	protected Dimension labelSize;
	protected Graph graphModel;

	/** The internal node. */
	protected Object internalNode;
	private boolean selected;
	private int highlighted = HIGHLIGHT_NONE;
	private IFigure tooltip;
	private GraphLabel nodeFigure;
	private IFigure customFigure;
	private boolean useCustomFigure = false;
	private boolean isDisposed = false;

	public GraphNode(Graph graphModel, int style) {
		super(graphModel);
		initModel(graphModel, " ");
	}

	public GraphNode(Graph graphModel, int style, String text) {
		super(graphModel);
		initModel(graphModel, text);

	}

	protected void initModel(Graph graphModel, String text) {
		this.nodeStyle |= graphModel.getNodeStyle();
		this.sourceConnections = new ArrayList();
		this.targetConnections = new ArrayList();
		this.preferredLocation = false;
		this.foreColor = graphModel.DARK_BLUE;
		this.backColor = graphModel.LIGHT_BLUE;
		this.highlightColor = graphModel.HIGHLIGHT_COLOR;
		this.highlightAdjacentColor = ColorConstants.orange;
		this.nodeStyle = IZestGraphDefaults.NODE_STYLE;
		this.borderColor = ColorConstants.black;
		this.borderHighlightColor = ColorConstants.blue;
		this.borderUnhighlightColor = ColorConstants.black;
		this.borderWidth = 1;
		this.currentLocation = new PrecisionPoint(10, 10);
		this.size = new Dimension(0, 0);
		this.font = Display.getDefault().getSystemFont();
		this.graphModel = graphModel;
		this.cacheLabel = false;
		this.setText(text);

		if (font == null) {
			font = Display.getDefault().getSystemFont();
		}
		nodeFigure = createFigureForModel();
		graphModel.addNode(this);

		//		if (this.isContainer) {
		//			AspectRatioScaledFigure aspectRatioScaledFigure = new AspectRatioScaledFigure("item");
		//			aspectRatioScaledFigure.setSize(500000, 500000);
		//			aspectRatioScaledFigure.setBackgroundColor(ColorConstants.gray);
		//			RoundedRectangle roundedRectangle = new RoundedRectangle();
		//			aspectRatioScaledFigure.setScale(0.5, 0.5);
		//			roundedRectangle.setSize(50, 50);
		//			roundedRectangle.setBackgroundColor(ColorConstants.green);
		//			aspectRatioScaledFigure.add(roundedRectangle);
		//			setCustomFigure(aspectRatioScaledFigure);
		//		}
	}

	/**
	 * A simple toString that we can use for debugging
	 */
	public String toString() {
		return "GraphModelNode: " + getText();
	}

	public void dispose() {
		super.dispose();
		this.isDisposed = true;
		while (getSourceConnections().size() > 0) {
			GraphConnection connection = (GraphConnection) getSourceConnections().get(0);
			if (!connection.isDisposed()) {
				connection.dispose();
			}
		}
		while (getTargetConnections().size() > 0) {
			GraphConnection connection = (GraphConnection) getTargetConnections().get(0);
			if (!connection.isDisposed()) {
				connection.dispose();
			}
		}
		graphModel.removeNode(this);
	}

	public boolean isDisposed() {
		return isDisposed;
	}

	/**
	 * Compares two nodes.
	 */
	public int compareTo(Object otherNode) {
		int rv = 0;
		if (otherNode instanceof IGraphNode) {
			IGraphNode node = (IGraphNode) otherNode;
			if (this.getText() != null) {
				rv = this.getText().compareTo(node.getText());
			}
		}
		return rv;
	}

	/**
	 * Gets the user data associated with this node.
	 * 
	 * @return The user data associated with this node
	 */
	public Object getExternalNode() {
		return this.getData();
	}

	/**
	 * Returns a new list of the source connections (GraphModelConnection
	 * objects).
	 * 
	 * @return List a new list of GraphModelConnect objects
	 */
	public List getSourceConnections() {
		return new ArrayList(sourceConnections);
	}

	/**
	 * Returns a new list of the target connections (GraphModelConnection
	 * objects).
	 * 
	 * @return List a new list of GraphModelConnect objects
	 */
	public List getTargetConnections() {
		return new ArrayList(targetConnections);
	}

	void addSourceConnection(GraphConnection connection) {
		this.sourceConnections.add(connection);
	}

	void addTargetConnection(GraphConnection connection) {
		this.targetConnections.add(connection);
	}

	void removeSourceConnection(GraphConnection connection) {
		this.sourceConnections.remove(connection);
	}

	void removeTargetConnection(GraphConnection connection) {
		this.targetConnections.remove(connection);
	}

	public void setHasPreferredLocation(boolean preferredLocation) {
		this.preferredLocation = preferredLocation;
	}

	public boolean hasPreferredLocation() {
		return preferredLocation;
	}

	public double getXInLayout() {
		return currentLocation.x;
	}

	public double getYInLayout() {
		return currentLocation.y;
	}

	/**
	 * Returns the bounds of this node. It is just the combination of the
	 * location and the size.
	 * 
	 * @return Rectangle
	 */
	public Rectangle getBounds() {
		return new Rectangle(getLocation(), getSize());
	}

	/**
	 * Returns a copy of the node's location.
	 * 
	 * @return Point
	 */
	public Point getLocation() {
		return currentLocation;
	}

	public double getWidthInLayout() {
		return size.width;
	}

	public double getHeightInLayout() {
		return size.height;
	}

	public void setSelected(boolean selected) {
		if (selected = isSelected()) {
			return;
		}
		if (selected) {
			highlight();
		} else {
			unhighlight();
		}
		this.selected = selected;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	public void setPreferredLocation(double x, double y) {
		setLocation(x, y);
	}

	public void setLocation(double x, double y) {
		currentLocation.x = (int) x;
		currentLocation.y = (int) y;
		refreshLocation();
	}

	/**
	 * Sets the layout location of this node
	 */
	public void setLocationInLayout(double x, double y) {
		this.setLocation(x, y);
	}

	/**
	 * Returns a copy of the node's size.
	 * 
	 * @return Dimension
	 */
	public Dimension getSize() {
		return size.getCopy();
	}

	/**
	 * Sets the size of this node
	 */
	public void setSizeInLayout(double width, double height) {
		setSize(width, height);
	}

	/**
	 * Get the foreground colour for this node
	 */
	public Color getForegroundColor() {
		return foreColor;
	}

	/**
	 * Set the foreground colour for this node
	 */
	public void setForegroundColor(Color c) {
		this.foreColor = c;
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Get the background colour for this node
	 */
	public Color getBackgroundColor() {
		return backColor;
	}

	/**
	 * Permantly sets the background color (unhighlighted). For temporary color
	 * changes call #changeBackgroundColor instead.
	 * 
	 * @param c
	 */
	public void setBackgroundColor(Color c) {
		backColor = c;
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Sets the tooltip on this node
	 */
	public void setTooltip(IFigure tooltip) {
		this.tooltip = tooltip;
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Gets the current tooltip for this node
	 */
	public IFigure getTooltip() {
		return this.tooltip;
	}

	/**
	 * Sets the border color.
	 * 
	 * @param c
	 *            the border color.
	 */
	public void setBorderColor(Color c) {
		borderColor = c;
		borderUnhighlightColor = c;
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Sets the highlighted border color.
	 * 
	 * @param c
	 *            the highlighted border color.
	 */
	public void setBorderHighlightColor(Color c) {
		this.borderHighlightColor = c;
		updateFigureForModel(nodeFigure);
	}

	// /**
	// * Changes the background color and fires a property change event.
	// *
	// * @param c
	// */
	// public void changeBackgroundColor(Color c) {
	// Color old = backColor;
	// backColor = c;
	// firePropertyChange(COLOR_BG_PROP, old, c);
	//
	// }

	/**
	 * Get the highlight colour for this node
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	/**
	 * Set the highlight colour for this node
	 */
	public void setHighlightColor(Color c) {
		this.highlightColor = c;
	}

	/**
	 * Get the highlight adjacent colour for this node. This is the colour that
	 * adjacent nodes will get
	 */
	public Color getHighlightAdjacentColor() {
		return highlightAdjacentColor;
	}

	/**
	 * Set the highlight adjacent colour for this node. This is the colour that
	 * adjacent node will get.
	 */
	public void setHighlightAdjacentColor(Color c) {
		this.highlightAdjacentColor = c;
	}

	/**
	 * Highlights the node changing the background color and border color. The
	 * source and destination connections are also highlighted, and the adjacent
	 * nodes are highlighted too in a different color.
	 */
	public void highlight() {
		if (highlighted == HIGHLIGHT_ON) {
			return;
		}
		highlighted = HIGHLIGHT_ON;
		if (ZestStyles.checkStyle(getNodeStyle(), ZestStyles.NODES_HIGHLIGHT_ADJACENT)) {
			for (Iterator iter = sourceConnections.iterator(); iter.hasNext();) {
				IGraphConnection conn = (IGraphConnection) iter.next();
				conn.highlight();
				conn.getDestination().highlightAdjacent();
			}
			for (Iterator iter = targetConnections.iterator(); iter.hasNext();) {
				IGraphConnection conn = (IGraphConnection) iter.next();
				conn.highlight();
				conn.getSource().highlightAdjacent();
			}
		}
		updateFigureForModel(nodeFigure);
		graphModel.highlightNode(this);
	}

	public boolean isHighlighted() {
		return highlighted > 0;
	}

	/**
	 * Restores the nodes original background color and border width.
	 */
	public void unhighlight() {
		boolean highlightedAdjacently = (highlighted == HIGHLIGHT_ADJACENT);
		if (highlighted == HIGHLIGHT_NONE) {
			return;
		}
		highlighted = HIGHLIGHT_NONE;
		if (!highlightedAdjacently) {
			// IF we are highlighted as an adjacent node, we don't need to deal with our connections.
			if (ZestStyles.checkStyle(getNodeStyle(), ZestStyles.NODES_HIGHLIGHT_ADJACENT)) {
				// unhighlight the adjacent edges
				for (Iterator iter = sourceConnections.iterator(); iter.hasNext();) {
					IGraphConnection conn = (IGraphConnection) iter.next();
					conn.unhighlight();
					if (conn.getDestination() != this) {
						conn.getDestination().unhighlight();
					}
				}
				for (Iterator iter = targetConnections.iterator(); iter.hasNext();) {
					IGraphConnection conn = (IGraphConnection) iter.next();
					conn.unhighlight();
					if (conn.getSource() != this) {
						conn.getSource().unhighlight();
					}
				}
			}
			graphModel.unhighlightNode(this);
		}
		updateFigureForModel(nodeFigure);

	}

	private void refreshLocation() {
		if (useCustomFigure) {
			if (customFigure == null || customFigure.getParent() == null) {
				return;
			}
			IGraphNode node = this;
			Point loc = node.getLocation();
			Dimension size = node.getSize();
			Rectangle bounds = new Rectangle(loc, size);
			customFigure.getParent().setConstraint(customFigure, bounds);
		} else {
			if (nodeFigure == null || nodeFigure.getParent() == null) {
				return; // node figure has not been created yet
			}
			IGraphNode node = this;
			Point loc = node.getLocation();
			Dimension size = node.getSize();
			Rectangle bounds = new Rectangle(loc, size);

			//			bounds.x = this.currentLocation.x;
			//			bounds.y = this.currentLocation.y;
			//			bounds.width = size.width;
			//			bounds.height = size.height;
			nodeFigure.getParent().setConstraint(nodeFigure, bounds);

			//nodeFigure = updateFigureForModel(nodeFigure);
		}
	}

	/**
	 * Highlights this node using the adjacent highlight color. This only does
	 * something if highlighAdjacentNodes is set to true and if the node isn't
	 * already highlighted.
	 * 
	 * @see #setHighlightAdjacentNodes(boolean)
	 */
	public void highlightAdjacent() {
		if (highlighted > 0) {
			return;
		}
		highlighted = HIGHLIGHT_ADJACENT;
		updateFigureForModel(nodeFigure);
	}

	/**
	 * Returns if the nodes adjacent to this node will be highlighted when this
	 * node is selected.
	 * 
	 * @return GraphModelNode
	 */
	public boolean isHighlightAdjacentNodes() {
		return ZestStyles.checkStyle(nodeStyle, ZestStyles.NODES_HIGHLIGHT_ADJACENT);
	}

	/**
	 * Sets if the adjacent nodes to this one should be highlighted when this
	 * node is selected.
	 * 
	 * @param highlightAdjacentNodes
	 *            The highlightAdjacentNodes to set.
	 */
	public void setHighlightAdjacentNodes(boolean highlightAdjacentNodes) {
		if (!highlightAdjacentNodes) {
			this.nodeStyle |= ZestStyles.NODES_HIGHLIGHT_ADJACENT;
			this.nodeStyle ^= ZestStyles.NODES_HIGHLIGHT_ADJACENT;
			return;
		}
		this.nodeStyle |= ZestStyles.NODES_HIGHLIGHT_ADJACENT;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int width) {
		this.borderWidth = width;
	}

	public Object getLayoutInformation() {
		return internalNode;
	}

	public void setLayoutInformation(Object layoutInformation) {
		this.internalNode = layoutInformation;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.labelSize = null;
		this.font = font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Item#setText(java.lang.String)
	 */
	public void setText(String string) {
		if (useCustomFigure) {
			return; // We don't want to set any properties if a custom figure has been set.
		}
		this.labelSize = null;
		super.setText(string);

		if (nodeFigure != null) {
			updateFigureForModel(this.nodeFigure);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Item#setImage(org.eclipse.swt.graphics.Image)
	 */
	public void setImage(Image image) {
		this.labelSize = null;
		super.setImage(image);
	}

	/**
	 * Returns the extent of the text and the image with some padding.
	 * 
	 * @return Dimension the minimum size needed to display the text and the
	 *         image
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

	/**
	 * Gets the minimum size for this node. This is the minimum size of the
	 * label (text & icon)
	 * 
	 * @return Dimension
	 */
	public Dimension calculateMinimumSize() {
		return calculateMinimumLabelSize();
	}

	/**
	 * Gets the graphModel that this node is contained in
	 * 
	 * @return The graph model that this node is contained in
	 */
	public Graph getGraphModel() {
		return this.graphModel;
	}

	private Dimension calculateTextExtents() {
		Dimension dim = new Dimension(0, 0);
		String text = getText();
		if (text != null) {
			if (font == null) {
				font = Display.getDefault().getSystemFont();
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
	 * @return the nodeStyle
	 */
	public int getNodeStyle() {
		return nodeStyle;
	}

	/**
	 * @param nodeStyle
	 *            the nodeStyle to set
	 */
	public void setNodeStyle(int nodeStyle) {
		this.nodeStyle = nodeStyle;
		this.cacheLabel = ((this.nodeStyle & ZestStyles.NODES_CACHE_LABEL) > 0) ? true : false;
	}

	public void populateLayoutConstraint(LayoutConstraint constraint) {
		graphModel.invokeConstraintAdapters(this.getData(), constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#setSize(double,
	 *      double)
	 */
	public void setSize(double width, double height) {
		if ((width != size.width) || (height != size.height)) {
			size.width = (int) width;
			size.height = (int) height;
			refreshLocation();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getBorderHighlightColor()
	 */
	public Color getBorderHighlightColor() {
		return borderHighlightColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelNode#getBorderUnhiglightColor()
	 */
	public Color getBorderUnhiglightColor() {
		return borderUnhighlightColor;
	}

	public boolean cacheLabel() {
		return this.cacheLabel;
	}

	public void setCacheLabel(boolean cacheLabel) {
		this.cacheLabel = cacheLabel;
	}

	public IFigure getNodeFigure() {
		if (useCustomFigure) {
			return customFigure;
		}
		return this.nodeFigure;
	}

	public void setCustomFigure(IFigure nodeFigure) {
		Object old = this.nodeFigure;
		useCustomFigure = true;

		this.customFigure = nodeFigure;
		this.nodeFigure = null;
		Dimension d = this.customFigure.getSize();
		setSizeInLayout(d.width, d.height);

		graphModel.changeFigure((IFigure) old, customFigure, this);

	}

	public void disableMouseOver() {

	}

	public void enableMouseOver() {

	}

	protected GraphLabel updateFigureForModel(GraphLabel figure) {
		if (useCustomFigure) {
			return figure;
		}
		IFigure toolTip;

		figure.setText(this.getText());
		figure.setIcon(getImage());

		// @tag TODO: Add border and foreground colours to highlight
		// (this.borderColor)
		if (highlighted == HIGHLIGHT_ON) {
			figure.setForegroundColor(getForegroundColor());
			figure.setBackgroundColor(getHighlightColor());
		} else if (highlighted == HIGHLIGHT_ADJACENT) {
			figure.setForegroundColor(getForegroundColor());
			figure.setBackgroundColor(getHighlightAdjacentColor());
		} else {
			figure.setForegroundColor(getForegroundColor());
			figure.setBackgroundColor(getBackgroundColor());
		}

		figure.setFont(getFont());

		Dimension d = figure.getSize();
		setSizeInLayout(d.width, d.height);

		if (this.getTooltip() == null) {
			toolTip = new Label();
			((Label) toolTip).setText(getText());
		} else {
			toolTip = this.getTooltip();
		}
		figure.setToolTip(toolTip);

		figure.addLayoutListener(LayoutAnimator.getDefault());
		return figure;
	}

	protected GraphLabel createFigureForModel() {
		IGraphNode node = this;
		boolean cacheLabel = (this).cacheLabel();
		GraphLabel label = new GraphLabel(node.getText(), node.getImage(), cacheLabel);
		return updateFigureForModel(label);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		graphModel.setItemVisible(this, visible);
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.mylar.zest.core.widgets.IGraphItem#getItemType()
	 */
	public int getItemType() {
		return NODE;
	}
}
