/*******************************************************************************
 * Copyright 2005, 2024 CHISEL Group, University of Victoria, Victoria, BC,
 *                      Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.zest.layouts.exampleStructures;

import org.eclipse.zest.layouts.interfaces.ConnectionLayout;

/**
 * The SimpleRelation class describes the relationship between two objects:
 * source and destination. Each relationship has a weight and direction
 * associated with it. Note: The source object is at the beginning of the
 * relationship. Note: The destination object is at the end of the relationship.
 *
 * @version 2.0
 * @author Casey Best (version 1.0 by Jingwei Wu)
 * @author Chris Bennett
 */
public class SimpleRelationship implements ConnectionLayout {

	private static int DEFAULT_REL_LINE_WIDTH = 1;
	private static int DEFAULT_REL_LINE_WIDTH_SELECTED = DEFAULT_REL_LINE_WIDTH + 2;
	private static Object DEFAULT_RELATIONSHIP_COLOR;
	private static Object DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR;

	/** The line width for this relationship. */
	private int lineWidth = DEFAULT_REL_LINE_WIDTH;

	/** The color for this relationship. */
	private Object color = DEFAULT_RELATIONSHIP_COLOR;

	/**
	 * The sourceEntity of this SimpleRelation.
	 */
	protected SimpleNode sourceEntity;

	/**
	 * The object of this SimpleRelation.
	 */
	protected SimpleNode destinationEntity;

	/**
	 * If directional, algorithms must note the direction of the relationship. If
	 * not directional, algorithms are to ignore which direction the relationship is
	 * going. Switching the source and destination should make no difference.
	 */
	protected boolean bidirectional;

	/**
	 * The weight given to this relation.
	 */
	private double weight;

	/**
	 * Whether this relation is visible.
	 */
	private boolean visible;

	/**
	 * Constructor.
	 *
	 * @param sourceEntity      The sourceEntity of this SimpleRelation.
	 * @param destinationEntity The object of this SimpleRelation.
	 * @param bidirectional     Determines if the <code>sourceEntity</code> and
	 *                          <code>destinationEntity</code> are
	 *                          equal(exchangeable).
	 * @throws java.lang.NullPointerException If either <code>sourceEntity
	 * </code>                             or <code>destinationEntity</code> is
	 *                                        <code>null</code>.
	 */
	public SimpleRelationship(SimpleNode sourceEntity, SimpleNode destinationEntity, boolean bidirectional) {
		this(sourceEntity, destinationEntity, bidirectional, 1);
	}

	/**
	 * Constructor.
	 *
	 * @param sourceEntity      The sourceEntity of this SimpleRelation.
	 * @param destinationEntity The destinationEntity of this SimpleRelation.
	 */
	public SimpleRelationship(SimpleNode sourceEntity, SimpleNode destinationEntity, boolean bidirectional,
			double weight) {
		this.destinationEntity = destinationEntity;
		this.sourceEntity = sourceEntity;
		this.bidirectional = bidirectional;
		this.weight = weight;
		this.lineWidth = DEFAULT_REL_LINE_WIDTH;
		this.color = DEFAULT_RELATIONSHIP_COLOR;
	}

	/**
	 * Gets the sourceEntity of this SimpleRelation whether the relation is
	 * exchangeable or not.
	 *
	 * @return The sourceEntity.
	 */
	@Override
	public SimpleNode getSource() {
		return sourceEntity;
	}

	/**
	 * Gets the destinationEntity of this SimpleRelation whether the relation is
	 * exchangeable or not.
	 *
	 * @return The destinationEntity of this SimpleRelation.
	 */
	@Override
	public SimpleNode getTarget() {
		return destinationEntity;
	}

	/**
	 * If bidirectional, the direction of the relationship doesn't matter. Switching
	 * the source and destination should make no difference. If not bidirectional,
	 * layout algorithms need to take into account the direction of the
	 * relationship. The direction is based on the source and destination entities.
	 */
	@Override
	public boolean isDirected() {
		return bidirectional;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		String arrow = (isDirected() ? " <-> " : " -> "); //$NON-NLS-1$ //$NON-NLS-2$
		return "(" + sourceEntity + arrow + destinationEntity + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public int getLineWidth() {
		return this.lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void resetLineWidth() {
		this.lineWidth = DEFAULT_REL_LINE_WIDTH;
	}

	public static void setDefaultSize(int i) {
		DEFAULT_REL_LINE_WIDTH = i;
		DEFAULT_REL_LINE_WIDTH_SELECTED = DEFAULT_REL_LINE_WIDTH + 2;
	}

	public void setSelected() {
		this.color = DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR;
		this.lineWidth = DEFAULT_REL_LINE_WIDTH_SELECTED;
	}

	public void setUnSelected() {
		this.color = DEFAULT_RELATIONSHIP_COLOR;
		this.lineWidth = DEFAULT_REL_LINE_WIDTH;
	}

	public Object getColor() {
		return color;
	}

	public void setColor(Object c) {
		this.color = c;
	}

	public static void setDefaultColor(Object c) {
		DEFAULT_RELATIONSHIP_COLOR = c;
	}

	public static void setDefaultHighlightColor(Object c) {
		DEFAULT_RELATIONSHIP_HIGHLIGHT_COLOR = c;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}
}
