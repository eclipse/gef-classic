package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.geometry.Rectangle;

public class FreeformLayer
	extends Layer
	implements FreeformFigure
{

private FreeformHelper helper = new FreeformHelper(this);

private FigureListener listener = new FigureListener() {
	public void figureMoved(IFigure source) {
		revalidate();
	}
};

public void add(IFigure figure, Object constraint, int index){
	super.add(figure, constraint, index);
	figure.addFigureListener(listener);
}

protected void fireMoved(){}

public void primTranslate(int dx, int dy){
	bounds.x += dx;
	bounds.y += dy;
}

public void remove(IFigure fig){
	fig.removeFigureListener(listener);
	super.remove(fig);
}

public void updateFreeformBounds(Rectangle union){
	setBounds(helper.updateFreeformBounds(union));
}

}
