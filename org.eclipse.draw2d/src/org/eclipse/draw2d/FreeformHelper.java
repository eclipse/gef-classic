package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;

import org.eclipse.draw2d.geometry.*;

public class FreeformHelper {

private FreeformFigure host;

FreeformHelper(FreeformFigure host){
	this.host = host;
}

public Rectangle updateFreeformBounds(Rectangle union){
	Rectangle newBounds = null;
	List children = host.getChildren();
	for (int i=0; i<children.size(); i++){
		IFigure child = (IFigure)children.get(i);
		if (child instanceof FreeformFigure)
			 ((FreeformFigure) child).updateFreeformBounds(union);
		Rectangle r = child.getBounds();
		if (newBounds == null)
			newBounds = r.getCopy();
		else
			newBounds.union(r);
	}
	Insets insets = host.getInsets();
	if (newBounds == null)
		newBounds = new Rectangle(0,0,insets.getWidth(),insets.getHeight());
	else {
		newBounds.expand(insets);
		newBounds.union(0, 0);
	}
	newBounds.union(union);
	return newBounds;
}

}
