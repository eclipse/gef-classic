package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import com.ibm.etools.gef.commands.*;
import com.ibm.etools.gef.examples.logicdesigner.LogicResources;

import com.ibm.etools.draw2d.geometry.*;

public class OrphanChildCommand
	extends AbstractCommand
{

private LogicDiagram diagram;
private LogicSubpart child;
private int index;

public OrphanChildCommand () {
	super(LogicResources.getString("OrphanChildCommand.Label")); //$NON-NLS-1$
}

public void execute() {
	List children = diagram.getChildren();
	index = children.indexOf(child);
	diagram.removeChild(child);
}

public void redo() {
	diagram.removeChild(child);
}

public void setChild(LogicSubpart child) {
	this.child = child;
}

public void setParent(LogicDiagram parent) { 
	diagram = parent;
}

public void undo() {
	diagram.addChild(child,index);
}

}
