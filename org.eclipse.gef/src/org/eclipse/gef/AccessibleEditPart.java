package org.eclipse.gef;

import org.eclipse.swt.accessibility.*;

import org.eclipse.draw2d.AccessibleBase;

abstract public class AccessibleEditPart
	extends AccessibleBase
{

abstract public void getChildCount(AccessibleControlEvent e);
abstract public void getChildren(AccessibleControlEvent e);
public void getDefaultAction(AccessibleControlEvent e){}
public void getDescription(AccessibleEvent e){}
public void getKeyboardShortcut(AccessibleEvent e){}
public void getHelp(AccessibleEvent e){}
abstract public void getName(AccessibleEvent e);
abstract public void getLocation(AccessibleControlEvent e);
public void getRole(AccessibleControlEvent e){}
abstract public void getState(AccessibleControlEvent e);
public void getValue(AccessibleControlEvent e){}

}