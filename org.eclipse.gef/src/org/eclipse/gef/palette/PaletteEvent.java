package org.eclipse.gef.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.EventObject;

/**
 * Event sent with palette notifications.
 */
public class PaletteEvent
	extends EventObject
{

private Object entry;

/**
 * PaletteEvent with no entry.
 * @param source java.lang.Object
 */
public PaletteEvent(Object source) {
	super(source);
}

/**
 * PaletteEvent with a entry.
 * @param object java.lang.Object
 * @param entry java.lang.Object
 */
public PaletteEvent(Object object, Object entry) {
	super(object);
	this.entry = entry;
}

/**
 * Return the affected entry.
 * @return java.lang.Object
 */
public Object getEntry() {
	return entry;
}

/** @deprecated */
public Object getSource(){
	return super.getSource();
}
}
