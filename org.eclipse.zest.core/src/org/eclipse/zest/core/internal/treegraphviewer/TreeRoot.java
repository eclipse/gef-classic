/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.treegraphviewer;

import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.geometry.Transposer;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 23, 2003
 * @author Ian Bull
 */
public class TreeRoot extends TreeBranch {

	private int major = 10;
	private int minor = 10;
	private Transposer transposer = new Transposer();

	private boolean compression;

	public TreeRoot( String text ) {
		this(new PageNode(text));
	}
	
	/**
	 * @param title
	 */
	public TreeRoot(PageNode title) {
		super(title);
		this.addLayoutListener(LayoutAnimator.getDefault());
	}

	/**
	 * @param title
	 * @param style
	 */
	public TreeRoot(PageNode title, int style) {
		super(title, style);
	}

	public int getMajorSpacing() {
		return major;
	}

	/**
	 * @return
	 */
	public int getMinorSpacing() {
		return minor;
	}

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
	public void validate() {
		if (isValid())
			return;
		setRowHeights(getPreferredRowHeights(), 0);
		super.validate();
	}

	/**
	 * @return
	 */
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
