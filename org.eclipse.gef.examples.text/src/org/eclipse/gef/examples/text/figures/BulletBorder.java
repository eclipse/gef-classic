/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.figures;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @since 3.1
 */
public class BulletBorder extends ListItemBorder {

private static final String BULLET = " \u25cf ";

public Insets getInsets(IFigure figure) {
	FigureUtilities.getTextExtents(BULLET, figure.getFont(), Dimension.SINGLETON);
	return new Insets(0, Dimension.SINGLETON.width, 0, 0);
}

public void paintBorder(IFigure figure, Graphics graphics, Insets insets) {
	Rectangle r = getPaintRectangle(figure, insets);
	graphics.drawString(BULLET, r.x, r.y);
}

}
