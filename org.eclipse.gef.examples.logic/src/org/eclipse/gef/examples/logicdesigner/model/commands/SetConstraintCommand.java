/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.Guide;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class SetConstraintCommand
	extends org.eclipse.gef.commands.Command
{

private Point newPos;
private Dimension newSize;
private Point oldPos;
private Dimension oldSize;
private int oldVAlignment = -2, oldHAlignment = -2, 
            newHAlignment = -2, newVAlignment = -2;
private Guide oldVGuide, oldHGuide, newVGuide, newHGuide;
private LogicSubpart part;

protected void changeGuide(Guide oldGuide, Guide newGuide, int newAlignment) {
	if (oldGuide != null && oldGuide != newGuide) {
		oldGuide.removePart(part);
	}
	if (newGuide != null) {
		newGuide.addPart(part, newAlignment);
	}
}

public void execute() {
	oldSize = part.getSize();
	oldPos  = part.getLocation();
	oldVGuide = part.getVerticalGuide();
	oldHGuide = part.getHorizontalGuide();
	if (oldVGuide != null) {
		oldVAlignment = ((Integer)oldVGuide.getMap().get(part)).intValue();
	}
	if (oldHGuide != null) {
		oldHAlignment = ((Integer)oldHGuide.getMap().get(part)).intValue();
	}

	part.setLocation(newPos);
	part.setSize(newSize);
	changeGuide(oldHGuide, newHGuide, newHAlignment);
	changeGuide(oldVGuide, newVGuide, newVAlignment);
}

public String getLabel() {
	if (oldSize.equals(newSize))
		return LogicMessages.SetLocationCommand_Label_Location;
	return LogicMessages.SetLocationCommand_Label_Resize;
}

public void redo() {
	part.setSize(newSize);
	part.setLocation(newPos);
}

public void setHorizontalGuide(Guide guide, int alignment) {
	newHGuide = guide;
	newHAlignment = alignment;
}

public void setLocation(Rectangle r) {
	setLocation(r.getLocation());
	setSize(r.getSize());
}

public void setLocation(Point p) {
	newPos = p;
}

public void setPart(LogicSubpart part) {
	this.part = part;
}

public void setSize(Dimension p) {
	newSize = p;
}

public void setVerticalGuide(Guide guide, int alignment) {
	newVGuide = guide;
	newVAlignment = alignment;
}

public void undo() {
	part.setSize(oldSize);
	part.setLocation(oldPos);
	changeGuide(newVGuide, oldVGuide, oldVAlignment);
	changeGuide(newHGuide, oldHGuide, oldHAlignment);
}

}