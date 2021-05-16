/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2dl.text;

import java.util.List;

import org.eclipse.draw2dl.Figure;
import org.eclipse.draw2dl.IFigure;
import org.eclipse.draw2dl.geometry.Dimension;
import org.eclipse.draw2dl.geometry.Rectangle;

/**
 * The root of a Flow hierarchy. A flow page can be treated as a normal figure,
 * but contains FlowFigures.
 * <P>
 * A FlowPage will not have a defined width unless it is inside a figure whose
 * layout provides width hints when calling
 * {@link IFigure#getPreferredSize(int, int)}.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 */
public class FlowPage extends org.eclipse.draw2dl.text.BlockFlow {

	private Dimension pageSize = new Dimension();
	private int recommendedWidth;
	private int pageSizeCacheKeys[] = new int[3];
	private Dimension pageSizeCacheValues[] = new Dimension[3];

	/**
	 * @see Figure#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		setValid(false);
	}

	/**
	 * @see org.eclipse.draw2dl.text.BlockFlow#createDefaultFlowLayout()
	 */
	protected FlowFigureLayout createDefaultFlowLayout() {
		return new PageFlowLayout(this);
	}

	/**
	 * @see Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int w, int h) {
		return getPreferredSize(w, h);
	}

	/**
	 * @see Figure#invalidate()
	 */
	public void invalidate() {
		pageSizeCacheValues = new Dimension[3];
		super.invalidate();
	}

	/**
	 * @see Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int width, int h) {
		for (int i = 0; i < 3; i++) {
			if (pageSizeCacheKeys[i] == width && pageSizeCacheValues[i] != null)
				return pageSizeCacheValues[i];
		}

		pageSizeCacheKeys[2] = pageSizeCacheKeys[1];
		pageSizeCacheKeys[1] = pageSizeCacheKeys[0];
		pageSizeCacheKeys[0] = width;

		pageSizeCacheValues[2] = pageSizeCacheValues[1];
		pageSizeCacheValues[1] = pageSizeCacheValues[0];

		// Flowpage must temporarily layout to determine its preferred size
		int oldWidth = getPageWidth();
		setPageWidth(width);
		validate();
		pageSizeCacheValues[0] = pageSize.getCopy();

		if (width != oldWidth) {
			setPageWidth(oldWidth);
			getUpdateManager().addInvalidFigure(this);
		}
		return pageSizeCacheValues[0];
	}

	int getPageWidth() {
		return recommendedWidth;
	}

	/**
	 * @see BlockFlow#postValidate()
	 */
	public void postValidate() {
		Rectangle r = getBlockBox().toRectangle();
		pageSize.width = r.width;
		pageSize.height = r.height;
		List<IFigure> v = getChildren();
		for (IFigure iFigure : v) ((FlowFigure) iFigure).postValidate();
	}

	/**
	 * Overridden to set valid.
	 * 
	 * @see IFigure#removeNotify()
	 */
	public void removeNotify() {
		super.removeNotify();
		setValid(true);
	}

	/**
	 * @see FlowFigure#setBounds(Rectangle)
	 */
	public void setBounds(Rectangle r) {
		if (getBounds().equals(r))
			return;
		boolean invalidate = getBounds().width != r.width
				|| getBounds().height != r.height;
		super.setBounds(r);
		int newWidth = r.width;
		if (invalidate || getPageWidth() != newWidth) {
			setPageWidth(newWidth);
			getUpdateManager().addInvalidFigure(this);
		}
	}

	private void setPageWidth(int width) {
		if (recommendedWidth == width)
			return;
		recommendedWidth = width;
		super.invalidate();
	}

	/**
	 * @see Figure#validate()
	 */
	public void validate() {
		if (isValid())
			return;
		super.validate();
		postValidate();
	}

}
