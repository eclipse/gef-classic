package org.eclipse.draw2d.parts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

class TabBorder 
	extends TitleBarBorder 
{
	
private Rectangle tabRect;
	
{
	setBackgroundColor( ColorConstants.button );
	setTextColor( ColorConstants.black );
	setPadding(4);
}

protected Insets calculateInsets(IFigure figure) {
	return new Insets( getTextExtents(figure).height + getPadding().getHeight() - 2, // subtracting 2 is a hack
				 0, 0, 0 );
}

public boolean containsPoint( int x, int y ){
	if( tabRect == null )
		return false;
	return tabRect.contains( x, y );
}

public Dimension getPreferredSize( IFigure fig ){
	return super.getPreferredSize(fig).expand( getPadding().getWidth(),
							       getPadding().getHeight() - 2 );
}

public void paint(IFigure figure, Graphics g, Insets insets){
	Rectangle paintRect = getPaintRectangle( figure, insets );
	Insets padding = getPadding();
	Dimension textExtents = getTextExtents( figure );
	
	Rectangle rec = paintRect.getCopy();
	rec.height = Math.min( rec.height,
			 	     textExtents.height + padding.getHeight() );
	// What should be done if the text cannot fit on the tab?
	rec.width = Math.min( rec.width,
				    textExtents.width + padding.getWidth() );
	rec.x = paintRect.x + paintRect.width - rec.width;
	setTabRectangle( rec );
	PointList list = new PointList();
	list.addPoint( rec.x, rec.y + rec.height );
	list.addPoint( rec.x, rec.y + 2 );
	list.addPoint( rec.x + 2, rec.y );
	list.addPoint( rec.x + rec.width - 3, rec.y );
	list.addPoint( rec.x + rec.width - 1, rec.y + 2 );
	list.addPoint( rec.x + rec.width - 1, rec.y + rec.height );
	
	g.setBackgroundColor( getBackgroundColor() );
	g.fillPolygon( list );
	
	int x = rec.x + padding.left;
	int y = rec.y + padding.top;
	g.setFont( getFont(figure) );
	g.setForegroundColor( getTextColor() );
	g.drawString(getLabel(), x, y);
	
	g.setForegroundColor( ColorConstants.buttonLightest );
	g.setLineStyle( Graphics.LINE_SOLID );
	PointList whiteLineList = new PointList();
	Point pt = list.getPoint(0);
	pt.y--;
	whiteLineList.addPoint( pt );
	pt = new Point( pt );
	pt.x++;
	whiteLineList.addPoint( pt );
	pt = list.getPoint(1);
	pt.x++;
	whiteLineList.addPoint( pt );
	pt = list.getPoint(2);
	pt.y++;
	whiteLineList.addPoint( pt );
	pt = list.getPoint(3);
	pt.y++;
	whiteLineList.addPoint( pt );
	g.drawPolyline( whiteLineList );
	
	g.setForegroundColor( ColorConstants.buttonDarkest );
	list.removePoint( 2 );
	list.removePoint( 1 );
	list.removePoint( 0 );
	g.drawPolyline( list );
}

private void setTabRectangle( Rectangle r ){
	tabRect = new Rectangle( r );
}

}