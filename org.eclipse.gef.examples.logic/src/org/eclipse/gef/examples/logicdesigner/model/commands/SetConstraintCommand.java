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
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class SetConstraintCommand
	extends org.eclipse.gef.commands.Command
{

private Point newPos;
private Dimension newSize;
private Point oldPos;
private Dimension oldSize;
private GuideChange hGuideChange = new GuideChange();
private GuideChange vGuideChange = new GuideChange();
private LogicSubpart part;

protected void changeGuide(LogicGuide oldGuide, LogicGuide newGuide, int newAlignment) {
	if (oldGuide != null && oldGuide != newGuide) {
		oldGuide.detachPart(part);
	}
	if (newGuide != null) {
		newGuide.attachPart(part, newAlignment);
	}
}

public void clearHorizontalGuide() {
	hGuideChange.changeGuide = true;
}

public void clearVerticalGuide() {
	vGuideChange.changeGuide = true;
}

public void execute() {
	oldSize = part.getSize();
	oldPos  = part.getLocation();
	part.setLocation(newPos);
	part.setSize(newSize);
	
	vGuideChange.oldGuide = part.getVerticalGuide();
	hGuideChange.oldGuide = part.getHorizontalGuide();
	
	if (hGuideChange.oldGuide != null)
		hGuideChange.oldAlign = ((Integer)hGuideChange.oldGuide.getMap().get(part)).intValue();
	
	if (vGuideChange.oldGuide != null) {
		vGuideChange.oldAlign = ((Integer)vGuideChange.oldGuide.getMap().get(part)).intValue();
	}
	
	if (vGuideChange.changeGuide) {
		changeGuide(vGuideChange.oldGuide, vGuideChange.newGuide, vGuideChange.newAlign);
		vGuideChange.changeGuide = false;
	}
	
	if (hGuideChange.changeGuide) { 
		changeGuide(hGuideChange.oldGuide, hGuideChange.newGuide, hGuideChange.newAlign);
		hGuideChange.changeGuide = false;
	}
	
}

public String getLabel() {
	if (oldSize.equals(newSize))
		return LogicMessages.SetLocationCommand_Label_Location;
	return LogicMessages.SetLocationCommand_Label_Resize;
}

public void redo() {
	part.setSize(newSize);
	part.setLocation(newPos);
	changeGuide(vGuideChange.oldGuide, vGuideChange.newGuide, vGuideChange.newAlign);
	changeGuide(hGuideChange.oldGuide, hGuideChange.newGuide, hGuideChange.newAlign);
}

public void setHorizontalGuide(LogicGuide guide, int alignment) {
	hGuideChange.newGuide = guide;
	hGuideChange.newAlign = alignment;
	hGuideChange.changeGuide = true;
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

public void setVerticalGuide(LogicGuide guide, int alignment) {
	vGuideChange.newGuide = guide;
	vGuideChange.newAlign = alignment;
	vGuideChange.changeGuide = true;
}

public void undo() {
	part.setSize(oldSize);
	part.setLocation(oldPos);
	changeGuide(vGuideChange.newGuide, vGuideChange.oldGuide, vGuideChange.oldAlign);
	changeGuide(hGuideChange.newGuide, hGuideChange.oldGuide, hGuideChange.oldAlign);
}

class GuideChange {
	protected LogicGuide oldGuide, newGuide;
	protected int oldAlign, newAlign;	
	protected boolean changeGuide;
}

}