package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An event that occurs as a result of an action being performed.
 */
public class ActionEvent
	extends java.util.EventObject
{

private String actionName;

/**
 * Constructs a new ActionEvent with <i>source</i> as the source of the event.
 * 
 * @param source The source of the event
 */
public ActionEvent(Object source) {
	super(source);
}

/**
 * Constructs a new ActionEvent with <i>source</i> as the source of the event and
 * <i>name</i> as the name of the action that was performed.
 * 
 * @param source The source of the event * @param name The name of the action */
public ActionEvent(Object source, String name) {
	super(source);
	actionName = name;
}

/**
 * Returns the name of the action that was performed.
 * 
 * @return String The name of the action */
public String getActionName() {
	return actionName;
}

}