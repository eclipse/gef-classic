package org.eclipse.gef.internal.ui.palette.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

final class SeparatorBorder extends MarginBorder {

SeparatorBorder(int t, int l, int b, int r) {
	super(t, l, b, r);
}

public void paint(IFigure f, Graphics g, Insets i) {
	Rectangle r = getPaintRectangle(f, i);
	r.height--;
	g.setForegroundColor(ColorConstants.buttonDarker);
	g.drawLine(r.x, r.bottom(), r.right(), r.bottom());
}

}