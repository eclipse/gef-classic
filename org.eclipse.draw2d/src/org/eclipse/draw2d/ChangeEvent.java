package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * An event for property changes.  Includes the source of the event as well as the name of
 * the property that has changed.
 */
public class ChangeEvent
	extends java.util.EventObject
{

private String property;

/**
 * Constructs a new ChangeEvent with the given object as the source of the event.
 * @param source The source of the event
 */
public ChangeEvent(Object source) {
	super(source);
}

/**
 * Constructs a new ChangeEvent with the given source object and property name.
 * @param source The source of the event * @param property The property name */
public ChangeEvent(Object source, String property) {
	super(source);
	setPropertyName(property);
}

/**
 * Returns the name of the property that has changed.
 * @return String the name of the property that has changed */
public String getPropertyName() {
	return property;
}

/**
 * Sets the name of the property that has changed.
 * @param string The property name */
protected void setPropertyName(String string) {
	property = string;
}

}