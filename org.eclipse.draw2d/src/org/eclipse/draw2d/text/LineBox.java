/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
public abstract class LineBox extends CompositeBox {

	/**
	 * The maximum ascent of all contained fragments.
	 */
	int contentAscent;

	/**
	 * The maximum descent of all contained fragments.
	 */
	int contentDescent;

	List<FlowBox> fragments = new ArrayList<>();

	/**
	 * @see org.eclipse.draw2d.text.CompositeBox#add(org.eclipse.draw2d.text.FlowBox)
	 */
	@Override
	public void add(FlowBox child) {
		fragments.add(child);
		width += child.getWidth();
		contentAscent = Math.max(contentAscent, child.getOuterAscent());
		contentDescent = Math.max(contentDescent, child.getOuterDescent());
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBox#getAscent()
	 */
	@Override
	public int getAscent() {
		int ascent = 0;
		for (FlowBox fragment : fragments) {
			ascent = Math.max(ascent, fragment.getAscent());
		}
		return ascent;
	}

	/**
	 * Returns the remaining width available for line content.
	 *
	 * @return the available width in pixels
	 */
	int getAvailableWidth() {
		if (recommendedWidth < 0) {
			return Integer.MAX_VALUE;
		}
		return recommendedWidth - getWidth();
	}

	@Override
	int getBottomMargin() {
		return 0;
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBox#getDescent()
	 */
	@Override
	public int getDescent() {
		int descent = 0;
		for (FlowBox fragment : fragments) {
			descent = Math.max(descent, fragment.getDescent());
		}
		return descent;
	}

	/**
	 * @return Returns the fragments.
	 */
	List<FlowBox> getFragments() {
		return fragments;
	}

	@Override
	int getTopMargin() {
		return 0;
	}

	/**
	 * @return <code>true</code> if this box contains any fragments
	 */
	public boolean isOccupied() {
		return !fragments.isEmpty();
	}

	/**
	 * @see org.eclipse.draw2d.text.FlowBox#requiresBidi()
	 */
	@Override
	public boolean requiresBidi() {
		return getFragments().stream().anyMatch(FlowBox::requiresBidi);
	}

}
