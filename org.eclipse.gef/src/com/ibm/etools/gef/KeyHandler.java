package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.KeyEvent;

public class KeyHandler {

private Map actions;
private KeyHandler parent;

public boolean keyPressed(KeyEvent event){
	if (performStroke(new KeyStroke(event, true)))
		return true;
	return parent != null && parent.keyPressed(event);
}

public boolean keyReleased(KeyEvent event){
	if (performStroke(new KeyStroke(event, false)))
		return true;
	return parent != null && parent.keyReleased(event);
}

final private boolean performStroke(KeyStroke key) {
	if (actions == null)
		return false;
	IAction action = (IAction)actions.get(key);
	if (action == null)
		return false;
	if (action.isEnabled())
		action.run();
	return true;
}

public void put(KeyStroke stroke, IAction action){
	if (actions == null)
		actions = new HashMap();
	actions.put(stroke, action);
}

public void remove(KeyStroke stroke){
	if (actions != null)
		actions.remove(stroke);
}

public KeyHandler setParent(KeyHandler parent){
	this.parent = parent;
	return this;
}

}