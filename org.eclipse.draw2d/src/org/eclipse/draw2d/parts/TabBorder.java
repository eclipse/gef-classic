/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

class TabBorder 
	extends TitleBarBorder 
{
	
private Rectangle tabRect;
	
{
	setBackgroundColor(ColorConstants.button);
	setTextColor(ColorConstants.black);
	setPadding(4);
}

protected Insets calculateInsets(IFigure figure) {
	// subtracting 2 from the padding height is a hack
	return 
		new Insets(getTextExtents(figure).height + getPadding().getHeight() - 2, 0, 0, 0);
}

public boolean containsPoint(int x, int y) {
	if (tabRect == null)
		return false;
	return tabRect.contains(x, y);
}

public Dimension getPreferredSize(IFigure fig) {
	return super.getPreferredSize(fig).expand(getPadding().getWidth(),
							       getPadding().getHeight() - 2);
}

public void paint(IFigure figure, Graphics g, Insets insets) {
	Rectangle paintRect = getPaintRectangle(figure, insets);
	Insets padding = getPadding();
	Dimension textExtents = getTextExtents(figure);
	
	Rectangle rec = paintRect.getCopy();
	rec.height = Math.min(rec.height,
			 	     textExtents.height + padding.getHeight());
	// What should be done if the text cannot fit on the tab?
	rec.width = Math.min(rec.width,
				    textExtents.width + padding.getWidth());
	rec.x = paintRect.x + paintRect.width - rec.width;
	setTabRectangle(rec);
	PointList list = new PointList();
	list.addPoint(rec.x, rec.y + rec.height);
	list.addPoint(rec.x, rec.y + 2);
	list.addPoint(rec.x + 2, rec.y);
	list.addPoint(rec.x + rec.width - 3, rec.y);
	list.addPoint(rec.x + rec.width - 1, rec.y + 2);
	list.addPoint(rec.x + rec.width - 1, rec.y + rec.height);
	
	g.setBackgroundColor(getBackgroundColor());
	g.fillPolygon(list);
	
	int x = rec.x + padding.left;
	int y = rec.y + padding.top;
	g.setFont(getFont(figure));
	g.setForegroundColor(getTextColor());
	g.drawString(getLabel(), x, y);
	
	g.setForegroundColor(ColorConstants.buttonLightest);
	g.setLineStyle(Graphics.LINE_SOLID);
	PointList whiteLineList = new PointList();
	Point pt = list.getPoint(0);
	pt.y--;
	whiteLineList.addPoint(pt);
	pt = new Point(pt);
	pt.x++;
	whiteLineList.addPoint(pt);
	pt = list.getPoint(1);
	pt.x++;
	whiteLineList.addPoint(pt);
	pt = list.getPoint(2);
	pt.y++;
	whiteLineList.addPoint(pt);
	pt = list.getPoint(3);
	pt.y++;
	whiteLineList.addPoint(pt);
	g.drawPolyline(whiteLineList);
	
	g.setForegroundColor(ColorConstants.buttonDarkest);
	list.removePoint(2);
	list.removePoint(1);
	list.removePoint(0);
	g.drawPolyline(list);
}

private void setTabRectangle(Rectangle r) {
	tabRect = new Rectangle(r);
}

}
