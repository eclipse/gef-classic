/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
package org.eclipse.zest.internal.dot;

import java.util.Comparator;
import java.util.List;

import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.progress.ProgressListener;

/**
 * Purely forwarding layout algorithm implementation acting as a dummy custom
 * layout to test support for custom layouts in the transformation.
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class CustomLayoutAlgorithm implements LayoutAlgorithm {
	private LayoutAlgorithm algorithm = new TreeLayoutAlgorithm();

	/**
	 * @param noLayoutNodeResizing
	 *            The LayoutStyles constant
	 */
	public CustomLayoutAlgorithm(final int noLayoutNodeResizing) {
		algorithm = new TreeLayoutAlgorithm(noLayoutNodeResizing);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#addEntity(org.eclipse.zest.layouts.LayoutEntity)
	 */
	public void addEntity(final LayoutEntity entity) {
		algorithm.addEntity(entity);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#addProgressListener(org.eclipse.zest.layouts.progress.ProgressListener)
	 */
	public void addProgressListener(final ProgressListener listener) {
		algorithm.addProgressListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#addRelationship(org.eclipse.zest.layouts.LayoutRelationship)
	 */
	public void addRelationship(final LayoutRelationship relationship) {
		algorithm.addRelationship(relationship);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#applyLayout(org.eclipse.zest.layouts.LayoutEntity[],
	 *      org.eclipse.zest.layouts.LayoutRelationship[], double, double,
	 *      double, double, boolean, boolean)
	 */
	public void applyLayout(final LayoutEntity[] entitiesToLayout,
			final LayoutRelationship[] relationshipsToConsider, final double x,
			final double y, final double width, final double height,
			final boolean asynchronous, final boolean continuous)
			throws InvalidLayoutConfiguration {
		algorithm.applyLayout(entitiesToLayout, relationshipsToConsider, x, y,
				width, height, asynchronous, continuous);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#getEntityAspectRatio()
	 */
	public double getEntityAspectRatio() {
		return algorithm.getEntityAspectRatio();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#getStyle()
	 */
	public int getStyle() {
		return algorithm.getStyle();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#isRunning()
	 */
	public boolean isRunning() {
		return algorithm.isRunning();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#removeEntity(org.eclipse.zest.layouts.LayoutEntity)
	 */
	public void removeEntity(final LayoutEntity entity) {
		algorithm.removeEntity(entity);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#removeProgressListener(org.eclipse.zest.layouts.progress.ProgressListener)
	 */
	public void removeProgressListener(final ProgressListener listener) {
		algorithm.removeProgressListener(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#removeRelationship(org.eclipse.zest.layouts.LayoutRelationship)
	 */
	public void removeRelationship(final LayoutRelationship relationship) {
		algorithm.removeRelationship(relationship);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#removeRelationships(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	// The API demands raw type
	public void removeRelationships(final List relationships) {
		algorithm.removeRelationships(relationships);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#setComparator(java.util.Comparator)
	 */
	@SuppressWarnings("unchecked")
	// The API demands raw type
	public void setComparator(final Comparator comparator) {
		algorithm.setComparator(comparator);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#setEntityAspectRatio(double)
	 */
	public void setEntityAspectRatio(final double ratio) {
		algorithm.setEntityAspectRatio(ratio);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#setFilter(org.eclipse.zest.layouts.Filter)
	 */
	public void setFilter(final Filter filter) {
		algorithm.setFilter(filter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#setStyle(int)
	 */
	public void setStyle(final int style) {
		algorithm.setStyle(style);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.zest.layouts.LayoutAlgorithm#stop()
	 */
	public void stop() {
		algorithm.stop();
	}

}
