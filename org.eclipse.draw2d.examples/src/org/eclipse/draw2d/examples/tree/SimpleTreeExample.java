/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
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
package org.eclipse.draw2d.examples.tree;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.examples.AbstractExample;

/**
 * @author hudsonr Created on Apr 18, 2003
 */
public class SimpleTreeExample extends AbstractExample {

	public static void main(String[] args) {
		new SimpleTreeExample().run();
	}

	/**
	 * @see org.eclipse.draw2d.examples.AbstractExample#createContents()
	 */
	@Override
	protected IFigure createContents() {
		getFigureCanvas().setBackground(ColorConstants.white);
		TreeRoot root = new TreeRoot(new PageNode("Graph Root")); //$NON-NLS-1$
		root.setAlignment(PositionConstants.LEFT);

		TreeBranch branch = new TreeBranch(new PageNode("Child 1")); //$NON-NLS-1$
		root.addBranch(branch);

		branch = new TreeBranch(new PageNode("Child 2"), TreeBranch.STYLE_HANGING); //$NON-NLS-1$
		root.addBranch(branch);
		branch.addBranch(new TreeBranch(new PageNode("Child 1"))); //$NON-NLS-1$

		return root;
	}

}
