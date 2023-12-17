/*******************************************************************************
 * Copyright 2006, 2023 CHISEL Group, University of Victoria, Victoria, BC, Canada.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: The Chisel Group - initial API and implementation
 *               Mateusz Matela
 *               Ian Bull
 ******************************************************************************/
package org.eclipse.zest.layouts.algorithms;

import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
import org.eclipse.zest.layouts.interfaces.LayoutContext;

public class CompositeLayoutAlgorithm extends AbstractLayoutAlgorithm {

	private LayoutAlgorithm[] algorithms = null;

	public CompositeLayoutAlgorithm(int styles, LayoutAlgorithm[] algoirthms) {
		super(styles);
		this.algorithms = algoirthms;
	}

	/**
	 * @deprecated Since Zest 2.0, use
	 *             {@link #CompositeLayoutAlgorithm(LayoutAlgorithm[])}
	 */
	@Deprecated(forRemoval = true)
	public CompositeLayoutAlgorithm(LayoutAlgorithm[] algoirthms) {
		this(0, algoirthms);
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void applyLayout(boolean clean) {
		for (LayoutAlgorithm algorithm : algorithms) {
			algorithm.applyLayout(clean);
		}
	}

	/**
	 * @since 2.0
	 */
	@Override
	public void setLayoutContext(LayoutContext context) {
		for (LayoutAlgorithm algorithm : algorithms) {
			algorithm.setLayoutContext(context);
		}
	}

	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
			double boundsX, double boundsY, double boundsWidth, double boundsHeight) {

		for (LayoutAlgorithm algorithm : algorithms) {
			try {
				algorithm.applyLayout(entitiesToLayout, relationshipsToConsider, boundsX, boundsY, boundsWidth,
						boundsHeight, this.internalAsynchronous, this.internalContinuous);
			} catch (InvalidLayoutConfiguration e) {
				e.printStackTrace();
			}
		}
		for (InternalNode element : entitiesToLayout) {
			element.getLayoutEntity().setLocationInLayout(element.getXInLayout(), element.getYInLayout());
		}

		// updateLayoutLocations(entitiesToLayout);
	}

	@Override
	protected int getCurrentLayoutStep() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout,
			InternalRelationship[] relationshipsToConsider) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
			double x, double y, double width, double height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		// TODO Auto-generated method stub

	}

}
