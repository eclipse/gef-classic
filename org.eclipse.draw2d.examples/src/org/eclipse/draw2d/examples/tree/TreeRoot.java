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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Transposer;

/**
 *
 * @author hudsonr Created on Apr 23, 2003
 */
public class TreeRoot extends TreeBranch {

	private int major = 10;
	private int minor = 10;
	private final Transposer transposer = new Transposer();
	private boolean compression;

	/**
	 * @param title
	 */
	public TreeRoot(IFigure title) {
		super(title);
	}

	/**
	 * @param title
	 * @param style
	 */
	public TreeRoot(IFigure title, int style) {
		super(title, style);
	}

	public int getMajorSpacing() {
		return major;
	}

	public int getMinorSpacing() {
		return minor;
	}

	@Override
	public TreeRoot getRoot() {
		return this;
	}

	public Transposer getTransposer() {
		return transposer;
	}

	public boolean isHorizontal() {
		return !transposer.isEnabled();
	}

	/**
	 * sets the space (in pixels) between this branch's node and its subtrees.
	 */
	public void setMajorSpacing(int value) {
		this.major = value;
		invalidateTree();
		revalidate();
	}

	public void setHorizontal(boolean value) {
		transposer.setEnabled(!value);
		invalidateTree();
		revalidate();
	}

	/**
	 * @param i
	 */
	public void setMinorSpacing(int i) {
		minor = i;
		invalidateTree();
		revalidate();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#validate()
	 */
	@Override
	public void validate() {
		if (isValid()) {
			return;
		}
		setRowHeights(getPreferredRowHeights(), 0);
		super.validate();
	}

	public boolean isCompressed() {
		return compression;
	}

	/**
	 * @param b
	 */
	public void setCompression(boolean b) {
		compression = b;
	}

}
