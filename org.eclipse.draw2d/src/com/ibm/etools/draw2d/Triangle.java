package com.ibm.etools.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.geometry.*;

/**
 * A triangular graphical figure.
 */
final public class Triangle
	extends Shape
	implements Orientable
{

protected int direction = NORTH;
protected int orientation = VERTICAL;

{
	setOutline(false);
}

protected PointList triangle = new PointList(4);

/**
 * Fill the Triangle with the background color
 * set by <i>g</i>.
 * 
 * @since 2.0
 */
protected void fillShape(Graphics g){
	g.fillPolygon(triangle);
}

/**
 * Draw the outline of the Triangle.
 * 
 * @since 2.0
 */
protected void outlineShape(Graphics g){
}

public void primTranslate(int dx, int dy){
	super.primTranslate(dx, dy);
	triangle.translate(dx, dy);
}

public void setDirection(int value){
	if ((value & (NORTH|SOUTH)) != 0)
		orientation = VERTICAL;
	else
		orientation = HORIZONTAL;
	direction = value;
	revalidate();
	repaint();
}

public void setOrientation(int value){
	if (orientation == VERTICAL && value == HORIZONTAL){
		if (direction == NORTH) setDirection(WEST);
		else setDirection(EAST);
	}
	if (orientation == HORIZONTAL && value == VERTICAL){
		if (direction == WEST) setDirection(NORTH);
		else setDirection(SOUTH);
	}
}

public void validate(){
	super.validate();
	Rectangle r = new Rectangle();
	r.setBounds(getBounds());
	r.crop(getInsets());
	r.resize(-1,-1);
	int size;
	switch (direction & (NORTH | SOUTH)){
		case 0: //East or west.
			size = Math.min(r.height/2, r.width);
			r.x += (r.width -size)/2;
			break;
		default: //North or south
			size = Math.min(r.height, r.width/2);
			r.y += (r.height-size+1)/2;
			break;
	}

	size = Math.max(size, 1); //Size cannot be negative

	Point head,p2,p3,p4=null;

	switch (direction){
		case NORTH:
			head = new Point(r.x+r.width/2, r.y);
			p2   = new Point (head.x-size-1, head.y+size+1);
			p3   = new Point (head.x+size+1, head.y+size+1);
			p4   = new Point (head.x, head.y-1);
			break;
		case SOUTH:
			head = new Point (r.x+r.width/2, r.y+size);
			p2   = new Point (head.x-size, head.y-size);
			p3   = new Point (head.x+size+1, head.y-size);
			p4   = new Point (head.x, head.y+1);
			break;
		case WEST:
			head = new Point (r.x, r.y+r.height/2);
			p2   = new Point (head.x+size+1, head.y-size-1);
			p3   = new Point (head.x+size+1, head.y+size+1);
			break;
		default:
			head = new Point(r.x+size+2, r.y+r.height/2);
			p2   = new Point(head.x-size-1, head.y-size-1);
			p3   = new Point(head.x-size-1, head.y+size+1);

	}
	triangle.removeAllPoints();
	triangle.addPoint(head);
	triangle.addPoint(p2);
	triangle.addPoint(p3);
	if (p4 != null)
		triangle.addPoint(p4);
}

}