package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


public class ActionEvent
	extends java.util.EventObject
{

private String actionName;

public ActionEvent(Object source){
	super(source);
}

public ActionEvent(Object source, String name){
	super(source);
	actionName = name;
}

public String getActionName(){return actionName;}

}