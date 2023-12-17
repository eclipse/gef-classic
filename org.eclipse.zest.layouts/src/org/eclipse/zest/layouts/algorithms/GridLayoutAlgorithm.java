/*******************************************************************************
 * Copyright 2005, 2023 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria - initial API and implementation
 *               Mateusz Matela
 *               Ian Bull
 *******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import java.util.Arrays;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentDimension;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.layouts.interfaces.EntityLayout;
import org.eclipse.zest.layouts.interfaces.LayoutContext;

/**
 * @version 2.0
 * @author Ian Bull
 * @author Casey Best and Rob Lintern
 * @since 2.0
 */
public class GridLayoutAlgorithm extends AbstractLayoutAlgorithm {

	private static final double PADDING_PERCENTAGE = 0.95;
	private static final int MIN_ENTITY_SIZE = 5;

	protected int rowPadding = 0;

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		throw new RuntimeException("Operation not implemented");
	}

	/**
	 * @since 2.0
	 */
	protected int rows;
	/**
	 * @since 2.0
	 */
	protected int cols;
	/**
	 * @since 2.0
	 */
	protected int numChildren;
	/**
	 * @since 2.0
	 */
	protected double colWidth;
	/**
	 * @since 2.0
	 */
	protected double rowHeight;
	/**
	 * @since 2.0
	 */
	protected double offsetX;
	/**
	 * @since 2.0
	 */
	protected double offsetY;
	/**
	 * @since 2.0
	 */
	protected double h;
	/**
	 * @since 2.0
	 */

	protected double w;
	private boolean resize = false;
	private int totalProgress;
	private LayoutContext context;

	/**
	 * Initializes the grid layout.
	 *
	 * @param styles
	 * @see LayoutStyles
	 * @deprecated Since Zest 2.0, use {@link #GridLayoutAlgorithm()}.
	 */
	@Deprecated(forRemoval = true)
	public GridLayoutAlgorithm(int styles) {
		super(styles);
		setResizing(styles != LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

	/**
	 * Inititalizes the grid layout with no style.
	 */
	public GridLayoutAlgorithm() {
		this(LayoutStyles.NONE);
	}

	@Override
	protected int getCurrentLayoutStep() {
		// TODO: This isn't right
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return totalProgress;
	}

	/**
	 *
	 */
	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
			double x, double y, double width, double height) {

		// TODO: Filter unwanted entities and relationships
		// super.applyLayout (entitiesToLayout, relationshipsToConsider, boundsX,
		// boundsY, boundsWidth, boundsHeight);
		// now begin
		numChildren = entitiesToLayout.length;
		if (numChildren < 1)
			return;

		int[] colsAndRows = calculateNumberOfRowsAndCols(numChildren, x, y, width, height);
		cols = colsAndRows[0];
		rows = colsAndRows[1];

		totalProgress = rows + 2;
		fireProgressEvent(1, totalProgress);

		// sort the entities
		if (comparator != null) {
			Arrays.sort(entitiesToLayout, comparator);
		} else {
			Arrays.sort(entitiesToLayout);
		}
		fireProgressEvent(2, totalProgress);

		// Calculate row height and column width
		colWidth = width / cols;
		rowHeight = height / rows;

		// Calculate amount to scale children
		double[] nodeSize = calculateNodeSize(colWidth, rowHeight);
		w = nodeSize[0];
		h = nodeSize[1];
		offsetX = (colWidth - w) / 2.0; // half of the space between columns
		offsetY = (rowHeight - h) / 2.0; // half of the space between rows
	}

	/**
	 * Use this algorithm to layout the given entities, using the given
	 * relationships and bounds. The entities will be placed in the same order as
	 * they are passed in, unless a comparator is supplied.
	 *
	 * @param entitiesToLayout        Apply the algorithm to these entities
	 * @param relationshipsToConsider Only consider these relationships when
	 *                                applying the algorithm.
	 * @param boundsX                 The left side of the bounds in which the
	 *                                layout can place the entities.
	 * @param boundsY                 The top side of the bounds in which the layout
	 *                                can place the entities.
	 * @param boundsWidth             The width of the bounds in which the layout
	 *                                can place the entities.
	 * @param boundsHeight            The height of the bounds in which the layout
	 *                                can place the entities.
	 * @throws RuntimeException Thrown if entitiesToLayout doesn't contain all of
	 *                          the endpoints for each relationship in
	 *                          relationshipsToConsider
	 */
	@Override
	protected synchronized void applyLayoutInternal(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider, double boundsX, double boundsY, double boundsWidth,
			double boundsHeight) {

		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ((i * cols + j) < numChildren) {
					// find new position for child
					double xmove = boundsX + j * colWidth + offsetX;
					double ymove = boundsY + i * rowHeight + offsetY;
					InternalNode sn = entitiesToLayout[index++];
					sn.setInternalLocation(xmove, ymove);
					sn.setInternalSize(Math.max(w, MIN_ENTITY_SIZE), Math.max(h, MIN_ENTITY_SIZE));
				}
			}
			fireProgressEvent(2 + i, totalProgress);
		}
		updateLayoutLocations(entitiesToLayout);
		fireProgressEvent(totalProgress, totalProgress);
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {

	}

	/**
	 * Calculates and returns an array containing the number of columns, followed by
	 * the number of rows
	 */
	protected int[] calculateNumberOfRowsAndCols(int numChildren, double boundX, double boundY, double boundWidth,
			double boundHeight) {
		if (getEntityAspectRatio() == 1.0) {
			return calculateNumberOfRowsAndCols_square(numChildren, boundX, boundY, boundWidth, boundHeight);
		} else {
			return calculateNumberOfRowsAndCols_rectangular(numChildren);
		}
	}

	protected int[] calculateNumberOfRowsAndCols_square(int numChildren, double boundX, double boundY,
			double boundWidth, double boundHeight) {
		int rows = Math.max(1, (int) Math.sqrt(numChildren * boundHeight / boundWidth));
		int cols = Math.max(1, (int) Math.sqrt(numChildren * boundWidth / boundHeight));

		// if space is taller than wide, adjust rows first
		if (boundWidth <= boundHeight) {
			// decrease number of rows and columns until just enough or not enough
			while (rows * cols > numChildren) {
				if (rows > 1)
					rows--;
				if (rows * cols > numChildren)
					if (cols > 1)
						cols--;
			}
			// increase number of rows and columns until just enough
			while (rows * cols < numChildren) {
				rows++;
				if (rows * cols < numChildren)
					cols++;
			}
		} else {
			// decrease number of rows and columns until just enough or not enough
			while (rows * cols > numChildren) {
				if (cols > 1)
					cols--;
				if (rows * cols > numChildren)
					if (rows > 1)
						rows--;
			}
			// increase number of rows and columns until just enough
			while (rows * cols < numChildren) {
				cols++;
				if (rows * cols < numChildren)
					rows++;
			}
		}
		int[] result = { cols, rows };
		return result;
	}

	protected int[] calculateNumberOfRowsAndCols_rectangular(int numChildren) {
		int rows = Math.max(1, (int) Math.ceil(Math.sqrt(numChildren)));
		int cols = Math.max(1, (int) Math.ceil(Math.sqrt(numChildren)));
		int[] result = { cols, rows };
		return result;
	}

	protected double[] calculateNodeSize(double colWidth, double rowHeight) {
		double childW = Math.max(MIN_ENTITY_SIZE, PADDING_PERCENTAGE * colWidth);
		double childH = Math.max(MIN_ENTITY_SIZE, PADDING_PERCENTAGE * (rowHeight - rowPadding));
		double whRatio = colWidth / rowHeight;
		if (whRatio < getEntityAspectRatio()) {
			childH = childW / getEntityAspectRatio();
		} else {
			childW = childH * getEntityAspectRatio();
		}
		double[] result = { childW, childH };
		return result;
	}

	/**
	 * Increases the padding between rows in the grid
	 *
	 * @param rowPadding Value will not be set if less than 0.
	 */
	public void setRowPadding(int rowPadding) {
		if (rowPadding < 0) {
			return;
		}
		this.rowPadding = rowPadding;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continueous) {
		if (asynchronous && continueous)
			return false;
		else if (asynchronous && !continueous)
			return true;
		else if (!asynchronous && continueous)
			return false;
		else if (!asynchronous && !continueous)
			return true;

		return false;
	}

	/**
	 *
	 * @return true if this algorithm is set to resize elements
	 * @since 2.0
	 */
	public boolean isResizing() {
		return resize;
	}

	/**
	 *
	 * @param resizing true if this algorithm should resize elements (default is
	 *                 false)
	 * @since 2.0
	 */
	public void setResizing(boolean resizing) {
		resize = resizing;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setLayoutContext(LayoutContext context) {
		this.context = context;
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void applyLayout(boolean clean) {
		if (!clean) {
			return;
		}
		DisplayIndependentRectangle bounds = context.getBounds();
		calculateGrid(bounds);
		applyLayoutInternal(context.getEntities(), bounds);
	}

	/**
	 * Calculates all the dimensions of grid that layout entities will be fit in.
	 * The following fields are set by this method: {@link #numChildren},
	 * {@link #rows}, {@link #cols}, {@link #colWidth}, {@link #rowHeight},
	 * {@link #offsetX}, {@link #offsetY}
	 *
	 * @param bounds
	 * @since 2.0
	 */
	protected void calculateGrid(DisplayIndependentRectangle bounds) {
		numChildren = context.getNodes().length;
		int[] result = calculateNumberOfRowsAndCols(numChildren, bounds.x, bounds.y, bounds.width, bounds.height);
		cols = result[0];
		rows = result[1];

		colWidth = bounds.width / cols;
		rowHeight = bounds.height / rows;

		double[] nodeSize = calculateNodeSize(colWidth, rowHeight);
		w = nodeSize[0];
		h = nodeSize[1];
		offsetX = (colWidth - w) / 2.0; // half of the space between
										// columns
		offsetY = (rowHeight - h) / 2.0; // half of the space
											// between rows
	}

	/**
	 * Use this algorithm to layout the given entities and bounds. The entities will
	 * be placed in the same order as they are passed in, unless a comparator is
	 * supplied.
	 *
	 * @param entitiesToLayout apply the algorithm to these entities
	 * @param bounds           the bounds in which the layout can place the
	 *                         entities.
	 * @since 2.0
	 */
	protected synchronized void applyLayoutInternal(EntityLayout[] entitiesToLayout,
			DisplayIndependentRectangle bounds) {

		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if ((i * cols + j) < numChildren) {
					EntityLayout node = entitiesToLayout[index];
					index++;
					if (resize && node.isResizable()) {
						node.setSize(Math.max(w, MIN_ENTITY_SIZE), Math.max(h, MIN_ENTITY_SIZE));
					}
					DisplayIndependentDimension size = node.getSize();
					double xmove = bounds.x + j * colWidth + offsetX + size.width / 2;
					double ymove = bounds.y + i * rowHeight + offsetY + size.height / 2;
					if (node.isMovable()) {
						node.setLocation(xmove, ymove);
					}
				}
			}
		}
	}

}
