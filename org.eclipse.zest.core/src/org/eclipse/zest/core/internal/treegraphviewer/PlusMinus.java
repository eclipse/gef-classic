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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This was originally a Draw2D Snippet that has been modified for Zest. All bugs 
 * in this should be opened against the Zest project.
 * 
 * @author hudsonr Created on Apr 22, 2003
 * @author Ian Bull
 */
public class PlusMinus extends Toggle {

	{
		setPreferredSize(9, 9);
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
		g.drawRectangle(r);
		int xMid = r.x + r.width / 2;
		int yMid = r.y + r.height / 2;
		g.drawLine(r.x + 2, yMid, r.right() - 2, yMid);
		if (!isSelected()) {
			g.drawLine(xMid, r.y + 2, xMid, r.bottom() - 2);
		}
	}

	
	

}
