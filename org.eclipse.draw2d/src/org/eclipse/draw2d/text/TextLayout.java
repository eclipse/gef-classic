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

import java.util.List;

/**
 * @author hudsonr
 * @since 2.1
 */
public abstract class TextLayout extends FlowFigureLayout {

	/**
	 * Creates a new TextLayout with the given TextFlow
	 *
	 * @param flow The TextFlow
	 */
	public TextLayout(TextFlow flow) {
		super(flow);
	}

	/**
	 * Reuses an existing <code>TextFragmentBox</code>, or creates a new one.
	 *
	 * @param i         the index
	 * @param fragments the original list of fragments
	 * @return a TextFragmentBox
	 */
	protected TextFragmentBox getFragment(int i, List<TextFragmentBox> fragments) {
		if (fragments.size() > i) {
			return fragments.get(i);
		}
		TextFragmentBox box = new TextFragmentBox((TextFlow) getFlowFigure());
		fragments.add(box);
		return box;
	}

}
