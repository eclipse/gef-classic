package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.List;
import org.eclipse.draw2d.geometry.*;

public class FreeformLayeredPane
	extends LayeredPane
	implements FreeformFigure
{
	
private FreeformHelper helper = new FreeformHelper(this);

public FreeformLayeredPane(){
	setLayoutManager(null);
}

public void add(IFigure figure,Object constraint, int index){
//	figure.setBorder(new GroupBoxBorder(constraint.toString()));
	super.add(figure, constraint,index);
}

protected void fireMoved(){}

protected void primTranslate(int dx, int dy){
	bounds.x += dx;
	bounds.y += dy;
}

public void updateFreeformBounds(Rectangle union){
	setBounds(helper.updateFreeformBounds(union));
}

public void validate(){
	if (isValid())
		return;
	super.validate();
}

}
