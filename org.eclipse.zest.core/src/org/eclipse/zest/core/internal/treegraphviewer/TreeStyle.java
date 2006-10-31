/*******************************************************************************
 * Copyright 2006, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.internal.treegraphviewer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.mylar.zest.core.IZestImageConstants;
import org.eclipse.mylar.zest.core.ZestPlugin;

/**
 * 
 * @author Ian Bull
 */
public class TreeStyle extends Toggle {
	{
		setPreferredSize(12, 12);
	}

	private PageNode node = null;

	public TreeStyle(PageNode node) {
		this.node = node;
	}

	/**
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);

		g.setBackgroundColor(ColorConstants.black);
		g.setForegroundColor(ColorConstants.black);
		Rectangle r = Rectangle.SINGLETON;
		r.setBounds(getBounds()).resize(-1, -1);

		// If the node is selected use the inverse icons
		// because they look much better
		if (node.isSelected()) {
			if (isSelected())
				g.drawImage(ZestPlugin.getDefault().getImageRegistry().get(IZestImageConstants.TREE_HANGING_INVERSE_ICON), r.x,	r.y);
			else
				g.drawImage(ZestPlugin.getDefault().getImageRegistry().get(IZestImageConstants.TREE_NORMAL_INVERSE_ICON), r.x, r.y);
		} else {
			if (isSelected())
				g.drawImage(ZestPlugin.getDefault().getImageRegistry().get(IZestImageConstants.TREE_HANGING_ICON), r.x, r.y);
			else
				g.drawImage(ZestPlugin.getDefault().getImageRegistry().get(IZestImageConstants.TREE_NORMAL_ICON), r.x, r.y);
		}

	}

	protected boolean useLocalCoordinates() {
		return true;
	}

}
